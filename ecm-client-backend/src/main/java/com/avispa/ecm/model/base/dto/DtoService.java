/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.avispa.ecm.model.base.dto;

import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.type.Type;
import com.avispa.ecm.model.type.TypeService;
import com.avispa.ecm.util.GenericService;
import com.avispa.ecm.util.error.EcmDtoValidator;
import com.avispa.ecm.util.exception.EcmException;
import com.avispa.ecm.util.exception.RepositoryCorruptionError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author Rafał Hiszpański
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DtoService {
    private static final String DTO_OBJECT_NOT_FOUND = "Can't find Dto object for %s type";

    private final DtoRepository dtoRepository;
    private final GenericService genericService;
    private final TypeService typeService;

    private final ObjectMapper objectMapper;
    private final EcmDtoValidator validator;

    /**
     * Converts JSON to Dto object based on the type name and discriminator value if exists. The Dto is validated
     * against JSR-303 specification.
     * @param reader source JSON
     * @param typeName name of the type to which the JSON should be converted
     * @return
     */
    public <D extends Dto> D parse(BufferedReader reader, String typeName) {
        return parse(reader, typeName, null);
    }

    /**
     * Converts JSON to Dto object based on the type name and discriminator value if exists. The Dto is validated
     * against JSR-303 specification. It is possible to enrich the converted Dto with custom data.
     * @param reader source JSON
     * @param typeName name of the type to which the JSON should be converted
     * @param enrichConsumer consumer for enriching the data
     * @return
     */
    public <D extends Dto> D parse(BufferedReader reader, String typeName, Consumer<D> enrichConsumer) {
        Class<? extends EcmObject> entityClass = typeService.getType(typeName).getEntityClass();
        String typeDiscriminatorName = TypeService.getTypeDiscriminatorFromAnnotation(entityClass);

        // convert json to appropriate Dto
        D dto = StringUtils.isNotEmpty(typeDiscriminatorName) ?
                getDtoWithDiscriminator(reader, typeName, typeDiscriminatorName, entityClass) :
                getDtoWithoutDiscriminator(reader, typeName, entityClass);

        // enrich dto with additional details if required
        if(null != enrichConsumer) {
            enrichConsumer.accept(dto);
        }

        // validate dto using JSR-303
        validator.validate(dto);

        return dto;
    }

    @SuppressWarnings("unchecked")
    private <D extends Dto> D getDtoWithDiscriminator(BufferedReader reader, String typeName, String typeDiscriminatorName, Class<? extends EcmObject> entityClass) {
        try {
            // convert json to tree
            JsonNode node = objectMapper.readTree(reader);

            // extract discriminator value
            String value = node.findPath(typeDiscriminatorName).textValue();

            return (D) dtoRepository
                    .findByEntityClassAndDiscriminator(entityClass, value)
                    .map(dtoObject -> {
                        try {
                            return objectMapper.treeToValue(node, dtoObject.getDtoClass());
                        } catch (JsonProcessingException e) {
                            throw new RepositoryCorruptionError("A", e);
                        }
                    })
                    .orElseThrow(() -> new RepositoryCorruptionError(String.format(DTO_OBJECT_NOT_FOUND, typeName)));
        } catch (IOException e) {
            throw new EcmException("Cannot parse request data", e);
        }
    }

    @SuppressWarnings("unchecked")
    private <D extends Dto> D getDtoWithoutDiscriminator(BufferedReader reader, String typeName, Class<? extends EcmObject> entityClass) {
        return  (D) dtoRepository
                .findByEntityClassAndDiscriminatorIsNull(entityClass)
                .map(dtoObject -> {
                    try {
                        return objectMapper.readValue(reader, dtoObject.getDtoClass());
                    } catch (IOException e) {
                        throw new RepositoryCorruptionError("A", e);
                    }
                })
                .orElseThrow(() -> new RepositoryCorruptionError(String.format(DTO_OBJECT_NOT_FOUND, typeName)));
    }

    public DtoObject getDtoObjectFromTypeName(String typeName) {
        return dtoRepository.findByTypeNameAndDiscriminatorIsNull(typeName)
                .orElseThrow(() -> new RepositoryCorruptionError(String.format(DTO_OBJECT_NOT_FOUND, typeName)));
    }

    public DtoObject getDtoObjectFromType(Type type) {
        return dtoRepository.findByTypeAndDiscriminatorIsNull(type)
                .orElseThrow(() -> new RepositoryCorruptionError(String.format(DTO_OBJECT_NOT_FOUND, type.getObjectName())));
    }

    public Dto convertObjectToDto(EcmObject object) {
        return genericService.getService(object.getClass()).getEntityDtoMapper().convertToDto(object);
    }
}

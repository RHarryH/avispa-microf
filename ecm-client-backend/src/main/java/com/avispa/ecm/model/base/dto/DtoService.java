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
    private static final String CANT_PARSE_JSON = "Can't parse the input JSON";

    private final DtoRepository dtoRepository;
    private final GenericService genericService;
    private final TypeService typeService;

    private final ObjectMapper objectMapper;
    private final EcmDtoValidator validator;

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

    /**
     * Converts JSON to Dto object based on the type name and discriminator value if exists. The Dto is validated
     * against JSR-303 specification.
     *
     * @param reader   source JSON
     * @param typeName name of the type to which the JSON should be converted
     * @return Dto object from the JSON
     * @param <D>
     */
    public <D extends Dto> D convert(BufferedReader reader, String typeName) {
        return convert(reader, typeName, null);
    }

    /**
     * Converts JSON to Dto object based on the type name and discriminator value if exists. The Dto is validated
     * against JSR-303 specification. It is possible to enrich the converted Dto with custom data.
     *
     * @param reader         source JSON
     * @param typeName       name of the type to which the JSON should be converted
     * @param enrichConsumer consumer for enriching the data
     * @return Dto object from the JSON
     * @param <D>
     */
    public <D extends Dto> D convert(BufferedReader reader, String typeName, Consumer<D> enrichConsumer) {
        Type type = typeService.getType(typeName);
        String typeDiscriminatorName = TypeService.getTypeDiscriminatorFromAnnotation(type.getEntityClass());

        // convert json to appropriate Dto
        D dto = StringUtils.isNotEmpty(typeDiscriminatorName) ?
                getDtoWithDiscriminator(reader, type, typeDiscriminatorName) :
                getDtoWithoutDiscriminator(reader, type);

        // enrich dto with additional details if required
        if (null != enrichConsumer) {
            enrichConsumer.accept(dto);
        }

        // validate dto using JSR-303
        validator.validate(dto);

        return dto;
    }

    /**
     * Converts JSON to Dto object based on the type name and discriminator value if exists. The Dto is validated
     * against JSR-303 specification.
     *
     * @param jsonNode source JSON in form of parsed tree
     * @param typeName name of the type to which the JSON should be converted
     * @return Dto object from the JSON
     * @param <D>
     */
    public <D extends Dto> D convert(JsonNode jsonNode, String typeName) {
        Type type = typeService.getType(typeName);
        Class<? extends EcmObject> entityClass = typeService.getType(typeName).getEntityClass();
        String typeDiscriminatorName = TypeService.getTypeDiscriminatorFromAnnotation(entityClass);

        // convert json to appropriate Dto
        D dto = StringUtils.isNotEmpty(typeDiscriminatorName) ?
                getDtoWithDiscriminator(jsonNode, type, typeDiscriminatorName) :
                getDtoWithoutDiscriminator(jsonNode, type);

        // validate dto using JSR-303
        validator.validate(dto);

        return dto;
    }

    /**
     * Finds discriminator field in provided JSON, determines Dto configuration and converts JSON to the Dto object
     * pointed by the configuration.
     *
     * @param reader source JSON
     * @param type type which the JSOn represents, it is used to determine Dto configuration
     * @param typeDiscriminatorName name of type discriminator field
     * @return Dto object from the JSON
     * @param <D>
     */
    private <D extends Dto> D getDtoWithDiscriminator(BufferedReader reader, Type type, String typeDiscriminatorName) {
        try {
            // convert json to tree
            JsonNode node = objectMapper.readTree(reader);

            return getDtoWithDiscriminator(node, type, typeDiscriminatorName);
        } catch (IOException e) {
            throw new EcmException("Cannot parse request data", e);
        }
    }

    /**
     * Finds discriminator field in provided JSON, determines Dto configuration and converts JSON to the Dto object
     * pointed by the configuration.
     *
     * @param node source JSON
     * @param type type which the JSOn represents, it is used to determine Dto configuration
     * @param typeDiscriminatorName name of type discriminator field
     * @return Dto object from the JSON
     * @param <D>
     */
    @SuppressWarnings("unchecked")
    private <D extends Dto> D getDtoWithDiscriminator(JsonNode node, Type type, String typeDiscriminatorName) {
        // extract discriminator value
        String value = node.findPath(typeDiscriminatorName).textValue();

        return (D) dtoRepository
                .findByEntityClassAndDiscriminator(type.getEntityClass(), value)
                .map(dtoObject -> {
                    try {
                        return objectMapper.treeToValue(node, dtoObject.getDtoClass());
                    } catch (JsonProcessingException e) {
                        throw new EcmException(CANT_PARSE_JSON, e);
                    }
                })
                .orElseThrow(() -> new RepositoryCorruptionError(String.format(DTO_OBJECT_NOT_FOUND, type.getObjectName())));
    }

    /**
     * Determines Dto configuration and converts JSON to the Dto object pointed by the configuration. Discriminator is
     * not used so always the main Dto will be used.
     *
     * @param reader source JSON
     * @param type type which the JSOn represents, it is used to determine Dto configuration
     * @return Dto object from the JSON
     * @param <D>
     */
    @SuppressWarnings("unchecked")
    private <D extends Dto> D getDtoWithoutDiscriminator(BufferedReader reader, Type type) {
        return (D) dtoRepository
                .findByEntityClassAndDiscriminatorIsNull(type.getEntityClass())
                .map(dtoObject -> {
                    try {
                        return objectMapper.readValue(reader, dtoObject.getDtoClass());
                    } catch (IOException e) {
                        throw new EcmException(CANT_PARSE_JSON, e);
                    }
                })
                .orElseThrow(() -> new RepositoryCorruptionError(String.format(DTO_OBJECT_NOT_FOUND, type.getObjectName())));
    }

    /**
     * Determines Dto configuration and converts JSON to the Dto object pointed by the configuration. Discriminator is
     * not used so always the main Dto will be used.
     *
     * @param node source JSON
     * @param type type which the JSOn represents, it is used to determine Dto configuration
     * @return Dto object from the JSON
     * @param <D>
     */
    @SuppressWarnings("unchecked")
    private <D extends Dto> D getDtoWithoutDiscriminator(JsonNode node, Type type) {
        return (D) dtoRepository
                .findByEntityClassAndDiscriminatorIsNull(type.getEntityClass())
                .map(dtoObject -> {
                    try {
                        return objectMapper.treeToValue(node, dtoObject.getDtoClass());
                    } catch (IOException e) {
                        throw new EcmException(CANT_PARSE_JSON, e);
                    }
                })
                .orElseThrow(() -> new RepositoryCorruptionError(String.format(DTO_OBJECT_NOT_FOUND, type.getObjectName())));
    }
}
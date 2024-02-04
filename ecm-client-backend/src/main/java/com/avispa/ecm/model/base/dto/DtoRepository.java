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

import com.avispa.ecm.model.EcmEntityRepository;
import com.avispa.ecm.model.EcmObject;
import com.avispa.ecm.model.type.Type;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Rafał Hiszpański
 */
@Repository
public interface DtoRepository extends EcmEntityRepository<DtoObject> {
    List<DtoObject> findByType(Type type);

    /**
     * Finds default Dto
     * @param entityClass
     * @return
     */
    @Query("select d from DtoObject d, Type t where d.type = t and t.entityClass = :entityClass and d.discriminator is null")
    Optional<DtoObject> findByEntityClassAndDiscriminatorIsNull(Class<? extends EcmObject> entityClass);

    /**
     * Finds default Dto
     * @param typeName
     * @return
     */
    @Query("select d from DtoObject d, Type t where d.type = t and upper(t.objectName) = upper(:typeName) and d.discriminator is null")
    Optional<DtoObject> findByTypeNameAndDiscriminatorIsNull(String typeName);

    Optional<DtoObject> findByTypeAndDiscriminatorIsNull(Type type);

    @Query("select d from DtoObject d, Type t where d.type = t and t.entityClass = :entityClass and d.discriminator = :discriminator")
    Optional<DtoObject> findByEntityClassAndDiscriminator(Class<? extends EcmObject> entityClass, String discriminator);

    @Query("select d from DtoObject d, Type t where d.type = t and upper(t.objectName) = upper(:typeName) and d.discriminator = :discriminator")
    Optional<DtoObject> findByTypeNameClassAndDiscriminator(String typeName, String discriminator);

    Optional<DtoObject> findByDtoClass(Class<? extends Dto> dtoClass);
}

package com.avispa.microf.model.base.dto;

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
public interface DtoRepository extends EcmEntityRepository<Dto> {
    List<Dto> findByType(Type type);

    /**
     * Finds default/common Dto
     * @param entityClass
     * @return
     */
    @Query("select d from Dto d, Type t where d.type = t and t.entityClass = :entityClass and d.discriminator is null")
    Optional<Dto> findByEntityClassAndDiscriminatorIsNull(Class<? extends EcmObject> entityClass);

    /**
     * Finds default/common Dto
     * @param typeName
     * @return
     */
    @Query("select d from Dto d, Type t where d.type = t and t.objectName = :typeName and d.discriminator is null")
    Optional<Dto> findByTypeNameAndDiscriminatorIsNull(String typeName);

    @Query("select d from Dto d, Type t where d.type = t and t.entityClass = :entityClass and d.discriminator = :discriminator")
    Optional<Dto> findByEntityClassAndDiscriminator(Class<? extends EcmObject> entityClass, String discriminator);

    @Query("select d from Dto d, Type t where d.type = t and t.objectName = :typeName and d.discriminator = :discriminator")
    Optional<Dto> findByTypeNameClassAndDiscriminator(String typeName, String discriminator);
}

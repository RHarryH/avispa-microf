package com.avispa.microf.model.customer;

import com.avispa.ecm.model.EcmEntityRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rafał Hiszpański
 */
@Repository
public interface CustomerRepository extends EcmEntityRepository<Customer> {
}

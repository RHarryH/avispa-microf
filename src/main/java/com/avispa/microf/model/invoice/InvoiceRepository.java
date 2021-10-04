package com.avispa.microf.model.invoice;

import com.avispa.ecm.model.EcmEntityRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rafał Hiszpański
 */
@Repository
public interface InvoiceRepository extends EcmEntityRepository<Invoice> {
}

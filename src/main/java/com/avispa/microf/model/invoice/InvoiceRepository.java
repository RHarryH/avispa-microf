package com.avispa.microf.model.invoice;

import com.avispa.cms.model.CmsEntityRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rafał Hiszpański
 */
@Repository
public interface InvoiceRepository extends CmsEntityRepository<Invoice> {
}

package com.avispa.microf.model.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rafał Hiszpański
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}

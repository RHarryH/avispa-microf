package com.avispa.microf.model.invoice;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Rafał Hiszpański
 */

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
}

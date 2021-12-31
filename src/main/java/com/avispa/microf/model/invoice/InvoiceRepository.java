package com.avispa.microf.model.invoice;

import com.avispa.ecm.model.EcmEntityRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Rafał Hiszpański
 */
@Repository
public interface InvoiceRepository extends EcmEntityRepository<Invoice> {
    @Query("select coalesce(max(i.serialNumber), 0) from Invoice i")
    int findMaxSerialNumber();

    @Query("select coalesce(max(i.serialNumber), 0) from Invoice i where month(i.issueDate) = ?1")
    int findMaxSerialNumberByMonth(int month);
}

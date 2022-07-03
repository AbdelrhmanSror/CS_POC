package com.example.content_server.repository;

import com.example.content_server.models.ocr.ApprovedOcrCustomer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovedCustomerRepository extends OcrCustomerRepository<ApprovedOcrCustomer, Long> {
    @Query(value = "select existence_times from ApprovedOcrCustomer where id_number=:id_number ", nativeQuery = true)
    Integer findRepetitionTimes(@Param("id_number") long id);

}

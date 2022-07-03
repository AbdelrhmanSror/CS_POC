package com.example.content_server.repository;

import com.example.content_server.models.ocr.RejectedOcrCustomer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RejectedOcrCustomerRepository extends CrudRepository<RejectedOcrCustomer, Long> {

    @Query(value = "select existence_times from RejectedOcrCustomer where id_number=:id_number", nativeQuery = true)
    Integer findRepetitionTimes(@Param("id_number") long id);

}

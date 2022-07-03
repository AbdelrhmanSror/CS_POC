package com.example.content_server.repository;

import com.example.content_server.models.ocr.OcrCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface OcrCustomerRepository<T extends OcrCustomer, E extends Long> extends
        CrudRepository<T, E> {

    Integer findRepetitionTimes(@Param("id_number") long id);


}
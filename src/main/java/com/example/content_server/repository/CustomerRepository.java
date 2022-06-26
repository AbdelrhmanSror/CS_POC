package com.example.content_server.repository;

import com.example.content_server.models.OcrCustomer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<OcrCustomer, Long> {

}

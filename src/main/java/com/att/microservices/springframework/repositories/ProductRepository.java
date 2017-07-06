package com.att.microservices.springframework.repositories;

import org.springframework.data.repository.CrudRepository;

import com.att.microservices.springframework.domain.Product;

public interface ProductRepository extends CrudRepository<Product, String> {
}

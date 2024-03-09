package com.microservices.productservice.repository;

import com.microservices.productservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Page<Product> findAll(Pageable pageable);

    //@Override
    //List<Product> findAll();

    Product findProductById(String id);

    Product findDistinctByNameLike(String name);

    List<Product> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Product> findAllByName(String name, Pageable pageable);
    List<Product> findAllByName(String name);
}

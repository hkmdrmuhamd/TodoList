package com.example.todolistbackend.repository;

import com.example.todolistbackend.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

//    Product findByProductName(String productName);


}

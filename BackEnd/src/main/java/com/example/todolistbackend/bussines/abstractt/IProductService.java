package com.example.todolistbackend.bussines.abstractt;

import com.example.todolistbackend.Entity.Product;
import com.example.todolistbackend.utilities.DataResult;
import com.example.todolistbackend.utilities.Result;

import java.util.List;

public interface IProductService {


    DataResult<List<Product>> getAllProduct();
//
//    DataResult<Product> getOneProductById(int productId);

    Result createProduct(Product product);

//    DataResult<Product> getByProducts();

}

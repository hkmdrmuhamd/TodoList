package com.example.todolistbackend.bussines.concretes;

import com.example.todolistbackend.Entity.Product;
import com.example.todolistbackend.bussines.abstractt.IProductService;
import com.example.todolistbackend.repository.ProductRepository;
import com.example.todolistbackend.utilities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ProductManager implements IProductService {
    private final ProductRepository productRepository;
    @Autowired
    public ProductManager(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public DataResult<List<Product>> getAllProduct() {
        List<Product> products = productRepository.findAll();
        products.sort(Comparator.comparing(Product::getId));
        return new SuccesDataResult<List<Product>>("Data getirildi",products);
    }
//
//    @Override
//    public DataResult<Product> getOneProductById(int productId) {
//        Optional<Product> productTest=productRepository.findById(productId);
//        if (productTest.isPresent()){
//            return new SuccesDataResult<Product>("Ürün getirildi",productTest.get());
//        }
//
//        return new ErrorDataResult<Product>("Böyle bir ürün yok",null);
//    }
//
//    @Override
//    public DataResult<Product> getByProductName(String productName) {
//
//        return new SuccesDataResult<Product>
//                ("Veri getirildi",productRepository.findByProductName(productName));
//
//    }

    @Override
    public Result createProduct(Product product) {

        Product productTest = productRepository.save(product);
        if (productTest==null){
            return new ErrorResult("Ürün Eklenemedi !");
        }
        return new SuccessResult("Ürün Başarıyla Eklendi");
    }

}

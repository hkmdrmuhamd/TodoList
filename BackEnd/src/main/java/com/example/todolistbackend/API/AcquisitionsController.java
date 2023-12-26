package com.example.todolistbackend.API;

import com.example.todolistbackend.DTOs.request.CreateProductRequest;
import com.example.todolistbackend.DTOs.response.ProductResponse;
import com.example.todolistbackend.Entity.Product;
import com.example.todolistbackend.bussines.abstractt.IProductService;
import com.example.todolistbackend.repository.ProductRepository;
import com.example.todolistbackend.repository.UserRepository;
import com.example.todolistbackend.security.JwtTokenProvider;
import com.example.todolistbackend.utilities.DataResult;
import com.example.todolistbackend.utilities.ErrorDataResult;
import com.example.todolistbackend.utilities.SuccesDataResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/acquisitions")
public class AcquisitionsController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    public final IProductService productService;
    public final ProductRepository productRepository;
    private final UserRepository userRepository;


    public AcquisitionsController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, IProductService productService, ProductRepository productRepository, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.productService = productService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody CreateProductRequest productRequest, @RequestHeader("Authorization") String token) {
        try {
            String cleanToken = token.replace("Bearer ", "");
            Integer userId = jwtTokenProvider.getUserIdFromJwt(cleanToken);
            String userName = jwtTokenProvider.getUsernameFromJwt(cleanToken);

            if (userRepository.findById(userId).isEmpty() || !userRepository.findById(userId).get().getUserName().equals(userName) ) {
                ProductResponse errorResponse = new ProductResponse();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDataResult<>("Unregistered user",errorResponse));
            }

            ProductResponse productResponse = new ProductResponse();
            Product product = new Product();
            product.setProductName(productRequest.getProductName());
            product.setProductAmount(productRequest.getProductAmount());
            product.setProductUnit(productRequest.getProductUnit());
            product.setCreatedBy(userName);
            product.setUpdatedBy(userName);

            productService.createProduct(product);

            productResponse.setProductId(product.getId());
            productResponse.setProductName(product.getProductName());
            productResponse.setProductAmount(product.getProductAmount());
            productResponse.setProductUnit(product.getProductUnit());
            productResponse.setCreatedBy(product.getCreatedBy());
            productResponse.setUpdatedBy(product.getUpdatedBy());
            productResponse.setCreateDate(product.getCreateDate());
            productResponse.setUpdateDate(product.getUpdateDate());

            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccesDataResult<>("Product addition successful",productResponse));

        } catch (Exception e) {
            ProductResponse errorResponse = new ProductResponse();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDataResult<>("Unauthorized user",errorResponse));
        }
    }

    @GetMapping("/listProducts")
    public ResponseEntity<?> listProducts(@RequestHeader("Authorization") String token){
        try {
            String cleanToken = token.replace("Bearer ", "");
            Integer userId = jwtTokenProvider.getUserIdFromJwt(cleanToken);
            String userName = jwtTokenProvider.getUsernameFromJwt(cleanToken);

            if (userRepository.findById(userId).isEmpty() || !userRepository.findById(userId).get().getUserName().equals(userName) ) {
                ProductResponse errorResponse = new ProductResponse();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDataResult<>("Unregistered user",errorResponse));
            }

            DataResult<List<Product>> result = productService.getAllProduct();
            if (result != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new SuccesDataResult<>("Product list", result.getData()));
            } else {
                ProductResponse errorResponse = new ProductResponse();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDataResult<>("No products found", errorResponse));
            }

        } catch (Exception e) {
            ProductResponse errorResponse = new ProductResponse();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDataResult<>("Unauthorized user",errorResponse));
        }
    }

    @PutMapping("/updateProduct/{productId}")
    public ResponseEntity<?> updateEmployee(@PathVariable("productId") int id, @RequestHeader("Authorization") String token, @RequestBody Product product) {

        try {
            String cleanToken = token.replace("Bearer ", "");
            Integer userId = jwtTokenProvider.getUserIdFromJwt(cleanToken);
            String userName = jwtTokenProvider.getUsernameFromJwt(cleanToken);

            if (userRepository.findById(userId).isEmpty() || !userRepository.findById(userId).get().getUserName().equals(userName) ) {
                ProductResponse errorResponse = new ProductResponse();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDataResult<>("Unregistered user",errorResponse));
            }

            Product productData = productRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found for this id : " + id));

            productData.setProductName(product.getProductName());
            productData.setProductAmount(product.getProductAmount());
            productData.setProductUnit(product.getProductUnit());
            productData.setUpdateDate(LocalDateTime.now());
            productData.setUpdatedBy(userName);

            Product updatedProduct = productRepository.save(productData);
            return ResponseEntity.status(HttpStatus.OK).body(updatedProduct);

        } catch (Exception e) {
            ProductResponse errorResponse = new ProductResponse();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDataResult<>("Unauthorized user",errorResponse));
        }
    }

    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") int id, @RequestHeader("Authorization") String token){
        try {
            String cleanToken = token.replace("Bearer ", "");
            Integer userId = jwtTokenProvider.getUserIdFromJwt(cleanToken);
            String userName = jwtTokenProvider.getUsernameFromJwt(cleanToken);

            if (userRepository.findById(userId).isEmpty() || !userRepository.findById(userId).get().getUserName().equals(userName) ) {
                ProductResponse errorResponse = new ProductResponse();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDataResult<>("Unregistered user",errorResponse));
            }

            productRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found for this id : " + id));

            productRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");

        } catch (Exception e) {
            ProductResponse errorResponse = new ProductResponse();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDataResult<>("Unauthorized user",errorResponse));
        }
    }

}

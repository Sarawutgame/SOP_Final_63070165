package com.example.sop_final_63070165;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

//    public ProductService(ProductRepository productRepository) {
//        this.productRepository = productRepository;
//    }

    @RabbitListener(queues = "AddProductQueue")
    public boolean addProduct(Product product){
        try{
            productRepository.save(product);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    @RabbitListener(queues = "UpdateProductQueue")
    public boolean updateProduct(Product product){
        try {
            productRepository.save(product);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @RabbitListener(queues = "DeleteProductQueue")
    public boolean deleteProduct(Product product){
        try{
            productRepository.delete(product);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @RabbitListener(queues = "GetAllProductQueue")
    public List<Product> getAllProduct(String message){
        try{
//            System.out.println(productRepository.findAll());
//            System.out.println(message);
            return productRepository.findAll();
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }

    @RabbitListener(queues = "GetNameProductQueue")
    public Product getProductByName(String name){
        try{
            return productRepository.findByName(name);
        }catch (Exception e){
            return null;
        }
    }

}

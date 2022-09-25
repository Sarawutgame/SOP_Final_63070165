package com.example.sop_final_63070165;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean serviceAddProduct(@RequestBody Product product){
        try {
            return (boolean) rabbitTemplate.convertSendAndReceive("ProductExchange", "add", product);
        }catch (Exception e){
            return false;
        }
    }
    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean serviceUpdateProduct(@RequestBody Product product){
        try {
            return (boolean) rabbitTemplate.convertSendAndReceive("ProductExchange", "update", product);
        }catch (Exception e){
            return false;
        }
    }
    @RequestMapping(value = "/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public boolean serviceDeleteProduct(@RequestBody Product product){
        try {
            return (boolean) rabbitTemplate.convertSendAndReceive("ProductExchange", "delete", product);
        }catch (Exception e){
            return false;
        }
    }
    @RequestMapping(value = "/getProduct/{name}", method = RequestMethod.GET)
    public Product serviceDeleteProduct(@PathVariable("name") String name){
        try {
            Object out = rabbitTemplate.convertSendAndReceive("ProductExchange", "getname", name);
            return (Product) out;
        }catch (Exception e){
            return null;
        }
    }
    @RequestMapping(value = "/getAllProduct", method = RequestMethod.GET)
    public List<Product> serviceGetAllProduct(){
        try {
            Object out = rabbitTemplate.convertSendAndReceive("ProductExchange", "getall", "GetProduct");
            return (List<Product>) out;
        }catch (Exception e){
            return null;
        }
    }
}

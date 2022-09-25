package com.example.sop_final_63070165;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Route(value = "")
public class ProductView extends VerticalLayout {
    private ComboBox productList;
    private TextField productName;
    private TextField productCost;
    private TextField productProfit;
    private NumberField productPrice;
    private Button b_AddProduct;
    private Button b_UpdateProduct;
    private Button b_DeleteProduct;
    private Button b_ClearProduct;
    private HorizontalLayout layer1;

    private Notification noti;

    private ArrayList<String> productListName;
    private Product atProduct;

    public ProductView() {

        productListName = new ArrayList<>();
        productList = new ComboBox("Product_List");
        productName = new TextField("Product Name:");
        productCost = new TextField("Product Cost:");
        productProfit = new TextField("Product Profit");
        productPrice = new NumberField("Product Price:");
        b_AddProduct = new Button("Add Product");
        b_UpdateProduct = new Button("Update Product");
        b_DeleteProduct = new Button("Delete Product");
        b_ClearProduct = new Button("Clear Product");
        noti = new Notification();

        layer1 = new HorizontalLayout();
        layer1.add(b_AddProduct, b_UpdateProduct, b_DeleteProduct, b_ClearProduct);


        productList.setWidth("600px");
        productList.setItems("Google", "Lunar");
        productName.setWidth("600px");
        productCost.setWidth("600px");
        productProfit.setWidth("600px");
        productPrice.setWidth("600px");
        productPrice.setEnabled(false);
        this.add(productList, productName, productCost, productProfit, productPrice, layer1);
        clearProduct();
        productList();


        b_AddProduct.addClickListener(event -> {
            callPrice();
            try{
                String name = productName.getValue();
                double cost = Double.parseDouble(productCost.getValue());
                double profit = Double.parseDouble(productProfit.getValue());
                double price = productPrice.getValue();
                Product newProduct = new Product(null, name, cost, profit, price);
                boolean status = WebClient.create()
                        .post()
                        .uri("http://127.0.0.1:8080/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Mono.just(newProduct), Product.class)
                        .retrieve().bodyToMono(Boolean.class).block();
                if(status){
                    noti.show("Add complete");
                    productList();
                }
            }catch (Exception e){
                System.out.println(e);
            }
        });

        b_UpdateProduct.addClickListener(event -> {
            callPrice();
            String name = productName.getValue();
            System.out.println(name);
            double cost = Double.parseDouble(productCost.getValue());
            double profit = Double.parseDouble(productProfit.getValue());
            double price = productPrice.getValue();
            Product updateProduct = new Product(atProduct.get_id(), name, cost, profit, price);
            boolean status = WebClient.create()
                    .post()
                    .uri("http://127.0.0.1:8080/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(updateProduct), Product.class)
                    .retrieve().bodyToMono(Boolean.class).block();
            productList();
            productList.setValue(name);
            if(status){

                noti.show("Update complete");
            }

        });

        b_ClearProduct.addClickListener(event -> {
            clearProduct();
            productList();
            noti.show("Clear");
            noti.setDuration(500);
            noti.setPosition(Notification.Position.BOTTOM_START);
        });

        b_DeleteProduct.addClickListener(event -> {


        });

        productCost.addKeyPressListener(Key.ENTER, e ->{
            callPrice();
        });

        productProfit.addKeyPressListener(Key.ENTER, e ->{
            callPrice();
        });

        productList.addValueChangeListener(event ->{
            if(!productList.getValue().equals("")){
                atProduct = WebClient.create().get()
                        .uri("http://127.0.0.1:8080/getProduct/"+ productList.getValue())
                        .retrieve().bodyToMono(Product.class).block();
                productName.setValue(atProduct.getProductName());
                productCost.setValue(atProduct.getProductCost() + "");
                productProfit.setValue(atProduct.getProductProfit()+"");
                productPrice.setValue(atProduct.getProductPrice());
            }

        });

    }

    public void clearProduct(){
        productName.setValue("");
        productCost.setValue("0");
        productProfit.setValue("0");
        productPrice.setValue(0.0);
    }

    public void callPrice(){
        double numCost = Double.parseDouble(productCost.getValue());
        double numProfit = Double.parseDouble(productProfit.getValue());
        double number = WebClient.create().get().uri("http://127.0.0.1:8080/getPrice/"+ numCost +"/"+ numProfit)
                .retrieve().bodyToMono(Double.class).block();
        productPrice.setValue(number);
    }

    public void productList(){
//        ArrayList<Product> list_product = new ArrayList<>();
        ArrayList<Product> productAll = WebClient.create()
                .get()
                .uri("http://127.0.0.1:8080/getAllProduct")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ArrayList<Product>>() {})
                .block();
        productListName.clear();
        for(int i = 0; i < productAll.size(); i++){
            productListName.add(productAll.get(i).getProductName());
        }
        productList.setItems(productListName);

    }

}

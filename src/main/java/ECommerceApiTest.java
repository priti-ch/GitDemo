import LoginRequest.LoginRequest;
import LoginResponse.LoginResponse;
import OrderDetails.OrderDetails;
import Orders.Orders;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class ECommerceApiTest {
    public static void main(String[] args){

        RestAssured.useRelaxedHTTPSValidation();
        RequestSpecification req=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .setContentType(ContentType.JSON).build();

        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setUserEmail("pritiApiTesting@gmail.com");
        loginRequest.setUserPassword("Priti@123");

        RequestSpecification reqLogin=given().log().all().spec(req).body(loginRequest);
        LoginResponse loginResponse=reqLogin.when().post("/api/ecom/auth/login")
                .then().log().all().extract().response()
                .as(LoginResponse.class);
        System.out.println(loginResponse.getToken());
        String token=loginResponse.getToken();
        System.out.println(loginResponse.getUserId());
        String userID=loginResponse.getUserId();

        //Add Product

        RequestSpecification addProductBaseReq=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization",token)
                .build();
        RequestSpecification reqAddProduct=given().log().all().spec(addProductBaseReq)
                .param("productName","Laptop")
                .param("productAddedBy",userID).param("productCategory","fashion")
                .param("productSubCategory","shirts").param("productPrice","11500")
                .param("productDescription","Lenovo").param("productFor","Men")
                .multiPart("productImage",new File("C://Users//Priti_Choudhary2//Pictures//Screenshot 2022-08-03 123716.jpg"));

        String addProductRespose=reqAddProduct.when().post("/api/ecom/product/add-product")
                .then().log().all().extract().response().asString();
        JsonPath js=new JsonPath(addProductRespose);
        String productId=js.get("productId");

    //Create Product
        RequestSpecification createOrderBaseReq=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
                .addHeader("Authorization",token).setContentType(ContentType.JSON)
                .build();

        OrderDetails orderDetails=new OrderDetails();
        orderDetails.setCountry("India");
        orderDetails.setProductOrderedId(productId);

        List<OrderDetails> orderDetailsList=new ArrayList<OrderDetails>();
        orderDetailsList.add(orderDetails);
        Orders orders=new Orders();
        orders.setOrders(orderDetailsList);

        RequestSpecification reqCreateProd=given().log().all().spec(createOrderBaseReq).body(orders);
        String responseAddOrder=reqCreateProd.when().post("/api/ecom/order/create-order")
                .then().log().all().extract().response().asString();
                System.out.println(responseAddOrder);

    //Delete Product
        RequestSpecification deleteOrderBaseReq=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
            .addHeader("Authorization",token)
            .build();

        RequestSpecification reqDeleteProd=given().log().all().spec(deleteOrderBaseReq).pathParam("productId",productId);
        String responseDeleteOrder=reqDeleteProd.when().delete("/api/ecom/product/delete-product/{productId}")
                .then().log().all().extract().response().asString();
        System.out.println(responseDeleteOrder);
        JsonPath js1=new JsonPath(responseDeleteOrder);
        Assert.assertEquals("Product Deleted Successfully",js1.get("message"));



    }
}

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SpecBuilderTest {


    public static void main(String[] args) {

        RestAssured.useRelaxedHTTPSValidation();
        //RestAssured.baseURI = "https://rahulshettyacademy.com";
        AddPlace p = new AddPlace();
        p.setAccuracy(50);
        p.setAddress("29, side layout, cohen 09");
        p.setLanguage("French-IN");
        p.setPhone_number("(+91) 983 893 3937");
        p.setWebsite("https://rahulshettyacademy.com");
        p.setName("Frontline house");
        List<String> myList = new ArrayList();
        myList.add("shoe park");
        myList.add("shop");
        p.setTypes(myList);
        Location l = new Location();
        l.setLat(-38.383494D);
        l.setLng(33.427362D);
        p.setLocation(l);
//        Response res = given().log().all().queryParam("key", new Object[]{"qaclick123"})
//                .body(p).when().post("/maps/api/place/add/json")
//                .then().assertThat().statusCode(200).extract().response();

        RequestSpecification req=new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("Key","qaclick123").setContentType(ContentType.JSON).build();
        ResponseSpecification rspec=new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

        RequestSpecification res=given().spec(req).body(p);
        Response response= res.when().post("/maps/api/place/add/json").then().spec(rspec).extract().response();

        String responseString = response.asString();
        System.out.println(responseString);


        //comment to verify git
        //add something to verify again

        //add comment in dev branch to reflect

        //add one more comment
    }
}

import files.ReUseableMethod;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class Basics {
    public static void main(String[] args) throws IOException {

        //Add Place-> update place with new address -> Get place to validate if new address is present in response

        //content of file to string-> content of file to convert into byte -> Byte data to string

        RestAssured.baseURI="https://rahulshettyacademy.com";
        RestAssured.useRelaxedHTTPSValidation();
        String response=given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
                //.body(inputLoad.AddPlace())
                .body(new String(Files.readAllBytes(Paths.get("D:\\RestAssuredFiles\\AddBook.json"))))
                .when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope",equalTo("APP"))
                .header("server","Apache/2.4.41 (Ubuntu)").extract().response().prettyPrint().toString();

        System.out.println(response);

        JsonPath js=new JsonPath(response); //for parsing json
        String placeid=js.getString("place_id");

        System.out.println(placeid);

        //update place
        String newAddress="Summer walk, Africa";

        given().log().all().queryParam("key","qaclick123").header("Content-Type","application/json")
                .body("{\n" +
                        "\"place_id\":\""+placeid+"\",\n" +
                        "\"address\":\""+newAddress+"\",\n" +
                        "\"key\":\"qaclick123\"\n" +
                        "}\n").
                when().put("maps/api/place/update/json")
                .then().log().all().assertThat().statusCode(200).body("msg",equalTo("Address successfully updated"));

        //get place
        String getPlaceResponse=given().log().all().queryParam("key","qaclick123")
                .queryParam("place_id",placeid)
                .when().get("maps/api/place/get/json")
                .then().log().all().assertThat().statusCode(200).extract().response().prettyPrint().toString();

        JsonPath js1= ReUseableMethod.rawToJson(getPlaceResponse);
        //JsonPath js1=new JsonPath(getPlaceResponse);
        String actualAddress=js1.getString("address");
        System.out.println(actualAddress);

        Assert.assertEquals(actualAddress,newAddress);
        //cucmber Junit, TestNG




    }
}

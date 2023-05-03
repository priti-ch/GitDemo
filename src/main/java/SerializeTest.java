import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojo.AddPlace;
import pojo.Location;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;

public class SerializeTest {


    public static void main(String[] args) {

        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.baseURI = "https://rahulshettyacademy.com";
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
        Response res = given().log().all().queryParam("key", new Object[]{"qaclick123"})
                .body(p).when().post("/maps/api/place/add/json")
                .then().assertThat().statusCode(200).extract().response();
        String responseString = res.asString();
        System.out.println(responseString);

        //comment to verify in git
    }
}

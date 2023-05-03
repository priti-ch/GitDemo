package files;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class DynamicJson {



    @Test(dataProvider = "BooksData")
    public void addBook(String isbn, String aisle){
        RestAssured.baseURI="http://216.10.245.166";
        String response= given().log().all().header("Content-Type","application/json")
                        .body(inputLoad.AddBook(isbn, aisle))
                        .when()
                        .post("/Library/Addbook.php")
                        .then().log().all().assertThat().statusCode(200)
                .extract().response().prettyPrint();
        JsonPath js=ReUseableMethod.rawToJson(response);
        String id=js.get("ID");
        System.out.println(id);

    }

    @DataProvider(name="BooksData")
    public Object[][] getData(){
        return new Object[][] {{"aabbcc","4567"},{"ssddffee","2343"},{"rrffgg","2234"}};
    }


    /*@Test
    public void getbookById(){
        RestAssured.baseURI="http://216.10.245.166";
        //String response=given().log().all().queryParam("ID",id)
                .when()
    }*/

}


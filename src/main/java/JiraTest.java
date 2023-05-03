import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.given;

public class JiraTest {
    public static void main(String[] args){

        RestAssured.baseURI="http://localhost:8090";

        //Login
        SessionFilter session=new SessionFilter();
        String response=given().header("Content-Type","application/json")
                .body("{ \"username\": \"pchoudhary\", \"password\": \"Priti@1234\" }").log().all().filter(session)
                .when().post("rest/auth/1/session")
                .then().log().all().extract().response().prettyPrint().toString();

        String expectedMessage ="Hi How are you?";
        //add comment
        String addCommentResponse = given().pathParam("key","10004").header("Content-Type","application/json")
                .body("{\n" +
                        "    \"body\": \""+expectedMessage+"\",\n" +
                        "    \"visibility\": {\n" +
                        "        \"type\": \"role\",\n" +
                        "        \"value\": \"Administrators\"\n" +
                        "    }\n" +
                        "}").log().all().filter(session)
                .when().post("rest/api/2/issue/{key}/comment")
                .then().log().all().assertThat().statusCode(201).extract().response().prettyPrint().toString();

        JsonPath js=new JsonPath(addCommentResponse);
        String commentId= js.getString("id");


        //add attachment
        //having issue with jira software to add attachment otherwise code is good!
        /*given().header("X-Atlassian-Token", "no-check").filter(session).pathParam("key","10004")
                .header("Content-Type","multipart/form-data")
                .multiPart("file",new File("jira.txt"))
                .when().post("rest/api/2/issue/{key}/attachments")
                .then().log().all().assertThat().statusCode(200);*/

        //Get Issue
        String issueDetails=given().filter(session).pathParam("key", "10004")
                .queryParam("fields", "comment").log().all()
                .when().get("/rest/api/2/issue/{key}")
                .then().log().all().extract().response().prettyPrint().toString();

        System.out.println(issueDetails);
        JsonPath js1 =new JsonPath(issueDetails);
        int commentsCount=js1.getInt("fields.comment.comments.size()");
        for(int i=0;i<commentsCount;i++)
            {
                String commentIdIssue =js1.get("fields.comment.comments["+i+"].id").toString();
                if (commentIdIssue.equalsIgnoreCase(commentId))
                    {
                        String message= js1.get("fields.comment.comments["+i+"].body").toString();
                        System.out.println(message);
                        Assert.assertEquals(message, expectedMessage);
                    }
            }
    }
}


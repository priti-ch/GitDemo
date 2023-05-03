import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

public class oAuthTest {

    public static void main(String[] args) throws InterruptedException {

        String[] courseTitles={"Selenium Webdriver Java","Cypress","Protractor"}; //my own list not same as getCourses

        RestAssured.useRelaxedHTTPSValidation();
        //Example not working as page does not exist
        //parameters will be provided by developers
        // moto of this video is to automate OAuth2.0 to get the access token

        //https://accounts.google.com/o/oauth2/v2/auth/oauthchooseaccount?access_type=offline&client_id=587594460880-u53ikl5ast2sup28098ofsm9iku8vvm6.apps.googleusercontent.com&code_challenge=y2BWtfx-bx-VxLQXyPnBstocFCQ_Onc8Uq0xCgHV7XI&code_challenge_method=S256&include_granted_scopes=true&prompt=select_account%20consent&redirect_uri=https%3A%2F%2Fsso.teachable.com%2Fidentity%2Fcallbacks%2Fgoogle%2Fcallback&response_type=code&scope=email%20https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email%20https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile%20openid%20profile&state=eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJnb29nbGUiLCJpYXQiOjE2NzMxODY1ODcsImV4cCI6MTY3MzE4ODM4NywianRpIjoiYjcyODJjZTEtMjlmMC00NjJlLTk2ZjgtY2YxZTk4ZjVkMzk4IiwiaXNzIjoic2tfeno4dHc2ZGciLCJzdWIiOiJRSUxmS19sbjhFNTJZa2hJM25aRDNnTHJEd29jbm84MUoyNS1kcFNzbEtQTmF3NTY0bGEwQ1ZCby03YzAtM2NISGFuNmIyc1JHU1lBYW0yNmhDTkdEUSJ9.MKqdtKmPF6PuCaR8okTpXwk-hKcaDJf-QNXy19YA2_A&service=lso&o2v=2&flowName=GeneralOAuthFlow
        //String url ="https://rahulshettyacademy.com/getCourse.php?state=verifyfjdss&code=4%2FvAHBQUZU6o4WJ719NrGBzSELBFVBI9XbxvOtYpmYpeV47bFVExkaxWaF_XR14PHtTZf7ILSEeamywJKwo_BYs9M&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&session_state=0c32992f0d47e93d273922018ade42d1072b9d1f..a35c&prompt=none#";

        //to generate new code use below link
        //https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php


        String url="https://rahulshettyacademy.com/getCourse.php?code=4%2F0AWgavdeoMD3CHD99xUS65xVAaQpTsZeLEt8N5NaaC9yZyjNUH8gVp_vvQMwJDQQMZQjw5w&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none=";
        String partialcode=url.split("code=")[1];
        String code=partialcode.split("&scope")[0];
        System.out.println(code);       //get the code from the google auth

        //String code="4%2F0AWgavdd3LOOier4t4PohUBWEMDTtSOPYAKTKsR7-UYM8RQ_ljZLlPWlz8O47BAYRY6g1XA";
        String response =given().urlEncodingEnabled(false).queryParams("code",code)
                .queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("grant_type", "authorization_code")
                .queryParams("state", "verifyfjdss")
                .queryParams("session_state", "ff4a89d1f7011eb34eef8cf02ce4353316d9744b..7eb8")
                // .queryParam("scope", "email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email")
                .queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                .when().log().all()
                .post("https://www.googleapis.com/oauth2/v4/token").asString();

// System.out.println(response);

        JsonPath jsonPath = new JsonPath(response);
        String accessToken = jsonPath.getString("access_token");
        System.out.println(accessToken);        //get the access token

        //using access token hit the main url (i.e. session ID)
        //String r2=    given().contentType("application/json").queryParams("access_token", accessToken).expect().defaultParser(Parser.JSON)
        GetCourse r2=    given().contentType("application/json").queryParams("access_token", accessToken).expect().defaultParser(Parser.JSON)
                .when()
                //.get("https://rahulshettyacademy.com/getCourse.php").asString();
                .get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);

         //now, return type for response is class hence r2 is class type of variable
        //System.out.println(r2);
        System.out.println(r2.getLinkedIn());
        System.out.println(r2.getInstructor());
        System.out.println(r2.getCourses().getApi().get(1).getCourseTitle());

        //print price for perticular title in Api course
        List<Api> apiCourses=r2.getCourses().getApi();
        for(int i=0;i<apiCourses.size();i++){
            if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
            {
                System.out.println(apiCourses.get(i).getPrice());
            }
        }

        //print all courses in web automation course
        //Compare course list with expected list

        ArrayList<String> a=new ArrayList<String>();
        List<WebAutomation> w=r2.getCourses().getWebAutomation();
        for(int i=0;i<w.size();i++){
            System.out.println(w.get(i).getCourseTitle());
            a.add(w.get(i).getCourseTitle());
        }

        List<String> expectedList= Arrays.asList(courseTitles);
        Assert.assertTrue(a.equals(expectedList));




    }


}



import files.inputLoad;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SumValidation {

    @Test
    public void sumOfCourses(){
        int sum=0;
        JsonPath js=new JsonPath(inputLoad.CoursePrice());
        int count=js.getInt("courses.size()");

        for(int i=0; i<count;i++){
            int price= js.getInt("courses["+i+"].price");
            int copies= js.getInt("courses["+i+"].copies");
            sum=sum+(price*copies);
           // System.out.println(sum);
        }

        int totalAmount= js.getInt("dashboard.purchaseAmount");
        //System.out.println(totalAmount);
        Assert.assertEquals(sum,totalAmount);

        int sum1;
        sum1=10+20;

        //sum added

    }
}

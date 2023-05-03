import files.inputLoad;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
    public static void main(String args[]){

        JsonPath js=new JsonPath(inputLoad.CoursePrice());

        //Print no. of courses returned by API
        int count=js.getInt("courses.size()");
        System.out.println(count);

        //Print purchase amount
        int totalAmount= js.getInt("dashboard.purchaseAmount");
        System.out.println(totalAmount);

        //Print title of the first course
        String titleFirstCourse= js.getString("courses[0].title");
        System.out.println(titleFirstCourse);

        //Print All course titles and their respective Prices
        for(int i=0;i<count;i++){
            String courseTitle=js.getString("courses["+i+"].title");
            int price=js.getInt("courses["+i+"].price");
            System.out.println(courseTitle +"\t" + price);
        }

        //Print no of copies sold by RPA Course
        System.out.println("Print no of copies sold by RPA Course");
        for(int i=0;i<count;i++){
            String courseTitle=js.getString("courses["+i+"].title");
            if (courseTitle.equalsIgnoreCase("RPA")){
                System.out.println(js.getInt("courses["+i+"].copies"));
                break;
            }
        }

        //Verify if Sum of all Course prices matches with Purchase Amount
        System.out.println("Verify if Sum of all Course prices matches with Purchase Amount");
        int sum=0;
        for(int i=0; i<count;i++){
            int price= js.getInt("courses["+i+"].price");
            int copies= js.getInt("courses["+i+"].copies");
            sum=sum+(price*copies);
        }
        if(totalAmount==sum){
            System.out.println("Sum " +sum+ " of all Course prices matches with Purchase Amount " +totalAmount );
        }


    }
}

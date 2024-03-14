package examples;
import io.minio.MinioClient;
import org.apache.camel.builder.RouteBuilder;

public class GetEmployeeRoute extends RouteBuilder {



        @Override
        public void configure() throws Exception {/*
            rest("/onboarding/employee")
                    .get("/byMail/{email}")
                    .to("direct:getEmployeeByMail")

                    .get("/all")
                    .to("direct:getAllEmployees")

                    .get("/getProfileImage")
                    .to("direct:getProfileImage");


            from("direct:getAllEmployees")
                    .log("Getting all employees")
                    .to("sql:select * from ba.employees")
                    .process(exchange -> {
                       // Format the response
                        String response = exchange.getIn().getBody().toString();
                        response = response.replace("},", "},\n");
                        exchange.getIn().setBody(response);

                    })
                    .log("All employees: ${body}");

            from("direct:getEmployeeByMail")
                    .log("Getting employee by mail")
                    .to("sql:select * from ba.employees where email = :#email")
                    .process(exchange -> {
                        // Format the response
                        String response = exchange.getIn().getBody().toString();
                        response = response.replace("},", "},\n");
                        exchange.getIn().setBody(response);

                    })
                    .log("Employee by mail: ${body}");

            from("direct:getProfileImage")
                    .log("Getting profile image")
                    //.to("minio:test?operation=getObject&objectName=test.txt")
                    .to("log:profileImage")
                    .log("Profile image: ${body}");

        }


*/
}
}

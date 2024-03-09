package examples;

import org.apache.camel.builder.RouteBuilder;

public class GetEmployeeRoute extends RouteBuilder {

        @Override
        public void configure() throws Exception {
            rest("/employee")
                    .get("/byMail/{mail}")
                    .to("direct:getEmployeeByMail")
            // get all employees
                    .get("/all")
                    .to("direct:getAllEmployees");


            from("direct:getAllEmployees")
                    .log("Getting all employees")
                    .to("sql:select * from employee?dataSource=employeeDS")
                    .log("All employees: ${body}");

        }



}

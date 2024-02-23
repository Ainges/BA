import org.apache.camel.builder.RouteBuilder;
import org.json.JSONObject;

public class RestAPITest extends RouteBuilder {

    private final String testMessage = "Hello back!";


    @Override
    public void configure() throws Exception {


        // return a simple message when the rest API is called
        rest("/test")
                .get("/").produces("text/plain").to("direct:testoverview")
                .get("/hello").produces("text/plain")
                .to("direct:GET_test")
                .get("json").produces("application/json")
                .to("direct:GET_json");

        from("direct:GET_test")
                .setBody().simple(this.testMessage);

        //return a json message when the rest API is called
        from("direct:GET_json")
                // return properly formatted JSON
                .process(exchange -> {

                    String jsonString = new JSONObject()
                            .put("message", "GET successfully called!").toString();
                    System.out.println(jsonString);
                    exchange.getMessage().setBody(jsonString);
                });

        from("direct:testoverview").process(exchange -> {
            String jsonString = new JSONObject()
                    .put("GET ./hello", " returns a string")
                    .put("GET ./json", " returns a JSON object").toString();
            exchange.getMessage().setBody(jsonString);
        });
    }
}

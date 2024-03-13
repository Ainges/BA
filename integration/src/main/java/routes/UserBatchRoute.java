package routes;

import org.apache.camel.builder.RouteBuilder;
import processors.SyncUsersProcessor;

public class UserBatchRoute extends RouteBuilder {

    public void configure() throws Exception {

        from(("timer://userBatch?period=10000"))
                .id("UserBatch-Route")
                .log("User Batch Triggered")
                .process(new SyncUsersProcessor())
                .to("log:userBatchFinished");


    }
}

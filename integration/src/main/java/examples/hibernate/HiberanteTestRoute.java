package examples.hibernate;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class HiberanteTestRoute extends RouteBuilder {
    /**
     * <b>Called on initialization to build the routes using the fluent builder syntax.</b>
     * <p/>
     * This is a central method for RouteBuilder implementations to implement the routes using the Java fluent builder
     * syntax.
     *
     * @throws Exception can be thrown during configuration
     */
    @Override
    public void configure() throws Exception {

        from("timer:test?period=10000")
                .id("Panache-Test-Route")
                .process(new HibernateTestProcessor())
                .log("Test");


    }
}

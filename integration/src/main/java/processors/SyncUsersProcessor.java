package processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class SyncUsersProcessor implements Processor {

    Logger logger = LoggerFactory.getLogger(SyncUsersProcessor.class);
    /**
     * Processes the message exchange
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */
    @Override
    public void process(Exchange exchange) throws Exception {

        logger.info("Syncing users...");

        // Get the database connection of Authority
        String url = ConfigProvider.getConfig().getValue("quarkus.datasource.authority.username", String.class);
        String username = ConfigProvider.getConfig().getValue("quarkus.datasource.authority.username", String.class);
        String password = ConfigProvider.getConfig().getValue("quarkus.datasource.authority.password", String.class);

        Connection connection = DriverManager.getConnection(url, username, password);

        String sql = "SELECT * FROM users";






        // get all Users from Authority (user table + email)
        // ...
        // Compare to local users
        // ...
        // Sync users (update local Users if needed)




    }
}

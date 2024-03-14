package examples;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.compress.utils.IOUtils;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

@RegisterForReflection // Erforderlich für die Verwendung von Reflection in Quarkus
public class MinioRoute extends RouteBuilder {

    Logger logger = LoggerFactory.getLogger(MinioRoute.class);


    @Override
    public void configure() throws Exception {
        // Minio-Konfiguration aus application.properties lesen
        String minioUrl = ConfigProvider.getConfig().getValue("minio.url", String.class);
        String accessKey = ConfigProvider.getConfig().getValue("minio.accessKey", String.class);
        String secretKey = ConfigProvider.getConfig().getValue("minio.secretKey", String.class);

        // Minio-Client erstelle
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();


        rest("/minio")
                .get("/picture/")
                .to("direct:getPicture");

        // Example for getting a picture from Minio
        from("direct:getPicture")
                .process(exchange -> {
                    try (InputStream stream = minioClient.getObject(
                            GetObjectArgs.builder()
                                    .bucket("profilepictures")
                                    .object("angelamartin.png")
                                    .build())) {
                        byte[] imageBytes = IOUtils.toByteArray(stream);
                        exchange.getIn().setBody(imageBytes);
                    }
                })
                .log("Minio Route executed");
    }
}

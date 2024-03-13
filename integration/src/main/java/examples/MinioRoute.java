package examples;

import io.minio.MinioClient;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.builder.RouteBuilder;
import org.eclipse.microprofile.config.ConfigProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

        // Camel Route mit Minio-Client konfigurieren
//        from("timer:myTimer?period=5000") // Beispielhafte Timer-Auslösung alle 5 Sekunden
//            .process(exchange -> {
//                // Beispiel: Minio-Operationen hier ausführen
//                 logger.info(minioClient.listBuckets().toString());
//                // Weitere Informationen finden Sie in der Minio-Dokumentation.
//            })
//            .log("Minio Route executed");
    }
}

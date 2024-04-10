package processors.CanonicalDataModel;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.compress.utils.IOUtils;
import org.eclipse.microprofile.config.ConfigProvider;

import java.io.InputStream;

public class GetProfileFromS3Processor implements Processor {

    /**
     * Processes the message exchange
     *
     * @param exchange the message exchange
     * @throws Exception if an internal processing error has occurred.
     */
    @Override
    public void process(Exchange exchange) throws Exception {

        // Minio-Konfiguration aus application.properties lesen
        String minioUrl = ConfigProvider.getConfig().getValue("minio.url", String.class);
        String accessKey = ConfigProvider.getConfig().getValue("minio.accessKey", String.class);
        String secretKey = ConfigProvider.getConfig().getValue("minio.secretKey", String.class);

        // Minio-Client erstelle
        MinioClient minioClient = MinioClient.builder()
                .endpoint(minioUrl)
                .credentials(accessKey, secretKey)
                .build();

        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket("profilepictures")

                        // Get the picture_file_name from the header
                        .object(exchange.getMessage().getHeader("picture_file_name", String.class))
                        .build())) {
            byte[] imageBytes = IOUtils.toByteArray(stream);
            exchange.getIn().setBody(imageBytes);
        }

    }
}

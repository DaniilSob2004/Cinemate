package com.example.cinemate.service.amazon;

import com.example.cinemate.dto.content.files.TempContentFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Service
public class AmazonS3Service {

    @Value("${amazon_s3.storage_name}")
    private String bucketName;

    @Value("${amazon_s3.credential_name}")
    private String credentialProfile;

    @Value("${amazon_s3.dns_cloudfront_url}")
    private String dnsCloudFrontUrl;

    private S3Client s3Client;

    @PostConstruct
    public void init() {
        // клиент позволяет выполнять действия с S3
        this.s3Client = S3Client.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(ProfileCredentialsProvider.create(credentialProfile))
                .build();

        Logger.info("Initialized Amazon S3 Client");
    }

    public String getCloudFrontUrl(final String keyName) {
        return dnsCloudFrontUrl + keyName.replace(" ", "%20");
    }

    public String extractKeyFromUrl(final String fullUrl) {
        return (fullUrl == null || !fullUrl.startsWith(dnsCloudFrontUrl))
                ? fullUrl
                :fullUrl.substring(dnsCloudFrontUrl.length());
    }

    public void uploadToS3(final TempContentFile file, final String keyName) {
        try {
            // формируется запрос на загрузку объекта
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType(file.getContentType())
                    .build();

            // загружает файл в бакет
            s3Client.putObject(request, RequestBody.fromBytes(
                    file.getContent()
            ));

            Logger.info("Uploaded successfully: " + keyName);
        } catch (Exception e) {
            Logger.error(e.getClass() + " - " + e.getMessage());
        }
    }

    public String uploadAndGenerateKey(final TempContentFile file, final String rootPathPrefix) {
        if (file == null || file.getContent().length == 0) {
            return "";
        }
        String fileKey = rootPathPrefix + "/" + file.getOriginalFilename() + "_" + UUID.randomUUID();
        this.uploadToS3(file, fileKey);
        return fileKey;
    }
}

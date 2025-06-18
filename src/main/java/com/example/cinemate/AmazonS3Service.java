package com.example.cinemate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Paths;
import java.util.List;

@Service
public class AmazonS3Service {

    @Value("${amazon_s3.storage_name}")
    private String bucketName;

    @Value("${amazon_s3.credential_name}")
    private String credentialProfile;

    @Value("${amazon_s3.dns_cloudfront_url}")
    private String dnsCloudFrontUrl;

    public String getCloudFrontUrl(final String objectKey) {
        return dnsCloudFrontUrl + objectKey.replace(" ", "%20");
    }

    public void uploadFile(final String filename) {
        // клиент позволяет выполнять действия с S3
        try (S3Client s3Client = S3Client.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(ProfileCredentialsProvider.create(credentialProfile))  // берёт креды из ~/.aws/credentials
                .build()) {

            // формируется запрос на загрузку объекта
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(filename)
                    .build();

            // загружает файл в бакет
            s3Client.putObject(request, RequestBody.fromFile(
                    Paths.get("src/main/resources/data/", filename)
            ));

            Logger.info("Uploaded successfully: " + filename);
        } catch (Exception e) {
            Logger.error(e.getClass() + " - " + e.getMessage());
        }
    }

    public void uploadFiles(final List<String> filesNames) {
        try (S3Client s3Client = S3Client.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(ProfileCredentialsProvider.create(credentialProfile))
                .build()) {

            for (var fileName : filesNames) {
                PutObjectRequest request = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build();

                s3Client.putObject(request, RequestBody.fromFile(
                        Paths.get("src/main/resources/data/", fileName)
                ));

                Logger.info("Uploaded successfully: " + fileName);
            }

            Logger.info("Uploaded all!");
        } catch (Exception e) {
            Logger.error(e.getClass() + " - " + e.getMessage());
        }
    }
}

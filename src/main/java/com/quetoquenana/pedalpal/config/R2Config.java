package com.quetoquenana.pedalpal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class R2Config {

    @Bean
    public S3Presigner r2Presigner(
            @Value("${cloudflare.r2.endpoint}") String endpoint,
            @Value("${cloudflare.r2.accessKeyId}") String accessKeyId,
            @Value("${cloudflare.r2.secretAccessKey}") String secretAccessKey,
            @Value("${cloudflare.r2.region:auto}") String region
    ) {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        return S3Presigner.builder()
                .endpointOverride(URI.create(endpoint)) // e.g. https://<accountid>.r2.cloudflarestorage.com
                .region(Region.of(region))              // often "auto" works; otherwise "us-east-1"
                .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))
                .build();
    }
}
package com.et.domain;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.StringInputStream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static com.et.constant.AWSConstants.ACCESS_KEY;
import static com.et.constant.AWSConstants.SECRET_KEY;
import static org.assertj.core.api.Assertions.assertThat;

import com.et.web.rest.TestUtil;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActivityLogTest {

    /*@Test
    @Disabled
    public void equalsVerifier() throws Exception {
        AmazonS3 amazonS3 = amazonS3();
        String bucketName = "subscription-exports";
        String key = "abc";
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(bucketName, key);

        InitiateMultipartUploadResult initiateMultipartUploadResult = amazonS3.initiateMultipartUpload(initiateMultipartUploadRequest);

        String uploadId = initiateMultipartUploadResult.getUploadId();


        UploadPartRequest uploadPartRequest = new UploadPartRequest();
        uploadPartRequest.setUploadId(uploadId);
        uploadPartRequest.setBucketName(bucketName);
        uploadPartRequest.setKey(key);
        uploadPartRequest.setPartNumber(1);

        String content = "fedsddsdsf";
        StringInputStream inputStream = new StringInputStream(content);
        uploadPartRequest.setInputStream(inputStream);
        uploadPartRequest.setPartSize(content.length());

        UploadPartResult uploadPartResult = amazonS3.uploadPart(uploadPartRequest);

        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest();
        completeMultipartUploadRequest.setBucketName(bucketName);
        completeMultipartUploadRequest.setUploadId(uploadId);
        completeMultipartUploadRequest.setKey(key);

        List<PartETag> partETags = new ArrayList<>();
        partETags.add(new PartETag(1, uploadPartResult.getETag()));
        completeMultipartUploadRequest.setPartETags(partETags);
        amazonS3.completeMultipartUpload(completeMultipartUploadRequest);
        String a = "b";
    }*/

    public AWSCredentials awsCredentials() {
        AWSCredentials credentials =
            new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY);

        return credentials;
    }

    public AmazonS3 amazonS3() {
        AmazonS3 s3client =
            AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials()))
                .withRegion(Regions.US_WEST_1)
                .build();

        return s3client;
    }
}

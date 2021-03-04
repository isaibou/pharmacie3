package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;

@Configuration
public class AWSS3Config {

	@Value("${aws.acces.key_id}")
	private String accessKeyId;
	
	@Value("${aws.secret.access_key}")
	private String secretAccessId;
	
	@Value("${aws.s3.region}")
	private String region; 
	
	@Bean
	public AmazonS3 getAmazonS3Client() {
		final BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKeyId, secretAccessId);
		
		return AmazonS3ClientBuilder
				.standard()
				.withRegion(region)
				.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
				.build();
	}
	

	}

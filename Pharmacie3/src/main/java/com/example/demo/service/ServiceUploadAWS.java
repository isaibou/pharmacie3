package com.example.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.dao.CommandeOrdonnanceRepository;
import com.example.demo.dao.CommandeRepository;
import com.example.demo.entities.Commande;
import com.example.demo.entities.CommandeOrdonnance;

@Service
public class ServiceUploadAWS {

	@Autowired
	private AmazonS3 amazonS3;
	
	@Value("${aws.s3.bucket}")
	private String s3bucket;
	
	@Autowired
	CommandeOrdonnanceRepository commandeOrdonnanceRepository;
	
	
	public String  uploadFileToS3Bucet(CommandeOrdonnance com ,MultipartFile multipartFile) {
		File  file = convertMultipartFiletoFile(multipartFile);
		
		String orgId = "ORG1";
		String userId = "user1";
		String docCategorie ="education";
		String filename = com.getId().toString()+file.getName();
		
		String path = orgId+"/"+userId+"/"+docCategorie+"/"+filename;
		com.setFileName(filename);
		com.setPath(path);
		commandeOrdonnanceRepository.save(com);
		uploadFile(s3bucket, path, file);
		return "success";
	}
	
	
	
	private void uploadFile(String bucketName , String filePath, File file) {
		amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file)
				.withCannedAcl(CannedAccessControlList.PublicRead) );
	}
	
	
	public File convertMultipartFiletoFile(MultipartFile multipartFile) {
		File file = new File(multipartFile.getOriginalFilename());
		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			outputStream.write(multipartFile.getBytes());
		}
		catch(final IOException e) {
			System.out.println("Error to convert MultipartFile to File" + e.getMessage());
		}
		
		return file;
	}
}

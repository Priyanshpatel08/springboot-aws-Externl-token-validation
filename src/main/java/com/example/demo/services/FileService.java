package com.example.demo.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectInputStream;


@Service
public class FileService {
	Logger logger = LogManager.getLogger(FileService.class);
	

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${s3.bucket.name}")
    private String s3BucketName;

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        final File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes()); 
        } catch (IOException e) {
        	logger.error("Error {} occurred while converting the multipart file"+ e.getLocalizedMessage());
        }
        return file;
    }

    // @Async annotation ensures that the method is executed in a different thread

    @Async
    public S3ObjectInputStream findByName(String fileName) {
    	logger.info("Downloading file with name {}"+ fileName);
        return amazonS3.getObject(s3BucketName, fileName).getObjectContent();
    }

    @Async
    public void save(final MultipartFile multipartFile) {
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            final String fileName =file.getName();
            logger.info("Uploading file with name {}"+ fileName);
            final PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, fileName, file).withCannedAcl(CannedAccessControlList.BucketOwnerFullControl);
            amazonS3.putObject(putObjectRequest);
            Files.delete(file.toPath());
            // Remove the file locally created in the project folder
        } catch (AmazonServiceException e) {
        	logger.error("Error {} occurred while uploading file"+ e.getLocalizedMessage());
        } catch (IOException ex) {
        	logger.error("Error {} occurred while deleting temporary file"+ ex.getLocalizedMessage());
        }
    }
    
    public URL getFileUrl(MultipartFile multipartFile) {
    	final File file = convertMultiPartFileToFile(multipartFile);
        final String fileName =  file.getName();
      return getPreSignedFileUrl(fileName);
    }

	public URL getPreSignedFileUrl(String fileName) {
		
		Date expiration = new Date();
	    // 1 hour expiration period
	    expiration = addHours(expiration, 1);
	    return amazonS3.generatePresignedUrl(s3BucketName, fileName, expiration, HttpMethod.GET);
	}

	  public static Date addHours(Date date, int hours) {
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.add(Calendar.HOUR_OF_DAY, hours);
		    return calendar.getTime();
		  }
    
    

}

package com.example.demo.services;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RecordingService {
	Logger logger = LogManager.getLogger(RecordingService.class);
	
	@Autowired
    FileService fileService;
	

	public URL getFileURL(String fileName) {
		return fileService.getPreSignedFileUrl(fileName);
	}
	
	public URL uplaodFile(MultipartFile multipartFile){
		 fileService.save(multipartFile);
		 return fileService.getFileUrl(multipartFile);
	}
	





}

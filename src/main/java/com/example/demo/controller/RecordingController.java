 package com.example.demo.controller;

 import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.services.RecordingService;

@RestController 
@RequestMapping("/recording")
public class RecordingController {
	
	Logger logger = LogManager.getLogger(RecordingController.class);
	
	@Autowired
	RecordingService recordingService;
	
	@GetMapping("/file")
	public URL getFileURL(@RequestParam(value = "fileName") String fileName){
		return recordingService.getFileURL(fileName);
	}
	
	@PostMapping("/uploadFile")
	public URL uploadfile(@RequestParam(value = "file") MultipartFile multipartFile){
		return recordingService.uplaodFile(multipartFile);
	}
	
	
}

package com.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;


@SpringBootApplication
@RestController
public class JavaWebServiceApplication {

	private String logDir = "/app/log";
	private String logFile = "java-web-service.log";

	private String logPath = logDir + "/" + logFile;

	private FileWriter fileWriter ;

	@PostConstruct
	public void init() {
		try {
			File logDirFile = new File(logDir);
			if (!logDirFile.exists()) {
				logDirFile.mkdirs();
			}
			File logFile = new File(logPath);
			if (!logFile.exists()) {
				logFile.createNewFile();
			}
			fileWriter = new FileWriter(logPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/hello")
	public String hello() {
		// Log request to file
		try {
			fileWriter.write("Request received at " + new Date() + "\n");
			fileWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "Hello World!";
	}

	public static void main(String[] args) {
		SpringApplication.run(JavaWebServiceApplication.class, args);
	}

}

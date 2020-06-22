package com.project.springboot.bestRoute;

import com.project.springboot.bestRoute.service.InitFileService;
import com.project.springboot.bestRoute.service.ConsoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.text.ParseException;

@SpringBootApplication
public class BestRouteApplication {

	private static InitFileService initFileService;
	private static ConsoleService consoleService;

	@Autowired
	public BestRouteApplication(InitFileService initFileService, ConsoleService consoleService) {
		BestRouteApplication.initFileService = initFileService;
		BestRouteApplication.consoleService = consoleService;
	}

	public static void main(String[] args) throws IOException, ParseException {
		SpringApplication.run(BestRouteApplication.class, args);
		initFileService.readInputFile(args);
		consoleService.applicationConsole();
	}
}

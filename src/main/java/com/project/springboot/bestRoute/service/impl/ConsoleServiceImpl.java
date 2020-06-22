package com.project.springboot.bestRoute.service.impl;

import com.project.springboot.bestRoute.mapper.RouteDataMapper;
import com.project.springboot.bestRoute.model.TravelModel;
import com.project.springboot.bestRoute.service.ConsoleService;
import com.project.springboot.bestRoute.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.Scanner;

@Service
@Slf4j
public class ConsoleServiceImpl implements ConsoleService {

    private static final String EXIT = "EXIT";
    private Scanner scanner;

    public ConsoleServiceImpl() {
        scanner = new Scanner(System.in);
    }

    @Autowired
    private RouteService routeService;

    @Override
    public void applicationConsole() {
        String textInput = null;

			while (!EXIT.equalsIgnoreCase(textInput)) {
			    try {
                    if(!ObjectUtils.isEmpty(textInput)) {
                        TravelModel responseModel =
                                routeService.findBestRoute(RouteDataMapper.mapToRouteModel(textInput));
                        printConsoleMessages("Best route for path " + textInput + " found:",
                                responseModel.getCompleteRoute());
                    }
                } catch(Exception e) {
                    log.error("CAUGHT EXCEPTION", e);
                    printConsoleMessages(e.getMessage());
                }
                printConsoleDefaultMessage();
                textInput = scanNextInput();
			}
            printConsoleMessages("Thank you for using our best route application!");
			shutdownApplication();
    }

    @Override
    public String printConsoleMessages(String... messages) {
        Arrays.stream(messages).forEach(this::printAndLog);
        return String.join(" - ", Arrays.asList(messages));
    }

    @Override
    public void printConsoleDefaultMessage() {
        printConsoleMessages("Please enter the desired route or type EXIT to quit application", "Route: ");
    }

    @Override
    public String scanNextInput() {
        return scanner.next();
    }

    @Override
    public void shutdownApplication() {
        printConsoleMessages("Shutting down...");
        System.exit(0);
    }

    private void printAndLog(String text) {
        System.out.println(text);
        log.info("Console output -> " + text);
    }
}

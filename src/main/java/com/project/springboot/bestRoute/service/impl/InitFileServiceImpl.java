package com.project.springboot.bestRoute.service.impl;

import com.project.springboot.bestRoute.repository.RouteRepository;
import com.project.springboot.bestRoute.service.ConsoleService;
import com.project.springboot.bestRoute.service.InitFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.InputMismatchException;

@Service
@Slf4j
public class InitFileServiceImpl implements InitFileService {

    @Autowired
    private RouteRepository routeRepository;

    @Autowired
    private ConsoleService consoleService;

    @Override
    public void readInputFile(String... args) {
        try {
            if (args.length != 1) {
                throw new InputMismatchException(
                        "Application must start with the input complete filepath as sole argument");
            }
            String fileName = args[0];
            File routesFile = new File(fileName);
            if (!routesFile.exists()) {
                throw new FileNotFoundException("File not found from path " + fileName);
            }
            consoleService.printConsoleMessages("Loading routes entry from file...");
            routeRepository.updateRoutes(routesFile);
        } catch (InputMismatchException | IOException | ParseException e) {
            String messages = consoleService.printConsoleMessages(
                    "There was a problem loading data from file!", e.getMessage());
            log.error(messages, e);
            consoleService.shutdownApplication();
        }
    }
}

package com.project.springboot.bestRoute.service;

import java.io.IOException;
import java.text.ParseException;

public interface ConsoleService {
    void applicationConsole() throws IOException, ParseException;

    String scanNextInput();
    String printConsoleMessages(String... messages);
    void printConsoleDefaultMessage();
    void shutdownApplication();
}

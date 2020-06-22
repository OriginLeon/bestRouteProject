package com.project.springboot.bestRoute.service;

import java.io.IOException;
import java.text.ParseException;

public interface InitFileService {
    void readInputFile(String... args) throws IOException, ParseException;
}

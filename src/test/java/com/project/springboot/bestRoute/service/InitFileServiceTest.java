package com.project.springboot.bestRoute.service;

import com.project.springboot.bestRoute.repository.RouteRepository;
import mocks.Mocker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InitFileServiceTest {

    @SpyBean
    InitFileService initFileService ;

    @SpyBean
    ConsoleService consoleService;

    @MockBean
    RouteRepository routeRepositoryMock;

    @BeforeEach
    void beforeEach() {
        doNothing().when(consoleService).shutdownApplication();
    }

    @Test
    public void initValidInputFile() throws Exception {
        doNothing().when(routeRepositoryMock).updateRoutes(any(File.class));
        initFileService.readInputFile(Mocker.getDefaultInputFilePathMock());
        verify(routeRepositoryMock, times(1)).updateRoutes(any(File.class));
        verify(consoleService, times(1)).printConsoleMessages(
                "Loading routes entry from file...");
        verify(consoleService, times(0)).shutdownApplication();
    }

    @Test
    public void initInvalidInputParameterCount() throws Exception {
        doNothing().when(routeRepositoryMock).updateRoutes(any(File.class));
            initFileService.readInputFile(Mocker.getDefaultInputFilePathMock(), "secondParameter");
        verify(routeRepositoryMock, times(0)).updateRoutes(any(File.class));
        verify(consoleService, times(1)).printConsoleMessages(
                "There was a problem loading data from file!",
                "Application must start with the input complete filepath as sole argument");
        verify(consoleService, times(1)).shutdownApplication();
    }

    @Test
    public void initInvalidFilePathParameter() throws Exception {
        doNothing().when(routeRepositoryMock).updateRoutes(any(File.class));
        initFileService.readInputFile("wrong/path/" + Mocker.getDefaultInputFilePathMock());
        verify(routeRepositoryMock, times(0)).updateRoutes(any(File.class));
        verify(consoleService, times(1)).printConsoleMessages(
                "There was a problem loading data from file!",
                "File not found from path wrong/path/" + Mocker.getDefaultInputFilePathMock());
        verify(consoleService, times(1)).shutdownApplication();
    }

    @Test
    public void errorLoadingInputFile() throws Exception {
        IOException expectedException = new IOException("Error parsing datasource text.");
        doThrow(expectedException).when(routeRepositoryMock).updateRoutes(any(File.class));
        initFileService.readInputFile(Mocker.getDefaultInputFilePathMock());
        verify(routeRepositoryMock, times(1)).updateRoutes(any(File.class));
        verify(consoleService, times(1)).printConsoleMessages(
                "There was a problem loading data from file!",
                expectedException.getMessage());
        verify(consoleService, times(1)).shutdownApplication();
    }
}

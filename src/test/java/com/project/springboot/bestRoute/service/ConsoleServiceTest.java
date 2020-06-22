package com.project.springboot.bestRoute.service;

import com.project.springboot.bestRoute.model.RouteModel;
import mocks.Mocker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ConsoleServiceTest {

    @SpyBean
    ConsoleService consoleService;

    @MockBean
    RouteService routeServiceMock;

    @BeforeEach
    void beforeEach() {
        doNothing().when(consoleService).shutdownApplication();
    }

    @Test
    public void applicationConsoleBasicRun() throws Exception {
        doReturn("ABC-DEF", "exit").when(consoleService).scanNextInput();
        doReturn(Mocker.getDefaultTravelModelMock()).when(routeServiceMock).findBestRoute(any(RouteModel.class));
        consoleService.applicationConsole();
        verify(routeServiceMock, times(1)).findBestRoute(any(RouteModel.class));
        verify(consoleService, times(4)).printConsoleMessages(any());
        verify(consoleService, times(1)).printConsoleMessages(
                "Best route for path ABC-DEF found:", "ABC - XYZ - DEF > 8");
        verify(consoleService, times(2)).printConsoleMessages(
                "Please enter the desired route or type EXIT to quit application", "Route: ");
        verify(consoleService, times(1)).printConsoleMessages(
                "Thank you for using our best route application!");
    }

    @Test
    public void applicationConsoleErrorRun() throws Exception {
        Exception expectedException = new IllegalArgumentException(
                "There are no availables routes starting from ABC.");
        doReturn("ABC-DEF", "exit").when(consoleService).scanNextInput();
        doThrow(expectedException).when(routeServiceMock).findBestRoute(any(RouteModel.class));
        consoleService.applicationConsole();
        verify(routeServiceMock, times(1)).findBestRoute(any(RouteModel.class));
        verify(consoleService, times(4)).printConsoleMessages(any());
        verify(consoleService, times(1)).printConsoleMessages(
                expectedException.getMessage());
        verify(consoleService, times(2)).printConsoleMessages(
                "Please enter the desired route or type EXIT to quit application", "Route: ");
        verify(consoleService, times(1)).printConsoleMessages(
                "Thank you for using our best route application!");
    }
}

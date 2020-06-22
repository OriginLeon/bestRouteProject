package com.project.springboot.bestRoute.controller;

import mocks.Mocker;
import com.project.springboot.bestRoute.model.PaidRouteModel;
import com.project.springboot.bestRoute.model.RouteModel;
import com.project.springboot.bestRoute.service.ConsoleService;
import com.project.springboot.bestRoute.service.RouteService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.ObjectUtils;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RouteRestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private RouteService routeServiceMock;

    @MockBean
    private ConsoleService consoleServiceMock;

    static HttpHeaders httpHeaders;

    private final String LOCALHOST_PATH = "http://localhost:";
    private final String BEST_ROUTE_PATH = "/route/best";
    private final String ADD_ROUTE_PATH = "/route/add";

    @BeforeAll
    static void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    public void successfullBestCall() throws Exception {

        when(routeServiceMock.findBestRoute(Mockito.any(RouteModel.class))).thenReturn(
                Mocker.getDefaultTravelModelMock());

        HttpEntity<String> entity = new HttpEntity<String>(Mocker.getDefaultRouteJSONMock(), httpHeaders);

        ResponseEntity<String> response = this.restTemplate.postForEntity(
                LOCALHOST_PATH + port + BEST_ROUTE_PATH, entity, String.class);

        assert(!ObjectUtils.isEmpty(response));
        assert(response.getStatusCode().equals(HttpStatus.OK));
        verify(consoleServiceMock, times(2)).printConsoleMessages(anyString());
        verify(consoleServiceMock, times(1)).printConsoleDefaultMessage();
    }

    @Test
    public void notSuccessfullBestCall() throws Exception {

        doThrow(new IllegalArgumentException("IllegalArgumentException message here")).when(
                routeServiceMock).findBestRoute(Mockito.any(RouteModel.class));

        HttpEntity<String> entity = new HttpEntity<String>(Mocker.getDefaultRouteJSONMock(), httpHeaders);

        ResponseEntity<String> response = this.restTemplate.postForEntity(
                LOCALHOST_PATH + port + BEST_ROUTE_PATH, entity, String.class);

        assert(!ObjectUtils.isEmpty(response));
        assert(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
        verify(consoleServiceMock, times(3)).printConsoleMessages(anyString());
        verify(consoleServiceMock).printConsoleMessages("IllegalArgumentException message here");
        verify(consoleServiceMock, times(1)).printConsoleDefaultMessage();
    }

    @Test
    public void successfullAddCall() throws Exception {

        doNothing().when(routeServiceMock).addRoute(any(PaidRouteModel.class));

        HttpEntity<String> entity = new HttpEntity<String>(Mocker.getDefaultPaidRouteJSONMock(), httpHeaders);

        ResponseEntity<String> response = this.restTemplate.postForEntity(
                LOCALHOST_PATH + port + ADD_ROUTE_PATH, entity, String.class);

        assert(!ObjectUtils.isEmpty(response));
        assert(response.getStatusCode().equals(HttpStatus.OK));
        verify(consoleServiceMock, times(2)).printConsoleMessages(anyString());
        verify(consoleServiceMock, times(1)).printConsoleDefaultMessage();
    }

    @Test
    public void notSuccessfullAddCall() throws Exception {

        doThrow(new IOException("IOException message here")).when(
                routeServiceMock).addRoute(Mockito.any(PaidRouteModel.class));

        HttpEntity<String> entity = new HttpEntity<String>(Mocker.getDefaultPaidRouteJSONMock(), httpHeaders);

        ResponseEntity<String> response = this.restTemplate.postForEntity(
                LOCALHOST_PATH + port + ADD_ROUTE_PATH, entity, String.class);

        assert(!ObjectUtils.isEmpty(response));
        assert(response.getStatusCode().equals(HttpStatus.BAD_REQUEST));
        verify(consoleServiceMock, times(3)).printConsoleMessages(anyString());
        verify(consoleServiceMock).printConsoleMessages("IOException message here");
        verify(consoleServiceMock, times(1)).printConsoleDefaultMessage();
    }
}

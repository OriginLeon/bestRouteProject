package com.project.springboot.bestRoute.controller;

import com.project.springboot.bestRoute.model.PaidRouteModel;
import com.project.springboot.bestRoute.model.RouteModel;
import com.project.springboot.bestRoute.model.TravelModel;
import com.project.springboot.bestRoute.service.ConsoleService;
import com.project.springboot.bestRoute.service.RouteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "RouteRestController")
@RestController
@RequestMapping("/route")
@Slf4j
public class RouteRestController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private ConsoleService consoleService;

    @ApiOperation(value = "Find cheapest route for input origin and destination",
            response = ResponseEntity.class)
    @PostMapping("/best")
    public ResponseEntity<TravelModel> bestRoute(@RequestBody RouteModel routeModel) {
        try {
            consoleService.printConsoleMessages("Received best route call from REST API for processing");
            return ResponseEntity.ok(routeService.findBestRoute(routeModel));
        } catch (Exception e) {
            return exceptionResponseHandling(e);
        } finally {
            consoleService.printConsoleMessages("Finished processing best route call from REST API");
            consoleService.printConsoleDefaultMessage();
        }
    }

    @ApiOperation(value = "Add new route to datasource",
            response = ResponseEntity.class)
    @PostMapping("/add")
    public ResponseEntity addRoute(@RequestBody PaidRouteModel paidRouteModel) throws IllegalArgumentException {
        try {
            consoleService.printConsoleMessages("Received new route data from REST API");
            routeService.addRoute(paidRouteModel);
            return ResponseEntity.ok("Route successfully added");
        } catch (Exception e) {
            return exceptionResponseHandling(e);
        }
         finally {
            consoleService.printConsoleMessages("Finished adding route process from REST API");
            consoleService.printConsoleDefaultMessage();
        }
    }

    private ResponseEntity exceptionResponseHandling(Exception e) {
        String messages = "Ocorreu um erro na chamada do serviço";
        try {
            messages = consoleService.printConsoleMessages(e.getMessage());
            log.error(messages, e);
        }
        finally {
            return new ResponseEntity(messages, HttpStatus.BAD_REQUEST);
        }
    }
}

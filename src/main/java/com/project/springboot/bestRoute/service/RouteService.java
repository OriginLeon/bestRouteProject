package com.project.springboot.bestRoute.service;

import com.project.springboot.bestRoute.model.PaidRouteModel;
import com.project.springboot.bestRoute.model.RouteModel;
import com.project.springboot.bestRoute.model.TravelModel;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.text.ParseException;

public interface RouteService {

    TravelModel findBestRoute(RouteModel routeInput) throws InvalidObjectException, IllegalArgumentException;
    void addRoute(PaidRouteModel route) throws IllegalArgumentException, IOException, ParseException;

}

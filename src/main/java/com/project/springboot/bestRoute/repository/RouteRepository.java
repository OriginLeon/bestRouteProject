package com.project.springboot.bestRoute.repository;

import com.project.springboot.bestRoute.model.GraphModel;
import com.project.springboot.bestRoute.model.LocationNodeModel;
import com.project.springboot.bestRoute.model.PaidRouteModel;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface RouteRepository {
    void updateRoutes(File routesFile) throws IOException, ParseException;
    List<PaidRouteModel> getRoutes();
    GraphModel getRoutesGraph();
    LocationNodeModel getLocationNodeModule(String locationNodeName);
    void addRoute(PaidRouteModel route) throws IOException, ParseException;
}

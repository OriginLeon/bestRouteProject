package com.project.springboot.bestRoute.service.impl;

import com.project.springboot.bestRoute.mapper.RouteDataMapper;
import com.project.springboot.bestRoute.model.*;
import com.project.springboot.bestRoute.repository.RouteRepository;
import com.project.springboot.bestRoute.service.GraphService;
import com.project.springboot.bestRoute.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.text.ParseException;
import java.util.List;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    GraphService graphService;

    @Override
    public TravelModel findBestRoute(RouteModel routeInput) throws InvalidObjectException, IllegalArgumentException {

        RouteDataMapper.validateModel(routeInput);

        List<PaidRouteModel> availableRoutesList = routeRepository.getRoutes();
        validateAvailableOriginsRoutes(routeInput, availableRoutesList);
        validateAvailableDestinationRoutes(routeInput, availableRoutesList);

        graphService.calculateCheapestRouteFromSource(routeRepository.getLocationNodeModule(routeInput.getOrigin()));

        //ROUTES GRAPH WILL ALREADY BE UPDATED WITH RESULTS FROM THE CALCULATION SINCE ALL THE NODES ARE ASSIGNED
        LocationNodeModel resultRouteNodeModule = routeRepository.getRoutesGraph().getLocationNodes().stream().filter(
                LocationNodeModel -> LocationNodeModel.getName().equalsIgnoreCase(
                        routeInput.getDestination())).findFirst().get();

        TravelModel travelModel = RouteDataMapper.mapToTravelModel(
                routeInput, resultRouteNodeModule, availableRoutesList);

        if(!travelModel.isValid()) {
            throw new IllegalArgumentException(
                "There are no availables routes from " + routeInput.getOrigin().toUpperCase() +
                " to " + routeInput.getDestination().toUpperCase() + ".");
        }

        return travelModel;
    }

    @Override
    public void addRoute(PaidRouteModel route) throws IllegalArgumentException, IOException, ParseException {
        RouteDataMapper.validateModel(route);
        List<PaidRouteModel> routesList = routeRepository.getRoutes();
        validateUniqueRoute(route, routesList);
        routeRepository.addRoute(route);
    }

    private void validateUniqueRoute(PaidRouteModel paidRouteInput, List<PaidRouteModel> availableRoutesList)
            throws IllegalArgumentException {
        if(availableRoutesList.stream().map(paidRouteInput::equals).anyMatch(Boolean::valueOf)) {
            throw new IllegalArgumentException(
                    "There's already a route from " + paidRouteInput.getOrigin().toUpperCase() +
                            " to " + paidRouteInput.getDestination().toUpperCase() + " available.");
        };
    }

    private void validateAvailableOriginsRoutes(
            RouteModel routeInput, List<PaidRouteModel> availableRoutes) {
        if(availableRoutes.stream().map(route ->  routeInput.getOrigin().equalsIgnoreCase(
                route.getOrigin())).noneMatch(Boolean::booleanValue)) {
            throw new IllegalArgumentException("There are no availables routes starting from "
                    + routeInput.getOrigin().toUpperCase() + ".");
        };
    }

    private void validateAvailableDestinationRoutes(
            RouteModel routeInput, List<PaidRouteModel> availableRoutes) {
        if(availableRoutes.stream().map(route -> routeInput.getDestination().equalsIgnoreCase(
                route.getDestination())).noneMatch(Boolean::booleanValue)) {
            throw new IllegalArgumentException("There are no availables routes ending in "
                    + routeInput.getDestination().toUpperCase() + ".");
        }
    }
}

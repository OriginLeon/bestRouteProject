package com.project.springboot.bestRoute.mapper;

import com.project.springboot.bestRoute.model.LocationNodeModel;
import com.project.springboot.bestRoute.model.PaidRouteModel;
import com.project.springboot.bestRoute.model.RouteModel;
import com.project.springboot.bestRoute.model.TravelModel;
import org.springframework.util.ObjectUtils;

import java.io.InvalidObjectException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RouteDataMapper {

    private final static String ROUTE_REGEX = "^([A-Za-z]{3}[-][A-Za-z]{3})$";
    private final static String ROUTE_DELIMITER = "-";
    private final static String PAID_ROUTE_REGEX = "^([A-Za-z]{3}[,][A-Za-z]{3}[,][0-9]+([.][0-9]{1,2})?)$";
    private final static String PAID_ROUTE_DELIMITER = ",";
    private final static String LOCATION_REGEX = "^([A-Za-z]{3})$";

    public static RouteModel mapToRouteModel(String text) throws ParseException, InvalidObjectException {
        Iterator<String> modelParametersIterator = parseRouteText(text).iterator();
        return validateModel(RouteModel.routeBuilder()
                .origin(modelParametersIterator.next())
                .destination(modelParametersIterator.next())
                .build());
    }

    public static PaidRouteModel mapToPaidRouteModel(String text) throws ParseException, InvalidObjectException {
        Iterator<String> modelParametersIterator = parsePaidRouteText(text).iterator();
        return validateModel(PaidRouteModel.paidRouteBuilder()
                .origin(modelParametersIterator.next())
                .destination(modelParametersIterator.next())
                .price(new BigDecimal(modelParametersIterator.next()))
                .build());
    }

    public static RouteModel validateModel(RouteModel routeModel) throws InvalidObjectException {
        if(ObjectUtils.isEmpty(routeModel.getOrigin())
                || !Pattern.matches(LOCATION_REGEX, routeModel.getOrigin())) {
            throw new InvalidObjectException("Invalid origin value " + routeModel.getOrigin()
                    + " for model -> " + routeModel.toString() + " - Locations must have ABC pattern.");
        }
        if(ObjectUtils.isEmpty(routeModel.getDestination())
                || !Pattern.matches(LOCATION_REGEX, routeModel.getDestination())) {
            throw new InvalidObjectException("Invalid destination value " + routeModel.getDestination()
                    + " for model -> " + routeModel.toString() + " - Locations must have ABC pattern.");
        }
        if(routeModel.getOrigin().equalsIgnoreCase(routeModel.getDestination())) {
            throw new InvalidObjectException("Invalid route. Origin and destination cannot be the same.");
        }
        return routeModel;
    }

    public static PaidRouteModel validateModel(PaidRouteModel paidRouteModel) throws InvalidObjectException {
        validateModel((RouteModel) paidRouteModel);
        if(ObjectUtils.isEmpty(paidRouteModel.getPrice()) || paidRouteModel.getPrice().doubleValue() <= 0) {
            throw new InvalidObjectException("Invalid price value for model -> " + paidRouteModel.toString());
        }
        return paidRouteModel;
    }

    public static TravelModel mapToTravelModel(RouteModel routeInput, LocationNodeModel resultRouteNodeModule,
                                               List<PaidRouteModel> availableRoutesList) {

        final AtomicReference<String> nextOriginPoint = new AtomicReference(routeInput.getOrigin());
        List<PaidRouteModel> paidRouteModelsResultList = new ArrayList<>();

        for (LocationNodeModel nextNodeModel : resultRouteNodeModule.getCheapestRoute()
                .stream().collect(Collectors.toList())) {
            PaidRouteModel nextModel = availableRoutesList.stream().filter(route ->
                    route.getOrigin().equalsIgnoreCase(nextOriginPoint.get())).filter(route ->
                    route.getDestination().equalsIgnoreCase(nextNodeModel.getName()))
                    .findFirst().orElse(null);
            if(!ObjectUtils.isEmpty(nextModel)) {
                paidRouteModelsResultList.add(nextModel.copy());
                nextOriginPoint.set(nextModel.getDestination());
            }
        };

        paidRouteModelsResultList.add(availableRoutesList.stream().filter(route ->
                route.getOrigin().equalsIgnoreCase(nextOriginPoint.get())).filter(route ->
                route.getDestination().equalsIgnoreCase(routeInput.getDestination())).findFirst().orElse(null));

        return TravelModel.builder().paidRouteList(paidRouteModelsResultList).build();
    }

    private static List<String> parseRouteText(String text) throws ParseException {
        if(!Pattern.matches(ROUTE_REGEX, text)) {
            throw new ParseException("Error parsing route text '" + text +
                    "'. Inputs must have 'ABC-DEF' pattern.", 0);
        }
        return Arrays.asList(text.split(ROUTE_DELIMITER));
    }

    private static List<String> parsePaidRouteText(String text) throws ParseException {
        if(!Pattern.matches(PAID_ROUTE_REGEX, text)) {
            throw new ParseException("Error parsing datasource text '" + text +
                    "'. Route values must have 'ABC,DEF,12.34' pattern.", 0);
        }
        return Arrays.asList(text.split(PAID_ROUTE_DELIMITER));
    }
}

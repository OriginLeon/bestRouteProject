package com.project.springboot.bestRoute.repository.impl;

import com.project.springboot.bestRoute.mapper.RouteDataMapper;
import com.project.springboot.bestRoute.model.GraphModel;
import com.project.springboot.bestRoute.model.LocationNodeModel;
import com.project.springboot.bestRoute.repository.RouteRepository;
import com.project.springboot.bestRoute.model.PaidRouteModel;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class RouteRepositoryImpl implements RouteRepository {

    private File datasourceFile;
    private List<PaidRouteModel> datasource;
    private GraphModel graphModel;
    List<LocationNodeModel> createdNodeModels;

    @Override
    public void updateRoutes(File routesFile) throws IOException, ParseException {
        this.datasourceFile = routesFile;
        updateRoutes();
    }

    @Override
    public List<PaidRouteModel> getRoutes() {
        return datasource;
    }

    @Override
    public GraphModel getRoutesGraph() {
        return graphModel;
    }

    @Override
    public LocationNodeModel getLocationNodeModule(String locationNodeName) {
        AtomicReference<String> atomicLocationNodeName = new AtomicReference<>(locationNodeName);
        return ObjectUtils.isEmpty(createdNodeModels) ? null : createdNodeModels.stream().filter(
                node -> node.getName().equalsIgnoreCase(atomicLocationNodeName.get()))
                .findFirst().orElse(null);
    }

    @Override
    public void addRoute(PaidRouteModel route) throws IOException, ParseException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(datasourceFile, true));
        writer.newLine();
        writer.write(route.toString());
        writer.flush();
        writer.close();
        updateRoutes();
    }

    private void updateRoutes() throws IOException, ParseException {
        updateRoutesDatasource();
        upgradeRoutesGraph();
    }

    private void updateRoutesDatasource() throws IOException, ParseException {
        List<PaidRouteModel> modelList = new ArrayList<>();
        Scanner scanner = new Scanner(datasourceFile);
        while (scanner.hasNextLine()) {
            modelList.add(RouteDataMapper.mapToPaidRouteModel(scanner.nextLine()));
        }
        datasource = modelList;
    }

    private void upgradeRoutesGraph() {

        graphModel = new GraphModel();
        createdNodeModels = new ArrayList<>();

        datasource.stream().forEach(paidRouteModel -> {
                LocationNodeModel originLocation = getLocationNodeModule(paidRouteModel.getOrigin());
                if(ObjectUtils.isEmpty(originLocation)) {
                    originLocation = new LocationNodeModel(paidRouteModel.getOrigin());
                    createdNodeModels.add(originLocation);
                    graphModel.addLocationNode(originLocation);
                }
                LocationNodeModel destinationLocation = getLocationNodeModule(paidRouteModel.getDestination());
                if(ObjectUtils.isEmpty(destinationLocation)) {
                    destinationLocation = new LocationNodeModel(paidRouteModel.getDestination());
                    createdNodeModels.add(destinationLocation);
                    graphModel.addLocationNode(destinationLocation);
                }
                originLocation.addDestination(destinationLocation, paidRouteModel.getPrice());
        });
    }
}

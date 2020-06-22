package com.project.springboot.bestRoute.service.impl;

import com.project.springboot.bestRoute.model.LocationNodeModel;
import com.project.springboot.bestRoute.service.GraphService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

@Service
public class GraphServiceImpl implements GraphService {

    public void calculateCheapestRouteFromSource(LocationNodeModel source) {
        source.setPrice(BigDecimal.ZERO);
        Set<LocationNodeModel> settledLocationNodeModels = new HashSet<>();
        Set<LocationNodeModel> unsettledLocationNodeModels = new HashSet<>();
        unsettledLocationNodeModels.add(source);
        while (unsettledLocationNodeModels.size() != 0) {
            LocationNodeModel currentLocationNodeModel = getLowestDistanceNode(unsettledLocationNodeModels);
            unsettledLocationNodeModels.remove(currentLocationNodeModel);
            for (Map.Entry<LocationNodeModel, BigDecimal> adjacencyPair:
                    currentLocationNodeModel.getAdjacentNodes().entrySet()) {
                LocationNodeModel adjacentLocationNodeModel = adjacencyPair.getKey();
                BigDecimal edgeWeight = adjacencyPair.getValue();
                if (!settledLocationNodeModels.contains(adjacentLocationNodeModel)) {
                    calculateMinimumDistance(adjacentLocationNodeModel, edgeWeight, currentLocationNodeModel);
                    unsettledLocationNodeModels.add(adjacentLocationNodeModel);
                }
            }
            settledLocationNodeModels.add(currentLocationNodeModel);
        }
    }

    private void calculateMinimumDistance(LocationNodeModel evaluationLocationNodeModel,
                                                 BigDecimal edgeWeigh, LocationNodeModel sourceLocationNodeModel) {
        BigDecimal sourceDistance = sourceLocationNodeModel.getPrice();
        if (sourceDistance.add(edgeWeigh).doubleValue() < evaluationLocationNodeModel.getPrice().doubleValue()) {
            evaluationLocationNodeModel.setPrice(sourceDistance.add(edgeWeigh));
            LinkedList<LocationNodeModel> cheapestRoute = new LinkedList<>(sourceLocationNodeModel.getCheapestRoute());
            cheapestRoute.add(sourceLocationNodeModel);
            evaluationLocationNodeModel.setCheapestRoute(cheapestRoute);
        }
    }

    private LocationNodeModel getLowestDistanceNode(Set <LocationNodeModel> unsettledLocationNodeModels) {
        LocationNodeModel lowestDistanceLocationNodeModel = null;
        BigDecimal lowestDistance = BigDecimal.valueOf(Double.MAX_VALUE);
        for (LocationNodeModel LocationNodeModel : unsettledLocationNodeModels) {
            BigDecimal nodeDistance = LocationNodeModel.getPrice();
            if (nodeDistance.doubleValue() < lowestDistance.doubleValue()) {
                lowestDistance = nodeDistance;
                lowestDistanceLocationNodeModel = LocationNodeModel;
            }
        }
        return lowestDistanceLocationNodeModel;
    }
}
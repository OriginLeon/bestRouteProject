package com.project.springboot.bestRoute.model;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class LocationNodeModel {
    private String name;
    private List<LocationNodeModel> cheapestRoute = new LinkedList<>();
    private BigDecimal price = BigDecimal.valueOf(Long.MAX_VALUE);
    Map<LocationNodeModel, BigDecimal> adjacentNodes = new HashMap<>();
    public void addDestination(LocationNodeModel destination, BigDecimal price) {
        adjacentNodes.put(destination, price);
    }
    public LocationNodeModel(String name) {
        this.name = name;
    }
}
package com.project.springboot.bestRoute.model;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class GraphModel {
    private Set<LocationNodeModel> locationNodes = new HashSet<>();
    public void addLocationNode(LocationNodeModel locationNode) {
        locationNodes.add(locationNode);
    }
}
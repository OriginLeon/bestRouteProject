package com.project.springboot.bestRoute.service;

import com.project.springboot.bestRoute.model.LocationNodeModel;

public interface GraphService {
    void calculateCheapestRouteFromSource(LocationNodeModel source);
}

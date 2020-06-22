package com.project.springboot.bestRoute.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;

@Builder(builderMethodName = "routeBuilder")
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
public class RouteModel {

    @ApiModelProperty(notes = "Origin point", name="origin", required=true, example="ABC")
    private String origin;

    @ApiModelProperty(notes = "Destination point", name="destination", required=true, example="DEF")
    private String destination;

    public String toString() {
        return String.join(",", Arrays.asList(origin, destination));
    }

    public boolean equals(RouteModel model) {
        return this.getOrigin().equalsIgnoreCase(model.getOrigin())
                && this.getDestination().equalsIgnoreCase(model.getDestination());
    }
}

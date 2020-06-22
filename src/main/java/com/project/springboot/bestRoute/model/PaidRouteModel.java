package com.project.springboot.bestRoute.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Arrays;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel
@EqualsAndHashCode
public class PaidRouteModel extends RouteModel {

    @ApiModelProperty(notes = "Route cost", name="price", required=true, example="12.34")
    private BigDecimal price;

    @Builder(builderMethodName = "paidRouteBuilder")
    public PaidRouteModel(String origin, String destination, BigDecimal price) {
        super(origin, destination);
        this.price = price;
    }

    public String toString() {
        return String.join(",", Arrays.asList(getOrigin(), getDestination(), String.valueOf(price)));
    }

    public PaidRouteModel copy(){
        return PaidRouteModel.paidRouteBuilder().origin(this.getOrigin()).destination(
                this.getDestination()).price(this.getPrice()).build();
    }
}

package com.project.springboot.bestRoute.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TravelModel {
    List<PaidRouteModel> paidRouteList;

    public String getCompleteRoute() {
        if (!ObjectUtils.isEmpty(paidRouteList)) {
            return paidRouteList.get(0).getOrigin().concat(" - ").concat(
                    (paidRouteList.stream().map(PaidRouteModel::getDestination)
                            .collect(Collectors.joining(" - ")))
                    .concat(" > ".concat(paidRouteList.stream().map(PaidRouteModel::getPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add).toString())));
        }
        return "Incomplete travel route";
    }

    @JsonIgnore
    public boolean isValid() {
        return !ObjectUtils.isEmpty(paidRouteList) && !paidRouteList.contains(null);
    }
}

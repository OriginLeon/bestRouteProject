package com.project.springboot.bestRoute.mapper;

import com.project.springboot.bestRoute.model.PaidRouteModel;
import com.project.springboot.bestRoute.model.RouteModel;
import mocks.Mocker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ObjectUtils;

import java.io.InvalidObjectException;
import java.text.ParseException;

@ExtendWith(MockitoExtension.class)
public class RouteDataMapperTest {

    @Test
    public void successfullMapToRouteModel() throws Exception {
        RouteModel mappedModel = RouteDataMapper.mapToRouteModel(Mocker.getDefaultRouteTEXTMock());
        assert(!ObjectUtils.isEmpty(mappedModel));
        assert(mappedModel.equals(Mocker.getDefaultRouteMock()));
    }

    @Test
    public void notSuccessfullMapToRouteModel() {
        Exception myException = null;
        try {
            RouteDataMapper.mapToRouteModel("ABCD-EFG");
        } catch (Exception e){
            myException = e;
        }
        assert(myException instanceof ParseException);
        assert(myException.getMessage().contains("Error parsing route text 'ABCD-EFG'"));
    }

    @Test
    public void successfullMapToPaidRouteModel() throws Exception {
        PaidRouteModel mappedModel = RouteDataMapper.mapToPaidRouteModel(Mocker.getDefaultPaidRouteTEXTMock());
        assert(!ObjectUtils.isEmpty(mappedModel));
        assert(mappedModel.equals(Mocker.getDefaultPaidRouteMock()));
    }

    @Test
    public void notSuccessfullMapToPaidRouteModel() {
        Exception myException = null;
        try {
            RouteDataMapper.mapToPaidRouteModel("ABC,DEF,");
        } catch (Exception e){
            myException = e;
        }
        assert(myException instanceof ParseException);
        assert(myException.getMessage().contains("Error parsing datasource text 'ABC,DEF,'"));
    }

    @Test
    public void successfullValidateRouteModel() {
        RouteModel validModel = null;
        Exception myException = null;
        try {
            validModel = RouteDataMapper.validateModel(Mocker.getDefaultRouteMock());
        } catch (Exception e){
            myException = e;
        }
        assert(!ObjectUtils.isEmpty(validModel));
        assert(ObjectUtils.isEmpty(myException));
    }

    @Test
    public void validateInvalidOriginRouteModel() {
        RouteModel validModel = null;
        Exception myException = null;
        try {
            validModel = RouteDataMapper.validateModel(Mocker.getRouteModelMockWithValues(
                    "ABCZ", "DEF"));
        } catch (Exception e){
            myException = e;
        }
        assert(ObjectUtils.isEmpty(validModel));
        assert(myException instanceof InvalidObjectException);
        assert(myException.getMessage().contains("Invalid origin value"));
    }

    @Test
    public void validateInvalidDestinationRouteModel() {
        RouteModel validModel = null;
        Exception myException = null;
        try {
            validModel = RouteDataMapper.validateModel(Mocker.getRouteModelMockWithValues(
                    "ABC", "DEFZ"));
        } catch (Exception e){
            myException = e;
        }
        assert(ObjectUtils.isEmpty(validModel));
        assert(myException instanceof InvalidObjectException);
        assert(myException.getMessage().contains("Invalid destination value"));
    }

    @Test
    public void validateSameLocationRouteModel() {
        RouteModel validModel = null;
        Exception myException = null;
        try {
            validModel = RouteDataMapper.validateModel(Mocker.getRouteModelMockWithValues(
                    "ABC", "ABC"));
        } catch (Exception e){
            myException = e;
        }
        assert(ObjectUtils.isEmpty(validModel));
        assert(myException instanceof InvalidObjectException);
        assert(myException.getMessage().contains("Invalid route. Origin and destination cannot be the same."));
    }

    @Test
    public void successfullValidatePaidRouteModel() {
        PaidRouteModel validModel = null;
        Exception myException = null;
        try {
            validModel = RouteDataMapper.validateModel(Mocker.getDefaultPaidRouteMock());
        } catch (Exception e){
            myException = e;
        }
        assert(!ObjectUtils.isEmpty(validModel));
        assert(ObjectUtils.isEmpty(myException));
    }

    @Test
    public void validateInvalidPricePaidRouteModel() {
        PaidRouteModel validModel = null;
        Exception myException = null;
        try {
            validModel = RouteDataMapper.validateModel(Mocker.getPaidRouteModelMockWithValues(
                    "ABC", "DEF", null));
        } catch (Exception e){
            myException = e;
        }
        assert(ObjectUtils.isEmpty(validModel));
        assert(myException instanceof InvalidObjectException);
        assert(myException.getMessage().contains("Invalid price value for model"));
    }
}

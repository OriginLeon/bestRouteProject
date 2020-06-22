package com.project.springboot.bestRoute.service;

import com.project.springboot.bestRoute.model.PaidRouteModel;
import com.project.springboot.bestRoute.model.TravelModel;
import com.project.springboot.bestRoute.repository.RouteRepository;
import mocks.Mocker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RouteServiceTest {

    @SpyBean
    RouteService routeService;

    @SpyBean
    RouteRepository routeRepository;

    TravelModel myModel;
    Exception myException;

    @BeforeEach
    void beforeEach() throws Exception {
        routeRepository.updateRoutes(Mocker.newInputFileMock());
        myModel = null;
        myException = null;
    }

    @Test
    public void successfullFindBestRoute() {
        try {
            myModel = routeService.findBestRoute(Mocker.getDefaultRouteMock());
        } catch (Exception e){
            myException = e;
        }
        assert(ObjectUtils.isEmpty(myException));
        assert(!ObjectUtils.isEmpty(myModel));
        assert(myModel.equals(Mocker.getDefaultTravelModelMock()));
    }

    @Test
    public void notPossibleOriginRoute() {
        try {
            myModel = routeService.findBestRoute(Mocker.getRouteModelMockWithValues("DEF", "XYZ"));
        } catch (Exception e){
            myException = e;
        }
        assert(ObjectUtils.isEmpty(myModel));
        assert(myException instanceof IllegalArgumentException);
        assert(myException.getMessage().contains("There are no availables routes starting from"));
    }

    @Test
    public void notPossibleDestinationRoute() {
        try {
            myModel = routeService.findBestRoute(Mocker.getRouteModelMockWithValues("XYZ", "ABC"));
        } catch (Exception e){
            myException = e;
        }
        assert(ObjectUtils.isEmpty(myModel));
        assert(myException instanceof IllegalArgumentException);
        assert(myException.getMessage().contains("There are no availables routes ending in"));
    }

    @Test
    public void notPossibleBestRoute() {
        try {
            myModel = routeService.findBestRoute(Mocker.getRouteModelMockWithValues("ABC", "JKL"));
        } catch (Exception e){
            myException = e;
        }

        assert(ObjectUtils.isEmpty(myModel));
        assert(myException instanceof IllegalArgumentException);
        assert(myException.getMessage().contains("There are no availables routes from ABC to JKL"));
    }

    @Test
    public void successfullAddRoute() {
        List<PaidRouteModel> paidRouteModelList = routeRepository.getRoutes();
        PaidRouteModel newPaidRouteModel = Mocker.getPaidRouteModelMockWithValues(
                "ABC", "JKL", BigDecimal.TEN);
        try {
            routeService.addRoute(newPaidRouteModel);
            paidRouteModelList = routeRepository.getRoutes();
        } catch (Exception e){
            myException = e;
        }

        assert(ObjectUtils.isEmpty(myException));
        assert(paidRouteModelList.contains(newPaidRouteModel));
    }

    @Test
    public void notSuccessfullAddDuplicateRoute() {
        List<PaidRouteModel> paidRouteModelList = routeRepository.getRoutes();
        int listCount = paidRouteModelList.size();
        try {
            routeService.addRoute(Mocker.getPaidRouteModelMockWithValues(
                    "ABC", "DEF", BigDecimal.ONE));
            paidRouteModelList = routeRepository.getRoutes();
        } catch (Exception e){
            myException = e;
        }

        assert(paidRouteModelList.size() == listCount);
        assert(myException instanceof IllegalArgumentException);
        assert(myException.getMessage().contains("There's already a route from ABC to DEF available"));
    }
}

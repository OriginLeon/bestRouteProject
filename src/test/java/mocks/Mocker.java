package mocks;

import com.project.springboot.bestRoute.model.PaidRouteModel;
import com.project.springboot.bestRoute.model.RouteModel;
import com.project.springboot.bestRoute.model.TravelModel;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Mocker {

    public static String getDefaultRouteTEXTMock() { return "ABC-DEF"; }

    public static String getDefaultPaidRouteTEXTMock() { return "ABC,DEF,10"; }

    public static String getDefaultRouteJSONMock() {
        return getRouteJSONMockWithValues("ABC", "DEF");
    }

    public static String getRouteJSONMockWithValues(String origin, String destination) {
        return "{ \"origin\":\"" + origin + "\", \"destination\":\"" + destination +"\" }";
    }

    public static String getDefaultPaidRouteJSONMock() {
        return getPaidRouteJSONMockWithValues("ABC", "DEF", BigDecimal.TEN);
    }

    public static String getPaidRouteJSONMockWithValues(String origin, String destination, BigDecimal price) {
        return "{ \"origin\":\"" + origin + "\", \"destination\":\"" + destination +
                "\", \"price\":\"" + price + "\" }";
    }

    public static RouteModel getDefaultRouteMock() {
        return getRouteModelMockWithValues("ABC", "DEF");
    }

    public static RouteModel getRouteModelMockWithValues(String origin, String destination) {
        return RouteModel.routeBuilder().origin(origin).destination(destination).build();
    }

    public static PaidRouteModel getDefaultPaidRouteMock() {
        return getPaidRouteModelMockWithValues("ABC", "DEF", BigDecimal.TEN);
    }

    public static PaidRouteModel getPaidRouteModelMockWithValues(String origin, String destination, BigDecimal price) {
        return PaidRouteModel.paidRouteBuilder().origin(origin).destination(destination).price(price).build();
    }

    public static List<PaidRouteModel> getDefaultPaidRouteModelListMock() {
        List<PaidRouteModel> paidRouteModelList = new ArrayList<>();
        paidRouteModelList.add(getPaidRouteModelMockWithValues("ABC", "XYZ", BigDecimal.valueOf(3)));
        paidRouteModelList.add(getPaidRouteModelMockWithValues("XYZ", "DEF", BigDecimal.valueOf(5)));
        return paidRouteModelList;
    }

    public static TravelModel getDefaultTravelModelMock() {
        return TravelModel.builder().paidRouteList(getDefaultPaidRouteModelListMock()).build();
    }

    public static String getDefaultInputFilePathMock() {
        return "src/test/java/mocks/input-routes.csv";
    }

    public static File newInputFileMock() throws IOException {
        File fixedMockFile = new File("src/test/java/mocks/routes-mock.csv");
        File inputFile = new File(getDefaultInputFilePathMock());
        if(inputFile.exists()) inputFile.delete();
        inputFile.createNewFile();
        FileCopyUtils.copy(fixedMockFile, inputFile);
        return inputFile;
    }
}

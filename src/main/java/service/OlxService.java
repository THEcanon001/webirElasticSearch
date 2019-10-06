package service;


import entity.Vehicle;

import java.util.ArrayList;

public final class OlxService {

    private OlxService() {
    }

    public static void loadVehicles(){
        //todo JSOUP
        ArrayList<Vehicle> vehicles = getVehicles();
        //ElasticSearchService.insert(vehicles);
    }

    private static ArrayList<Vehicle> getVehicles() {
        return null;
    }
}

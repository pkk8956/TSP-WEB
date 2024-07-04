package web.services;

import core.io.*;
import core.tsp.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MainPageService extends CostMatrixFromPoints {

    private List<City> cities;
    private List<City> route;
    private List<City> changedRoute;
    private int routeCost;
    private int newRouteCost;
    private long bestTime;
    int[][] costMatrix;
    private int[][] extendedCostMatrix;
    private String methodName;


    {
        try {
            ConfigDAO dao = new ConfigDAO();
            dao.setNumOfPoints(10);
            dao.setNumOfTraveler(1);
            dao.setLoadCapacityForTraveler(50);
            dao.setRestrictionsNeeded(false);

            dao.setMaxStagnation(20);
            dao.setNumGenerations(200);
            dao.setTournamentSize(5);
            dao.setPopulationSize(100);
            dao.setElitismCount(20);
            dao.setMutationRate(0.4);
            newTask();
            this.methodName = "-";
            this.bestTime = 0;
            this.routeCost = 0;
            this.newRouteCost = 0;
            this.changedRoute = new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void newTask() throws IOException {
        ConfigDAO dao = new ConfigDAO();
        this.cities = CityRandomGenerator.generateCities();
        costMatrix = CostMatrixFromPoints.getCostMatrixFromPoints(cities);
        dao.setRestrictionsArray(RestrictionGenerator.generateRestriction(dao));
        dao.setStartPoint(dao.getNumOfPoints());
        RestrictionChecker.setProp();
        this.route = new ArrayList<>();
        this.changedRoute = new ArrayList<>();
        this.routeCost = 0;
        this.newRouteCost = 0;
    }

    public void importTask() throws IOException {
        ConfigDAO dao = new ConfigDAO();
        costMatrix = CostMatrixFromPoints.getCostMatrixFromPoints(cities);
        dao.setStartPoint(dao.getNumOfPoints());
        RestrictionChecker.setProp();
        this.route = new ArrayList<>();
        this.changedRoute = new ArrayList<>();
        this.routeCost = 0;
        this.newRouteCost = 0;
    }

    public boolean tspGaRestriction() throws IOException {
        ConfigDAO dao = new ConfigDAO();
        if (dao.isRestrictionsNeeded() && dao.getNumOfTraveler() < SetUpUtil.calcNecessaryTraveler()){
            return false;
        } else {
            extendedCostMatrix = CostMatrixExtender.extendMatrix(costMatrix);
            TspGaRestrict tspGaRestrict = new TspGaRestrict(extendedCostMatrix);
            setRoute(tspGaRestrict.getBestRoute());
            this.routeCost = tspGaRestrict.getBestTourCost();
            this.changedRoute = new ArrayList<>();
            this.newRouteCost = 0;
            this.methodName = "Genetic algorithm";
            this.bestTime = tspGaRestrict.getWorkTime();
            return true;
        }
    }

    public boolean tspBnbRestriction() throws IOException {
        ConfigDAO dao = new ConfigDAO();
        if (dao.isRestrictionsNeeded() && dao.getNumOfTraveler() < SetUpUtil.calcNecessaryTraveler()){
            return false;
        } else {
            extendedCostMatrix = CostMatrixExtender.extendMatrix(costMatrix);
            TspBnbRestrict tspBnbRestrict = new TspBnbRestrict(extendedCostMatrix);
            setRoute(tspBnbRestrict.getFinal_path());
            this.routeCost = tspBnbRestrict.getFinal_res();
            this.changedRoute = new ArrayList<>();
            this.newRouteCost = 0;
            this.methodName = "Branch and Bound";
            this.bestTime = tspBnbRestrict.getWorkTime();
            return true;
        }
    }
    private void setRoute(List<Integer> bestRoute) {
        this.route = new ArrayList<>(bestRoute.size());
        for (int index : bestRoute) {
            if (index > 0 && index <= cities.size()) {
                this.route.add(cities.get(index - 1));
            }
        }
    }
    public void calculateNewCost(List<Integer> newPath) {
        int totalDistance = 0;
        int size = newPath.size();
        int from = newPath.get(0) - 1;
        for (int i = 1; i < size; i++) {
            int to = newPath.get(i) - 1;
            totalDistance += costMatrix[from][to];
            from = to;
        }
        totalDistance += costMatrix[from][newPath.get(0) - 1];
        setNewRouteCost(totalDistance);
    }
    public void exportResult() throws IOException {
        ConfigDAO dao = new ConfigDAO();
        //TODO Додати красоти звіту
        List<Integer> routeAsIntegers = new ArrayList<>();
        for (City c : route){
            routeAsIntegers.add(c.getCityIndex());
        }
        StringBuilder content = new StringBuilder("Hello " + "\n" +
                "Num of points: " + dao.getNumOfPoints() + "\n" +
                "Num of traveler: " + dao.getNumOfTraveler() + "\n");
        if (dao.isRestrictionsNeeded()){
            content.append("Max load for traveler: ").append(dao.getLoadCapacityForTraveler()).append("\n");
            content.append("With restrictions: ").append(Arrays.toString(dao.getRestrictionsArray())).append("\n");
        }
        content.append("Optimal route:")
                .append(routeAsIntegers).append("\n");
        if (methodName.equals("Genetic algorithm")){
            content.append("Method: ").append(methodName).append("\n");
            content.append("MutationRate: ").append(dao.getMutationRate()).append("\n");
            content.append("NumGenerations: ").append(dao.getNumGenerations()).append("\n");
            content.append("MaxStagnation: ").append(dao.geMaxStagnation()).append("\n");
            content.append("PopulationSize: ").append(dao.getPopulationSize()).append("\n");
            content.append("TournamentSize: ").append(dao.getTournamentSize()).append("\n");
            content.append("ElitismCount: ").append(dao.getElitismCount()).append("\n");
        } else {
            content.append("Method: ").append(methodName).append("\n");
        }
        content.append("Elapsed time: ").append(String.format("%,6.3f ms\n", bestTime / 1_000_000.0)).append("\n")
                .append("Bye bye");
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("report.txt"));
            writer.write(String.valueOf(content));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public int getRouteCost(){
        return routeCost;
    }
    public List<City> getCities(){
        return cities;
    }
    public List<City> getRoute(){
        return route;
    }
    public List<City> getChangedRoute() {
        return changedRoute;
    }
    public void setChangedRoute(List<City> changedRoute) {
        this.changedRoute = changedRoute;
    }
    public int getNewRouteCost() {
        return newRouteCost;
    }
    public void setNewRouteCost(int newRouteCost) {
        this.newRouteCost = newRouteCost;
    }
    public String getMethodName() {
        return methodName;
    }
    public long getBestTime() {
        return bestTime;
    }
    public boolean isRes(){
        List<Integer> routeAsIntegers = new ArrayList<>();
        for (City c : cities){
            routeAsIntegers.add(c.getCityIndex());
        }
        return RestrictionChecker.checkRoute(routeAsIntegers);
    }
    public void setCities(List<City> cities) {
        this.cities = cities;
    }
}

package core.io;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConfigDAO {

    private final Properties prop;

    public ConfigDAO() throws IOException {
        InputStream input = new FileInputStream("src//main//resources//configs//config.properties");
        prop = new Properties();
        prop.load(input);
    }

    //Getters for GA configs
    public int getPopulationSize(){
        return Integer.parseInt(prop.getProperty("GA_PopulationSize"));
    }
    public int getElitismCount(){
        return Integer.parseInt(prop.getProperty("GA_ElitismCount"));
    }
    public double getMutationRate(){
        return Double.parseDouble(prop.getProperty("GA_MutationRate"));
    }
    public int getTournamentSize(){
        return Integer.parseInt(prop.getProperty("GA_TournamentSize"));
    }
    public int getNumGenerations(){
        return Integer.parseInt(prop.getProperty("GA_NumGenerations"));
    }
    public int geMaxStagnation(){
        return Integer.parseInt(prop.getProperty("GA_MaxStagnation"));
    }

    //Getters for TSP configs
    public int getStartPoint(){
        return Integer.parseInt(prop.getProperty("StartPoint"));
    }
    public int getLoadCapacityForTraveler(){
        return Integer.parseInt(prop.getProperty("LoadCapacityForTraveler"));
    }
    public int getNumOfPoints(){
        return Integer.parseInt(prop.getProperty("NumOfPoints"));
    }
    public int getNumOfTraveler(){
        return Integer.parseInt(prop.getProperty("NumOfTraveler"));
    }
    public boolean isRestrictionsNeeded(){
        return Boolean.parseBoolean(prop.getProperty("isRestrictionsNeeded"));
    }

    public int[] getRestrictionsArray(){
        String[] buf = prop.getProperty("RestrictionsArray").replaceAll("[\\[\\],]", " ").trim().split(" ");
        int[] restrictions = new int[getNumOfPoints()];
        int count = 0;
        for (String s : buf) {
            if (!s.equals("")) {
                restrictions[count] = Integer.parseInt(s.trim());
                count++;
            }
        }
        return restrictions;
    }

    //Getters for generators configs
    public int getMinRestriction(){
        return Integer.parseInt(prop.getProperty("minRestriction"));
    }
    public int getMaxRestriction(){
        return Integer.parseInt(prop.getProperty("maxRestriction"));
    }
    public int getMaxXCoordinate(){
        return Integer.parseInt(prop.getProperty("maxXCoordinate"));
    }
    public int getMinXCoordinate(){
        return Integer.parseInt(prop.getProperty("minXCoordinate"));
    }
    public int getMaxYCoordinate(){
        return Integer.parseInt(prop.getProperty("maxYCoordinate"));
    }
    public int getMinYCoordinate(){
        return Integer.parseInt(prop.getProperty("minYCoordinate"));
    }

    //Setters for TSP configs
    public void setStartPoint(int StartPoint) throws IOException {
        prop.setProperty("StartPoint", String.valueOf(StartPoint));
        try (FileWriter output = new FileWriter("src//main//resources//configs//config.properties")) {
            prop.store(output, null);
        }
    }
    public void setRestrictionsNeeded(boolean isRestrictionsNeeded) throws IOException {
        prop.setProperty("isRestrictionsNeeded", String.valueOf(isRestrictionsNeeded));
        try (FileWriter output = new FileWriter("src//main//resources//configs//config.properties")) {
            prop.store(output, null);
        }
    }
    public void setLoadCapacityForTraveler(int LoadCapacityForTraveler) throws IOException {
        prop.setProperty("LoadCapacityForTraveler", String.valueOf(LoadCapacityForTraveler));
        try (FileWriter output = new FileWriter("src//main//resources//configs//config.properties")) {
            prop.store(output, null);
        }
    }
    public void setNumOfPoints(int NumOfPoints) throws IOException {
        prop.setProperty("NumOfPoints", String.valueOf(NumOfPoints));
        try (FileWriter output = new FileWriter("src//main//resources//configs//config.properties")) {
            prop.store(output, null);
        }
    }
    public void setNumOfTraveler(int NumOfTraveler) throws IOException {
        prop.setProperty("NumOfTraveler", String.valueOf(NumOfTraveler));
        try (FileWriter output = new FileWriter("src//main//resources//configs//config.properties")) {
            prop.store(output, null);
        }
    }
    public void setRestrictionsArray(int[] RestrictionsArray) throws IOException {
        String arrayAsString = Arrays.stream(RestrictionsArray)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
        prop.setProperty("RestrictionsArray", arrayAsString);
        try (FileWriter output = new FileWriter("src//main//resources//configs//config.properties")) {
            prop.store(output, null);
        }
    }

    //Setters for GA configs
    public void setPopulationSize(int PopulationSize) throws IOException {
        prop.setProperty("GA_PopulationSize", String.valueOf(PopulationSize));
        try (FileWriter output = new FileWriter("src//main//resources//configs//config.properties")) {
            prop.store(output, null);
        }
    }
    public void setElitismCount(int ElitismCount) throws IOException {
        prop.setProperty("GA_ElitismCount", String.valueOf(ElitismCount));
        try (FileWriter output = new FileWriter("src//main//resources//configs//config.properties")) {
            prop.store(output, null);
        }
    }
    public void setMutationRate(double MutationRate) throws IOException {
        prop.setProperty("GA_MutationRate", String.valueOf(MutationRate));
        try (FileWriter output = new FileWriter("src//main//resources//configs//config.properties")) {
            prop.store(output, null);
        }
    }
    public void setTournamentSize(int TournamentSize) throws IOException {
        prop.setProperty("GA_TournamentSize", String.valueOf(TournamentSize));
        try (FileWriter output = new FileWriter("src//main//resources//configs//config.properties")) {
            prop.store(output, null);
        }
    }
    public void setNumGenerations(int NumGenerations) throws IOException {
        prop.setProperty("GA_NumGenerations", String.valueOf(NumGenerations));
        try (FileWriter output = new FileWriter("src//main//resources//configs//config.properties")) {
            prop.store(output, null);
        }    }
    public void setMaxStagnation(int MaxStagnation) throws IOException {
        prop.setProperty("GA_MaxStagnation", String.valueOf(MaxStagnation));
        try (FileWriter output = new FileWriter("src//main//resources//configs//config.properties")) {
            prop.store(output, null);
        }    }

    //Getters for generators configs
    public void setMinRestriction(int minRestriction) throws IOException {
        prop.setProperty("minRestriction", String.valueOf(minRestriction));
        try (FileWriter output = new FileWriter("src//main//resources//configs//config.properties")) {
            prop.store(output, null);
        }    }
    public void setMaxRestriction(int maxRestriction) throws IOException {
        prop.setProperty("maxRestriction", String.valueOf(maxRestriction));
        try (FileWriter output = new FileWriter("src//main//resources//configs//config.properties")) {
            prop.store(output, null);
        }    }

}

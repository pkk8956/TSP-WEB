package core;

import core.io.*;
import core.tsp.TspBnbRestrict;
import core.tsp.TspGaRestrict;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static core.io.CityRandomGenerator.*;
import static core.io.CostMatrixFromPoints.getCostMatrixFromPoints;
import static core.io.RestrictionGenerator.generateRestriction;
import static core.tsp.CostMatrixExtender.extendMatrix;

public class AutoMain {

    private static final ConfigDAO dao;
    private static final int numOfTry;
    private static final int startNumOfPoints;
    private static final int endNumOfPoints;
    private static final int maxNumOfTraveler;

    static {
        try {
            dao = new ConfigDAO();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        numOfTry = 20;
        startNumOfPoints = 15;
        endNumOfPoints = 20;
        maxNumOfTraveler = 4;
        try {
            dao.setRestrictionsNeeded(false);
            dao.setNumOfTraveler(3);
            dao.setMutationRate(0.3);
            dao.setTournamentSize(10);
            dao.setPopulationSize(100);
            dao.setElitismCount(30);
            dao.setNumGenerations(500);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws IOException {
//        exp1();
//        exp2();
//        exp4();
//        exp5();
//        exp6();
//        exp7();
//        exp8();
    }
    public static void exp1() throws IOException {



        for (int i = startNumOfPoints; i <= endNumOfPoints; i+=10) {
            long[] bnBWorkTimes = new long[numOfTry];
            long[] GAWorkTimes = new long[numOfTry];
            double[] relativeError = new double[numOfTry];
            dao.setNumOfPoints(i);
            dao.setNumOfTraveler(1);

            System.out.println(Color.GREEN_BOLD + "==== Points: " + i + " ====" + Color.RESET);

            for (int j = 0; j < numOfTry; j++) {
                int[][] costMatrix = extendMatrix(getCostMatrixFromPoints(Collections.unmodifiableList(generateCities())));
                dao.setStartPoint(i);

                TspGaRestrict GA = new TspGaRestrict(costMatrix);
                double routeCostGA = GA.getBestTourCost();
                GAWorkTimes[j] = GA.getWorkTime();

//                TspBnbRestrict BnB = new TspBnbRestrict(costMatrix);
//                double routeCostBnB = BnB.getFinal_res();
//                bnBWorkTimes[j] = BnB.getWorkTime();
//
//                relativeError[j] = Math.abs(routeCostGA - routeCostBnB) / routeCostBnB * 100;
            }
            System.out.printf("B&B Average time: %,6.3f ms", calculateAverage(bnBWorkTimes) /1_000_000.0);
            System.out.printf("\nGA Average time: %,6.3f ms", calculateAverage(GAWorkTimes) /1_000_000.0);
            System.out.println("\nRelative error: " + calculateAverage(relativeError));
        }
    }
    public static void exp2() throws IOException {
        for (int i = startNumOfPoints; i <= endNumOfPoints; i++) {
            long[] bnBWorkTimes = new long[numOfTry];
            long[] GAWorkTimes = new long[numOfTry];
            double[] relativeError = new double[numOfTry];
            dao.setNumOfPoints(i);
            System.out.println(Color.GREEN_BOLD + "==== Points: " + i + " ====" + Color.RESET);

            for (int k = 4; k <= maxNumOfTraveler; k++) {
                dao.setNumOfTraveler(k);
                System.out.println(Color.BLUE_BOLD_BRIGHT + "==== Travelers: " + k + " ====" + Color.RESET);

                for (int j = 0; j < numOfTry; j++) {

                    int[][] costMatrix = extendMatrix(getCostMatrixFromPoints(Collections.unmodifiableList(generateCities())));
                    dao.setStartPoint(i);

//                    TspGaRestrict GA = new TspGaRestrict(costMatrix);
//                    double routeCostGA = GA.getBestTourCost();
//                    GAWorkTimes[j] = GA.getWorkTime();

                    TspBnbRestrict BnB = new TspBnbRestrict(costMatrix);
                    double routeCostBnB = BnB.getFinal_res();
                    bnBWorkTimes[j] = BnB.getWorkTime();

//                    relativeError[j] = Math.abs(routeCostGA - routeCostBnB) / routeCostBnB * 100;
                }
                System.out.printf("B&B Average time: %,6.3f ms", calculateAverage(bnBWorkTimes) /1_000_000.0);
                System.out.printf("\nGA Average time: %,6.3f ms", calculateAverage(GAWorkTimes) /1_000_000.0);
                System.out.println("\nRelative error: " + calculateAverage(relativeError));

            }
        }
    }
    public static void exp4() throws IOException {


        for (int i = startNumOfPoints; i <= endNumOfPoints; i++) {
            long[] bnBWorkTimes = new long[numOfTry];
            long[] GAWorkTimes = new long[numOfTry];
            double[] relativeError = new double[numOfTry];
            dao.setNumOfPoints(i);
            dao.setRestrictionsNeeded(true);
            dao.setNumOfTraveler(3);


            System.out.println(Color.GREEN_BOLD + "==== Points: " + i + " ====" + Color.RESET);

            for (int j = 0; j < numOfTry; j++) {
                int[][] costMatrix = extendMatrix(getCostMatrixFromPoints(Collections.unmodifiableList(generateCities())));
                dao.setStartPoint(i);
                dao.setRestrictionsArray(RestrictionGenerator.generateRestriction(dao));
                dao.setLoadCapacityForTraveler(calcLoad(3));

                TspGaRestrict GA = new TspGaRestrict(costMatrix);
                double routeCostGA = GA.getBestTourCost();
                GAWorkTimes[j] = GA.getWorkTime();

                TspBnbRestrict BnB = new TspBnbRestrict(costMatrix);
                double routeCostBnB = BnB.getFinal_res();
                bnBWorkTimes[j] = BnB.getWorkTime();

                relativeError[j] = Math.abs(routeCostGA - routeCostBnB) / routeCostBnB * 100;
            }
            System.out.printf("B&B Average time: %,6.3f ms", calculateAverage(bnBWorkTimes) /1_000_000.0);
            System.out.printf("\nGA Average time: %,6.3f ms", calculateAverage(GAWorkTimes) /1_000_000.0);
            System.out.println("\nRelative error: " + calculateAverage(relativeError));
        }
    }
    public static void exp5() throws IOException {

        dao.setNumOfPoints(100);
        dao.setStartPoint(100);
        dao.setRestrictionsNeeded(true);
        int[][] costMatrix = extendMatrix(getCostMatrixFromPoints(Collections.unmodifiableList(generateCities())));


        for (double i = 0; i < 1; i+=0.1) {
            dao.setMutationRate(i);
            System.out.println(Color.RED + "==== Mutation rate: " + i + " ====" + Color.RESET);

            int[] routeCosts = new int[numOfTry];
            long[] GAWorkTimes = new long[numOfTry];

            for (int j = 0; j < numOfTry; j++) {

                dao.setRestrictionsArray(RestrictionGenerator.generateRestriction(dao));
                dao.setLoadCapacityForTraveler(calcLoad(3));

                TspGaRestrict GA = new TspGaRestrict(costMatrix);
                routeCosts[j] = GA.getBestTourCost();
                GAWorkTimes[j] = GA.getWorkTime();


            }
            System.out.printf("\nGA Average time: %,6.3f ms", calculateAverage(GAWorkTimes) /1_000_000.0);
            System.out.println("\nBest route cost: " + Arrays.stream(routeCosts).min());
            System.out.println("\nAverage route cost: " + Arrays.stream(routeCosts).average());

        }






    }
    public static void exp6() throws IOException {

        dao.setNumOfPoints(100);
        dao.setStartPoint(100);
        dao.setRestrictionsNeeded(true);
        int[][] costMatrix = extendMatrix(getCostMatrixFromPoints(Collections.unmodifiableList(generateCities())));


        for (int i = 1000; i < 1050; i+=50) {
            dao.setPopulationSize(i);
            dao.setElitismCount((int) (i*0.1));
            System.out.println(Color.RED + "==== Population size: " + i + " ====" + Color.RESET);

            int[] routeCosts = new int[numOfTry];
            long[] GAWorkTimes = new long[numOfTry];

            for (int j = 0; j < numOfTry; j++) {

                dao.setRestrictionsArray(RestrictionGenerator.generateRestriction(dao));
                dao.setLoadCapacityForTraveler(calcLoad(3));

                TspGaRestrict GA = new TspGaRestrict(costMatrix);
                routeCosts[j] = GA.getBestTourCost();
                GAWorkTimes[j] = GA.getWorkTime();


            }
            System.out.printf("\nGA Average time: %,6.3f ms", calculateAverage(GAWorkTimes) /1_000_000.0);
            System.out.println("\nBest route cost: " + Arrays.stream(routeCosts).min());
            System.out.println("\nAverage route cost: " + Arrays.stream(routeCosts).average());

        }






    }
    public static void exp7() throws IOException {

        dao.setNumOfPoints(100);
        dao.setStartPoint(100);
        dao.setRestrictionsNeeded(true);
        int[][] costMatrix = extendMatrix(getCostMatrixFromPoints(Collections.unmodifiableList(generateCities())));


        for (double i = 0.1; i < 1; i+=0.1) {
            dao.setPopulationSize(300);
            dao.setElitismCount((int) (i * 300));
            System.out.println(Color.RED + "==== Elitism: " + i + " ====" + Color.RESET);

            int[] routeCosts = new int[numOfTry];
            long[] GAWorkTimes = new long[numOfTry];

            for (int j = 0; j < numOfTry; j++) {

                dao.setRestrictionsArray(RestrictionGenerator.generateRestriction(dao));
                dao.setLoadCapacityForTraveler(calcLoad(3));

                TspGaRestrict GA = new TspGaRestrict(costMatrix);
                routeCosts[j] = GA.getBestTourCost();
                GAWorkTimes[j] = GA.getWorkTime();


            }
            System.out.printf("\nGA Average time: %,6.3f ms", calculateAverage(GAWorkTimes) /1_000_000.0);
            System.out.println("\nBest route cost: " + Arrays.stream(routeCosts).min());
            System.out.println("\nAverage route cost: " + Arrays.stream(routeCosts).average());

        }






    }
    public static void exp8() throws IOException {

        dao.setNumOfPoints(100);
        dao.setStartPoint(100);
        dao.setRestrictionsNeeded(true);
        int[][] costMatrix = extendMatrix(getCostMatrixFromPoints(Collections.unmodifiableList(generateCities())));


        for (int i = 50; i < 55; i+=5) {
            dao.setPopulationSize(300);
            dao.setTournamentSize(i);
            System.out.println(Color.RED + "==== Tournament size: " + i + " ====" + Color.RESET);

            int[] routeCosts = new int[numOfTry];
            long[] GAWorkTimes = new long[numOfTry];

            for (int j = 0; j < numOfTry; j++) {

                dao.setRestrictionsArray(RestrictionGenerator.generateRestriction(dao));
                dao.setLoadCapacityForTraveler(calcLoad(3));

                TspGaRestrict GA = new TspGaRestrict(costMatrix);
                routeCosts[j] = GA.getBestTourCost();
                GAWorkTimes[j] = GA.getWorkTime();


            }
            System.out.printf("\nGA Average time: %,6.3f ms", calculateAverage(GAWorkTimes) /1_000_000.0);
            System.out.println("\nBest route cost: " + Arrays.stream(routeCosts).min());
            System.out.println("\nAverage route cost: " + Arrays.stream(routeCosts).average());

        }






    }
    private static long calculateAverage (long[] WorkTimes){
        long sum = 0;
        for (long workTime : WorkTimes) {
            sum += workTime;
        }
        return sum / WorkTimes.length;
    }
    private static double calculateAverage (double[] relativeError){
        double sum = 0;
        for (double distance : relativeError) {
            sum += distance;
        }
        return sum / relativeError.length;
    }

    private static int calcLoad (int a){

        int load;

        int[] res = dao.getRestrictionsArray();

        load = Arrays.stream(res).sum() / a + 5;


        return load;
    }


}

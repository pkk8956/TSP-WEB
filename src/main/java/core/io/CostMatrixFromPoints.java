package core.io;

import java.util.List;

public class CostMatrixFromPoints {

    public static int[][] getCostMatrixFromPoints(List<City> cities){
        int size = cities.size();
        int [][] costMatrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j){
                    costMatrix[i][j] = 0;
                }
                else {
                    City from = cities.get(i);
                    City to = cities.get(j);
                    costMatrix[i][j] = (int) calculateDistance(from.getxCoordinate(), from.getyCoordinate(),
                                                               to.getxCoordinate(), to.getyCoordinate());
                }
            }
        }
        return costMatrix;
    }

    public static double calculateDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}

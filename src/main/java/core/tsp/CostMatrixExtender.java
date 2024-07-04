package core.tsp;

import core.io.ConfigDAO;
import java.io.IOException;

public class CostMatrixExtender {

    public static int[][] extendMatrix(int[][] distanceMatrix) throws RuntimeException, IOException {

       ConfigDAO dao = new ConfigDAO();
       int numTraveler = dao.getNumOfTraveler();
       if (numTraveler == 1) return distanceMatrix;
       int n = distanceMatrix.length;
       if (numTraveler > n) throw new RuntimeException("Quantity of travelers greater than number of citi");
       int newLength = n + numTraveler - 1;
       int[][] extendedMatrix = new int[newLength][newLength];
       for (int i = 0; i < n; i++) {
           System.arraycopy(distanceMatrix[i], 0, extendedMatrix[i], 0, n);
       }
       for (int i = 0; i < newLength; i++) {
           for (int j = 0; j < newLength; j++) {
               if (i > n - 1) extendedMatrix[i][j] = extendedMatrix[0][j];
               if (j > n - 1) extendedMatrix[i][j] = extendedMatrix[i][0];
               if (i > n - 1 && j == 0) extendedMatrix[i][j] = 999;
               if (j > n - 1 && i == 0) extendedMatrix[i][j] = 999;
               if (i > n - 1 && j ==  newLength - 1) extendedMatrix[i][j] = 999;
               if (j > n - 1 && i == newLength - 1 ) extendedMatrix[i][j] = 999;
               if (i == j) extendedMatrix[i][j] = 0;
           }
       }
        return extendedMatrix;
   }
}

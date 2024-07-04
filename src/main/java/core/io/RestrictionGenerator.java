package core.io;

import java.util.Random;

public class RestrictionGenerator {

    public static int[] generateRestriction(ConfigDAO dao ) {

        int numOfVertex = dao.getNumOfPoints();
        int maxValue = dao.getMaxRestriction();
        int minValue = dao.getMinRestriction();
        int[] restrictions = new int[numOfVertex];
        Random random = new Random();
        restrictions[0] = 0;
        for (int i = 1; i < restrictions.length; i++) {
            restrictions[i] = random.nextInt(maxValue - minValue + 1) + minValue;
        }
        return restrictions;
    }
}

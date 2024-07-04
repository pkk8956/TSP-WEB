package core.tsp;

import core.io.ConfigDAO;
import java.io.IOException;
import java.util.List;

public class RestrictionChecker {
    private static int numOfVertex;
    private static int startCity;
    private static int loadCapacity;
    private static int[] restriction;

    public static boolean checkRoute(List<Integer> route) {
        int subRouteLoad = 0;
        for (int i = 1; i < route.size() - 1; i++) {
            int city = route.get(i);
            if (city == startCity || city >= numOfVertex) {
                if (subRouteLoad > loadCapacity) {
                    return false;
                }
                subRouteLoad = 0;
            } else {
                subRouteLoad += restriction[city];
            }
        }
        return subRouteLoad <= loadCapacity;
    }

    public static void setProp() throws IOException {
        ConfigDAO dao = new ConfigDAO();
        startCity = dao.getStartPoint();
        loadCapacity = dao.getLoadCapacityForTraveler();
        numOfVertex = dao.getNumOfPoints();
        restriction = dao.getRestrictionsArray();
    }
}

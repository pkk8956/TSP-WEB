package core.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CityRandomGenerator {

    public static List<City> generateCities() throws IOException {

        ConfigDAO dao = new ConfigDAO();
        int minXCoordinate = dao.getMinXCoordinate();
        int minYCoordinate = dao.getMinYCoordinate();
        int maxXCoordinate = dao.getMaxXCoordinate();
        int maxYCoordinate = dao.getMaxYCoordinate();
        int numOfCity = dao.getNumOfPoints();
        List<City> cities = new ArrayList<>(numOfCity);
        Random random = new Random();
        int cityIndex = 0;
        for (int i = 0; i < numOfCity; i++) {
            int x = random.nextInt(maxXCoordinate - minXCoordinate + 1) + minXCoordinate;
            int y = random.nextInt(maxYCoordinate - minYCoordinate + 1) + minYCoordinate;
            cities.add(new City(x, y, ++cityIndex));
        }
        return cities;
    }
}

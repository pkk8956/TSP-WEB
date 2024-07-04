package core.io;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CityFromFile {
    public static List<City> readFile(MultipartFile file) throws IOException {

        String content = new String(file.getBytes()).replace("\n", " ").replace("\r", " ");
        List<City> cities = new ArrayList<>();
        int[] restrictions;
        String[] citiesAsString = content.split(";");
        int lastRow;
        lastRow = citiesAsString.length - 1;
        Pattern pattern = Pattern.compile("\\{xCoordinate=(\\d+), yCoordinate=(\\d+), cityIndex=(\\d+)}");

        for (String s : citiesAsString) {
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                int x = Integer.parseInt(matcher.group(1));
                int y = Integer.parseInt(matcher.group(2));
                int index = Integer.parseInt(matcher.group(3));
                cities.add(new City(x, y, index));
            }
        }
        String[] numbers = citiesAsString[lastRow].split("=")[1].split(",");
        restrictions = new int[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            restrictions[i] = Integer.parseInt(numbers[i].trim());
        }

        ConfigDAO dao = new ConfigDAO();
        dao.setRestrictionsArray(restrictions);
        dao.setNumOfPoints(lastRow);
        return cities;
    }
}

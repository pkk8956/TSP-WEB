package core.io;

public class City {

    private final int xCoordinate;
    private final int yCoordinate;
    private final int cityIndex;

    public int getxCoordinate() {
        return xCoordinate;
    }
    public int getyCoordinate() {
        return yCoordinate;
    }
    public int getCityIndex() {
        return cityIndex;
    }

    public City(int xCoordinate, int yCoordinate, int cityIndex) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.cityIndex = cityIndex;
    }

    @Override
    public String toString() {
        return "City{" +
                "xCoordinate=" + xCoordinate +
                ", yCoordinate=" + yCoordinate +
                ", cityIndex=" + cityIndex +
                '}';
    }
}

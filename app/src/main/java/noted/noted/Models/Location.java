package noted.noted.Models;

/**
 * Created by Anna on 17-Jan-16.
 */
public class Location {
    private double longitudeToShow;
    private double latitudeToShow;

    public Location(double longitudeToShow, double latitudeToShow) {
        this.longitudeToShow = longitudeToShow;
        this.latitudeToShow = latitudeToShow;
    }

    public double getLongitudeToShow() {
        return longitudeToShow;
    }

    public void setLongitudeToShow(double longitudeToShow) {
        this.longitudeToShow = longitudeToShow;
    }

    public double getLatitudeToShow() {
        return latitudeToShow;
    }

    public void setLatitudeToShow(double latitudeToShow) {
        this.latitudeToShow = latitudeToShow;
    }

    @Override
    public String toString() {
        return getLongitudeToShow() + "," + getLatitudeToShow();
    }
}

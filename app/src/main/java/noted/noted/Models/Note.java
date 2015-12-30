package noted.noted.Models;

import android.location.Location;

import java.util.Calendar;

/**
 * Created by adi on 26-Dec-15.
 */
public class Note {
    private int id;
    private String from;
    private String to;
    private String details;
    private Calendar sentTime;
    private Calendar receivedTime;
    private Calendar showedTime;
    private Calendar timeToShow;
    private Location locationToShow;
    private boolean isShown;

    public Note(int id, String from, String to, String details, Calendar sentTime, Calendar receivedTime, Calendar showedTime, Calendar timeToShow, Location locationToShow, boolean isShown) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.details = details;
        this.sentTime = sentTime;
        this.receivedTime = receivedTime;
        this.showedTime = showedTime;
        this.timeToShow = timeToShow;
        this.locationToShow = locationToShow;
        this.isShown = isShown;
    }

    public int getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Calendar getSentTime() {
        return sentTime;
    }

    public void setSentTime(Calendar sentTime) {
        this.sentTime = sentTime;
    }

    public Calendar getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(Calendar receivedTime) {
        this.receivedTime = receivedTime;
    }

    public Calendar getShowedTime() {
        return showedTime;
    }

    public void setShowedTime(Calendar showedTime) {
        this.showedTime = showedTime;
    }

    public Calendar getTimeToShow() {
        return timeToShow;
    }

    public void setTimeToShow(Calendar timeToShow) {
        this.timeToShow = timeToShow;
    }

    public Location getLocationToShow() {
        return locationToShow;
    }

    public void setLocationToShow(Location locationToShow) {
        this.locationToShow = locationToShow;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setIsShown(boolean isShown) {
        this.isShown = isShown;
    }
}


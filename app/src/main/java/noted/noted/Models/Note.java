package noted.noted.Models;

/**
 * Created by adi on 26-Dec-15.
 */
public class Note {
    private String id;
    private String from;
    private String to;
    private String details;
    private String sentTime;
    private String receivedTime;
    private String showedTime;
    private String timeToShow;
    private String locationToShow;
    private boolean isShown;

    public Note(String from, String to, String details, String sentTime) {
        this.from = from;
        this.to = to;
        this.details = details;
        this.sentTime = sentTime;
    }

    public Note(String from, String to, String details, String sentTime, String timeToShow, String locationToShow) {
        this.from = from;
        this.to = to;
        this.details = details;
        this.sentTime = sentTime;
        this.timeToShow = timeToShow;
        this.locationToShow = locationToShow;
    }

    public Note(String id, String from, String to, String details, String sentTime, String receivedTime, String showedTime, String timeToShow, String locationToShow, boolean isShown) {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {this.id = id; }

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

    public String getSentTime() {
        return sentTime;
    }

    public void setSentTime(String sentTime) {
        this.sentTime = sentTime;
    }

    public String getReceivedTime() {
        return receivedTime;
    }

    public void setReceivedTime(String receivedTime) {
        this.receivedTime = receivedTime;
    }

    public String getShowedTime() {
        return showedTime;
    }

    public void setShowedTime(String showedTime) {
        this.showedTime = showedTime;
    }

    public String getTimeToShow() {
        return timeToShow;
    }

    public void setTimeToShow(String timeToShow) {
        this.timeToShow = timeToShow;
    }

    public String getLocationToShow() {
        return locationToShow;
    }

    public void setLocationToShow(String locationToShow) {
        this.locationToShow = locationToShow;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setIsShown(boolean isShown) {
        this.isShown = isShown;
    }
}


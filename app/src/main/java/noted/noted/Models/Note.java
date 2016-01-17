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
    private String timeToShow;
    private Location locationToShow;

    public Note(String from, String to, String details, String sentTime) {
        this.from = from;
        this.to = to;
        this.details = details;
        this.sentTime = sentTime;
    }

    public Note(String id, String from, String to, String details, String sentTime, String timeToShow, Location locationToShow) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.details = details;
        this.sentTime = sentTime;
        this.timeToShow = timeToShow;
        this.locationToShow = locationToShow;
    }

    // To use in gridview
    public Long getNumericId() {
        return new Long(id);
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

    public String getTimeToShow() {
        return timeToShow;
    }

    public void setTimeToShow(String timeToShow) {
        this.timeToShow = timeToShow;
    }

    public Location getLocationToShow() {
        return locationToShow;
    }

    public void setLocationToShow(Location locationToShow) {
        this.locationToShow = locationToShow;
    }
}


package noted.noted.Models;

import android.location.Location;

import java.util.Calendar;

/**
 * Created by adi on 26-Dec-15.
 */
// test
public class Note {
        int id;
        int from;
        int to;
        String details;
        Calendar time;
        Location location;
        boolean isShown;

    public Note(int id, int from, int to, String details, boolean isAppear) {
        this.details = details;
        this.from = from;
        this.id = id;
        this.isShown = isShown;
        this.location = location;
        this.time = time;
        this.to = to;
    }

    public int getId() {
        return id;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setIsShown(boolean isShown) {
        this.isShown = isShown;
    }
}


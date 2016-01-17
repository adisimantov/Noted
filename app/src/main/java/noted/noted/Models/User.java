package noted.noted.Models;

/**
 * Created by Anna on 10-Jan-16.
 */
public class User {
    private String phoneNumber;
    private String authCode;
    private String joinDate;
    private Boolean isActive;

    public User(String phoneNumber, String authCode, String joinDate, Boolean isActive) {
        this.phoneNumber = phoneNumber;
        this.authCode = authCode;
        this.joinDate = joinDate;
        this.isActive = isActive;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAuthCode() {
        return authCode;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public void setJoinDate(String joinDate) {
        this.joinDate = joinDate;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}

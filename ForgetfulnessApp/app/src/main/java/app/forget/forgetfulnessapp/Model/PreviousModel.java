package app.forget.forgetfulnessapp.Model;

public class PreviousModel {
    private String title, description;
    private String photo;
    private String status;
    private String category;
    private String date;
    private String time;
    private String id;
    private String creationDate;
    private String Monday, Saturday, Wednesday, Tuesday, Thursday, Friday, Sunday;
    private String type;


    public PreviousModel() {
    }

    public PreviousModel(String title, String description, String photo, String status, String category, String date, String time, String id, String creationDate, String monday,
                         String saturday, String wednesday, String tuesday, String thursday, String friday, String sunday,String type) {
        this.title = title;
        this.description = description;
        this.photo = photo;
        this.status = status;
        this.category = category;
        this.date = date;
        this.time = time;
        this.id = id;
        this.creationDate = creationDate;
        Monday = monday;
        Saturday = saturday;
        Wednesday = wednesday;
        Tuesday = tuesday;
        Thursday = thursday;
        Friday = friday;
        Sunday = sunday;
        this.type = type;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getMonday() {
        return Monday;
    }

    public void setMonday(String monday) {
        Monday = monday;
    }

    public String getSaturday() {
        return Saturday;
    }

    public void setSaturday(String saturday) {
        Saturday = saturday;
    }

    public String getWednesday() {
        return Wednesday;
    }

    public void setWednesday(String wednesday) {
        Wednesday = wednesday;
    }

    public String getTuesday() {
        return Tuesday;
    }

    public void setTuesday(String tuesday) {
        Tuesday = tuesday;
    }

    public String getThursday() {
        return Thursday;
    }

    public void setThursday(String thursday) {
        Thursday = thursday;
    }

    public String getFriday() {
        return Friday;
    }

    public void setFriday(String friday) {
        Friday = friday;
    }

    public String getSunday() {
        return Sunday;
    }

    public void setSunday(String sunday) {
        Sunday = sunday;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
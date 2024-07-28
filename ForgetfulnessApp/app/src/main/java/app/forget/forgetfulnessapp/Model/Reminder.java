package app.forget.forgetfulnessapp.Model;

public class Reminder {
    private String title, description;
    private String photo;
    private String status;
    private String category;
    private String date;
    private String time;
    private String id;
    private String creationDate;

    public Reminder(){
    }

    public Reminder(String title, String description, String photo,String status,String category,String date,String time,String id,String creationDate) {
        this.title = title;
        this.description = description;
        this.photo = photo;
        this.status = status;
        this.category = category;
        this.time =time;
        this.date = date;
        this.id = id;
        this.creationDate = creationDate;
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
}

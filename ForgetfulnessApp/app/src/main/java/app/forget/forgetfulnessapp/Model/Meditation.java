package app.forget.forgetfulnessapp.Model;

public class Meditation {
    private String photo,field;

    public Meditation(String photo, String field) {
        this.photo = photo;
        this.field = field;
    }
    public Meditation(){}

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}

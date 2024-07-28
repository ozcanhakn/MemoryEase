package app.forget.forgetfulnessapp.Model;

public class Category {
    private String field,photo;

    public Category(String field, String photo) {
        this.field = field;
        this.photo = photo;
    }
    public Category(){}

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}

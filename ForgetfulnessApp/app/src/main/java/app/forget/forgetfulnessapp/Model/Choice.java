package app.forget.forgetfulnessapp.Model;

public class Choice {
    private String field,photo,type,forcat;
    private int min;

    public Choice(String field, String photo, String type, int min,String forcat) {
        this.field = field;
        this.photo = photo;
        this.type = type;
        this.min = min;
        this.forcat = forcat;
    }

    public Choice(){}

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String getForcat() {
        return forcat;
    }

    public void setForcat(String forcat) {
        this.forcat = forcat;
    }
}

package app.forget.forgetfulnessapp.Model;


public class Bill {
    public String price, date, desc, title,type;

    public Bill(String price, String date, String desc, String title,String type) {
        this.price = price;
        this.date = date;
        this.desc = desc;
        this.title = title;
        this.type = type;
    }
    public Bill(){}


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
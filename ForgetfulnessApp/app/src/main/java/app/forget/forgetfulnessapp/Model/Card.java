package app.forget.forgetfulnessapp.Model;

public class Card {
    public int img_id;
    public boolean isClosed;

    public Card(int id, boolean isClosed){
        this.img_id = id;
        this.isClosed = isClosed;
    }
}
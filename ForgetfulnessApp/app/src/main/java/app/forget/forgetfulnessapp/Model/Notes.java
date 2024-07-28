package app.forget.forgetfulnessapp.Model;

public class Notes {
    public String notes, dates,headers;

    public Notes(String notes, String dates,String headers) {
        this.notes = notes;
        this.dates = dates;
        this.headers = headers;
    }
    public Notes(){}

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }
}

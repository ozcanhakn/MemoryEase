package app.forget.forgetfulnessapp;

import android.content.Context;

import java.util.ArrayList;

public class NumberPickerText {

    private static ArrayList<NumberPickerText> TextArrayList;
    private int id;
    private String name;
    private String englishName; // Yeni eklenen özellik
    private Context context;

    public NumberPickerText(int id, String name, String englishName) {
        this.id = id;
        this.name = name;
        this.englishName = englishName;
    }

    public static void initTextCategory(Context context){

        TextArrayList = new ArrayList<>();


        String personal1 = context.getString(R.string.Personal);
        String social1 = context.getString(R.string.Social);
        String education1 = context.getString(R.string.education);
        String job1 = context.getString(R.string.Job);
        String finance1 = context.getString(R.string.finance);
        String shopping1 = context.getString(R.string.Shopping);
        String other1 = context.getString(R.string.Other);



        NumberPickerText personal = new NumberPickerText(0, personal1, "Personal");
        TextArrayList.add(personal);

        NumberPickerText social = new NumberPickerText(1, social1, "Social");
        TextArrayList.add(social);

        NumberPickerText education = new NumberPickerText(2, education1, "Education");
        TextArrayList.add(education);

        NumberPickerText job = new NumberPickerText(3, job1, "Job");
        TextArrayList.add(job);

        NumberPickerText finance = new NumberPickerText(4, finance1, "Finance");
        TextArrayList.add(finance);

        NumberPickerText shopping = new NumberPickerText(5, shopping1, "Shopping");
        TextArrayList.add(shopping);

        NumberPickerText other = new NumberPickerText(6, other1, "Other");
        TextArrayList.add(other);
    }

    public static ArrayList<NumberPickerText> getTextArrayList() {
        return TextArrayList;
    }

    public static String[] categoryName(){
        String[] categories = new String[TextArrayList.size()];
        for (int i = 0; i < TextArrayList.size(); i++){
            categories[i] = TextArrayList.get(i).name;
        }
        return categories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }
}
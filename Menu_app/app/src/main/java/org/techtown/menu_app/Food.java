package org.techtown.menu_app;

public class Food {
    private String name;

    public Food(){}

    public Food(String foodname){
        this.name = foodname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

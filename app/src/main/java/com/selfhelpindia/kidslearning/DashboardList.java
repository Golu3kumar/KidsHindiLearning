package com.selfhelpindia.kidslearning;

public class DashboardList {

    private int image;
    private String name;

    public DashboardList(int image, String name) {
        this.image = image;
        this.name = name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}

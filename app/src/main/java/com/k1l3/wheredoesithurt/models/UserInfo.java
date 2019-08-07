package com.k1l3.wheredoesithurt.models;

public class UserInfo {
    private String Age;
    private String Disease;
    private String Gender;
    private String LifeStyles;
    private String Name;
    private Times times;

    public UserInfo() {
    }

    public String getAge() {
        return Age;
    }

    public String getDisease() {
        return Disease;
    }

    public String getGender() {
        return Gender;
    }

    public String getLifeStyles() {
        return LifeStyles;
    }

    public String getName() {
        return Name;
    }

    public Times getTimes() {
        return times;
    }

    public void setAge(String Age) {
        this.Age = Age;
    }

    public void setDisease(String disease) {
        Disease = disease;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setLifeStyles(String lifeStyles) {
        LifeStyles = lifeStyles;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setTimes(Times times) {
        this.times = times;
    }
}

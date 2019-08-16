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

    public void setAge(String Age) {
        this.Age = Age;
    }

    public String getDisease() {
        return Disease;
    }

    public void setDisease(String disease) {
        Disease = disease;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getLifeStyles() {
        return LifeStyles;
    }

    public void setLifeStyles(String lifeStyles) {
        LifeStyles = lifeStyles;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Times getTimes() {
        return times;
    }

    public void setTimes(Times times) {
        this.times = times;
    }
}

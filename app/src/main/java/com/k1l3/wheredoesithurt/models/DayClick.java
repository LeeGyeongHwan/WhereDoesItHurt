package com.k1l3.wheredoesithurt.models;

public class DayClick {
    int one,two,three;

    public DayClick(){

    }
    public DayClick(int one, int two, int three) {
        this.one = one;
        this.two = two;
        this.three = three;
    }

    public int getOne() {
        return one;
    }

    public void setOne(int one) {
        this.one = one;
    }

    public int getTwo() {
        return two;
    }

    public void setTwo(int two) {
        this.two = two;
    }

    public int getThree() {
        return three;
    }

    public void setThree(int three) {
        this.three = three;
    }
}

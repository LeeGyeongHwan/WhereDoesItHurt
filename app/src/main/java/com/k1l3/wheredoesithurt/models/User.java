package com.k1l3.wheredoesithurt.models;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static final User instance = new User();
    private ArrayList<Prescription> Prescriptions;
    private UserInfo userInfo;

    private User() {
    }

    public static User getInstance() {
        return instance;
    }

    public List<Prescription> getPrescriptions() {
        return Prescriptions;
    }

    public void setPrescriptions(ArrayList<Prescription> prescriptions) {
        Prescriptions = prescriptions;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

}

package com.k1l3.wheredoesithurt.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private ArrayList<Prescription> Prescriptions;
    private UserInfo userInfo;

    public User() {
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

package com.k1l3.wheredoesithurt.models;

import java.util.ArrayList;
import java.util.List;

public class User { //TODO (@nono5546) : User정보 관리하는 클래스 만들기.
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

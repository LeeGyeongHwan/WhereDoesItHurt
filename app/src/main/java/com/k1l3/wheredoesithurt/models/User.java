package com.k1l3.wheredoesithurt.models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class User { //TODO (@nono5546) : User정보 관리하는 클래스 만들기.
    private static final User instance = new User();
    private String id;
    private ArrayList<Prescription> Prescriptions;
    private UserInfo userInfo;

    private User() {
    }

    public static User getInstance() {
        return instance;
    }

    public ArrayList<Prescription> getPrescriptions() {
        return Prescriptions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void syncWithDatabase(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users");

        databaseReference.child(id).setValue(instance);
    }
}

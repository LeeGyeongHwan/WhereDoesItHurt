package com.k1l3.wheredoesithurt;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.k1l3.wheredoesithurt.models.Medicine;
import com.k1l3.wheredoesithurt.models.Prescription;
import com.k1l3.wheredoesithurt.models.Times;
import com.k1l3.wheredoesithurt.models.User;
import com.k1l3.wheredoesithurt.models.UserInfo;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private SessionCallback sessionCallback;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Session.getCurrentSession().isOpened()) {
            isLogin = true;
        }

        sessionCallback = new SessionCallback();
        Session.getCurrentSession().addCallback(sessionCallback);
        Session.getCurrentSession().checkAndImplicitOpen();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            UserManagement.getInstance().me(new MeV2ResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    int result = errorResult.getErrorCode();

                    if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Toast.makeText(getApplicationContext(), "세션이 닫혔습니다. 다시 시도해 주세요: " + errorResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(MeV2Response result) {
                    if (!isLogin) {
                        registerUserInfoToFireBase(result);
                    }

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("name", result.getNickname());
                    intent.putExtra("profile", result.getProfileImagePath());
                    intent.putExtra("id", result.getId());
                    intent.putExtra("email", result.getKakaoAccount().getEmail());
                    startActivity(intent);
                    finish();
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUserInfoToFireBase(MeV2Response result) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("users");

        final User user = new User();
        final Prescription prescription = new Prescription();
        final Times times = new Times();
        final Medicine medicine = new Medicine();
        final UserInfo userInfo = new UserInfo();

        final UserAccount kakaoAcount = result.getKakaoAccount();

        times.setBreakFast(" ");
        times.setLunch(" ");
        times.setDinner(" ");

        if (kakaoAcount.getAgeRange() == null)
            userInfo.setAge(" ");
        else
            userInfo.setAge(kakaoAcount.getAgeRange().toString());
        userInfo.setName(result.getNickname());

        if (kakaoAcount.getGender() == null)
            userInfo.setGender(" ");
        else
            userInfo.setGender(kakaoAcount.getGender().toString());
        userInfo.setTimes(times);
        userInfo.setDisease("");
        userInfo.setLifeStyles("");

        medicine.setName("파티엔 정");
        ArrayList<Medicine> d = new ArrayList<>();
        d.add(medicine);

        ArrayList<String> hashTag = new ArrayList<>();
        hashTag.add("가려움");
        hashTag.add("알레르기");
        hashTag.add("기침");
        hashTag.add("내과");
        hashTag.add("외과");

        prescription.setBegin("2019.07.29");
        prescription.setEnd(0);
        prescription.setCaution(0);
        prescription.setMedicines(d);
        prescription.setSideEffect(0);
        prescription.setHashTag(hashTag);

        ArrayList<Prescription> p = new ArrayList<>();
        p.add(prescription);

        user.setPrescriptions(p);
        user.setUserInfo(userInfo);

        myRef.child(Long.toString(result.getId())).setValue(user);
    }
}
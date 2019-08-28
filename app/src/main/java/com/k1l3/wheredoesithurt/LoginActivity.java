package com.k1l3.wheredoesithurt;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
import com.kakao.util.exception.KakaoException;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private static final User user = User.getInstance();
    private static final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private static final String TAG = MainActivity.class.getSimpleName();
    private SessionCallback sessionCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

    private void loadDatabase(DataSnapshot dataSnapshot, String id) {
        final ArrayList<Prescription> prescriptions = new ArrayList<>();

        if(dataSnapshot.hasChild("userInfo")) {
            user.setUserInfo(dataSnapshot.child("userInfo").getValue(UserInfo.class));
        }else{
            user.setUserInfo(new UserInfo());
        }

        if(user.getUserInfo().getDefaultTimes() == null) {
            final Times defaultTimes = new Times();
            user.getUserInfo().setDefaultTimes(defaultTimes);
        }

        for (DataSnapshot dataSnapshotChild : dataSnapshot.child("prescriptions").getChildren()) {
            prescriptions.add(dataSnapshotChild.getValue(Prescription.class));
            Log.i(TAG, "loadPrescription");
        }

        user.setPrescriptions(prescriptions);

        user.setId(id);

        Log.i(TAG, "loadDatabase: " + id);
    }

    private void existDatabase(final String id, final String name, final Intent intent) { //TODO (@nono5546) : Rename
        final DatabaseReference userDatabaseReference = firebaseDatabase.getReference("users");

        ValueEventListener databaseListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(id)) {
                    createDatabase(id, name);
                } else {
                    loadDatabase(dataSnapshot.child(id), id);
                }

                startActivity(intent);

                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: ", databaseError.toException());
            }
        };

        userDatabaseReference.addListenerForSingleValueEvent(databaseListener);
    }

    private void createDatabase(String id, String name) {
        final DatabaseReference databaseReference = firebaseDatabase.getReference("users");
        final UserInfo userInfo = new UserInfo();

        userInfo.setName(name);

        user.setUserInfo(userInfo);
        user.setPrescriptions(new ArrayList<Prescription>());
        user.setId(id.toString());

        databaseReference.child(id.toString()).setValue(user);

        Log.i(TAG, "createDatabase: " + id);
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
                    final Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    intent.putExtra("name", result.getNickname());
                    intent.putExtra("profile", result.getProfileImagePath());
                    intent.putExtra("id", result.getId());
                    intent.putExtra("email", result.getKakaoAccount().getEmail());

                    existDatabase(String.valueOf(result.getId()), result.getNickname(), intent);
                }
            });
        }

        @Override
        public void onSessionOpenFailed(KakaoException e) {
            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: " + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
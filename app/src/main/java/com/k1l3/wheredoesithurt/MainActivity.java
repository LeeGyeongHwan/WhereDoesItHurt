package com.k1l3.wheredoesithurt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import static android.support.constraint.Constraints.TAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    Fragment fragment_main, fragment_search;
    String image;
    String name;
    Long id;
    String email;
    FragmentManager manager;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fragment_main = new Fragment_main();
        fragment_search = new Fragment_search();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();

        Intent intent = getIntent();

        image = intent.getStringExtra("profile");
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        id = intent.getLongExtra("id",0);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        //replaceFragment(fragment_main);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment_main)
                .commit();
        View headerView = navigationView.getHeaderView(0);

        //프로필 사진과 이름을 출력
        ImageView profileImage = headerView.findViewById(R.id.profileImage);
        TextView profileName = headerView.findViewById(R.id.profileName);

        Glide.with(this).load(image).into(profileImage);
        profileName.setText(name);

        profileImage.setBackground(new ShapeDrawable(new OvalShape()));
        profileImage.setClipToOutline(true);

        ImageView cambtn = findViewById(R.id.cameraBtn);
        cambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Addword_Activity.class);
                startActivity(intent);
            }
        });

        Button mypagebtn = (Button)findViewById(R.id.mypagebtn);
        mypagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment_mypage = new Fragment_mypage();
                /*Bundle bundle = new Bundle();
                bundle.putString("profile", image);
                bundle.putString("name",name);
                bundle.putString("email",email);
                fragment_mypage.setArguments(bundle);*/
                replaceFragment(fragment_mypage);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Toast.makeText(this, "검색", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(@NonNull Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putLong("id", id);
        bundle.putString("profile", image);
        bundle.putString("name",name);
        bundle.putString("email",email);
        fragment.setArguments(bundle);
        transaction = manager.beginTransaction();
        transaction.replace(R.id.main_container, fragment);
        transaction.addToBackStack("fragment");
        transaction.commit();
        Log.e(TAG, "값 : " + String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.nav_home) {
            logout();
        } else if (id == R.id.nav_gallery) {
            withdrawal();
        } else if (id == R.id.nav_history) {
            replaceFragment(new Fragment_history());
        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Fragment current_fragment = getSupportFragmentManager().findFragmentById(R.id.main_container);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!drawer.isDrawerOpen(GravityCompat.START) && current_fragment == fragment_main) {
            super.onBackPressed();
        } else if (!drawer.isDrawerOpen(GravityCompat.START) && current_fragment != fragment_main) {
            manager.popBackStack();
        }
    }

    public void logout() {
        Toast.makeText(getApplicationContext(), "정상적으로 로그아웃되었습니다.", Toast.LENGTH_SHORT).show();

        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    private void withdrawal() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("탈퇴하시겠습니까?")
                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
                            @Override
                            public void onFailure(ErrorResult errorResult) {
                                int result = errorResult.getErrorCode();

                                if (result == ApiErrorCode.CLIENT_ERROR_CODE) {
                                    Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "회원탈퇴에 실패했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onSessionClosed(ErrorResult errorResult) {
                                Toast.makeText(getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onNotSignedUp() {
                                Toast.makeText(getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onSuccess(Long result) {
                                Toast.makeText(getApplicationContext(), "회원탈퇴에 성공했습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });

                        dialog.dismiss();
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

    }

    public void toolbar_search() {
        toolbar.findViewById(R.id.toolbar_history).setVisibility(View.GONE);
        toolbar.findViewById(R.id.logo).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_search).setVisibility(View.VISIBLE);
        toolbar.findViewById(R.id.toolbar_mypage).setVisibility(View.GONE);
        toolbar.setBackgroundColor(Color.rgb(173,165,253));
        setup_nav(R.drawable.ic_menu);
    }

    public void toolbar_main() {
        toolbar.findViewById(R.id.toolbar_history).setVisibility(View.GONE);
        toolbar.findViewById(R.id.logo).setVisibility(View.VISIBLE);
        toolbar.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_mypage).setVisibility(View.GONE);
        toolbar.setBackgroundColor(Color.rgb(255,255,255));
        setup_nav(R.drawable.ic_menu);
    }

    public void toolbar_history() {
        toolbar.findViewById(R.id.toolbar_history).setVisibility(View.VISIBLE);
        toolbar.findViewById(R.id.logo).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_mypage).setVisibility(View.GONE);
        toolbar.setBackgroundColor(Color.rgb(255,255,255));
        setup_nav(R.drawable.ic_menu);
    }
    public void toolbar_mypage(){
        toolbar.findViewById(R.id.toolbar_mypage).setVisibility(View.VISIBLE);
        toolbar.findViewById(R.id.toolbar_history).setVisibility(View.GONE);
        toolbar.findViewById(R.id.logo).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        toolbar.setBackgroundColor(Color.rgb(255,255,255));
        setup_nav(R.drawable.ic_menu);
    }
    public void setup_nav(int menuImage){
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
        actionBar.setHomeAsUpIndicator(menuImage);
    }
}
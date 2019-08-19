package com.k1l3.wheredoesithurt;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
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
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;
import com.google.api.services.vision.v1.model.Page;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar;
    Fragment fragment_main, fragment_search;
    String image;
    String name;
    Long id;
    String email;
    FragmentManager manager;
    FragmentTransaction transaction;
    Button mypagebtn;

    private static final String CLOUD_VISION_API_KEY = "AIzaSyBqCdIPxM7wHztVVPtXP4a_KFULRH3mPm0";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";
    private static final int MAX_LABEL_RESULTS = 10;
    private static final int MAX_DIMENSION = 1200;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fragment_main = new Fragment_main();
        fragment_search = new Fragment_search();
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        mypagebtn = (Button)findViewById(R.id.mypagebtn);

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

        ImageView cambtn= findViewById(R.id.cameraBtn);
        cambtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
                builder
                        .setMessage("처방전을 추가하세요")
                        .setPositiveButton("앨범에서 가져오기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.this.startGalleryChooser();
                            }
                        })
                        .setNegativeButton("사진찍기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.this.startCamera();
                            }
                        });
                builder.create().show();
            }
        });
        mypagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment_mypage = new Fragment_mypage();
                Bundle bundle = new Bundle();
                bundle.putString("profile", image);
                bundle.putString("name",name);
                bundle.putString("email",email);
                fragment_mypage.setArguments(bundle);
                replaceFragment(fragment_mypage);

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                manager.popBackStack();
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

    //Cloud vision function
    public void startGalleryChooser() {
        if (PermissionUtils.requestPermission(this, GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Log.d("success","Gallery");
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"),
                    GALLERY_IMAGE_REQUEST);
        }
    }

    public void startCamera() {
        if (PermissionUtils.requestPermission(
                this,
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uploadImage(data.getData());
        } else if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {
            Uri photoUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", getCameraFile());
            uploadImage(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
            case GALLERY_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, GALLERY_PERMISSIONS_REQUEST, grantResults)) {
                    startGalleryChooser();
                }
                break;
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getContentResolver(), uri),
                                MAX_DIMENSION);

                callCloudVision(bitmap);

            } catch (IOException e) {
                Log.d(TAG, "Image picking failed because " + e.getMessage());
                Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d(TAG, "Image picker gave us a null image.");
            Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    private Vision.Images.Annotate prepareAnnotationRequest(final Bitmap bitmap) throws IOException {
        HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        Log.d("gallery", "Json make " + jsonFactory.toString());
        VisionRequestInitializer requestInitializer =
                new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    /**
                     * We override this so we can inject important identifying fields into the HTTP
                     * headers. This enables use of a restricted cloud platform API key.
                     */
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> visionRequest)
                            throws IOException {
                        super.initializeVisionRequest(visionRequest);

                        String packageName = getPackageName();
                        visionRequest.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(getPackageManager(), packageName);

                        visionRequest.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };

        Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
        builder.setVisionRequestInitializer(requestInitializer);
        Log.d("gallery", "Json find " + jsonFactory.toString());
        Vision vision = builder.build();

        BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                new BatchAnnotateImagesRequest();
        batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
            AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

            // Add the image
            Image base64EncodedImage = new Image();
            // Convert the bitmap to a JPEG
            // Just in case it's a format that Android understands but Cloud Vision
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // Base64 encode the JPEGr
            base64EncodedImage.encodeContent(imageBytes);
            annotateImageRequest.setImage(base64EncodedImage);

            // add the features we want
            annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                Feature textDetection = new Feature();
                textDetection.setType("DOCUMENT_TEXT_DETECTION");
                textDetection.setMaxResults(MAX_LABEL_RESULTS);
                add(textDetection);
            }});

            // Add the list of one thing to the request
            add(annotateImageRequest);
        }});
        Log.d("gallery", "batch find " + batchAnnotateImagesRequest.toString());
        Vision.Images.Annotate annotateRequest =
                vision.images().annotate(batchAnnotateImagesRequest);
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.setDisableGZipContent(true);
        Log.d("gallery", "annotate request " + annotateRequest.toString());
        Log.d(TAG, "created Cloud Vision request object, sending request");

        return annotateRequest;
    }

    private class LabelDetectionTask extends AsyncTask<Object, Void, String> {
        private final WeakReference<MainActivity> mActivityWeakReference;
        private Vision.Images.Annotate mRequest;

        LabelDetectionTask(MainActivity activity, Vision.Images.Annotate annotate) {
            mActivityWeakReference = new WeakReference<>(activity);
            mRequest = annotate;
        }

        ProgressDialog asyncDialog = new ProgressDialog(
                MainActivity.this);

        @Override
        protected void onPreExecute() {
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            asyncDialog.setMessage("분석중입니다..");

            // show dialog
            asyncDialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.d(TAG, "created Cloud Vision request object, sending request");
                BatchAnnotateImagesResponse response = mRequest.execute();
                return convertResponseToString(response);

            } catch (GoogleJsonResponseException e) {
                Log.d(TAG, "failed to make API request because " + e.getContent());
            } catch (IOException e) {
                Log.d(TAG, "failed to make API request because of other IOException " +
                        e.getMessage());
            }
            return "Cloud Vision API request failed. Check logs for details.";
        }

        protected void onPostExecute(String result) {
            MainActivity activity = mActivityWeakReference.get();
            asyncDialog.dismiss();
            if (activity != null && !activity.isFinishing()) {
                Intent intent = new Intent (MainActivity.this,ResultOfVision.class);
                intent.putExtra("result",result);
                startActivity(intent);

                //TextView imageDetail = activity.findViewById(R.id.image_details);
                //imageDetail.setText(result);
            }
        }
    }

    private void callCloudVision(final Bitmap bitmap) {
        // Switch text to loading

        // Do the real work in an async task, because we need to use the network anyway
        try {
            AsyncTask<Object, Void, String> labelDetectionTask = new LabelDetectionTask(this, prepareAnnotationRequest(bitmap));
            labelDetectionTask.execute();
        } catch (IOException e) {
            Log.d(TAG, "failed to make API request because of other IOException " +
                    e.getMessage());
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private static String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder("I found these things:\n\n");
        Log.d("gallery", "getresponse " + response.getResponses().toString());

        List<Page> pages =response.getResponses().get(0).getFullTextAnnotation().getPages();
        Log.d("gallery", "convertResponseToString: "+pages.size());
        /*
        for(Page page: pages){
            List<Block> blocks = page.getBlocks();
            for(Block block : blocks){
                List<Paragraph> paragraphs = block.getParagraphs();
                for(Paragraph paragraph: paragraphs){
                    List<Word> words = paragraph.getWords();
                    for(Word word: words){
                        word.getBoundingBox();
                        word.getSymbols()
                    }
                }
            }

        }*/

        List<EntityAnnotation> labels = response.getResponses().get(0).getTextAnnotations();
        Log.d("gallery", "get0" + response.getResponses().get(0).toString());
        Log.d("gallery", "label" + labels);
        Log.d("gallery", "get0 getpage" + response.getResponses().get(0).getFullTextAnnotation());
        Log.d("gallery", "get0 getpage size" + response.getResponses().get(0).getFullTextAnnotation().getPages());

        int vertixArray[][]=new int[3][2];
        int Y_eachmedicine[] = new int[5];
        String medicine[] = new String[5];
        int info_med[][] = new int[5][3];
        int arrcount=0;

        if (labels != null) {
            // 첫번째. 투약량 투여횟수 투약일수 x좌표 뽑기


            for (EntityAnnotation label : labels) {
                String labelstr=label.getDescription();
                if(labelstr.equals("량") || labelstr.equals("투약량")){
                    int firstX=label.getBoundingPoly().getVertices().get(0).getX();
                    int secondX=label.getBoundingPoly().getVertices().get(1).getX();
                    vertixArray[0][0]=firstX;
                    vertixArray[0][1]=secondX;
                }else if(labelstr.equals("투여횟수")||labelstr.equals("투여")){
                    int firstX=label.getBoundingPoly().getVertices().get(0).getX();
                    int secondX=label.getBoundingPoly().getVertices().get(1).getX();
                    vertixArray[1][0]=firstX;
                    vertixArray[1][1]=secondX;
                }else if(labelstr.equals("투약일수")||labelstr.equals("일수")){
                    int firstX=label.getBoundingPoly().getVertices().get(0).getX();
                    int secondX=label.getBoundingPoly().getVertices().get(1).getX();
                    vertixArray[2][0]=firstX;
                    vertixArray[2][1]=secondX;
                }
            }
            for( int i=0;i<3;i++){
                for(int j=0;j<2;j++){
                    Log.d("gallery", "convertResponseToString: vertix["+i+"]["+j+"] : "+ vertixArray[i][j]);
                }
            }

            //두번째 9자리 번호 찾고 숫자 확인 -> 옆옆에 약이름 가져오기

            int findcount=3;
            for (EntityAnnotation label : labels) {
                String labelstr = label.getDescription();
                if(findcount==2){
                    medicine[arrcount]=labelstr;
                    Y_eachmedicine[arrcount]=label.getBoundingPoly().getVertices().get(0).getY();
                    arrcount++;
                }
                //message.append(String.format(Locale.US, "%.3f: %s", label.getScore(), label.getDescription()));

                if(labelstr.length()==9){
                    try{
                        findcount=0;
                        Integer.parseInt(labelstr);
                        Log.d("gallery", "convertResponseToString: "+labelstr);
                    }catch (NumberFormatException e){
                        Log.d("exception", "convertResponseToString: "+e);
                        findcount=3;
                    }
                }
                Log.d("galler", "convertResponseToString: "+label.getDescription());
                Log.d("galler", "convertResponseToString: "+label.getBoundingPoly().getVertices());
                //9자리번호 찾기  func find()getvertices.get(0,1,2,3,4,)로
                //투약,투여,횟수시기,일수, x,y좌표 가져오기
                findcount++;
            }
            for(int i=0;i<5;i++){
                Log.d("gallery", "convertResponseToString: "+ Y_eachmedicine[i]);
                Log.d("gallery", "convertResponseToString: "+ medicine[i]);
            }

            for (EntityAnnotation label : labels) {
                int labelX=label.getBoundingPoly().getVertices().get(0).getX();
                int labelX2=label.getBoundingPoly().getVertices().get(1).getX();
                int labelY=label.getBoundingPoly().getVertices().get(0).getY();
                for(int i=0;i<arrcount;i++){
                    if((Y_eachmedicine[i]-10<labelY) && (Y_eachmedicine[i]+10>labelY)){
                        Log.d(TAG, "convertResponseToString: labely"+labelY);
                        for(int j=0;j<arrcount;j++){
                            if((vertixArray[j][0]-40<labelX) && (vertixArray[j][1]+40>labelX2)) {
                                Log.d("gallery", "convertResponseToString: labelx"+labelX);
                                try {
                                    info_med[i][j] = Integer.parseInt(label.getDescription());
                                    Log.d("gallery", "convertResponseToString: ["+i+"]["+j+"]: "+info_med[i][j]);
                                } catch (NumberFormatException e) {

                                }
                            }
                        }

                    }
                }

            }
        } else {
            message.append("nothing");
        }

        String returnstr="";
        for(int i=0;i<arrcount;i++){
            returnstr=returnstr.concat(medicine[i]+" ");
            for(int j=0;j<arrcount;j++){
                returnstr= returnstr.concat(info_med[i][j]+" ");
            }

        }
        Log.d("gallery", "convertResponseToString: "+returnstr);
        return returnstr;
    }


    public void toolbar_search() {
        toolbar.findViewById(R.id.toolbar_history).setVisibility(View.GONE);
        toolbar.findViewById(R.id.logo).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_search).setVisibility(View.VISIBLE);
        toolbar.findViewById(R.id.toolbar_mypage).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_my_default_time).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_edit_time).setVisibility(View.GONE);
        toolbar.setBackgroundColor(Color.rgb(173,165,253));
        setup_nav(R.drawable.ic_menu);
    }

    public void toolbar_main() {
        toolbar.findViewById(R.id.toolbar_history).setVisibility(View.GONE);
        toolbar.findViewById(R.id.logo).setVisibility(View.VISIBLE);
        toolbar.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_mypage).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_my_default_time).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_edit_time).setVisibility(View.GONE);
        toolbar.setBackgroundColor(Color.rgb(255,255,255));
        setup_nav(R.drawable.ic_menu);
    }

    public void toolbar_history() {
        toolbar.findViewById(R.id.toolbar_history).setVisibility(View.VISIBLE);
        toolbar.findViewById(R.id.logo).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_mypage).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_my_default_time).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_edit_time).setVisibility(View.GONE);
        toolbar.setBackgroundColor(Color.rgb(255,255,255));
        setup_nav(R.drawable.ic_menu);
    }
    public void toolbar_mypage(){
        toolbar.findViewById(R.id.toolbar_mypage).setVisibility(View.VISIBLE);
        toolbar.findViewById(R.id.toolbar_history).setVisibility(View.GONE);
        toolbar.findViewById(R.id.logo).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_my_default_time).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_edit_time).setVisibility(View.GONE);
        toolbar.setBackgroundColor(Color.rgb(173,165,253));
        setup_nav(R.drawable.ic_menu);
    }
    public void toolbar_my_default_time(){
        toolbar.findViewById(R.id.toolbar_mypage).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_history).setVisibility(View.GONE);
        toolbar.findViewById(R.id.logo).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_my_default_time).setVisibility(View.VISIBLE);
        toolbar.findViewById(R.id.toolbar_edit_time).setVisibility(View.GONE);
        toolbar.setBackgroundColor(Color.rgb(255,255,255));
        setup_nav(R.drawable.ic_menu);
    }
    public void toolbar_edit_time(){
        toolbar.findViewById(R.id.toolbar_mypage).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_history).setVisibility(View.GONE);
        toolbar.findViewById(R.id.logo).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_search).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_my_default_time).setVisibility(View.GONE);
        toolbar.findViewById(R.id.toolbar_edit_time).setVisibility(View.VISIBLE);
        toolbar.setBackgroundColor(Color.rgb(255,255,255));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(false); //커스터마이징 하기 위해 필요
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김
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
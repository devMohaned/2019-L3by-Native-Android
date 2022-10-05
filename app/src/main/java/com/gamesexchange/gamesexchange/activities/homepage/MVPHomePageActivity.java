package com.gamesexchange.gamesexchange.activities.homepage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamesexchange.gamesexchange.BuildConfig;
import com.gamesexchange.gamesexchange.R;
import com.gamesexchange.gamesexchange.Utils.AmazonUtils;
import com.gamesexchange.gamesexchange.Utils.AppUtils;
import com.gamesexchange.gamesexchange.Utils.Constants;
import com.gamesexchange.gamesexchange.activities.DummyActivity;
import com.gamesexchange.gamesexchange.activities.legals.EulaActivity;
import com.gamesexchange.gamesexchange.activities.legals.PrivacyPolicyActivity;
import com.gamesexchange.gamesexchange.activities.legals.TermsOfUseActivity;
import com.gamesexchange.gamesexchange.activities.login.MVPLoginActivity;
import com.gamesexchange.gamesexchange.activities.messaging.UserListMessagesActivity;
import com.gamesexchange.gamesexchange.activities.mygames.MyGamesActivity;
import com.gamesexchange.gamesexchange.activities.mywishes.MyWishesActivity;
import com.gamesexchange.gamesexchange.activities.refers.ReferActivity;
import com.gamesexchange.gamesexchange.activities.register.RegisterFailedActivity;
import com.gamesexchange.gamesexchange.activities.reports.ReportActivity;
import com.gamesexchange.gamesexchange.activities.settings.SettingsActivity;
import com.gamesexchange.gamesexchange.activities.settings.UpdateUserData;
import com.gamesexchange.gamesexchange.activities.user_states.UnderMaintenanceActivity;
import com.gamesexchange.gamesexchange.activities.user_states.UpdateActivity;
import com.gamesexchange.gamesexchange.adapter.ViewPagerFragmentsAdapter;
import com.gamesexchange.gamesexchange.dialogs.LocationChangedDialog;
import com.gamesexchange.gamesexchange.dialogs.RewardsDialog;
import com.gamesexchange.gamesexchange.fragments.TradeGamesFragment;
import com.gamesexchange.gamesexchange.model.Game;
import com.gamesexchange.gamesexchange.model.User;
import com.gamesexchange.gamesexchange.rooms.AppDatabase;
import com.gamesexchange.gamesexchange.rooms.AppExecuter;
import com.gamesexchange.gamesexchange.services.AddingAllGamesService;
import com.gamesexchange.gamesexchange.services.GamesTasks;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import static com.gamesexchange.gamesexchange.Utils.FileUtils.createFile;
import static com.gamesexchange.gamesexchange.Utils.FileUtils.getFileExtension;
import static com.gamesexchange.gamesexchange.Utils.FileUtils.validateInputFileName;


public class MVPHomePageActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 25;
    private static final int MY_EXT_SOURCE_REQUEST_CODE = 990;
    private TabLayout tabLayout;
    public static User currentUser;
    public static List<Game> gameListStatic = new ArrayList<>();
    public static List<Game> removeGameListStatic = new ArrayList<>();

    public static List<Game> wishRemoveGameListStatic = new ArrayList<>();
    public static List<Game> wishgameListStatic = new ArrayList<>();
    public static Integer wishPtsCount = 0;
    public static Integer gamePtsCount = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_homepage);
        AppUtils.hideSoftKeyboard(this);
        setupFirebaseAuth();
        setupNavigationBar();
        getMyUser();
//        updateToken();
        setupViews();
        setupToolbars();
        getGamePoints();
        getWishPoints();
        initAdmob();

    }

    private void showWarningInformation()
    {
        SharedPreferences prefs = getSharedPreferences("warning", MODE_PRIVATE);
        if (prefs.getAll().size() > 0)
        {

        }else {

            final LocationChangedDialog locationChangedDialog = new LocationChangedDialog();
            locationChangedDialog.showDialog(this);
            locationChangedDialog.dialogGreatButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MVPHomePageActivity.this, UpdateUserData.class);
                    startActivity(intent);
                    locationChangedDialog.dialog.dismiss();

                    SharedPreferences.Editor editor = getSharedPreferences("warning", MODE_PRIVATE).edit();
                    editor.putString("warning_text", "already_warned");
                    editor.apply();
                }
            });
        }
    }

     /*


    AMAZON


     */

    private void showFileChooser() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}
                    , MY_EXT_SOURCE_REQUEST_CODE);
        }else {
         selectImage();
        }

    }


    private void selectImage()
    {
         /*  Intent intent = new Intent(this, FilePickerActivity.class);
            intent.putExtra(FilePickerActivity.CONFIGS, new Configurations.Builder()
                    .setCheckPermission(true)
                    .setShowImages(true)
                    .setShowVideos(false)
                    .setSingleChoiceMode(true)
                    .setSkipZeroSizeFiles(true)
                    .setSuffixes("jpeg", "jpg", "png")
                    .build());
            startActivityForResult(intent, PICK_IMAGE_REQUEST);*/

        Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
        pickImageIntent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }


    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_EXT_SOURCE_REQUEST_CODE) {

            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
              selectImage();
            } else {
                //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                Toast.makeText(this, getString(R.string.permission_rejected), Toast.LENGTH_LONG).show();
            }


        }}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            //getting the image Uri
           Uri fileUri = data.getData();
            CircleImageView circleImageView = mNavigationView.getHeaderView(0).findViewById(R.id.ID_my_profile_picture);
            Picasso.get().load(fileUri).into(circleImageView);
            uploadFileVer1("name" + SystemClock.currentThreadTimeMillis(),fileUri);
           /* ArrayList<MediaFile> files = data.getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            uploadFile(mAuth.getUid(), files.get(0).getPath(), files.get(0).getMimeType());*/
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private AmazonS3Client s3Client;

    private void initializeAmazonSDK(String accessKey, String secretKey) {
        AWSMobileClient.getInstance().initialize(this).execute();
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        s3Client = new AmazonS3Client(credentials);
    }

    private void showProfilePicture(String fileNameWithExtention) {
        CircleImageView profileCircleImageView = mNavigationView.getHeaderView(0).findViewById(R.id.ID_my_profile_picture);
        if (!fileNameWithExtention.equals(Constants.NO_PROFILE_IMAGE_FOUND)) {
            Uri url = Uri.parse(AmazonUtils.CLOUDFLARE + AmazonUtils.PROFILE_DIRECTORY + fileNameWithExtention);
            Picasso.get().load(url).into(profileCircleImageView);
        }

    }

    private void uploadFile(String fileName/*, Uri fileUri*/, String filePath, String memeFileType) {
        Context mContext = MVPHomePageActivity.this;
        TextView tvFileName = mNavigationView.getHeaderView(0).findViewById(R.id.ID_amazon_upload_photo_progress);
        tvFileName.setVisibility(View.VISIBLE);
       /* if (fileUri != null) {

            if (!validateInputFileName(mContext, fileName)) {
                return;
            }*/

           /* File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "/" + fileName);

            createFile(getApplicationContext(), fileUri, file);*/
        String fileType=memeFileType.substring(memeFileType.indexOf("/") + 1);

        File file = new File(filePath);

        try {
            file = new Compressor(this).compressToFile(file);
            CircleImageView circleImageView = mNavigationView.getHeaderView(0).findViewById(R.id.ID_my_profile_picture);
            Picasso.get().load(file).into(circleImageView);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String uniqueName = Constants.AMAZON_PROFILE_P + FirebaseDatabase.getInstance().getReference().push().getKey();

        String nameWithExtentsion = /*fileName*/uniqueName + "." + /*getFileExtension(mContext, fileUri)*/fileType;



        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();


        TransferObserver uploadObserver =
                transferUtility.upload(AmazonUtils.PROFILE_DIRECTORY + nameWithExtentsion, file);

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    Toast.makeText(mContext, getString(R.string.uploaded), Toast.LENGTH_SHORT).show();
                    tvFileName.setVisibility(View.GONE);
                    removeFileFromS3(currentUser.getProfile_image());
                    addImageNameToServer(nameWithExtentsion);
                    currentUser.setProfile_image(nameWithExtentsion);
//                        file.delete();
                } else if (TransferState.FAILED == state) {
//                        file.delete();
                    Toast.makeText(mContext, getString(R.string.uploading_failed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;

                tvFileName.setText(percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                ex.printStackTrace();
            }

        });
//        }
    }

    private void uploadFileVer1(String fileName, Uri fileUri) {
        Context mContext = MVPHomePageActivity.this;
        TextView tvFileName = mNavigationView.getHeaderView(0).findViewById(R.id.ID_amazon_upload_photo_progress);
        tvFileName.setVisibility(View.VISIBLE);

        if (fileUri != null) {

            if (!validateInputFileName(mContext, fileName)) {
                return;
            }

            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "/" + fileName);

            createFile(getApplicationContext(), fileUri, file);


        try {
            file = new Compressor(this).compressToFile(file);
            CircleImageView circleImageView = mNavigationView.getHeaderView(0).findViewById(R.id.ID_my_profile_picture);
            Picasso.get().load(file).into(circleImageView);
        } catch (IOException e) {
            e.printStackTrace();
        }


        String uniqueName = Constants.AMAZON_PROFILE_P + FirebaseDatabase.getInstance().getReference().push().getKey();

        String nameWithExtentsion = fileName + uniqueName + "." + getFileExtension(mContext, fileUri);



        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();


        TransferObserver uploadObserver =
                transferUtility.upload(AmazonUtils.PROFILE_DIRECTORY + nameWithExtentsion, file);

        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    tvFileName.setVisibility(View.GONE);
                    removeFileFromS3(currentUser.getProfile_image());
                    addImageNameToServer(nameWithExtentsion);
                    currentUser.setProfile_image(nameWithExtentsion);
//                        file.delete();
                } else if (TransferState.FAILED == state) {
//                        file.delete();
                    Toast.makeText(mContext, getString(R.string.uploading_failed), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;

                tvFileName.setText(percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                ex.printStackTrace();
            }

        });
       }
    }

    private void removeFileFromS3(String fileDestination) {
        if (!fileDestination.equals(Constants.NO_PROFILE_IMAGE_FOUND)) {
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... voids) {
                    s3Client.deleteObject(new DeleteObjectRequest(
                            AmazonUtils.BUCKET_NAME,
                            AmazonUtils.PROFILE_DIRECTORY + fileDestination));
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
//                    Toast.makeText(MVPHomePageActivity.this, "Removed", Toast.LENGTH_SHORT).show();
                }
            }.execute();

        }


    }


    private void addImageNameToServer(String fileDestination) {
        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());

        final String url =
                Constants.SERVER_BASE + "rel_update_profile_image_url.php" + Constants.SERVER_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String referralUserFirebaseId) {
//                Toast.makeText(getApplicationContext(), getString(R.string.profile_image_uploaded), Toast.LENGTH_SHORT).show();
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppUtils.checkVolleyError(MVPHomePageActivity.this,error);
            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("file_name", fileDestination);
                params.put("firebase_uid", mAuth.getUid());
                return params;
            }


        };
        requstQueue.add(stringRequest);
    }

    private void updateToken() {
        SharedPreferences prefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        String token = prefs.getString("token", "");

//        Log.e("NEW_INACTIVITY_TOKEN", token);

        if (TextUtils.isEmpty(token)) {
            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MVPHomePageActivity.this, new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String newToken = instanceIdResult.getToken();
//                    Log.e("newToken", newToken);
                    SharedPreferences.Editor editor = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE).edit();
                    if (token != null) {
                        editor.putString("token", newToken);
                        editor.apply();
                        FirebaseDatabase.getInstance().getReference()
                                .child("tokens").child(mAuth.getUid())
                                .setValue(newToken);

                    }

                }
            });
        }

    }

    private void addNewToken()
    {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(MVPHomePageActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                final String url =
                        Constants.SERVER_BASE + "rel_update_token.php?firebase_uid=" + mAuth.getUid()
                                + Constants.SERVER_AND_GET_API_KEY
                                + "&token=" + newToken;


                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(final JSONArray responseJSONArray) {


                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AppUtils.checkVolleyError(getApplicationContext(),error);
                    }
                });


                requestQueue.add(jsonArrayRequest);
            }
        });
    }

    private void setupViews() {

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        ViewPagerFragmentsAdapter adapter = new ViewPagerFragmentsAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setIcon(R.drawable.ic_gamepad_active);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_wishlist_inactive);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_handshake_inactive);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_gift_inactive);

        tabLayout.setSelectedTabIndicatorColor(R.color.bnb_so_dark_blue_color);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                checksForChangesInGamesAndWishes();
                switch (tab.getPosition()) {
                    // Games
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_gamepad_active);
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_wishlist_inactive);
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_handshake_inactive);
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_gift_inactive);

                        mGamesToolbar.setVisibility(View.VISIBLE);
                        mWishesToolbar.setVisibility(View.GONE);
                        mTradesToolbar.setVisibility(View.GONE);
                        mRewardsToolbar.setVisibility(View.GONE);
                        mToolbarTitle.setVisibility(View.GONE);

                        break;
                    case 1:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_gamepad_inactive);
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_wishlist_active);
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_handshake_inactive);
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_gift_inactive);

                        mGamesToolbar.setVisibility(View.GONE);
                        mWishesToolbar.setVisibility(View.VISIBLE);
                        mTradesToolbar.setVisibility(View.GONE);
                        mRewardsToolbar.setVisibility(View.GONE);
                        mToolbarTitle.setVisibility(View.GONE);

                        break;
                    case 2:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_gamepad_inactive);
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_wishlist_inactive);
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_handshake_active);
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_gift_inactive);

                        mGamesToolbar.setVisibility(View.GONE);
                        mWishesToolbar.setVisibility(View.GONE);
                        mTradesToolbar.setVisibility(View.VISIBLE);
                        mRewardsToolbar.setVisibility(View.GONE);
                        setToolbarText(getString(R.string.trade));
                        if (!searchView.isIconified())
                        {
                            mToolbarTitle.setVisibility(View.GONE);
                        }


                        break;

                    case 3:
                        tabLayout.getTabAt(0).setIcon(R.drawable.ic_gamepad_inactive);
                        tabLayout.getTabAt(1).setIcon(R.drawable.ic_wishlist_inactive);
                        tabLayout.getTabAt(2).setIcon(R.drawable.ic_handshake_inactive);
                        tabLayout.getTabAt(3).setIcon(R.drawable.ic_gift_active);

                        mGamesToolbar.setVisibility(View.GONE);
                        mWishesToolbar.setVisibility(View.GONE);
                        mTradesToolbar.setVisibility(View.GONE);
                        mRewardsToolbar.setVisibility(View.VISIBLE);
                        setToolbarText(getString(R.string.rewards));

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    private int getGamePoints() {
        Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_GAME_POINTS)
                .child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    gamePtsCount = dataSnapshot.getValue(Integer.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return gamePtsCount;
    }


    private int getWishPoints() {
        Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_WISH_POINTS)
                .child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    wishPtsCount = dataSnapshot.getValue(Integer.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return wishPtsCount;
    }


    private void checksForChangesInGamesAndWishes() {
        if (gameListStatic.size() > 0) {
            List<Game> list = new ArrayList<>(gameListStatic);
            postData(list);
            gameListStatic.clear();
        }

        if (removeGameListStatic.size() > 0) {
            List<Game> list = new ArrayList<>(removeGameListStatic);
            removeData(list);
            removeGameListStatic.clear();
        }


        if (wishgameListStatic.size() > 0) {
            List<Game> list = new ArrayList<>(wishgameListStatic);
            postWishesData(list);
            wishgameListStatic.clear();
        }

        if (wishRemoveGameListStatic.size() > 0) {
            List<Game> list = new ArrayList<>(wishRemoveGameListStatic);
            removeWishesData(list);
            wishRemoveGameListStatic.clear();
        }
    }






    /* -------------------------------------------------------------------------


                                        Firebase


    ----------------------------------------------------------------------------
     */

    private static final String TAG = "HomePage:";
    public static  FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private void setupFirebaseAuth() {
        currentUser = new User();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    //     if (user.isEmailVerified()) {
                    // User is signed in
//                    currentUser.setName(user.getDisplayName());
                    currentUser.setEmail(user.getEmail());
                    currentUser.setFirebase_uid(mAuth.getUid());

                } else {
                    // User is signed out
                    finish();
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);


    }


    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setOnlineStatus(Constants.FIREBASE_DB_ONLINE_STATUS_IS_ONLINE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setOnlineStatus(Constants.FIREBASE_DB_ONLINE_STATUS_IS_OFFLINE);
        checksForChangesInGamesAndWishes();
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {

            switch (tabLayout.getSelectedTabPosition()) {
                case 0:
                    super.onBackPressed();
                    break;
                case 2:
                    if (!searchView.isIconified())
                    {
                        searchView.setIconified(true);
                    }else{
                        tabLayout.getTabAt(0).select();
                    }
                    break;
                default:
                    tabLayout.getTabAt(0).select();
                    break;
            }
        }
    }

    // If 0, then user's online
    // if 1 then user's offline
    private void setOnlineStatus(int status) {


        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());

        final String url =
                Constants.SERVER_BASE + "rel_add_online_status.php" + Constants.SERVER_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), getString(R.string.couldnot_fetch), Toast.LENGTH_SHORT).show();
                AppUtils.checkVolleyError(MVPHomePageActivity.this,error);
            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("online_status", String.valueOf(status));
                if (mAuth.getUid() != null) params.put("firebase_uid", mAuth.getUid());
                else params.put("firebase_uid", currentUser.getFirebase_uid());

                return params;
            }


        };
        requstQueue.add(stringRequest);
    }


    private void setRandomText(String txt) {
        final TextView gameRandomText = findViewById(R.id.ID_splash_screen_random_game_information);

        if (txt == null)
            gameRandomText.setText(getString(R.string.welcome));
        else gameRandomText.setText(getString(R.string.failed_please_retry));
    }

    public void hideSplashScreen() {
        final RelativeLayout splashScreen = findViewById(R.id.ID_splash_screen);
        final AppBarLayout appBarLayout = findViewById(R.id.id_appbar);
        final ViewPager viewpager = findViewById(R.id.viewpager);

      /*  final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {*/

                appBarLayout.setVisibility(View.VISIBLE);
                viewpager.setVisibility(View.VISIBLE);
                splashScreen.setVisibility(View.GONE);
                mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

          /*  }
        }, 1000);*/
    }


    private void removeData(List<Game> removeGameList) {
        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());

        final String url =
                Constants.SERVER_BASE + "rel_remove_game_from_game_list.php" + Constants.SERVER_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                AppExecuter.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < removeGameList.size(); i++) {
                            String gameName = removeGameList.get(i).getGame_name();
                            AppDatabase.getAppDatabase(getApplicationContext()).gameDAO().updateCertainGames(gameName,
                                    false, "");
                        }
                    }
                });

//                Log.e("response", response);

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MVPHomePageActivity.this, getString(R.string.couldnot_fetch), Toast.LENGTH_SHORT).show();
                AppUtils.checkVolleyError(MVPHomePageActivity.this,error);
            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Gson gson = new Gson();
                String gamesJson = gson.toJson(removeGameList);
                params.put("game_name", gamesJson);
                params.put("game_firebase_uid", mAuth.getUid());
                return params;
            }

            ;
        };
//        stringRequest.setTag(REQ_TAG);
        requstQueue.add(stringRequest);


    }


    private void postData(List<Game> gameList2) {
        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());

        final String url =
                Constants.SERVER_BASE + "rel_add_game_to_game_list.php" + Constants.SERVER_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                AppExecuter.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < gameList2.size(); i++) {
                            String gameName = gameList2.get(i).getGame_name();
                            AppDatabase.getAppDatabase(getApplicationContext()).gameDAO().updateCertainGames(gameName,
                                    true, "o");
                        }
                    }
                });

//                Log.e("response:", response);
                removeGamePoint(gameList2.size() * Constants.ADDITION_OF_GAMES_COST);


            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MVPHomePageActivity.this, getString(R.string.couldnot_fetch), Toast.LENGTH_SHORT).show();
                AppUtils.checkVolleyError(MVPHomePageActivity.this,error);
            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Gson gson = new Gson();
                String gamesJson = gson.toJson(gameList2);
                params.put("game_name", gamesJson);
                params.put("game_firebase_uid", mAuth.getUid());
                return params;
            }

            ;
        };
//        stringRequest.setTag(REQ_TAG);
        requstQueue.add(stringRequest);


    }

    private void removeGamePoint(final int count) {
        Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_GAME_POINTS)
                .child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int value = dataSnapshot.getValue(Integer.class);

                    Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_GAME_POINTS)
                            .child(mAuth.getUid()).setValue(value - count);
                }/*else{
                    // User doesn't have any points before.
                    Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_GAME_POINTS)
                            .child(currentUser.getFirebase_uid()).setValue(count);
                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // WISHES


    private void postWishesData(List<Game> wishlist) {
        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());

        final String url =
                Constants.SERVER_BASE + "rel_add_game_to_wishlist.php" + Constants.SERVER_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                AppExecuter.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < wishlist.size(); i++) {
                            String gameName = wishlist.get(i).getGame_name();
                            AppDatabase.getAppDatabase(getApplicationContext()).gameDAO().updateCertainGames(gameName,
                                    true, "w");
                        }


                    }
                });
                removeWishPoint(wishlist.size() * Constants.ADDITION_OF_GAMES_COST);
//                Log.e("Response:", response);
            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MVPHomePageActivity.this, getString(R.string.couldnot_fetch), Toast.LENGTH_SHORT).show();
                AppUtils.checkVolleyError(MVPHomePageActivity.this,error);
            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Gson gson = new Gson();
                String gamesJson = gson.toJson(wishlist);
                params.put("wishlist_game_name", gamesJson);
                params.put("wishlist_game_firebase_uid", mAuth.getUid());
                return params;
            }

            ;
        };
        requstQueue.add(stringRequest);


    }

    private void removeWishesData(List<Game> wishlist) {
        RequestQueue requstQueue = Volley.newRequestQueue(getApplicationContext());

        final String url =
                Constants.SERVER_BASE + "rel_remove_game_from_wishlist.php" + Constants.SERVER_GET_API_KEY;


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AppExecuter.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < wishlist.size(); i++) {
                            String gameName = wishlist.get(i).getGame_name();
                            AppDatabase.getAppDatabase(getApplicationContext()).gameDAO().updateCertainGames(gameName,
                                    false, "");
                        }


                    }
                });
//                Log.e("Response:", response);

            }
        }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(MVPHomePageActivity.this, getString(R.string.couldnot_fetch), Toast.LENGTH_SHORT).show();
                AppUtils.checkVolleyError(MVPHomePageActivity.this,error);

            }
        }) {
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                Gson gson = new Gson();
                String gamesJson = gson.toJson(wishlist);
                params.put("wishlist_game_name", gamesJson);
                params.put("wishlist_game_firebase_uid", mAuth.getUid());

                return params;
            }

            ;
        };
        requstQueue.add(stringRequest);


    }


    private void removeWishPoint(final int count) {
        Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_WISH_POINTS)
                .child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int value = dataSnapshot.getValue(Integer.class);

                    Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_WISH_POINTS)
                            .child(mAuth.getUid()).setValue(value - count);
                }/*else{
                    // User doesn't have any points before.
                    Constants.FIREBASE_DB_BASE.child(Constants.FIREBASE_DB_GAME_POINTS)
                            .child(currentUser.getFirebase_uid()).setValue(count);
                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


  /*
   ---------------------------------------------------------------------------------


                                         VOLLEY


   --------------------------------------------------------------------------------
        */

    private void getMyUser() {
        setRandomText(null);

        TextView myName = mNavigationView.getHeaderView(0).findViewById(R.id.ID_my_name);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final String url =
                Constants.SERVER_BASE + "rel_get_user_data.php?firebase_uid=" + mAuth.getUid() + Constants.SERVER_AND_GET_API_KEY;


        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray responseJSONArray) {
                for (int i = 0; i < responseJSONArray.length(); i++) {
                    try {
                        if (responseJSONArray.getJSONObject(i).getString("user_state")
                                .equals( Constants.USER_STATE_DOES_EXIST)) {
                            initializeAmazonSDK(responseJSONArray.getJSONObject(i).getString("access_key"),
                                    responseJSONArray.getJSONObject(i).getString("secret_key"));


                            int id = responseJSONArray.getJSONObject(i).getInt("id");
                            currentUser.setId(id);
                            String location = responseJSONArray.getJSONObject(i).getString("location");
                            if (!location.toLowerCase().equals("null")) {
                                currentUser.setLocation(location);
                            } else {
                                currentUser.setLocation("null");
                            }

                            String district = responseJSONArray.getJSONObject(i).getString("district");
                            if (!district.toLowerCase().equals("null")) {
                                currentUser.setDistrict(district);
                            } else {
                                currentUser.setDistrict(getString(R.string.no_district_found));
                            }

                            String refer = responseJSONArray.getJSONObject(i).getString("referral");
                            if (!refer.toLowerCase().equals("null")) {
                                currentUser.setRefer(refer);
                            } else {
                                currentUser.setRefer("null");

                            }

                            String firstName = responseJSONArray.getJSONObject(i).getString("first_name");
                            currentUser.setFirst_name(firstName);
                            String lastName = responseJSONArray.getJSONObject(i).getString("last_name");
                            currentUser.setLast_name(lastName);

                            myName.setText(firstName + " " + lastName);

                            String lastCollected = responseJSONArray.getJSONObject(i).getString("last_collected");
                            String accumlatedDays = responseJSONArray.getJSONObject(i).getString("accumlated_days");
                            String phone = responseJSONArray.getJSONObject(i).getString("phone");

                            currentUser.setLast_collected(lastCollected);
                            currentUser.setAccumlated_days(accumlatedDays);
                            currentUser.setPhoneNumber(phone);

                            String profileImage = responseJSONArray.getJSONObject(i).getString("profile_image");
                            if (!profileImage.toLowerCase().equals("null")) {
                                currentUser.setProfile_image(profileImage);
                                showProfilePicture(profileImage);
                            } else {
                                currentUser.setProfile_image(Constants.NO_PROFILE_IMAGE_FOUND);
                                showProfilePicture(Constants.NO_PROFILE_IMAGE_FOUND);
                            }

                            String token = responseJSONArray.getJSONObject(i).getString("token");
                            if (token.equals("null"))
                            {
                                addNewToken();
                            }

                            hideSplashScreen();
                            setupSearchView();
                            showWarningInformation();
                        }else if (responseJSONArray.getJSONObject(i).getString("user_state").equals(Constants.USER_STATE_DOES_NOT_EXIST))
                        {
                            Intent intent = new Intent(MVPHomePageActivity.this, RegisterFailedActivity.class);
                            startActivity(intent);
                            finish();

                        } else if (responseJSONArray.getJSONObject(i).getString("user_state").equals(Constants.USER_STATE_NEED_UPDATE))
                        {
                            Intent intent = new Intent(MVPHomePageActivity.this, UpdateActivity.class);
                            startActivity(intent);
                            finish();

                        }else if (responseJSONArray.getJSONObject(i).getString("user_state").equals(Constants.USER_STATE_UNDER_MAINTENANCE))
                        {
                            Intent intent = new Intent(MVPHomePageActivity.this, UnderMaintenanceActivity.class);
                            startActivity(intent);
                            finish();

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        setRandomText("error");
                    }
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.e("HomePageVolleyE", error.toString());
                setRandomText("error");
               /* if (error.toString().contains("org.json.JSONException: Value <br of type java.lang.String cannot be converted to JSONArray")) {
                    removeFirebaseUser();
                }*/
               AppUtils.checkVolleyError(MVPHomePageActivity.this,error);
            }
        });


        requestQueue.add(jsonArrayRequest);


    }

    private void initAdmob() {

        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        MobileAds.initialize(this, "ca-app-pub-7003413723788424~4521918965");
    }

   /* private void removeFirebaseUser() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential("user@example.com", "password1234");

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mAuth.signOut();
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
//                                            Log.d(TAG, "User account deleted.");
                                        }
                                    }
                                });

                    }
                });
    }
*/
    RelativeLayout mGamesToolbar, mWishesToolbar,mTradesToolbar, mRewardsToolbar;
    TextView mToolbarTitle;

    private void setupToolbars()
    {
        mGamesToolbar = findViewById(R.id.ID_games_toolbar);
        mWishesToolbar = findViewById(R.id.ID_wishes_toolbar);
        mToolbarTitle = findViewById(R.id.ID_textView_toolbar_title);
        mTradesToolbar = findViewById(R.id.ID_trades_toolbar);
        mRewardsToolbar = findViewById(R.id.ID_rewards_toolbar);

        ImageView navigationBar = findViewById(R.id.ID_nav_menu_image);
        navigationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        ImageView referPoints = findViewById(R.id.ID_rewards_toolbar_teamwork);
        referPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               goTo(ReferActivity.class);
            }
        });

        setupToolbarItems();
    }

    public static EditText myGamesSearchEditText, wishListSearchEditText;
    public static TextView mGamesPts, mWishPts;

    private void setupToolbarItems()
    {
        myGamesSearchEditText = findViewById(R.id.ID_searchview_games);
        wishListSearchEditText = findViewById(R.id.ID_searchview_wishes);
        mGamesPts = findViewById(R.id.ID_games_count);
        mWishPts = findViewById(R.id.ID_wishes_count);

    }

    private void hideAllLayouts() {
        mGamesToolbar.setVisibility(View.GONE);
        mWishesToolbar.setVisibility(View.GONE);
        mToolbarTitle.setText("");
    }

    private void setToolbarText(String text) {
        mToolbarTitle.setVisibility(View.VISIBLE);
        mToolbarTitle.setText(text);
    }

    public static SearchView searchView;
    public static SearchView.SearchAutoComplete searchAutoComplete;
    public static int districtSearched = 5000;
    private void setupSearchView()
    {

        // Get SearchView object.
         searchView = (SearchView) findViewById(R.id.ID_searchview_trades);

        // Get SearchView autocomplete object.
          searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);

        // Create a new ArrayAdapter and add data to search auto complete object.
        try {
            String dataArr[] = AppUtils.LocationUtils.getDistrictArray(this, Integer.parseInt(currentUser.getLocation()));
            ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
            searchAutoComplete.setAdapter(newsAdapter);



            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mToolbarTitle.setVisibility(View.GONE);
                }
            });
            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    mToolbarTitle.setVisibility(View.VISIBLE);

                    return false;
                }
            });

        }catch (Exception e)
        {
            // SHOW ALERT DIALOG WITH WRONG DISTRICT ( TO CHANGE IT )

        }


    }


    NavigationView mNavigationView;
    DrawerLayout mDrawerLayout;

    private void setupNavigationBar() {


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.ID_drawer_layout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);


        mNavigationView = (NavigationView) findViewById(R.id.ID_navigation_view);
        mNavigationView.setItemIconTintList(null);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                checksForChangesInGamesAndWishes();
                switch (menuItem.getItemId()) {
                    case R.id.ID_nav_home:
//                        setupHome();
                        break;
                    case R.id.ID_nav_messages:
                        goTo(UserListMessagesActivity.class);
                        break;

                    case R.id.ID_nav_my_games:
                        goTo(MyGamesActivity.class);
                        break;

                    case R.id.ID_nav_my_wishes:
                        goTo(MyWishesActivity.class);
                        break;


                    case R.id.ID_nav_settings:
                        goTo(SettingsActivity.class);
                        break;

                    case R.id.ID_nav_privacy_policy:
                        goTo(PrivacyPolicyActivity.class);
                        break;

                    case R.id.ID_nav_terms:
                        goTo(TermsOfUseActivity.class);
                        break;

                    case R.id.ID_nav_report:
                        goTo(ReportActivity.class);
                        break;

                    case R.id.ID_nav_terms_and_conditions:
                        goTo(EulaActivity.class);
                        break;
                    case R.id.ID_nav_logout:
                        signOut();
                        break;

                    case R.id.ID_nav_refer:
                        goTo(ReferActivity.class);
                        break;
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
                AppUtils.hideSoftKeyboard(MVPHomePageActivity.this);
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        Menu menu = mNavigationView.getMenu();
        MenuItem messagesItemShare = menu.findItem(R.id.ID_nav_messages);
        MenuItem referItemShare = menu.findItem(R.id.ID_nav_refer);


        // Get the custom view for menu item action view
        View v = View.inflate(this, R.layout.menu_item_messaging_count_txtview, null);
        View v2 = View.inflate(this, R.layout.menu_item_refer_point_txtview, null);

        // Get the edit and delete image button reference from custom view

        TextView mReferPoints = v2.findViewById(R.id.ID_refer_count);
        mReferPoints.setGravity(Gravity.END);
//        mReferPoints.setTextColor(getResources().getColor(R.color.red));

        referItemShare.setActionView(v2);


        TextView mMessagesCount = v.findViewById(R.id.ID_messaging_count);
        mMessagesCount.setGravity(Gravity.END);
        mMessagesCount.setTextColor(getResources().getColor(R.color.red));

        checkReadibility(mMessagesCount);

        messagesItemShare.setActionView(v);


                    CircleImageView circleImageView = mNavigationView.getHeaderView(0).findViewById(R.id.ID_my_profile_picture);

                    circleImageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            showFileChooser();
                        }
                    });


    }

    private void checkReadibility(TextView count)
    {
        SharedPreferences prefs = getSharedPreferences(Constants.SHARED_PREF_MESSAGES, MODE_PRIVATE);
        if (!prefs.getAll().isEmpty())
        {
            // I've notification
            count.setVisibility(View.VISIBLE);
            count.setText(String.valueOf(prefs.getAll().size()));
        }else{
            count.setVisibility(View.GONE);
        }
    }

    private void goTo(Class context) {
        Intent intent = new Intent(this, context);
        startActivity(intent);
    }

    private void signOut()
    {
        removeAllGamesLocally();

        SharedPreferences tokenPrefs = getSharedPreferences("TOKEN_PREF", MODE_PRIVATE);
        SharedPreferences msgPrefs = getSharedPreferences(Constants.SHARED_PREF_MESSAGES, MODE_PRIVATE);
        tokenPrefs.edit().clear().apply();
        msgPrefs.edit().clear().apply();

        mAuth.signOut();
        Intent intent = new Intent(this, MVPLoginActivity.class);
        startActivity(intent);
       finish();
    }


    private void removeAllGamesLocally()
    {
        AppExecuter.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                    AppDatabase.getAppDatabase(getApplicationContext()).gameDAO().removeAllGamesAndWishes(
                             false, "");
            }
        });



    }





}

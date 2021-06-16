package com.placements.abhyaas;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.placements.abhyaas.Home.AddRecordActivity;
import com.placements.abhyaas.addcategory.Addcategory_Fragment;
import com.placements.abhyaas.addcompany.Addcompany_Fragment;
import com.placements.abhyaas.Home.HomeFragment;
import com.placements.abhyaas.Login.Intro_Signin;
import com.placements.abhyaas.profile.MyProfile;
import com.placements.abhyaas.viewcompany.AppointmentFragment;
import com.placements.abhyaas.viewcompany.RecentAppointment_Fragment;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Fragment selectorFragment;
    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private CoordinatorLayout coordinatorLayout;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    static TextView navUsername;
    private FloatingActionButton btn_upload;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_main);
        btn_upload=findViewById(R.id.fabupload);
        getDrawer_started();
        getProfile();
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);





    }

    private void getProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
            View headerView = navigationView.getHeaderView(0);
            navUsername = (TextView) headerView.findViewById(R.id.user_name);
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            db.collection("users")
                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                String name = (document.getString("Name"));
                                String category=document.getString("Category");
                                navUsername.setText(name);
                                if (category.equals("Student"))
                                    navigationView.inflateMenu(R.menu.student_items);
                                else
                                {
                                    navigationView.inflateMenu(R.menu.tpo_items);
                                    upload();
                                }

                            }
                        }
                    });
        }
    }

    public static String getName() {
        return navUsername.getText().toString();
    }

    private void getDrawer_started() {

        drawerLayout = findViewById(R.id.drawerlayout);
        coordinatorLayout = findViewById(R.id.coordinator);
        toolbar = findViewById(R.id.toolbar);
        // frameLayout= findViewById(R.id.framelayout);
        navigationView = findViewById(R.id.navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new HomeFragment()).addToBackStack("Dashboard").commit();


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        setUpToolbar();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();




        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectorFragment = new HomeFragment();
                if (item.getItemId() == R.id.logout) {
                    signOut();
                }
                else if(item.getItemId() == R.id.add_company)
                {
                    selectorFragment= new Addcompany_Fragment();
                }
               else if(item.getItemId() == R.id.add_category)
                {
                    selectorFragment= new Addcategory_Fragment();
                }
                else if(item.getItemId() == R.id.view_company)
                {
                    selectorFragment= new AppointmentFragment();
                }
                else if(item.getItemId() == R.id.applied_companies)
                {
                    selectorFragment= new RecentAppointment_Fragment();
                }
                else if(item.getItemId() == R.id.dashboard)
                {
                    selectorFragment= new HomeFragment();
                }
                else if(item.getItemId() == R.id.edit_company)
                {
                    selectorFragment= new AppointmentFragment();
                }
                else if(item.getItemId() == R.id.profile)
                {
                    selectorFragment= new MyProfile();
                }
                else if (item.getItemId() == R.id.about) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("About Abhyaas")
                            .setMessage("If you have any queries or want to contact us, you can email to tarunc.4406@gmail.com");

                    builder.create().show();
                    selectorFragment=new HomeFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectorFragment).addToBackStack("Profile Setup").commit();
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        setTitle("Abhyaas");
        (getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colororange));



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(MainActivity.this,"Signout sucessfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Intro_Signin.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }
    public void upload()
    {
        btn_upload=findViewById(R.id.fabupload);
        btn_upload.setVisibility(View.VISIBLE);
        btn_upload.setClickable(true);
        Dexter.withActivity(MainActivity.this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        btn_upload.setOnClickListener(view -> {
                            if(report.areAllPermissionsGranted()){
                                String name=getIntent().getStringExtra("Name");
                                //Toast.makeText(SoundMeterActivity.this,"Name:--"+name,Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext(), AddRecordActivity.class);
                                startActivity(intent);

                            }else{
                                Toast.makeText(MainActivity.this, "we need all permissions to continue", Toast.LENGTH_SHORT).show();
                                Log.d("Denied Permissions", "Permission list: "+report.getDeniedPermissionResponses().get(0).getPermissionName());
                            }

                        });
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).withErrorListener(dexterError -> {
            Log.e("Dexter", "There was an error: " + dexterError.toString());
            Toast.makeText(MainActivity.this, "Error :" + dexterError.toString(), Toast.LENGTH_SHORT).show();
        })
                .onSameThread()
                .check();
    }
}
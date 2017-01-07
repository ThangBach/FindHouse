package com.example.thangbach.findhouse.VIEW;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thangbach.findhouse.Adapter.MenuAdapter;
import com.example.thangbach.findhouse.DAO.Address;
import com.example.thangbach.findhouse.DAO.Post;
import com.example.thangbach.findhouse.DAO.User;
import com.example.thangbach.findhouse.Dialog.FindDialog;
import com.example.thangbach.findhouse.PROVIDER.MenuProvider;
import com.example.thangbach.findhouse.R;
import com.example.thangbach.findhouse.SERVICE.UserIMP;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, View.OnClickListener {

    SupportMapFragment supportMapFragment;

    android.app.FragmentTransaction fragmentTransaction;
    FragmentManager manager = getSupportFragmentManager();

    de.hdodenhof.circleimageview.CircleImageView main_image_person;
    ImageView imageView;

    HashMap<com.example.thangbach.findhouse.DAO.MenuItem, List<com.example.thangbach.findhouse.DAO.MenuItem>> menu_category;
    List<com.example.thangbach.findhouse.DAO.MenuItem> menu_list;
    ExpandableListView exp_list;
    MenuAdapter menuAdapter;

    String addressData="";
    TextView    menu_trangchu,
                menu_canhan,
                menu_thich,
                menu_xungquanh,
                menu_caidat,
                menu_dangxuat,
                txt_name;

    Toolbar     toolbar;

    android.app.Fragment fragment;

    public static FirebaseAuth mAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    User userCurrent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        UserIMP.myData= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user!=null){
            userCurrent=new User(user.getUid(),
                    user.getProviderId(),
                    user.getDisplayName(),
                    "",
                    "",
                    user.getEmail());
            String ten=userCurrent.getUserName();
        }

        supportMapFragment=SupportMapFragment.newInstance();

        setSupportActionBar(toolbar);

        menu_trangchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.Fragment fragment=null;
                fragment=new FragmentHome();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
                onBackPressed();
            }
        });
        menu_xungquanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.Fragment fragment=null;
                fragment=new FragmentAround();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
                onBackPressed();
            }
        });
        menu_thich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.Fragment fragment=null;
                fragment=new FragmentFavorite();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
                onBackPressed();
            }
        });
        menu_canhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.Fragment fragment=null;
                fragment=new FragmentAccountDetail();
                fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
                onBackPressed();
            }
        });
        menu_dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (user!=null){
//                    mAuth.signOut();
////                    LoginManager.getInstance().logOut();
//                }
                onBackPressed();
            }
        });


        FragmentHome fragment=new FragmentHome();
        fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
        String data=addressData;

//        //exp_list= (ExpandableListView) findViewById(R.id.exp_list);
//        menu_category=MenuProvider.getInfo();
//        menu_list=new ArrayList<com.example.thangbach.findhouse.DAO.MenuItem>(menu_category.keySet());
//        menuAdapter =new MenuAdapter(this,menu_category,menu_list);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        supportMapFragment.getMapAsync(this);
    }

    public void anhXa(){
        menu_caidat=(TextView)findViewById(R.id.menu_caidat);
        menu_canhan=(TextView)findViewById(R.id.menu_canhan);
        menu_thich=(TextView)findViewById(R.id.menu_like);
        menu_trangchu=(TextView)findViewById(R.id.menu_trangchu);
        menu_xungquanh=(TextView)findViewById(R.id.menu_xungquanh);
        menu_dangxuat=(TextView)findViewById(R.id.menu_dangxuat);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        main_image_person=(de.hdodenhof.circleimageview.CircleImageView)findViewById(R.id.main_image_person);


    }

    public void addPost(Post post){
        UserIMP.myData.child("POST").push().setValue(post);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void showFindDialog(){
        FindDialog findDialog=new FindDialog();
        findDialog.show(getFragmentManager(),"Tìm đường");
    }

    public void goLogin(){
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem menuItem=menu.findItem(R.id.action_login);
//        menuItem.setVisible(true);
        Intent intent=getIntent();
        String loginBy=intent.getStringExtra("USER");
        if (loginBy!=null){
            menuItem.setIcon(R.drawable.logout_52);
        }else {
            menuItem.setIcon(R.drawable.login_48);
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_find) {
            showFindDialog();
            return true;
        }
        if(id==R.id.action_login){


            Intent intent=getIntent();
            String x=intent.getStringExtra("USER");
            if (item.getIcon().getCurrent().toString().equals(R.drawable.login_48)){
                goSigin();
            }
            if (x!=null){
                switch (x){
                    case "FIREBASE":{
                        x=null;
                        mAuth.signOut();
                        item.setIcon(R.drawable.login_48);
                    }break;
                    case "FB":{
                        x=null;
                        LoginManager.getInstance().logOut();
                        item.setIcon(R.drawable.login_48);
                    }break;
                }
            }else {
                goSigin();
            }


        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            FragmentHome fragment=new FragmentHome();
            fragmentTransaction=getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_gallery) {
            FragmentAccountDetail fragment=new FragmentAccountDetail();
            fragmentTransaction=getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container,fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {
            LoginActivity.mAuth.signOut();
            LoginManager.getInstance().logOut();
            goLogin();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    public void goSigin(){
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void saveData(String data) {
        if (data!=null || data!=""){
            addressData=data;
        }
    }

    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.menu_trangchu :{
//                fragment=new FragmentHome();
//            }break;
//            case R.id.menu_canhan:{
//                fragment=new FragmentAccountDetail();
//            }
//            case R.id.menu_like:{
//                fragment=new FragmentFavorite();
//            }
//            case R.id.menu_xungquanh:{
//                fragment=new FragmentAround();
//            }
//            default: fragment=new FragmentHome();
//        }
//        fragmentTransaction = getFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
//        onBackPressed();
    }
}

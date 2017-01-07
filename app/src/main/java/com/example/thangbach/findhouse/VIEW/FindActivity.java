package com.example.thangbach.findhouse.VIEW;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thangbach.findhouse.Adapter.RecyclerAdapter;
import com.example.thangbach.findhouse.DAO.Post;
import com.example.thangbach.findhouse.R;
import com.example.thangbach.findhouse.SERVICE.UserIMP;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThangBach on 11/2/2016.
 */

public class FindActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerClickListener {

    ImageButton btn_trangchu,
                btn_timkiem;



    String diskey;
    String addressfind;

    DecimalFormat precision = new DecimalFormat("#,#");

    final ArrayList<Post> posts=new ArrayList<Post>();
    final ArrayList<LatLng> latLngs=new ArrayList<LatLng>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        Intent intent=getIntent();
        addressfind=intent.getStringExtra("Address");
        diskey=intent.getStringExtra("DIS");

        anhXa();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_find);
        mapFragment.getMapAsync(this);

        btn_trangchu.setOnClickListener(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {


        final View marker = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.maker_item, null);
        final TextView numTxt = (TextView) marker.findViewById(R.id.num_txt);

        Geocoder gc=new Geocoder(getApplication());
        List<Address> addressList=null;
        if(gc.isPresent()){
            try {
                addressList=gc.getFromLocationName(addressfind,1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address=addressList.get(0);
            LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        }

        UserIMP.myData.child("POST").orderByChild("postDate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post post=dataSnapshot.getValue(Post.class);
                post.setPostID(dataSnapshot.getKey());
                Geocoder gc=new Geocoder(getApplication());
                List<Address> addressList=null;

                if(post.getDistrictID().equals(diskey)==true){
                    posts.add(post);
                    if(gc.isPresent()) {
                        try {
                            addressList = gc.getFromLocationName(post.getPostAddress().toString(), 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Address address = addressList.get(0);
                        LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                        Float gia= Float.valueOf(Integer.valueOf(post.getPostPrice().substring(0,2)));
                        numTxt.setText(precision.format(gia)+"Tr");
                        googleMap.addMarker(new MarkerOptions()
                                .position(latLng)
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.home_flash))
                                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(FindActivity.this,marker)))
                                .title(dataSnapshot.getKey())

                        );

                        //Toast.makeText(FindActivity.this, "" + latLng.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Post post=dataSnapshot.getValue(Post.class);
                posts.add(post);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        googleMap.setOnMarkerClickListener(this);
    }


    private void anhXa(){
        btn_timkiem=(ImageButton)findViewById(R.id.findac_btn_timkiem);
        btn_trangchu=(ImageButton)findViewById(R.id.findac_btn_back);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.findac_btn_back:{
                onBackPressed();
            }break;
            case R.id.findac_btn_timkiem:{

            }
        }
    }

    private ArrayList<LatLng> getLatLng(){
        //final ArrayList<LatLng> latLngs=new ArrayList<LatLng>();

        UserIMP.myData.child("POST").orderByChild("postDate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post post=dataSnapshot.getValue(Post.class);
                post.setPostID(dataSnapshot.getKey());
                Geocoder gc=new Geocoder(getApplication());
                List<Address> addressList=null;

                if(post.getDistrictID().equals(diskey)==true){
                    if(gc.isPresent()) {
                        try {
                            addressList = gc.getFromLocationName(post.getPostAddress().toString(), 1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Address address = addressList.get(0);
                        LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                        latLngs.add(latLng);
                        //Toast.makeText(FindActivity.this, "" + latLng.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Post post=dataSnapshot.getValue(Post.class);
                post.setPostID(dataSnapshot.getKey());
                Geocoder gc=new Geocoder(getApplication());
                List<Address> addressList=null;

                if(post.getDistrictID().equals(diskey)==true){
                    if(gc.isPresent()) {
                        try {
                            addressList = gc.getFromLocationName(post.getPostAddress().toString(), 1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Address address = addressList.get(0);
                        LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                        latLngs.add(latLng);
                        //Toast.makeText(FindActivity.this, "" + latLng.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return  latLngs;
    }

    private ArrayList<Post> getPost(){
         //final ArrayList<Post> posts=new ArrayList<Post>();

        UserIMP.myData.child("POST").orderByChild("postDate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post post=dataSnapshot.getValue(Post.class);
                post.setPostID(dataSnapshot.getKey());
                Geocoder gc=new Geocoder(getApplication());
                List<Address> addressList=null;

                if(post.getDistrictID().equals(diskey)==true){
                    //Toast.makeText(FindActivity.this, ""+post.getDistrictID().toString(), Toast.LENGTH_SHORT).show();
                    posts.add(post);
                    if(gc.isPresent()) {
                        try {
                            addressList = gc.getFromLocationName(post.getPostAddress().toString(), 1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Address address = addressList.get(0);
                        LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
                        latLngs.add(latLng);
                        //Toast.makeText(FindActivity.this, "" + latLng.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Post post=dataSnapshot.getValue(Post.class);
                posts.add(post);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return posts;
    }

    private  LatLng addressTocoordinates(String postAddress){
        LatLng latLng=null;

        Geocoder gc=new Geocoder(this);
        List<Address> addressList=null;
        if(gc.isPresent()){
            try {
                addressList=gc.getFromLocationName("",1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address=addressList.get(0);
            latLng=new LatLng(address.getLatitude(),address.getLongitude());
        }
        return latLng;
    }

    public static Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT, android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }



    @Override
    public boolean onMarkerClick(Marker marker) {
        // Retrieve the data from the marker.

        Intent intent=new Intent(FindActivity.this,DetailPostActivity.class);
        intent.putExtra("KEYPOST",marker.getTitle());
        startActivity(intent);
        //Toast.makeText(this, ""+marker.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
    }
}

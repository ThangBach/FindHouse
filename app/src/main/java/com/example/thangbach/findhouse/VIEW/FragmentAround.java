package com.example.thangbach.findhouse.VIEW;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thangbach.findhouse.DAO.GPSTracker;
import com.example.thangbach.findhouse.DAO.Post;
import com.example.thangbach.findhouse.R;
import com.example.thangbach.findhouse.SERVICE.UserIMP;
import com.google.android.gms.drive.internal.StringListResponse;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

public class FragmentAround extends Fragment implements OnMapReadyCallback,GoogleMap.OnPoiClickListener,GoogleMap.OnMarkerClickListener {

    private static View view;
    GPSTracker gps;
    private LatLng myLocation;
    HashMap<String,String> kmHashMap=new HashMap<String, String>();

    MaterialSpinner spinner;

    DecimalFormat precision = new DecimalFormat("#,#");

    GoogleMap googleMap;
    MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_around, container, false);
        UserIMP.myData= FirebaseDatabase.getInstance().getReference();

        spinner = (MaterialSpinner)view.findViewById(R.id.spinner);
//        String[] items={"1 Km", "3 Km", "5 Km", "10 Km", "15 Km","20 Km", "Tất cả"};
        spinner.setItems("1 Km", "3 Km", "5 Km", "10 Km", "15 Km","20 Km", "Tất cả");
        kmHashMap.put("1 Km","1");
        kmHashMap.put("3 Km","3");
        kmHashMap.put("5 Km","5");
        kmHashMap.put("10 Km","10");
        kmHashMap.put("15 Km","15");
        kmHashMap.put("20 Km","20");
        kmHashMap.put("Tất cả","0");

        gps = new GPSTracker(getActivity());
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            myLocation=new LatLng(latitude,longitude);
            Toast.makeText(getActivity(), ""+myLocation, Toast.LENGTH_SHORT).show();
        }
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view=inflater.inflate(R.layout.fragment_around, container, false);
        } catch (InflateException e) {
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        MapFragment mapFragment=(MapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        mapView=(MapView)view.findViewById(R.id.map);
        if(mapView!=null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        MapsInitializer.initialize(getActivity());

        final View pricemarker = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.maker_item, null);
        final TextView numTxt = (TextView) pricemarker.findViewById(R.id.num_txt);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,14));
        googleMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.geo_32))
                .title("Vị trí hiện tại của bạn")
        );
        googleMap.addCircle(new CircleOptions()
                .center(myLocation)
                .radius(100)
                .strokeColor(Color.RED)
                .fillColor(Color.BLUE));
        googleMap.setMyLocationEnabled(true);
        UserIMP.myData.child("POST").orderByChild("postDate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post post=dataSnapshot.getValue(Post.class);
                post.setPostID(dataSnapshot.getKey());

                googleMap.addMarker(new MarkerOptions()
                        .position(myLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.geo_32))
                        .title("Vị trí hiện tại của bạn")
                );

                Geocoder gc=new Geocoder(getActivity());
                List<Address> addressList=null;

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
                    double distance=CalculationByDistance(myLocation,latLng);
                    //double km= Double.parseDouble(kmHashMap.get(item));
                    List<LatLng> latLngs=new ArrayList<LatLng>();
                    //latLngs.add(latLng);
                            googleMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(),pricemarker)))
                                    .title(dataSnapshot.getKey())

                            );

                    if (distance>0){
//                        Marker marker = googleMap.addMarker(new MarkerOptions()
//                                .position(latLng)
//                                .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(),pricemarker)))
//                                .title(dataSnapshot.getKey())
//                        );
//                        marker.remove();
                    }else {
//                        if (distance<0){
//                            latLngs.add(latLng);
//                            googleMap.addMarker(new MarkerOptions()
//                                    .position(latLng)
//                                    .icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(),pricemarker)))
//                                    .title(dataSnapshot.getKey())
//
//                            );
//                        }
                    }


                    //Toast.makeText(FindActivity.this, "" + latLng.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Post post=dataSnapshot.getValue(Post.class);
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
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override public void onItemSelected(MaterialSpinner view, int position, long id, final String item) {
                Snackbar.make(view, "Đang tìm trong bán kính " + item, Snackbar.LENGTH_LONG).show();
                googleMap.clear();

            }
        });
        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public void onPoiClick(PointOfInterest pointOfInterest) {
        Toast.makeText(getActivity(), ""+pointOfInterest.name.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Retrieve the data from the marker.

        Intent intent=new Intent(getActivity(),DetailPostActivity.class);
        intent.putExtra("KEYPOST",marker.getTitle());
        startActivity(intent);
//        Toast.makeText(getActivity(), ""+marker.getTitle(), Toast.LENGTH_SHORT).show();
        return true;
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

    private double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
//        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
//                + " Meter   " + meterInDec);
//        Toast.makeText(getActivity(), "" + valueResult + "   KM  " + kmInDec
//                + " Meter   " + meterInDec, Toast.LENGTH_SHORT).show();
        return Radius * c;
    }
}

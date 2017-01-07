package com.example.thangbach.findhouse.VIEW;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thangbach.findhouse.DAO.GPSTracker;
import com.example.thangbach.findhouse.DAO.Route;
import com.example.thangbach.findhouse.R;
import com.example.thangbach.findhouse.SERVICE.DirectionFinderListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.example.thangbach.findhouse.SERVICE.DirectionFinder;

/**
 * Created by ThangBach on 10/25/2016.
 */

public class ConductorActivity extends AppCompatActivity implements View.OnClickListener,OnMapReadyCallback,DirectionFinderListener {

    TextView txt_parent;
    private GoogleMap mMap;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;


    ImageButton btn_find;

    private LatLng myLocation;
    private Location location;
    private LocationManager locationManager;
    private LocationListener locationListener;

    GPSTracker gps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conductor);

        //locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);

        final LatLng latLng1=new LatLng(10.811099, 106.690450);
        final LatLng latLng2=new LatLng(10.802795, 106.709934);

        anhXa();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_conductor);
        mapFragment.getMapAsync(this);

        txt_parent.setOnClickListener(this);
        //sendRequest(("10.809666, 106.694851"),("10.815873, 106.695752"));

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                String Address=intent.getStringExtra("Address");

                gps = new GPSTracker(ConductorActivity.this);
                if(gps.canGetLocation()){

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    myLocation=new LatLng(latitude,longitude);
//                    CalculationByDistance(myLocation,addressTocoordinates());
//                    Toast.makeText(ConductorActivity.this, ""+CalculationByDistance(myLocation,addressTocoordinates()), Toast.LENGTH_SHORT).show();
                    sendRequest((latitude+", "+longitude),Address);
                }
            }
        });

    }

    private void anhXa(){
        txt_parent=(TextView)findViewById(R.id.conduc_txt_title_parent);
        btn_find=(ImageButton)findViewById(R.id.conduc_btn_find);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.conduc_txt_title_parent:{
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.","Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.home_flash);
        Bitmap bitmap= Bitmap.createScaledBitmap(icon, 100, 100, false);

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.conduc_txt_min)).setText(route.duration.text);
            ((TextView) findViewById(R.id.conduc_txt_km)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.scooter_50))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.parseColor("#85DB18")).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = addressTocoordinates();

        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.home_flash);
        Bitmap bitmap= Bitmap.createScaledBitmap(icon, 100, 100, false);

        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        originMarkers.add(mMap.addMarker(new MarkerOptions()
                .title("Phòng Đang Tìm")
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap)))
                );

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

//        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(Location location) {
//                myLocation=new LatLng(location.getLatitude(),location.getLongitude());
//                Toast.makeText(ConductorActivity.this, ""+myLocation.latitude, Toast.LENGTH_SHORT).show();
//                String mygps=location.getLatitude()+", "+location.getLongitude();
//                String myHouse= String.valueOf(addressTocoordinates());
//                //sendRequest( mygps,myHouse);
//            }
//        });
    }

    private void sendRequest(String mygps, String myhouse) {
        String origin = mygps;
        String destination = myhouse;
        if (origin.isEmpty()) {
            Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (destination.isEmpty()) {
            Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        Toast.makeText(this, "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec, Toast.LENGTH_SHORT).show();
        return Radius * c;
    }

    private  LatLng addressTocoordinates(){
        LatLng latLng=null;

        Intent intent=getIntent();
        String myAddress=intent.getStringExtra("Address");

        Geocoder gc=new Geocoder(this);
        List<Address> addressList=null;
        if(gc.isPresent()){
            try {
                addressList=gc.getFromLocationName(myAddress,1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address=addressList.get(0);
            latLng=new LatLng(address.getLatitude(),address.getLongitude());
        }
        return latLng;
    }


    public void myGPS() {

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location lastLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (lastLocation != null)
        {
            myLocation= new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            Toast.makeText(this, ""+myLocation.latitude, Toast.LENGTH_SHORT).show();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))      // Sets the center of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

}

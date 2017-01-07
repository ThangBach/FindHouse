package com.example.thangbach.findhouse.Dialog;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thangbach.findhouse.DAO.Address;
import com.example.thangbach.findhouse.DAO.City;
import com.example.thangbach.findhouse.DAO.District;
import com.example.thangbach.findhouse.R;
import com.example.thangbach.findhouse.SERVICE.Communicator;
import com.example.thangbach.findhouse.SERVICE.UserIMP;
import com.example.thangbach.findhouse.VIEW.FindActivity;
import com.example.thangbach.findhouse.VIEW.FragmentAround;
import com.example.thangbach.findhouse.VIEW.MainActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.thangbach.findhouse.SERVICE.UserIMP.myData;

/**
 * Created by THANG_BACH on 10/13/16.
 */

public class FindDialog extends DialogFragment {

    Spinner sp_thanhpho,
            sp_quan;

    EditText edt_tenduong;

    Button btn_timkiem;


    ArrayList<City> cities=new ArrayList<City>();
    ArrayList<String> cityNames=new ArrayList<String>();

    ArrayList<District> districts=new ArrayList<District>();
    ArrayList<String> disNames=new ArrayList<>();

    HashMap<String,String> cityHashMap=new HashMap<String, String>();
    HashMap<String,String> disHashMap=new HashMap<String, String>();

    String cityID;

    Communicator communicator;
    com.seatgeek.placesautocomplete.PlacesAutocompleteTextView placesAutocomplete;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.dialog_find_address,container,false);

        UserIMP.myData= FirebaseDatabase.getInstance().getReference();


        anhXa(view);
        placesAutocomplete.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(final Place place) {
                        // do something awesome with the selected place
                        placesAutocomplete.setText("");
                        placesAutocomplete.getDetailsFor(place, new DetailsCallback() {
                            @Override
                            public void onSuccess(PlaceDetails placeDetails) {
                                Toast.makeText(getActivity(), ""+placeDetails.name, Toast.LENGTH_SHORT).show();
                                placesAutocomplete.setText(placeDetails.name);
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });
                    }
                }
        );
        btn_timkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String address=placesAutocomplete.getText().toString()+","+sp_quan.getSelectedItem().toString()+","+sp_thanhpho.getSelectedItem().toString();
                String disKey=disHashMap.get(sp_quan.getSelectedItem().toString());

                Toast.makeText(getActivity(), ""+address, Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(), FindActivity.class);
                intent.putExtra("Address",address);
                intent.putExtra("DIS",disKey);
                startActivity(intent);
                dismiss();
            }
        });

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        getCity();
    }

    public void anhXa(View view){
        sp_thanhpho=(Spinner)view.findViewById(R.id.find_spin_thanhpho);
        sp_quan=(Spinner)view.findViewById(R.id.find_spin_quan);
//        edt_tenduong=(EditText)view.findViewById(R.id.find_edt_tenduong);
        btn_timkiem=(Button)view.findViewById(R.id.find_btn_timkiem);
        placesAutocomplete=(com.seatgeek.placesautocomplete.PlacesAutocompleteTextView)view.findViewById(R.id.places_autocomplete);
    }

    private void getCity(){
        myData.child("CITY").orderByChild("cityName").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                City city=dataSnapshot.getValue(City.class);
                cities.add(city);
                cityNames.add(city.getCityName());
                ArrayAdapter adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,cityNames);
                sp_thanhpho.setAdapter(adapter);

                sp_thanhpho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            cityID=cities.get(position).getCityID();
                            getDistrict(cityID);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //getchildCity(dataSnapshot);
                City city=dataSnapshot.getValue(City.class);
                cities.add(city);
                cityNames.add(city.getCityName());
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
    }

    private void getDistrict(final String cityid){
        districts.clear();
        disNames.clear();

        myData.child("DISTRICT").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                District district=dataSnapshot.getValue(District.class);
                if(district.getCityID().toString().equals(cityid)){
//                    Toast.makeText(getActivity(), ""+district.getDistrictName().toString(), Toast.LENGTH_SHORT).show();
                    //districts.add(district);
                    disNames.add(district.getDistrictName());
                    disHashMap.put(district.getDistrictName().toString(),district.getDistrictID().toString());
                }
                ArrayAdapter adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,disNames);
                adapter.notifyDataSetChanged();
                sp_quan.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //getchildCity(dataSnapshot);
                District district=dataSnapshot.getValue(District.class);
                districts.add(district);
                disNames.add(district.getDistrictName());
                ArrayAdapter adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,disNames);
                sp_quan.setAdapter(adapter);
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
    }

    private void getchildCity(DataSnapshot ds){
        cities.clear();

        for (DataSnapshot data : ds.getChildren()){
            City city=new City();

            city.setCityID(data.getValue(City.class).getCityID());
            city.setCityName(data.getValue(City.class).getCityName());

            cities.add(city);
        }
        if (cities.size()>0){
            ArrayAdapter adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_dropdown_item,cities);
            sp_thanhpho.setAdapter(adapter);
        }
    }
}

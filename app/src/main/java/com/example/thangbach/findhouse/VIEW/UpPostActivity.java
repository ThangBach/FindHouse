package com.example.thangbach.findhouse.VIEW;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.thangbach.findhouse.DAO.City;
import com.example.thangbach.findhouse.DAO.District;
import com.example.thangbach.findhouse.DAO.Post;
import com.example.thangbach.findhouse.R;
import com.example.thangbach.findhouse.SERVICE.UserIMP;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.example.thangbach.findhouse.SERVICE.UserIMP.myData;

/**
 * Created by ThangBach on 10/19/2016.
 */

public class UpPostActivity extends AppCompatActivity {

    ImageView   img_1,
                img_2,
                img_3,
                img_4;

    EditText    edt_diachi,
                edt_dientich,
                edt_phone,
                edt_gia,
                edt_mota,
                edt_sonha,
                edt_chude;

    Spinner     sp_quan,
                sp_thanhpho;

    Button      btn_post,
                btn_anh;

    private LinearLayout lnrImages;
    private ArrayList<String> imagesPathList;
    private ArrayList<Bitmap> bitmaps=new ArrayList<Bitmap>();
    private Bitmap yourbitmap;

    ArrayList<City> cities=new ArrayList<City>();
    ArrayList<String> cityNames=new ArrayList<String>();

    ArrayList<District> districts=new ArrayList<District>();
    ArrayList<String> disNames=new ArrayList<>();

    HashMap<String,String> cityHashMap=new HashMap<String, String>();
    HashMap<String,String> disHashMap=new HashMap<String, String>();
    String cityID;

    private final int PICK_IMAGE_MULTIPLE =1;

    StorageReference storageReference;

    ArrayList<Uri> uris=new ArrayList<Uri>();
    com.seatgeek.placesautocomplete.PlacesAutocompleteTextView placesAutocomplete;

    String[] pathImages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_post);

        final UserIMP userIMP=new UserIMP(this);

        storageReference = FirebaseStorage.getInstance().getReference();
        userIMP.myData= FirebaseDatabase.getInstance().getReference();
        getCity();
        anhXa();


        placesAutocomplete.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(final Place place) {
                        // do something awesome with the selected place
                        placesAutocomplete.setText("");
                        placesAutocomplete.getDetailsFor(place, new DetailsCallback() {
                            @Override
                            public void onSuccess(PlaceDetails placeDetails) {
                                placesAutocomplete.setText(placeDetails.name);
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });
                    }
                }
        );
        btn_anh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpPostActivity.this,CustomPhotoGalleryActivity.class);
                startActivityForResult(intent,PICK_IMAGE_MULTIPLE);
            }
        });
        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validate().equals("")){
                    Toast.makeText(UpPostActivity.this,validate(), Toast.LENGTH_SHORT).show();
                }else {
                    if (pathImages != null) {
                        if (pathImages.length < 4) {
                            Toast.makeText(UpPostActivity.this, "Chọn tối thiểu 4 hình ảnh cho bài viết của bạn !", Toast.LENGTH_SHORT).show();
                        } else {
                            upLoadImage(pathImages);
                        }
                    }
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        img_1.setImageResource(R.drawable.image_not);
        img_2.setImageResource(R.drawable.image_not);
        img_3.setImageResource(R.drawable.image_not);
        img_4.setImageResource(R.drawable.image_not);
        for (int i=0;i<bitmaps.size();i++){

            switch (i){
                case 0:{
                    img_1.setImageBitmap(bitmaps.get(i));
                }break;
                case 1:{
                    img_2.setImageBitmap(bitmaps.get(i));
                }break;
                case 2:{
                    img_3.setImageBitmap(bitmaps.get(i));
                }break;
                case 3:{
                    img_4.setImageBitmap(bitmaps.get(i));
                }break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmaps=new ArrayList<Bitmap>();
        if (resultCode == RESULT_OK) {
            if(requestCode == PICK_IMAGE_MULTIPLE){
                imagesPathList=new ArrayList<>();
                String[] imagesPath = data.getStringExtra("data").split("\\|");
                pathImages=imagesPath;
                //upLoadImage(imagesPath[0]);
                try{
                    lnrImages.removeAllViews();
                }catch (Throwable e){
                    e.printStackTrace();
                }
                for (int i=0;i<imagesPath.length;i++){
                    imagesPathList.add(imagesPath[i]);
                    yourbitmap = BitmapFactory.decodeFile(imagesPath[i]);
                    bitmaps.add(yourbitmap);
                }
            }
        }
    }

    public void anhXa(){
        img_1=(ImageView)findViewById(R.id.up_anh_1);
        img_2=(ImageView)findViewById(R.id.up_anh_2);
        img_3=(ImageView)findViewById(R.id.up_anh_3);
        img_4=(ImageView)findViewById(R.id.up_anh_4);

        edt_dientich=(EditText)findViewById(R.id.up_edt_dientich);
        edt_phone=(EditText)findViewById(R.id.up_edt_phone);
        edt_mota=(EditText)findViewById(R.id.up_edt_mota);
        edt_sonha=(EditText)findViewById(R.id.up_edt_sonha);
        edt_chude = (EditText)findViewById(R.id.up_edt_chude);
        edt_gia=(EditText)findViewById(R.id.up_edt_gia);

        sp_quan=(Spinner)findViewById(R.id.up_spin_quan);
        sp_thanhpho=(Spinner)findViewById(R.id.up_spin_thanhpho);

        btn_post=(Button)findViewById(R.id.up_btn_post);
        btn_anh=(Button)findViewById(R.id.up_btn_anh);

        placesAutocomplete=(com.seatgeek.placesautocomplete.PlacesAutocompleteTextView)findViewById(R.id.places_autocomplete);

    }


    private void getCity(){
        myData.child("CITY").orderByChild("cityName").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                City city=dataSnapshot.getValue(City.class);
                cities.add(city);
                cityNames.add(city.getCityName());
                ArrayAdapter adapter=new ArrayAdapter(UpPostActivity.this,android.R.layout.simple_spinner_dropdown_item,cityNames);
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
                ArrayAdapter adapter=new ArrayAdapter(UpPostActivity.this,android.R.layout.simple_spinner_dropdown_item,disNames);
                adapter.notifyDataSetChanged();
                sp_quan.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //getchildCity(dataSnapshot);
                District district=dataSnapshot.getValue(District.class);
                districts.add(district);
                disNames.add(district.getDistrictName());
                ArrayAdapter adapter=new ArrayAdapter(UpPostActivity.this,android.R.layout.simple_spinner_dropdown_item,disNames);
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

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void upLoadImage(String filePath){


        Uri file = Uri.fromFile(new File(filePath));
        StorageReference riversRef = storageReference.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    public void upLoadImage(String[] filePath){

        final List<Uri> urlImages=new ArrayList<>();

        for (int i=0;i<filePath.length;i++){
            Uri file = Uri.fromFile(new File(filePath[i]));
            StorageReference riversRef = storageReference.child("Images/"+file.getLastPathSegment());
            UploadTask uploadTask = riversRef.putFile(file);
            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    urlImages.add(downloadUrl);
                }
            });
        }
        SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String datetime=dateformat.format(date);
        Post post=new Post();
        post.setPostImage1(urlImages.get(0).toString());
        post.setPostImage2(urlImages.get(1).toString());
        post.setPostImage3(urlImages.get(2).toString());
        post.setPostImage4(urlImages.get(3).toString());
        post.setCityID(cityID);
        post.setDistrictID(disHashMap.get(sp_quan.getSelectedItem().toString()));
        post.setPostDescFast(edt_chude.getText().toString().trim());
        post.setPostDescDetail(edt_mota.getText().toString().trim());
        String address=edt_sonha.getText().toString().trim()+" "+placesAutocomplete.getText().toString().trim()+",, "+sp_quan.getSelectedItem().toString().trim()+", "+sp_thanhpho.getSelectedItem().toString().trim();
        post.setPostAddress(address);
        post.setPostDate(datetime);
        post.setPostPhone(edt_phone.getText().toString().trim());
        post.setPostPrice(edt_gia.getText().toString().trim());
        post.setPostAcreage(edt_dientich.getText().toString().trim());
        post.setPostID("");
        post.setUserID("");
        addPost(post);
        Toast.makeText(this, "Đăng bài viết thành công !", Toast.LENGTH_SHORT).show();
    }



    public void addPost(Post post){
        UserIMP.myData.child("POST").push().setValue(post);
    }


    private String validate(){
        String soNha=edt_sonha.getText().toString().trim();
        String tenDuong=placesAutocomplete.getText().toString().trim();
        String dienTich=edt_dientich.getText().toString().trim();
        String dienThoai=edt_phone.getText().toString().trim();
        String chuDe=edt_chude.getText().toString().trim();
        String moTa=edt_mota.getText().toString().trim();
        String gia=edt_gia.getText().toString().trim();

        String error="";

        if (soNha==null ||soNha.equals("")){
            error+="Sô nhà: còn trống !\n";
        }
        if (tenDuong==null || tenDuong.equals("")){
            error+="Tên đường: còn trống !\n";
        }
        if (dienTich==null || dienTich.equals("")){
            error+="Diện tích: còn trống !\n";
        }
        if(dienThoai==null || dienThoai.equals("")){
            error+="Điện thoại: còn trống !\n";
        }
        if(chuDe==null || chuDe.equals("")){
            error+="Chủ đề: còn trống !\n";
        }
        if(gia==null || gia.equals("")){
            error+="Giá: còn trống !\n";
        }
        return  error;
    }
}

package com.example.thangbach.findhouse.VIEW;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.thangbach.findhouse.DAO.City;
import com.example.thangbach.findhouse.DAO.District;
import com.example.thangbach.findhouse.DAO.Post;
import com.example.thangbach.findhouse.DAO.User;
import com.example.thangbach.findhouse.R;
import com.example.thangbach.findhouse.SERVICE.UserIMP;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;



public class SignUpActivity extends AppCompatActivity {

    EditText    edt_fullname,
                edt_username,
                edt_pass1,
                edt_pass2;
    Button      btn_login;

    ImageView   dk_img;
    private final int PICK_IMAGE_MULTIPLE =1;
    private LinearLayout lnrImages;
    private ArrayList<String> imagesPathList;
    private ArrayList<Bitmap> bitmaps=new ArrayList<Bitmap>();
    private Bitmap yourbitmap;
    private String imagepath;

    StorageReference storageReference;
    private FirebaseAuth mAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    List<User> users=new ArrayList<User>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        anhXa();
        final UserIMP userIMP=new UserIMP(this);
        mAuth = FirebaseAuth.getInstance();
        userIMP.myData= FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();


        dk_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this,CustomPhotoGalleryActivity.class);
                startActivityForResult(intent,PICK_IMAGE_MULTIPLE);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String error="";
            if(validateUserName()!=true){
                error+="Tên tài khoản còn trống hoặc bị trùng! \n";
            }
            if(validatePass()!=true){
                error+="Mật khẩu còn trống hoặc không giống nhau! \n";
            }
            if(validateMail()!=true){
                error+="Email tài khoản không đúng địng dạng! \n";
            }
            if(validateFullName()!=true){
                error+="Họ tên còn trống ! \n";
            }
            if(error!=""){
                Toast.makeText(SignUpActivity.this, error, Toast.LENGTH_SHORT).show();
            }else{
                createAccount(edt_username.getText().toString(),edt_pass1.getText().toString());
//                upLoadImage(imagepath);
            }
//            else
//            {
////                User user=new User();
////                user.setId("");
////                user.setFullName(edt_fullname.getText().toString().trim());
////                user.setUserName(edt_username.getText().toString());
////                user.setPassWord(edt_pass1.getText().toString().trim());
////                user.setEmail(edt_email.getText().toString().trim());
////                userIMP.addUser(user);
//
//
//            }
//                User user=userIMP.checkUser("thangbach");
//                Toast.makeText(SignUpActivity.this, user.getFullName(), Toast.LENGTH_SHORT).show();

                //addCity(edt_fullname.getText().toString(),edt_username.getText().toString());
                //Toast.makeText(SignUpActivity.this, "hello", Toast.LENGTH_SHORT).show();)
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        bitmaps=new ArrayList<Bitmap>();
        if (resultCode == RESULT_OK) {
            if(requestCode == PICK_IMAGE_MULTIPLE){
                imagesPathList = new ArrayList<String>();
                String[] imagesPath = data.getStringExtra("data").split("\\|");
                imagepath=imagesPath[0];
                try{
                    lnrImages.removeAllViews();
                }catch (Throwable e){
                    e.printStackTrace();
                }
                for (int i=0;i<imagesPath.length;i++){
                    imagesPathList.add(imagesPath[i]);
                    yourbitmap = BitmapFactory.decodeFile(imagesPath[i]);
                    bitmaps.add(yourbitmap);
                    dk_img.setImageBitmap(yourbitmap);

                }
            }
        }
    }
    private void anhXa(){
        edt_fullname=(EditText)findViewById(R.id.dk_edt_fullname);
        edt_username=(EditText)findViewById(R.id.dk_edt_username);
        edt_pass1=(EditText)findViewById(R.id.dk_edt_pass1);
        edt_pass2=(EditText)findViewById(R.id.dk_edt_pass2);

        btn_login=(Button)findViewById(R.id.dk_btn_login);

        dk_img=(ImageView)findViewById(R.id.dk_img);
    }

    private boolean validateUserName()
    {
        boolean value=true;
        String userName=edt_username.getText().toString().trim();

        if(userName==null ||userName.equals("")){
            value=false;
        }

        return  value;
    }

    private boolean validateFullName(){
        boolean value =true;
        String fullname=edt_fullname.getText().toString().trim();

        if(fullname==null || fullname.equals("")){
            value=false;
        }

        return value;
    }

    private boolean validatePass(){
        boolean value=true;

        String pass1=edt_pass1.getText().toString().trim();
        String pass2=edt_pass2.getText().toString().trim();

        if(pass1.equals("") ||pass1==null){
            value=false;
        }
        if(pass2.equals("") ||pass2==null){
            value=false;
        }
        if(pass1.equals(pass2)==false){
            value=false;
        }
        return  value;
    }

    private boolean validateMail(){
        boolean value=false;
        String email=edt_username.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(email.matches(emailPattern)){
            value=true;
        }
        return value;
    }

    private  void createAccount(String email,String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Chúc mừng bạn đã đăng ký thành công !", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void addUser(User user){
        UserIMP.myData.child("USER").push().setValue(user);
    }
    public void upLoadImage(String filePath){

        Uri file = Uri.fromFile(new File(filePath));
        StorageReference riversRef = storageReference.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = riversRef.putFile(file);

// Register observers to listen for when the download is done or if it fails
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

                User user=new User(
                            "",
                            edt_fullname.getText().toString(),
                            edt_username.getText().toString(),
                            edt_pass1.getText().toString(),
                        taskSnapshot.getDownloadUrl().toString(),
                        edt_username.getText().toString()
                );
                addUser(user);
                createAccount(edt_username.getText().toString(),edt_pass1.getText().toString());
                Toast.makeText(SignUpActivity.this, ""+downloadUrl.toString(), Toast.LENGTH_SHORT).show();

            }
        });    }

    public void addCity(String cityID, String cityName){

        City city=new City();
        city.setCityID(cityID);
        city.setCityName(cityName);

        UserIMP.myData.child("CITY").push().setValue(city);
    }

    public void addDistrict(String districtID, String districtName, String cityID){
        District district=new District();
        district.setDistrictID(districtID);
        district.setDistrictName(districtName);
        district.setCityID(cityID);

        UserIMP.myData.child("DISTRICT").push().setValue(district);
    }
}

package com.example.thangbach.findhouse.VIEW;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.thangbach.findhouse.Adapter.RecyclerAdapter;
import com.example.thangbach.findhouse.DAO.Post;
import com.example.thangbach.findhouse.DAO.Street;
import com.example.thangbach.findhouse.R;
import com.example.thangbach.findhouse.SERVICE.UserIMP;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.util.HashMap;

/**
 * Created by ThangBach on 10/22/2016.
 */

public class DetailPostActivity extends AppCompatActivity implements View.OnClickListener,BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    ImageButton btn_back,
                btn_danduong,
                btn_call;

    TextView    txt_title_parent,
                txt_title,
                txt_gia,
                txt_diachi,
                txt_dientich,
                txt_lienhe,
                txt_mota;

    Toolbar toolbar;

    private SliderLayout sliderImage;

    Post post=new Post();
    String key;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_post);

        anhXa();
        UserIMP.myData= FirebaseDatabase.getInstance().getReference();

        setSupportActionBar(toolbar);


        btn_back.setOnClickListener(this);
        btn_danduong.setOnClickListener(this);
        btn_call.setOnClickListener(this);
        txt_title_parent.setOnClickListener(this);
        sliderImage.addOnPageChangeListener(this);

        getPost();
    }

    private void anhXa(){
        toolbar=(Toolbar)findViewById(R.id.toolbar_post_detail);

        btn_back=(ImageButton)findViewById(R.id.detail_btn_back);
        btn_danduong=(ImageButton)findViewById(R.id.detail_btn_danduong);
        btn_call=(ImageButton)findViewById(R.id.detail_btn_call);

        txt_title_parent=(TextView)findViewById(R.id.detail_txt_title_parent);
        txt_title=(TextView)findViewById(R.id.detail_txt_title);
        txt_gia=(TextView)findViewById(R.id.detail_txt_price);
        txt_diachi=(TextView)findViewById(R.id.detail_txt_diachi);
        txt_dientich=(TextView)findViewById(R.id.detail_txt_dientich);
        txt_lienhe=(TextView)findViewById(R.id.detail_txt_lienhe);
        txt_mota=(TextView)findViewById(R.id.detail_txt_mota);

        sliderImage = (SliderLayout)findViewById(R.id.slider);

    }

    private Post getPost(){

        Intent intent=getIntent();
         key=intent.getStringExtra("KEYPOST");

        UserIMP.myData.child("POST").orderByChild("postDate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
              if (dataSnapshot.getKey().equals(key)){
                  post=new Post();
                  post=dataSnapshot.getValue(Post.class);
                  setSliderImage(post);
                  setDetail(post);
                  //Toast.makeText(DetailPostActivity.this, ""+post.getPostAddress().toString(), Toast.LENGTH_SHORT).show();
              }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        return null;
    }

    private void setDetail(Post post){
        DecimalFormat precision = new DecimalFormat("#,#00");

        txt_lienhe.setText(post.getPostPhone().toString());
        txt_diachi.setText(post.getPostAddress().toString());
        txt_dientich.setText(post.getPostAcreage().toString()+"m2");
        Float gia= Float.valueOf(Integer.valueOf(post.getPostPrice().toString()));
        String[] mota=post.getPostDescDetail().split(";");
        String noidung="-"+post.getPostDescFast()+"\n\n";
        for (int i=0;i<mota.length;i++){
            noidung+="-"+mota[i]+"\n\n";
        }

        txt_mota.setText(noidung);
        txt_gia.setText(precision.format(gia));
    }

    private void setSliderImage(Post post){
        HashMap<String,String > file_maps = new HashMap<String, String>();
        file_maps.put("Hình 1",post.getPostImage1());
        file_maps.put("Hình 2",post.getPostImage2());
        file_maps.put("Hình 3",post.getPostImage3());
        file_maps.put("Hình 4", post.getPostImage4());

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            sliderImage.addSlider(textSliderView);
        }
        sliderImage.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderImage.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderImage.setCustomAnimation(new DescriptionAnimation());
        sliderImage.setDuration(4000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.detail_txt_title_parent:{
                super.onBackPressed();
            }break;
            case  R.id.detail_btn_danduong:{
                Intent intent=new Intent(DetailPostActivity.this,ConductorActivity.class);
                intent.putExtra("Address",post.getPostAddress());
                intent.putExtra("postKey",key);
                startActivity(intent);
            }break;
            case R.id.detail_btn_call:{
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" +post.getPostPhone()));
                startActivity(intent);
            }
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}

package com.example.thangbach.findhouse.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thangbach.findhouse.DAO.Post;
import com.example.thangbach.findhouse.Dialog.LoginDialog;
import com.example.thangbach.findhouse.R;
import com.example.thangbach.findhouse.SERVICE.UserIMP;
import com.example.thangbach.findhouse.VIEW.DetailPostActivity;
import com.example.thangbach.findhouse.VIEW.FragmentHome;
import com.example.thangbach.findhouse.VIEW.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by ThangBach on 10/20/2016.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecylerViewHolder> {

    public static FirebaseAuth mAuth;
    static FirebaseUser user;
    DecimalFormat precision = new DecimalFormat("#,#00");

    private ArrayList<Post> posts=new ArrayList<Post>();
    private Context context;

    public RecyclerAdapter(ArrayList<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @Override
    public RecylerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_list,parent,false);
        UserIMP.myData= FirebaseDatabase.getInstance().getReference();

        RecylerViewHolder recylerViewHolder=new RecylerViewHolder(view,context,posts);

        return recylerViewHolder;
    }

    @Override
    public void onBindViewHolder(RecylerViewHolder holder, int position) {

        DecimalFormat precision = new DecimalFormat("#,#00");

        Post post=posts.get(position);
        String[] address=post.getPostAddress().split(",");

        Picasso.with(context)
                .load(post.getPostImage1().toString())
                .resize(350,200)
                .into(holder.img_post);
        holder.txt_ngaydang.setText(post.getPostDate().toString());
        Float gia= Float.valueOf(Integer.valueOf(post.getPostPrice().toString()));
        holder.txt_gia.setText(precision.format(gia));
        holder.txt_quan.setText(address[2]);
        holder.txt_thanhpho.setText(address[3]);
        holder.txt_diachi.setText(address[0]+", "+address[1]);
        holder.txt_lienhe.setText(post.getPostPhone().toString());

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class RecylerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView    txt_ngaydang,
                    txt_gia,
                    txt_diachi,
                    txt_quan,
                    txt_thanhpho,
                    txt_dientich,
                    txt_lienhe;

        ImageView   img_post;

        ImageButton img_btn_like;

        ArrayList<Post> posts=new ArrayList<Post>();
        Context context;

        public RecylerViewHolder(View itemView, Context context, final ArrayList<Post> posts) {
            super(itemView);
            this.posts=posts;
            this.context=context;

            itemView.setOnClickListener(this);

            txt_diachi=(TextView)itemView.findViewById(R.id.post_item_txt_diachi);
            txt_dientich=(TextView)itemView.findViewById(R.id.item_post_txt_dientich);
            txt_lienhe=(TextView)itemView.findViewById(R.id.item_post_txt_lienhe);
            txt_quan=(TextView)itemView.findViewById(R.id.item_post_txt_quan);
            txt_thanhpho=(TextView)itemView.findViewById(R.id.item_post_txt_thanhpho);
            txt_gia=(TextView)itemView.findViewById(R.id.post_item_txt_gia);
            txt_ngaydang=(TextView)itemView.findViewById(R.id.post_item_txt_ngaydang);

            img_post=(ImageView)itemView.findViewById(R.id.post_item_image);
            img_btn_like=(ImageButton)itemView.findViewById(R.id.post_item_image_like);
            img_btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth = FirebaseAuth.getInstance();
                    user = mAuth.getCurrentUser();
                    if (user!=null){

                    }

                }
            });


        }

        @Override
        public void onClick(View v) {

            int position=getAdapterPosition();

            Post post=this.posts.get(position);

            Intent intent=new Intent(this.context, DetailPostActivity.class);
            intent.putExtra("KEYPOST",post.getPostID());
            context.startActivity(intent);

        }
    }
}

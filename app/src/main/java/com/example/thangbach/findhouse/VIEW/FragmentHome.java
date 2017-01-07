package com.example.thangbach.findhouse.VIEW;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.thangbach.findhouse.Adapter.RecyclerAdapter;
import com.example.thangbach.findhouse.DAO.Post;
import com.example.thangbach.findhouse.Dialog.FindDialog;
import com.example.thangbach.findhouse.Dialog.LoginDialog;
import com.example.thangbach.findhouse.R;
import com.example.thangbach.findhouse.SERVICE.UserIMP;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends android.app.Fragment {

    RecyclerAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager  layoutManager;
    Button btn_dangtin;

    public static FirebaseAuth mAuth;
    FirebaseUser user;

    public FragmentHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_home, container, false);
        // Inflate the layout for this fragment
        UserIMP.myData= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        btn_dangtin=(Button)view.findViewById(R.id.home_btn_dangtin);
        recyclerView=(RecyclerView)view.findViewById(R.id.list_Post);
        layoutManager=new LinearLayoutManager(getActivity());


        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SpacesItemDecoration(10));

        btn_dangtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user==null){
                    showFindDialog();
                }else{
                    Intent intent=new Intent(getActivity(),UpPostActivity.class);
                    startActivity(intent);
                }
            }
        });
        
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getPost();
    }

    public void showFindDialog(){
            LoginDialog loginDialog=new LoginDialog();
            loginDialog.show(getFragmentManager(),"");
    }

    private ArrayList<Post> getPost(){
        final ArrayList<Post> posts=new ArrayList<Post>();

        UserIMP.myData.child("POST").orderByChild("postDate").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post post=dataSnapshot.getValue(Post.class);
                post.setPostID(dataSnapshot.getKey());
                posts.add(post);
                adapter=new RecyclerAdapter(posts,getActivity());
                recyclerView.setAdapter(adapter);
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
}

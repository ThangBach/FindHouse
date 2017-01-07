package com.example.thangbach.findhouse.SERVICE;

import android.content.Context;
import android.widget.Toast;

import com.example.thangbach.findhouse.DAO.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by THANG_BACH on 09/22/16.
 */

public class UserIMP implements UserIF {

    public  static DatabaseReference myData;
    Context mContext;

    List<User> users=new ArrayList<User>();

    public UserIMP(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void addUser(User user) {

        myData= FirebaseDatabase.getInstance().getReference();
        myData.child("USER").push().setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(mContext, "Add User Complete !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void deleteUser(String idUser) {
        myData= FirebaseDatabase.getInstance().getReference();
        myData.child(idUser).removeValue();
    }

    @Override
    public User checkUser(final String userName) {
        User user=new User();
        myData= FirebaseDatabase.getInstance().getReference();
        myData.child("USER").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                myData.child("USER").child(dataSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User userCurrent=dataSnapshot.getValue(User.class);
                        if(userCurrent.getUserName().equals(userName)){
                            users.add(new User(dataSnapshot.getKey(),
                                                userCurrent.getFullName(),
                                                userCurrent.getUserName(),
                                                userCurrent.getPassWord(),
                                                userCurrent.getUrlImg(),
                                                userCurrent.getEmail()
                                                ));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
        for (int i=0;i< users.size();i++){
            user=users.get(i);
        }
        return user;
    }


}

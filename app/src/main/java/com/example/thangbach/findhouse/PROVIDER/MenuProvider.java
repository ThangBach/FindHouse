package com.example.thangbach.findhouse.PROVIDER;

import com.example.thangbach.findhouse.DAO.MenuItem;
import com.example.thangbach.findhouse.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by THANG_BACH on 10/05/16.
 */

public class MenuProvider {

    public static HashMap<MenuItem, List<MenuItem>> getInfo(){
        HashMap<MenuItem,List<MenuItem >> menu_detail=new HashMap<MenuItem, List<MenuItem>>();

//        List<MenuItem> canhan_detail_list=new ArrayList<MenuItem>();
//        //canhan_detail_list.add(new MenuItem(R.drawable.bussines_50,"Thông Tin cá Nhân"));
//        canhan_detail_list.add(new MenuItem(R.drawable.baiviet_50,"Quản lý bài viết"));
//
//        List<MenuItem> yeuthich_detail_list=new ArrayList<MenuItem>();
//
//        List<MenuItem> home_detail_list=new ArrayList<MenuItem>();
//
//        List<MenuItem> center_detail_list=new ArrayList<MenuItem>();
//
//        List<MenuItem> logout_detail=new ArrayList<MenuItem>();
//
//        menu_detail.put(new MenuItem(R.drawable.home_50,"Home"),home_detail_list);
//        menu_detail.put(new MenuItem(R.drawable.person_main,"Quản lý cá nhân"),canhan_detail_list);
//        menu_detail.put(new MenuItem(R.drawable.star_50,"Danh sách yêu thích"),yeuthich_detail_list);
//        menu_detail.put(new MenuItem(R.drawable.center_50,"Tìm kiếm quanh đây"),center_detail_list);
//        menu_detail.put(new MenuItem(R.drawable.exit_52,"Đăng xuất"),logout_detail);

        return menu_detail;
    }
}

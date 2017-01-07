package com.example.thangbach.findhouse.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thangbach.findhouse.DAO.MenuItem;
import com.example.thangbach.findhouse.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by THANG_BACH on 10/05/16.
 */

public class MenuAdapter extends BaseExpandableListAdapter{

    private Context mContext;
    private HashMap<MenuItem,List<MenuItem>> menu_category;
    private List<MenuItem> menu_list;

    public MenuAdapter(Context mContext, HashMap<MenuItem, List<MenuItem>> menu_category, List<MenuItem> menu_list) {
        this.mContext = mContext;
        this.menu_category = menu_category;
        this.menu_list = menu_list;
    }

    @Override
    public int getGroupCount() {
        return menu_list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return menu_category.get(menu_list.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return menu_list.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return menu_category.get(menu_list.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        MenuItem group_item= (MenuItem) getGroup(groupPosition);

        //String title_group=(String)getGroup(groupPosition);
        if (convertView==null){
            LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.item_parent,parent,false);
        }
        ImageView image_parent=(ImageView)convertView.findViewById(R.id.menu_img_parent);
        TextView txt_parent=(TextView)convertView.findViewById(R.id.menu_title_parent);
        image_parent.setImageResource(group_item.getImage());
        txt_parent.setText(group_item.getTitle());


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        MenuItem child_item= (MenuItem) getChild(groupPosition,childPosition);
        //String title_child=(String)getChild(groupPosition,childPosition);
        if (convertView==null){
            LayoutInflater inflater= (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.item_child,parent,false);
        }

        ImageView image_child=(ImageView)convertView.findViewById(R.id.menu_img_child);
        TextView txt_child=(TextView)convertView.findViewById(R.id.menu_title_child);

        image_child.setImageResource(child_item.getImage());
        txt_child.setText(child_item.getTitle());


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

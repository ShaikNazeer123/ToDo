package com.abhibus.intern.todo.todo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Shaik Nazeer on 20-06-2017.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    private ArrayList data;
    ListModel tempValues=null;
    DataBase db;
    public CustomAdapter(ArrayList d) {
        data = d;
    }
    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data) {
        this.data = data;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView t_name;
        public TextView t_stime;
        public TextView t_etime;
        public TextView t_sdate;
        public  TextView t_edate;
        public  TextView t_addedOn;
        public TextView t_det;
        public ViewHolder(View v){
            super(v);
            this.t_name=v.findViewById(R.id.text1);
            this.t_stime=v.findViewById(R.id.text3);
            this.t_sdate=v.findViewById(R.id.text2);
            this.t_etime = v.findViewById(R.id.endtime);
            this.t_edate = v.findViewById(R.id.enddate);
            this.t_addedOn = v.findViewById(R.id.addedon);
            this.t_det = v.findViewById(R.id.det);
        }
    }


    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View v =  LayoutInflater.from(MainActivity.getContext()).inflate(R.layout.list_items, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        tempValues = null;
        tempValues = (ListModel) data.get(position);
        holder.t_name.setText(tempValues.getTask_name());
        holder.t_stime.setText(tempValues.getStime());
        holder.t_sdate.setText(tempValues.getSdate());
        holder.t_etime.setText(tempValues.getEtime());
        holder.t_edate.setText(tempValues.getEdate());
        holder.t_addedOn.setText(tempValues.getAddedOn());
        if(tempValues.getFinish().equals("1")){
            holder.t_det.setText("Completed");
        }
        else{
            String t = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
            if(t.charAt(0)=='0'){
                t = t.substring(0, 0) + t.substring(0 + 1);
                if(t.charAt(2)=='0'){
                    t = t.substring(0, 2) + t.substring(2 + 1);;
                }
            }
            else if(t.charAt(3)=='0'){
                t = t.substring(0, 3) + t.substring(3 + 1);;
            }
            if(tempValues.getSdate().equals(t)){
                holder.t_det.setText("Today");
            }
            else{
                holder.t_det.setText(t);
            }
        }

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}

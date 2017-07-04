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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Shaik Nazeer on 20-06-2017.
 */

public class CustomAdapter extends SectionedRecyclerViewAdapter<CustomAdapter.ViewHolder>{

    private ArrayList data;
    ListModel tempValues=null;
    DataBase db;
    int count,flag=1;
    public CustomAdapter(ArrayList d) {
        this.data = d;
        db = new DataBase(MainActivity.getContext());
    }
    public ArrayList getData() {
        return data;
    }

    public void setData(ArrayList data) {
        this.data = data;
        notifyDataSetChanged();
    }
    public static class ViewHolder extends SectionedViewHolder implements View.OnClickListener {

        public TextView t_name;
        public TextView t_stime;
        public TextView t_etime;
        public TextView t_sdate;
        public  TextView t_edate;
        public  TextView t_addedOn;
        public TextView t_det;
        TextView title;
        Toast toast;
        CustomAdapter adapter;


        public ViewHolder(View v,CustomAdapter adapter){
            super(v);
            this.t_name=v.findViewById(R.id.text1);
            this.t_stime=v.findViewById(R.id.text3);
            this.t_sdate=v.findViewById(R.id.text2);
            this.t_etime = v.findViewById(R.id.endtime);
            this.t_edate = v.findViewById(R.id.enddate);
            this.t_addedOn = v.findViewById(R.id.addedon);
            this.t_det = v.findViewById(R.id.det);

            this.title = v.findViewById(R.id.sectionname);
            this.adapter = adapter;
            v.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            if (isFooter()) {
                // ignore footer clicks
                return;
            }

            if (isHeader()) {
                adapter.toggleSectionExpanded(getRelativePosition().section());
            } else {
                if (toast != null) {
                    toast.cancel();
                }
                toast =
                        Toast.makeText(view.getContext(), getRelativePosition().toString(), Toast.LENGTH_SHORT);
                toast.show();
            }
        }

    }


    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //final View v = LayoutInflater.from(MainActivity.getContext()).inflate(R.layout.list_items, parent, false);
        int layout;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                layout = R.layout.sectionheader;
                break;
            case VIEW_TYPE_ITEM:
                layout = R.layout.list_items;
                break;
            default:
                layout = R.layout.list_items;
                break;
        }
        View v = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ViewHolder vh = new ViewHolder(v, this);
        return vh;

    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getSectionCount() {
        int i=0;
        Cursor c = db.getDistinct();
        while(c.moveToNext()){
            i++;
        }
        Log.e("No:of sections",i+"");
        return i;
    }

    @Override
    public int getItemCount(int section) {
        String date;int i=0,j=0,count=0;
        Cursor c = db.getDistinct();c.moveToFirst();
        do{
            i++;
            date =c.getString(0);
            if(i==section)
                break;

        }while (c.moveToNext());

        for(j=0;j<data.size();j++){
            ListModel ll = (ListModel) data.get(j);
            if(ll.getSdate().equals(date)){
                count++;
            }
        }Log.e(date,count+"");
        return count;
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, int section, boolean expanded) {
        Cursor c = db.getDistinct();
        c.moveToPosition(section);
        holder.title.setText(c.getString(0));
        // holder.caret.setImageResource(expanded ? R.drawable.ic_collapse : R.drawable.ic_expand);
    }

    @Override
    public void onBindFooterViewHolder(ViewHolder holder, int section) {

    }

    @Override
    public int getItemViewType(int section, int relativePosition, int absolutePosition) {
        if (section == 1) {
            // VIEW_TYPE_FOOTER is -3, VIEW_TYPE_HEADER is -2, VIEW_TYPE_ITEM is -1.
            // You can return 0 or greater.
            return 0;
        }
        return super.getItemViewType(section, relativePosition, absolutePosition);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ViewHolder holder, int section, int relativePosition, int absolutePosition) {

        tempValues = null;
        absolutePosition = absolutePosition - 1;
            if(absolutePosition>=data.size()){
                absolutePosition=0;
            }
            tempValues = (ListModel) data.get(absolutePosition);
            holder.t_name.setText(tempValues.getTask_name());
            holder.t_stime.setText(tempValues.getStime());
            holder.t_sdate.setText(tempValues.getSdate());
            holder.t_etime.setText(tempValues.getEtime());
            holder.t_edate.setText(tempValues.getEdate());
            holder.t_addedOn.setText(tempValues.getAddedOn());
            if (tempValues.getFinish().equals("1")) {
                holder.t_det.setText("Completed");
            } else {
                String t = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
                if (t.charAt(0) == '0') {
                    t = t.substring(0, 0) + t.substring(0 + 1);
                    if (t.charAt(2) == '0') {
                        t = t.substring(0, 2) + t.substring(2 + 1);
                        ;
                    }
                } else if (t.charAt(3) == '0') {
                    t = t.substring(0, 3) + t.substring(3 + 1);
                    ;
                }
                if (tempValues.getSdate().equals(t)) {
                    holder.t_det.setText("Today");
                } else {
                    holder.t_det.setText(t);
                }
            }

    }


}

package com.abhibus.intern.todo.todo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import static android.R.id.list;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    PopupWindow popupWindow;
    View popupLayout;
    LinearLayout submit,esubmit;
    DataBase db;
    RecyclerView rc;
    public ArrayList<ListModel> customListViewValues =new ArrayList<ListModel>();
    private RecyclerView.LayoutManager mLayoutManager;
    private CustomAdapter mAdapter;
    DrawerLayout mainLayout;
    ListModel dl;
    View bottomSheet,btmscr,dtr;
   EditText taskName,startTime,endTime,startdate,enddate;


    static  Context k;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.actionbar);
        db = new DataBase(this);
        dl=new ListModel();
        getData(0);
        k=this;
        mainLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addTask);
        bottomSheet = findViewById(R.id.bottomsheet);
        btmscr = (View) findViewById(R.id.btm_scr);
        esubmit = (LinearLayout) findViewById(R.id.esubmit);
        dtr = (View) findViewById(R.id.datetime);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        rc = (RecyclerView)findViewById(R.id.recycler_view);
        rc.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        rc.setLayoutManager(mLayoutManager);
        mAdapter = new CustomAdapter(customListViewValues);
        rc.setAdapter(mAdapter);

        taskName = (EditText) findViewById(R.id.task_name);
        startTime = (EditText) findViewById(R.id.start_time);
        endTime = (EditText) findViewById(R.id.end_time);
        startdate = (EditText) findViewById(R.id.start_date);
        enddate = (EditText) findViewById(R.id.end_date);
        submit = (LinearLayout) findViewById(R.id.bottom);
        final TextView sub = (TextView)findViewById(R.id.submit);
        final TextView esub = (TextView)findViewById(R.id.esubmittext);
        final TimePicker tp= (TimePicker) findViewById(R.id.timepicker);
        final DatePicker dp = (DatePicker) findViewById(R.id.datepicker);
        final TimePicker tp2= (TimePicker) findViewById(R.id.timepicker2);
        final DatePicker dp2 = (DatePicker) findViewById(R.id.datepicker2);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mainLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mainLayout.setDrawerListener(toggle);
        toggle.syncState();

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Log.e("Moving ","L");
                return true;
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                  /*  View itemView = viewHolder.itemView;
                    Paint p = new Paint();
                    p.setColor(Color.parseColor("#388E3C"));
                    RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom());
                    c.drawRect(background, p);
                    if (dX > 0) {
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                        c.drawBitmap(bm, 90, 90, p);
                    } else {
                        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.finish);
                        c.drawBitmap(bm, 90, 90, p);

                    }*/


                Bitmap icon;
                Paint p = new Paint();
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.delete);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else {
                        if(customListViewValues.get(viewHolder.getAdapterPosition()).getFinish().equals("1")){
                            p.setColor(Color.parseColor("#388E33"));
                            RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.moveinbox);
                            RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        }
                        else {
                            p.setColor(Color.parseColor("#388E3C"));
                            RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                            c.drawRect(background, p);
                            icon = BitmapFactory.decodeResource(getResources(), R.drawable.finish);
                            RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                            c.drawBitmap(icon, null, icon_dest, p);
                        }
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition(); //get position which is swipe


                if (direction == ItemTouchHelper.RIGHT) {    //if swipe right
                    dl = customListViewValues.get(position);
                    db.delete(customListViewValues.get(position).getTask_name());
                    customListViewValues.remove(position);
                    mAdapter.notifyItemRemoved(position);
                    Snackbar snackbar = Snackbar.make(mainLayout, "Task deleted", Snackbar.LENGTH_LONG).setAction("Undo.", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            customListViewValues.add(dl);

                            db.insertData(dl.getTask_name(),dl.getStime(),dl.getEtime(),dl.getSdate(),dl.getEdate());
                            mAdapter.setData(customListViewValues);
                            Snackbar.make(mainLayout, "Task restored", Snackbar.LENGTH_LONG);
                        }
                    });
                    snackbar.setActionTextColor(Color.RED);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();


                    /* Alert dialog box
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this); //alert for confirm to delete
                    builder.setMessage("Are you sure to delete?");    //set message
                    builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() { //when click on DELETE
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAdapter.notifyItemRemoved(position);
                            db.delete(customListViewValues.get(position).getTask_name());
                            customListViewValues.remove(position);
//                            adapter.notifyItemRemoved(position);    //item removed from recylcerview
//                            sqldatabase.execSQL("delete from " + TABLE_NAME + " where _id='" + (position + 1) + "'"); //query for delete
//                            list.remove(position);  //then remove item

                            return;
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                             mAdapter.setData(customListViewValues);
//                            adapter.notifyItemRemoved(position + 1);    //notifies the RecyclerView Adapter that data in adapter has been removed at a particular position.
//                            adapter.notifyItemRangeChanged(position, adapter.getItemCount());   //notifies the RecyclerView Adapter that positions of element in adapter has been changed from position(removed element index to end of list), please update it.
                            return;
                        }
                    }).show();  //show alert dialog*/
                }
                else{

                    dl = customListViewValues.get(position);
                    if(dl.getFinish().equals("1")){
                        db.update(customListViewValues.get(position).getTask_name(), 0);
                        customListViewValues.get(position).setFinish("0");
                        mAdapter.setData(customListViewValues);
                        Snackbar snackbar = Snackbar.make(mainLayout, "Task scheduled.", Snackbar.LENGTH_LONG).setAction("Undo.", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                db.update(customListViewValues.get(position).getTask_name(), 1);
                                customListViewValues.get(position).setFinish("1");
                                mAdapter.setData(customListViewValues);
                                Snackbar.make(mainLayout, "Task marked as finished.", Snackbar.LENGTH_LONG);
                            }
                        });
                        snackbar.setActionTextColor(Color.GREEN);
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                    else {
                        db.update(customListViewValues.get(position).getTask_name(), 1);
                        customListViewValues.get(position).setFinish("1");
                        mAdapter.setData(customListViewValues);
                        Snackbar snackbar = Snackbar.make(mainLayout, "Task finished!", Snackbar.LENGTH_LONG).setAction("Undo.", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                db.update(customListViewValues.get(position).getTask_name(), 0);
                                customListViewValues.get(position).setFinish("0");
                                mAdapter.setData(customListViewValues);
                                Snackbar.make(mainLayout, "Task scheduled", Snackbar.LENGTH_LONG);
                            }
                        });
                        snackbar.setActionTextColor(Color.GREEN);
                        View sbView = snackbar.getView();
                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        textView.setTextColor(Color.YELLOW);
                        snackbar.show();
                    }
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rc); //set

//        list = (ListView)findViewById(R.id.list);
//        adapter =new CustomAdapter(this,customListViewValues,getResources());
//        list.setAdapter(adapter);

        scheduleAlarm(this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null)
        {
            setupDrawerContent(navigationView);
        }
        ///////////////////
        taskName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dp.setVisibility(View.GONE);
                tp.setVisibility(View.GONE);
                tp2.setVisibility(View.GONE);
                dp2.setVisibility(View.GONE);
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dp.setVisibility(View.GONE);
                tp.setVisibility(View.VISIBLE);
                tp2.setVisibility(View.GONE);
                dp2.setVisibility(View.GONE);
                hideKeyboardFrom(MainActivity.this,view);
                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override///
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    }
                });////
            }
        });
        endTime.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                dp.setVisibility(View.GONE);
                tp.setVisibility(View.GONE);
                dp2.setVisibility(View.GONE);
                tp2.setVisibility(View.VISIBLE);
                hideKeyboardFrom(MainActivity.this,view);
                startTime.setText(tp.getHour()+":"+tp.getMinute());
                behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override///
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    }
                });
            }
        });
        startdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                tp.setVisibility(View.GONE);
                dp.setVisibility(View.VISIBLE);
                tp2.setVisibility(View.GONE);
                dp2.setVisibility(View.GONE);
                hideKeyboardFrom(MainActivity.this,view);
                endTime.setText(tp2.getHour()+":"+tp2.getMinute());
            }
        });
        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tp.setVisibility(View.GONE);
                dp.setVisibility(View.GONE);
                tp2.setVisibility(View.GONE);
                dp2.setVisibility(View.VISIBLE);
                hideKeyboardFrom(MainActivity.this,view);
                startdate.setText(dp.getDayOfMonth()+"-"+(dp.getMonth()+1)+"-"+dp.getYear());
            }
        });
//////////////////////////////
        fab.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                tp.setVisibility(View.GONE);
                dp.setVisibility(View.GONE);
                tp2.setVisibility(View.GONE);
                dp2.setVisibility(View.GONE);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                esubmit.setVisibility(View.GONE);
                submit.setVisibility(View.VISIBLE);
                taskName.setText("");startTime.setText("");endTime.setText("");startdate.setText("");enddate.setText("");

                fab.setVisibility(View.INVISIBLE);


                sub.setOnClickListener(new View.OnClickListener() {//Data save
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onClick(View view) {

                        String task_name,stTime,enTime,stDate,enDate;
                        task_name = taskName.getText().toString();
                        stTime = tp.getHour()+":"+tp.getMinute();
                        enTime = tp2.getHour()+":"+tp2.getMinute();
                        stDate = dp.getDayOfMonth()+"-"+(dp.getMonth()+1)+"-"+dp.getYear();
                        enDate = dp2.getDayOfMonth()+"-"+(dp2.getMonth()+1)+"-"+dp2.getYear();
                        enddate.setText(enDate);
                        if(task_name.isEmpty()){
                            taskName.setError("Enter task name");
                        }
                        else{
                            boolean res = db.insertData(task_name, stTime, enTime, stDate,enDate);
                            if(res){
                                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                fab.setVisibility(View.VISIBLE);
                                Snackbar snackbar = Snackbar.make(mainLayout, "Task Added", Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.GREEN);
                                snackbar.show();
                                getData(0);
                                mAdapter.setData(customListViewValues);
                            }
                            else{
                                Snackbar snackbar = Snackbar.make(mainLayout, "Error. Please try again.", Snackbar.LENGTH_LONG);
                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.GREEN);
                                snackbar.show();
                            }
                        }
                    }
                });


            }
        });

        btmscr.setBackgroundColor(Color.BLACK);
        btmscr.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                fab.setVisibility(View.VISIBLE);
                return true;
            }
        });

        rc.addOnItemTouchListener(
                new RecyclerItemClickListener(this, rc ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        tp.setVisibility(View.GONE);
                        dp.setVisibility(View.GONE);
                        tp2.setVisibility(View.GONE);
                        dp2.setVisibility(View.GONE);
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        submit.setVisibility(View.GONE);
                        esubmit.setVisibility(View.VISIBLE);
                        final ListModel ll = customListViewValues.get(position);
                        taskName.setText(ll.getTask_name());
                        startTime.setText(ll.getStime());
                        startdate.setText(ll.getSdate());
                        endTime.setText(ll.getEtime());
                        enddate.setText(ll.getEdate());
                        Log.e("jg","P");
                        esub.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e("K","P");
                                String task_name,stTime,enTime,stDate,enDate;
                                task_name = taskName.getText().toString();
                                stTime = tp.getHour()+":"+tp.getMinute();
                                enTime = tp2.getHour()+":"+tp2.getMinute();
                                stDate = dp.getDayOfMonth()+"-"+(dp.getMonth()+1)+"-"+dp.getYear();
                                enDate = dp2.getDayOfMonth()+"-"+(dp2.getMonth()+1)+"-"+dp2.getYear();
                                enddate.setText(enDate);
                                if(task_name.isEmpty()){
                                    taskName.setError("Enter task name");
                                }
                                else{

                                   // boolean res = db.insertData(task_name, stTime, enTime, stDate,enDate);
                                    db.update(ll.getTask_name(),task_name,stDate,enDate,stTime,enTime);
                                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                                        fab.setVisibility(View.VISIBLE);
                                        Snackbar snackbar = Snackbar.make(mainLayout, "Task Updated", Snackbar.LENGTH_LONG);
                                        View sbView = snackbar.getView();
                                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                        textView.setTextColor(Color.GREEN);
                                        snackbar.show();
                                        getData(0);
                                        mAdapter.setData(customListViewValues);

                                }
                            }
                        });

                                            }
                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );

    }




    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        Log.e("Keyboard ","close");
    }

    public static Context getContext(){
        return k;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getData(int i){
        customListViewValues.clear();
        if(i==3){
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            for(int k=0;k<7;k++) {
                String t = new SimpleDateFormat("dd-MM-yyyy").format(c.getTime());
                if(t.charAt(0)=='0'){
                    t = t.substring(0, 0) + t.substring(0 + 1);
                    if(t.charAt(2)=='0'){
                        t = t.substring(0, 2) + t.substring(2 + 1);;
                    }
                }
                else if(t.charAt(3)=='0'){
                    t = t.substring(0, 3) + t.substring(3 + 1);;
                }
                Log.e("Date ",t);
                Cursor res = db.getTasks(t);
                if (res.getCount() == 0) {
//                    showMessage("Empty", "Nothing found");
                } else {
                    do {
                        ListModel l = new ListModel();
                        l.setTask_name(res.getString(1));
                        l.setStime(res.getString(2));
                        l.setEtime(res.getString(3));
                        l.setSdate(res.getString(4));
                        l.setEdate(res.getString(5));
                        l.setAddedOn(res.getString(7));
                        l.setFinish(res.getString(8));
                        Log.e("Data ", l.getTask_name() + " " + l.getStime() + " " + l.getSdate());
                        customListViewValues.add(l);
                    } while (res.moveToNext());
                }
                res.close();
                c.add(Calendar.DATE, 1);
            }
        }
        else {
            Cursor res = db.getTasks(i);
            if (res.getCount() == 0) {
                showMessage("Empty", "Nothing found");
            } else {
                 do{
                     ListModel l = new ListModel();
                     l.setTask_name(res.getString(1));
                     l.setStime(res.getString(2));
                     l.setEtime(res.getString(3));
                     l.setSdate(res.getString(4));
                     l.setEdate(res.getString(5));
                     l.setAddedOn(res.getString(7));
                     l.setFinish(res.getString(8));
                     Log.e("Data ", l.getTask_name() + " " + l.getStime() + " " + l.getSdate());
                     customListViewValues.add(l);
                }while (res.moveToNext());
            }

            res.close();
        }

    }


    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }


    public static void scheduleAlarm(Context context){
        Calendar time = Calendar.getInstance();
        Long t = time.getTimeInMillis() + 60*1000;

        Intent intent = new Intent(context,ScheduleNotification.class);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Log.e("F","F");
        manager.set(AlarmManager.RTC_WAKEUP,t, PendingIntent.getBroadcast(context,1,intent, 0));

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.inbox) {
            getData(0);
//            adapter = new CustomAdapter(MainActivity.this, customListViewValues, getResources());
//            list.setAdapter(adapter);
            mAdapter.setData(customListViewValues);
        } else if (id == R.id.today) {
            getData(1);
//            adapter = new CustomAdapter(MainActivity.this, customListViewValues, getResources());
//            list.setAdapter(adapter);
            mAdapter.setData(customListViewValues);
        } else if (id == R.id.tomorrow) {
            getData(2);
//            adapter = new CustomAdapter(MainActivity.this, customListViewValues, getResources());
//            list.setAdapter(adapter);
           mAdapter.setData(customListViewValues);

        } else if (id == R.id.sdays) {
            getData(3);
//            adapter = new CustomAdapter(MainActivity.this, customListViewValues, getResources());
//            list.setAdapter(adapter);

        } else if (id == R.id.account) {

        } else if (id == R.id.notification) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}


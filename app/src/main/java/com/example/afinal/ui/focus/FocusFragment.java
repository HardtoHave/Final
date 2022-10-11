package com.example.afinal.ui.focus;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.afinal.R;
import com.example.afinal.db.DBContract;
import com.example.afinal.db.DBHelper;
import com.example.afinal.db.DBUser;
import com.example.afinal.service.notificationBroadcastReceiver;
import com.example.afinal.utility.DateFormatUtils;

import java.util.Calendar;

public class FocusFragment extends Fragment {
    public static PassDataInterface passDataInterface;
    private final String channelID="notification";

    private TextView m_HintText;
    private int m_coinNum;
    private SQLiteDatabase database;

    private ImageView mBase;
    private ImageView mTree;


    private Button mStartButton;
    private Button mCancelButton;

    private ChooseDialog mChooseDialog;
    private String choose;
    private int progress;

    private TextView mTimeText;

    private CountDownTimer appListener;
    private ActivityManager mActivityManager;

    private int timerProgress;
    private ProcessBar mProcessBar;
    private int growProgress;

    int level;
    private static boolean flagLock;
    private static boolean flagDanger = false;

    private String sche;
    private CountDownTimer timer;

    private int timeLimit = 5;
    public interface PassDataInterface {
        void passData(String data);
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            passDataInterface = (PassDataInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement onSomeEventListener");
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        return inflater.inflate(R.layout.fragment_focus, container,false);
    }
    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //create notification which required min API26
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            //notification channel
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            String channelName = "Time Notification";
            NotificationChannel channel=new NotificationChannel(channelID, channelName, importance);
            //notification manager
            NotificationManager notificationManager=(NotificationManager)requireActivity().getSystemService(NOTIFICATION_SERVICE);
            //bind
            notificationManager.createNotificationChannel(channel);
        }

        //init
        choose="startBurst";
        progress=5;
        timerProgress=progress;
        SharedPreferences preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        m_coinNum = preferences.getInt("coinSum",0);
        DBHelper dbHelper = new DBHelper(getActivity());
        database = dbHelper.getWritableDatabase();
        //passDataInterface.passData(m_coinNum+"");
        m_HintText = view.findViewById(R.id.hintText);

        mProcessBar = view.findViewById(R.id.process_bar);

        mStartButton = view.findViewById(R.id.startButton);
        mCancelButton = view.findViewById(R.id.cancelButton);

        mBase = view.findViewById(R.id.base);
        //mTree = view.findViewById(R.id.tree);
        mTimeText = view.findViewById(R.id.timeText);

        flagLock = mProcessBar.getflagLock();
        if(flagLock){
            mCancelButton.setVisibility(View.VISIBLE);
            mStartButton.setVisibility(View.GONE);
        }
        else{
            mCancelButton.setVisibility(View.GONE);
            mStartButton.setVisibility(View.VISIBLE);
        }
        initListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        m_coinNum = preferences.getInt("coinSum",0);
        passDataInterface.passData(m_coinNum+"");
        DBUser dbUser= (DBUser) DBUser.getInstance(getActivity());
        SQLiteDatabase db = dbUser.getReadableDatabase();
        String selection="isEquip=?";
        String[] projection={"decoration"};
        String[] selectionArgs= new String[]{"true"};
        Cursor cursor=db.query("theme",projection,selection,selectionArgs,null,null,null);
        String dec=null;
        while(cursor.moveToNext()){
            dec=cursor.getString(0);
        }
    }
    private void initListener(){
        mBase.setOnClickListener(view -> {
            mChooseDialog=new ChooseDialog(getActivity());
            mChooseDialog.setMarioOnClickListener(() -> {
                choose="mario";
                mChooseDialog.dismiss();
                update(choose, progress);
            });
            mChooseDialog.setKabiOnClickListener(() -> {
                choose = "kabi";
                mChooseDialog.dismiss();
                update(choose, progress);
            });
            mChooseDialog.setKoalaOnClickListener(() -> {
                choose = "koala";
                mChooseDialog.dismiss();
                update(choose, progress);
            });
            mChooseDialog.setWukongClickListener(() -> {
                choose = "wukong";
                mChooseDialog.dismiss();
                update(choose, progress);
            });
            mChooseDialog.show();
        });
        mProcessBar.setProgressCallback(_progress -> {
            progress = _progress;
            update(choose, progress);
        });
        mStartButton.setOnClickListener(view -> {
            mBase.setClickable(false);
            m_HintText.setText(R.string.progress_hint);
            flagLock=true;
            mProcessBar.setflagLock(true);
            mProcessBar.invalidate();
            mCancelButton.setVisibility(View.VISIBLE);
            mStartButton.setVisibility(View.GONE);
            timerProgress = progress;
            growProgress = progress-timerProgress;
            long now = Calendar.getInstance().getTimeInMillis();
            sche = DateFormatUtils.longToStr(now,true);
            //progress is the second sum
            createLockTimer();
            timer.start();
            createAPPListener();
            appListener.start();
        });
        mCancelButton.setOnClickListener(view -> {
            mBase.setClickable(true);
            m_HintText.setText(R.string.start_hint);
            flagLock = false;
            mProcessBar.setflagLock(false);
            mProcessBar.invalidate();
            if(timer!=null)
                timer.cancel();
            if(appListener != null)
                appListener.cancel();
            mCancelButton.setVisibility(View.GONE);
            mStartButton.setVisibility(View.VISIBLE);
            setTimeText(progress);
            update(choose, progress);

            boolean succeed = saveNoteToDatabase(choose+level,sche,"0", "0");
            if (succeed) {
                Toast.makeText(getActivity(),
                        "Successful ! (｡･ω･｡)ﾉ♡", Toast.LENGTH_SHORT).show();
                requireActivity().setResult(Activity.RESULT_OK);
            } else {
                Toast.makeText(getActivity(),
                        "Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void createLockTimer(){
        timer = new CountDownTimer(1000L *timerProgress,1000) {
            @Override
            public void onTick(long l) {
                Log.d("progress:",timerProgress+"");
                timerProgress --;       //倒计时--
                growProgress ++;        //lock进度++

                if(flagDanger)          //进入第三方应用
                    timeLimit --;       //枯死倒计时
                if(timeLimit == 0){     //枯死
                    flagLock = false;
                    mProcessBar.setflagLock(false);
                    mProcessBar.invalidate();
                    mCancelButton.setVisibility(View.GONE);
                    mStartButton.setVisibility(View.VISIBLE);
                    setTimeText(progress);
                    update(choose, progress);

                    Log.d("debug:","time out");
                    if(timer != null)
                        timer.cancel();
                    if(appListener != null)
                        appListener.cancel();

                    flagDanger = false;
                    mBase.setClickable(true);
                    m_HintText.setText(R.string.start_hint);
                    sendMessage();
                    boolean succeed = saveNoteToDatabase(choose+level,sche, "0","0");
                    if (succeed) {
                        Toast.makeText(getActivity(),
                                "Successful ! (｡･ω･｡)ﾉ♡", Toast.LENGTH_SHORT).show();
                        requireActivity().setResult(Activity.RESULT_OK);
                    } else {
                        Toast.makeText(getActivity(),
                                "Error", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    if(mTimeText!=null)
                        setTimeText(timerProgress);
                    update(choose, growProgress);
                }
            }

            @Override
            //完成种植
            public void onFinish() {
                mBase.setClickable(true);
                m_HintText.setText("Start your plan of the day！");
                flagLock=false;
                mProcessBar.setflagLock(false);
                mProcessBar.invalidate();
                mCancelButton.setVisibility(View.GONE);
                mStartButton.setVisibility(View.VISIBLE);
                setTimeText(progress);
                update(choose, progress);

                SharedPreferences preferences = requireActivity().getSharedPreferences("user",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();

                m_coinNum += growProgress;
                editor.putInt("coinSum",m_coinNum);
                editor.apply();
                passDataInterface.passData(m_coinNum+"");

                String time = progress/60 + "min";
                boolean succeed = saveNoteToDatabase(choose+level,sche, time,"1");
                if (succeed) {
                    Toast.makeText(getActivity(),
                            "Successful ! (｡･ω･｡)ﾉ♡", Toast.LENGTH_SHORT).show();
                    requireActivity().setResult(Activity.RESULT_OK);
                } else {
                    Toast.makeText(getActivity(),
                            "Error", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void createAPPListener(){
        mActivityManager= (ActivityManager) requireActivity().getSystemService(Context.ACTIVITY_SERVICE);
        appListener=new CountDownTimer(1000L *timerProgress,500) {
            @Override
            public void onTick(long l) {
                if(Build.VERSION.SDK_INT > 21){
                    int time_ms = 60*1000;  //60s
                    Context context = requireActivity().getApplicationContext();
                    try {
                        long time = System.currentTimeMillis();
                        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                        UsageEvents events = usageStatsManager.queryEvents(time-time_ms,time);
                        UsageEvents.Event usageEvent = new UsageEvents.Event();
                        UsageEvents.Event lastMoveToFGEvent = null;
                        while(events.hasNextEvent()){
                            events.getNextEvent(usageEvent);
                            if(usageEvent.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND){
                                lastMoveToFGEvent = usageEvent;
                            }
                        }
                        if(lastMoveToFGEvent != null){
                            lastMoveToFGEvent.getPackageName();
                        }
                    } catch(Throwable e){
                        e.printStackTrace();
                    }
                }
                else {
                    mActivityManager.getRunningTasks(1)
                            .get(0).topActivity.getPackageName();
                }
            }


            @Override
            public void onFinish() {

            }
        };
    }
    private void update(String name,int progress){
        int minute = progress/60;

        if(!flagLock){
            minute = progress/60;
            mTimeText.setText(minute+":00");
        }

    }
    private void setTimeText(int progress){
        int minute = progress / 60;
        int second = progress % 60;
        setTimeText(minute, second);
    }
    @SuppressLint("SetTextI18n")
    private void setTimeText(int minute, int second){
        if (second<10)
            mTimeText.setText(minute + ":0" + second);
        else
            mTimeText.setText(minute + ":" + second);
    }
    @SuppressLint({"UnspecifiedImmutableFlag", "NotificationTrampoline"})
    public void sendMessage(){
        Intent intent=new Intent(getActivity(), notificationBroadcastReceiver.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) requireActivity().getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(requireActivity(), channelID)
                .setOngoing(true)
                .setContentTitle("Message")
                .setContentText("keep going, success is on your way")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon))
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .build();
        manager.notify(1,notification);
    }
    private Boolean saveNoteToDatabase(String title, String scheduled, String time, String state){

        if(database==null){
            return false;
        }
        long now = Calendar.getInstance().getTimeInMillis();
        String deadline = DateFormatUtils.longToStr(now,true);
        ContentValues values = new ContentValues();
        values.put(DBContract.ToDoNote.COLUMN_DEADLINE, deadline);
        values.put(DBContract.ToDoNote.COLUMN_STATE,state);
        values.put(DBContract.ToDoNote.COLUMN_SCHEDULED,scheduled);
        values.put(DBContract.ToDoNote.COLUMN_TIME,time);
        values.put(DBContract.ToDoNote.COLUMN_CAPTION,title);
        long rowId = database.insert(DBContract.ToDoNote.TABLE_NAME, null, values);
        return rowId!=-1;
    }
}
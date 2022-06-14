package com.example.mardichim20;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


@RequiresApi(api = Build.VERSION_CODES.O)
public class Mil extends AppCompatActivity implements ExampleDialog.ExampleDialogListener {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
    String CurrentDate = getCurrentDate();
    ArrayList<Model_mil>ShowInfo =null;
    SwipeRefreshLayout swipeRefreshLayout;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mil);
        ActivityCompat.requestPermissions(Mil.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}
                , PackageManager.PERMISSION_GRANTED);
        getSupportActionBar().setTitle(" Milu-In");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#003300")));
        getWindow().setStatusBarColor(Color.parseColor("#003300"));

        Configuration configuration = getResources().getConfiguration();//Set layout to LTR on RTL phone
        configuration.setLayoutDirection(new Locale("en"));//Set layout to LTR on RTL phone
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());//Set layout to LTR on RTL phone
        fontSizeAdjust(configuration);

        ShowInfoOnCreate();
        try {
            ShowList();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        swipeRefreshLayout = findViewById(R.id.Refresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            ShowInfoOnCreate();
            try {
                ShowList();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            swipeRefreshLayout.setRefreshing(false);
        });

    }

    public void fontSizeAdjust(Configuration configuration){
        configuration.fontScale = (float) 1.0;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale* metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration,metrics);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ShowInfoOnCreate(){
        DataBaseHelperMil dataBaseHelperMil1 = new DataBaseHelperMil(this);
        ShowInfo = (ArrayList<Model_mil>) dataBaseHelperMil1.GETINFO();
        Model_mil activeSam = new Model_mil("צווים פעילים",getCurrentDate(),getCurrentDate(),0,"event","1",-1,1);
        Model_mil nextWeekSam = new Model_mil("שבוע הבא",getNextWeek(),getNextWeek(),0,"event","1",-1,2);

        ShowInfo.add(activeSam);
        ShowInfo.add(nextWeekSam);

        SortArray(ShowInfo);
        java.util.List<Model_mil> needToRemove1 = new ArrayList<>();

        for (Model_mil model_mil  :ShowInfo) {
            String edate = model_mil.getEndDate();
            LocalDate Cdate = LocalDate.parse(CurrentDate, formatter);
            LocalDate Edate = LocalDate.parse(edate, formatter);

            if (Edate.isBefore(Cdate)) {
                    needToRemove1.add(model_mil);
            }
        }

        if(ShowInfo.size() == needToRemove1.size()){
            Toast.makeText(this, "No Future Summons", Toast.LENGTH_SHORT).show();
        }else{
            ShowInfo.removeAll(needToRemove1);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem menuItem = menu.findItem(R.id.search);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("שם חייל המיל או מספרו האישי");
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onClose() {
                ShowInfoOnCreate();
                try {
                    ShowList();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onQueryTextChange(String newText) {

                DataBaseHelperMil dataBaseHelperMil = new DataBaseHelperMil(Mil.this);
                ShowInfo = (ArrayList<Model_mil>) dataBaseHelperMil.GETINFO();
                Collections.sort(ShowInfo,CompareAndSort);
                java.util.List<Model_mil> needToRemoveName = new ArrayList<>();
                for (int i = 0; i < ShowInfo.size(); i++) {
                    Model_mil model_mil = ShowInfo.get(i);
                    String Pnum = "" + model_mil.getpNum();

                    if(!model_mil.getName().contains(newText) ){
                        if(Pnum.contains(newText)){
                            System.out.println(newText);
                        }else{
                            needToRemoveName.add(ShowInfo.get(i));
                        }
                    }
                }
                try {
                    if (newText == null){
                        ShowInfoOnCreate();
                        ShowList();
                    }else{
                        ShowList();
                    }
                }catch (ParseException e) {
                    e.printStackTrace();
                }
                ShowInfo.removeAll(needToRemoveName);
                return false;
            }
        });

        return true; //The on create Return
    }

    @SuppressLint("NonConstantResourceId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
           switch (item_id){
            case R.id.AS:
                DataBaseHelperMil dataBaseHelperMil = new DataBaseHelperMil(this);
                ShowInfo = (ArrayList<Model_mil>) dataBaseHelperMil.GETINFO();
                SortArray(ShowInfo);
                java.util.List<Model_mil> needToRemove = new ArrayList<>();
                for (int i = 0; i < ShowInfo.size(); i++) {
                   Model_mil model_mil = ShowInfo.get(i);
                   String bdate = model_mil.getBeginDate();
                   String edate = model_mil.getEndDate();
                   LocalDate Cdate = LocalDate.parse(CurrentDate, formatter);
                   LocalDate Bdate = LocalDate.parse(bdate, formatter);
                   LocalDate Edate = LocalDate.parse(edate, formatter);
                   boolean check = inRange(Bdate, Edate, Cdate);
                   if (!check) {
                       needToRemove.add(ShowInfo.get(i));
                   }

               }
                if(ShowInfo.size() == needToRemove.size()){
                    Toast.makeText(this, "No Active Summons", Toast.LENGTH_SHORT).show();
                }
                ShowInfo.removeAll(needToRemove);
                try {
                    ShowList();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.SHOW_ALL:
                DataBaseHelperMil dataBaseHelperMil3 = new DataBaseHelperMil(this);
                ShowInfo = (ArrayList<Model_mil>) dataBaseHelperMil3.GETINFO();
                SortArray(ShowInfo);
                try {
                    ShowList();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.SOC:
                try {
                    ShowInfoOnCreate();
                    ShowList();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.AddPhoneNum:
                startActivity(new Intent(getApplicationContext(), TableActivity.class));
                ProgressDialog progress = new ProgressDialog(this);    //ProgressDialog
                    progress.setTitle("Loading");
                    progress.setMessage("Please wait...");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.setCancelable(false);
                    progress.show();
                new Thread(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void run() {
                        try {
                            TimeUnit.MILLISECONDS.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        progress.dismiss();
                    }
                }).start();

                break;
            case R.id.GetInfo:
                DialogSMS dialogSMS = new DialogSMS();
                dialogSMS.showDialog(this,ShowInfo);
                break;

        }
        return true;
    }


   @RequiresApi(api = Build.VERSION_CODES.O)
    public void Button_add(View view){
        Button button = findViewById(R.id.button_add);
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.button_click);
        button.startAnimation(myAnim);
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getSupportFragmentManager(),"addDialog");
        ShowInfoOnCreate();
        try {
            ShowList();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void applyTexts(String name, String Bdate, String Edate, String who, String place) {
        DataBaseHelperPhone dataBaseHelperMil = new DataBaseHelperPhone(this);
        ArrayList<Model_Name>GETINFORM = (ArrayList<Model_Name>) dataBaseHelperMil.GETINFO();
        int phoneNum = 0;
        int pNum = 0;
        for (int i= 0; i<GETINFORM.toArray().length; i++){
            Model_Name model_name = GETINFORM.get(i);
            if (model_name.getPhone() != 0){
                String t = model_name.getName().trim();
                boolean nameCheck = t.contains(name.trim());
                if(nameCheck){
                    phoneNum = model_name.getPhone();
                    pNum = model_name.getNumber();
                    break;
                }
            }
        }
        Model_mil model_mil1 = new Model_mil(name,Bdate,Edate,phoneNum,who,place,-1,pNum);
        DataBaseHelperMil dataBaseHelperMil1 = new DataBaseHelperMil(this);
        dataBaseHelperMil1.addOne(model_mil1);
        try {
            ShowInfoOnCreate();
            ShowList();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void ShowList() throws ParseException {

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerAdapter adapter = new recyclerAdapter(ShowInfo,Mil.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

    }


    @SuppressLint("SimpleDateFormat")
    private String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }

    @SuppressLint("SimpleDateFormat")
    private String getNextWeek(){
        int week  = 7 + 5;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        c.add(Calendar.DAY_OF_WEEK, week);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(c.getTime());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean inRange(LocalDate min, LocalDate max, LocalDate check){
        boolean isAfter = check.isAfter(min);
        boolean isBefore = check.isBefore(max);
        boolean isEq = check.isEqual(min);
        boolean isEq1 = check.isEqual(max);
        if(isAfter && isBefore){
            return true;
        }else if(isEq){
            return true;
        }else return isEq1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "try again", Toast.LENGTH_SHORT).show();
        }
    }



    public static Comparator<Model_mil> CompareAndSort =new Comparator<Model_mil>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public int compare(Model_mil o1, Model_mil o2 ) {
            String StringBdate1 = o1.getBeginDate();
            String StringBdate2 = o2.getBeginDate();
            DateTimeFormatter formatterS = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
            LocalDate Bdate1 = LocalDate.parse(StringBdate1, formatterS);
            LocalDate Bdate2 = LocalDate.parse(StringBdate2, formatterS);
            return Bdate1.compareTo(Bdate2);

        }
    };



    public void SortArray(ArrayList<Model_mil> Array){
        Collections.sort(Array,CompareAndSort);
        int IndexOfEvent;
        for (int i =0 ; i < Array.size(); i++){
            Model_mil model_mil = Array.get(i);
            if(model_mil.getWho().equals("event")){
                IndexOfEvent = i;
                swap(Array,IndexOfEvent);
            }
        }

    }

    public void swap(ArrayList<Model_mil> Array , int IndexOfEvent ){
        if(Array.get(IndexOfEvent).getPlace().equals("0")){
            for (int i = 0 ; i< Array.size(); i++){
                if(Array.get(IndexOfEvent).getBeginDate().equals(Array.get(i).getBeginDate())){
                    Collections.swap(Array,IndexOfEvent , i);
                    break;
                }
            }
        }

        if (Array.get(IndexOfEvent).getPlace().equals("1")) {
            for (int i = Array.size()-1; i > 0; i--) {
                if (Array.get(IndexOfEvent).getBeginDate().equals(Array.get(i).getBeginDate())) {
                    Collections.swap(Array, IndexOfEvent, i);
                    break;
                }
            }
        }
    }










}


package com.example.mardichim20;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class recyclerAdapter extends RecyclerView.Adapter {
    private ArrayList<Model_mil> model;
    private ArrayList<Integer> index = new ArrayList<>();
    private ArrayList<Integer> id = new ArrayList<>();

    private boolean isL = false;
    private Activity activity;
    boolean indexForMultiThreading = false;

    public recyclerAdapter(ArrayList<Model_mil> model, Activity activity){
        this.activity = activity;
        this.model = model;

    }

    @Override
    public int getItemViewType(int position) {
        if(model.get(position).getWho().toLowerCase().contains("event")){
            return 1;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;
        if (viewType == 0){
            view = layoutInflater.inflate(R.layout.list_row,parent,false);
            return new ViewHolderR(view);
        }
        view = layoutInflater.inflate(R.layout.header,parent,false);
        return new ViewHolderHeader(view);

    }

    @Override
    @SuppressLint("RecyclerView")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(model.get(position).getWho().toLowerCase().contains("event")){
            if(!model.get(position).getName().equals("")){
                ViewHolderHeader viewHolderHeader = (ViewHolderHeader) holder;
                boolean Start = model.get(position).getPlace().equals("0");
                String text;
                text =model.get(position).getName();
                if(model.get(position).getName().equals("צווים פעילים")){
                    viewHolderHeader.eventName.setTextColor(Color.parseColor("#3D7946"));
                    viewHolderHeader.eventName.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.arrow_green, 0, 0);
                }else{
                    if(Start){
                        viewHolderHeader.eventName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.arrow_down);
                    }else{
                        viewHolderHeader.eventName.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.arrow_up, 0, 0);
                    }
                    viewHolderHeader.eventName.setTextColor(Color.parseColor("#999999"));
                }


                viewHolderHeader.eventName.setText(text);
                viewHolderHeader.eventName.setGravity(Gravity.CENTER);


            }

                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        final Animation myAnim = AnimationUtils.loadAnimation(activity, R.anim.button_click);
                        holder.itemView.startAnimation(myAnim);


                        String message= "האם אתה בטוח שתרצה למחוק את האירוע הבא: \"" + model.get(position).getName() +"\"" ;
                        new AlertDialog.Builder(activity)
                                .setTitle("Delete")
                                .setMessage(message)
                                .setPositiveButton("מחק", new DialogInterface.OnClickListener() {
                                    @SuppressLint("NotifyDataSetChanged")
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int[] delete = {0,0};
                                        delete[0] = model.get(position).getIdMil();
                                        //going forward
                                        if(model.get(position).getPlace().equals("0")){
                                            for (int i = position+1;i<model.size() ;i++ ){
                                                if (model.get(i).getName().equals(model.get(position).getName())){
                                                    delete[1] = model.get(i).getIdMil();
                                                    break;
                                                }
                                            }
                                        }else{
                                            for (int i = 0;i<position-1 ;i++ ){
                                                if (model.get(i).getName().equals(model.get(position).getName())){
                                                    delete[1] = model.get(i).getIdMil();
                                                    break;
                                                }
                                            }
                                        }

                                        System.out.println(delete[0] + "   " + delete[1]);

                                        DataBaseHelperMil dataBaseHelperMil = new DataBaseHelperMil(activity);
                                        dataBaseHelperMil.deleteTitle(String.valueOf(delete[0]));
                                        dataBaseHelperMil.deleteTitle(String.valueOf(delete[1]));


                                        model.clear();
                                        DataBaseHelperMil dataBaseHelperMil1 = new DataBaseHelperMil(activity);
                                        List<Model_mil> needToRemove1 = new ArrayList<>();
                                        model.addAll(dataBaseHelperMil1.GETINFO());
                                        String CurrentDate = getCurrentDate();
                                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy", Locale.ENGLISH);

                                        for (int i = 0; i < model.size(); i++) {
                                            Model_mil model_mil = model.get(i);
                                            String edate = model_mil.getEndDate();
                                            LocalDate Cdate = LocalDate.parse(CurrentDate, formatter);
                                            LocalDate Edate = LocalDate.parse(edate, formatter);

                                            if (Edate.isBefore(Cdate)) {
                                                needToRemove1.add(model.get(i));
                                            }
                                        }


                                        model.removeAll(needToRemove1);
                                        Mil mil = new Mil();
                                        mil.SortArray(model);
                                        notifyDataSetChanged();


                                    }
                                })
                                .setNegativeButton("בטל",null)
                                .show();
                        return false;
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!model.get(position).getName().equals("צווים פעילים")
                                && model.get(position).getpNum() != 1 ){
                            boolean status =false;
                        if(model.get(position).getPlace().equals("0")){
                            status = true;
                        }

                        if(model.get(position).getName().equals("שבוע הבא")){
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy", Locale.ENGLISH);
                            LocalDate BeginDateFormatted = LocalDate.parse(getNextWeek(), formatter);
                            LocalDate EndDateFormatted = LocalDate.parse(model.get(position).getEndDate(), formatter);
                            ArrayList<Model_mil> ParticipantsInfo = new ArrayList<>();

                            for (Model_mil model_mil : model) { // need to change to start from position N > n or N<n
                                LocalDate DatesCheck = LocalDate.parse(model_mil.getBeginDate(), formatter);
                                if (inRange(BeginDateFormatted,EndDateFormatted,DatesCheck)&& !model_mil.getPlace().equals("0") && !model_mil.getPlace().equals("1")) {
                                    ParticipantsInfo.add(model_mil);
                                }
                            }
                            DialogEvent dialogEvent = new DialogEvent();
                            dialogEvent.showDialog(activity,model,position,getNextWeek(),model.get(position).getEndDate(),ParticipantsInfo);
                        }else{
                            startLoader(status,position,model);
                            }
                        }
                    }
                });




        }else{
            //the Bind View Holder Begin
            ViewHolderR viewHolderR = (ViewHolderR) holder;
            Model_mil  model_mil = model.get(position);

            String Name = model_mil.getName();
            int Pnum = model_mil.getpNum();
            String Bdate = model_mil.getBeginDate();
            String Edate = model_mil.getEndDate();
            String CurrentDate = getCurrentDate();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy", Locale.ENGLISH);
            LocalDate Cdate = LocalDate.parse(CurrentDate, formatter);
            LocalDate Bdate1 = LocalDate.parse(Bdate, formatter);
            LocalDate Edate1 = LocalDate.parse(Edate, formatter);


            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.round_corner53));

                //deleting all the already stored data
                id.clear();
                index.clear();
                //adding the new Selected Information
                model_mil.setSelected(!model_mil.isSelected());
                id.add(model_mil.getIdMil());
                index.add(holder.getAdapterPosition());
                isL = true;

                ActionMode.Callback callback = new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        MenuInflater menuInflater = mode.getMenuInflater();
                        menuInflater.inflate(R.menu.menu_delete, menu);
                        MenuItem item = menu.findItem(R.id.delete1);
                        item.setVisible(true);
                        item.setEnabled(false);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        String INFO = "";
                        for(int i = 0; i< index.size() ;i++){
                            INFO = INFO + model.get(index.get(i)).getName()
                                    +"   "
                                    + model.get(index.get(i)).getBeginDate()
                                    +" - "
                                    + model.get(index.get(i)).getEndDate()
                                    +"\n";
                        }



                        String massage = "האם אתה בטוח שאתה רוצה למחוק את הצוים הבאים: " +"\n\n" + INFO;
                        AlertDialog dialog = new AlertDialog.Builder(activity)
                                .setTitle("Delete")
                                .setMessage(massage)
                                .setPositiveButton("מחיקה", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DataBaseHelperMil dataBaseHelperMil = new DataBaseHelperMil(activity);
                                        //check if a header need to be deleted

                                        for(int i = 0; i < id.size() ;i++){
                                              dataBaseHelperMil.deleteTitle(String.valueOf(id.get(i)));
                                        }
                                        isL=false;
                                        model.clear();
                                        DataBaseHelperMil dataBaseHelperMil1 = new DataBaseHelperMil(activity);
                                        List<Model_mil> needToRemove1 = new ArrayList<>();
                                        model.addAll(dataBaseHelperMil1.GETINFO());

                                        for (int i = 0; i < model.size(); i++) {
                                            Model_mil model_mil = model.get(i);
                                            String edate = model_mil.getEndDate();
                                            LocalDate Cdate = LocalDate.parse(CurrentDate, formatter);
                                            LocalDate Edate = LocalDate.parse(edate, formatter);

                                            if (Edate.isBefore(Cdate)) {
                                                needToRemove1.add(model.get(i));
                                            }
                                        }

                                        if(model.size() == needToRemove1.size()){
                                            Toast.makeText(activity, "No Future Summons", Toast.LENGTH_SHORT).show();
                                        }else{
                                            model.removeAll(needToRemove1);
                                        }

                                        Collections.sort(model,CompareAndSort);

                                        notifyDataSetChanged();
                                        mode.finish();

                                    }
                                })
                                .setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        isL=false;
                                        mode.finish();
                                    }
                                })
                                .show();

                        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                        textView.setTextSize(14);

                        Typeface font = Typeface.createFromAsset(activity.getAssets(), "tryF.ttf");
                        textView.setTypeface(font);

                        Spannable spannable = new SpannableString(massage);
                        spannable.setSpan(new RelativeSizeSpan(1.5f),0,44,0);
                        textView.setText(spannable, TextView.BufferType.SPANNABLE);
                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        for(int i=0; i<index.size() ; i++){
                            holder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.round_corner51 ));
                            notifyItemChanged(index.get(i));
                        }
                        isL=false;

                    }
                };
                ((AppCompatActivity) v.getContext()).startActionMode(callback);

                return true;

            }
        });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isL){
                    DialogV dialogV = new DialogV();
                    dialogV.showDialog(activity,"context",model_mil);
                }else{
                    if(model_mil.isSelected()){
                        holder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.round_corner51));
                        id.removeAll(Collections.singleton(model_mil.getIdMil()));
                        index.removeAll(Collections.singleton(position));
                        model_mil.setSelected(false);

                    }else{
                        holder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.round_corner53));
                        id.add(model_mil.getIdMil());
                        index.add(position);
                        model_mil.setSelected(true);
                    }
                    System.out.println(index);

                }

            }
        });


            if(isL && model.get(position).isSelected()){
                holder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.round_corner53));
            }else{
                if(Bdate1.isEqual(Cdate)){
                    holder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.round_corner_today));
                }else{
                    holder.itemView.setBackgroundDrawable(ContextCompat.getDrawable(holder.itemView.getContext(),R.drawable.round_corner51));
                }
            }

            boolean check = inRange(Bdate1, Edate1, Cdate);
            if (check){
                viewHolderR.Circle.setBackgroundResource(R.drawable.round_corner10);
                viewHolderR.name.setTextColor(Color.BLACK);//White
                viewHolderR.bdate.setTextColor(Color.BLACK);
                viewHolderR.edate.setTextColor(Color.BLACK);
                viewHolderR.line.setTextColor(Color.BLACK);
            }else{
                viewHolderR.bdate.setTextColor(Color.parseColor("#999999"));//GREY
                viewHolderR.edate.setTextColor(Color.parseColor("#999999"));//GREY
                viewHolderR.line.setTextColor(Color.parseColor("#999999"));//GREY
                viewHolderR.name.setTextColor(Color.parseColor("#999999"));//GREY

                if(Cdate.isAfter(Edate1)){
                    viewHolderR.Circle.setBackgroundResource(R.drawable.round_corner_red);

                }else{
                    viewHolderR.Circle.setBackgroundResource(R.drawable.round_corner_grey);
                }

            }



            String NameNum = "0";
            if(Pnum == 0){
                NameNum = Name+" | " +"0000000";
            }else{
                NameNum = Name+" | " +Pnum;
            }


            Spannable spannable = new SpannableString(NameNum);
            spannable.setSpan(new RelativeSizeSpan(1f),0,Name.length(),0);
            spannable.setSpan(new RelativeSizeSpan(0.7f),Name.length(),NameNum.length(),0);
            viewHolderR.name.setText(spannable, TextView.BufferType.SPANNABLE);
            viewHolderR.bdate.setText(Bdate);
            viewHolderR.edate.setText(Edate);

        }
    }



    @Override
    public int getItemCount() {
        return model.size();
    }

    class ViewHolderR extends RecyclerView.ViewHolder{
        private final TextView name;
        private final TextView bdate;
        private final TextView edate;
        private final TextView Circle;
        private final TextView line;
        public ViewHolderR(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.Name);
            bdate = itemView.findViewById(R.id.begin_date);
            edate = itemView.findViewById(R.id.end_date);
            Circle = itemView.findViewById(R.id.textView);
            line = itemView.findViewById(R.id.line);
        }
    }
    class ViewHolderHeader extends RecyclerView.ViewHolder{
        TextView eventName;

        public ViewHolderHeader(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.event);

        }
    }




    //Date related TODO not related to the recyclerAdapter
    @SuppressLint("SimpleDateFormat")
    private String getCurrentDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
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
        }else if(isEq1){
            return true;
        }else{
            return false;
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
    public static Comparator<Model_mil> comparatorByNames = new Comparator<Model_mil>() {
        @Override
        public int compare(Model_mil o1, Model_mil o2) {
            String StringName1 = o1.getName();
            String StringName2 = o2.getName();
            return StringName1.compareTo(StringName2);
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startLoader(boolean dateStatus , int position, ArrayList<Model_mil> ListModel) {
        String dateStatusS = dateStatus ? "1" : "0";
        DataBaseHelperMil dataBaseHelperMil = new DataBaseHelperMil(activity);
        ArrayList<Model_mil>ShowInfo = (ArrayList<Model_mil>) dataBaseHelperMil.GETINFO();
        Collections.sort(ShowInfo,comparatorByNames);
        ArrayList<Model_mil> ParticipantsInfo = new ArrayList<>();

        int  t = binarySearch(ShowInfo, ListModel.get(position).getName(), 0,ShowInfo.size()-1, ListModel.get(position).getPlace());
        Log.d("binary", "binarySearch: " + t );
        int Index = -1;
        if(t < 0 || t > ShowInfo.size()){
            return;
        }
        if(ShowInfo.get(t).getPlace().equals(dateStatusS)) {
           Index = t;
        }


        if(!ShowInfo.get(t).getPlace().equals(dateStatusS)){
            if(ShowInfo.get(t+1).getPlace().equals(dateStatusS)){
                Index = t+1;
            }
            if(ShowInfo.get(t-1).getPlace().equals(dateStatusS)){
                Index = t-1;
            }
        }
        final String[] Dates = {ShowInfo.get(Index).getBeginDate() , ListModel.get(position).getEndDate()};
        if(dateStatus){
            String temp;
            temp = Dates[0] ;
            Dates[0] = Dates[1];
            Dates[1] = temp;
        }
        ProgressDialog progress = new ProgressDialog(activity);    //ProgressDialog
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.show();
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy", Locale.ENGLISH);
                LocalDate BeginDateFormatted = LocalDate.parse(Dates[0], formatter);
                LocalDate EndDateFormatted = LocalDate.parse(Dates[1], formatter);
                for (Model_mil model_mil : ListModel) { // need to change to start from position N > n or N<n
                    LocalDate DatesCheck = LocalDate.parse(model_mil.getBeginDate(), formatter);
                    if (inRange(BeginDateFormatted,EndDateFormatted,DatesCheck)&& !model_mil.getPlace().equals("0") && !model_mil.getPlace().equals("1")) {
                        ParticipantsInfo.add(model_mil);
                        System.out.println(model_mil);
                    }
                }
                progress.dismiss();
            }
        }).start();
        progress.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
               DialogEvent dialogEvent = new DialogEvent();
               dialogEvent.showDialog(activity,model,position,Dates[0],Dates[1],ParticipantsInfo);
            }
        });

    }
    @SuppressLint("SimpleDateFormat")
    private String getNextWeek(){
        int week  = 7;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        c.add(Calendar.DAY_OF_WEEK, week);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(c.getTime());
    }

    private int binarySearch(ArrayList<Model_mil>array,String target , int from , int to , String isDiff){
        int mid = from + (to - from) / 2;
        if(to >= from){
            if(array.get(mid).getName().equals(target)){
                Log.d("binary", "found" );
                return mid;
            }
            if(array.get(mid).getName().compareTo(target) > 0){
                Log.d("binary", "bigger" );
                return binarySearch(array,target ,from , mid-1 , isDiff);
            }
            if(array.get(mid).getName().compareTo(target) < 0){
                Log.d("binary", "smaller" );
                return binarySearch(array,target ,mid+1 , to , isDiff);
            }
        }
        return -1;
    }




}



package com.example.mardichim20;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapterData extends RecyclerView.Adapter<recyclerAdapterData.viewHolder>{
    private ArrayList<Model_Name> model;
    private Activity activity;
    public recyclerAdapterData(ArrayList<Model_Name> Data, Activity activity){
        this.model = Data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_data,parent,false);
        return new viewHolder(view);
    }

    @Override
    @SuppressLint("RecyclerView")
    public void onBindViewHolder(@NonNull viewHolder holder,  int position) {
        Model_Name model_name = model.get(position);
        String Phone =  String.valueOf(model_name.getPhone());
        String FormattedPhone = "";
        if(Phone.length()<6){
            FormattedPhone = Phone;
        }else{
            FormattedPhone = "0" + Phone.substring(0,2) +"-" + Phone.substring(2,5)+"-" + Phone.substring(5);
        }
        holder.id.setText(String.valueOf(model_name.getID()));
        holder.name.setText(String.valueOf(model_name.getName()));
        holder.pnum.setText(String.valueOf(model_name.getNumber()));
        holder.phone.setText(String.valueOf(FormattedPhone));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                //color animating
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#AFABAA"), Color.TRANSPARENT);
                colorAnimation.setDuration(400);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        holder.itemView.setBackgroundColor((int) animator.getAnimatedValue());
                    }
                });
                colorAnimation.start();


                ViewDialog viewDialog = new ViewDialog();
                viewDialog.showDialog(activity, model_name, false);

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //color animating
                ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), Color.parseColor("#AFABAA"), Color.TRANSPARENT);
                colorAnimation.setDuration(400);
                colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animator) {
                        holder.itemView.setBackgroundColor((int) animator.getAnimatedValue());
                    }

                });
                colorAnimation.start();

                new AlertDialog.Builder(activity)
                            .setTitle("Delete")
                            .setMessage("מחיקת איש הקשר: \n"+model_name.getName() +" | " + model_name.getNumber())
                            .setPositiveButton("מחק", new DialogInterface.OnClickListener() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DataBaseHelperPhone dataBaseHelperPhone = new DataBaseHelperPhone(activity);
                                    dataBaseHelperPhone.deleteTitle(String.valueOf(model_name.getID()));
                                    model.remove(position);
                                    notifyItemRemoved(position);
                                }
                            })
                        .setNegativeButton("בטל",null)
                        .show();

                return false;
            }
        });



    }

    @Override
    public int getItemCount() {
        return model.size();
    }



    public static class viewHolder extends RecyclerView.ViewHolder{
        public TextView id,name,phone,pnum;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.column_name);
            phone = itemView.findViewById(R.id.column_phone_number);
            id = itemView.findViewById(R.id.ID);
            pnum = itemView.findViewById(R.id.column_personal_number);
        }
    }








}



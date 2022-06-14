package com.example.mardichim20;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ViewDialog{
     private AutoCompleteTextView autoCompleteName;
     private TextView HeadLine;

     private boolean t = true;

    public void showDialog(Activity activity, Model_Name model_name, boolean add){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        Button dialogBtn_cancel = (Button) dialog.findViewById(R.id.cancel_button);
        dialogBtn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (add){
            DataBaseHelperPhone dataBaseHelperPhone = new DataBaseHelperPhone(activity.getApplicationContext());
            ArrayList<Model_Name> GETINFORM = (ArrayList<Model_Name>) dataBaseHelperPhone.GETINFO();
            String[] Names = new String[GETINFORM.size()];
            if(t){
                for(int i=0 ; i< GETINFORM.size() ; i++){
                    Model_Name name = GETINFORM.get(i);
                    Names[i] = name.getName();
                    System.out.println(Names[i]);
                }
                t = false;
            }


            autoCompleteName = dialog.findViewById(R.id.editTextName);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity.getApplicationContext(), android.R.layout.simple_list_item_1,Names);
            autoCompleteName.setAdapter(adapter);




            Button dialogBtn_okay = (Button) dialog.findViewById(R.id.save_button);
            dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText Name = (EditText) dialog.findViewById(R.id.editTextName);
                    EditText Phone = (EditText) dialog.findViewById(R.id.editTextPhone);
                    EditText Num = (EditText) dialog.findViewById(R.id.editTextNum);

                    String stringName = Name.getText().toString();
                    String stringPhone = Phone.getText().toString();
                    String stringNum = Num.getText().toString();

                    if(stringName.equals("")||stringPhone.equals("")||stringNum.equals("")){
                        Toast.makeText(activity, "Not Saved", Toast.LENGTH_SHORT).show();
                        if(stringName.equals("")){
                            Name.setHint("Please enter a Name ");
                            Name.setHintTextColor(Color.RED);
                        }
                        if(stringPhone.equals("")){
                            Phone.setHint("Please enter a Phone Number ");
                            Phone.setHintTextColor(Color.RED);
                        }
                        if(stringNum.equals("")){
                            Num.setHint("Please enter a Personal Number ");
                            Num.setHintTextColor(Color.RED);
                        }
                    }else {
                        Model_Name model_name = new Model_Name(stringName, Integer.parseInt(stringPhone),Integer.parseInt(stringNum));
                        DataBaseHelperPhone PhoneDataBase = new DataBaseHelperPhone(activity.getApplicationContext());
                        List<Model_Name> GETINFORM = PhoneDataBase.GETINFO();
                        int ID = IfTheresNum(GETINFORM,stringNum);
                        if(ID == 0){

                            PhoneDataBase.addOne(model_name);//if there is NO equal personal num just add

                        }else{

                            AlertDialog alertDialog = new AlertDialog.Builder(activity)
                                    .setTitle("שם שמור כבר במערכת")
                                    .setMessage("חיל עם מספר אישי זה שמור כבר במערכת האם תרצה לעדכן את פרטיו")
                                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            PhoneDataBase.deleteTitle(String.valueOf(ID));//if there IS personal num delete then add the old one and add the new one
                                            PhoneDataBase.addOne(model_name);
                                        }
                                    })
                                    .setNegativeButton("Cancel", null)
                                    .show();


                        }
                        dialog.cancel();
                    }
                }
            });
             }else{
            HeadLine = dialog.findViewById(R.id.HeadLineD);
            HeadLine.setText("עדכן איש קשר");
            EditText Name = (EditText) dialog.findViewById(R.id.editTextName);
            EditText Phone = (EditText) dialog.findViewById(R.id.editTextPhone);
            EditText Num = (EditText) dialog.findViewById(R.id.editTextNum);
            Button dialogBtn_okay = (Button) dialog.findViewById(R.id.save_button);
            String id = String.valueOf(model_name.getID());


            Name.setText(String.valueOf(model_name.getName()));
            Phone.setText(String.valueOf("0"+model_name.getPhone()));
            Num.setText(String.valueOf(model_name.getNumber()));
            dialogBtn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataBaseHelperPhone dataBaseHelperPhone =new DataBaseHelperPhone(activity);
                    dataBaseHelperPhone.deleteTitle(id);
                    EditText Name = (EditText) dialog.findViewById(R.id.editTextName);
                    EditText Phone = (EditText) dialog.findViewById(R.id.editTextPhone);
                    EditText Num = (EditText) dialog.findViewById(R.id.editTextNum);
                    String stringName = Name.getText().toString();
                    String stringPhone = Phone.getText().toString();
                    String stringNum = Num.getText().toString();

                    Model_Name modelNameUpdate = new Model_Name(stringName,Integer.parseInt(stringPhone),Integer.parseInt(stringNum));
                    dataBaseHelperPhone.addOne(modelNameUpdate);
                    dialog.cancel();

                }
            });




        }

        dialog.show();

    }

    private int IfTheresNum( List<Model_Name> GETINFORM, String num){

        for(int i = 0; i< GETINFORM.size(); i++){
            Model_Name modelName1 = GETINFORM.get(i);
            if(num.equals(String.valueOf(modelName1.getNumber()))){
                return modelName1.getID();
            }
        }
        return 0;
    }



}

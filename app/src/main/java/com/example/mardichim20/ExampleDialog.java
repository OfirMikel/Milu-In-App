package com.example.mardichim20;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ExampleDialog extends AppCompatDialogFragment{
    private AutoCompleteTextView autoCompleteName;
    private AutoCompleteTextView autoCompleteWHO;
    private AutoCompleteTextView autoCompletePlace;
    private Button button;
    private EditText edit_Bdate;
    private EditText edit_Edate;
    private EditText edit_who;
    private EditText edit_place;
    private EditText edit_name;
    private ExampleDialogListener listener;
    private String dateB;
    private String dateE;
    private DatePickerDialog.OnDateSetListener setListenerBD;
    private DatePickerDialog.OnDateSetListener setListenerED;
    private int counter=0;





    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("en"));
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

        View view = inflater.inflate(R.layout.layout_dialog_mil,null);
        final AlertDialog alertDialog = builder.create();
        builder.setView(view);
        DataBaseHelperPhone dataBaseHelperPhone = new DataBaseHelperPhone(getContext());
        ArrayList<Model_Name> GETINFORM = (ArrayList<Model_Name>) dataBaseHelperPhone.GETINFO();
        String[] Names = new String[GETINFORM.size()];

        for(int i=0 ; i< GETINFORM.size() ; i++){
            Model_Name name = GETINFORM.get(i);
            Names[i] = name.getName();
        }


        button = view.findViewById(R.id.button_mil_add);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout rootView = (LinearLayout) view.findViewById(R.id.rootLayout);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT,WRAP_CONTENT,1);
                AutoCompleteTextView editText = new AutoCompleteTextView(rootView.getContext());
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,Names);
                editText.setAdapter(adapter);
                editText.setLayoutParams(params);
                rootView.addView(editText);
                editText.setGravity(Gravity.TOP);
                editText.setHint("שם משתתף באירוע");
                Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "tryF.ttf");
                editText.setTypeface(font);
                edit_name = view.findViewById(R.id.edit_name);
                edit_name.setHint("שם האירוע (ניתן להשאיר ריק)");
                button.setText("+ הוסף שם");
                counter++;
                editText.setId(counter);
                System.out.println(counter);
            }
        });


        //Auto complete the name of the solider


        if(counter == 0){
            autoCompleteName = view.findViewById(R.id.edit_name);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,Names);
            autoCompleteName.setAdapter(adapter);
        }


        String[] WHO = {"אופיר מיקל", "מאיר אפללו","בתאל בוסקילה","עומרי עבד","ליעם קלוד","שקד סגל","יניב מור יוסף","נתי נעים","יהונתן זילכה","עידו נשר "};
        autoCompleteWHO = view.findViewById(R.id.edit_who);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,WHO);
        autoCompleteWHO.setAdapter(adapter1);

        String[] places = {"נבטים בח\"א28", "מרום", "קריה","ירפא"};
        autoCompletePlace = view.findViewById(R.id.edit_place);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1,places);
        autoCompletePlace.setAdapter(adapter2);


        //Date Picker
        edit_Bdate = view.findViewById(R.id.edit_Bdate);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        edit_Bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),setListenerBD,year,month,day);
                int nightModeFlags = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                switch (nightModeFlags) {
                    case Configuration.UI_MODE_NIGHT_YES:
                        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                        break;
                    case Configuration.UI_MODE_NIGHT_NO:
                                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        break;
                    case Configuration.UI_MODE_NIGHT_UNDEFINED:
                                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                        break;
                }
                datePickerDialog.show();
            }
        });
        setListenerBD = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                dateB = dayOfMonth +"/" + month+"/" + year;
                if(dayOfMonth<10){
                    if(month<10){
                        dateB = "0" +dayOfMonth +"/0" + month+"/" + year;
                    }else{
                        dateB = "0" +dayOfMonth +"/" + month+"/" + year;
                    }
                }
                if(month<10){
                    if(dayOfMonth<10){
                        dateB = "0" +dayOfMonth +"/0" + month+"/" + year;
                    }else{
                        dateB = dayOfMonth +"/0" + month+"/" + year;
                    }
                }
                edit_Bdate.setText(dateB);
            }
        };

        edit_Edate = view.findViewById(R.id.edit_Edate);
        edit_Edate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),setListenerED,year,month,day);
                int nightModeFlags = getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                switch (nightModeFlags) {
                    case Configuration.UI_MODE_NIGHT_YES:
                        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
                        break;

                        case Configuration.UI_MODE_NIGHT_NO:
                            datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                            break;

                            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLUE));
                                break;
                        }
                        datePickerDialog.show();
                    }
                });
        setListenerED = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month = month + 1;
                        dateE = dayOfMonth +"/" + month+"/" + year;
                        if(dayOfMonth<10){
                            if(month<10){
                                dateE = "0" +dayOfMonth +"/0" + month+"/" + year;
                            }else{
                                dateE = "0" +dayOfMonth +"/" + month+"/" + year;
                            }

                        }
                        if(month<10){
                            if(dayOfMonth<10){
                                dateE = "0" +dayOfMonth +"/0" + month+"/" + year;
                            }else{
                                dateE = dayOfMonth +"/0" + month+"/" + year;
                            }
                        }
                        edit_Edate.setText(dateE);
                    }
                };





        builder.setNegativeButton("Cancel", null);






            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int i) {

                    if(counter <= 0){

                        edit_name = view.findViewById(R.id.edit_name);
                        String name = edit_name.getText().toString().trim();

                        edit_who = view.findViewById(R.id.edit_who);
                        String who = edit_who.getText().toString();

                        edit_place = view.findViewById(R.id.edit_place);
                        String place = edit_place.getText().toString();

                        if (isThereEqual(name,GETINFORM )
                                && isValid(edit_Bdate.getText().toString(),edit_Edate.getText().toString()) ) {

                            listener.applyTexts(name, dateB, dateE, who, place);

                        }else if(!isThereEqual(name,GETINFORM)
                                && isValid(edit_Bdate.getText().toString(),edit_Edate.getText().toString())){

                            Toast.makeText(getContext(), "לא נשמר עקב שם לא שמור במערכת", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(getContext(), "לא נשמר עקב מחסור במידע", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        edit_name = view.findViewById(R.id.edit_name);
                        String name = edit_name.getText().toString().trim();

                        edit_who = view.findViewById(R.id.edit_who);
                        String who = edit_who.getText().toString();

                        edit_place = view.findViewById(R.id.edit_place);
                        String place = edit_place.getText().toString();


                        if(!name.trim().equals("")){
                            String EventName = edit_name.getText().toString();
                            String event = "event";
                            listener.applyTexts(EventName, dateB, dateB, event, "0");
                            for(int j = 0 ; j < counter ; j++ ){
                                EditText t = view.findViewById(j+1);
                                String textName = t.getText().toString();
                                listener.applyTexts(textName, dateB, dateE, who, place);
                            }
                            listener.applyTexts(EventName, dateE, dateE, event, "1");
                        }else{
                            for(int j = 0 ; j < counter ; j++ ){
                                EditText t = view.findViewById(j+1);
                                String textName = t.getText().toString();
                                listener.applyTexts(textName, dateB, dateE, who, place);
                            }
                        }


                    }


                }
            });

        return builder.create();


    }


    public interface ExampleDialogListener {
        void applyTexts(String name, String Bdate,String Edate,String who,String place);
    }

    public boolean isThereEqual(String a,ArrayList<Model_Name> b){
        for (int i =0 ; i<b.size();i++){
            Model_Name model_name = b.get(i);
            if (model_name.getName().toLowerCase().trim().contains(a)) {
                return true;
            }
        }
        return false;
    }
    public boolean isValid(String a,String b){
        if(a.length() == 10&& b.length() == 10){
            return true;
        }
        return false;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+ "must implement ExampleDialogListener");

        }
    }

}

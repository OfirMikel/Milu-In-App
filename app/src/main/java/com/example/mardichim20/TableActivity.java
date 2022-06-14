package com.example.mardichim20;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TableActivity extends AppCompatActivity {
    static {
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLInputFactory",
                "com.fasterxml.aalto.stax.InputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLOutputFactory",
                "com.fasterxml.aalto.stax.OutputFactoryImpl"
        );
        System.setProperty(
                "org.apache.poi.javax.xml.stream.XMLEventFactory",
                "com.fasterxml.aalto.stax.EventFactoryImpl"
        );
    }

    ArrayList<Model_Name> Data =null;
    SwipeRefreshLayout swipeRefreshLayout;
    recyclerAdapterData adapter;
    Model_Name model_name;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        getSupportActionBar().setTitle(" Milu-In");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#003300")));
        getWindow().setStatusBarColor(Color.parseColor("#003300"));

        Configuration configuration = getResources().getConfiguration();//Set layout to LTR on RTL phone
        configuration.setLayoutDirection(new Locale("en"));//Set layout to LTR on RTL phone
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());//Set layout to LTR on RTL phone
        fontSizeAdjust(configuration);

        DataBaseHelperPhone dataBaseHelperMil = new DataBaseHelperPhone(this);
        Data = (ArrayList<Model_Name>) dataBaseHelperMil.GETINFO();


        Button buttonAdd;
        buttonAdd = findViewById(R.id.button_add_table);
        buttonAdd.setOnClickListener(v -> {
            ViewDialog viewDialog = new ViewDialog();
            viewDialog.showDialog(TableActivity.this,null,true);
        });
        swipeRefreshLayout = findViewById(R.id.SwipeR);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            Refresh();
            swipeRefreshLayout.setRefreshing(false);
        });
        Show_data();


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id){
            case R.id.search2:
                Toast.makeText(this, "working", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ReadExcelFile:
                chooseFile();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Refresh(){
        DataBaseHelperPhone dataBaseHelperMil = new DataBaseHelperPhone(TableActivity.this);
        Data = (ArrayList<Model_Name>) dataBaseHelperMil.GETINFO();
        Show_data();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_data,menu);
        MenuItem menuItem = menu.findItem(R.id.search2);

        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("שם חייל המיל או מספרו האישי");
        searchView.setTextDirection(View.TEXT_DIRECTION_RTL);
        searchView.setOnCloseListener(() -> {
            Show_data();
            return false;
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

                DataBaseHelperPhone dataBaseHelperPhone = new DataBaseHelperPhone(TableActivity.this);
                Data = (ArrayList<Model_Name>) dataBaseHelperPhone.GETINFO();
                java.util.List<Model_Name> needToRemoveName = new ArrayList<>();

                for (int i = 0; i < Data.size(); i++) {
                    Model_Name model_name = Data.get(i);
                    String Pnum = "" + model_name.getNumber();

                    if(!model_name.getName().contains(newText) ){
                        if(Pnum.contains(newText)){
                            System.out.println(newText);
                        }else{
                            needToRemoveName.add(Data.get(i));
                        }
                    }
                }
                Data.removeAll(needToRemoveName);
                Show_data();

                return false;
            }
        });



        return true;
    }



    public void Show_data(){
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new recyclerAdapterData(Data,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void fontSizeAdjust(Configuration configuration) {
        configuration.fontScale = (float) 1.0;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }





    public void chooseFile(){

        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        chooseFile = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(chooseFile, 7);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(MainActivity.this, data.toString(), Toast.LENGTH_LONG).show();

        switch (requestCode) {

            case 7:

                if (resultCode == RESULT_OK) {

                    String PathHolder = data.getData().getPath().replace("primary:","");

                    Toast.makeText(this, PathHolder, Toast.LENGTH_LONG).show();
                    Log.d("DataOUTPUT",PathHolder);
                    Uri uri = data.getData();
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ReadExcelFile(TableActivity.this, uri);


                }
                break;

        }
    }

    public void ReadExcelFile(Context context, Uri uri) {
        try {
            InputStream inStream;
            Workbook wb = null;

            try {
                inStream = context.getContentResolver().openInputStream(uri);
                wb = new XSSFWorkbook(inStream);

                inStream.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            assert wb != null;
            Sheet sheet = wb.getSheetAt(0);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startLoader(sheet);
            }
            Refresh();

        } catch (Exception ex) {
            Toast.makeText(this, "ReadExcelFile Error:" + ex.getMessage().toString(), Toast.LENGTH_SHORT).show();
            Debugging(ex.getMessage().toString());
        }
    }

    private void Debugging(String message){
        System.out.println(message);
        Log.d("Debugging",message);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startLoader(Sheet sheet) {
        int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
        Debugging(String.valueOf(rowCount));
        for (int i = 1 ; i <=rowCount ; i++) { //Start From one because of the header
            Row row = sheet.getRow(i);
            row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
            row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
            row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCellType(CellType.STRING);
            String name = row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
            String num = row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
            String phone = row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
            model_name = new Model_Name(name, Integer.parseInt(phone), Integer.parseInt(num));
            DataBaseHelperPhone dataBaseHelperPhone = new DataBaseHelperPhone(TableActivity.this);
            dataBaseHelperPhone.addOne(model_name);
        }



    }


}
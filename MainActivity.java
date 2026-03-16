package com.example.todolistapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button add,deleteAll; //“+” veya “Add” butonu //ve her şeyi sil butonu
    AlertDialog dialog; //Görev eklemek için açılan pencere
    LinearLayout layout; //Görev kartlarının eklendiği ana alan
    RecyclerView recyclerView;
    List<Task> taskList;
    TaskAdapter adapter;
    CalendarView calendarView;
    String selectedDate; //O an seçili olan tarihi "yyyy-MM-dd" formatında tutar.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //Ekrana activity_main.xml yerleştirilir

        //XML’deki buton ve LinearLayout Java tarafına bağlanır
        add=findViewById(R.id.add);
        deleteAll = findViewById(R.id.deleteAll);
        calendarView = findViewById(R.id.calendarView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList, new TaskAdapter.OnTaskChangedListener() {
            @Override
            public void onTaskChanged(){
                saveTasks();
            }
        });
        recyclerView.setAdapter(adapter);//adapterı bağlama

        //Önce tarihi belirle (loadTasks'in doğru çalışması için bu değişkenin dolu olması şart)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        selectedDate = sdf.format(new Date());

        //Arayüzü hazırla ve verileri yükle
        buildDialog();
        loadTasks();

        add.setOnClickListener(new View.OnClickListener(){ //add butonuna basıldığında
               @Override
            public void onClick(View v){
                dialog.show();
            }
        });

        //Delete (Silme)
        deleteAll.setOnClickListener(v ->  {
                new AlertDialog.Builder(this)
                        .setTitle("Delete All Task")
                        .setMessage("Are you sure you want to delete all tasks?")
                        .setPositiveButton("Yes",(dialog, which) -> {
                            taskList.clear();//Bellekteki listeyi temizle
                            adapter.notifyDataSetChanged();//Görünümü yenile
                            saveTasks();//Güncelle
                        })
                        .setNegativeButton("CANCEL",null)
                        .show();
        });

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> { //herhangi bir güne tıklandığında
            month = month + 1; //aylar sıfırdan başlatıldığı için bir ekliyoruz

            //rakamsa başına sıfır koy
            selectedDate = year + "-" +
                    String.format("%02d",month) + "-" +
                    String.format("%02d",dayOfMonth);

            loadTasks(); //yeni gün görevleri yükler
        });
    }
    public void buildDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //Dialog oluşturmak için builder kullanılır
        View view = getLayoutInflater().inflate(R.layout.dialog,null); //dialog.xml tasarımı şişirilir (inflate edilir)

        final EditText name = view.findViewById(R.id.nameEdit); //Kullanıcının görev yazacağı EditText

        builder.setView(view);
        builder.setTitle("Enter your Task")
                .setPositiveButton("SAVE",(dialog,which) -> {
                        String taskName = name.getText().toString().trim();
                        if(!taskName.isEmpty()){
                        addTask(taskName);
                    }
                    name.setText("");
                })
                .setNegativeButton("CANCEL",null);

        dialog = builder.create();
    }

    public void addTask(String name){ //Create (Oluşturma)
        taskList.add(new Task(name, false));
        adapter.notifyItemInserted(taskList.size()-1);
        saveTasks();
    }

    //Verileri JSON formatına çevirip SharedPreferences'a seçili tarihe göre kaydeder.
    private void saveTasks(){ //Update (Güncelleme)
        JSONArray array = new JSONArray();
        for (Task task : taskList){
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", task.getName());
                obj.put("done", task.isDone());
                array.put(obj);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString(selectedDate, array.toString()).apply();
    }

    //Seçili tarihe ait JSON verisini okur, Java nesnelerine çevirir ve listeyi yeniler.
    private void loadTasks(){ //Read (Okuma)
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String tasksString = prefs.getString(selectedDate, "[]"); //güne ait veri yoksa boş dizi döner

        try{
            JSONArray array = new JSONArray(tasksString);
            taskList.clear();//Listeyi temizlemezsek eski günler üzerine eklenmeye devam eder.

            for (int i=0; i<array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                taskList.add(new Task(
                        obj.getString("name"),
                        obj.getBoolean("done")
                ));
            }

            adapter.notifyDataSetChanged(); //görünümü güncelle

        }catch (JSONException e){
            Log.e("Hata", "Görev yüklenemedi", e);//e.printStackTrace(); yerine kullanıldı
        }
    }
}

package com.example.todolistapp;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context context;
    private List<Task> taskList;
    private OnTaskChangedListener listener;

    // MainActivity'ye veri değiştiğinde (silme/işaretleme) haber vermek için kullanılan köprü (Callback)
    public interface OnTaskChangedListener{
        void onTaskChanged();
    }

    public TaskAdapter(Context context, List<Task> taskList, OnTaskChangedListener listener){
        this.context = context;
        this.taskList = taskList;
        this.listener = listener;
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        CheckBox checkBox;
        ImageButton delete;

        public TaskViewHolder(@NonNull View itemView){
            super(itemView);
            //Kart tasarımındaki bileşenleri Java nesnelerine bağlıyoruz
            name = itemView.findViewById(R.id.name);
            checkBox = itemView.findViewById(R.id.checkTask);
            delete = itemView.findViewById(R.id.delete);
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        //card.xml tasarımını belleğe yükleyip (inflate) yeni bir ViewHolder oluşturur
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position){
        Task task = taskList.get(position);//O anki satırın veri modelini al
        holder.name.setText(task.getName());

        // CheckBox dinleyicisini geçici olarak kapatıyoruz (Geri yükleme sırasında tetiklenmemesi için)
        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(task.isDone());

        // Eğer tamamlandıysa üstü çizili göster
        if(task.isDone()){
            holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.name.setPaintFlags(holder.name.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        //CheckBox işaretlendiğinde veriyi güncelle ve üst çizgi efektini tetikle
        holder.checkBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            task.setDone(isChecked);
            notifyItemChanged(holder.getAdapterPosition());
            listener.onTaskChanged();
        }));

        //Silme butonuna tıklandığında
        holder.delete.setOnClickListener(v -> {
            int pos = holder.getAbsoluteAdapterPosition();

            if (pos != RecyclerView.NO_POSITION) { //Geçersiz pozisyon kontrolü (çökmeyi önlemek için)
                taskList.remove(pos); //Veriyi listeden kaldırır
                notifyItemRemoved(pos); //Silinen verinin kutusunu çeker
                notifyItemRangeChanged(pos, taskList.size()); //Alttaki öğelerin pozisyonlarını güncelle (Pozisyon kaymasını önler)
                listener.onTaskChanged(); //Yeni listenin kaydedilmesi için arayüze bildir
            }
        });
    }

    public int getItemCount(){
        return taskList.size();
    }
}

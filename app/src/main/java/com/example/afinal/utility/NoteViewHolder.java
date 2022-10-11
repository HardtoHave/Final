package com.example.afinal.utility;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.R;
import com.example.afinal.bean.Note;


public class NoteViewHolder extends RecyclerView.ViewHolder {
    private TextView item_id;
    private ImageView item_image;
    private TextView item_content;
    private TextView item_time;
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        item_time = itemView.findViewById(R.id.item_time);
        item_id = itemView.findViewById(R.id.item_id);
        item_image = itemView.findViewById(R.id.item_image);
        item_content = itemView.findViewById(R.id.item_content);
    }
    public void bind(Note note){
        item_id.setText(note.getId()+"");
        switch (note.getStatue()){
            case "1":
                String con=note.getScheduled()+"~"+note.getDeadline();
                item_time.setText(con);
                String tex="Success ! Keep going";
                item_content.setText(tex);
                break;
            case "0":
                item_time.setText(note.getScheduled());
                item_content.setText("Failed！");
                break;
        }
    }
}

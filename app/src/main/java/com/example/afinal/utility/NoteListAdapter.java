package com.example.afinal.utility;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.afinal.R;
import com.example.afinal.bean.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteViewHolder> implements View.OnClickListener{

    private final List<Note> notes=new ArrayList<>();

    public NoteListAdapter(){

    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
    private OnItemClickListener onItemClickListener;

    public void refresh(List<Note> newNotes){
        notes.clear();
        if (newNotes!=null){
            notes.addAll(newNotes);
        }
        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position){
        Note note=notes.get(position);
        if (note.getStatue().equals("0")){
            return 1;
        }else if (note.getStatue().equals("1")){
            return 2;
        }
        return 0;
    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int pos) {
        View itemView;
        if (pos==1){
            itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo,parent,false);
        }else {
            itemView= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_done,parent,false);
        }
        itemView.setOnClickListener(this);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.itemView.setTag(position);
        holder.bind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener!=null){
            onItemClickListener.onItemClick(view,(int)view.getTag());
        }
    }
}

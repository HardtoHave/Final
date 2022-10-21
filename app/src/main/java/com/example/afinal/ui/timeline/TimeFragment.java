package com.example.afinal.ui.timeline;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afinal.R;
import com.example.afinal.bean.Note;
import com.example.afinal.db.DBContract;
import com.example.afinal.db.DBHelper;
import com.example.afinal.utility.NoteListAdapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class TimeFragment extends Fragment {
    private SQLiteDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_timeline, container,false);

    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        DBHelper dbHelper = new DBHelper(getActivity());
        database= dbHelper.getWritableDatabase();
        //recycler view to show timeline
        RecyclerView recyclerView = view.findViewById(R.id.list_todo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL));
        NoteListAdapter notesAdapter = new NoteListAdapter();
        recyclerView.setAdapter(notesAdapter);
        notesAdapter.refresh(loadNotesFromDatabase());
    }
    @SuppressLint("Range")
    //get information from database
    private List<Note> loadNotesFromDatabase(){
        if (database==null){
            return Collections.emptyList();
        }
        List<Note> result=new LinkedList<>();
        try (Cursor cursor = database.query(DBContract.ToDoNote.TABLE_NAME, null, null, null, null, null, null)) {
            while (cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(DBContract.ToDoNote.COLUMN_ID));
                String caption = cursor.getString(cursor.getColumnIndex(DBContract.ToDoNote.COLUMN_CAPTION));
                String state = cursor.getString(cursor.getColumnIndex(DBContract.ToDoNote.COLUMN_STATE));
                String scheduled = cursor.getString(cursor.getColumnIndex(DBContract.ToDoNote.COLUMN_SCHEDULED));
                String time = cursor.getString(cursor.getColumnIndex(DBContract.ToDoNote.COLUMN_TIME));
                String deadline = cursor.getString(cursor.getColumnIndex(DBContract.ToDoNote.COLUMN_DEADLINE));
                Note note = new Note(id);
                note.setCaption(caption);
                note.setStatue(state);
                note.setDeadline(deadline);
                note.setScheduled(scheduled);
                note.setTime(time);
                result.add(note);
            }
        }
        return result;
    }

}
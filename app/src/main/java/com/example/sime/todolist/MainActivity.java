package com.example.sime.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ManageDB Mdb;
    ArrayAdapter<String> mAdapter;
    ListView listTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Mdb = new ManageDB(this);
        listTask = (ListView) findViewById(R.id.task);

        loadTaskList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View parent = (View) view.getParent();
                final EditText taskEditText = new EditText(view.getContext());
                AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("Add New Task")
                        .setMessage("What do you want to do next?")
                        .setView(taskEditText)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(taskEditText.getText());
                                Mdb.insertNewTask(task);
                                loadTaskList();
                            }
                        })
                        .setNegativeButton("Cancel",null)
                        .create();
                dialog.show();
            }
        });
    }

    private void loadTaskList() {
        ArrayList<String> taskList = Mdb.getTaskList();
        if(mAdapter==null){
            mAdapter = new ArrayAdapter<String>(this, R.layout.view, R.id.taskTitle, taskList);
            listTask.setAdapter(mAdapter);
        }
        else{
            mAdapter.clear();
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void deleteTask(View view){
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.taskTitle);

        String task = String.valueOf(taskTextView.getText());
        Mdb.deleteTask(task);
        loadTaskList();
    }

    public void editTask(View view) {
        final EditText taskEditText = new EditText(view.getContext());
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.taskTitle);
        final String task = String.valueOf(taskTextView.getText());

        taskEditText.setText(task);
        AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                .setTitle("Edit Task")
                .setMessage("Enter your new task?")
                .setView(taskEditText)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newTask = String.valueOf(taskEditText.getText());
                        Mdb.updateTask(task, newTask);
                        loadTaskList();
                    }
                })
                .setNegativeButton("Cancel",null)
                .create();
        dialog.show();
    }
}
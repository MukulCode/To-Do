package com.test.todo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.todo.DTO.ToDo;

public class DashboardActivity extends AppCompatActivity {
    DBHandler dbHandler;
    DashboardActivity activity;
    Toolbar dashboard_toolbar;
    RecyclerView rv_dashboard;
    FloatingActionButton fab_dashboard;
    DashboardAdapter dashboardAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dashboard_toolbar = findViewById(R.id.dashboard_toolbar);
        rv_dashboard = findViewById(R.id.rv_dashboard);
        fab_dashboard = findViewById(R.id.fab_dashboard);
        setSupportActionBar(dashboard_toolbar);
        setTitle("Dashboard");
        activity = this;
        dbHandler = new DBHandler(activity);
        rv_dashboard.setLayoutManager(new LinearLayoutManager(activity));


        fab_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                dialog.setTitle("Add ToDo");
                View view = getLayoutInflater().inflate(R.layout.dialog_dashboard, null);
                final EditText toDoName = view.findViewById(R.id.ev_todo);
                dialog.setView(view);

                dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (toDoName.getText().toString().length() > 0) {
                            ToDo toDo = new ToDo();
                            toDo.setName(toDoName.getText().toString());
                            dbHandler.addToDo(toDo);
                            refreshList();
                        }
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
            }
        });
        dashboardAdapter = new DashboardAdapter(activity, dbHandler.getToDos());
    }

    @Override
    protected void onResume() {
        refreshList();
        super.onResume();
    }

    public void updateToDo(final ToDo toDo) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setTitle("Update ToDo");
        View view = getLayoutInflater().inflate(R.layout.dialog_dashboard, null);
        final EditText toDoName = view.findViewById(R.id.ev_todo);
        toDoName.setText(toDo.getName());
        dialog.setView(view);
        dialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (toDoName.getText().toString().length() > 0) {
                    toDo.setName(toDoName.getText().toString());
                    dbHandler.updateToDo(toDo);
                    refreshList();
                }
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    public void refreshList() {
        rv_dashboard.setAdapter(new DashboardAdapter(activity, dbHandler.getToDos()));
    }
}
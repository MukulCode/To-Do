package com.test.todo;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.test.todo.DTO.ToDo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.test.todo.Const.INTENT_TODO_ID;
import static com.test.todo.Const.INTENT_TODO_NAME;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {
    ArrayList<ToDo> list;
    DashboardActivity activity;

    DashboardAdapter(DashboardActivity activity, ArrayList<ToDo> list) {
        this.list = list;
        this.activity = activity;

    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(activity).inflate(R.layout.rv_child_dashboard, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int i) {
        holder.toDoName.setText(list.get(i).getName());

        holder.toDoName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, ItemActivity.class);
                intent.putExtra(INTENT_TODO_ID, list.get(i).getId());
                intent.putExtra(INTENT_TODO_NAME, list.get(i).getName());
                activity.startActivity(intent);
            }
        });

        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(activity, holder.menu);
                popup.inflate(R.menu.dashboard_child);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_edit: {
                                activity.updateToDo(list.get(i));
                                break;
                            }
                            case R.id.menu_delete: {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
                                dialog.setTitle("Are you sure");
                                dialog.setMessage("Do you want to delete this task ?");
                                dialog.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        activity.dbHandler.deleteToDo(list.get(holder.getAdapterPosition()).getId());
                                        activity.refreshList();
                                    }
                                });
                                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                                dialog.show();
                            }
                            case R.id.menu_mark_as_completed: {
                                activity.dbHandler.updateToDoItemCompletedStatus(list.get(i).getId(), true);
                                break;
                            }
                            case R.id.menu_reset: {
                                activity.dbHandler.updateToDoItemCompletedStatus(list.get(i).getId(), false);
                                break;
                            }
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView toDoName;
        ImageView menu;

        ViewHolder(View v) {
            super(v);
            toDoName = v.findViewById(R.id.tv_todo_name);
            menu = v.findViewById(R.id.iv_menu);
        }
    }
};
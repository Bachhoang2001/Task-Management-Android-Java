package com.example.taskmanagement;


import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanagement.model.TaskModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {

    private ArrayList<TaskModel> taskDataset;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView taskNameTv, taskStatusTv;
        LinearLayout containerLl;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            taskNameTv = (TextView) view.findViewById(R.id.taskNameTv);
            taskStatusTv = (TextView) view.findViewById(R.id.taskStatusTv);
            containerLl = (LinearLayout) view.findViewById(R.id.containerLl);
        }


    }

    public TaskListAdapter(ArrayList<TaskModel> taskDataset) {
        this.taskDataset = taskDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_task, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.taskNameTv.setText(taskDataset.get(position).getTaskName());
        viewHolder.taskStatusTv.setText(taskDataset.get(position).getTaskStatus());

        String status = taskDataset.get(position).getTaskStatus();
        if(status.toLowerCase().equals("pending")){
            viewHolder.taskStatusTv.setBackgroundColor(Color.parseColor("#FFFF00"));
        } else if(status.toLowerCase().equals("completed")){
            viewHolder.taskStatusTv.setBackgroundColor(Color.parseColor("#00FF00"));
        } else {
            viewHolder.taskStatusTv.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        viewHolder.containerLl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), viewHolder.containerLl);
                popupMenu.inflate(R.menu.taskmenu);
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if(menuItem.getItemId() == R.id.deleteMenu){
                            FirebaseFirestore.getInstance().collection("tasks").document(taskDataset.get(position).getTaskId()).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(view.getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                                            viewHolder.containerLl.setVisibility(View.GONE);
                                        }
                                    });
                        }

                        if(menuItem.getItemId() == R.id.markCompletedMenu){

                            TaskModel completedTask = taskDataset.get(position);
                            completedTask.setTaskStatus("completed");
                            FirebaseFirestore.getInstance().collection("tasks").document(taskDataset.get(position).getTaskId()).set(completedTask)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(view.getContext(), "Item completed", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            viewHolder.taskStatusTv.setBackgroundColor(Color.parseColor("#00FF00"));
                            viewHolder.taskStatusTv.setText("completed");
                        }

                        return false;
                    }
                });

                return false;
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return taskDataset.size();
    }
    public void clearAllItems(){
        taskDataset.clear();
        notifyDataSetChanged();

    }
}


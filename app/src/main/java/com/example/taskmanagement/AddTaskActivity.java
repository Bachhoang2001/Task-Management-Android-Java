package com.example.taskmanagement;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taskmanagement.model.TaskModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddTaskActivity extends AppCompatActivity {

    EditText etTaskInput;
    Button saveBtn;
    String TAG = "TaskManagement";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        getSupportActionBar().setTitle("Add Task");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        saveBtn = findViewById(R.id.taskSaveBtn);
        etTaskInput = findViewById(R.id.inputTaskName);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String taskName = etTaskInput.getText().toString().trim();
                if(taskName != null) {
                    findViewById(R.id.progress).setVisibility(View.VISIBLE);
                    TaskModel taskModel = new TaskModel("", taskName, "pending", FirebaseAuth.getInstance().getUid());
                    db.collection("tasks")
                            .add(taskModel)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                    showSnackbar("Successfully");
                                    findViewById(R.id.successLayout).setVisibility(View.VISIBLE);
                                    findViewById(R.id.progress).setVisibility(View.GONE);
                                    findViewById(R.id.addTaskLayout).setVisibility(View.GONE);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error adding document", e);
                                    showSnackbar("Failed");
                                }
                            });
                }
            }
        });
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        View customSnackbarView = getLayoutInflater().inflate(R.layout.custom_snack_bar, null);
        ((Snackbar.SnackbarLayout) snackbarView).removeAllViews();
        ((Snackbar.SnackbarLayout) snackbarView).addView(customSnackbarView, 0);
        TextView textView = customSnackbarView.findViewById(R.id.snackbar_text);
        textView.setText(message);

        int textColor;
        if(message.equals("Successfully")){
            textColor = Color.GREEN;
        } else if (message.equals("Failed")) {
            textColor = Color.RED;
        } else {
            textColor = Color.WHITE;
        }
        textView.setTextColor(textColor);

        snackbar.show();
    }


    //Method custom Icon back page
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
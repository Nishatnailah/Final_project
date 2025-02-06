package com.example.hostelhub;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewRoomTypes extends AppCompatActivity {

    private ListView listviewDemo;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room_types);

        listviewDemo = findViewById(R.id.list_view_Demo);
        Button buttonUpdate = findViewById(R.id.button_update);
        Button buttonDelete = findViewById(R.id.button_delete);

        databaseHelper = new DatabaseHelper(this);

        displayDemo();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleUpdate();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDelete();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayDemo();
    }

    private void displayDemo() {
        Cursor cursor = databaseHelper.getAllDemo();
        if (cursor != null && cursor.getCount() > 0) {
            DemoAdapter adapter = new DemoAdapter(this, cursor, 0);
            listviewDemo.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No room types available.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleUpdate() {
        Intent intent = new Intent(ViewRoomTypes.this, UpdateHostelActivity.class);
        startActivity(intent);
    }

    private void handleDelete() {
        Intent intent = new Intent(ViewRoomTypes.this, DeleteHostelActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Delete button clicked", Toast.LENGTH_SHORT).show();
    }
}

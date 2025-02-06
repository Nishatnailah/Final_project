package com.example.hostelhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class FacilitesDisplay extends AppCompatActivity {

    private ListView listviewDemo;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private UserDemoAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilites_display);


        listviewDemo = findViewById(R.id.list_view_Id);
        SearchView searchView = findViewById(R.id.search_view);
        toolbar = findViewById(R.id.tool_bar);


        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("HostelHub", MODE_PRIVATE);


        displayDemo();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String trimmedText = newText.trim();
                Cursor filteredCursor = databaseHelper.getFilteredProducts(trimmedText);
                if (adapter != null) {
                    adapter.changeCursor(filteredCursor);
                }
                return false;
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayDemo();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.profile) {
            Intent intent = new Intent(FacilitesDisplay.this, ProfileActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.share) {
            Toast.makeText(this, "Share is pressed", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.search) {
            Toast.makeText(this, "Search is pressed", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.settings) {
            Toast.makeText(this, "Setting is pressed", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.logout) {
            clearSession();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return true;
    }


    private void clearSession() {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();


        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    private void displayDemo() {
        Cursor cursor = databaseHelper.getAllDemo();
        if (cursor != null && cursor.getCount() > 0) {
            adapter = new UserDemoAdapter(this, cursor, 0);
            listviewDemo.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No room types available.", Toast.LENGTH_SHORT).show();
        }
    }
}

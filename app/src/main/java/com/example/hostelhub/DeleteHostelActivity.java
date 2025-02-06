package com.example.hostelhub;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DeleteHostelActivity extends AppCompatActivity {

    private EditText editTextRoomTypes;
    private EditText textViewRoomPrice;
    private TextView textViewRoomId;
    private ImageView imageViewRoom;
    private Button buttonDelete;
    private Button buttonSearch;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_hostel);


        editTextRoomTypes = findViewById(R.id.text_view_product_name);
        textViewRoomPrice = findViewById(R.id.text_view_product_price);
        textViewRoomId = findViewById(R.id.text_view_product_id);
        imageViewRoom = findViewById(R.id.image_view_Room);
        buttonDelete = findViewById(R.id.button_delete);
        buttonSearch = findViewById(R.id.button_search);


        databaseHelper = new DatabaseHelper(this);


        buttonSearch.setOnClickListener(view -> searchRoom());
        buttonDelete.setOnClickListener(view -> deleteRoom());
    }

    private void searchRoom() {
        String roomName = editTextRoomTypes.getText().toString().trim();

        if (roomName.isEmpty()) {
            Toast.makeText(this, "Please enter a room name to search", Toast.LENGTH_SHORT).show();
            return;
        }

        Cursor cursor = databaseHelper.getRoomByName(roomName);

        if (cursor != null && cursor.moveToFirst()) {
            int roomId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_Room_ID));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ROOM_PRICE));
            byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ROOM_IMAGE_URI));


            textViewRoomPrice.setText(String.format("Price: %.2f", price));
            textViewRoomId.setText("Room ID: " + roomId);

            if (image != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                imageViewRoom.setImageBitmap(bitmap);
            }

            cursor.close();
        } else {
            Toast.makeText(this, "Room not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteRoom() {
        String roomName = editTextRoomTypes.getText().toString().trim();

        if (roomName.isEmpty()) {
            Toast.makeText(this, "Please enter a room name to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean rowsAffected = databaseHelper.deleteRoom(roomName);

        if (rowsAffected) {
            Toast.makeText(this, "Room deleted successfully", Toast.LENGTH_SHORT).show();
//            textViewRoomPrice.setText("");
//            textViewRoomId.setText("");
//            imageViewRoom.setImageBitmap(null);
        } else {
            Toast.makeText(this, "Room not found or failed to delete", Toast.LENGTH_SHORT).show();
        }
    }
}

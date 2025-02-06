package com.example.hostelhub;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdateHostelActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText editTextRoomTypes;
    private EditText editTextRoomPrice;
    private TextView textViewRoomId;
    private ImageView imageViewRoom;
    private Button buttonSearch;
    private Button buttonSelectImage;
    private Button buttonUpdate;

    private DatabaseHelper databaseHelper;
    private byte[] roomImageByteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hostel);


        editTextRoomTypes = findViewById(R.id.edit_text_Room_name);
        editTextRoomPrice = findViewById(R.id.edit_text_Room_price);
        textViewRoomId = findViewById(R.id.text_view_Room_id);
        imageViewRoom = findViewById(R.id.image_view_Room);
        buttonSearch = findViewById(R.id.button_search);
        buttonSelectImage = findViewById(R.id.button_select_image);
        buttonUpdate = findViewById(R.id.button_update);


        databaseHelper = new DatabaseHelper(this);


        buttonSearch.setOnClickListener(view -> searchRoom());
        buttonSelectImage.setOnClickListener(view -> selectImage());
        buttonUpdate.setOnClickListener(view -> updateRoom());
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


            editTextRoomPrice.setText(String.format("%.2f", price));
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

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewRoom.setImageBitmap(bitmap);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
                roomImageByteArray = byteArrayOutputStream.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateRoom() {
        String roomName =editTextRoomTypes.getText().toString().trim();
        String roomPrice = editTextRoomPrice.getText().toString().trim();


        if (roomName.isEmpty() || roomPrice.isEmpty() ) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(roomPrice);


        String roomIdText = textViewRoomId.getText().toString();
        int productId = Integer.parseInt(roomIdText.replaceAll("\\D+", ""));

        boolean isUpdated = databaseHelper.updateRoom(productId, roomName, price,roomImageByteArray);

        if (isUpdated) {
            Toast.makeText(this, "Room successfully updated ", Toast.LENGTH_SHORT).show();

        }
    }
}
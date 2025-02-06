package com.example.hostelhub;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cursoradapter.widget.CursorAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserDemoAdapter extends CursorAdapter {

    public UserDemoAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_demo, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {



        int roomId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String type = cursor.getString(cursor.getColumnIndexOrThrow("RoomTypes"));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("RoomPrice"));
        byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("RoomImageUri"));

        TextView roomTypeText = view.findViewById(R.id.text_view_Room_Types);
        TextView roomPriceText = view.findViewById(R.id.text_view_Room_Price);
        TextView booked = view.findViewById(R.id.text_booked);
        roomTypeText.setText(type);
        roomPriceText.setText("Price: Tk " + price);


        ImageView roomImageView = view.findViewById(R.id.image_view_Demo);
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            roomImageView.setImageBitmap(bitmap);
        }

        Button btnBooking = view.findViewById(R.id.btn_booking);
        btnBooking.setVisibility(View.VISIBLE);

        btnBooking.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(context);

            boolean success = dbHelper.bookRoom(roomId, type, price, imageBytes);
            if (success) {
                Toast.makeText(context, "Room " + roomId + " is booked successfully!", Toast.LENGTH_SHORT).show();
                btnBooking.setVisibility(View.GONE);
                booked.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(context, "You have already booked a room or the room is unavailable!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

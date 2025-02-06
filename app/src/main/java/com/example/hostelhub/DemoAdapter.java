package com.example.hostelhub;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;



public class DemoAdapter extends CursorAdapter {
    public DemoAdapter(Context context, Cursor cursor, int flags) {
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

        if (imageBytes != null) {

            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

            ImageView imageView = view.findViewById(R.id.image_view_Demo);
            imageView.setImageBitmap(bitmap);
        }


        TextView roomTypeText = view.findViewById(R.id.text_view_Room_Types);
        roomTypeText.setText(type);

        TextView roomPriceText = view.findViewById(R.id.text_view_Room_Price);
        roomPriceText.setText(String.valueOf(price));


    }
}





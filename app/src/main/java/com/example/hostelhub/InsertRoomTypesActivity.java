package com.example.hostelhub;



import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class InsertRoomTypesActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_PICK=1;
    private EditText RoomTypesEditText;
    private EditText RoomPriceEditText;
    private ImageView selectedImageView;
    private Button SelectImageButton;
    private Button InsertDemoButton;
    private  DatabaseHelper databaseHelper;
    private byte[] imageByteArray;

    private ActivityResultLauncher<Intent> imagePickerLauncher;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_insert_room_types);

        RoomTypesEditText=findViewById(R.id.et_Room_Types);
        RoomPriceEditText=findViewById(R.id.et_Room_Price);
        selectedImageView=findViewById(R.id.iv_selected_image);
        SelectImageButton=findViewById(R.id.btn_select_image);
        InsertDemoButton=findViewById(R.id.btn_insert_demo);

        databaseHelper=new DatabaseHelper(this);
        imagePickerLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),result ->{
            if(result.getResultCode()== Activity.RESULT_OK && result.getData() !=null){
                Uri imageUri= result.getData().getData();
                try {
                    Bitmap imageBitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    selectedImageView.setImageBitmap(imageBitmap);
                    imageByteArray= bitmapToByteArray(imageBitmap);



                }
                catch (IOException e){
                    System.out.print(e);
                }

            }

        });
        SelectImageButton.setOnClickListener(view -> ShowImageSelectionDialog());
        InsertDemoButton.setOnClickListener(view -> InsertDemo());

    }
    private void ShowImageSelectionDialog(){
        Intent pickIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");
        imagePickerLauncher.launch(pickIntent);


    }
    private byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();


    }
    private void InsertDemo(){
        String RoomTypes=RoomTypesEditText.getText().toString();
        double Price=Double.parseDouble(RoomPriceEditText.getText().toString());

        if (RoomTypes.isEmpty() || imageByteArray == null) {
            Toast.makeText(InsertRoomTypesActivity.this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        try{
            databaseHelper.insertDemo(RoomTypes,Price,imageByteArray);
            Toast.makeText(InsertRoomTypesActivity.this, "Demo inserted successfully", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(InsertRoomTypesActivity.this,ViewRoomTypes.class);
            startActivity(intent);


        } catch (Exception e) {
            Toast.makeText(this, "Not inserted", Toast.LENGTH_SHORT).show();

        }


    }



}
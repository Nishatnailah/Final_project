package com.example.hostelhub;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "TEST_DB";

    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_REGISTER = "register";

    public static final String TABLE_ROOM = "Room";

    public static final String COL_ID = "id";

    public static final String COL_USERNAME = "username";

    public static final String COL_EMAIL = "email";

    public static final String COL_PASSWORD = "password";

    public static final String COL_ROOM_TYPES = "RoomTypes";

    public static final String COL_ROOM_PRICE = "RoomPrice";

    public static final String COL_ROOM_IMAGE_URI = "RoomImageUri";
    public static final String COL_Room_ID = "room_id";

    public static final String COL_Booking_ID = "booking_id";
    public static final String COL_Booking_ROOM_TYPES = "booking_roomtypes";
    public static final String COL_Booking_PRICE = "booking_price";
    public static final String COL_Booking_Room_Num = "booking_roomnum";
    public static final String COL_Booking_ROOM_IMAGE_URI = "booking_image";
    public static final String TABLE_ROOM_Booking = "room_booking";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOM);


        onCreate(db);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_REGISTER + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_ROOM + " (" +
                COL_Room_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ROOM_TYPES + " TEXT, " +
                COL_ROOM_PRICE + " REAL, " +
                COL_ROOM_IMAGE_URI + " BLOB)");


        db.execSQL("CREATE TABLE " + TABLE_ROOM_Booking + " (" +
                COL_Booking_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_Booking_ROOM_TYPES + " TEXT, " +
                COL_Booking_PRICE + " REAL, " +
                COL_Booking_Room_Num + " REAL, " +
                COL_Booking_ROOM_IMAGE_URI + " BLOB)");


    }


    public boolean insertUser(String username, String email, String password, String phone) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);
        long result = db.insert(TABLE_REGISTER, null, contentValues);

        return result != -1;
    }

    public boolean checkUserByUsername(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_REGISTER + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASSWORD + " = ?", new String[]{username, password});
        boolean exits = cursor.getCount() > 0;
        cursor.close();


        return exits;
    }

    public void insertDemo(String roomTypes, double price, byte[] imageByteArray) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ROOM_TYPES, roomTypes);
        contentValues.put(COL_ROOM_PRICE, price);
        contentValues.put(COL_ROOM_IMAGE_URI, imageByteArray);
        db.insert(TABLE_ROOM, null, contentValues);
        db.close();
    }


    public Cursor getAllDemo() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT room_id AS _id, RoomTypes, RoomPrice, RoomImageUri FROM " + TABLE_ROOM, null);
    }



    public Cursor getRoomByName(String roomName) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ROOM + " WHERE " + COL_ROOM_TYPES + " = ?", new String[]{roomName});

    }

    public boolean updateRoom(int roomId, String roomName, double price, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_ROOM_TYPES, roomName);
        contentValues.put(COL_ROOM_PRICE, price);
        contentValues.put(COL_ROOM_IMAGE_URI, image);

        int rowsAffected = db.update(TABLE_ROOM, contentValues, COL_Room_ID + "=?", new String[]{String.valueOf(roomId)});
        db.close();

        return rowsAffected > 0;
    }

    public boolean deleteRoom(String roomName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ROOM, COL_ROOM_TYPES + " = ?", new String[]{roomName});
        db.close();
        return result > 0;
    }

    public boolean bookRoom(int roomId, String roomType, double roomPrice, byte[] roomImage) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;

        try {

            String query = "SELECT * FROM " + TABLE_ROOM_Booking + " WHERE " + COL_Booking_Room_Num + " = ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(roomId)});

            if (cursor != null && cursor.moveToFirst()) {
                return false;
            }


            ContentValues bookingValues = new ContentValues();
            bookingValues.put(COL_Booking_Room_Num, roomId);
            bookingValues.put(COL_Booking_ROOM_TYPES, roomType);
            bookingValues.put(COL_Booking_PRICE, roomPrice);
            bookingValues.put(COL_Booking_ROOM_IMAGE_URI, roomImage);

            long result = db.insert(TABLE_ROOM_Booking, null, bookingValues);
            return result != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }

    public Cursor getFilteredProducts(String query) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT room_id AS _id, RoomTypes, RoomPrice, RoomImageUri FROM " + TABLE_ROOM + " WHERE RoomTypes LIKE ?";
        String[] args = new String[]{"%" + query + "%"};
        return db.rawQuery(sql, args);
    }
}
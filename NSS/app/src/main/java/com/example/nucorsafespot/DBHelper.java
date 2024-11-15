package com.example.nucorsafespot;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;


/**
 * {@author Christian}
 *
 * Implementation of this class in activity DatabaseHelper dbHelper = new DBHelper(this);
 * SQLiteDatabase db = DBHelper.getReadableDB();
 *
 */
public class DBHelper extends SQLiteOpenHelper {
    private static String DATABASE_PATH;
    private static String DATABASE_NAME = "PPEDB.db";
    private static final int DATABASE_VER = 1;
    private final Context context;


    // 4 tables total location, equipment, equipment_type, and equipment_location
    // column/table names for location table
    public static final String LOCATION_TABLE = "location";
    public static final String COLUMN_LOCATION_ID = "location_id";
    public static final String COLUMN_LOCATION_NAME = "location_name";
    public static final String COLUMN_LOCATION_DESCRIPTION = "location_description";

    //column/table names for equipment
    public static final String EQUIPMENT_TABLE = "equipment";
    public static final String COLUMN_ITEM_ID = "item_id";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_ITEM_DESCRIPTION = "item_description";
    public static final String COLUMN_ITEM_TYPE = "item_type_id";

    // column/table names for equipment type
    public static final String TYPE_TABLE = "equipment_type";
    public static final String COLUMN_EQUIPMENT_TYPE_ID = "item_type_id";
    public static final String COLUMN_EQUIPMENT_TYPE_NAME = "type_name";

    // column/table names for equipment_location (relational table)
    public static final String EQLOCATION_TABLE = "equipment_location";
    public static final String COLUMN_EQLOCATION_ID = "equipment_id";
    public static final String COLUMN_EQLOCATION_LOCATION_ID = "location_id";

    @Override
    public void onCreate(SQLiteDatabase db) {
        //not needed since the DB is pre-populated.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void initializeDB() throws IOException {
        if (!checkDBExists()) {
            Log.d("DBHelper", "Database does not exist. Copying from assets.");

            // Create an empty database to set up the path
            this.getReadableDB().close();

            // Now copy the database from assets
            copyDBFromAssets();
        } else {
            Log.d("DBHelper", "Database already exists. No need to copy.");
        }
    }

    public DBHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VER);
        this.context = context;
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
        Log.d("DBHelper", "Database path: " + DATABASE_PATH);
    }

    private boolean checkDBExists() {
        File dbFile = new File(DATABASE_PATH);
        return dbFile.exists();
    }

    /**
     * Copies Database contents from the database file in assets
     */
    private void copyDBFromAssets() {
        try (InputStream input = context.getAssets().open(DATABASE_NAME);
             OutputStream output = new FileOutputStream(DATABASE_PATH)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            Log.d("DBHelper", "Database copied successfully from assets to " + DATABASE_PATH);
        } catch (IOException e) {
            Log.e("DBHelper", "Error copying database from assets: " + e.getMessage());
        }
    }


    // only need a readable db method since we will not be writing to the DB
    /**
     * Retrieves a readable database instance, initializing the database if necessary.
     *
     * @return an instance of {@link SQLiteDatabase} that can be read
     *
     */
    public synchronized SQLiteDatabase getReadableDB(){
        return super.getReadableDatabase();
    }

    public List<Equipment> getEquipmentByLocation(String locationName) throws IOException{
        List<Equipment> returnList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDB();
        String query = "SELECT e." + COLUMN_ITEM_NAME + ", e." + COLUMN_ITEM_DESCRIPTION + ", et." + COLUMN_EQUIPMENT_TYPE_NAME +
                " FROM " + EQUIPMENT_TABLE + " e " +
                "JOIN " + EQLOCATION_TABLE + " el ON e." + COLUMN_ITEM_ID + " = el." + COLUMN_EQLOCATION_ID +
                " JOIN " + LOCATION_TABLE + " l ON el." + COLUMN_EQLOCATION_LOCATION_ID + " = l." + COLUMN_LOCATION_ID +
                " JOIN " + TYPE_TABLE + " et ON e." + COLUMN_ITEM_TYPE + " = et." + COLUMN_EQUIPMENT_TYPE_ID +
                " WHERE l." + COLUMN_LOCATION_NAME + " = '" + locationName + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            //if there are results then we loop through to create a Equipment Object for each row
            do {
                String itemName = cursor.getString(0);
                String itemDescription = cursor.getString(1);
                String typeName = cursor.getString(2);

                Equipment equipment = new Equipment(itemName,itemDescription,typeName);
                returnList.add(equipment);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return returnList;
    }


    /**
     * Initial call to create a list of Locations
     * @return an instance of {@link List} that contains all {@link Location} objects found in the location table
     *
     * @throws IOException if the database copy from assets fails
     *
     * Adding logs because there are errors -Gage
     */
    public List<Location> getLocations() throws IOException {
        List<Location> returnList = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDB();
            String query = "SELECT * FROM " + LOCATION_TABLE;
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(1);
                    String description = cursor.getString(2);
                    Location location = new Location(name, description);
                    returnList.add(location);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e("DBHelper", "Error accessing locations: " + e.getMessage());
        } finally {
            if (db != null) db.close();
        }
        return returnList;
    }
}


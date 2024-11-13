package com.example.nucorsafespot;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


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

    public void initializeDB() throws IOException{
        if(!checkDBExists()){
            this.getReadableDB();
            this.close();
            copyDBFromAssets();
        }
    }

    public DBHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VER);
        this.context = context;
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
    }

    private boolean checkDBExists() {
        File dbFile = new File(DATABASE_PATH);
        return dbFile.exists();
    }

    /**
     * Copies Database contents from the database file in assets
     * @throws IOException
     */
    private void copyDBFromAssets() throws IOException{
        try (InputStream input = context.getAssets().open(DATABASE_NAME);
             OutputStream output = new FileOutputStream(DATABASE_PATH)){

            byte[] buffer = new byte[1024];
            int length;

            while((length = input.read(buffer)) > 0){
                output.write(buffer, 0, length);
            }
            output.flush();
        }
    }


    // only need a readable db method since we will not be writing to the DB
    /**
     * Retrieves a readable database instance, initializing the database if necessary.
     *
     * @return an instance of {@link SQLiteDatabase} that can be read
     * @throws IOException if the database copy from assets fails
     */
    public synchronized SQLiteDatabase getReadableDB() throws IOException{
        try {
            initializeDB();
        } catch (IOException e){
            throw new RuntimeException("Error initializing database", e);
        }
        return super.getWritableDatabase();
    }
}


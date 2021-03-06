package com.pico.picoinvoices;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter 
{

    public static final String DATABASE_NAME = "picoinvoices"; //$NON-NLS-1$

    public static final int DATABASE_VERSION = 15;

    static final String CREATE_TABLE_INVOICE = "create table "

            + InvoiceAdapter.DATABASE_TABLE
            + " ("
            + InvoiceAdapter.KEY_ROWID
            + " integer primary key autoincrement, "

            /*
             * CHANGE 2:
             */
            // TODO: Place your fields here!
            // + KEY_{...} + " {type} not null"
            // - Key is the column name you created above.
            // - {type} is one of: text, integer, real, blob
            // (http://www.sqlite.org/datatype3.html)
            // - "not null" means it is a required field (must be given a
            // value).
            // NOTE: All must be comma separated (end of line!) Last one must
            // have NO comma!!
            + InvoiceAdapter.KEY_ISSUEDATE + " string not null, "
            + InvoiceAdapter.KEY_CUSTOMER + " integer not null, "
            + InvoiceAdapter.KEY_DUEDATE + " string not null, "
            + InvoiceAdapter.KEY_PRICESERVICE + " string not null, "
            + InvoiceAdapter.KEY_SERVICE + " string not null, "
            + InvoiceAdapter.KEY_AMOUNTDUE + " string not null, "
            + InvoiceAdapter.KEY_STATUS + " string not null "

            + ");";

     static final String CREATE_TABLE_CLIENT = "create table "

            + ClientAdapter.DATABASE_TABLE
            + " ("
            + ClientAdapter.KEY_ROWID
            + " integer primary key autoincrement, "

            /*
             * CHANGE 2:
             */
            // TODO: Place your fields here!
            // + KEY_{...} + " {type} not null"
            // - Key is the column name you created above.
            // - {type} is one of: text, integer, real, blob
            // (http://www.sqlite.org/datatype3.html)
            // - "not null" means it is a required field (must be given a
            // value).
            // NOTE: All must be comma separated (end of line!) Last one must
            // have NO comma!!
            + ClientAdapter.KEY_FNAME + " string not null, "
            + ClientAdapter.KEY_LNAME + " string not null, "
            + ClientAdapter.KEY_ADDRESS + " string not null, "
            + ClientAdapter.KEY_PHONE + " string not null, "
            + ClientAdapter.KEY_EMAIL + " string not null, "
            + ClientAdapter.KEY_BUSINESS + " string not null "

            // Rest of creation:
            + ");";
     
     static final String CREATE_TABLE_SERVICES = "create table "

            + RegisterServicesAdapter.DATABASE_TABLE
            + " ("
            + RegisterServicesAdapter.KEY_ROWID
            + " integer primary key autoincrement, "

            /*
             * CHANGE 2:
             */
            // TODO: Place your fields here!
            // + KEY_{...} + " {type} not null"
            // - Key is the column name you created above.
            // - {type} is one of: text, integer, real, blob
            // (http://www.sqlite.org/datatype3.html)
            // - "not null" means it is a required field (must be given a
            // value).
            // NOTE: All must be comma separated (end of line!) Last one must
            // have NO comma!!
            + RegisterServicesAdapter.KEY_NAME + " string not null, "
            + RegisterServicesAdapter.KEY_RATE + " string not null, "
            + RegisterServicesAdapter.KEY_TYPE + " string "

            // Rest of creation:
            + ");";
    private final Context context;
    private DatabaseHelper DBHelper;
    public SQLiteDatabase db;

    /**
     * Constructor
     * 
     * @param ctx
     */
    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        this.DBHelper = new DatabaseHelper(this.context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.execSQL(CREATE_TABLE_INVOICE);
            db.execSQL(CREATE_TABLE_CLIENT);
            db.execSQL(CREATE_TABLE_SERVICES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            System.out.printf("Upgrading db...");
         
            
            // Destroy old database:
            db.execSQL("DROP TABLE IF EXISTS " + ClientAdapter.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + InvoiceAdapter.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + RegisterServicesAdapter.DATABASE_TABLE);
            
            // Recreate new database:
            onCreate(db);
            // Adding any table mods to this guy here
        }
    }

    /**
     * open the db
     * 
     * @return this
     * @throws SQLException
     *             return type: DBAdapter
     */
    public DBAdapter open() throws SQLException
    {
        this.db = this.DBHelper.getWritableDatabase();
        return this;
    }

    /**
     * close the db return type: void
     */
    public void close()
    {
        this.DBHelper.close();
    }
    
    public SQLiteDatabase getDB()
    {
        return this.db;
    }
    
    public void reset()
    {
     // Destroy old database:
        db.execSQL("DROP TABLE IF EXISTS " + ClientAdapter.DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + InvoiceAdapter.DATABASE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + RegisterServicesAdapter.DATABASE_TABLE);
        
        // Recreate new database:
        db.execSQL(CREATE_TABLE_INVOICE);
        db.execSQL(CREATE_TABLE_CLIENT);
        db.execSQL(CREATE_TABLE_SERVICES);
    }
}
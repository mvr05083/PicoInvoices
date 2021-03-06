// ------------------------------------ DBADapter.java ---------------------------------------------

// TODO: Change the package to match your project.
package com.pico.picoinvoices;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

// TO USE:
// Change the package (at top) to match your project.
// Search for "TODO", and make the appropriate changes.


public class InvoiceAdapter
{

    // ///////////////////////////////////////////////////////////////////
    // Constants & Data
    // ///////////////////////////////////////////////////////////////////

    // DB Fields
    public static final String KEY_ROWID = "_id";
    public static final int COL_ROWID = 0;
    /*
     * CHANGE 1:
     */
    // TODO: Setup your fields here:
    public static final String KEY_ISSUEDATE = "issuedate";
    public static final String KEY_CUSTOMER = "customer";
    public static final String KEY_SERVICE = "service";
    public static final String KEY_DUEDATE = "duedate";
    public static final String KEY_PRICESERVICE = "priceservice";
    public static final String KEY_AMOUNTDUE = "amountdue";
    public static final String KEY_STATUS = "status";

    // TODO: Setup your field numbers here (0 = KEY_ROWID, 1=...)
    public static final int COL_ISSUEDATE = 1;
    public static final int COL_CUSTOMER = 2;
    public static final int COL_DUEDATE = 3;
    public static final int COL_PRICESERVICE = 4;
    public static final int COL_SERVICE = 5;
    public static final int COL_AMOUNTDUE = 6;
    public static final int COL_STATUS = 7;


    public static final String[] ALL_KEYS = new String[] { KEY_ROWID,
            KEY_ISSUEDATE, KEY_CUSTOMER, KEY_DUEDATE,
            KEY_PRICESERVICE, KEY_SERVICE, KEY_AMOUNTDUE, KEY_STATUS };

    // DB info: it's name, and the table we are using (just one).
    public static final String DATABASE_TABLE = "invoices";

    // Context of application who uses us.
    private final Context context;

    private DatabaseHelper myDBHelper;
    private static SQLiteDatabase db;

    // ///////////////////////////////////////////////////////////////////
    // Public methods:
    // ///////////////////////////////////////////////////////////////////

    public InvoiceAdapter(Context ctx)
    {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public InvoiceAdapter open()
    {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close()
    {
        myDBHelper.close();
    }

    // Add a new set of values to the database.
    public long insertRow(String issuedate, String customer,String duedate, String priceservice, String service, String amountdue, String status)
    {
        /*
         * CHANGE 3:
         */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ISSUEDATE, issuedate);
        initialValues.put(KEY_CUSTOMER, customer);
        initialValues.put(KEY_DUEDATE, duedate);
        initialValues.put(KEY_PRICESERVICE, priceservice);
        initialValues.put(KEY_SERVICE, service);
        initialValues.put(KEY_AMOUNTDUE, amountdue);
        initialValues.put(KEY_STATUS, status);
        
        System.out.println("Inserting...");
        // Insert it into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId)
    {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll()
    {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst())
        {
            do
            {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }

    // Return all data in the database.
    public Cursor getAllRows()
    {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null,
                null, null, null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }
    
    // Return all data from any database.
    public Cursor getAllRows(String db_name, String[] db_keys)
    {
        String where = null;
        Cursor c = db.query(true, db_name, db_keys, where, null, null,
                null, null, null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId)
    {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null,
                null, null, null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }
 // Get a specific row (by rowId)
    public Cursor getCustRow(long rowId)
    {
        String where = KEY_CUSTOMER + "=" + rowId;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null,
                null, null, null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }
    // Return the list of invoices associated with a specific customer
    public Cursor getCustomerInvoice(long rowId)
    {
        String where = KEY_CUSTOMER + "=" + rowId;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null,
                null, null, null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }
    
    public Cursor query(String[] q, String table)
    {
        Cursor c = db.rawQuery("SELECT * FROM "+table+" WHERE _id = ?", q);
        if (c!=null)
        {
            c.moveToFirst();
        }
        return c;
    }
    public Cursor querySort(String[] q, String order, String table)
    {
        Cursor c = db.rawQuery("SELECT * FROM " + table +" ORDER BY ? ASC", q);
        if (c!=null)
        {
            c.moveToFirst();
        }
        return c;
    }
    public Cursor querySort2(String[] q, String table)
    {
        Cursor c = db.query(DATABASE_TABLE, ALL_KEYS, null, null, null, null, q[0] +" DESC");
        if (c!=null)
        {
            c.moveToFirst();
        }
        return c;
    }
   

//    public ArrayList<?> getCustomerContact(long customer)
//    {
//        Cursor mCursor = db.rawQuery("SELECT * FROM Table1, Table2 "
//                + "WHERE Table1.id = Table2.id_table1 "
//                + "GROUP BY Table1.data1", null);
//
//        return null;
//    }

    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, String issuedate, String customer,
            String duedate, String priceservice,String amountdue, String status)
    {

        String where = KEY_ROWID + "=" + rowId;

        /*
         * CHANGE 4:
         */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_ISSUEDATE, issuedate);
        newValues.put(KEY_CUSTOMER, customer);
        newValues.put(KEY_DUEDATE, duedate);
        newValues.put(KEY_PRICESERVICE, priceservice);
        newValues.put(KEY_AMOUNTDUE, amountdue);
        newValues.put(KEY_STATUS, status);

        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }
    public boolean updateStatus(long rowId, String status)
    {

        String where = KEY_ROWID + "=" + rowId;

        /*
         * CHANGE 4:
         */
        // TODO: Update data in the row with new fields.
        // TODO: Also change the function's arguments to be what you need!
        // Create row's data:
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_STATUS, status);

        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }
    // ///////////////////////////////////////////////////////////////////
    // Private Helper Classes:
    // ///////////////////////////////////////////////////////////////////

    /**
     * Private class which handles database creation and upgrading. Used to
     * handle low-level database access.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DBAdapter.DATABASE_NAME, null,
                    DBAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db)
        {
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion)
        {
        }
    }
}

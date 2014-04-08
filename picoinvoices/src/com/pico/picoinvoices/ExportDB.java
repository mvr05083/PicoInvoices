package com.pico.picoinvoices;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

public class ExportDB extends Activity
{

    DBAdapter _myDb = null;
    SPAdapter _sp = null;
    SQLiteDatabase db = null;

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Activity Lifecycle functions
    // ///*
    // //////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exportdb);
        initialize();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        closeDB();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        initialize();
    }

    private void initialize()
    {
        _sp = new SPAdapter(getApplicationContext());
        _sp.saveClientID("0");
        _sp.saveInvioceID("0");
        openDB();
        // closeDB();
    }

    // @Override
    // public boolean onCreateOptionsMenu(Menu menu)
    // {
    // // Inflate the menu; this adds items to the action bar if it is present.
    // getMenuInflater().inflate(R.menu.home, menu);
    // return true;
    // }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Database functions
    // ///*
    // //////////////////////////////////////////////////////
    private void closeDB()
    {
        _myDb.close();
    }

    private void openDB()
    {
        _myDb = new DBAdapter(this);
        _myDb.open();

    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* OnClick listener for starting new activities
    // ///*
    // //////////////////////////////////////////////////////
    public void onClick_ToExportXML(View v)
    {
        confirmation(v);
        
        
    }
    
    public void exportToXML(View v)
    {
        String path = Environment.getExternalStorageDirectory() + "/Android/data/com.pico.picoinvoices/";
        createDirIfNotExists(path);

        XmlExporter xmlExporter = null;
        this.db = _myDb.getDB();
        xmlExporter = new XmlExporter(this.db);

        try
        {
            xmlExporter.export("picoinvoices", "picodatabase");
        } catch (IOException e)
        {
            // TODO Auto-generated catch block; add real handling
            e.printStackTrace();
        }
    }

    public void onClick_ToExportCSV(View v)
    {
        // Intent intent = new Intent(this, ExportCSV.class);
        // startActivity(intent);
    }

    public static boolean createDirIfNotExists(String path)
    {
        boolean exists = true;

        File file = new File(path);

        if (!file.exists())
        {
            System.out.println("directory does not exist; creating directory");
            if (!file.mkdirs())
            {
                System.out.println("Problem creating data storage folder");
                exists = false;
            }
        } else
        {
            System.out.println("directory exists");
        }
        return exists;
    }

    public void confirmation(final View v)
    {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirm export")
                .setMessage("Are you sure you want to export the database to XML?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        exportToXML(v);
                    }

                }).setNegativeButton("No", null).show();
    }

}

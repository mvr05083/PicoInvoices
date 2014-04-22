package com.pico.picoinvoices;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class Reports extends Activity
{

    private SPAdapter _sp = null;
    private InvoiceAdapter _myDb = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        initialize();
    }
    private void initialize()
    {
        _sp = new SPAdapter(this);
        _myDb = new InvoiceAdapter(this);
        refresh();
        
    }
    ////////////////////////////////////////////////////////
    /////*
    /////*  Database access functions
    /////*
    ////////////////////////////////////////////////////////
    private void closeDB() 
    {
        _myDb.close();
    }
    private void openDB() 
    {
        _myDb.open();
    }

    ////////////////////////////////////////////////////////
    /////*
    /////*  Refresh functions
    /////*
    ////////////////////////////////////////////////////////
    private void refresh()
    {
        //Open database
        openDB();
        
        //Create the list of items on in the listview
        populateListView();
        
        //Set the functionality for selecting a given element in the list
        //registerClickCallback();
        
        //Close database
        closeDB();
    }
    
    private void populateListView() 
    {
        //Open database
        openDB();
        //Create the list of items
        Cursor cursor = _myDb.getCustRow(_sp.getClientID());                                 
        
        System.out.println(cursor.getString(InvoiceAdapter.COL_SERVICE));
        //Create the arrays for mapping DB columns to elements in the listview
        String[] client_name_list = new String[]{InvoiceAdapter.KEY_ROWID, InvoiceAdapter.KEY_SERVICE, InvoiceAdapter.KEY_AMOUNTDUE};
        int[] ints = new int[] {R.id.report_serviceName, R.id.report_instances, R.id.report_avgCost};
        
        //Set the custom listviewadapter on the data and view
        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.reports, cursor,client_name_list , ints, 0);
        ListView list = (ListView) findViewById(R.id.reports_listView);
        list.setAdapter(adapter);
        
        //Close database
        closeDB();
        
    }
    
    private void registerClickCallback() 
    {
        //Find listview that was created from populateListView
        ListView list = (ListView) findViewById(R.id.client_listView);
        
        //Set the action for an element selection
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) 
            {
                
            }
        });
    }

}
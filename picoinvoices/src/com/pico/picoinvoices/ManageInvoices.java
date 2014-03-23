package com.pico.picoinvoices;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class ManageInvoices extends Activity
{

    private Spinner spinner2;
    private InvoiceAdapter myDb;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_invoices);
        addItemsOnSpinner();
        addListenerOnSpinnerItemSelection();
        refresh();
    }

    protected void onDestroy()
    {
        super.onDestroy();
        closeDB();
    }
    protected void onResume()
    {
        super.onResume();
        openDB();
    }
    
    
    
    ////////////////////////////////////////////////////////
    /////*
    //// *  Database maintenance functions
    //// */
    ////////////////////////////////////////////////////////
    private void closeDB() 
    {
        myDb.close();
    }
    private void openDB() 
    {
        myDb = new InvoiceAdapter(this);
        myDb.open();
    }
    
    
    
    ////////////////////////////////////////////////////////
    //////*
    ///// *  refresh functions
    ///// */
    ///////////////////////////////////////////////////////
    private void refresh()
    {
        openDB();
        populateListView();
        registerClickCallback();
        closeDB();
    }
    
    private void populateListView() 
    {
        openDB();
        
        //Create the list of items
        Cursor cursor = myDb.querySort2(new String[] {SpinnerAdapter.sort}, InvoiceAdapter.DATABASE_TABLE);                              

        //  String array to use as a map for which db rows should be mapped to which element in the template layout
        //  client_name_list corresponds to the database columns that should be mapped to the corresponding xml element as specified in ints. NOTE: this mappings are 
        //      done on directly (ie: InvoiceAdapter.KEY_ROWID maps to R.id.invoice_listview_layout_template_txtInvoiceNumber) 
        String[] client_name_list = new String[]{InvoiceAdapter.KEY_ROWID, InvoiceAdapter.KEY_ISSUEDATE, InvoiceAdapter.KEY_STATUS, InvoiceAdapter.KEY_CUSTOMER};
        int[] ints = new int[] {R.id.invoice_listview_layout_template_txtInvoiceNumber, R.id.invoice_listview_layout_template_txtDate, R.id.invoice_listview_layout_template_txtStatus, R.id.invoice_listview_layout_template_CustomerID};
    
        //  Create the adapter that will bind the data from the DB to the listview
        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.invoice_listview_layout_template, cursor,client_name_list , ints, 0);
        
        ListView list = (ListView) findViewById(R.id.manageInvoices_listView);
        list.setAdapter(adapter);
        closeDB();
    }
    
    private void registerClickCallback() 
    {
        ListView list = (ListView) findViewById(R.id.manageInvoices_listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) 
            {
                    //  Note that the viewClicked needs to be specified here so that the selected item in the list will be used - not just the first in the lists
                    //  Instead of passing the client's id number from activity to activity, the clientID will be coupled with the invoice directly
                    TextView textView = (TextView) viewClicked.findViewById(R.id.invoice_listview_layout_template_CustomerID);
                    String customerID = textView.getText().toString();
                    
                    //  Now use the intent to send the information to the final activity - ShowDetailedInvoice)
                    Intent intent1 = new Intent(ManageInvoices.this, ShowDetailedInvoice.class);
                    intent1.putExtra("InvoiceID", Long.toString(idInDB));
                    intent1.putExtra("CustomerID", customerID);
                    
                    System.out.println(customerID);
                    startActivity(intent1);
            }
        });
    }
    
    
    ////////////////////////////////////////////////////////
    /////*
    //// * add items into spinner dynamically
    //// *
    ////////////////////////////////////////////////////////
    public void addItemsOnSpinner()
    {

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        List<String> list = new ArrayList<String>();
        list.add(InvoiceAdapter.KEY_CUSTOMER);
        list.add(InvoiceAdapter.KEY_ISSUEDATE);
        list.add(InvoiceAdapter.KEY_STATUS);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection()
    {
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new SpinnerAdapter());
    }
    
    ////////////////////////////////////////////////////////
    /////*
    //// * Use an inner class so that I can access the Invoice Adapter.
    //// *
    ////////////////////////////////////////////////////////
    public void onClick_SortInvoices(View v)
    {
        refresh();
    }
    

}
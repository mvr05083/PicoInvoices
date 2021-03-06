package com.pico.picoinvoices;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class RegisterServices extends Activity
{

    private RegisterServicesAdapter myDb = null;
    String customerID = "";
    
    ////////////////////////////////////////////////////////
    /////*
    /////*  Activity lifecycle functions
    /////*
    ////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_services);
        refresh();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        refresh();
    }
    @Override
    protected void onResume() 
    {
        super.onResume();
        refresh();
    }
    @Override
    protected void onDestroy() 
    {
        super.onDestroy();
        closeDB();
    }
    @Override
    protected void onStop() 
    {
        super.onStop();
        closeDB();
    }
    @Override
    protected void onPause() 
    {
        super.onPause();
        closeDB();
    }
    ////////////////////////////////////////////////////////
    /////*
    /////*  Action bar functions
    /////*
    ////////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.register_services, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.goto_Home:
                Intent home = new Intent(this, Home.class);
                startActivity(home);
                return true;
            case R.id.goto_Clients:
                Intent clients = new Intent(this, ClientList.class);
                startActivity(clients);
                return true;
            case R.id.goto_ManageInvoices:
                Intent manage = new Intent(this, ManageInvoices.class);
                startActivity(manage);
                return true;
            case R.id.goto_Services:
                Intent services = new Intent(this, RegisterServices.class);
                startActivity(services);
                return true;
            case R.id.goto_Settings:
                Intent settings = new Intent(this, Settings.class);
                startActivity(settings);
                return true;
            default:
            return super.onOptionsItemSelected(item);
       }
    }

    ////////////////////////////////////////////////////////
    /////*
    /////*  Database open/close
    /////*
    ////////////////////////////////////////////////////////
    private void closeDB() 
    {
        myDb.close();
    }
    private void openDB() 
    {
        myDb = new RegisterServicesAdapter(this);
        myDb.open();
    }
    
    ////////////////////////////////////////////////////////
    /////*
    /////*  Refresh functions
    /////*
    ////////////////////////////////////////////////////////
    private void refresh()
    {
      //Make sure that there is a message if the listview is empty
        ListView listView = (ListView) findViewById(R.id.services_listView);
        listView.setEmptyView(findViewById(R.id.emptyRegisteredServices));
        openDB();
        populateListView();
        registerClickCallback();
        closeDB();
    }
    
    private void populateListView()
    {
        openDB();
        //Create the list of items
        Cursor cursor = myDb.getAllRows();                          
        
        //  String array to use as a map for which db rows should be mapped to which element in the template layout
        String[] client_name_list = new String[]{RegisterServicesAdapter.KEY_NAME, RegisterServicesAdapter.KEY_TYPE, RegisterServicesAdapter.KEY_RATE};
        int[] ints = new int[] {R.id.services_Name, R.id.service_Type, R.id.service_Rate};
    
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.service_layout_template, cursor, client_name_list , ints, 0);
        
        
        ListView list = (ListView) findViewById(R.id.services_listView);
        list.setAdapter(adapter);
        closeDB();
        
    }
    
    private void registerClickCallback() 
    {
        ListView list = (ListView) findViewById(R.id.services_listView);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) 
            {
//                    TextView textView = (TextView) viewClicked.findViewById(R.id.invoice_listview_layout_template_CustomerID);
//                    customerID = (String) textView.getText();
//                    Intent intent1 = new Intent(ClientInvoices.this, ShowDetailedInvoice.class);
//                    intent1.putExtra("InvoiceID", Long.toString(idInDB));
//                    intent1.putExtra("CustomerID", customerID);
//                     System.out.println(customerID);
//                    startActivity(intent1);
            }
        });
    }
    
    ////////////////////////////////////////////////////////
    /////*
    /////*  Utility functions 
    /////*
    ////////////////////////////////////////////////////////
    private String getDateTime() {
        Random randomGenerator = new Random();
        int rand = randomGenerator.nextInt(100) + 20;
        String value = Integer.toString(rand);
        
        return value;
    }
    
    private String getType()
    {
        String[] types = new String[] {"Education", "Packaging", "Construction", "Insurance", "Finance", "Repair", "Installation", "Advertising", "Business Management", "Medical Services",
                "Legal Services"};
        Random randomGenerator = new Random();
        int rand = randomGenerator.nextInt(11);
        
        return types[rand];
        
    }
    private String getName()
    {
        String[] types = new String[] {"Lawn Mow", "Spring Cleanup", "Mulch", "Trim", "Clean Gutters", "Plow Driveway", "Weed", "Hedge Clipping"};
        Random randomGenerator = new Random();
        int rand = randomGenerator.nextInt(8);
        
        return types[rand];
        
    }
    ////////////////////////////////////////////////////////
    /////*
    /////*  OnClick listener for adding an invoice as described in the xml
    /////*
    ////////////////////////////////////////////////////////
    public void onClick_AddNewService(View v)
    {
        openDB();
        String name = getName();
        String type = getType();
        String rate = getDateTime();
        
        myDb.insertRow(name, type, rate);
        closeDB();
        refresh();
    }
}

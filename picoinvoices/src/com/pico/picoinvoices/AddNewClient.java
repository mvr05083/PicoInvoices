package com.pico.picoinvoices;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

public class AddNewClient extends Activity 
{
	ClientAdapter myDb;
	
	// //////////////////////////////////////////////////////
    // ///*
    // ///* Activity lifecycle functions
    // ///*
    // //////////////////////////////////////////////////////
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_new_client);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		openDB();
	}
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
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
        getMenuInflater().inflate(R.menu.add_new_client, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
        case R.id.action_settings:
        System.out.println("Settings selected");
        return true;
        case R.id.action_acceptNewInvoice:
        System.out.println("Added new invoice");
        return true;
        default:
        return super.onOptionsItemSelected(item);
        }
    }
    
    	
	// //////////////////////////////////////////////////////
    // ///*
    // ///* Database functions
    // ///*
    // //////////////////////////////////////////////////////
	private void openDB() 
	{
		myDb = new ClientAdapter(this);
		myDb.open();
	}
	private void closeDB() 
	{
		myDb.close();
	}

	
	// //////////////////////////////////////////////////////
    // ///*
    // ///* OnClick functions
    // ///*
    // //////////////////////////////////////////////////////
	public void onClick_addUser(View v)
	{
		//First Name
	    EditText fname = (EditText) findViewById(R.id.txt_addNew_fname);
		String fname_txt = fname.getText().toString().trim() + " ";
		
		//Last Name
		EditText lname = (EditText) findViewById(R.id.txt_addNew_lname);
		String lname_txt = lname.getText().toString();
		
		//Address
		EditText address = (EditText) findViewById(R.id.txt_addNew_address);
		String address_txt = address.getText().toString();
		
		//Phone
		EditText phone = (EditText) findViewById(R.id.txt_addNew_phone);
		String phone_txt = phone.getText().toString();
		
		//EMail
		EditText email = (EditText) findViewById(R.id.txt_addNew_email);
		String email_txt = email.getText().toString();
		
		//Insert values into database
		myDb.insertRow(fname_txt, lname_txt, address_txt, phone_txt, email_txt, "Wegmans Lawn Care");
		
		//Call finish() to prevent the flow of activities from accessing this activity from the backstack
		finish();
		
	}
	public void onClick_cancelAdd(View v)
	{
		finish();
	}
	
}

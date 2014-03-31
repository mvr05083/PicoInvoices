package com.pico.picoinvoices;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class AddNewInvoice extends Activity
{
    //Database adpater
    private InvoiceAdapter _myDb = null;
    private SPAdapter _sp = null;
    //Used to track the element to add new elements to
    private int _nextBelowID = R.id.addNewInvoice_rateInput;
    private  int _value=1;
    
    //Array lists are used to store the View, Spinner, and EditText that are created dynamically 
    //So they can be referenced for remove and data retrieval 
    private ArrayList<Integer> _rIdStore = new ArrayList<Integer>();
    private ArrayList<Integer> _serviceID = new ArrayList<Integer>();
    private ArrayList<Spinner> _rIdStore_spinners = new ArrayList<Spinner>();
    private ArrayList<EditText> _rIdStore_editText = new ArrayList<EditText>();

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Activity Lifecycle functions
    // ///*
    // //////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_invoice);
        initialize();
    }

    private void initialize()
    {
        _sp = new SPAdapter(getApplicationContext());
        //Prevents the keyboard from inflating on Activity startup
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        //Create the original spinners
        Spinner customerSpinner = (Spinner) findViewById(R.id.addNewInvoice_customerSpinner);
        Spinner serviceSpinner = (Spinner) findViewById(R.id.addNewInvoice_serviceSpinner);
        
        //Load the services and customers into the proper spinner element
        addItemsOnSpinner(customerSpinner, "customer");
        addItemsOnSpinner(serviceSpinner, "services");
        
        addListenerOnSpinnerItemSelection(R.id.addNewInvoice_customerSpinner);
        addListenerOnSpinnerItemSelection(R.id.addNewInvoice_serviceSpinner);
        
        setSelection();
    }
    // //////////////////////////////////////////////////////
    // ///*
    // ///* Action Bar functions
    // ///*
    // //////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_invoice, menu);
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
    // ///* Database open/close
    // ///*
    // //////////////////////////////////////////////////////
    private void closeDB()
    {
        _myDb.close();
    }

    private void openDB()
    {
        _myDb = new InvoiceAdapter(this);
        _myDb.open();
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* add items into spinner dynamically
    // ///*
    // //////////////////////////////////////////////////////
    public void addItemsOnSpinner(Spinner spinner,  String cs)
    {
        List<String> list = null;
        //Use one function for adding items, send a string to determine which values to use
        if (cs.equals("services"))
        {
            list = getServices();
        } else
        {
            list = getCustomers();
        }
        
        //Set an adapter on the spinner to map the values to the layout
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    public void addListenerOnSpinnerItemSelection(int spinnerID)
    {
        //Get the spinner from the value of the spinnerID that was passed in.
        //The spinnerID is found from the array list that stores the dynamically created spinners
        Spinner spinner = (Spinner) findViewById(spinnerID);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int pos,
                    long id)
            {
                if(pos > 0)
                {
                System.out.println("You selected: " + pos);
                System.out.println("Value in serviceID" + _serviceID.get(pos-1));
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView)
            {
                return;
            }
        });
    }

    // Get the values from the services table to populate into an ArrayList to
    // load into the services spinner
    private ArrayList<String> getServices()
    {
        //Open database
        openDB();
        
        //Create the arraylist to store the services to add to the spinner
        ArrayList<String> list = new ArrayList<String>();
        list.add("No Service Selected");
        Cursor cursor = _myDb.getAllRows(RegisterServicesAdapter.DATABASE_TABLE, RegisterServicesAdapter.ALL_KEYS);
        cursor.moveToFirst();
        if(cursor != null)
        {
            do
            {
                //Only add the service if it is not already in the list of services
                if (list.contains(cursor.getString(RegisterServicesAdapter.COL_NAME)))
                {} 
                else
                {
                    list.add(cursor.getString(RegisterServicesAdapter.COL_NAME));
                    //Add the service 
                    _serviceID.add(cursor.getInt(RegisterServicesAdapter.COL_ROWID));
                }
            } while (cursor.moveToNext());
        }
        
        //Close cursor
        cursor.close();
        
        //Close database
        closeDB();
        return list;
    }

    // Get the values from the customer table to populate into an ArrayList to
    // load into the customer spinner
    private ArrayList<String> getCustomers()
    {
        //Open database
        openDB();
        ArrayList<String> list = new ArrayList<String>();
        list.add("No Client Selected");
        Cursor cursor = _myDb.getAllRows(ClientAdapter.DATABASE_TABLE,ClientAdapter.ALL_KEYS);
        cursor.moveToFirst();
        if (cursor != null)
        {
            do
            {
                //Only allow one of each instance into the dropdown
                if (list.contains(cursor.getString(ClientAdapter.COL_FNAME) + " " + cursor.getString(ClientAdapter.COL_LNAME)))
                {} 
                else
                {
                    list.add(cursor.getString(ClientAdapter.COL_FNAME) + " " + cursor.getString(ClientAdapter.COL_LNAME));
                }
    
            } while (cursor.moveToNext());
        }
        //Close cursor
        cursor.close();
        
        //Close database
        closeDB();
        return list;
    }
    private void setSelection()
    {
        Spinner spinner = (Spinner) findViewById(R.id.addNewInvoice_customerSpinner);
        spinner.setSelection(_sp.getClientID());
    }
    
    ////////////////////////////////////////////////////////
    /////*
    /////*  OnClick listener for accepting/cancelling changes
    /////*
    ////////////////////////////////////////////////////////
    public void onClick_addInvoice(View v)
    {
        String services = "";
        String rates ="";
        
        //Makes sure that the original edittext and spinner are added as well
        Spinner s = (Spinner) findViewById(R.id.addNewInvoice_serviceSpinner);
        services = services + s.getSelectedItem().toString() + "||";
        
        EditText et = (EditText) findViewById(R.id.addNewInvoice_rateInput);
        rates = rates + et.getText().toString() + "||";
        
        for(int i = 0; i < _rIdStore_spinners.size(); i++)
        {
            Spinner s2 = _rIdStore_spinners.get(i);
            services = services + s2.getSelectedItem().toString() + "||";
        }
        for(int i = 0; i < _rIdStore_editText.size(); i++)
        {
            EditText et2 = _rIdStore_editText.get(i);
            rates = rates + et2.getText().toString() + "||";
        }
        
        System.out.println(services + "\n\n\n" + rates);
    }
    public void onClick_cancelInvoice(View v)
    {
        finish();
    }
    
    ////////////////////////////////////////////////////////
    /////*
    /////*  OnClick listener adding/removing dynamic elements
    /////*
    ////////////////////////////////////////////////////////
    public void onClick_removeServiceDyn(View v)
    {
        //Prevent the subtraction button from replying to any subtraction if there are no additional serverices added
        if(_rIdStore.size() > 0)
        {
            //This relative view references the internal relativelayout view inside scroll view
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.addNewInvoiceLayout);
            int size = _rIdStore.size() - 1;
            
            //Use the ArrayList of stored, newly added spinners to retrieve as a 'View' and remove them from last to first
            View view = (View) findViewById(_rIdStore.get(size));
            layout.removeView(view);
            if(_rIdStore.size() > 1)
            {
                _rIdStore.remove(size);
                _nextBelowID = _rIdStore.get(size-1);
            }
            
            //This else is used to set the default spinner view to the original view so that the add/cancel buttons are flush 
            else
            {
                _rIdStore.remove(0);
                _nextBelowID = R.id.addNewInvoice_rateInput;
            }
            moveButtons();
        }
    }
    public void onClick_addServiceDyn(View v)
    {
        //Create a relativelayout that corresponds to an inner relative layout of the activity
        //Inner is used so that the view can be housed inside of a scroll view
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.addNewInvoiceLayout);
        
        //Inflate a layout already defined that contains a spinner and an edittext
        View l = LayoutInflater.from(getBaseContext()).inflate(R.layout.service_rows, null);
        int newID = generateViewId();

        //Set the layout rules for the new service_rows layout and add it to the activity layout
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, _nextBelowID);
        layout.addView(l, params);

        //Assign the generated ViewID to the new spinner created
        l.setId(newID);
        
        //Update the nextBelowID so that any new layouts added know which layout to attach to
        _nextBelowID = newID;
        
        //Add the element to the arraylist so they can be removed in the future
        _rIdStore.add(_nextBelowID);
        
        //Create new EditText and Spinner that corresponds to the newly created elements in the new layout.
        //This is done because by default the values are R.id.serviceRow_edit and R.id.serviceRow_spinner
        EditText et = (EditText) findViewById(R.id.serviceRow_edit);
        et.setId(generateViewId());
        _rIdStore_editText.add(et);
        
        Spinner s = (Spinner) findViewById(R.id.serviceRow_spinner);
        int id = generateViewId();
        s.setId(id);
        _rIdStore_spinners.add(s);
        addItemsOnSpinner(s, "services");
        addListenerOnSpinnerItemSelection(id);
        
        //Align the 
        moveButtons();
    }
    private void moveButtons()
    {
        //Create a linearlayout that corresponds to the container that holds the 'Add' and 'Cancel' buttons
        LinearLayout cancel = (LinearLayout) findViewById(R.id.buttonContainer);
        
        //Programmically set the layout of the button container to redraw itself to the bottom of the newest 
        //'new service' laytout. This is mapped to by the nextBelowID variable.
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.BELOW, _nextBelowID);
        cancel.setLayoutParams(p);
        
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public int generateViewId()
    {
        //This is used to generate a unique layout for the newly created elements
        //Older versions of Android do not support generateViewID() therefore a makeshift version of 'id' is used
        if (Build.VERSION.SDK_INT < 17)
        {
            int v = _value;
            _value++;
            return v;
        } else
        {
            return View.generateViewId();
        }
    }
    
}

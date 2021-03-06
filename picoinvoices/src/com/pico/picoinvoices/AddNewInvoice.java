package com.pico.picoinvoices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

public class AddNewInvoice extends Activity
{
    // Database adpater
    private InvoiceAdapter _myDb = null;
    private SPAdapter _sp = null;
    // Used to track the element to add new elements to
    private int _nextBelowID = R.id.addNewInvoice_rateInput;
    private int _value = 1;

    // Array lists are used to store the View, Spinner, and EditText that are
    // created dynamically
    // So they can be referenced for remove and data retrieval
    private ArrayList<Integer> _rIdStore = new ArrayList<Integer>();
    private ArrayList<Spinner> _rIdStore_spinners = new ArrayList<Spinner>();
    private ArrayList<EditText> _rIdStore_editText = new ArrayList<EditText>();

    private ArrayList<String> _serviceID = new ArrayList<String>();
    private ArrayList<String> _customerID = new ArrayList<String>();
    

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
    @Override
    protected void onResume()
    {
        super.onResume();
        initialize();
    }

    private void initialize()
    {
        _sp = new SPAdapter(getApplicationContext());
        // Prevents the keyboard from inflating on Activity startup
        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Create the original spinners
        Spinner customerSpinner = (Spinner) findViewById(R.id.addNewInvoice_customerSpinner);
        Spinner serviceSpinner = (Spinner) findViewById(R.id.addNewInvoice_serviceSpinner);

        // Load the services and customers into the proper spinner element
        addItemsOnSpinner(customerSpinner, "customer");
        addItemsOnSpinner(serviceSpinner, "services");

        //addListeners to the spinners
//        addListenerOnSpinnerItemSelection(R.id.addNewInvoice_customerSpinner);
//        addListenerOnSpinnerItemSelection(R.id.addNewInvoice_serviceSpinner);
        
        //Sets the default client if applicable otherwise specifies "No client selected"
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_new_invoice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId())
        {
            case R.id.action_cancelNewClient:
                onClick_cancelInvoice();
                return true;
            case R.id.action_acceptNewInvoice:
                onClick_addInvoice();
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
    public void addItemsOnSpinner(Spinner spinner, String cs)
    {
        List<String> list = null;
        // Use one function for adding items, send a string to determine which
        // values to use
        if (cs.equals("services"))
        {
            list = getServices();
            if (_serviceID.size() >0)
            {
                MyAdapter dataAdapter = new MyAdapter(this,
                        R.layout.spinner_text_layout, list, _serviceID);
                spinner.setAdapter(dataAdapter);
            }
        } 
        else if(cs.equals("customer"))
        {
            list = getCustomers();
            if ( _customerID.size() >0)
            {
                MyAdapter dataAdapter = new MyAdapter(this,
                        R.layout.spinner_text_layout, list, _customerID);
                spinner.setAdapter(dataAdapter);
            }
            
        }

        // Set an adapter on the spinner to map the values to the layout
        // ArrayAdapter<String> dataAdapter = new
        // ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,
        // list);
        // dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

       
    }

    public void addListenerOnSpinnerItemSelection(int spinnerID)
    {
        // Get the spinner from the value of the spinnerID that was passed in.
        // The spinnerID is found from the array list that stores the
        // dynamically created spinners
        Spinner spinner = (Spinner) findViewById(spinnerID);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int pos, long id)
            {
                if (pos > 0)
                {
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
        // Open database
        openDB();

        // Create the arraylist to store the services to add to the spinner
        ArrayList<String> list = new ArrayList<String>();
        list.add("No Service Selected");
        _serviceID.add("0");
        Cursor cursor = _myDb.getAllRows(
                RegisterServicesAdapter.DATABASE_TABLE,
                RegisterServicesAdapter.ALL_KEYS);

        if (cursor != null && cursor.getCount() >0)
        {
            cursor.moveToFirst();
            do
            {
                // Only add the service if it is not already in the list of
                // services
                if (list.contains(cursor.getString(RegisterServicesAdapter.COL_NAME)))
                {} 
                else
                {
                    list.add(cursor.getString(RegisterServicesAdapter.COL_NAME));
                    // Add the service
                    _serviceID.add(cursor.getString(RegisterServicesAdapter.COL_ROWID));
                }
            } while (cursor.moveToNext());
        } else
        {
        }

        // Close cursor
        cursor.close();

        // Close database
        closeDB();
        return list;
    }

    // Get the values from the customer table to populate into an ArrayList to
    // load into the customer spinner
    private ArrayList<String> getCustomers()
    {
        // Open database
        openDB();
        ArrayList<String> list = new ArrayList<String>();
        list.add("No Client Selected");
        _customerID.add("0");
        Cursor cursor = _myDb.getAllRows(ClientAdapter.DATABASE_TABLE,ClientAdapter.ALL_KEYS);
        
        if (cursor != null && cursor.getCount() >0)
        {
            cursor.moveToFirst();
            do
            {
                // Only allow one of each instance into the dropdown
                if (list.contains(cursor.getString(ClientAdapter.COL_FNAME)+ " " + cursor.getString(ClientAdapter.COL_LNAME)))
                {} 
                else
                {
                    list.add(cursor.getString(ClientAdapter.COL_FNAME) + " "+ cursor.getString(ClientAdapter.COL_LNAME));
                    _customerID.add(cursor.getString(ClientAdapter.COL_ROWID));
                }

            } while (cursor.moveToNext());
        }
        // Close cursor
        cursor.close();

        // Close database
        closeDB();
        return list;
    }

    private void setSelection()
    {
        Spinner spinner = (Spinner) findViewById(R.id.addNewInvoice_customerSpinner);
        spinner.setSelection(_sp.getClientID());
    }

    // //////////////////////////////////////////////////////
    // ///*
    // ///* OnClick listener for accepting/cancelling changes
    // ///*
    // //////////////////////////////////////////////////////
    public void onClick_cancelInvoice()
    {
        new AlertDialog.Builder(this)
        .setTitle("Add Invoice")
        .setMessage("Cancel add?")
        .setPositiveButton(android.R.string.yes,
        new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,
                    int which)
            {
               finish();
            }
        })
        .setNegativeButton(android.R.string.no,
        new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,
                    int which)
            {
                //Do nothing to go back to the current entry
            }
        }).setIcon(R.drawable.ic_launcher).show();
    }
    public void onClick_addInvoice()
    {
        new AlertDialog.Builder(this)
        .setTitle("Add Invoice")
        .setMessage("Do you want to add new invoice?")
        .setPositiveButton(android.R.string.yes,
        new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,
                    int which)
            {
                String services = "";
                String rates = "";
                boolean spinnerWrong = false;
                boolean rateEmpty = true;
                // Makes sure that the original edittext and spinner are added as well
                Spinner s = (Spinner) findViewById(R.id.addNewInvoice_serviceSpinner);
                TextView tv = (TextView) s.findViewById(R.id.spinnerText2);
                System.out.println(tv.getText().toString());
                if(tv.getText().toString().equals("0"))
                    spinnerWrong = true;
                else
                    services = services + tv.getText().toString() + "&";

                EditText et = (EditText) findViewById(R.id.addNewInvoice_rateInput);
                System.out.println(et.getText().toString());
                if (et.getText().toString().equals(""))
                    rateEmpty = false;
                else
                    rates = rates + et.getText().toString() + "&";
                
                //Loop through the list of EditTexts and Spinners for services
                for (int i = 0; i < _rIdStore_spinners.size(); i++)
                {
                    Spinner s2 = _rIdStore_spinners.get(i);
                    TextView tv2 = (TextView) s2.findViewById(R.id.spinnerText2);
                    System.out.println(tv2.getText().toString());
                    if(tv2.getText().toString().equals("0"))
                    {
                        System.out.println("Spinner is wrong");
                        spinnerWrong = true;
                    }
                    else
                        services = services + tv2.getText().toString() + "&";
                }
                for (int i = 0; i < _rIdStore_editText.size(); i++)
                {
                    EditText et2 = _rIdStore_editText.get(i);
                    System.out.println(et2.getText().toString());
                    if (et2.getText().toString().equals(""))
                        rateEmpty = false;
                    else
                        rates = rates + et2.getText().toString() + "&";
                }
                //Get the customer information
                s = (Spinner) findViewById(R.id.addNewInvoice_customerSpinner);
                tv = (TextView) s.findViewById(R.id.spinnerText2);
                if (tv.getText().toString().equals("0"))
                    spinnerWrong = true;
                
                
                //Check to make sure that the fields are filled out
                //Do not need to do sqlinjection checking here because ContentValues are used to insert into DB
                if (spinnerWrong == true)
                {
                    Toast.makeText(getBaseContext(), "Please choose a service and/or client.", Toast.LENGTH_LONG).show();
                }
                else if (rateEmpty == false)
                {
                    Toast.makeText(getBaseContext(), "Please enter rates for all services.", Toast.LENGTH_LONG).show();
                }
                else
                {
                    insertRow(services, rates, tv.getText().toString());
                    System.out.println("Inserted: " + services + "\n\n\n" + rates);
                    finish();
                }
            }
        })
        .setNegativeButton(android.R.string.no,
        new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog,
                    int which)
            {
                finish();
            }
        }).setIcon(R.drawable.ic_launcher).show();

    }
    private void insertRow(String services, String rates, String customer)
    {
        openDB();
        int total = 0;
        String[] amountdue = rates.trim().split("&");
        System.out.println(rates);
        for (int i=0; i < amountdue.length; i++)
        {
            System.out.println(amountdue[i]);
            total+=Integer.parseInt(amountdue[i]);
        }
       
        _myDb.insertRow(getDateTime(), customer, getDueDate(), rates, services, Integer.toString(total) , getStatus());
        
        closeDB();
    }
    private String getStatus() {
        String[] types = new String[] {"Open", "Pending", "Paid", "Quote", "Overdue"};
        Random randomGenerator = new Random();
        int rand = randomGenerator.nextInt(4);
        
        return types[rand];
    }
    @SuppressLint("SimpleDateFormat")
	private String getDateTime() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = new Date();
        return dateFormat.format(date);
    }
    @SuppressLint("SimpleDateFormat")
	private String getDueDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date()); // Now use today date.
        c.add(Calendar.DATE, 30); // Adding 5 days
        String output = sdf.format(c.getTime());
        System.out.println(output);
        return output;
    }
    public void onClick_cancelInvoice(View v)
    {
        finish();
    }
   
    // //////////////////////////////////////////////////////
    // ///*
    // ///* OnClick listener adding/removing dynamic elements
    // ///*
    // //////////////////////////////////////////////////////
    public void onClick_removeServiceDyn(View v)
    {
        // Prevent the subtraction button from replying to any subtraction if
        // there are no additional serverices added
        if (_rIdStore.size() > 0)
        {
            // This relative view references the internal relativelayout view
            // inside scroll view
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.addNewInvoiceLayout);
            int size = _rIdStore.size() - 1;

            // Use the ArrayList of stored, newly added spinners to retrieve as
            // a 'View' and remove them from last to first
            View view = (View) findViewById(_rIdStore.get(size));
            layout.removeView(view);
            if (_rIdStore.size() > 1)
            {
                _rIdStore.remove(size);
                _rIdStore_spinners.remove(size);
                _rIdStore_editText.remove(size);
                _nextBelowID = _rIdStore.get(size - 1);
            }

            // This else is used to set the default spinner view to the original
            // view so that the add/cancel buttons are flush
            else
            {
                _rIdStore.remove(0);
                _rIdStore_editText.remove(0);
                _rIdStore_spinners.remove(0);
                _nextBelowID = R.id.addNewInvoice_rateInput;
            }
            
            moveButtons();
        }
    }

    public void onClick_addServiceDyn(View v)
    {
        // Create a relativelayout that corresponds to an inner relative layout
        // of the activity
        // Inner is used so that the view can be housed inside of a scroll view
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.addNewInvoiceLayout);

        // Inflate a layout already defined that contains a spinner and an
        // edittext
        View l = LayoutInflater.from(getBaseContext()).inflate(
                R.layout.service_rows, null);
        int newID = generateViewId();

        // Set the layout rules for the new service_rows layout and add it to
        // the activity layout
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.BELOW, _nextBelowID);
        layout.addView(l, params);

        // Assign the generated ViewID to the new spinner created
        l.setId(newID);

        // Update the nextBelowID so that any new layouts added know which
        // layout to attach to
        _nextBelowID = newID;

        // Add the element to the arraylist so they can be removed in the future
        _rIdStore.add(_nextBelowID);

        // Create new EditText and Spinner that corresponds to the newly created
        // elements in the new layout.
        // This is done because by default the values are R.id.serviceRow_edit
        // and R.id.serviceRow_spinner
        EditText et = (EditText) findViewById(R.id.serviceRow_edit);
        et.setId(generateViewId());
//        et.requestFocus();
        _rIdStore_editText.add(et);

        Spinner s = (Spinner) findViewById(R.id.serviceRow_spinner);
        int id = generateViewId();
        s.setId(id);
        
        _rIdStore_spinners.add(s);
        addItemsOnSpinner(s, "services");
        addListenerOnSpinnerItemSelection(id);

        // Align the
        moveButtons();
    }

    private void moveButtons()
    {
        // Create a linearlayout that corresponds to the container that holds
        // the 'Add' and 'Cancel' buttons
        LinearLayout cancel = (LinearLayout) findViewById(R.id.buttonContainer);

        // Programmically set the layout of the button container to redraw
        // itself to the bottom of the newest
        // 'new service' laytout. This is mapped to by the nextBelowID variable.
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        p.addRule(RelativeLayout.BELOW, _nextBelowID);
        cancel.setLayoutParams(p);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public int generateViewId()
    {
        // This is used to generate a unique layout for the newly created
        // elements
        // Older versions of Android do not support generateViewID() therefore a
        // makeshift version of 'id' is used
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

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Internal class to handle the custom spinner
    // ///*
    // //////////////////////////////////////////////////////
    //  This needs to be an internal class so that the getLayoutInflater can be called (needs to extend the activity class)
    private class MyAdapter extends ArrayAdapter<String>
    {
        ArrayList<String> l = null, l2 =null;

        public MyAdapter(Context context, int textViewResourceId,List<String> list, List<String> list2)
        {
            super(context, textViewResourceId, list);
            l = (ArrayList<String>) list;
            l2 = (ArrayList<String>) list2;
        }

        @Override
        public View getDropDownView(int position, View convertView,ViewGroup parent)
        {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView,
                ViewGroup parent)
        {
            //Only return a row if there are elements (clients and services have been registered) in the arraylists
           if (l.size() > 0 && l2.size() > 0)
           {
                LayoutInflater inflater = getLayoutInflater();
                View row = inflater.inflate(R.layout.spinner_text_layout, parent,false);
                TextView label = (TextView) row.findViewById(R.id.spinnerText1);
                // if (position > 0)
                // label.setText(l.get(position-1));
                // else
                label.setText(l.get(position));
    
                TextView sub = (TextView) row.findViewById(R.id.spinnerText2);
                
                if (position > 0)
                    sub.setText(l2.get(position));
                else
                    sub.setText("0");
    
                return row;
           }
           else
           {
               return null;
           }
        }

    }

}

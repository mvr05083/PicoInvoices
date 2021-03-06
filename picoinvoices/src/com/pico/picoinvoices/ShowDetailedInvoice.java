package com.pico.picoinvoices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ShowDetailedInvoice extends Activity
{

    private List<String> _groupList;
    private List<String> _childList;
    private Map<String, List<String>> _invoice;
    private ExpandableListView _expListView;
    private InvoiceAdapter _myDb = null;
    private SPAdapter _sp = null;
    private String _fname, _lname, _address, _email, _phone;
    private String _issuedate, _service, _duedate, _amountdue, _status;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_detailed_invoice);

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

    // //////////////////////////////////////////////////////
    // ///*
    // ///* Action bar functions
    // ///*
    // //////////////////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.show_detailed_invoice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId())
        {
            case R.id.sendMail:
                onClick_Email();
                return true;
            case R.id.goto_Home:
                Intent home = new Intent(this, Home.class);
                startActivity(home);
                finish();
                return true;
            case R.id.goto_Clients:
                Intent clients = new Intent(this, ClientList.class);
                startActivity(clients);
                finish();
                return true;
            case R.id.goto_ManageInvoices:
                Intent manage = new Intent(this, ManageInvoices.class);
                startActivity(manage);
                finish();
                return true;
            case R.id.goto_Services:
                Intent services = new Intent(this, RegisterServices.class);
                startActivity(services);
                finish();
                return true;
            case R.id.goto_Settings:
                Intent settings = new Intent(this, Settings.class);
                startActivity(settings);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*
     * Database maintenance functions
     */
    private void closeDB()
    {
        _myDb.close();
    }

    private void openDB()
    {
        _myDb = new InvoiceAdapter(this);
        _myDb.open();
    }
    
    private void initialize()
    {
        _sp = new SPAdapter(getApplicationContext());
        System.out.println("Client Id (ShowDetailedInvoices):"+ _sp.getClientID());
        System.out.println("Invoice Id (ShowDetailedInvoices):"+ _sp.getInvoiceID());
        openDB();

        populateValues();

        createGroupList();

        createCollection();
        
        addItemsOnSpinner();

        _expListView = (ExpandableListView) findViewById(R.id.expandableListView1);
        final ExpandableListViewAdapter expListAdapter = new ExpandableListViewAdapter(
                this, _groupList, _invoice);
        _expListView.setAdapter(expListAdapter);

        // setGroupIndicatorToRight();

        _expListView.setOnChildClickListener(new OnChildClickListener()
        {
            public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id)
            {
                final String selected = (String) expListAdapter.getChild(groupPosition, childPosition);
                Toast.makeText(getBaseContext(), selected, Toast.LENGTH_LONG).show();

                return true;
            }
        });
        closeDB();
    }
    
    public void addItemsOnSpinner()
    {

        Spinner spinner = (Spinner) findViewById(R.id.detail_StatusSpinner);
        List<String> list = new ArrayList<String>();
        list.add("Quote");
        list.add("Pending");
        list.add("Overdue");
        list.add("Paid");
        
       
        MyAdapter dataAdapter = new MyAdapter(this,
                R.layout.simple_spinner_layout,list);
        spinner.setAdapter(dataAdapter);
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_spinner_item, list);
//        dataAdapter
//                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        _spinner.setAdapter(dataAdapter);
        addListenerOnSpinnerItemSelection();
    }

    public void addListenerOnSpinnerItemSelection()
    {
        Spinner spinner = (Spinner) findViewById(R.id.detail_StatusSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view,
                    int pos, long id)
            {
                System.out.println("Changed to");
//                TextView tv = (TextView) view.findViewById(R.id.detail_StatusSpinner);
                
//                Toast.makeText(ShowDetailedInvoice.this,"You have changed to " + tv.getText().toString(),Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> adapterView)
            {
                return;
            }
        });
    }
    
    private void populateValues()
    {
        openDB();
        // Populate the values for the contact information.
        Cursor cursor = _myDb.query(new String[] { Integer.toString(_sp.getClientID()) },ClientAdapter.DATABASE_TABLE);

        if (cursor.moveToFirst())
        {
            _fname = "First Name:\n\t" + cursor.getString(ClientAdapter.COL_FNAME);
            _lname = "Last Name:\n\t"  + cursor.getString(ClientAdapter.COL_LNAME);
            _address = "Address:\n\t" + cursor.getString(ClientAdapter.COL_ADDRESS);
            _email = "Email:\n\t" + cursor.getString(ClientAdapter.COL_EMAIL);
            _phone = "Phone:\n\t" + cursor.getString(ClientAdapter.COL_PHONE);
        } 
        else
        {
            Toast.makeText(ShowDetailedInvoice.this,"failed to load cursor for customerId " + _sp.getClientID(),Toast.LENGTH_SHORT).show();
        }
        
        // Populate invoice specific information
        cursor = _myDb.query(new String[] { Integer.toString(_sp.getInvoiceID()) },InvoiceAdapter.DATABASE_TABLE);
        if (cursor.moveToFirst())
        {
            _issuedate = "Date issued:\n\t" + returnDate(cursor.getString(InvoiceAdapter.COL_ISSUEDATE));
            _duedate = "Payment due date:\n\t" + returnDate(cursor.getString(InvoiceAdapter.COL_DUEDATE));
//            _priceservice = cursor.getString(InvoiceAdapter.COL_PRICESERVICE);
            _amountdue = "Amount due:\n\t$" + cursor.getString(InvoiceAdapter.COL_AMOUNTDUE);
            _status = "Current status:\n\t" + cursor.getString(InvoiceAdapter.COL_STATUS);
            _service = returnServices(cursor.getString(InvoiceAdapter.COL_SERVICE), cursor.getString(InvoiceAdapter.COL_PRICESERVICE));
        } 
        else
        {
            Toast.makeText(ShowDetailedInvoice.this,"failed to load cursor for invoiceID " + _sp.getInvoiceID(),Toast.LENGTH_SHORT).show();
        }
        
        cursor.close();
        closeDB();
    }
    private String returnServices(String s, String v)
    {
        Cursor cursor = null;
        String[] services = s.split("&");
        String[] amount = v.split("&");
        s = "Services:\n";
        
        for (int i = 0; i < services.length; i++)
        {
            System.out.println("Parsed service: " + services[i]);
            cursor = _myDb.query(new String[] {services[i]}, RegisterServicesAdapter.DATABASE_TABLE);
            if (cursor.moveToFirst())
            {
                s+="\t" + cursor.getString(RegisterServicesAdapter.COL_NAME) + "\t\t\t\t$" + amount[i] + "\n";
            }
            
        }
        cursor.close();
        return s.substring(0, s.length()-1);
    }
    
    //Convert the UTC time to a more readable form
    private String returnDate(String s)
    {
        DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        DateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        String formattedDate = "";
        try
        {
            Date dated = originalFormat.parse(s);
            formattedDate = targetFormat.format(dated);
            System.out.println(formattedDate);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return formattedDate;
    }

    //Creates the list for the main/parent headers (always shown)
    private void createGroupList()
    {
        _groupList = new ArrayList<String>();
        _groupList.add("Contact Info - " + _fname.substring(_fname.indexOf("\t") + 1, _fname.length()-1) + " " + _lname.substring(_lname.indexOf("\t") + 1, _lname.length()));
        _groupList.add("Invoice - " + _amountdue);
    }

    //Creates the list of child/sub categories
    private void createCollection()
    {
        // preparing laptops collection(child)
        String[] contactInfo = { _fname, _lname, _address, _email, _phone };
        String[] invoiceDetail = { _status, _issuedate, _duedate, _service, _amountdue };

        _invoice = new LinkedHashMap<String, List<String>>();

        for (String row : _groupList)
        {
            if (row.equals("Contact Info - " + _fname.substring(_fname.indexOf("\t") + 1, _fname.length()-1) + " " + _lname.substring(_lname.indexOf("\t") + 1, _lname.length())))
            {
                loadChild(contactInfo);
            } 
            else if (row.equals("Invoice - " + _amountdue))
            {
                loadChild(invoiceDetail);
            }
            _invoice.put(row, _childList);
        }
    }

    private void loadChild(String[] invoiceInfo)
    {
        _childList = new ArrayList<String>();
        for (String line : invoiceInfo)
        {
            _childList.add(line);
        }
    }

    // @SuppressWarnings("unused")
    // private void setGroupIndicatorToRight()
    // {
    // /* Get the screen width */
    // DisplayMetrics dm = new DisplayMetrics();
    // getWindowManager().getDefaultDisplay().getMetrics(dm);
    // int width = dm.widthPixels;
    //
    // _expListView.setIndicatorBounds(width - getDipsFromPixel(35), width
    // - getDipsFromPixel(5));
    // }
    //
    // // Convert pixel to dip
    // public int getDipsFromPixel(float pixels)
    // {
    // // Get the screen's density scale
    // final float scale = getResources().getDisplayMetrics().density;
    // // Convert the dps to pixels, based on density scale
    // return (int) (pixels * scale + 0.5f);
    // }


    public void onClick_Email()
    {
        System.out.println(_email.substring(_email.indexOf("\t"), _email.length()).trim());
        //This version only selects mail applications and will format the body with html
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.setType("text/html");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{_email.substring(_email.indexOf("\t"), _email.length()).trim()});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Invoice - " + _issuedate);
        intent.putExtra(Intent.EXTRA_TEXT,"" +  _fname +" " + _lname +"\n\t" + _service + "\n" + _amountdue);
  
        startActivity(Intent.createChooser(intent, "Send Email"));
    }
    
    private class MyAdapter extends ArrayAdapter<String>
    {
        ArrayList<String> l = null;

        public MyAdapter(Context context, int textViewResourceId,List<String> list)
        {
            super(context, textViewResourceId, list);
            l = (ArrayList<String>) list;
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
           if (l.size() > 0)
           {
                LayoutInflater inflater = getLayoutInflater();
                View row = inflater.inflate(R.layout.simple_spinner_layout, parent,false);
                TextView label = (TextView) row.findViewById(R.id.simple_spinner_tv);
                // if (position > 0)
                // label.setText(l.get(position-1));
                // else
                label.setText(l.get(position));
    
    
                return row;
           }
           else
           {
               return null;
           }
        }

    }
}
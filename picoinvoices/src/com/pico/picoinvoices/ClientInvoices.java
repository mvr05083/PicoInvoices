package com.pico.picoinvoices;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class ClientInvoices extends Activity 
{

	private InvoiceAdapter myDb = null;
<<<<<<< HEAD
	public static long INVOICE_ID = 0;
	private static long CLIENT_ID = ClientList.CLIENT_ID;
	
=======
	private long customer_ID = 0;
	private String customer_fname = "";
	private String customer_lname = "";
	private String customer_address = "";
	private String customer_phone = "";
	private String customer_email = "";
	private int INSERT_ID = Menu.FIRST;
>>>>>>> b78ad611cf4d378f58f0354984cd91df4c2925ab
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_client_invoices);
		openDB();
        refresh();
		
		TextView textView = (TextView) findViewById(R.id.client_invoices_txtClientName);
		String name = getClientName();
		System.out.println(name);
		textView.setText(name);
		
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
	        boolean result = super.onCreateOptionsMenu(menu);
<<<<<<< HEAD
=======
	        menu.add(0, INSERT_ID, 0, "Add New");
>>>>>>> b78ad611cf4d378f58f0354984cd91df4c2925ab
	        return result;
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
        
        openDB();
        refresh();
    }
	/*
	 * 	Open/close functions for the DB
	 */
	private void closeDB() 
	{
		myDb.close();
	}
	private void openDB() 
	{
		myDb = new InvoiceAdapter(this);
		myDb.open();
	}
	
	/*
	 * 	refresh functions
	 */
	private void refresh()
	{
		
		populateListView();
		registerClickCallback();
	}
	
	@SuppressWarnings("deprecation")
	private void populateListView()
	{
<<<<<<< HEAD
		Cursor cursor = myDb.getCustomerInvoice(ClientList.CLIENT_ID);							//Create the list of items
=======
		Cursor cursor = myDb.getCustomerInvoice(customer_ID);							//Create the list of items
>>>>>>> b78ad611cf4d378f58f0354984cd91df4c2925ab
		//	String array to use as a map for which db rows should be mapped to which element in the template layout
		String[] client_name_list = new String[]{InvoiceAdapter.KEY_ROWID, InvoiceAdapter.KEY_ISSUEDATE, InvoiceAdapter.KEY_STATUS};
		int[] ints = new int[] {R.id.invoice_listview_layout_template_txtInvoiceNumber, R.id.invoice_listview_layout_template_txtDate, 
				R.id.invoice_listview_layout_template_txtStatus};
	
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.invoice_listview_layout_template, cursor, client_name_list , ints);
		
		
		ListView list = (ListView) findViewById(R.id.client_invoices_listView);
		list.setAdapter(adapter);
		
	}
	
	private void registerClickCallback() 
	{
		ListView list = (ListView) findViewById(R.id.client_invoices_listView);
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long idInDB) 
			{
<<<<<<< HEAD
			    Intent intent = new Intent (ClientInvoices.this, ShowDetailedInvoice.class);
			    startActivity(intent);
//				Cursor cursor = myDb.getRow(idInDB);
//				if (cursor.moveToFirst())
//				{
//					long idDB = cursor.getLong(InvoiceAdapter.COL_ROWID);
					
					Intent intent1 = new Intent(ClientInvoices.this, ShowDetailedInvoice.class);
                    INVOICE_ID = idInDB;
                    
                    startActivity(intent1);
//				}
//				else 
//					Toast.makeText(ClientInvoices.this, "failed to load "+idInDB, Toast.LENGTH_SHORT).show();
//				cursor.close();
=======
				Cursor cursor = myDb.getRow(idInDB);
				if (cursor.moveToFirst())
				{
					long idDB = cursor.getLong(InvoiceAdapter.COL_ROWID);
					long  customer = cursor.getLong(InvoiceAdapter.COL_CUSTOMER);
					String issuedate = cursor.getString(InvoiceAdapter.COL_ISSUEDATE);
					String dateserviceperformed = cursor.getString(InvoiceAdapter.COL_DATESERVICEPERFORMED);
					String servicedesc = cursor.getString(InvoiceAdapter.COL_SERVICEDESC);
					String status = cursor.getString(InvoiceAdapter.COL_STATUS);
					String priceservice = cursor.getString(InvoiceAdapter.COL_PRICESERVICE);
					
					String message = "Rowid: " + idDB + "\n" + customer +"\n" + issuedate +"\n"+ dateserviceperformed + "\n" + 
							servicedesc + "\n" + status + "\n" + priceservice + "\nContact Info: " + customer_fname + " " +customer_lname
							+"\n" + customer_address + "\n" + customer_phone + "\n" + customer_email;
					Toast.makeText(ClientInvoices.this, message, Toast.LENGTH_LONG).show();
				}
				else 
					Toast.makeText(ClientInvoices.this, "failed to load "+idInDB, Toast.LENGTH_SHORT).show();
				cursor.close();
>>>>>>> b78ad611cf4d378f58f0354984cd91df4c2925ab
			}
		});
	}
	
	
	/*
	 * 	onClickListeners are implemented here
	 */
	public void onClick_AddInvoice(View v)
	{
<<<<<<< HEAD
	    String issuedate = String.valueOf(new Date());
	    String customer = Long.toString(CLIENT_ID);
	    String dateserviceperformed = String.valueOf(new Date());
	    String priceservice = "300";
	    String service = "Mowing";
	    String servicedesc = "Front/back yard";
	    String amountdue = "100";
	    String status = "pending";
	    
	    myDb.insertRow(issuedate, customer, dateserviceperformed, priceservice, service, servicedesc, amountdue, status);
	    refresh();
//	    Intent goToInvoices = new Intent(this, AddNewInvoice.class);
//		goToInvoices.putExtra("customerID", customer_ID);
//		goToInvoices.putExtra("fname", customer_fname);
//		goToInvoices.putExtra("lname", customer_lname);
//		goToInvoices.putExtra("address", customer_address);
//		goToInvoices.putExtra("phone", customer_phone);
//		goToInvoices.putExtra("email", customer_email);
//		startActivity(goToInvoices);
	}
	
	private String getClientName()
	{
	    String name = "";
	    Cursor cursor = myDb.query(new String[] {Long.toString(CLIENT_ID)}, ClientAdapter.DATABASE_TABLE);
	    if (cursor.moveToFirst())
        {
            name+=cursor.getString(ClientAdapter.COL_FNAME);
            name+=" ";
            name+=cursor.getString(ClientAdapter.COL_LNAME);
        }
        else 
            Toast.makeText(ClientInvoices.this, "failed to load", Toast.LENGTH_SHORT).show();
        cursor.close();
	    return name;
=======
		Intent goToInvoices = new Intent(this, AddNewInvoice.class);
		goToInvoices.putExtra("customerID", customer_ID);
		goToInvoices.putExtra("fname", customer_fname);
		goToInvoices.putExtra("lname", customer_lname);
		goToInvoices.putExtra("address", customer_address);
		goToInvoices.putExtra("phone", customer_phone);
		goToInvoices.putExtra("email", customer_email);
		startActivity(goToInvoices);
>>>>>>> b78ad611cf4d378f58f0354984cd91df4c2925ab
	}
}

package com.pico.picoinvoices;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Settings extends Activity
{

    private InvoiceAdapter myDb = null;

    // private SPAdapter _sp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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

    @Override
    protected void onResume()
    {
        super.onResume();
        refresh();
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
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle presses on the action bar items
        switch (item.getItemId())
        {
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

    /*
     * Open/close functions for the DB
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
     * refresh functions
     */
    private void refresh()
    {
        openDB();
        populateListView();
        registerClickCallback();
        closeDB();
    }

    private void populateListView()
    {
        /*
         * Cursor cursor = myDb.getAllRows(); // Create the list of items //
         * String array to use as a map for which db rows should be mapped to //
         * which element in the template layout String[] client_name_list = new
         * String[] { InvoiceAdapter.KEY_ROWID, InvoiceAdapter.KEY_ISSUEDATE,
         * InvoiceAdapter.KEY_STATUS }; int[] ints = new int[] {
         * R.id.invoice_listview_layout_template_txtInvoiceNumber,
         * R.id.invoice_listview_layout_template_txtDate,
         * R.id.invoice_listview_layout_template_txtStatus };
         * 
         * SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
         * R.layout.invoice_listview_layout_template, cursor, client_name_list,
         * ints);
         * 
         * ListView list = (ListView)
         * findViewById(R.id.client_invoices_listView);
         * list.setAdapter(adapter);
         */

    }

    private void registerClickCallback()
    {
        /*
         * ListView list = (ListView)
         * findViewById(R.id.client_invoices_listView);
         * list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         * 
         * @Override public void onItemClick(AdapterView<?> parent, View
         * viewClicked, int position, long idInDB) {
         * 
         * Intent intent1 = new Intent(Settings.this,
         * ShowDetailedInvoice.class); _sp.saveInvioceID(Long.toString(idInDB));
         * startActivity(intent1); } });
         */
    }

    /*
     * onClickListeners are implemented here
     */
    // TODO Implement editing user data
    public void onClick_ToUserData(View v)
    {
        // Intent intent = new Intent(this, UserData.class);
        // startActivity(intent);
    }

    // TODO Implement importing database
    public void onClick_toImport(View v)
    {
        Intent intent = new Intent(this, ImportDB.class);
        startActivity(intent);
    }

    // TODO Implement exporting database
    public void onClick_toExport(View v)
    {
        System.out.println("Export database selected.");
        Intent intent = new Intent(this, ExportDB.class);
        startActivity(intent);
    }
}

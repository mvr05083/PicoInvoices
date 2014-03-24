package com.pico.picoinvoices;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

    public class SpinnerAdapter implements OnItemSelectedListener
    {
        public static String sort = "customer";
        InvoiceAdapter myDb = null;

        public void onItemSelected(AdapterView<?> parent, View view, int pos,
                long id)
        {
            
            sort = parent.getItemAtPosition(pos).toString();
            System.out.println(sort);
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0)
        {
            // TODO Auto-generated method stub
        }
    }
package com.pico.picoinvoices;

import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter
{

    private Activity context;
    private Map<String, List<String>> invoice;
    private List<String> content;

    public ExpandableListViewAdapter(Activity context, List<String> content,
            Map<String, List<String>> invoice)
    {
        this.context = context;
        this.invoice = invoice;
        this.content = content;
    }

    public Object getChild(int groupPosition, int childPosition)
    {
        return invoice.get(content.get(groupPosition)).get(
                childPosition);
    }

    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    public View getChildView(final int groupPosition, final int childPosition,
            boolean isLastChild, View convertView, ViewGroup parent)
    {
        final String laptop = (String) getChild(groupPosition, childPosition);
        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.child_view, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.laptop);

        ImageView delete = (ImageView) convertView.findViewById(R.id.delete);
        delete.setOnClickListener(new OnClickListener()
        {

            public void onClick(View v)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you want to remove?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                List<String> child = invoice
                                        .get(content.get(groupPosition));
                                child.remove(childPosition);
                                notifyDataSetChanged();
                            }
                        });
                builder.setNegativeButton("No",
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        item.setText(laptop);
        return convertView;
    }

    public int getChildrenCount(int groupPosition)
    {
        return invoice.get(content.get(groupPosition)).size();
    }

    public Object getGroup(int groupPosition)
    {
        return content.get(groupPosition);
    }

    public int getGroupCount()
    {
        return content.size();
    }

    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded,
            View convertView, ViewGroup parent)
    {
        String laptopName = (String) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.parent_view, null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.laptop);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(laptopName);
        return convertView;
    }

    public boolean hasStableIds()
    {
        return true;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
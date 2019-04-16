package com.alphacholera.passwordkeeper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    private ArrayList<EntryItem> items;
    private ArrayList<EntryItem> filteredItems;
    private Context context;
    private ClickListener clickListener;

    CustomAdapter(Context context, ArrayList<EntryItem> items, ClickListener clickListener) {
        this.context = context;
        this.items = items;
        this.filteredItems = items;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view_item, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v, filteredItems.get(myViewHolder.getAdapterPosition()));
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clickListener.onClick(v, filteredItems.get(myViewHolder.getAdapterPosition()));
                return true;
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int position) {
        final EntryItem item = items.get(position);
        myViewHolder.websiteName.setText(item.getWebsiteName());
        myViewHolder.dateAndTime.setText(item.getDate());
        if (myViewHolder.websiteName.getText().toString().isEmpty())
            myViewHolder.logo.setText("");
        else {
            StringBuilder builder = new StringBuilder();
            for (String string : myViewHolder.websiteName.getText().toString().split(" "))
                if ((string.charAt(0)>='a' && string.charAt(0)<='z') || (string.charAt(0)>='A' && string.charAt(0)<='Z'))
                    builder.append(string.charAt(0));
            myViewHolder.logo.setText(builder.toString());
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : filteredItems.size();
    }

    // For ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView websiteName, dateAndTime, logo;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.websiteName = itemView.findViewById(R.id.titleTextFieldRecyclerView);
            this.dateAndTime = itemView.findViewById(R.id.dateAndTimeTextFieldRecyclerView);
            this.logo = itemView.findViewById(R.id.logoRecyclerView);
        }
    }

    interface ClickListener {
        public void onClick(View view, EntryItem item);
        public void onLongClick(View view, EntryItem item);
    }

    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filteredItems = items;
                } else {
                    ArrayList<EntryItem> filteredList = new ArrayList<>();
                    for (EntryItem item : items) {
                        if (item.getWebsiteName().toLowerCase().contains(charString.toLowerCase()) ||
                                item.getUsername().toLowerCase().contains(charString.toLowerCase()))
                            filteredList.add(item);
                    }
                    filteredItems = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredItems;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredItems = (ArrayList<EntryItem>) results.values;
                notifyDataSetChanged();
                // The below stores the count of the results matched. Make a listener or some other kind of thing to implement it
                // results.count
            }
        };
    }
}

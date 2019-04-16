package com.alphacholera.passwordkeeper;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewAllPasswords extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private SearchView searchView;
    private CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_passwords);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAllPasswords.this, AddNewEntry.class);
                startActivity(intent);
            }
        });

//        this.selectedItems = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        linearLayout = findViewById(R.id.linear_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final ArrayList<EntryItem> items = (new DatabaseManagement(this)).getList();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(ViewAllPasswords.this, items, new CustomAdapter.ClickListener() {
            @Override
            public void onClick(View view, EntryItem item) {
                Intent intent = new Intent(getApplicationContext(), EditOrViewItem.class);
                intent.putExtra("DateAndTimeIntent", item.getDate());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, EntryItem item) {

            }
        });
        recyclerView.setAdapter(adapter);

        if (items.isEmpty()) {
            linearLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            linearLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.view_all_passwords_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search_item).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_password) {
            Intent intent = new Intent(ViewAllPasswords.this, ChangePassword.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}

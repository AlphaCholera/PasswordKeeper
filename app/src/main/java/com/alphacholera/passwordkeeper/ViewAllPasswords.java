package com.alphacholera.passwordkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewAllPasswords extends AppCompatActivity {

    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private Menu menu;
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

        recyclerView = findViewById(R.id.recyclerView);
        linearLayout = findViewById(R.id.linear_layout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<EntryItem> items = (new DatabaseManagement(this)).getList();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        CustomAdapter adapter = new CustomAdapter(ViewAllPasswords.this, items);
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
        this.menu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.view_all_passwords_menu, menu);
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
}

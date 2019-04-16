package com.alphacholera.passwordkeeper;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class AddNewEntry extends AppCompatActivity {
    private EditText websiteName, username, password;
    private TextView logo;
    private Button addButton;
    private DatabaseManagement db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_details);

        websiteName = findViewById(R.id.nameOfWebsite);
        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        logo = findViewById(R.id.logo);
        addButton = findViewById(R.id.addButton);

        getSupportActionBar().setTitle("Add Entry");
        db = new DatabaseManagement(AddNewEntry.this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (websiteName.getText().toString().isEmpty())
                    Toast.makeText(AddNewEntry.this, "Website name cannot be empty", Toast.LENGTH_LONG).show();
                else {
                    if (db.addEntry(websiteName.getText().toString(), username.getText().toString(), password.getText().toString())) {
                        Toast.makeText(AddNewEntry.this, "Successfully Added...!!!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
        });

        websiteName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (websiteName.getText().toString().isEmpty())
                    logo.setText("");
                else {
                    StringBuilder builder = new StringBuilder();
                    for (String string : websiteName.getText().toString().split(" "))
                        if ((string.charAt(0)>='a' && string.charAt(0)<='z') || (string.charAt(0)>='A' && string.charAt(0)<='Z'))
                        builder.append(string.charAt(0));
                    logo.setText(builder.toString());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (websiteName.getText().toString().isEmpty() && username.getText().toString().isEmpty() && password.getText().toString().isEmpty())
            finish();
        else {
            new AlertDialog.Builder(this)
                    .setMessage("Discard changes?")
                    .setPositiveButton("Discard", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        }
    }
}

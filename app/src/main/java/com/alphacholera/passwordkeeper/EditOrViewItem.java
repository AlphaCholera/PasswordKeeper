package com.alphacholera.passwordkeeper;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.KeyListener;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class EditOrViewItem extends AppCompatActivity {

    private EditText websiteName, username, password;
    private TextView headingTextView;
    private TextView logo;
    private Button saveButton;
    private Menu menu;
    private DatabaseManagement db;
    private static final String buttonText = "Save";
    private static final String headingText = "View Entry";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_details);

        headingTextView = findViewById(R.id.headingTextView);
        logo = findViewById(R.id.logo);
        websiteName = findViewById(R.id.nameOfWebsite);
        username = findViewById(R.id.usernameEditText);
        password = findViewById(R.id.passwordEditText);
        saveButton = findViewById(R.id.addButton);

        db = new DatabaseManagement(this);
        String[] details = db.getDetails(getIntent().getStringExtra("DateAndTimeIntent"));
        websiteName.setText(details[0]);
        username.setText(details[1]);
        password.setText(details[2]);

        if (websiteName.getText().toString().isEmpty())
            logo.setText("");
        else {
            StringBuilder builder = new StringBuilder();
            for (String string : websiteName.getText().toString().split(" "))
                if ((string.charAt(0)>='a' && string.charAt(0)<='z') || (string.charAt(0)>='A' && string.charAt(0)<='Z'))
                    builder.append(string.charAt(0));
            logo.setText(builder.toString());
        }

        saveButton.setEnabled(false);
        websiteName.setTag(websiteName.getKeyListener());
        websiteName.setKeyListener(null);
        username.setTag(username.getKeyListener());
        username.setKeyListener(null);
        password.setTag(password.getKeyListener());
        password.setKeyListener(null);

        headingTextView.setText(headingText);
        saveButton.setText(buttonText);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (websiteName.getText().toString().isEmpty())
                    Toast.makeText(EditOrViewItem.this, "Website Name cannot be empty", Toast.LENGTH_LONG).show();
                else {
                    if (db.updateEntry(getIntent().getStringExtra("DateAndTimeIntent"), websiteName.getText().toString(), username.getText().toString(), password.getText().toString())) {
                        Toast.makeText(EditOrViewItem.this, "Updated Successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else
                        Toast.makeText(EditOrViewItem.this, "Error in updating..!!!", Toast.LENGTH_LONG).show();
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

        websiteName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (websiteName.getKeyListener() == null) {
                    copyToClipboard(websiteName.getText().toString());
                    Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getKeyListener() == null) {
                    copyToClipboard(username.getText().toString());
                    Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            }
        });

        password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getKeyListener() == null) {
                    copyToClipboard(password.getText().toString());
                    Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.edit_or_view_item_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit_item) {
            websiteName.setKeyListener((KeyListener) websiteName.getTag());
            username.setKeyListener((KeyListener) username.getTag());
            password.setKeyListener((KeyListener) password.getTag());

            saveButton.setEnabled(true);
            headingTextView.setText("Edit Entry");
            menu.clear();
        } else if (item.getItemId() == R.id.share_item) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String body = String.valueOf("Website Name\n" + websiteName.getText()) +
                    "\n\nUsername\n" + username.getText() +
                    "\n\nPassword\n"+password.getText();
            sendIntent.putExtra(Intent.EXTRA_TEXT, body);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (item.getItemId() == R.id.delete_item) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to delete this entry?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (db.deleteEntry(getIntent().getStringExtra("DateAndTimeIntent"))) {
                                Toast.makeText(getApplicationContext(), "Deleted Successfully...!!!", Toast.LENGTH_LONG).show();
                                finish();
                            } else
                                Toast.makeText(getApplicationContext(), "Error in Deleting Item...!!!", Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    void copyToClipboard(String text) {
        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Source Text", text);
        clipboardManager.setPrimaryClip(clipData);
    }
    // Override onBackPressed()
}

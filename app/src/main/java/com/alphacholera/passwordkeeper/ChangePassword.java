package com.alphacholera.passwordkeeper;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class ChangePassword extends AppCompatActivity {

    private EditText oldPassword, newPassword, reenterNewPassword;
    private Button saveNewPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPassword = findViewById(R.id.oldPasswordEditText);
        newPassword = findViewById(R.id.newPasswordEditText);
        reenterNewPassword = findViewById(R.id.reenterNewPasswordEditText);
        saveNewPassword = findViewById(R.id.saveNewPasswordButton);

        saveNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("PasswordSharedPreference", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if (oldPassword.getText().toString().isEmpty() || newPassword.getText().toString().isEmpty() || reenterNewPassword.getText().toString().isEmpty())
                    Toast.makeText(getApplicationContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else if (!newPassword.getText().toString().equals(reenterNewPassword.getText().toString()))
                    Toast.makeText(getApplicationContext(), "New passwords do not match", Toast.LENGTH_SHORT).show();
                else if (!sp.getString("password", "").equals(oldPassword.getText().toString()))
                    Toast.makeText(getApplicationContext(), "Old password is incorrect", Toast.LENGTH_SHORT).show();
                else {
                    editor.putString("password", newPassword.getText().toString());
                    editor.apply();
                    Toast.makeText(getApplicationContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}

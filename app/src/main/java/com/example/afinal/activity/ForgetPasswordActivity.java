package com.example.afinal.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.afinal.R;
import com.example.afinal.db.TimeyDbHelper;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgetPasswordActivity extends AppCompatActivity{

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Button sendButton = findViewById(R.id.bt_send_email);

        sendButton.setOnClickListener(view -> {
            EditText emailEditText = (EditText)findViewById(R.id.et_email);
            String email = emailEditText.getText().toString();

            if (view.getId() == R.id.bt_send_email) {
                sendEmail(email);
            }
        });
    }

    private void sendEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(ForgetPasswordActivity.this, "Please input email", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteOpenHelper database = TimeyDbHelper.getInstance(this);
        SQLiteDatabase db = database.getWritableDatabase();

        if(db.isOpen()){
            // check if the user had been registered
            Cursor cursor = db.query("users",null,"email=?", new String[]{email},null,null,null);

            if (cursor.getCount() == 0) {
                // did not find account
                Toast.makeText(this, "This email is not registered", Toast.LENGTH_SHORT).show();

            } else {
                // initialize sender information
                String senderEmail = "zolayuan93@gmail.com";
                String password = "shnlpdfrgwkmjefe";

                String host = "smtp.gmail.com";

                Properties properties = System.getProperties();

                properties.put("mail.smtp.host", host);
                properties.put("mail.smtp.port", "465");
                properties.put("mail.smtp.ssl.enable", "true");
                properties.put("mail.smtp.auth", "true");

                try {
                    Toast.makeText(this, "Sending...", Toast.LENGTH_SHORT).show();

                    // send email
                    Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(senderEmail, password);
                        }
                    });

                    MimeMessage mimeMessage = new MimeMessage(session);

                    mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

                    mimeMessage.setSubject("Retrieving Password");

                    while (cursor.moveToNext()){
                        String userPassword = cursor.getString(4);
                        mimeMessage.setText("Your password:" + userPassword);
                    }

                    Thread thread = new Thread(() -> {
                        try {
                            Transport.send(mimeMessage);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }
                    });

                    thread.start();

                    // after finish sending email, jump to login page
                    Intent intentToLogin = new Intent(this, LoginActivity.class);
                    startActivity(intentToLogin);

                    Toast.makeText(this, "Please check your email to get password", Toast.LENGTH_SHORT).show();

                } catch (MessagingException e){
                    e.printStackTrace();
                }
            }

            cursor.close();
            db.close();
        }

    }

}


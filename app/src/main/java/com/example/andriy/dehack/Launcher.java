package com.example.andriy.dehack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Launcher extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);
        getSupportActionBar().hide();

        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(1000);
                } catch (Exception e) {

                } finally {
                    try {
                        String path = "/data/user/0/com.example.andriy.dehack/databases/Gromad";
                        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, 0);
                        Cursor c = db.query("Gromad", null, null, null, null, null, null);
                        c.moveToFirst();
                        if (c.getString(c.getColumnIndex("email")).equals("false")) {
                            Intent i = new Intent(Launcher.this,
                                    ChooseGromada.class);
                            c.close();
                            db.close();
                            startActivity(i);
                            finish();

                        }
                        if (c.getString(c.getColumnIndex("email")).equals("true")) {
                            Intent i = new Intent(Launcher.this,
                                    MainActivity.class);
                            c.close();
                            db.close();
                            startActivity(i);
                            finish();
                        }
                    } catch (Exception e) {

                        Intent i = new Intent(Launcher.this,
                                ChooseGromada.class);
                        startActivity(i);
                        finish();

                    }


                }
            }
        };
        welcomeThread.start();

    }
}

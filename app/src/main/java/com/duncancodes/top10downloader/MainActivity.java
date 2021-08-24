package com.duncancodes.top10downloader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //first parameter is URL, second parameter is for progress bar, last parameter is type of result
    private class DownloadData extends AsyncTask<String, Void, String>{



    }
}
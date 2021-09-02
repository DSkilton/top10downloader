package com.duncancodes.top10downloader;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: starting Asynctask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
        Log.d(TAG,"onCreate: done");//log d reports at debug level

    }

    //first parameter is URL, second parameter is for progress bar, last parameter is type of result
    private class DownloadData extends AsyncTask<String, Void, String>{
        private static final String TAG = "DownloadData";


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d(TAG, "onPostExecute: parameter is " + s);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: Starts with " + strings[0]);
            String rssFeed = downloadXML(strings[0]);

            if(rssFeed == null){
                Log.e(TAG, "doInBackground: Error downloading");//log e reports at error level
            }
            return rssFeed;
        }

        private String downloadXML (String urlPath) {
            StringBuilder xmlResult = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int response = connection.getResponseCode();
                Log.d(TAG, "downloadXML: The repsonse was " + response);
//                InputStream inputStream = connection.getInputStream();                    //These three lines can be replaced with the
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream); //single line command on line 66
//                BufferedReader reader = new BufferedReader(inputStreamReader);
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead;
                char[] inputBuffer = new char[500];
                while(true){
                    charsRead = reader.read(inputBuffer);
                    if(charsRead < 0){  // detects the reader returns a value less than 0 there is no more data
                        break;          // and the loop will break
                    }
                    if(charsRead > 0){  //0kb of data can be returned hence waiting for more than 0 before appending
                        xmlResult.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    } //charsRead will append the characters as required rather than 500 per time
                }
                reader.close(); //this close all 3 IO objects on line 66

                return xmlResult.toString();

            } catch (MalformedURLException e) {
                Log.e(TAG, "downloadXML: Invalid URL" + e.getMessage());
            } catch (IOException e){
                Log.e(TAG, "downloadXML: IO Exception reading data " + e.getMessage());
            } catch (SecurityException e){
                Log.e(TAG, "downloadXML: Security Exception. Needs permission? " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
    }
}
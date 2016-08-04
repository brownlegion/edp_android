package com.example.owner.edp;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Krishna N. Deoram on 2016-01-30.
 * Gumi is love. Gumi is life.
 */

//The first String parameter will be the URL, the rest are POST data.
public class APICaller extends AsyncTask <String, Object, String> {

    public interface AsyncResponse {
        void response(String output);
    }

    public AsyncResponse delegate;

    public APICaller (AsyncResponse delegate) {
        this.delegate = delegate;
    }

    @Override
    protected String doInBackground(String... params) {

        String response = "";

        try {
            String url = params[0];
            URL obj = new URL(url);
            URLConnection con = obj.openConnection();
            con.setDoOutput(true);
            PrintStream ps = new PrintStream(con.getOutputStream());
            String apiUrl = params[0];
            for (int i = 1; i < params.length; i++) {
                ps.print(params[i]);
                apiUrl = apiUrl + params[i];
            }
            Log.e("API Url", apiUrl);
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                Log.e("API Response", line);
                response = line;
                //publishProgress(line);
            }
            ps.close();

        } catch (Exception e) {
            Log.i("Exception", e.toString());
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        this.delegate.response(response);
    }
}

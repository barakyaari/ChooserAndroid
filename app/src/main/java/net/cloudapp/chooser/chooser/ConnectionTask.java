package net.cloudapp.chooser.chooser;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

public class ConnectionTask extends AsyncTask<BaseConnectionData, Void, String> {
    private Runnable doAtFinish;
    private ConnectionManager connectionManager;
    public ConnectionTask(Runnable doAtFinish, ConnectionManager connectionManager){
        this.doAtFinish = doAtFinish;
        this.connectionManager = connectionManager;
    }
    public ConnectionTask(ConnectionManager connectionManager){
        this.connectionManager = connectionManager;
    }


    @Override
    protected String doInBackground(BaseConnectionData... params) {
        String responseText = null;
        BaseConnectionData data = params[0];
        String urlString = data.getAddress();
        String type = data.getType();
        String charset = "UTF-8";
        try {
            //Open HttpUrlConnection:
            URL url = new URL(urlString);
            Log.i("ChooserApp", "Connection Url: " + urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod(type);
            urlConnection.setRequestProperty("Accept-Charset", charset);

            List<Pair<String, String>> headers = data.getHeaders();

            for (Pair<String, String> header: headers) {
                urlConnection.setRequestProperty(header.first, header.second);
            }

            OutputStream os = urlConnection.getOutputStream();
            os.flush();
            os.close();

            //Send:
            InputStream response = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(response, "utf-8"), 8);
            StringBuilder sBuilder = new StringBuilder();

            String line = null;
            while ((line = bReader.readLine()) != null) {
                sBuilder.append(line + "\n");
            }
            response.close();
            responseText = sBuilder.toString();
            responseText = responseText.substring(0, responseText.length() - 1); //Cut the last '\n'.
            Log.i("ChooserApp", "Got response from server: " + responseText);
            urlConnection.disconnect();
        } catch (SocketTimeoutException e) {
            Log.i("ChooserApp", "SERVER IS DOWN");
            return "-2";
        } catch (Exception e) {
            e.printStackTrace();
        }
            return responseText;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        this.connectionManager.setResponseText(s);
        if (doAtFinish != null) {
            try {
                doAtFinish.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}

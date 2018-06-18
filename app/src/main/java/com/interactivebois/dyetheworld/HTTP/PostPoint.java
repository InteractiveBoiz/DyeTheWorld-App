package com.interactivebois.dyetheworld.HTTP;

import android.os.AsyncTask;
import android.util.Log;

import com.interactivebois.dyetheworld.MapContainer;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class PostPoint extends AsyncTask<String, Void, Void>
{
    public double latitude;
    public double longitude;


    public PostPoint(double latitude, double longitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        MapContainer.getInstance().getDialog().setMessage("Sending Points...");
    }

    @Override
    protected Void doInBackground(String... params)
    {
        String url = "https://dyetheworld-dev.herokuapp.com/createpoint/";

        HttpHandler sh = new HttpHandler();

        String jsonStr;

        Log.d(TAG, "doInBackground: polygonid: " + params[0]);

        JSONObject point   = new JSONObject();
        try
        {
            point.put("_polygonID", params[0]);
            point.put("latitude", latitude);
            point.put("longitude", longitude);
        }
        catch (JSONException je)
        {
            Log.d(TAG, "doInBackground: " + je.toString());
        }

        Log.d(TAG, "doInBackground: " + point.toString());

        // Making a request to url and getting response
        jsonStr = sh.makePostCall(url, point.toString());

        if(jsonStr == null)
        {
            Log.e(TAG,"Couldn't get json from server.");
            return null;
        }

        MapContainer.getInstance().getRoutePoints().clear();

        Log.e(TAG,"Response from url: " + jsonStr);

        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
        // Dismiss the progress dialog

        if (MapContainer.getInstance().getDialog().isShowing()) {
            MapContainer.getInstance().getDialog().dismiss();
        }
    }
}
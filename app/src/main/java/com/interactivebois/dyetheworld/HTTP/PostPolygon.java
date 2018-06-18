package com.interactivebois.dyetheworld.HTTP;

import android.os.AsyncTask;
import android.util.Log;

import com.interactivebois.dyetheworld.MapContainer;
import com.interactivebois.dyetheworld.Model.Point;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class PostPolygon extends AsyncTask<Void, Void, String>
{
    public String _teamID;
    public double area;

    public PostPolygon(String _teamID, double area)
    {
        this._teamID = _teamID;
        this.area = area;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        MapContainer.getInstance().getDialog().setMessage("Sending Polygon...");
        MapContainer.getInstance().getDialog().show();
    }

    @Override
    protected String doInBackground(Void...arg0)
    {
        String url = "https://dyetheworld-dev.herokuapp.com/createpolygon/";

        HttpHandler sh = new HttpHandler();

        String jsonStr;

        JSONObject polygon   = new JSONObject();
        try
        {
            polygon.put("_teamID", _teamID);
            polygon.put("areaImperial",area);
        }
        catch (JSONException je)
        {
            Log.d(TAG, "doInBackground JsonException: " + je.toString());
        }

        Log.d(TAG, "doInBackground: " + polygon.toString());

        // Making a request to url and getting response
        jsonStr = sh.makePostCall(url, polygon.toString());

        if(jsonStr == null)
        {
            Log.e(TAG,"Couldn't get json from server.");
            return null;
        }

        Log.e(TAG,"Response from url: " + jsonStr);

        return findID(jsonStr);
    }

    public String findID(String jsonStr)
    {
        int index = jsonStr.indexOf(":");

        String id = "";

        for (int i = index; i < jsonStr.length(); i++)
        {
            if (jsonStr.charAt(i) == '"' || jsonStr.charAt(i) == ':')
            {
                continue;
            }
            else if (jsonStr.charAt(i) == ',')
            {
                break;
            }

            id += jsonStr.charAt(i);
        }

        return id;
    }

    @Override
    protected void onPostExecute(String result)
    {
        super.onPostExecute(result);
        // Dismiss the progress dialog

        for (Point point : MapContainer.getInstance().getRoutePoints())
        {
            if (point.getPolygonID() == null)
            {
                new PostPoint(point.getLatLng().latitude, point.getLatLng().longitude).execute(result);
            }
        }
    }
}
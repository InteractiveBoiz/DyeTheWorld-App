package com.interactivebois.dyetheworld.HTTP;

import android.os.AsyncTask;
import android.util.Log;

import com.interactivebois.dyetheworld.AsyncResponse;
import com.interactivebois.dyetheworld.MapActivity;
import com.interactivebois.dyetheworld.MapContainer;
import com.interactivebois.dyetheworld.Model.OwnPolygon;
import com.interactivebois.dyetheworld.Model.Point;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class GetPoints extends AsyncTask<Void, Void, Void>
{
    public AsyncResponse delegate = null;

    ArrayList<HashMap<String, String>> pointList;

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        pointList = new ArrayList<>();

        MapContainer.getInstance().getDialog().setMessage("Loading Points...");
    }

    @Override
    protected Void doInBackground(Void...arg0)
    {
        String url = "https://dyetheworld-dev.herokuapp.com/points/";

        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeGetCall(url);

        Log.e(TAG,"Response from url: " + jsonStr);

        if(jsonStr == null)
        {
            Log.e(TAG,"Couldn't get json from server.");
            return null;
        }

        try
        {
            //JSONObject jsonObj = new JSONObject(jsonStr);

            // Getting JSON Array node
            JSONArray points = new JSONArray(jsonStr);

            // looping through All Contacts
            for(int i = 0; i < points.length(); i++)
            {
                JSONObject c = points.getJSONObject(i);

                String id = c.getString("_id");
                String polygonID = c.getString("_polygonID");
                String latitude = c.getString("latitude");
                String longitude = c.getString("longitude");

                // tmp hash map for single contact
                HashMap<String, String> point = new HashMap<>();

                // adding each child node to HashMap key => value
                point.put("_id", id);
                point.put("_polygonID", polygonID);
                point.put("latitude", latitude);
                point.put("longitude", longitude);

                // adding contact to contact list
                pointList.add(point);
            }
        }
        catch(final JSONException e)
        {
            Log.e(TAG,"Json parsing error: " + e.getMessage());
        }
        return null;
    }

   /* @Override
    protected Void doInBackground(Void...arg0)
    {
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String jsonStr = sh.makeServiceCall(url);

        Log.e(TAG,"Response from url: " + jsonStr);

        if(jsonStr != null)
        {
            try
            {
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray contacts = jsonObj.getJSONArray("contacts");

                // looping through All Contacts
                for(int i = 0; i < contacts.length(); i++)
                {
                    JSONObject c = contacts.getJSONObject(i);

                    String id = c.getString("id");
                    String name = c.getString("name");
                    String email = c.getString("email");
                    String address = c.getString("address");
                    String gender = c.getString("gender");

                    // Phone node is JSON Object
                    JSONObject phone = c.getJSONObject("phone");
                    String mobile = phone.getString("mobile");
                    String home = phone.getString("home");
                    String office = phone.getString("office");

                    // tmp hash map for single contact
                    HashMap<String, String> contact = new HashMap<>();

                    // adding each child node to HashMap key => value
                    contact.put("id", id);
                    contact.put("name", name);
                    contact.put("email", email);
                    contact.put("mobile", mobile);

                    // adding contact to contact list
                    contactList.add(contact);
                }
            }
            catch(final JSONException e)
            {
                Log.e(TAG,"Json parsing error: " + e.getMessage());
            }
        }
        else
        {
            Log.e(TAG,"Couldn't get json from server.");
        }
        return null;
    }*/

    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
        // Dismiss the progress dialog

        if (pointList.isEmpty())
        {
            if (MapContainer.getInstance().getDialog().isShowing()) {
                MapContainer.getInstance().getDialog().dismiss();
            }
            return;
        }

        /*for (HashMap.Entry<String, String> entry : this.pointList.get(0).entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            Log.d(TAG, "onPostExecute: Key: " + key + " , value: " + value);
        }*/

        for (int i = 0; i < pointList.size(); i++)
        {
            String id = null;
            String polygonID = null;
            double latitude = 0.0;
            double longitude = 0.0;

            for (HashMap.Entry<String, String> entry : this.pointList.get(i).entrySet())
            {
                String key = entry.getKey();
                String value = entry.getValue();

                switch (key)
                {
                    case "_id":
                    {
                        id = value;
                        break;
                    }
                    case "_polygonID":
                    {
                        polygonID = value;
                        break;
                    }
                    case "latitude":
                    {
                        latitude = Double.valueOf(value);
                        break;
                    }
                    case "longitude":
                    {
                        longitude = Double.valueOf(value);
                        break;
                    }
                }

                Log.d(TAG, "onPostExecute: Key: " + key + " , value: " + value);
            }

            if (id == null)
            {
                continue;
            }

            MapContainer.getInstance().getPoints().add(new Point(id, polygonID, latitude, longitude));
        }

        for (Point point : MapContainer.getInstance().getPoints())
        {
            Log.d(TAG, "onPostExecute: Point: " + point.getId() + ", polygonID: " + point.getPolygonID() + ", latitude: " + point.getLatLng().latitude + ", longitude: " + point.getLatLng().longitude);
        }

        if (MapContainer.getInstance().getDialog().isShowing()) {
            MapContainer.getInstance().getDialog().dismiss();
        }

        this.delegate.processFinish();
    }
}
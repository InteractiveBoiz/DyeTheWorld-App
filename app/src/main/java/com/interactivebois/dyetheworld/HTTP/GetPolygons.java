package com.interactivebois.dyetheworld.HTTP;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.interactivebois.dyetheworld.MapActivity;
import com.interactivebois.dyetheworld.MapContainer;
import com.interactivebois.dyetheworld.Model.OwnPolygon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class GetPolygons extends AsyncTask<Void, Void, Void>
{
    ArrayList<HashMap<String, String>> polygonList;

    private GetPoints getPointsTask;

    public GetPolygons(MapActivity mapActivity)
    {
        this.getPointsTask = new GetPoints();
        this.getPointsTask.delegate = mapActivity;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        polygonList = new ArrayList<>();


        MapContainer.getInstance().getDialog().setMessage("Loading Polygons...");
    }

    @Override
    protected Void doInBackground(Void...arg0)
    {
        String url = "https://dyetheworld-dev.herokuapp.com/polygons/";

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
            // Getting JSON Array node
            JSONArray polygons = new JSONArray(jsonStr);

            // looping through All Contacts
            for(int i = 0; i < polygons.length(); i++)
            {
                JSONObject c = polygons.getJSONObject(i);

                String id = c.getString("_id");
                String teamID = c.getString("_teamID");
                String areaImperial = c.getString("areaImperial");

                // tmp hash map for single Polygon
                HashMap<String, String> point = new HashMap<>();

                // adding each child node to HashMap key => value
                point.put("_id", id);
                point.put("_teamID", teamID);
                point.put("areaImperial", areaImperial);

                // adding contact to contact list
                polygonList.add(point);
            }
        }
        catch(final JSONException e)
        {
            Log.e(TAG,"Json parsing error: " + e.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result)
    {
        super.onPostExecute(result);
        // Dismiss the progress dialog

        if (polygonList.isEmpty())
        {
            if (MapContainer.getInstance().getDialog().isShowing()) {
                MapContainer.getInstance().getDialog().dismiss();
            }
            return;
        }

        for (int i = 0; i < polygonList.size(); i++)
        {
            String id = null;
            String teamID = null;
            double areaImperial = 0;

            for (HashMap.Entry<String, String> entry : this.polygonList.get(i).entrySet())
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
                    case "areaImperial":
                    {
                        areaImperial = Double.valueOf(value);
                        break;
                    }
                    case "_teamID":
                    {
                        teamID = value;
                        break;
                    }
                }

                Log.d(TAG, "onPostExecute: Key: " + key + " , value: " + value);
            }

            if (id == null)
            {
                continue;
            }

            MapContainer.getInstance().getOwnPolygons().add(new OwnPolygon(id, teamID, areaImperial));
        }

        for (OwnPolygon ownPolygon : MapContainer.getInstance().getOwnPolygons())
        {
            Log.d(TAG, "onPostExecute: Polygon: " + ownPolygon.getId() + ", areaImperial: " + ownPolygon.getAreaImperial());
        }

        Log.d(TAG, "onPostExecute: Polygon: size:" + MapContainer.getInstance().getOwnPolygons().size());

        this.getPointsTask.execute();
    }
}
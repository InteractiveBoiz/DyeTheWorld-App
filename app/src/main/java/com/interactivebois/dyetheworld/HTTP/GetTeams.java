package com.interactivebois.dyetheworld.HTTP;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.interactivebois.dyetheworld.MapActivity;
import com.interactivebois.dyetheworld.MapContainer;
import com.interactivebois.dyetheworld.Model.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class GetTeams extends AsyncTask<Void, Void, Void>
{
    ArrayList<HashMap<String, String>> teamList;

    MapActivity mapActivity;

    public GetTeams(MapActivity mapActivity)
    {
        ProgressDialog dialog = new ProgressDialog(mapActivity);

        this.mapActivity = mapActivity;

        MapContainer.getInstance().setDialog(dialog);
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        teamList = new ArrayList<>();

        MapContainer.getInstance().getDialog().setMessage("Loading Teams...");
        MapContainer.getInstance().getDialog().setCanceledOnTouchOutside(false);
        MapContainer.getInstance().getDialog().show();
    }

    @Override
    protected Void doInBackground(Void...arg0)
    {
        String url = "https://dyetheworld-dev.herokuapp.com/teams/";

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
            JSONArray teams = new JSONArray(jsonStr);

            // looping through All Contacts
            for(int i = 0; i < teams.length(); i++)
            {
                JSONObject c = teams.getJSONObject(i);

                String id = c.getString("_id");
                String color = c.getString("colour");
                String score = c.getString("score");

                // tmp hash map for single Polygon
                HashMap<String, String> team = new HashMap<>();

                // adding each child node to HashMap key => value
                team.put("_id", id);
                team.put("colour", color);
                team.put("score", score);

                // adding contact to contact list
                teamList.add(team);
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

        if (teamList.isEmpty())
        {
            return;
        }

        for (int i = 0; i < teamList.size(); i++)
        {
            String id = null;
            String colour = null;
            double score = 0;

            for (HashMap.Entry<String, String> entry : this.teamList.get(i).entrySet())
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
                    case "colour":
                    {
                        colour = value;
                        break;
                    }
                    case "score":
                    {
                        score = Double.valueOf(value);
                        break;
                    }
                }

                Log.d(TAG, "onPostExecute: Key: " + key + " , value: " + value);
            }

            if (id == null)
            {
                continue;
            }

            MapContainer.getInstance().getTeams().add(new Team(id, colour, score));
        }

        new GetPolygons(this.mapActivity).execute();
    }
}
package com.interactivebois.dyetheworld.HTTP;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.interactivebois.dyetheworld.AsyncResponse;
import com.interactivebois.dyetheworld.MainActivity;
import com.interactivebois.dyetheworld.MapActivity;
import com.interactivebois.dyetheworld.MapContainer;
import com.interactivebois.dyetheworld.Model.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class GetPlayer extends AsyncTask<Void, Void, Void>
{
    HashMap<String, String> player;

    public AsyncResponse delegate = null;
    private String username;
    private String password;

    public GetPlayer(MainActivity mainActivity, String username, String password)
    {
        ProgressDialog dialog = new ProgressDialog(mainActivity);

        delegate = mainActivity;

        MapContainer.getInstance().setDialog(dialog);

        this.username = username;
        this.password = password;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        player = new HashMap<>();

        MapContainer.getInstance().getDialog().setMessage("Loading Player...");
        MapContainer.getInstance().getDialog().setCanceledOnTouchOutside(false);
        MapContainer.getInstance().getDialog().show();
    }

    @Override
    protected Void doInBackground(Void...arg0)
    {
        String url = "https://dyetheworld-dev.herokuapp.com/players/mail/" + username;

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
            JSONArray playerJson = new JSONArray(jsonStr);

            // looping through All Contacts
            for(int i = 0; i < playerJson.length(); i++)
            {
                JSONObject c = playerJson.getJSONObject(i);

                String id = c.getString("_id");
                String teamId = c.getString("_teamID");
                String username = c.getString("username");
                String password = c.getString("password");
                String totalDistance = c.getString("totalDistance");

                if (!this.username.equals(username) || !this.password.equals(password))
                {
                    return null;
                }

                // adding each child node to HashMap key => value
                this.player.put("_id", id);
                this.player.put("_teamID", teamId);
                this.player.put("username", username);
                this.player.put("password", password);
                this.player.put("totalDistance", totalDistance);
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

        if (player.isEmpty())
        {
            if (MapContainer.getInstance().getDialog().isShowing()) {
                MapContainer.getInstance().getDialog().dismiss();
            }

            return;
        }

        String id = null;
        String teamID = null;
        String username = null;
        double totalDistance = 0;

        for (HashMap.Entry<String, String> entry : this.player.entrySet())
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
                case "_teamID":
                {
                    teamID = value;
                    break;
                }
                case "username":
                {
                    username = value;
                    break;
                }
                case "totalDistance":
                {
                    totalDistance = Double.valueOf(value);
                    break;
                }
            }
        }

        MapContainer.getInstance().getPlayer().setUsername(username);
        MapContainer.getInstance().getPlayer().setTeamID(teamID);
        MapContainer.getInstance().getPlayer().setTotalDistance(totalDistance);

        if (MapContainer.getInstance().getDialog().isShowing()) {
            MapContainer.getInstance().getDialog().dismiss();
        }

        delegate.processFinish();
    }
}
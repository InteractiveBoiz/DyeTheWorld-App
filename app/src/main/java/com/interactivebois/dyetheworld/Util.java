package com.interactivebois.dyetheworld;

import android.util.Log;

import com.interactivebois.dyetheworld.Model.Team;

import static android.content.ContentValues.TAG;

public class Util
{
    private MapActivity view;

    public Util (MapActivity view)
    {
        this.view = view;
    }

    public String findColourByID(String teamID)
    {
        if (teamID.isEmpty() || view.getMapContainer().getTeams().isEmpty())
        {
            return null;
        }

        for (Team team : view.getMapContainer().getTeams())
        {
            if (!team.getId().equals(teamID))
            {
                continue;
            }
            Log.d(TAG, "findColourByID: Teamid: " + teamID + " colour, " + team.getColour());

            return team.getColour();
        }

        return null;
    }

    public String findIDByColour(String colour)
    {
        if (colour.isEmpty() || view.getMapContainer().getTeams().isEmpty())
        {
            return null;
        }

        for (Team team : view.getMapContainer().getTeams())
        {
            if (!team.getColour().equals(colour))
            {
                continue;
            }

            return team.getColour();
        }

        return null;
    }
}

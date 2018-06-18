package com.interactivebois.dyetheworld.Model;

import android.graphics.Color;

public class Player
{
    private String username;
    private String teamColor;
    private String teamID;
    private double totalDistance;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    /*public String getTeamColor()
    {
        return teamColor;
    }

    public void setTeamColor(String teamColor)
    {
        this.teamColor = teamColor;
    }*/

    public String getTeamID()
    {
        return teamID;
    }

    public void setTeamID(String teamID)
    {
        this.teamID = teamID;
    }

    public double getTotalDistance()
    {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance)
    {
        this.totalDistance = totalDistance;
    }
}

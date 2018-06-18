package com.interactivebois.dyetheworld.Model;

import java.util.ArrayList;

public class OwnPolygon
{
    private String id;
    private String teamID;
    private double areaImperial;
    private ArrayList<Point> points;

    public OwnPolygon(String id, String teamID, double areaImperial)
    {
        this.id = id;
        this.teamID = teamID;
        this.areaImperial = areaImperial;
    }

    public OwnPolygon(String id, double areaImperial)
    {
        this.id = id;
        this.areaImperial = areaImperial;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTeamID()
    {
        return teamID;
    }

    public void setTeamID(String teamID)
    {
        this.teamID = teamID;
    }

    public double getAreaImperial()
    {
        return areaImperial;
    }

    public void setAreaImperial(int areaImperial)
    {
        this.areaImperial = areaImperial;
    }

    public ArrayList<Point> getPoints()
    {
        return points;
    }

    public void setPoints(ArrayList<Point> points)
    {
        this.points = points;
    }
}

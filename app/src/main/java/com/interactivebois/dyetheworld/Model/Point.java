package com.interactivebois.dyetheworld.Model;

import com.google.android.gms.maps.model.LatLng;

public class Point
{
    private String id;
    private String polygonID;
    private LatLng latLng;

    public Point(LatLng latLng)
    {
        this.latLng = latLng;
    }

    public Point(String id, LatLng latLng)
    {
        this.id = id;
        this.latLng = latLng;
    }

    public Point(String id, String polygonID, LatLng latLng)
    {
        this.id = id;
        this.polygonID = polygonID;
        this.latLng = latLng;
    }

    public Point(String id, String polygonID, double latitude, double longitude)
    {
        this.id = id;
        this.polygonID = polygonID;
        this.latLng = new LatLng(latitude, longitude);
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getPolygonID()
    {
        return polygonID;
    }

    public void setPolygonID(String polygonID)
    {
        this.polygonID = polygonID;
    }

    public LatLng getLatLng()
    {
        return latLng;
    }

    public void setLatLng(LatLng latLng)
    {
        this.latLng = latLng;
    }
}

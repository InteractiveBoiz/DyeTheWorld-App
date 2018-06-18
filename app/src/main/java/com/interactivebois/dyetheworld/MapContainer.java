package com.interactivebois.dyetheworld;

import android.app.ProgressDialog;
import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.interactivebois.dyetheworld.Model.OwnPolygon;
import com.interactivebois.dyetheworld.Model.Player;
import com.interactivebois.dyetheworld.Model.Point;
import com.interactivebois.dyetheworld.Model.Team;

import java.util.ArrayList;
import java.util.HashMap;

public class MapContainer
{
    private static final MapContainer instance = new MapContainer();

    private boolean hasLoadedMap = false;
    private boolean routeStarted = false;
    private ArrayList<Point> points = new ArrayList<>();
    private ArrayList<Point> routePoints = new ArrayList<>();
    private ArrayList<OwnPolygon> ownPolygons = new ArrayList<>();
    private LatLng currentLatLng;
    private Player player = new Player();

    private ProgressDialog dialog;

    private ArrayList<Team> teams = new ArrayList<>();

    private MapContainer()
    {
    }

    public static MapContainer getInstance()
    {
        return instance;
    }

    public boolean isHasLoadedMap()
    {
        return hasLoadedMap;
    }

    public void setHasLoadedMap(boolean hasLoadedMap)
    {
        this.hasLoadedMap = hasLoadedMap;
    }
    public boolean isRouteStarted()
    {
        return routeStarted;
    }

    public void setRouteStarted(boolean routeStarted)
    {
        this.routeStarted = routeStarted;
    }

    public ArrayList<Point> getPoints()
    {
        return points;
    }

    public void setPoints(ArrayList<Point> points)
    {
        this.points = points;
    }

    public ArrayList<Point> getRoutePoints()
    {
        return routePoints;
    }

    public void setRoutePoints(ArrayList<Point> routePoints)
    {
        this.routePoints = routePoints;
    }

    public ArrayList<OwnPolygon> getOwnPolygons()
    {
        return ownPolygons;
    }

    public void setOwnPolygons(ArrayList<OwnPolygon> ownPolygons)
    {
        this.ownPolygons = ownPolygons;
    }

    public LatLng getCurrentLatLng()
    {
        return currentLatLng;
    }

    public void setCurrentLatLng(LatLng currentLatLng)
    {
        this.currentLatLng = currentLatLng;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public ProgressDialog getDialog()
    {
        return dialog;
    }

    public void setDialog(ProgressDialog dialog)
    {
        this.dialog = dialog;
    }

    public ArrayList<Team> getTeams()
    {
        return teams;
    }

    public void setTeams(ArrayList<Team> teams)
    {
        this.teams = teams;
    }
}

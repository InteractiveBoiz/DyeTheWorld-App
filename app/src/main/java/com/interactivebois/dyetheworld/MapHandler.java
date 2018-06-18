package com.interactivebois.dyetheworld;

import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.interactivebois.dyetheworld.HTTP.PostPolygon;
import com.interactivebois.dyetheworld.Model.OwnPolygon;
import com.interactivebois.dyetheworld.Model.Point;
import com.interactivebois.dyetheworld.Model.Team;

import java.util.ArrayList;
import java.util.List;

public class MapHandler
{
    private static final String TAG = "MapHandler";

    private MapActivity view;

    private Polyline currentRoute;

    public MapHandler (MapActivity mapActivity)
    {
        this.view = mapActivity;
    }

    public void initRoute()
    {
        PolylineOptions polylineOptions = new PolylineOptions()
                .add(MapContainer.getInstance().getCurrentLatLng());
        this.currentRoute = view.getmMap().addPolyline(polylineOptions);
    }

    public void createPoint(LatLng latLng)
    {
        Point point = new Point(latLng);

        MapContainer.getInstance().getRoutePoints().add(point);
        view.getmMap().addMarker(new MarkerOptions()
                .position(latLng)
                .title("id: " + point.getId()));

        updatePolyLine();
    }

    public void updatePolyLine()
    {
        List<LatLng> latLngList = new ArrayList<>();

        for (Point point : MapContainer.getInstance().getRoutePoints())
        {
            if (point.getPolygonID() == null)
            {
                latLngList.add(point.getLatLng());
            }
        }

        this.currentRoute.setPoints(latLngList);
    }

    public void setMapStyle()
    {
        try
        {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = view.getmMap().setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            view, R.raw.style_json));

            if (!success)
            {
                Log.e(TAG, "Style parsing failed.");
            }
        }
        catch (Resources.NotFoundException e)
        {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    public void moveCamera(LatLng latLng, float zoom)
    {
        Log.d(TAG, "moveCamera: moving the camera to: lat:" + latLng.latitude + ", lng:" + latLng.longitude);
        view.getmMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public float calculateDistance()
    {
        MapContainer mapContainer = MapContainer.getInstance();

        Location prevLocation = new Location("");
        prevLocation.setLatitude(mapContainer.getRoutePoints().get(mapContainer.getRoutePoints().size() - 1).getLatLng().latitude);
        prevLocation.setLongitude(mapContainer.getRoutePoints().get(mapContainer.getRoutePoints().size() - 1).getLatLng().longitude);
        Location currentLocation = new Location("");
        currentLocation.setLatitude(mapContainer.getCurrentLatLng().latitude);
        currentLocation.setLongitude(mapContainer.getCurrentLatLng().longitude);

        float distanceInMeters = currentLocation.distanceTo(prevLocation);

        return distanceInMeters;
    }

    public void initMapItems()
    {
        List<LatLng> latLngList = new ArrayList<>();

        Log.d(TAG, "initMapItems: Polygon: size:" + MapContainer.getInstance().getOwnPolygons().size());
        Log.d(TAG, "initMapItems: Polygon: size:" + MapContainer.getInstance().getPoints().size());

        if (MapContainer.getInstance().getOwnPolygons().isEmpty())
        {
            return;
        }
        else if (MapContainer.getInstance().getPoints().isEmpty())
        {
            return;
        }
        else if (MapContainer.getInstance().getTeams().isEmpty())
        {
            return;
        }

        for (OwnPolygon ownPolygon : MapContainer.getInstance().getOwnPolygons())
        {
            for (Point point : MapContainer.getInstance().getPoints())
            {
                if (!point.getPolygonID().equals(ownPolygon.getId()))
                {
                    continue;
                }

                latLngList.add(point.getLatLng());
            }

            if (latLngList.isEmpty())
            {
                continue;
            }

            String colour = null;

            for (Team team : MapContainer.getInstance().getTeams())
            {
                if (!team.getId().equals(ownPolygon.getTeamID()))
                {
                    continue;
                }

                colour = team.getColour();
            }

            Polygon polygon = view.getmMap().addPolygon(new PolygonOptions()
                    .clickable(true)
                    .fillColor(Color.parseColor(colour))
                    .addAll(latLngList));
            polygon.setTag(ownPolygon.getId());

            Log.d(TAG, "initMapItems: Area of Polygon:" + polygon.getId() + ": " + SphericalUtil.computeArea(polygon.getPoints()));

            latLngList.clear();
        }
    }

    public void createPolygon()
    {
        List<LatLng> latLngList = new ArrayList<>();

        for (Point point : MapContainer.getInstance().getRoutePoints())
        {
            if (point.getPolygonID() == null)
            {
                latLngList.add(point.getLatLng());
            }
        }
        
        String teamID = MapContainer.getInstance().getPlayer().getTeamID();
        String colour = view.getUtil().findColourByID(teamID);

        /*for (Team team : MapContainer.getInstance().getTeams())
        {
            if (!team.getColour().equals(MapContainer.getInstance().getPlayer().getTeamColor()))
            {
                continue;
            }

            colour = team.getColour();
        }*/

        Polygon polygon = view.getmMap().addPolygon(new PolygonOptions()
                .clickable(true)
                .fillColor(Color.parseColor(colour))
                .addAll(latLngList));
        polygon.setTag("alpha");

        new PostPolygon(teamID, SphericalUtil.computeArea(polygon.getPoints())).execute();
    }

    public Polyline getCurrentRoute()
    {
        return currentRoute;
    }
}

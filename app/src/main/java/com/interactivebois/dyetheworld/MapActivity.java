package com.interactivebois.dyetheworld;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.interactivebois.dyetheworld.HTTP.GetTeams;
import com.interactivebois.dyetheworld.Model.Point;
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnPolygonClickListener, GoogleMap.OnMapClickListener, AsyncResponse
{
    private static final String TAG = "MapActivity";

    private GoogleMap mMap;
    private ImageView mGps;

    public Button btnStart;
    public Button btnStop;

    public ViewState currentViewState;

    private MapHandler mapHandler = new MapHandler(this);
    private LocationHandler locationHandler = new LocationHandler(this);
    private MapContainer mapContainer = MapContainer.getInstance();
    private Util util = new Util(this);
    private LeaderboardHandler leaderboardHandler = new LeaderboardHandler(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mGps = (ImageView)findViewById(R.id.ic_gps);

        locationHandler.getLocationPermission();
        this.currentViewState = ViewState.MAPVIEW;

        TextView playerName = findViewById(R.id.playerName);
        TextView teamName = findViewById(R.id.teamName);

        playerName.setText(MapContainer.getInstance().getPlayer().getUsername());
        teamName.setText(MapContainer.getInstance().getPlayer().getTeamID());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        locationHandler.handlePermissionsResult(requestCode, grantResults);
    }

    public void initMap()
    {
        Log.d(TAG, "initMap: initialising map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);

        mGps.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d(TAG, "onClick: Clicked gps icon");
                locationHandler.setCenterCameraOnPlayer(true);
                mapHandler.moveCamera(MapContainer.getInstance().getCurrentLatLng(), locationHandler.getDefaultZoom());
            }
        });

        locationHandler.createLocationRequest();
        locationHandler.createLocationCallBack();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        Toast.makeText(this, "Map Is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;

        if (locationHandler.ismLocationPermissionGranted())
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                return;
            }

            locationHandler.getDeviceLocation();

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setCompassEnabled(false);
            mMap.getUiSettings().setTiltGesturesEnabled(false);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setRotateGesturesEnabled(false);
            mapHandler.setMapStyle();
        }
        mMap.setOnPolygonClickListener(this);
        mMap.setOnMapClickListener(this);

        setButtonsClickListeners();
        new GetTeams(this).execute();
    }

    @Override
    public void onPolygonClick(Polygon polygon)
    {
        Log.d(TAG, "onPolygonClick: clicked on polygon: " + polygon.getId());
        Toast.makeText(this, "Polygon clicked!", Toast.LENGTH_SHORT).show();
    }

    private void setButtonsClickListeners()
    {
        this.btnStart = findViewById(R.id.StartButton);
        this.btnStop = findViewById(R.id.StopRoute);

        this.btnStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                mapHandler.initRoute();
                mapHandler.createPoint(MapContainer.getInstance().getCurrentLatLng());
                MapContainer.getInstance().setRouteStarted(true);
                btnStop.setVisibility(View.VISIBLE);
                TextView runningText = (TextView) findViewById(R.id.RouteStarted);
                runningText.setText("R: " + MapContainer.getInstance().isRouteStarted());

            }
        });

        this.btnStop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (MapContainer.getInstance().getRoutePoints().size() >= 3)
                {
                    MapContainer.getInstance().setRouteStarted(false);
                    mapHandler.getCurrentRoute().remove();
                    //createPolygon();
                    mapHandler.createPolygon();
                    btnStart.setVisibility(View.VISIBLE);
                    TextView runningText = (TextView) findViewById(R.id.RouteStarted);
                    runningText.setText("R: " + MapContainer.getInstance().isRouteStarted());
                }
            }
        });

        ImageButton btnMenu = findViewById(R.id.nav_menu);
        btnMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (MapActivity.this.currentViewState == ViewState.PLAYERVIEW)
                {
                    changeView(ViewState.MAPVIEW);
                }
                else
                {
                    changeView(ViewState.PLAYERVIEW);
                }
            }
        });

        ImageButton btmClan = findViewById(R.id.nav_clan);
        btmClan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (MapActivity.this.currentViewState == ViewState.CLANVIEW)
                {
                    changeView(ViewState.MAPVIEW);
                }
                else
                {
                    changeView(ViewState.CLANVIEW);

                    LatLng latLng1 = new LatLng(55.859996, 12.494088);
                    LatLng latLng2 = new LatLng(55.863222, 12.488681);
                    LatLng latLng3 = new LatLng(55.864112, 12.496878);

                    Point point1 = new Point(latLng1);
                    Point point2 = new Point(latLng2);
                    Point point3 = new Point(latLng3);

                    MapContainer.getInstance().getRoutePoints().add(point1);
                    MapContainer.getInstance().getRoutePoints().add(point2);
                    MapContainer.getInstance().getRoutePoints().add(point3);

                    mapHandler.createPolygon();
                }
            }
        });

        ImageButton btnLeaderBoard = findViewById(R.id.nav_leaderboard);
        btnLeaderBoard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (MapActivity.this.currentViewState == ViewState.LEADERBOARDVIEW)
                {
                    changeView(ViewState.MAPVIEW);
                }
                else
                {
                    changeView(ViewState.LEADERBOARDVIEW);
                }

                Toast.makeText(MapActivity.this, "loaded: " + MapContainer.getInstance().isHasLoadedMap(), Toast.LENGTH_SHORT).show();
            }
        });

        Button btnLandMass = findViewById(R.id.totalLandMassBtn);
        btnLandMass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                leaderboardHandler.calculateByLandmass();
            }
        });

        Button btnCoveredMass = findViewById(R.id.totalColouredBtn);
        btnCoveredMass.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                leaderboardHandler.calculateByTotalCoverage();
            }
        });

        if (findViewById(R.id.layoutTeam) == null || MapContainer.getInstance().getPlayer().getTeamID() != null)
        {
            LinearLayout layout = findViewById(R.id.layoutTeam);

            layout.removeAllViews();

            return;
        }

        ChooseColorButtons();
    }

    private void ChooseColorButtons()
    {
        Button btnGreenTeam = findViewById(R.id.btnGreenTeam);
        btnGreenTeam.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MapContainer.getInstance().getPlayer().setTeamID(util.findIDByColour("#64669900"));

                removeTeamLayout();
            }
        });

        Button btnBlueTeam = findViewById(R.id.btnBlueTeam);
        btnBlueTeam.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MapContainer.getInstance().getPlayer().setTeamID(util.findIDByColour("#640099CC"));

                removeTeamLayout();
            }
        });

        Button btnRedTeam = findViewById(R.id.btnRedTeam);
        btnRedTeam.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MapContainer.getInstance().getPlayer().setTeamID(util.findIDByColour("##64CC0000"));

                removeTeamLayout();
            }
        });
    }

    public void removeTeamLayout()
    {
        LinearLayout layout = findViewById(R.id.layoutTeam);

        btnStart.setVisibility(View.VISIBLE);
        layout.removeAllViews();
    }

    @Override
    public void onMapClick(LatLng latLng)
    {
        Log.d(TAG, "onMapClick: clicked map");
        locationHandler.setCenterCameraOnPlayer(false);
    }

    public void changeView(ViewState viewState)
    {
        if (viewState == this.currentViewState)
        {
            return;
        }

        resetImageButtonSrc();

        switch (viewState)
        {
            case MAPVIEW:
            {
                this.currentViewState = ViewState.MAPVIEW;
            }
            return;
            case LEADERBOARDVIEW:
            {
                ConstraintLayout leaderboardMenu = findViewById(R.id.leaderboardMenuLayout);

                leaderboardMenu.setVisibility(View.VISIBLE);

                ImageButton leaderboardMenuBtn = findViewById(R.id.nav_leaderboard);

                leaderboardMenuBtn.setImageResource(R.drawable.nav_back);

                this.currentViewState = ViewState.LEADERBOARDVIEW;
            }
            return;
            case CLANVIEW:
            {
                ImageButton clanMenuBtn = findViewById(R.id.nav_clan);

                clanMenuBtn.setImageResource(R.drawable.nav_back);

                this.currentViewState = ViewState.CLANVIEW;
            }
            return;
            case PLAYERVIEW:
            {
                ConstraintLayout playerMenu = findViewById(R.id.playerMenuLayout);

                playerMenu.setVisibility(View.VISIBLE);

                ImageButton playerMenuBtn = findViewById(R.id.nav_menu);

                playerMenuBtn.setImageResource(R.drawable.nav_back);

                this.currentViewState = ViewState.PLAYERVIEW;
            }
            return;
            default:
            {
                //we should never get here
            }
            return;
        }
    }

    public void resetImageButtonSrc()
    {
        ViewState currentViewStateTemp = this.currentViewState;

        switch (currentViewStateTemp)
        {
            case LEADERBOARDVIEW:
            {
                ConstraintLayout leaderboardMenu = findViewById(R.id.leaderboardMenuLayout);

                leaderboardMenu.setVisibility(View.INVISIBLE);

                ImageButton leaderboardMenuBtn = findViewById(R.id.nav_leaderboard);

                leaderboardMenuBtn.setImageResource(R.drawable.nav_leaderboard);

            }
            return;
            case CLANVIEW:
            {
                ImageButton clanMenuBtn = findViewById(R.id.nav_clan);

                clanMenuBtn.setImageResource(R.drawable.nav_clan);

            }
            return;
            case PLAYERVIEW:
            {
                ConstraintLayout playerMenu = findViewById(R.id.playerMenuLayout);

                playerMenu.setVisibility(View.INVISIBLE);

                ImageButton playerMenuBtn = findViewById(R.id.nav_menu);

                playerMenuBtn.setImageResource(R.drawable.nav_menu);
            }
            return;
        }
    }

    @Override
    public void processFinish()
    {
        mapHandler.initMapItems();

        //ConstraintLayout playerLayout = findViewById(R.id.playerLayout);
        ConstraintLayout playerLayout = findViewById(R.id.playerMenuLayout);

        int color = Color.parseColor(util.findColourByID(MapContainer.getInstance().getPlayer().getTeamID()));

        playerLayout.setBackgroundColor(color);
    }

    public GoogleMap getmMap()
    {
        return mMap;
    }

    public MapHandler getMapHandler()
    {
        return mapHandler;
    }

    public LocationHandler getLocationHandler()
    {
        return locationHandler;
    }

    public MapContainer getMapContainer()
    {
        return mapContainer;
    }

    public Util getUtil()
    {
        return util;
    }
}


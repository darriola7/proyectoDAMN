package com.example.proyectodamn.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import com.example.proyectodamn.R;
import com.example.proyectodamn.api.APIClient;
import com.example.proyectodamn.api.APIInterface;
import com.example.proyectodamn.local.AuthLocal;
import com.example.proyectodamn.room.UserViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;


import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapaActivity extends AppCompatActivity implements
        OnMapReadyCallback {

    private static final String TAG = MapaActivity.class.getSimpleName();

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    public static final float INITIAL_ZOOM = 12f;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;


    private UserViewModel mUserViewModel;
    APIInterface apiInterface;
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        apiInterface = APIClient.getRetrofitClient().create(APIInterface.class);
        ctx = this.getApplicationContext();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mapa_container, mapFragment).commit();
        mapFragment.getMapAsync(this);
    }


    /**
     * Triggered when the map is ready to be used.
     *
     * @param googleMap The GoogleMap object representing the Google Map.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocales();
        setMapLongClick(mMap); // Set a long click listener for the map;
        setPoiClick(mMap); // Set a click listener for points of interest.
        setMapStyle(mMap); // Set the custom map style.
        enableMyLocation(mMap); // Enable location tracking.


        setInfoWindowClickToPanorama(mMap);
    }

    /**
     * Adds a blue marker to the map when the user long clicks on it.
     *
     * @param map The GoogleMap to attach the listener to.
     */
    private void setMapLongClick(final GoogleMap map) {
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                String snippet = String.format(Locale.getDefault(),
                        getString(R.string.lat_long_snippet),
                        latLng.latitude,
                        latLng.longitude);

                map.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(getString(R.string.dropped_pin))
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.defaultMarker
                                (BitmapDescriptorFactory.HUE_BLUE)));
            }
        });
    }

    /**
     * Adds a marker when a place of interest (POI) is clicked with the name of
     * the POI and immediately shows the info window.
     *
     * @param map The GoogleMap to attach the listener to.
     */
    private void setPoiClick(final GoogleMap map) {
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest poi) {
                Marker poiMarker = map.addMarker(new MarkerOptions()
                        .position(poi.latLng)
                        .title(poi.name));
                poiMarker.showInfoWindow();
                poiMarker.setTag(getString(R.string.poi));
            }
        });
    }

    /**
     * Loads a style from the map_style.json file to style the Google Map. Log
     * the errors if the loading fails.
     *
     * @param map The GoogleMap object to style.
     */
    private void setMapStyle(GoogleMap map) {
        try {
            boolean success = map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    /**
     * Checks for location permissions, and requests them if they are missing.
     * Otherwise, enables the location layer.
     */
    private void enableMyLocation(GoogleMap map) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);

        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    /**
     * Starts a Street View panorama when an info window containing the poi tag
     * is clicked.
     *
     * @param map The GoogleMap to set the listener to.
     */
    private void setInfoWindowClickToPanorama(GoogleMap map) {
        map.setOnInfoWindowClickListener(
                new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        // Check the tag
                        if (marker.getTag() == "poi") {

                            // Set the position to the position of the marker
                            StreetViewPanoramaOptions options =
                                    new StreetViewPanoramaOptions().position(
                                            marker.getPosition());

                            SupportStreetViewPanoramaFragment streetViewFragment
                                    = SupportStreetViewPanoramaFragment
                                    .newInstance(options);

                            // Replace the fragment and add it to the backstack
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.mapa_container,
                                            streetViewFragment)
                                    .addToBackStack(null).commit();
                        }
                    }
                });
    }

    public void getLocales() {
        mUserViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        mUserViewModel.getUser().observe(this, user -> {
            String authotization = "Bearer " + user.getMToken();
            try {
                Call<List<AuthLocal>> callAuthResponse = apiInterface.locales(authotization, 0, 0);
                callAuthResponse.enqueue(new Callback<List<AuthLocal>>() {
                    @Override
                    public void onResponse(Call<List<AuthLocal>> call, Response<List<AuthLocal>> response) {
                        if (response.code() == 200) {

                            Log.d("GET_LOCALES", "Locales obtenidos");
                            List<AuthLocal> locales = response.body();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                locales.forEach(local -> {
                                    LatLng newLocal = new LatLng(local.geolocalizacion.latitud, local.geolocalizacion.longitud);
                                    mMap.addMarker(new MarkerOptions()
                                            .position(newLocal)
                                            .title(local.nombre));
                                });
                            }
                        } else {
                            Log.d("GET_LOCALES", "Error: " + response.code());
                            Toast.makeText(ctx, "Error al obtener locales.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<AuthLocal>> call, @NotNull Throwable t) {
                        Log.d("MAIN_ACTIVITY_LOCAL", "Error en el callback local" + t.getMessage());
                    }
                });
            } catch (Exception e) {
                Log.d("LOCALES ERROR", "Exploto todo");
            }
        });
    }
}

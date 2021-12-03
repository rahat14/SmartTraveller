package com.metacoder.transalvania.ui.nearme;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.metacoder.transalvania.R;
import com.metacoder.transalvania.databinding.ActivityPaceBinding;
import com.metacoder.transalvania.models.PlaceResponse;
import com.metacoder.transalvania.models.PlaceResult;
import com.metacoder.transalvania.utils.PermissionUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

public class Pace extends AppCompatActivity
        implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        OnMapReadyCallback {
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    int i = 0;
    LatLng latLng = null;
    String[] places = {"Please Select", "Atm", "Hospital", "Hotel", "Restaurant", "Police Station", "Fire Service"};
    String[] placesType = {"", "Atm", "Hospital", "Hotel", "Restaurant", "Police", "fire_station"};
    String placeType = "";
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean permissionDenied = false;
    private GoogleMap map;
    private PlacesClient placesClient;
    private ActivityPaceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Near Me");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));

        placesClient = Places.createClient(this);


        binding.find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (latLng != null && !placeType.isEmpty()) {

                        getNearByPlace(latLng, placeType);

                    }


                } catch (Exception e) {

                }

            }
        });


        ArrayAdapter<String> catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, places);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(catAdapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    placeType = placesType[i].toLowerCase(Locale.ROOT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        //    map.setMyLocationEnabled(true);
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        enableMyLocation();


        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                //   Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
//                Location location = map.getMyLocation();

                if (i == 0) {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 14);
                    map.animateCamera(cameraUpdate);
                    //getNearByPlace(latLng);
                }
                i = 1;


            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (map != null) {
                map.setMyLocationEnabled(true);

            }
        } else {
            // Permission to access the location is missing. Show rationale and request permission
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Permission was denied. Display an error message
            // Display the missing permission error dialog when the fragments resume.
            permissionDenied = true;
        }
    }

    private void getNearByPlace(LatLng latLng, String placesType) {
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" + "?location="
                + latLng.latitude + "," + latLng.longitude
                + "&radius=5000" + "&type=" + placesType
                + "&sensor=true" + "&key=" + getResources().getString(R.string.google_maps_key);

        new PlaceTask().execute(url);

    }

    private void InsetPlaceMarker(PlaceResponse placeResponse) {
        map.clear();
        Toast.makeText(getApplicationContext(), "Showing Result For " + placeType, Toast.LENGTH_LONG).show();
        for (PlaceResult item : placeResponse.getPlaceResults()) {
            LatLng latLng = new LatLng(item.getGeometry().getLocation().getLat(), item.getGeometry().getLocation().getLng());
            String name = item.getName();
            MarkerOptions options = new MarkerOptions();

            options.position(latLng);
            options.title(name);

            map.addMarker(options);


        }
    }

    private class PlaceTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);

            } catch (Exception e) {
                //     Toast.makeText(getApplicationContext(), "Error " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Log.d("TAGED", "doInBackground: " + e.getMessage());
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("TAG", "onPostExecute: " + s);
            Gson gson = new Gson();
            PlaceResponse placeResponse = gson.fromJson(s, PlaceResponse.class);
            //l  Toast.makeText(getApplicationContext(), "e " + placeResponse.getPlaceResults().get(0).getPlace_id(), Toast.LENGTH_LONG).show();
            InsetPlaceMarker(placeResponse);
        }

        private String downloadUrl(String string) throws IOException {
            URL url = new URL(string);
            Log.d("TAGw", "downloadUrl: " + string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream stream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);

            }
            String data = builder.toString();
            reader.close();
            Log.d("TAGED", "downloadUrl: " + data);
            return data;


        }
    }


}
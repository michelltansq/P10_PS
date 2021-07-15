package sg.edu.rp.webservices.p09_gettingmylocations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {

    Button btnRecord, btnGetLocation, btnRemoveLocation;
    GoogleMap map;
    TextView tvLat, tvLong;

    FusedLocationProviderClient client;

    LocationRequest locationRequest;
    LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRecord = findViewById(R.id.btnRecord);
        btnGetLocation = findViewById(R.id.btnGetLocation);
        btnRemoveLocation = findViewById(R.id.btnRemoveLocation);
        tvLat = findViewById(R.id.tvLat);
        tvLong = findViewById(R.id.tvLong);

        FragmentManager manager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) manager.findFragmentById(R.id.map);
        client = LocationServices.getFusedLocationProviderClient(this);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;

                UiSettings ui = map.getUiSettings();
                ui.setCompassEnabled(true);
                ui.setZoomControlsEnabled(true);

                checkPermission();
            }
        });

        btnGetLocation.setOnClickListener(v -> {
            if (checkPermission()) {
                LocationRequest locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(30000);
                locationRequest.setSmallestDisplacement(500);
                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        Location location = locationResult.getLastLocation();
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                        tvLat.setText("Latitude: " + location.getLatitude());
                        tvLong.setText("Longitude: " + location.getLongitude());

                        Toast.makeText(getApplicationContext(),"Lat: " + location.getLatitude() + ", Long: " + location.getLongitude(),Toast.LENGTH_SHORT).show();

                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        map.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title("Last Known Location")
                                .snippet("Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude())
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                };
                client.requestLocationUpdates(locationRequest, locationCallback, null);
            } else
                Log.i("Permission", "Denied");

        });

        btnRemoveLocation.setOnClickListener(v -> {
            client.removeLocationUpdates(locationCallback);
        });

        btnRecord.setOnClickListener(v -> {

        });
    }

    private boolean checkPermission() {
        int permissionCheck_Coarse = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int permissionCheck_Fine = ContextCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck_Coarse == PermissionChecker.PERMISSION_GRANTED || permissionCheck_Fine == PermissionChecker.PERMISSION_GRANTED)
            return true;
        else
            return false;
    }
}
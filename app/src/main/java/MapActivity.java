package com.example.myapplication;

import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.CameraUpdateFactory;
import android.widget.TextView;
import android.widget.Button;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private TextView technicianSummary;
    private Button btnUseLocation;
    private LatLng selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize summary text view
        technicianSummary = findViewById(R.id.technician_summary);
        btnUseLocation = findViewById(R.id.btnUseLocation);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (btnUseLocation != null) {
            btnUseLocation.setOnClickListener(v -> {
                if (selected != null) {
                    Intent data = new Intent();
                    data.putExtra("selectedLat", selected.latitude);
                    data.putExtra("selectedLng", selected.longitude);
                    setResult(RESULT_OK, data);
                    finish();
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add technician markers
        addTechnicianMarkers();

        // Set map to show all markers
        setMapToShowAllMarkers();

        // Allow selecting a point with long-press
        mMap.setOnMapLongClickListener(latLng -> {
            selected = latLng;
            mMap.clear();
            addTechnicianMarkers();
            mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
        });
    }

    private void addTechnicianMarkers() {
        // Add markers for each technician with better details
        LatLng technician1 = new LatLng(13.5407, 123.2412); // Camarines Norte
        mMap.addMarker(new MarkerOptions()
                .position(technician1)
                .title("John Doe")
                .snippet("Online - Camarines Norte"));

        LatLng technician2 = new LatLng(13.5407, 123.2412); // Same location for demo
        mMap.addMarker(new MarkerOptions()
                .position(technician2)
                .title("Jane Smith")
                .snippet("Online - Camarines Norte"));
    }

    private void setMapToShowAllMarkers() {
        // Create bounds to show all markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(13.5407, 123.2412)); // Camarines Norte
        builder.include(new LatLng(13.5407, 123.2412)); // Same location for demo
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }
}
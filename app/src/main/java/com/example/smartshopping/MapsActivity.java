package com.example.smartshopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.security.Permission;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_REQUEST_CODE = 100;
    private static final int PLACE_PICKER_REQUEST = 1;
    private GoogleMap mMap;

    TextView location;
    TextView address;
    TextView cardText;

    ProgressBar cardProgress;

    ImageView close;
    CardView confirmLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        location = findViewById(R.id.maps_location);
        address = findViewById(R.id.maps_address);
        cardText = findViewById(R.id.maps_card_text);

        cardProgress = findViewById(R.id.maps_card_progress);

        close = findViewById(R.id.maps_close);
        confirmLocation = findViewById(R.id.maps_card);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        confirmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardText.getVisibility() == View.INVISIBLE){
                    return;
                }

                String locality = location.getText().toString();
                String addressLine = address.getText().toString();

                double latitude = mMap.getCameraPosition().target.latitude;
                double longitude = mMap.getCameraPosition().target.longitude;

                Intent intent = new Intent();
                intent.putExtra("LOCALITY", locality);
                intent.putExtra("ADDRESS_LINE", addressLine);
                intent.putExtra("LATITUDE", latitude);
                intent.putExtra("LONGITUDE", longitude);

                setResult(PLACE_PICKER_REQUEST, intent);
                finish();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_REQUEST_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setLocationEnable();
            }
        }
    }

    private void setLocationEnable() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        setLocationEnable();

        CameraUpdate camera = CameraUpdateFactory.newLatLng(new LatLng(28.7041, 77.1025));

        mMap.moveCamera(camera);
        mMap.animateCamera(camera);

        mMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                cardText.setVisibility(View.INVISIBLE);
                cardProgress.setVisibility(View.VISIBLE);

                location.setText(getString(R.string.please_wait));
                address.setText(getString(R.string.loading));
            }
        });

        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                LatLng latLng = mMap.getCameraPosition().target;
                Geocoder geocoder = new Geocoder(MapsActivity.this);

                List<Address> geoAddress = null;

                try {
                    geoAddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(geoAddress == null || geoAddress.size() == 0) {
                    location.setText("Unauthorized location");
                    return;
                }

                if(geoAddress.get(0).getLocality() == null){
                    location.setText(geoAddress.get(0).getCountryName());
                } else {
                    location.setText(geoAddress.get(0).getLocality());
                }
                address.setText(geoAddress.get(0).getAddressLine(0));

                cardProgress.setVisibility(View.INVISIBLE);
                cardText.setVisibility(View.VISIBLE);
            }
        });

    }
}
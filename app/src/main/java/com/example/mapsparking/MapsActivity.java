package com.example.mapsparking;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mapsparking.Controller.DatabaseHandler;
import com.example.mapsparking.Model.Place;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.mapsparking.databinding.ActivityMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    DatabaseHandler db;

    ProgressBar progressBar;
    TextView textView;

    private LatLng Parking_Bordet = new LatLng(50.87762757467018, 4.411836140248157);
    private LatLng Parking_Tilleul = new LatLng(50.87110924274438, 4.3960856798539725);
    private LatLng Parking_Ceria = new LatLng(50.8159457230718, 4.288435421577517);
    private LatLng Parking_Hermann = new LatLng(50.812655068316644, 4.43123126551602);
    private LatLng Parking_Stjosse = new LatLng(50.855947436306884, 4.363790493668);
    private LatLng Parking_Ukkel = new LatLng(    50.794288260250184, 4.319323476567937);
    private LatLng Parking_Auderghem = new LatLng(      50.816313101340555, 4.4058846279915285);
    private LatLng Parking_Roodebeek = new LatLng(          50.84863726005167, 4.435640664804507);
    private LatLng Parking_RoutedeLennik = new LatLng(              50.81507580407859, 4.268509348018117);
    private LatLng Parking_Etterbeek = new LatLng(50.83767085974242, 4.382174717471488);

    private Marker mParking_Bordet;
    private Marker mParking_Tilleul;
    private Marker mParking_Ceria;
    private Marker mParking_Hermann;
    private Marker mParking_Stjosse;
    private Marker mParking_Ukkel;
    private Marker mParking_Auderghem;
    private Marker mParking_Roodebeek;
    private Marker mParking_RoutedeLennik;
    private Marker mParking_Etterbeek;

    private ActivityMapsBinding binding;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        db = new DatabaseHandler(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);
        if(currentLocation!= null){
            LatLng brussels = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(brussels).title("Current Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(brussels));
        }

        List<Marker> markerList = new ArrayList<>();
        List<Place> placeList = db.getAllPlaces();

        for (Place p:placeList){
            String myInfo = "ID: " + p.getId() + " Latitude: " + p.getPlatitude() + "Longitude" + p.getPlongitude() + "Title: " + p.getTitle();
            Log.d("myInfo", myInfo);

            markerList.add(mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Double.parseDouble(p.getPlatitude())
                    ,Double.parseDouble(p.getPlongitude()))).title(p.getTitle())));
        }

        mParking_Bordet = mMap.addMarker(new MarkerOptions()
                .position(Parking_Bordet).title("Parking Bordet")
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mParking_Bordet.setTag(0);
        markerList.add(mParking_Bordet);

        mParking_Tilleul = mMap.addMarker(new MarkerOptions()
                .position(Parking_Tilleul).title("Parking Tilleul")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mParking_Tilleul.setTag(0);
        markerList.add(mParking_Tilleul);

        mParking_Ceria = mMap.addMarker(new MarkerOptions()
                .position(Parking_Ceria).title("Parking Ceria")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mParking_Ceria.setTag(0);
        markerList.add(mParking_Ceria);

        mParking_Hermann = mMap.addMarker(new MarkerOptions()
                .position(Parking_Hermann).title("Parking Hermann")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mParking_Hermann.setTag(0);
        markerList.add(mParking_Hermann);

        mParking_Stjosse = mMap.addMarker(new MarkerOptions()
                .position(Parking_Stjosse).title("Parking Sjosse")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mParking_Stjosse.setTag(0);
        markerList.add(mParking_Stjosse);

        mParking_Ukkel = mMap.addMarker(new MarkerOptions()
                .position(Parking_Ukkel).title("Parking Ukkel")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mParking_Ukkel.setTag(0);
        markerList.add(mParking_Ukkel);

        mParking_Auderghem = mMap.addMarker(new MarkerOptions()
                .position(Parking_Auderghem).title("Parking Auderghem")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mParking_Auderghem.setTag(0);
        markerList.add(mParking_Auderghem);

        mParking_Roodebeek = mMap.addMarker(new MarkerOptions()
                .position(Parking_Roodebeek).title("Parking Roodebeek")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mParking_Roodebeek.setTag(0);
        markerList.add(mParking_Roodebeek);

        mParking_RoutedeLennik = mMap.addMarker(new MarkerOptions()
                .position(Parking_RoutedeLennik).title("Parking Route de Lennik")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mParking_RoutedeLennik.setTag(0);
        markerList.add(mParking_RoutedeLennik);

        mParking_Etterbeek = mMap.addMarker(new MarkerOptions()
                .position(Parking_Etterbeek).title("Parking Etterbeek")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mParking_Etterbeek.setTag(0);
        markerList.add(mParking_Etterbeek);

        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setOnMarkerClickListener(this);

        for(Marker m : markerList){
            LatLng latLng = new LatLng(50.868611733172834, 4.343448043452961);
            mMap.addMarker(new MarkerOptions().position(latLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11));
        }
    }

    private void getCurrentLocation(){
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(@NonNull @org.jetbrains.annotations.NotNull Location location) {
                if(location!= null){
                    currentLocation=location;
                    Toast.makeText(getApplicationContext(), (int) currentLocation.getLatitude(),Toast.LENGTH_LONG).show();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Toast.makeText(getApplicationContext()," location result is  " + locationResult, Toast.LENGTH_LONG).show();

                if (locationResult == null) {
                    Toast.makeText(getApplicationContext(),"current location is null ", Toast.LENGTH_LONG).show();

                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        Toast.makeText(getApplicationContext(),"current location is " + location.getLongitude(), Toast.LENGTH_LONG).show();

                        //TODO: UI updates.
                    }
                }
            }
        };
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MapsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
       // Toast.makeText(this,marker.getPosition().toString(),Toast.LENGTH_SHORT).show();
        //return false;
        Intent intent = new Intent(MapsActivity.this, ShowActivity.class);
        intent.putExtra("latitude" , marker.getPosition().latitude);
        intent.putExtra("longitude" , marker.getPosition().longitude);
        intent.putExtra("title" , marker.getTitle());
        startActivity(intent);
        return false;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title("new Marker")
               .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        Intent intent = new Intent(MapsActivity.this, AddActivity.class);
        intent.putExtra("latitude" , latLng.latitude);
        intent.putExtra("longitude" , latLng.longitude);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                }
                break;
        }
    }
}
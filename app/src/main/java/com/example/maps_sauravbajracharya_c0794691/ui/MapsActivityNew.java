package com.example.maps_sauravbajracharya_c0794691.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.maps_sauravbajracharya_c0794691.R;
import com.example.maps_sauravbajracharya_c0794691.Utilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivityNew extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private static final int REQUEST_CODE = 1;

    private LatLng currentLocationLatitudeLongitude;
    private LatLng markerOneLatitudeLongitude;
    private LatLng markerTwoLatitudeLongitude;
    private LatLng markerThreeLatitudeLongitude;
    private LatLng markerFourLatitudeLongitude;
    private Marker homeMarker;
    private double destMarker;
    private BitmapDescriptor flagBitmapDescriptor;
    private BitmapDescriptor flagBitmapDescriptor2;
    private BitmapDescriptor flagBitmapDescriptor3;
    private BitmapDescriptor flagBitmapDescriptor4;
    private double totalDistance;
    Marker centerOneMarker;
    Marker centerTwoMarker;
    Marker centerThreeMarker;
    Marker centerFourMarker;
    Polygon shape;


    private static final int POLYGON_SIDES = 4;
    List<Marker> markerList = new ArrayList();


    // location with location manager and listener
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_new);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                setHomeMarker(location);
                currentLocationLatitudeLongitude = new LatLng(location.getLatitude(), location.getLongitude());

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if (!hasLocationPermission())
            requestLocationPermission();
        else
            startUpdateLocation();

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(@NonNull LatLng latLng) {
//                setMarker(latLng);
//            }
//        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                Toast.makeText(MapsActivityNew.this, "Address: " + Utilities.getAddress(marker, getApplicationContext()), Toast.LENGTH_LONG).show();
//                Toast.makeText(MapsActivity.this, "Address: ", Toast.LENGTH_LONG).show();
//                Log.d("myTag", Utilities.getAddress(marker, MapsActivity.this));
//
                return false;
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                setMarker(latLng);

            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {
                if (shape != null) {
                    shape.remove();

                }

                if (centerOneMarker != null && centerTwoMarker != null && centerThreeMarker != null && centerFourMarker != null) {
                    centerOneMarker.remove();
                    centerTwoMarker.remove();
                    centerThreeMarker.remove();
                    centerFourMarker.remove();
                }


            }


            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                LatLng latLng = marker.getPosition();
                if (markerList.size() > 3) {
                    switch (marker.getTitle()) {

                        case "A":

                            markerOneLatitudeLongitude = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);


                            break;
                        case "B":

                            markerTwoLatitudeLongitude = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

                            break;
                        case "C":
                            markerThreeLatitudeLongitude = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

                            break;
                        case "D":
                            markerFourLatitudeLongitude = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);

                            break;
                    }

                    doCalculateDistanceWork();

                }


                drawShape();
            }
        });


    }

    public static double distance(LatLng start, LatLng end) {
        try {
            Location location1 = new Location("locationA");
            location1.setLatitude(start.latitude);
            location1.setLongitude(start.longitude);
            Location location2 = new Location("locationB");
            location2.setLatitude(end.latitude);
            location2.setLongitude(end.longitude);
            double distance = location1.distanceTo(location2);
            return distance;
        } catch (Exception e) {

            e.printStackTrace();

        }
        return 0;
    }


    private void setMarker(LatLng latLng) {
        Location location = null;

        if (markerList.size() == POLYGON_SIDES)
            clearMap();

        if (markerList.size() == 0) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title("A")
                    .draggable(true)
                    .icon(bitmapDescriptor(getApplicationContext(), R.drawable.ic_baseline_looks_one_24))
                    .snippet("Distance " + String.valueOf(Math.round(distance(currentLocationLatitudeLongitude, latLng)) + "m"));

            markerList.add(mMap.addMarker(options));

            markerOneLatitudeLongitude = new LatLng(latLng.latitude, latLng.longitude);

        } else if (markerList.size() == 1) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title("B")
                    .draggable(true)
                    .icon(bitmapDescriptor(getApplicationContext(), R.drawable.ic_baseline_looks_two_24))
                    .snippet("Distance " + String.valueOf(Math.round(distance(currentLocationLatitudeLongitude, latLng)) + "m"));
            ;
            markerList.add(mMap.addMarker(options));
            markerTwoLatitudeLongitude = new LatLng(latLng.latitude, latLng.longitude);


        } else if (markerList.size() == 2) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title("C")
                    .icon(bitmapDescriptor(getApplicationContext(), R.drawable.ic_baseline_looks_3_24))
                    .draggable(true)
                    .snippet("Distance " + String.valueOf(Math.round(distance(currentLocationLatitudeLongitude, latLng)) + "m"));
            ;
            markerList.add(mMap.addMarker(options));
            markerThreeLatitudeLongitude = new LatLng(latLng.latitude, latLng.longitude);

        } else if (markerList.size() == 3) {
            MarkerOptions options = new MarkerOptions().position(latLng)
                    .title("D")
                    .icon(bitmapDescriptor(getApplicationContext(), R.drawable.ic_baseline_looks_4_24))
                    .draggable(true)
                    .snippet("Distance " + String.valueOf(Math.round(distance(currentLocationLatitudeLongitude, latLng)) + "m"));
            markerFourLatitudeLongitude = new LatLng(latLng.latitude, latLng.longitude);
            markerList.add(mMap.addMarker(options));
            drawShape();
            doCalculateDistanceWork();


        }


    }

    private void doCalculateDistanceWork() {

        if (markerList.size() > 3) {

            //calculating distance between two markers
            double distance1 = distance(markerOneLatitudeLongitude, markerTwoLatitudeLongitude);
            double distance2 = distance(markerTwoLatitudeLongitude, markerThreeLatitudeLongitude);
            double distance3 = distance(markerThreeLatitudeLongitude, markerFourLatitudeLongitude);
            double distance4 = distance(markerFourLatitudeLongitude, markerOneLatitudeLongitude);


            //total distance of ABCD
            totalDistance = distance1 + distance2 + distance3 + distance4;

            createBitmapIconImagesWithDistance(distance1, distance2, distance3, distance4);


        } else {
            Toast.makeText(this, "Please select four points", Toast.LENGTH_SHORT).show();
        }
    }

    private void createBitmapIconImagesWithDistance(double distance1, double distance2, double distance3, double distance4) {

        //getting mid point between the markers so we can set a text showing distance at the middle

        flagBitmapDescriptor = Utilities.getBitMapWithDistanceValue(this, distance1);
        flagBitmapDescriptor2 = Utilities.getBitMapWithDistanceValue(this, distance2);
        flagBitmapDescriptor3 = Utilities.getBitMapWithDistanceValue(this, distance3);
        flagBitmapDescriptor4 = Utilities.getBitMapWithDistanceValue(this, distance4);

    }


    private BitmapDescriptor bitmapDescriptor(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    private void drawShape() {

        if (markerList.size() > 3) {
            PolygonOptions options = new PolygonOptions()
                    .fillColor(0x33000000)
                    .strokeColor(Color.RED)
                    .strokeWidth(5);

            for (int i = 0; i < POLYGON_SIDES; i++) {
                options.add(markerList.get(i).getPosition());

            }

            shape = mMap.addPolygon(options);

            shape.setClickable(true);
            mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener() {
                @Override
                public void onPolygonClick(@NonNull Polygon polygon) {

                    setDistanceViewInBetweenTwoMarker();
                }
            });
        }


    }

    private void setDistanceViewInBetweenTwoMarker() {
        Toast.makeText(MapsActivityNew.this, "Total Distance(A-B-C-D): " + String.valueOf(totalDistance) + "m", Toast.LENGTH_LONG).show();
        centerOneMarker = mMap.addMarker(new MarkerOptions()
                .position(Utilities.geMidPointOfTwoMarker(markerList.get(0).getPosition(), markerList.get(1).getPosition()))
                .icon(flagBitmapDescriptor));


        centerTwoMarker = mMap.addMarker(new MarkerOptions()
                .position(Utilities.geMidPointOfTwoMarker(markerList.get(1).getPosition(), markerList.get(2).getPosition()))
                .icon(flagBitmapDescriptor2));


        centerThreeMarker = mMap.addMarker(new MarkerOptions()
                .position(Utilities.geMidPointOfTwoMarker(markerList.get(2).getPosition(), markerList.get(3).getPosition()))
                .icon(flagBitmapDescriptor3));


        centerFourMarker = mMap.addMarker(new MarkerOptions()
                .position(Utilities.geMidPointOfTwoMarker(markerList.get(3).getPosition(), markerList.get(0).getPosition()))
                .icon(flagBitmapDescriptor4));


    }

    private void clearMap() {
        for (Marker marker : markerList)
            marker.remove();

        markerList.clear();
        shape.remove();
        shape = null;
    }


    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    private void startUpdateLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }

    private boolean hasLocationPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void setHomeMarker(Location location) {
        LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions options = new MarkerOptions().position(userLocation)
                .title("You are here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .snippet("Your Location");
        homeMarker = mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (REQUEST_CODE == requestCode) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
            }
        }
    }
}
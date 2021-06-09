package com.example.maps_sauravbajracharya_c0794691;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class Utilities {
    public static String getAddress(Marker marker, Context context){
        String address = "";
        Geocoder geocoder = new Geocoder(context);

        try {
            List<Address> addressList = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                address = "";

                // street name
                if (addressList.get(0).getThoroughfare() != null)
                    address += addressList.get(0).getThoroughfare() + "\n";
                if (addressList.get(0).getLocality() != null)
                    address += addressList.get(0).getLocality() + " ";
                if (addressList.get(0).getPostalCode() != null)
                    address += addressList.get(0).getPostalCode() + " ";
                if (addressList.get(0).getAdminArea() != null)
                    address += addressList.get(0).getAdminArea();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return address;
    }


    public static LatLng geMidPointOfTwoMarker(LatLng pointALatLng, LatLng pointBLatLng){

        double dLon = Math.toRadians(pointBLatLng.longitude - pointALatLng.longitude);

        double lat1 = Math.toRadians(pointALatLng.latitude);
        double lat2 = Math.toRadians(pointBLatLng.latitude);

        double lon1 = Math.toRadians(pointALatLng.longitude);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);

        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);


        double lat3Degree = Math.toDegrees(lat3);
        double lon3Degree = Math.toDegrees(lon3);

        return new LatLng(lat3Degree,lon3Degree);

    }

    public static BitmapDescriptor getBitMapWithDistanceValue(Activity context , double distance){

        LinearLayout distanceMarkerLayout = (LinearLayout) context.getLayoutInflater().inflate(R.layout.poyline_marker_layout, null);
        distanceMarkerLayout.setDrawingCacheEnabled(true);
        distanceMarkerLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        distanceMarkerLayout.layout(0, 0, distanceMarkerLayout.getMeasuredWidth(), distanceMarkerLayout.getMeasuredHeight());
        distanceMarkerLayout.buildDrawingCache(true);
        TextView positionDistance = (TextView) distanceMarkerLayout.findViewById(R.id.positionDistance);
        positionDistance.setText(distance + " meters");
        Bitmap flagBitmap = Bitmap.createBitmap(distanceMarkerLayout.getDrawingCache());
        distanceMarkerLayout.setDrawingCacheEnabled(false);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(flagBitmap);
        return bitmapDescriptor;
    }






}

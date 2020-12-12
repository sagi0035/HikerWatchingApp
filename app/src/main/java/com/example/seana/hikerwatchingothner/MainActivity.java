package com.example.seana.hikerwatchingothner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {




    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // so first we set up our locationmanager to pick up locations with the location listener
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // so on a location change we will update the info
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        // so here we see if the permission has been granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        } else {
            // if yes (permission granted) we wil get the gps
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,1,locationListener);
            // and if a location has already been inputted prior we will set that as the location
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (lastKnownLocation != null) {
                // and we will set the function as such
                updateLocationInfo(lastKnownLocation);
            }


        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // if we were able to get permission we will request the info within startListening()
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startListening();
        }
    }

    // and here we will request the user info
    public void startListening() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,1,locationListener);
        }
    }




    public void updateLocationInfo(Location location) {
        // so we will create a textview for each of the details that we want to provide to the user
        TextView textViewOne = findViewById(R.id.textView);
        TextView textViewTwo = findViewById(R.id.textView2);
        TextView textViewThree = findViewById(R.id.textView3);
        TextView textViewFour = findViewById(R.id.textView4);
        TextView textViewFive = findViewById(R.id.textView5);


        // herewe will pick up all the info as per the location and set the text to that
        // (so this is to provide some important location-related info to any potential hikers)
        textViewOne.setText("Latitude" + Double.toString(location.getLatitude()));
        textViewTwo.setText("Longitude" + Double.toString(location.getLongitude()));
        textViewThree.setText("Accurracy" + Double.toString(location.getAccuracy()));
        textViewFour.setText("Altitude" + Double.toString(location.getAltitude()));

        // we will first set the address to this just in case we are later on not going to get any address info
        // in which case we will just specify to the user that no adress is found
        String address = "Could not find an address :(";
        // so we will et geocoder to begin gettimg the info of the location
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {

            // we will get the adress info
            List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);

            // and if said info is available we will begin getting the specific details for the address
            if (listAddress != null && listAddress.size() > 0) {
                address = "Address:\n";
                if (listAddress.get(0).getThoroughfare()!= null) {
                    address+=listAddress.get(0).getThoroughfare()  + " ";
                }

                if (listAddress.get(0).getPostalCode() != null) {
                    address+=listAddress.get(0).getPostalCode() + " ";
                }


                if (listAddress.get(0).getLocality() != null) {
                    address+=listAddress.get(0).getLocality() + " ";
                }

                if (listAddress.get(0).getAdminArea() != null) {
                    address+=listAddress.get(0).getAdminArea();

                }
            }





        } catch (Exception e) {
            e.printStackTrace();
        }

        // and in the final text view we will (if available) palce the address
        textViewFive.setText(address);




    }
}

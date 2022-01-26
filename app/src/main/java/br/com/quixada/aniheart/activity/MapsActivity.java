package br.com.quixada.aniheart.activity;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.quixada.aniheart.R;
import br.com.quixada.aniheart.model.Manga;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        showPositionStudio(getIntent().getStringExtra("titulo"), mMap);
    }

    private void showPositionStudio(String titulo, GoogleMap googleMap){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("mangas").document(titulo).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            Manga manga = document.toObject(Manga.class);
                            String nome;
                            Double latitude;
                            Double longitude;

                            if(manga.getStudio() != null){
                                nome = manga.getStudio().getNome();
                                latitude = manga.getStudio().getLatitude();
                                longitude = manga.getStudio().getLongitude();
                            }else{
                                nome = "Not Found";
                                latitude = 0.0;
                                longitude = 0.0;
                            }

                            LatLng location = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions()
                                    .position(location)
                                    .title(nome));

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 20));
                        }
                    }
                });
    }


}


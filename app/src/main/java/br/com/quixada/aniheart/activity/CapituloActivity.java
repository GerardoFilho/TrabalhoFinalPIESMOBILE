package br.com.quixada.aniheart.activity;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import br.com.quixada.aniheart.R;
import br.com.quixada.aniheart.adapter.AdapterCapitulo;
import br.com.quixada.aniheart.model.Manga;

public class CapituloActivity extends AppCompatActivity implements SensorEventListener {

    RecyclerView paginasRecyclerView;
    SensorManager sensorManager;
    Sensor sensorProximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capitulo);
        paginasRecyclerView = findViewById(R.id.recyclerViewPaginas);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorProximity = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    @Override
    protected void onStart() {
        super.onStart();

        showPaginas(getIntent().getIntExtra("capitulo", 0), getIntent().getStringExtra("titulo"));

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void showPaginas(Integer position, String titulo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("mangas").document(titulo).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            Manga manga = document.toObject(Manga.class);
                            Integer qtdPaginas = Integer.parseInt(manga.getCapitulos().get(position-1).split("_")[1]);
                            List<String> paginas = new ArrayList<>();
                            for(int i = 0; i < qtdPaginas; i++){
                                paginas.add(((i < 10) ? "0":"")+i);
                            }
                            AdapterCapitulo adapterCapitulo = new AdapterCapitulo(CapituloActivity.this, paginas, titulo, position.toString());

                            paginasRecyclerView.setAdapter(adapterCapitulo);
                            paginasRecyclerView.setLayoutManager(new LinearLayoutManager(CapituloActivity.this));
                            paginasRecyclerView.setHasFixedSize(true);

                        }
                    }
                });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY){

            try {

                if(event.values[0] < 8){
                    Toast.makeText(this, "Mantenha distância da tela para não prejudicar sua visão", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // TODO document why this method is empty
    }

}
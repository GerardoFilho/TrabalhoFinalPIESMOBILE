package br.com.quixada.aniheart.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import br.com.quixada.aniheart.R;
import br.com.quixada.aniheart.adapter.AdapterManga;
import br.com.quixada.aniheart.model.Manga;
import br.com.quixada.aniheart.persistence.ContextoLocalDataSource;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Button btnLogout;
    TextView txtProfileEmail;
    RecyclerView recyclerView_mangas;


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        txtProfileEmail = findViewById(R.id.txtProfileEmail);
        recyclerView_mangas = findViewById(R.id.recyclerView_mangas);

        /*************** Hooks **************/
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar_home);

        /*************** Tool Bar **************/
        setSupportActionBar(toolbar);

        /********************* Navigation Drawer Menu ******************************/
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawe_open, R.string.navigation_drawe_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        listarMangas();

    }

    private void logout(){

        String email = ContextoLocalDataSource.getEmail(PrincipalActivity.this);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios_ativos").document(email)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Log.d("TAG", "DocumentSnapshot successfully deleted!");
                    ContextoLocalDataSource.limparContexto(PrincipalActivity.this);
                    FirebaseAuth.getInstance().signOut();
                    closePrincipal();
                })
                .addOnFailureListener(e -> Log.w("TAG", "Error deleting document", e));
    }

    private void listarMangas() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("mangas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Manga> mangas = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mangas.add(document.toObject(Manga.class));
                        }

                        AdapterManga adapterManga = new AdapterManga(PrincipalActivity.this, mangas);

                        recyclerView_mangas.setAdapter(adapterManga);
                        recyclerView_mangas.setLayoutManager(new LinearLayoutManager(PrincipalActivity.this));
                        recyclerView_mangas.setHasFixedSize(true);

                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }


    private void closePrincipal(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void openUserActivity(){
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.nav_perfil:
                openUserActivity();
                break;
            case R.id.nav_logout:
                logout();
                break;
            default:
                break;
        }

        return true;
    }
}


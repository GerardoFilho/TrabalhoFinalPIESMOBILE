package br.com.quixada.aniheart.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import br.com.quixada.aniheart.R;
import br.com.quixada.aniheart.adapter.AdapterMensagem;
import br.com.quixada.aniheart.model.Mensagem;
import br.com.quixada.aniheart.persistence.ContextoLocalDataSource;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MangaActivity extends AppCompatActivity {

    static final String TITULO = "titulo";

    ImageView imgViewManga;
    TextView txtTitulo;
    TextView txtAutores;
    TextView txtCapituloAtual;
    EditText edtMensagem;
    FloatingActionButton btnEnviarMensagem;
    RecyclerView recyclerView_mensagens;
    Spinner spinnerCapitulos;
    Button btnStudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga);

        txtTitulo = findViewById(R.id.txtTitulo);
        txtAutores = findViewById(R.id.txtAutor);

        imgViewManga = findViewById(R.id.imageManga);
        edtMensagem = findViewById(R.id.edtMessage);
        btnEnviarMensagem = findViewById(R.id.btnEnviarMensagem);
        spinnerCapitulos = findViewById(R.id.spinnerCapitulos);

        btnStudio = findViewById(R.id.btnStudio);

        txtTitulo.setText(getIntent().getStringExtra(TITULO));
        txtAutores.setText(getIntent().getStringExtra("autores"));

        btnStudio.setText(getIntent().getStringExtra("nome_estudio"));

        recyclerView_mensagens = findViewById(R.id.recyclerView_mensagens);

        btnEnviarMensagem.setOnClickListener(view -> {
            String mensagem = edtMensagem.getText().toString();
            if (!mensagem.equals("")) {
                adicionarMensagem(mensagem, txtTitulo.getText().toString());
            }
        });
        spinnerCapitulos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    Intent intent = new Intent(getApplicationContext(), CapituloActivity.class);
                    intent.putExtra("capitulo", position);
                    intent.putExtra(TITULO, txtTitulo.getText().toString());
                    startActivity(intent);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO document why this method is empty
            }
        });

        btnStudio.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            intent.putExtra(TITULO, txtTitulo.getText().toString());
            startActivity(intent);
        });



        Picasso.get().load(getIntent().getStringExtra("linkImage")).into(imgViewManga);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Integer qtdCapitulos = getIntent().getIntExtra("capitulos", 0);
        List<String> capitulos = new ArrayList<>();
        capitulos.add("     ");
        for(int i = 1; i <= qtdCapitulos; i++){
            capitulos.add(((i < 10) ? "0":"")+i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<> ( this, android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, capitulos );
        spinnerCapitulos.setAdapter(adapter);

        recyclerView_mensagens = findViewById(R.id.recyclerView_mensagens);

        carregarMensagens(getIntent().getStringExtra(TITULO));
    }

    private void carregarMensagens(String titulo) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("mangas").document(titulo).collection("mensagens").orderBy("timestamp", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Mensagem> mensagens = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            mensagens.add(document.toObject(Mensagem.class));
                        }

                        AdapterMensagem adapterMensagem = new AdapterMensagem(MangaActivity.this, mensagens);

                        recyclerView_mensagens.setAdapter(adapterMensagem);
                        recyclerView_mensagens.setLayoutManager(new LinearLayoutManager(MangaActivity.this));
                        recyclerView_mensagens.setHasFixedSize(true);

                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void adicionarMensagem(String mensagem, String titulo){
        Mensagem msg = new Mensagem(mensagem, ContextoLocalDataSource.getName(this), "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("mangas").document(titulo).collection("mensagens")
                .add(msg)
                .addOnSuccessListener(documentReference -> {
                    edtMensagem.setText("");
                    carregarMensagens(getIntent().getStringExtra(TITULO));
                })
                .addOnFailureListener(e -> Log.w("TAG", "Error adding document", e));
    }

    private void editarMensagem(String mensagem, String titulo, int ID){
        Mensagem msg = new Mensagem(mensagem, ContextoLocalDataSource.getName(this), "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("mangas").document(titulo).collection("mensagens")
                .add(msg)
                .addOnSuccessListener(documentReference -> {
                    edtMensagem.setText("");
                    carregarMensagens(getIntent().getStringExtra(TITULO));
                })
                .addOnFailureListener(e -> Log.w("TAG", "Error adding document", e));
    }

}
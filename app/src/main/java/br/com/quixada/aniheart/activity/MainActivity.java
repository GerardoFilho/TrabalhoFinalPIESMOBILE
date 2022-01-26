package br.com.quixada.aniheart.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import br.com.quixada.aniheart.R;
import br.com.quixada.aniheart.model.Usuario;
import br.com.quixada.aniheart.model.UsuarioAtivo;
import br.com.quixada.aniheart.persistence.ContextoLocalDataSource;

public class MainActivity extends AppCompatActivity {

    private EditText edtEmail;
    private EditText edtPassword;

    // firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        Button btnRegister = (Button) findViewById(R.id.btnRegister);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        btnLogin.setOnClickListener(view -> {
            String email = edtEmail.getText().toString();
            String password = edtPassword.getText().toString();
            if(!email.equals("")  && !password.equals("")){
                verifyLogin(email, password);
            }

        });

        btnCancel.setOnClickListener(view -> {
            edtEmail.setText("");
            edtPassword.setText("");
        });

        btnRegister.setOnClickListener(view -> openRegisterActivity());

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(userConnected()){
            openPrincipalActivity();
        }
    }

    private void loginUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success");

                    openPrincipalActivity();

                    salvarUsuarioAtivo(email);

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithEmail:failure", task.getException());
                    Toast.makeText(MainActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            });
    }

    private boolean userConnected(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    private void openPrincipalActivity(){
        Intent intent = new Intent(getApplicationContext(), PrincipalActivity.class);
        startActivity(intent);
        finish();
    }

    private void openRegisterActivity(){
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void salvarUsuarioAtivo(String email){
        UsuarioAtivo usuarioAtivo = new UsuarioAtivo(email, System.currentTimeMillis());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuarios_ativos").document(email)
                .set(usuarioAtivo)
                .addOnSuccessListener(aVoid -> {
                    Log.d("TAG", "DocumentSnapshot successfully written!");

                    ContextoLocalDataSource.setEmail(email, MainActivity.this);
                    buscarUsername();
                })
                .addOnFailureListener(e -> Log.w("TAG", "Error writing document", e));
    }

    private void buscarUsername(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .whereEqualTo("email", ContextoLocalDataSource.getEmail(this))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Usuario> usuarios = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            usuarios.add(document.toObject(Usuario.class));
                        }
                        ContextoLocalDataSource.setName(usuarios.get(0).getName(), MainActivity.this);

                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void verifyLogin(String email, String password){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("usuarios_ativos").document(email);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Toast.makeText(MainActivity.this, "Email já está em uso.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password);
                }
            } else {
                Log.d("TAG", "get failed with ", task.getException());
            }
        });
    }

}
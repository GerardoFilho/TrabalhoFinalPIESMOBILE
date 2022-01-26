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
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.quixada.aniheart.R;
import br.com.quixada.aniheart.model.Usuario;
import br.com.quixada.aniheart.model.UsuarioAtivo;
import br.com.quixada.aniheart.persistence.ContextoLocalDataSource;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister;
    EditText edtName;
    EditText edtEmail;
    EditText edtPassword;

    // firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();

        btnRegister = findViewById(R.id.btnRegister);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);

        btnRegister.setOnClickListener(view -> {
            String email = edtEmail.getText().toString();
            String name = edtName.getText().toString();
            String password = edtPassword.getText().toString();
            if(!email.equals("")  && !password.equals("") && !name.equals("")){
                registrarUsuario(name, email, password);
            }
        });
    }

    private void registrarUsuario(String name, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "createUserWithEmail:success");
                        salvarUsuario(new Usuario(email, name));

                    } else {

                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                    openLoginActivity();
                });
    }

    private void salvarUsuario(Usuario usuario){

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuarios")
                .document(usuario.getEmail())
                .set(usuario)
                .addOnSuccessListener(aVoid -> salvarUsuarioAtivo(usuario))
                .addOnFailureListener(e -> Log.w("TAG", "Error adding document", e));

    }

    private void salvarUsuarioAtivo(Usuario usuario){
        String email = usuario.getEmail();
        UsuarioAtivo usuarioAtivo = new UsuarioAtivo(email, System.currentTimeMillis());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("usuarios_ativos").document(email)
                .set(usuarioAtivo)
                .addOnSuccessListener(aVoid -> {
                    Log.d("TAG", "DocumentSnapshot successfully written!");

                    ContextoLocalDataSource.setEmail(email, RegisterActivity.this);
                    ContextoLocalDataSource.setName(usuario.getName(), RegisterActivity.this);
                })
                .addOnFailureListener(e -> Log.w("TAG", "Error writing document", e));
    }

    private void openLoginActivity(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

}
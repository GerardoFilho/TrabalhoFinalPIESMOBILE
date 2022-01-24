package br.com.quixada.aniheart.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.quixada.aniheart.R;
import br.com.quixada.aniheart.model.Usuario;
import br.com.quixada.aniheart.persistence.ContextoLocalDataSource;

public class UserActivity extends AppCompatActivity {

    EditText edtUsername;
    TextView txtEmail;
    Button btnCamera, btnEditar;
    ImageView imgFotoPerfil;
    private static final int REQUEST_IMAGE_CAPTURE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        edtUsername = findViewById(R.id.edtUsername);
        txtEmail = findViewById(R.id.txtEmail);
        btnCamera = findViewById(R.id.btnCamera);
        btnEditar = findViewById(R.id.btnEditar);
        imgFotoPerfil = findViewById(R.id.imgFotoPerfil);

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtUsername.getText() != null && !"".equals(edtUsername.getText().toString())
                    && !edtUsername.getText().toString().equals(ContextoLocalDataSource.getName(UserActivity.this))){
                    editarUsuario(edtUsername.getText().toString());
                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture(view);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String username = ContextoLocalDataSource.getName(this);
        edtUsername.setText(username);
        txtEmail.setText(ContextoLocalDataSource.getEmail(this));
        carregarFoto();
        //takeUrl();
    }

    private void carregarFoto(){

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("usuarios")
                .whereEqualTo("email", ContextoLocalDataSource.getEmail(this))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Usuario> usuarios = new ArrayList<Usuario>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                usuarios.add(document.toObject(Usuario.class));
                            }

                            if(!usuarios.get(0).getUrl().equals("")){
                                Picasso.get().load(usuarios.get(0).getUrl()).into(imgFotoPerfil);
                            }

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void editarUsuario(String username){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref = db.collection("usuarios").document(ContextoLocalDataSource.getEmail(UserActivity.this));


        ref
            .update("name", username)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    ContextoLocalDataSource.setName(username, UserActivity.this);
                    edtUsername.setText(username);
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("TAG", "Error updating document", e);
                }
            });
    }

    public void takePicture(View view){

        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(imageTakeIntent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            urlImagem(bitmap);
        }
    }

    private void salvarImagem(Bitmap bitmap){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("fotos_perfil/" + ContextoLocalDataSource.getEmail(this) + "_foto_perfil.jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                UploadTask.TaskSnapshot t = taskSnapshot;
            }
        });

    }

    private void urlImagem(Bitmap bitmap) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("fotos_perfil/" + ContextoLocalDataSource.getEmail(this) + "_foto_perfil.jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(data);

        //final StorageReference ref = storageRef.child("images/mountains.jpg");
        //uploadTask = ref.putFile(file);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                   //
                    // Uri downloadUri = task.getResult();
                    String url = task.getResult().toString();
                    adicionarUrl(url);
                    imgFotoPerfil.setImageBitmap(bitmap);

                } else {
                    // Handle failures
                    // ...
                }
            }
        });

    }

    private void adicionarUrl(String url){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference ref= db.collection("usuarios").document(ContextoLocalDataSource.getEmail(UserActivity.this));


        ref
                .update("url", url)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UserActivity.this, "URL adicionada com sucesso !!!",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error updating document", e);
                    }
                });
    }

}
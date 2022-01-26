 package br.com.quixada.aniheart.adapter;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.quixada.aniheart.R;


 public class AdapterCapitulo extends RecyclerView.Adapter<AdapterCapitulo.CapituloViewHolder> {

    List<String> paginas;
    String titulo;
     String capitulo;
    Context context;


    public AdapterCapitulo(Context context, List<String> paginas, String titulo, String capitulo) {
        this.context = context;
        this.paginas = paginas;
        this.titulo = titulo;
        this.capitulo = capitulo;
    }

    @NonNull
    @Override
    public CapituloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.capitulo_item, parent, false);

        return new CapituloViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull CapituloViewHolder holder, int position) {

        takeUrl(paginas.get(position), holder);

    }

    @Override
    public int getItemCount() {

        return paginas.size();
    }

    public class CapituloViewHolder extends RecyclerView.ViewHolder{

        ImageView imgCapitulo;

        public CapituloViewHolder(@NonNull View itemView) {

            super(itemView);

            imgCapitulo = itemView.findViewById(R.id.imgCapitulo);

        }
    }

     private void takeUrl(String pagina, CapituloViewHolder holder){
         FirebaseStorage storage = FirebaseStorage.getInstance();
         StorageReference storageRef = storage.getReference();

        String caminho = titulo.toLowerCase().replace(" ", "_")+"/"+capitulo+"/"+pagina+".jpg";

         storageRef.child(caminho).getDownloadUrl().addOnSuccessListener(uri -> Picasso.get().load(uri).into(holder.imgCapitulo)).addOnFailureListener(exception -> {
             // Handle any errors
         });
     }

}

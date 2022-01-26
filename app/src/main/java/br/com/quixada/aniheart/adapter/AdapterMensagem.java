package br.com.quixada.aniheart.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.quixada.aniheart.R;
import br.com.quixada.aniheart.model.Mensagem;
import br.com.quixada.aniheart.model.Usuario;

public class AdapterMensagem extends RecyclerView.Adapter<AdapterMensagem.MensagemViewHolder> {

    List<Mensagem> mensagemList;
    Context context;

    public AdapterMensagem(Context context, List<Mensagem> mensagemList) {
        this.context = context;
        this.mensagemList = mensagemList;
    }

    @NonNull
    @Override
    public AdapterMensagem.MensagemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.mensagem_item, parent, false);

        return new AdapterMensagem.MensagemViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMensagem.MensagemViewHolder holder, int position) {
        holder.txtAutorMensagem.setText(mensagemList.get(position).getEmail());
        holder.txtMensagem.setText(mensagemList.get(position).getMessenger());
        carregarFoto(mensagemList.get(position).getEmail(), holder.imgFotoPerfilMensagem);

    }

    @Override
    public int getItemCount() {

        return mensagemList.size();
    }

    private void carregarFoto(String email, ImageView image){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("usuarios").document(email);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();

                if (document.exists()) {
                    Usuario usuario = document.toObject(Usuario.class);
                    if(!usuario.getUrl().equals("")){
                        Picasso.get().load(usuario.getUrl()).into(image);
                    }

                }
            } else {
                Log.d("TAG", "get failed with ", task.getException());
            }
        });
    }

    public class MensagemViewHolder extends RecyclerView.ViewHolder{

        TextView txtAutorMensagem;
        TextView txtMensagem;
        ImageView imgFotoPerfilMensagem;

        public MensagemViewHolder(@NonNull View itemView) {

            super(itemView);

            txtMensagem = itemView.findViewById(R.id.txtMensagem);
            txtAutorMensagem = itemView.findViewById(R.id.txtAutorMensagem);
            imgFotoPerfilMensagem = itemView.findViewById(R.id.imgFotoPerfilMensagem);


        }
    }
}
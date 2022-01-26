 package br.com.quixada.aniheart.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.quixada.aniheart.R;
import br.com.quixada.aniheart.activity.MangaActivity;
import br.com.quixada.aniheart.model.Manga;


 public class AdapterManga extends RecyclerView.Adapter<AdapterManga.MangaViewHolder> {

    List<Manga> mangaList;
    Context context;

    public AdapterManga(Context context, List<Manga> mangaList) {
        this.context = context;
        this.mangaList = mangaList;

    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(context).inflate(R.layout.manga_item, parent, false);

        return new MangaViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        holder.txtTitulo.setText(mangaList.get(position).getTitulo());
        holder.txtAutor.setText(mangaList.get(position).getAutores());
        Picasso.get().load(mangaList.get(position).getLinkImage()).into(holder.imageButton);

        holder.imageButton.setOnClickListener(view -> openMangaActivity(mangaList.get(position)));


    }

    @Override
    public int getItemCount() {

        return mangaList.size();
    }

    public class MangaViewHolder extends RecyclerView.ViewHolder{

        ImageButton imageButton;
        TextView txtTitulo;
        TextView txtCapituloAtual;
        TextView txtAutor;


        public MangaViewHolder(@NonNull View itemView) {

            super(itemView);

             imageButton = itemView.findViewById(R.id.buttonManga);
             txtTitulo = itemView.findViewById(R.id.txtTitulo);
             txtAutor = itemView.findViewById(R.id.txtAutor);

        }
    }

     private void openMangaActivity(Manga manga){

         Intent intent = new Intent(context, MangaActivity.class);
         intent.putExtra("titulo", manga.getTitulo());
         intent.putExtra("autores", manga.getAutores());

         if(manga.getStudio() != null){
             intent.putExtra("nome_estudio", manga.getStudio().getNome());
         }else{
             intent.putExtra("nome_estudio", "Studio");
         }

         try{
             intent.putExtra("capitulos", manga.getCapitulos().size());
         }catch (Exception e){
             intent.putExtra("capitulos", 0);
         }

         intent.putExtra("linkImage", manga.getLinkImage());

         context.startActivity(intent);
     }

}

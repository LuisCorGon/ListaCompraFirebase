package com.luiscortes.firebaselistacompra.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.luiscortes.firebaselistacompra.R;
import com.luiscortes.firebaselistacompra.interfaces.OnCategoriaClickListener;
import com.luiscortes.firebaselistacompra.models.Categoria;
import com.luiscortes.firebaselistacompra.models.Producto;

import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private List<Categoria> categorias;
    private OnCategoriaClickListener listener;
    public CategoriaAdapter(List<Categoria> categorias) {

        this.categorias = categorias;
    }

    public void setOnCategoriaClickListener(OnCategoriaClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.bindCategoria(categorias.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombreCategoria = categorias.get(position).getNombre();
                if (listener != null){
                    listener.onCategoriaClick(nombreCategoria);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return categorias.size();
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categoria, parent,false);
        return new CategoriaViewHolder(v);
    }

    class CategoriaViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNombre;
        private ImageView ivImagen;

        private CardView cardView;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreCategoria);
            ivImagen = itemView.findViewById(R.id.ivFotoCategoria);
            cardView = itemView.findViewById(R.id.cvCategoria);
        }

        public void bindCategoria(Categoria categoria) {
            tvNombre.setText(categoria.getNombre());
            int idImagen = itemView.getContext().getResources().getIdentifier(categoria.getImagen(), "raw", itemView.getContext().getPackageName());
            ivImagen.setImageResource(idImagen);
        }
    }
}

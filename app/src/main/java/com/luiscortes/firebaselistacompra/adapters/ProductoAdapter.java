package com.luiscortes.firebaselistacompra.adapters;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.luiscortes.firebaselistacompra.R;
import com.luiscortes.firebaselistacompra.interfaces.OnItemClickListener;
import com.luiscortes.firebaselistacompra.models.Producto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class ProductoAdapter extends FirestoreRecyclerAdapter<Producto, ProductoAdapter.ProductoViewHolder> {

    private OnItemClickListener listener;
    private final Set<String> productosClickados;


    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductoAdapter(@NonNull FirestoreRecyclerOptions<Producto> options) {
        super(options);
        this.productosClickados = new HashSet<>();

    }


    @Override
    protected void onBindViewHolder(@NonNull ProductoViewHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Producto model) {
        holder.bindProduct(model);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null && !productosClickados.contains(model.getNombre())){
                    listener.onItemClick(model);
                    Log.d("PRODUCTO MODEL", model.toString());
                    holder.cardView.setAlpha(0.5f);
                    productosClickados.add(model.getNombre());
                } else if (productosClickados.contains(model.getNombre())){
                    Toast.makeText(v.getContext(), "Ya has guardado este producto!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent,false);
        return new ProductoViewHolder(v);
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNombre;
        private ImageView ivImagen;
        private CardView cardView;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            ivImagen = itemView.findViewById(R.id.ivFotoProducto);
            cardView = itemView.findViewById(R.id.cvItem);
        }

        public void bindProduct(Producto producto){
            tvNombre.setText(producto.getNombre());
            int idImagen = itemView.getContext().getResources().getIdentifier(producto.getImagen(),"raw",itemView.getContext().getPackageName());
            ivImagen.setImageResource(idImagen);
        }
    }
}
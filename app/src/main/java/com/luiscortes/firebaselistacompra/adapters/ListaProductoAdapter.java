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
import com.luiscortes.firebaselistacompra.models.Lista;
import com.luiscortes.firebaselistacompra.models.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListaProductoAdapter extends RecyclerView.Adapter<ListaProductoAdapter.ProductoViewHolder> {

    private List<Producto> productos;
    private OnItemClickListener listener;

    public ListaProductoAdapter(List<Producto> productos) {
        this.productos = productos;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_producto, parent, false);
        return new ProductoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
        holder.bindProduct(productos.get(position));
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(productos.get(position));
                holder.cardView.setAlpha(0.5f);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {

        private TextView tvNombre;
        private ImageView ivImagen;
        private CardView cardView;

        public ProductoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombreProducto);
            ivImagen = itemView.findViewById(R.id.ivFotoProducto);
            cardView = itemView.findViewById(R.id.cvItem);
        }

        public void bindProduct(Producto producto) {
            tvNombre.setText(producto.getNombre());
            int idImagen = itemView.getContext().getResources().getIdentifier(producto.getImagen(), "raw", itemView.getContext().getPackageName());
            ivImagen.setImageResource(idImagen);
        }
    }
}
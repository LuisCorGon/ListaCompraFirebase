package com.luiscortes.firebaselistacompra.adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.luiscortes.firebaselistacompra.R;
import com.luiscortes.firebaselistacompra.interfaces.OnAddProductosClickListener;
import com.luiscortes.firebaselistacompra.interfaces.OnItemClickListener;
import com.luiscortes.firebaselistacompra.models.Lista;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.luiscortes.firebaselistacompra.models.Producto;

import java.util.ArrayList;
import java.util.List;

public class ListaUsuarioAdapter extends  FirestoreRecyclerAdapter<Lista, ListaUsuarioAdapter.ListaUsuarioViewHolder> {

    private OnAddProductosClickListener listener;

    public void setOnAddProductosClickListener(OnAddProductosClickListener listener){
        this.listener = listener;
    }


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    public ListaUsuarioAdapter(@NonNull FirestoreRecyclerOptions<Lista> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ListaUsuarioViewHolder holder, int position, @NonNull Lista model) {
        holder.bindListas(model);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onAddProductosListener(model);
            }
        });
    }

    @NonNull
    @Override
    public ListaUsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_listas, parent, false);
        return new ListaUsuarioViewHolder(v);
    }

    class ListaUsuarioViewHolder extends RecyclerView.ViewHolder {

        private TextView nombre;
        private TextView fecha;
        private ImageView foto;
        private CardView cardView;
        public ListaUsuarioViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.tvNombreListaCv);
            fecha = itemView.findViewById(R.id.tvFechHoraCv);
            foto = itemView.findViewById(R.id.ivFotoUsuario);
            cardView = itemView.findViewById(R.id.cvLista);
        }


        public void bindListas(Lista lista) {
            nombre.setText(lista.getNombre());
            fecha.setText(lista.getFechaHora());
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(itemView.getContext());
            if (account != null) {
                String photoUrl = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : null;
                Glide.with(itemView.getContext())
                        .load(photoUrl)
                        .into(foto);

            }
        }
    }
}

package com.luiscortes.firebaselistacompra.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.luiscortes.firebaselistacompra.MainActivity;
import com.luiscortes.firebaselistacompra.R;
import com.luiscortes.firebaselistacompra.adapters.ProductoAdapter;
import com.luiscortes.firebaselistacompra.interfaces.OnCategoriaClickListener;
import com.luiscortes.firebaselistacompra.interfaces.OnItemClickListener;
import com.luiscortes.firebaselistacompra.models.Lista;
import com.luiscortes.firebaselistacompra.models.Producto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ProductoFragment extends Fragment implements OnItemClickListener{

    private ProductoAdapter adapter;
    private String nombreCategoria;
    private String nombreLista;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;

    private List<Producto> productosActuales;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_productos, container, false);

        RecyclerView recyclerView = v.findViewById(R.id.rvProductos);
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (getArguments() != null) {
            nombreCategoria = getArguments().getString("categoria");
            nombreLista = getArguments().getString("nombreLista");
        }


        if (nombreCategoria != null) {
            Query query = db.collection("producto").whereEqualTo("categoria.nombre", nombreCategoria);
            FirestoreRecyclerOptions<Producto> opciones =
                    new FirestoreRecyclerOptions.Builder<Producto>()
                            .setQuery(query, Producto.class)
                            .build();
            adapter = new ProductoAdapter(opciones);
            adapter.setOnItemClickListener(this);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            adapter.startListening();

        }

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null){
            adapter.stopListening();
        }
    }

    @Override
    public void onItemClick(Producto producto) {
        db.collection("lista")
                .whereEqualTo("nombre", nombreLista).whereArrayContains("correosUsuarios", firebaseUser.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String listaId = document.getId();
                            DocumentReference listaRef = db.collection("lista").document(listaId);

                            productosActuales = (List<Producto>) document.get("productos");

                            if (productosActuales == null) {
                                productosActuales = new ArrayList<>();
                            }
                            productosActuales.add(producto);

                            listaRef.update("productos", productosActuales)
                                    .addOnSuccessListener(unused -> Log.d("Firestore", "Documento de lista actualizado correctamente"))
                                    .addOnFailureListener(e -> Log.e("Error al actualizar el documento de lista", e.toString()));
                        }
                    } else {
                        Log.e("Error al obtener documentos", task.getException().toString());
                    }
                });
    }

}
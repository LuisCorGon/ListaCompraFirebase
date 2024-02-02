package com.luiscortes.firebaselistacompra.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.luiscortes.firebaselistacompra.R;
import com.luiscortes.firebaselistacompra.adapters.ListaProductoAdapter;
import com.luiscortes.firebaselistacompra.adapters.ListaUsuarioAdapter;
import com.luiscortes.firebaselistacompra.adapters.ProductoAdapter;
import com.luiscortes.firebaselistacompra.interfaces.OnItemClickListener;
import com.luiscortes.firebaselistacompra.models.Lista;
import com.luiscortes.firebaselistacompra.models.Producto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaProductoFragment extends Fragment {

    private String nombreLista;
    private FirebaseUser firebaseUser;
    private ListaProductoAdapter adapter;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_producto, container, false);
        TextView tvNombreLista = v.findViewById(R.id.tvNombreLista);
        RecyclerView rvListadoLista = v.findViewById(R.id.rvListaProducto);
        FloatingActionButton addProductos = v.findViewById(R.id.fabAddProducto);
        db = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        nombreLista = getArguments().getString("nombreLista");
        tvNombreLista.setText(nombreLista);

        CollectionReference listaRef = db.collection("lista");
        Query listaQuery = listaRef
                .whereEqualTo("nombre", nombreLista)
                .whereArrayContains("correosUsuarios", firebaseUser.getEmail());

        listaQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Lista lista = document.toObject(Lista.class);
                    if (lista.getProductos()!=null){
                        Log.d("LISTA", lista.getProductos().toString());
                        setupRecyclerView(rvListadoLista, lista.getProductos());
                    } else {
                        Toast.makeText(getContext(), "Todavia no tienes productos en tu lista", Toast.LENGTH_SHORT).show();
                    }

                }
            } else {
                Log.e("Error al obtener documentos", task.getException().toString());
            }
        });

        addProductos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoriaFragment categoriaFragment = new CategoriaFragment();
                Bundle args = new Bundle();
                args.putString("nombreLista", nombreLista);
                categoriaFragment.setArguments(args);
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fcvMain, categoriaFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return v;
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<Producto> productos) {
        if (productos != null) {
            ListaProductoAdapter adapter = new ListaProductoAdapter(productos);

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

            adapter.setOnItemClickListener(producto -> {
                db.collection("lista")
                        .whereEqualTo("nombre", nombreLista)
                        .whereArrayContains("correosUsuarios", firebaseUser.getEmail())
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    String listaId = document.getId();
                                    DocumentReference listaRef = db.collection("lista").document(listaId);

                                    Map<String, Object> updates = new HashMap<>();
                                    updates.put("productos", FieldValue.delete());

                                    listaRef.update(updates)
                                            .addOnSuccessListener(unused -> Log.d("Firestore", "Campo 'productos' eliminado correctamente"))
                                            .addOnFailureListener(e -> Log.e("Error al eliminar campo 'productos'", e.toString()));
                                }
                            } else {
                                Log.e("Error al obtener documentos", task.getException().toString());
                            }
                        });
                Log.d("Producto Click", "Nombre: " + producto.getNombre());
            });
        }
    }

}

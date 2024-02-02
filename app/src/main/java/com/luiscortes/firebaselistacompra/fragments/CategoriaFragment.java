package com.luiscortes.firebaselistacompra.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.luiscortes.firebaselistacompra.R;
import com.luiscortes.firebaselistacompra.adapters.CategoriaAdapter;
import com.luiscortes.firebaselistacompra.adapters.ProductoAdapter;
import com.luiscortes.firebaselistacompra.interfaces.OnCategoriaClickListener;
import com.luiscortes.firebaselistacompra.models.Categoria;
import com.luiscortes.firebaselistacompra.models.Producto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CategoriaFragment extends Fragment implements OnCategoriaClickListener {
    private  RecyclerView recyclerView;

    private String nombreLista;
    private CollectionReference ref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_categorias, container, false);

        recyclerView = v.findViewById(R.id.rvCategoria);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (getArguments() !=null){
           nombreLista = getArguments().getString("nombreLista");
        }

        ref = db.collection("producto");

        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Set<String> categoriasSet = new HashSet<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> categoriaMap = (Map<String, Object>) document.get("categoria");
                        if (categoriaMap != null) {
                            String nombreCategoria = (String) categoriaMap.get("nombre");
                            if (nombreCategoria != null) {
                                categoriasSet.add(nombreCategoria);
                            }
                        }
                    }
                    setupRecyclerView(new ArrayList<>(categoriasSet));
                } else {
                    Log.e("Error al obtener documentos: ", String.valueOf(task.getException()));
                }
            }
        });
        return v;


    }

    private void setupRecyclerView(List<String> categorias){
        List<Categoria> listaCategorias = new ArrayList<>();

        for (String nombreCategoria : categorias) {
            Categoria categoria = new Categoria(nombreCategoria);
            listaCategorias.add(categoria);
        }

        CategoriaAdapter adapter = new CategoriaAdapter(listaCategorias);
        adapter.setOnCategoriaClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    @Override
    public void onCategoriaClick(String nombreCategoria) {
        ProductoFragment fragment = new ProductoFragment();
        Bundle args = new Bundle();
        args.putString("categoria", nombreCategoria);
        args.putString("nombreLista", nombreLista);
        fragment.setArguments(args);
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fcvMain, fragment);
        transaction.addToBackStack("categorias");
        transaction.commit();
    }
}

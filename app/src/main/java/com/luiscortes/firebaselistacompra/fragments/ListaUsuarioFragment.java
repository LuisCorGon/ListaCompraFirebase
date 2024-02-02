package com.luiscortes.firebaselistacompra.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.luiscortes.firebaselistacompra.MainActivity;
import com.luiscortes.firebaselistacompra.R;
import com.luiscortes.firebaselistacompra.adapters.ListaUsuarioAdapter;
import com.luiscortes.firebaselistacompra.interfaces.OnAddProductosClickListener;
import com.luiscortes.firebaselistacompra.models.Lista;

import java.util.ArrayList;
import java.util.Calendar;

public class ListaUsuarioFragment extends Fragment implements OnAddProductosClickListener {


    private ListaUsuarioAdapter adapter;
    private FirebaseUser firebaseUser;
    private final ArrayList<String> correos = new ArrayList<>();



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cambiarNombreUsuario();
    }

    private void cambiarNombreUsuario(){
        TextView nombreUsuario = getView().findViewById(R.id.tvNombreUsuarioListas);
        try {
            if (firebaseUser!= null){
                nombreUsuario.setText(firebaseUser.getDisplayName());
            } else {
                nombreUsuario.setText("Usuario Invitado");
            }

        } catch (Exception e){
            Log.e("Error", "Error al obtener el nombre de usuario: " + e.getMessage());
            nombreUsuario.setText("Usuario Invitado");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_listas, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        RecyclerView recyclerView = v.findViewById(R.id.rvListas);
        FirebaseFirestore db = FirebaseFirestore.getInstance();



        FloatingActionButton addLista = v.findViewById(R.id.fabAddLista);


        String userEmail = firebaseUser.getEmail();
        if (!correos.contains(userEmail)){
            correos.add(userEmail);
        }

        Query query = db.collection("lista").whereArrayContains("correosUsuarios", userEmail);
        FirestoreRecyclerOptions<Lista> opciones =
                new FirestoreRecyclerOptions.Builder<Lista>()
                        .setQuery(query, Lista.class)
                        .build();

        adapter = new ListaUsuarioAdapter(opciones);
        adapter.setOnAddProductosClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adapter.startListening();

        addLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Nuevo Nombre de Lista");
                EditText input = new EditText(requireContext());
                builder.setView(input);

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nombreListaIntro = input.getText().toString().trim();
                        if (nombreListaIntro.isEmpty()) {
                            Toast.makeText(requireContext(), "Por favor, ingrese un nombre válido.", Toast.LENGTH_SHORT).show();
                        } else {
                            db.collection("lista").whereEqualTo("nombre", nombreListaIntro).whereArrayContains("correosUsuarios", userEmail).get().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    QuerySnapshot querySnapshot = task.getResult();
                                    if (querySnapshot.isEmpty()) {
                                        Lista lista = new Lista(nombreListaIntro, Calendar.getInstance().getTime().toString(), null, correos);
                                        db.collection("lista").add(lista)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Log.d(MainActivity.class.getSimpleName(), "Lista añadida " +
                                                                "correctamente con id: " + documentReference.getId());
                                                    }

                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.e(MainActivity.class.getSimpleName(), "Error al añadir la lista");
                                                        Log.e(MainActivity.class.getSimpleName(), e.getMessage());

                                                    }
                                                });

                                    } else {
                                        Toast.makeText(requireContext(), "Ya se encuentra una lista con ese nombre.", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(requireContext(), "Error al realizar la consulta.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (adapter != null) {
            adapter.stopListening();
        }
    }


    @Override
    public void onAddProductosListener(Lista lista) {
        ListaProductoFragment listaProductoFragment = new ListaProductoFragment();
        Bundle args = new Bundle();
        args.putString("nombreLista", lista.getNombre());
        listaProductoFragment.setArguments(args);
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fcvMain, listaProductoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

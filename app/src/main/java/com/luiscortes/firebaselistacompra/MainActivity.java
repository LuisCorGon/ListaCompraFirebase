package com.luiscortes.firebaselistacompra;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firestore.v1.FirestoreGrpc;
import com.luiscortes.firebaselistacompra.adapters.ListaUsuarioAdapter;
import com.luiscortes.firebaselistacompra.adapters.ProductoAdapter;
import com.luiscortes.firebaselistacompra.fragments.BienvenidaFragment;
import com.luiscortes.firebaselistacompra.fragments.CategoriaFragment;
import com.luiscortes.firebaselistacompra.fragments.ListaUsuarioFragment;
import com.luiscortes.firebaselistacompra.fragments.ProductoFragment;
import com.luiscortes.firebaselistacompra.models.Categoria;
import com.luiscortes.firebaselistacompra.models.Lista;
import com.luiscortes.firebaselistacompra.models.Producto;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        if (firebaseUser != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fcvMain, ListaUsuarioFragment.class, null)
                    .commit();

        } else {
            Toast.makeText(this, "Usuario desconocido", Toast.LENGTH_SHORT).show();
            ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult o) {
                            if (o.getResultCode() == RESULT_OK) {
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                Toast.makeText(MainActivity.this, "Bienvenido " + firebaseUser.getDisplayName(), Toast.LENGTH_SHORT).show();
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fcvMain, ListaUsuarioFragment.class, null)
                                        .commit();
                            } else {
                                Toast.makeText(MainActivity.this, "Acceso denegado", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

            );
            resultLauncher.launch(AuthUI.getInstance().createSignInIntentBuilder().build());
        }

    }
    public void getProductos () {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Categoria categoriaFruta = new Categoria("Fruta", "fruit");
        Categoria categoriaPescado = new Categoria("Pescado", "sea");
        Categoria categoriaCarne = new Categoria("Carne", "meat");
        Categoria categoriaVerdura = new Categoria("Verdura", "vegetable");
        Categoria categoriaDulce = new Categoria("Dulce", "crepe");

        Producto anchoa = new Producto("Anchoa", "anchovy", categoriaPescado);
        Producto atun = new Producto("Atún", "tuna", categoriaPescado);
        Producto zanahoria = new Producto("Zanahoria", "carrot", categoriaVerdura);
        Producto pimiento = new Producto("Pimiento", "food", categoriaVerdura);
        Producto uva = new Producto("Uva", "grapes", categoriaFruta);
        Producto sandia = new Producto("Sandia", "watermelon", categoriaFruta);
        Producto pollo = new Producto("Pollo", "hen", categoriaCarne);
        Producto pavo = new Producto("Pavo", "turkey", categoriaCarne);
        Producto crepe = new Producto("Crepe", "crepechoco", categoriaDulce);
        Producto galleta = new Producto("Galleta", "cookie", categoriaDulce);

        ArrayList<Producto> productos = new ArrayList<>();
        productos.add(anchoa);
        productos.add(atun);
        productos.add(zanahoria);
        productos.add(pimiento);
        productos.add(uva);
        productos.add(sandia);
        productos.add(pollo);
        productos.add(pavo);
        productos.add(crepe);
        productos.add(galleta);

        db.collection("producto").add(productos)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(MainActivity.class.getSimpleName(), "Producto añadido " +
                                "correctamente con id: " + documentReference.getId());
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(MainActivity.class.getSimpleName(), "Error al añadir el producto");
                        Log.e(MainActivity.class.getSimpleName(), e.getMessage());

                    }
                });

    }

}
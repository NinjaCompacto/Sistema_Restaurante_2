package com.example.sistemarestaurante.Funcoes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.hardware.lights.LightsManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.sistemarestaurante.Adapters.MesasCaixaAdapter;
import com.example.sistemarestaurante.Cadastro_e_login.LoginActivity;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CaixaActivity extends AppCompatActivity {
    //XML
    private RecyclerView recyclerMesasCaixa;
    private MesasCaixaAdapter mesasCaixaAdapter;
    //Firebase
    private final DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference mesaref = databaseReference.child("mesas");
    private ValueEventListener valueEventListener;
    private FirebaseAuth auth = ConfiguracaoFirebase.getAuth();
    //Model/Helper
    private List<Mesa> listMesas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caixa);

        recuperarMesas();
        configurarRecycler();

    }

    public void configurarRecycler () {
        recyclerMesasCaixa = findViewById(R.id.recyclerMesasCaixa);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mesasCaixaAdapter = new MesasCaixaAdapter(listMesas,getApplicationContext());
        recyclerMesasCaixa.setHasFixedSize(true);
        recyclerMesasCaixa.setLayoutManager(layoutManager);
        recyclerMesasCaixa.setAdapter(mesasCaixaAdapter);

    }

    public void recuperarMesas () {
        valueEventListener = mesaref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dados : snapshot.getChildren()){
                        listMesas.add(dados.getValue(Mesa.class));
                }
                mesasCaixaAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mesaref.removeEventListener(valueEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //infla menu para a activity CAIXA
        getMenuInflater().inflate(R.menu.menucaixa,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //faz signout do usuario e retorar para a area de login
        switch (item.getItemId()){
            case R.id.menuSair:
                auth.signOut();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
}
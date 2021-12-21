package com.example.sistemarestaurante.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.sistemarestaurante.Adapters.StatusDisponivelAdapter;
import com.example.sistemarestaurante.Adapters.StatusIndisponivelAdapter;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Helper.RecyclerViewClickListener;
import com.example.sistemarestaurante.Model.Prato;
import com.example.sistemarestaurante.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MudarPratoStatusActivity extends AppCompatActivity {

    //XML
    private RecyclerView recyclerDisponivel, recyclerIndisponivel;
    private StatusDisponivelAdapter statusDisponivelAdapter;
    private StatusIndisponivelAdapter statusIndisponivelAdapter;
    private FloatingActionButton fab;

    //firebase
    private final DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference pratosref;

    //model
    private List<Prato> pratosDisponiveis = new ArrayList<>();
    private List<Prato> pratosIndisponiveis = new ArrayList<>();
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mudar_status);

        //configruações iniciais
        recyclerDisponivel = findViewById(R.id.recyclerStatusDisponivel);
        recyclerIndisponivel = findViewById(R.id.recyclerStatusIndisponivel);
        pratosref = databaseReference.child("pratos");
        fab = findViewById(R.id.fabConfirmarStatus);


        //adapter (disponivel)
        recuperarPratos();
        statusDisponivelAdapter =  new StatusDisponivelAdapter(pratosDisponiveis,getApplicationContext());
        //configurando recycler (disponivel)
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerDisponivel.setLayoutManager(layoutManager);
        recyclerDisponivel.setHasFixedSize(true);
        recyclerDisponivel.setAdapter(statusDisponivelAdapter);
        recyclerDisponivel.addOnItemTouchListener( new RecyclerViewClickListener(getApplicationContext(), recyclerDisponivel, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Prato prato = pratosDisponiveis.get(position);
                prato.setIsDisponivel("false");
                pratosDisponiveis.remove(position);
                pratosIndisponiveis.add(prato);
                statusDisponivelAdapter.notifyDataSetChanged();
                pratosref.child(prato.getNomePrato()).setValue(prato);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));


        //configura adapter (indisponivel)
        statusIndisponivelAdapter = new StatusIndisponivelAdapter(pratosIndisponiveis,getApplicationContext());
        //configura recycler (indisponivel)
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        recyclerIndisponivel.setLayoutManager(layoutManager1);
        recyclerIndisponivel.setHasFixedSize(true);
        recyclerIndisponivel.setAdapter(statusIndisponivelAdapter);
        recyclerIndisponivel.addOnItemTouchListener(new RecyclerViewClickListener(getApplicationContext(), recyclerIndisponivel, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Prato prato = pratosIndisponiveis.get(position);
                prato.setIsDisponivel("true");
                pratosIndisponiveis.remove(position);
                pratosDisponiveis.add(prato);
                statusDisponivelAdapter.notifyDataSetChanged();
                statusIndisponivelAdapter.notifyDataSetChanged();
                pratosref.child(prato.getNomePrato()).setValue(prato);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Status salvo !", Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        pratosref.removeEventListener(valueEventListener);
    }

    public void recuperarPratos() {
        valueEventListener = pratosref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //limpa as listas para q n haja repetição de listagem de itens
                    pratosIndisponiveis.clear();
                    pratosDisponiveis.clear();
                for (DataSnapshot dados : snapshot.getChildren()){
                    Prato prato = dados.getValue(Prato.class);
                    if (prato.getIsDisponivel().contains("true")){
                        pratosDisponiveis.add(prato);
                        statusDisponivelAdapter.notifyDataSetChanged();
                    }
                    else {
                        pratosIndisponiveis.add(prato);
                        statusIndisponivelAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
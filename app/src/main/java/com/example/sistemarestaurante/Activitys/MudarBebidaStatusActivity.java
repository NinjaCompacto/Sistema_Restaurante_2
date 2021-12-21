package com.example.sistemarestaurante.Activitys;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemarestaurante.Adapters.StatusDisponivelBebidaAdapter;
import com.example.sistemarestaurante.Adapters.StatusIndisponivelBebidaAdapter;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Helper.RecyclerViewClickListener;
import com.example.sistemarestaurante.Model.Bebida;
import com.example.sistemarestaurante.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MudarBebidaStatusActivity extends AppCompatActivity {

    //XML
    private RecyclerView recyclerDisponivel, recyclerIndisponivel;
    private StatusDisponivelBebidaAdapter statusDisponivelAdapter;
    private StatusIndisponivelBebidaAdapter statusIndisponivelAdapter;
    private FloatingActionButton fab;

    //firebase
    private final DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference bebidassref;

    //model
    private List<Bebida> BebidasDisponiveis = new ArrayList<>();
    private List<Bebida> BebidasIndisponiveis = new ArrayList<>();
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mudar_status_bebida);

        //configruações iniciais
        recyclerDisponivel = findViewById(R.id.recyclerStatusDisponivel);
        recyclerIndisponivel = findViewById(R.id.recyclerStatusIndisponivel);
        bebidassref= databaseReference.child("bebidas");
        fab = findViewById(R.id.fabConfirmarStatus);

        recuperarBebidas();

        //adapter (disponivel)
        statusDisponivelAdapter =  new StatusDisponivelBebidaAdapter(BebidasDisponiveis,getApplicationContext());
        //configurando recycler (disponivel)
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerDisponivel.setLayoutManager(layoutManager);
        recyclerDisponivel.setHasFixedSize(true);
        recyclerDisponivel.setAdapter(statusDisponivelAdapter);
        recyclerDisponivel.addOnItemTouchListener( new RecyclerViewClickListener(getApplicationContext(), recyclerDisponivel, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bebida bebida = BebidasDisponiveis.get(position);
                bebida.setIsDisponivel("false");
                BebidasDisponiveis.remove(position);
                BebidasIndisponiveis.add(bebida);
                statusDisponivelAdapter.notifyDataSetChanged();
                bebidassref.child(bebida.getNomeBebida()).setValue(bebida);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        //configura adapter (indisponivel)
        statusIndisponivelAdapter = new StatusIndisponivelBebidaAdapter(BebidasIndisponiveis,getApplicationContext());
        //configura recycler (indisponivel)
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        recyclerIndisponivel.setLayoutManager(layoutManager1);
        recyclerIndisponivel.setHasFixedSize(true);
        recyclerIndisponivel.setAdapter(statusIndisponivelAdapter);
        recyclerIndisponivel.addOnItemTouchListener(new RecyclerViewClickListener(getApplicationContext(), recyclerIndisponivel, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bebida bebida = BebidasIndisponiveis.get(position);
                bebida.setIsDisponivel("true");
                BebidasIndisponiveis.remove(position);
                BebidasDisponiveis.add(bebida);
                statusDisponivelAdapter.notifyDataSetChanged();
                statusIndisponivelAdapter.notifyDataSetChanged();
                bebidassref.child(bebida.getNomeBebida()).setValue(bebida);
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
        bebidassref.removeEventListener(valueEventListener);
    }

    public void recuperarBebidas() {
        valueEventListener = bebidassref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //limpa as listas para q n haja repetição de listagem de itens
                BebidasIndisponiveis.clear();
                BebidasDisponiveis.clear();
                for (DataSnapshot dados : snapshot.getChildren()){
                    Bebida bebida = dados.getValue(Bebida.class);
                    if (bebida.getIsDisponivel().contains("true")  ){
                        BebidasDisponiveis.add(bebida);
                        statusDisponivelAdapter.notifyDataSetChanged();
                    }
                    else {
                        BebidasIndisponiveis.add(bebida);
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

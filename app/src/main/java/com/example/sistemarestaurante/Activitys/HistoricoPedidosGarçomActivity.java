package com.example.sistemarestaurante.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemarestaurante.Adapters.ListaPedidosAdapter;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Firebase.UsuarioFireBase;
import com.example.sistemarestaurante.Helper.RecyclerViewClickListener;
import com.example.sistemarestaurante.Model.Base64Custom;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.Model.Usuario;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.shape.InterpolateOnScrollPositionChangeHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoricoPedidosGarçomActivity extends AppCompatActivity {

    //xml
    private RecyclerView recyclerViewHistorico;
    private FloatingActionButton fab;
    private ListaPedidosAdapter listaPedidosAdapter;

    //firebase
    private  final DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference mesaref;
    private ValueEventListener valueEventListener;

    //model
    private List<Pedido> pedidos = new ArrayList<>();
    private List<Pedido> pedidosFiltrados = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_pedidos);

        //configurações iniciais
        fab = findViewById(R.id.fabSairHistorico);
        recyclerViewHistorico = findViewById(R.id.recyclerViewHistoricoPedidos);

        recuperarListaPedidods();
        configurarAdapter();
        configurarRecycler(recyclerViewHistorico);


        //set click para recyclerView
        recyclerViewHistorico.addOnItemTouchListener(new RecyclerViewClickListener(getApplicationContext(), recyclerViewHistorico, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(getApplicationContext(), InfoPedidoHistoricoGarcomActivity.class);
                Pedido pedido = pedidosFiltrados.get(position);
                i.putExtra("pedido",pedido);
                startActivity(i);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }));


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        mesaref.removeEventListener(valueEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarListaPedidods();
    }

    public void configurarRecycler (RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(listaPedidosAdapter);
    }
    public void configurarAdapter () {
        listaPedidosAdapter = new ListaPedidosAdapter(pedidosFiltrados,getApplicationContext(),"garcom");
    }
    public void recuperarListaPedidods () {
        String emailUsuarioLogado = UsuarioFireBase.getUsuarioLogado().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuarioLogado);
        DatabaseReference usuario = databaseReference.child("funcionarios").child(idUsuario);
        usuario.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Usuario usuario = task.getResult().getValue(Usuario.class);

                mesaref =  databaseReference.child("mesas");
                valueEventListener = mesaref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        pedidosFiltrados.clear();
                        for (DataSnapshot dados : snapshot.getChildren()){
                            Mesa mesa = dados.getValue(Mesa.class);
                            if (mesa != null){
                                pedidos = mesa.getPedidos();
                                if(pedidos != null){
                                    for (Pedido pedidoFiltrado : pedidos){
                                        if (pedidoFiltrado.getBebidaStauts().contains("pronto") || pedidoFiltrado.getBebidaStauts().contains("não tem") ) {
                                            if (pedidoFiltrado.getComidaStauts().contains("pronto") || pedidoFiltrado.getComidaStauts().contains("não tem")){
                                                if(pedidoFiltrado.getNomeGarçom().equals(usuario.getNome())) {
                                                    pedidosFiltrados.add(pedidoFiltrado);
                                                    listaPedidosAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }



                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}

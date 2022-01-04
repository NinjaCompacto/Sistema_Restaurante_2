package com.example.sistemarestaurante.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.sistemarestaurante.Adapters.ListaPedidosAdapter;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Firebase.UsuarioFireBase;
import com.example.sistemarestaurante.Model.Base64Custom;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.Model.Usuario;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HistoricoPedidosActivity extends AppCompatActivity {

    //XML
    private RecyclerView recyclerViewHistoricoPedidos;
    private ListaPedidosAdapter listaPedidosAdapter;
    private FloatingActionButton fab;

    //Firebase
    private final  DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private final DatabaseReference mesaref = databaseReference.child("mesas");
    private ValueEventListener valueEventListener;

    //model
    private  List<Pedido> listPedidos = new ArrayList<>();
    private List<Pedido> listPedidosFiltradosComida = new ArrayList<>();
    private List<Pedido> listPedidosFiltradosBebida = new ArrayList<>();
    private Usuario usuario;

    public HistoricoPedidosActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_pedidos);
        fab = findViewById(R.id.fabSairHistorico);

            String emailUsuarioLogado = UsuarioFireBase.getUsuarioLogado().getEmail();
            String idUsuario = Base64Custom.codificarBase64(emailUsuarioLogado);
            DatabaseReference usuarioref =  databaseReference.child("funcionarios").child(idUsuario);
            usuarioref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    usuario = task.getResult().getValue(Usuario.class);
                    //configurações inciais
                    recyclerViewHistoricoPedidos = findViewById(R.id.recyclerViewHistoricoPedidos);
                    recuperarPedidosProntos();
                    configurarAdapter(usuario);
                    configuraRecycler(recyclerViewHistoricoPedidos);

                }
            });

        ;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void configuraRecycler(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(listaPedidosAdapter);
    }
    private void configurarAdapter(Usuario usuario) {
        switch (usuario.getFuncao()) {
            case "Cozinha":
                if (listPedidosFiltradosComida != null){
                listaPedidosAdapter = new ListaPedidosAdapter(listPedidosFiltradosComida,getApplicationContext(),"cozinha");
                }
            break;
            case "Bar":
                if (listPedidosFiltradosBebida != null) {
                    listaPedidosAdapter = new ListaPedidosAdapter(listPedidosFiltradosBebida, getApplicationContext(), "bar");
                }
            break;


        }
    }
    private void recuperarPedidosProntos () {

        valueEventListener = mesaref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listPedidos.clear();
                listPedidosFiltradosComida.clear();
                listPedidosFiltradosBebida.clear();
                for(DataSnapshot dados : snapshot.getChildren()){
                    Mesa mesa = dados.getValue(Mesa.class);
                    if (mesa != null) {
                        listPedidos = mesa.getPedidos();
                        if (listPedidos != null) {
                            for (Pedido pedido : listPedidos) {
                                String statusComida = pedido.getComidaStauts();
                                String statusBebida = pedido.getBebidaStauts();

                                if (statusComida.contains("pronto")) {
                                    listPedidosFiltradosComida.add(pedido);
                                    listaPedidosAdapter.notifyDataSetChanged();
                                }
                                if (statusBebida.contains("pronto")) {
                                    listPedidosFiltradosBebida.add(pedido);
                                    listaPedidosAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                    listaPedidosAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
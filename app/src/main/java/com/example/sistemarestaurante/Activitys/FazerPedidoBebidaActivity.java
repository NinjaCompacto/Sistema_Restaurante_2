package com.example.sistemarestaurante.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.sistemarestaurante.Adapters.BebidaPedidasAdapter;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Model.Bebida;
import com.example.sistemarestaurante.Model.BebidaPedida;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.Model.Usuario;
import com.example.sistemarestaurante.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FazerPedidoBebidaActivity extends AppCompatActivity {

    //XML
    private RecyclerView recyclerBebidas;
    private FloatingActionButton fab;
    private BebidaPedidasAdapter bebidaPedidasAdapter;

    //firebase
    private final DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private  DatabaseReference bebidasref;
    private ValueEventListener valueEventListener;

    //Model
    private List<Bebida> bebidasLista = new ArrayList<>();
    private List<BebidaPedida> bebidaPedidas = new ArrayList<>();
    private Mesa mesa;
    private Pedido pedido;
    private Usuario garçom;
    private List<Pedido> listapedidos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fazer_pedido_bebida);

        //configurações inicias
        fab = findViewById(R.id.fabPedidosBebidas);
        recyclerBebidas = findViewById(R.id.recyclerPedidosBebidas);

        //recupera extras
        if (getIntent().getExtras() != null){
            mesa = (Mesa) getIntent().getExtras().getSerializable("mesa");
            pedido = (Pedido) getIntent().getExtras().getSerializable("pedido");
            garçom = (Usuario)  getIntent().getExtras().getSerializable("garçom");
        }

        bebidasref = databaseReference.child("bebidas");

        recuperarBebidas();
        //configura adapter
        bebidaPedidasAdapter = new BebidaPedidasAdapter(bebidasLista,getApplicationContext());
        //configura recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerBebidas.setLayoutManager(layoutManager);
        recyclerBebidas.setHasFixedSize(true);
        recyclerBebidas.setAdapter(bebidaPedidasAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mesa.getPedidos() == null){
                    for(BebidaPedida bebidaPedida: bebidaPedidasAdapter.getBebidas()){
                        if (bebidaPedida.getQuantidade() > 0){
                            bebidaPedidas.add(bebidaPedida);
                        }
                    }
                    //pedido.setBebida(bebidaPedidas);
                    listapedidos.add(pedido);
                }
                else{
                    listapedidos = mesa.getPedidos();
                    for(BebidaPedida bebidaPedida: bebidaPedidasAdapter.getBebidas()){
                        if (bebidaPedida.getQuantidade() > 0){
                            bebidaPedidas.add(bebidaPedida);
                        }
                    }
                    //pedido.setBebida(bebidaPedidas);

                    //deixar para adicionar pedido na lista de pedidos da mesa na tela final ou seja na proxima
                    //listapedidos.add(pedido);
                }
                //passar a lista de bebidas pedidas para fazer obs
                Intent i = new Intent(FazerPedidoBebidaActivity.this,AdicionarObsBebidaActivity.class);
                i.putExtra("bebidas", (Serializable) bebidaPedidas);
                //passar mesa para proxima tela para seta pedidos no final do processo[
                i.putExtra("mesa", mesa);
                //passar o objeto pedido
                i.putExtra("pedido", pedido);
                startActivity(i);
                finish();

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        bebidasref.removeEventListener(valueEventListener);
    }

    public void recuperarBebidas () {
        valueEventListener = bebidasref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bebidasLista.clear();
                for (DataSnapshot dados : snapshot.getChildren()){
                    Bebida bebida = dados.getValue(Bebida.class);
                    if(bebida.getIsDisponivel().contains("true")){
                        bebidasLista.add(bebida);
                        bebidaPedidasAdapter.notifyDataSetChanged();
                    }
                    if(bebida.getIsDisponivel().contains("false") && bebidasLista.contains(bebida)){
                        bebidasLista.remove(bebida);
                        bebidaPedidasAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
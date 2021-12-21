package com.example.sistemarestaurante.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemarestaurante.Adapters.PratoPedidosAdapter;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Firebase.UsuarioFireBase;
import com.example.sistemarestaurante.Model.Base64Custom;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.Model.Prato;
import com.example.sistemarestaurante.Model.PratoPedido;
import com.example.sistemarestaurante.Model.Usuario;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FazerPedidosPratoActivity extends AppCompatActivity {

    //XML
    private RecyclerView recyclerPratos;
    private FloatingActionButton fab;
    private TextView textInfoMesaSelecionada;

    //model
    private Mesa mesaSelecionada;
    private List<Prato> listaPratos = new ArrayList<>();
    private List<Pedido> listadepedidos = new ArrayList<>();
    private List<PratoPedido> listPratosPedidos = new ArrayList<>();
    private PratoPedidosAdapter pratoPedidosAdapter;
    private Pedido pedido = new Pedido();
    private Usuario garçom;
    //firebase
    private final DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private  DatabaseReference pratosref;
    private DatabaseReference garcomref;
    private FirebaseUser user = UsuarioFireBase.getUsuarioLogado();
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fazer_pedidos_prato);

        //configurações iniciais
        recyclerPratos = findViewById(R.id.recyclerPratos);
        textInfoMesaSelecionada = findViewById(R.id.textInfoMesa);
        fab = findViewById(R.id.fabConfirmarPedido);
        garcomref = databaseReference.child("funcionarios").child(Base64Custom.codificarBase64(user.getEmail()));

        garcomref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    //recuperando dados do garçom que fezo pedido
                    garçom = task.getResult().getValue(Usuario.class);
                }
            }
        });

        //recupera mesa selecionada pelo garçom
        if(getIntent().getExtras() != null){
            mesaSelecionada = (Mesa)  getIntent().getExtras().getSerializable("mesa");
            textInfoMesaSelecionada.setText("Mesa: "+ mesaSelecionada.getNumeroMesa() +"   Nome Cliente: " + mesaSelecionada.getNomeCliente() );
        }

        //faz a listagem dos pratos disponiveis e guarda em uma lista para ser usado como parametro de listagem do recycler
        recuperarpratos();

        //configurando adapter
        pratoPedidosAdapter = new PratoPedidosAdapter(listaPratos,getApplicationContext());
        //configurando recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerPratos.setLayoutManager(layoutManager);
        recyclerPratos.setHasFixedSize(true);
        recyclerPratos.setAdapter(pratoPedidosAdapter);

        //seta click do Fab
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setando dados do pedido
                pedido.setNomeGarçom(garçom.getNome());
                //verifica se já há pedidos ou não naquela mesa , para evitar sobre escrever dados nos pedidos
                if (mesaSelecionada.getPedidos() == null) {
                    for (PratoPedido pratoPedido : pratoPedidosAdapter.getpedidos()) {
                        //seleciona os pratos que foram selecionados dos que n foram para guarda em uma lista nova
                        if (pratoPedido.getQuantidade() > 0) {
                            listPratosPedidos.add(pratoPedido);
                        }
                    }
                    //seta a lista de pratos selecionados no objeto pedidos
                    pedido.setComida(listPratosPedidos);
                    //adiciona pedido a lista de pedidos
                    listadepedidos.add(pedido);
                }else {
                    for (PratoPedido pratoPedido : pratoPedidosAdapter.getpedidos()) {
                        if (pratoPedido.getQuantidade() > 0) {
                            listPratosPedidos.add(pratoPedido);
                        }
                    }

                }
                //passa lista de pratos pedidos para fazar observação em cada um deles
                Intent i = new Intent(FazerPedidosPratoActivity.this,AdiconarObsPratoActivity.class);
                i.putExtra("mesa", mesaSelecionada);
                i.putExtra("listapratos", (Serializable) listPratosPedidos);
                i.putExtra("garçom",garçom);
                //limpa a lista de pedidos para que não haja listagem em dobro
                startActivity(i);
                finish();
            }
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        pratosref.removeEventListener(valueEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getIntent().getExtras() != null){
            mesaSelecionada = (Mesa)  getIntent().getExtras().getSerializable("mesa");
            textInfoMesaSelecionada.setText("Mesa: "+ mesaSelecionada.getNumeroMesa() +"   Nome Cliente: " + mesaSelecionada.getNomeCliente() );
        }
    }

    public void recuperarpratos () {
        pratosref = databaseReference.child("pratos");
        valueEventListener = pratosref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaPratos.clear();
                for (DataSnapshot dados : snapshot.getChildren()) {
                    if (dados.getValue(Prato.class).getIsDisponivel().contains("true")){
                         listaPratos.add(dados.getValue(Prato.class));
                    }
                    if (dados.getValue(Prato.class).getIsDisponivel().contains("false") && listaPratos.contains(dados.getValue(Prato.class)) ){
                        listaPratos.remove(dados.getValue(Prato.class));
                    }
                    pratoPedidosAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
package com.example.sistemarestaurante.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.sistemarestaurante.Adapters.ListaBebidasPedidasAdapter;
import com.example.sistemarestaurante.Adapters.ListaPedidosAdapter;
import com.example.sistemarestaurante.Adapters.ListaPratosPedidosAdapter;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Model.Bebida;
import com.example.sistemarestaurante.Model.BebidaPedida;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.Model.PratoPedido;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class InfoPedidoHistoricoGarcomActivity extends AppCompatActivity {

    //model
    private Pedido pedido;
    private List<PratoPedido> pratoPedidos;
    private List<BebidaPedida> bebidaPedidas;

    //xml
    private RecyclerView recyclerListaPratos,recyclerListaBebidas;
    private FloatingActionButton fabConfirmaPedido;
    private TextView textInfoMesa1;
    private ListaPratosPedidosAdapter listaPratosPedidosAdapter;
    private ListaBebidasPedidasAdapter listaBebidasPedidasAdapter;

    //Firebase
    private final DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference mesaref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_pedido_historico_garcom);

        //recuperar Intent
        if (getIntent() != null){
            pedido = (Pedido) getIntent().getExtras().getSerializable("pedido");
            pratoPedidos = pedido.getComida();
            bebidaPedidas = pedido.getBebida();
        }

        //configurações inicais
        recyclerListaBebidas = findViewById(R.id.recyclerListaBebidas);
        recyclerListaPratos = findViewById(R.id.recyclerListaPratos);
        fabConfirmaPedido = findViewById(R.id.fabConfirmaPedido);
        textInfoMesa1 = findViewById(R.id.textInfoMesa1);

        if (pedido.getComida() !=null){
        configurarrecyclerPratos(recyclerListaPratos);
        }
        else {
            recyclerListaPratos.setVisibility(View.GONE);
        }
        if (pedido.getBebida() != null) {
            configurarrecyclerBebidas(recyclerListaBebidas);
        }
        else {
            recyclerListaBebidas.setVisibility(View.GONE);
        }
        mesaref = databaseReference.child("mesas").child(pedido.getNumeroMesa());
        mesaref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Mesa mesa = task.getResult().getValue(Mesa.class);
                textInfoMesa1.setText("Mesa: " + mesa.getNumeroMesa() + ", Cliente: " + mesa.getNomeCliente());
            }
        });

        fabConfirmaPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
    private void configurarrecyclerPratos (RecyclerView recyclerView){
        listaPratosPedidosAdapter = new ListaPratosPedidosAdapter(pratoPedidos,getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(listaPratosPedidosAdapter);
    }
    private void configurarrecyclerBebidas (RecyclerView recyclerView){
        listaBebidasPedidasAdapter = new ListaBebidasPedidasAdapter(bebidaPedidas,getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(listaBebidasPedidasAdapter);
    }
}
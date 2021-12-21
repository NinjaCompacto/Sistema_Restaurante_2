package com.example.sistemarestaurante.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.sistemarestaurante.Adapters.ListaPratosPedidosAdapter;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.Model.PratoPedido;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class PedidoPratoActivity extends AppCompatActivity {

    //model
    private  Pedido pedido;
    private List<PratoPedido> pratoPedidos;
    //xml
    private RecyclerView recyclerPedidoPrato;
    private ListaPratosPedidosAdapter listaPratosPedidosAdapter;
    private TextView textStatus;
    private Button buttonPreparando,buttonPronto;
    //Firebase
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference mesaref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_prato);
        //configurações iniciais
        recyclerPedidoPrato = findViewById(R.id.recyclerPedidoPratoInfo);
        textStatus = findViewById(R.id.textStatusPedidoComida);
        buttonPreparando = findViewById(R.id.buttonStatusPreparandoComida);
        buttonPronto = findViewById(R.id.buttonStatusProntoComida);

        //recupernado intents
        if (getIntent().getExtras() != null){
            pedido = (Pedido) getIntent().getExtras().getSerializable("pedido");
            if(pedido != null){
                pratoPedidos = recuperarListaComida(pedido);
            }
        }
        textStatus.setText("Status: " + pedido.getComidaStauts());

        switch (pedido.getComidaStauts()){
            case "em aberto":
                textStatus.setTextColor(Color.RED);
                break;
            case "preparando":
                textStatus.setTextColor(Color.rgb(255,193,7));
                break;
            case "pronto":
                textStatus.setTextColor(Color.GREEN);
                break;
        }


        //configura adpter
        listaPratosPedidosAdapter = new ListaPratosPedidosAdapter(pratoPedidos,getApplicationContext());
        //configura recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerPedidoPrato.setHasFixedSize(true);
        recyclerPedidoPrato.setLayoutManager(layoutManager);
        recyclerPedidoPrato.setAdapter(listaPratosPedidosAdapter);

        buttonPreparando.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesaref = databaseReference.child("mesas").child(pedido.getNumeroMesa());
                mesaref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Mesa  mesa = task.getResult().getValue(Mesa.class);
                        List<Pedido> listPedido = mesa.getPedidos();
                        List<Pedido> listaAtualizada = new ArrayList<>();

                        for (Pedido pedido1 : listPedido){
                            if (pedido1.getId().equals(pedido.getId())){
                                pedido1.setComidaStauts("preparando");
                                textStatus.setText("Status: Preparando");
                                textStatus.setTextColor(Color.rgb(255,193,7));
                                listaAtualizada.add(pedido1);
                            }
                            else {
                                listaAtualizada.add(pedido1);
                            }
                        }
                        mesa.setPedidos(listaAtualizada);
                        mesaref.setValue(mesa);
                    }
                });
            }});

        buttonPronto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesaref = databaseReference.child("mesas").child(pedido.getNumeroMesa());
                mesaref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        Mesa  mesa = task.getResult().getValue(Mesa.class);
                        List<Pedido> listPedido = mesa.getPedidos();
                        List<Pedido> listaAtualizada = new ArrayList<>();

                        for (Pedido pedido1 : listPedido){
                            if (pedido1.getId().equals(pedido.getId())){
                                pedido1.setComidaStauts("pronto");
                                textStatus.setText("Status: Pronto");
                                textStatus.setTextColor(Color.GREEN);
                                listaAtualizada.add(pedido1);
                            }
                            else {
                                listaAtualizada.add(pedido1);
                            }
                        }
                        mesa.setPedidos(listaAtualizada);
                        mesaref.setValue(mesa);
                    }
                });
            }
        });

    }

    public List<PratoPedido> recuperarListaComida (Pedido pedido) {
        List<PratoPedido> pratoPedidos = new ArrayList<>();
        pratoPedidos.clear();
        for (PratoPedido comida : pedido.getComida()){
            pratoPedidos.add(comida);
        }

        return pratoPedidos;
    }
}

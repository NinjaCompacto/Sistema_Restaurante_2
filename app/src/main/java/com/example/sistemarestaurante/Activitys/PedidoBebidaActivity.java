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

import com.example.sistemarestaurante.Adapters.ListaBebidasPedidasAdapter;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Model.Bebida;
import com.example.sistemarestaurante.Model.BebidaPedida;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PedidoBebidaActivity extends AppCompatActivity {

    //model
    private Pedido pedidoSelecionado;
    private List<BebidaPedida> listaBebidasPedidas ;

    //XML
    private RecyclerView recyclerPedidoBebidas;
    private Button buttonPreparandoStatus,buttonProntoStatus;
    private TextView textStatus;
    private ListaBebidasPedidasAdapter listaBebidasPedidasAdapter;

    //firebase
    private final DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference  mesaref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_bebida);

        //configurações iniciais
        recyclerPedidoBebidas = findViewById(R.id.recyclerPedidoBebida);
        buttonPreparandoStatus = findViewById(R.id.buttonStatusPreparandoBebida);
        buttonProntoStatus = findViewById(R.id.buttonStatusProntoBebida);
        textStatus = findViewById(R.id.textStatusBebida);

        //recuperando intents
        if (getIntent().getExtras() != null){
            pedidoSelecionado = (Pedido) getIntent().getExtras().getSerializable("pedidobebida");
            listaBebidasPedidas = pedidoSelecionado.getBebida();
        }

        setStatusIncial(pedidoSelecionado);

        //Intancia adapeter
        listaBebidasPedidasAdapter = new ListaBebidasPedidasAdapter(listaBebidasPedidas,getApplicationContext());
        //cofigurando recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerPedidoBebidas.setHasFixedSize(true);
        recyclerPedidoBebidas.setLayoutManager(layoutManager);
        recyclerPedidoBebidas.setAdapter(listaBebidasPedidasAdapter);

        //configura botões de mudança de status
        buttonPreparandoStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesaref = databaseReference.child("mesas").child(pedidoSelecionado.getNumeroMesa());
                    mesaref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            Mesa mesa = task.getResult().getValue(Mesa.class);
                            List <Pedido> pedidos = mesa.getPedidos();
                            List <Pedido> pedidosAtualizados = new ArrayList<>();

                            for (Pedido pedido : pedidos){
                                if (pedido.getId().equals(pedidoSelecionado.getId())){
                                    pedido.setBebidaStauts("preparando");
                                    textStatus.setText("Status : Preparando");
                                    textStatus.setTextColor(Color.rgb(255,193,7));
                                    pedidosAtualizados.add(pedido);
                                }
                                else {
                                    pedidosAtualizados.add(pedido);
                                }
                            }
                            mesa.setPedidos(pedidosAtualizados);
                            mesaref.setValue(mesa);
                        }
                    });
            }
        });

        buttonProntoStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mesaref = databaseReference.child("mesas").child(pedidoSelecionado.getNumeroMesa());
                if (mesaref != null) {
                    mesaref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            Mesa mesa = task.getResult().getValue(Mesa.class);
                            List <Pedido> pedidos = mesa.getPedidos();
                            List <Pedido> pedidosAtualizados = new ArrayList<>();

                            for (Pedido pedido : pedidos){
                                if (pedido.getId().equals(pedidoSelecionado.getId())){
                                    pedido.setBebidaStauts("pronto");
                                    textStatus.setText("Status: Pronto");
                                    textStatus.setTextColor(Color.GREEN);
                                    pedidosAtualizados.add(pedido);
                                }
                                else {
                                    pedidosAtualizados.add(pedido);
                                }
                            }
                            mesa.setPedidos(pedidosAtualizados);
                            mesaref.setValue(mesa);
                        }
                    });
                }
            }
        });
    }

    public void setStatusIncial (Pedido pedido) {
        switch (pedido.getBebidaStauts().toLowerCase()){
            case "preparando" :
                textStatus.setText("Status: Preparando");
                textStatus.setTextColor(Color.rgb(255,193,7));
                break;
            case "em aberto" :
                textStatus.setText("Status: Em aberto");
                textStatus.setTextColor(Color.RED);
                break;
            case "pronto" :
                textStatus.setText("Status: Pronto");
                textStatus.setTextColor(Color.GREEN);
        }
    }

}
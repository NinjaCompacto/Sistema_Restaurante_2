package com.example.sistemarestaurante.Activitys;

import android.icu.text.DateFormat;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemarestaurante.Adapters.ObsBebidaAdapter;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Model.BebidaPedida;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.Model.PratoPedido;
import com.example.sistemarestaurante.Model.Usuario;
import com.example.sistemarestaurante.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AdicionarObsBebidaActivity extends AppCompatActivity {

    //XML
    private RecyclerView recyclerObsPratos;
    private FloatingActionButton fabObsBebidas;
    private ObsBebidaAdapter obsBebidaAdapter ;

    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference pratosPedidosref;
    private ValueEventListener valueEventListener;

    //Model
    private Mesa mesa;
    private Pedido pedido;
    private List<BebidaPedida>  bebidaPedidas;
    private List<BebidaPedida>  bebidaPedidascomObs;
    private Pedido pedidosAtualizado = new Pedido();
    private Usuario garçom;
    private List<Pedido> pedidos = new ArrayList<>();
    private List<PratoPedido> pratoPedidos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adiconar_obs_prato);

        recyclerObsPratos = findViewById(R.id.recyclerObsPratos);
        fabObsBebidas = findViewById(R.id.fabObsPratos);


        if (getIntent().getExtras() != null) {
            bebidaPedidas = (List<BebidaPedida>) getIntent().getExtras().getSerializable("bebidas");
            mesa = (Mesa) getIntent().getExtras().getSerializable("mesa");
            pedido = (Pedido) getIntent().getExtras().getSerializable("pedido");
        }


        obsBebidaAdapter = new ObsBebidaAdapter(bebidaPedidas,getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerObsPratos.setHasFixedSize(true);
        recyclerObsPratos.setLayoutManager(layoutManager);
        recyclerObsPratos.setAdapter(obsBebidaAdapter);
        fabObsBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String time = getPedidoTime();
                    String idPedido = criarId();
                    bebidaPedidascomObs = obsBebidaAdapter.getBebidaPedidascomObs();

                    if (mesa.getPedidos() == null) {
                        pedido.setBebida(bebidaPedidascomObs);
                        pedido.setNumeroMesa(mesa.getNumeroMesa());
                        pedido.setId(idPedido);
                        pedido.setTime(time);
                        if (pedido.getComida().isEmpty()){
                            pedido.setComidaStauts("não tem");
                        }
                        else {
                            pedido.setComidaStauts("em aberto");
                        }

                        if(pedido.getBebida().isEmpty()){
                            pedido.setBebidaStauts("não tem");
                        }
                        else {
                            pedido.setBebidaStauts("em aberto");
                        }

                        pedidos.add(pedido);
                    }
                    else {

                        pedidos = mesa.getPedidos();
                        pedido.setBebida(bebidaPedidascomObs);
                        pedido.setNumeroMesa(mesa.getNumeroMesa());
                        pedido.setId(idPedido);
                        pedido.setTime(time);

                        if (pedido.getComida().isEmpty()){
                            pedido.setComidaStauts("não tem");
                        }
                        else {
                            pedido.setComidaStauts("em aberto");
                        }

                        if(pedido.getBebida().isEmpty()){
                            pedido.setBebidaStauts("não tem");
                        }
                        else {
                            pedido.setBebidaStauts("em aberto");
                        }
                        pedidos.add(pedido);
                    }

                    mesa.setPedidos(pedidos);
                    mesa.salvarmesa();
                    finish();

            }
        });
    }

    public String criarId() {
        UUID uniqueKey = UUID.randomUUID();
        String id = uniqueKey.toString();
        return id;
    }

    public String getPedidoTime () {
        Date time = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String timePedido = format.format(time).toString();
        return timePedido;
    }



}
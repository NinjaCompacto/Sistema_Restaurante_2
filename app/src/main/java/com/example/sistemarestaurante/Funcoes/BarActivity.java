package com.example.sistemarestaurante.Funcoes;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemarestaurante.Activitys.CadastroBebidaActivity;
import com.example.sistemarestaurante.Activitys.PedidoBebidaActivity;
import com.example.sistemarestaurante.Adapters.ListaPedidosAdapter;
import com.example.sistemarestaurante.Cadastro_e_login.LoginActivity;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Activitys.MudarBebidaStatusActivity;
import com.example.sistemarestaurante.Helper.RecyclerViewClickListener;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BarActivity extends AppCompatActivity {

    //XML
    private FloatingActionButton fabBar;
    private RecyclerView recyclerListaBebidasPedidas;
    private ListaPedidosAdapter listaPedidosAdapter;
    //firebase
    private FirebaseAuth auth = ConfiguracaoFirebase.getAuth();
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference mesaref = databaseReference.child("mesas");

    //model
    private List<Pedido> pedidos = new ArrayList<>();
    private List<Pedido> pedidosFiltrado = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);
        //configuração inicial
        recyclerListaBebidasPedidas = findViewById(R.id.recyclerBebidasPedidasBar);
        fabBar = findViewById(R.id.fabBar);
        recuperarPedidosComBebidas();

        //configurando adapter
        listaPedidosAdapter = new ListaPedidosAdapter(pedidosFiltrado,getApplicationContext(),"bar");
        //configura recyler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerListaBebidasPedidas.setHasFixedSize(true);
        recyclerListaBebidasPedidas.setLayoutManager(layoutManager);
        recyclerListaBebidasPedidas.setAdapter(listaPedidosAdapter);

        //adicionando evento de click
        recyclerListaBebidasPedidas.addOnItemTouchListener(new RecyclerViewClickListener(getApplicationContext(), recyclerListaBebidasPedidas
                , new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Pedido pedidoSelecionado = pedidosFiltrado.get(position);
                Intent intent = new Intent(getApplicationContext(), PedidoBebidaActivity.class);
                intent.putExtra("pedidobebida",pedidoSelecionado);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        //set click para adicionar bebidas
        fabBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BarActivity.this, CadastroBebidaActivity.class);
                startActivity(i);
            }
        });
    }


    //infla menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menubar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                auth.signOut();
                Intent i = new Intent(BarActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.menuStatus:
                Intent i1 = new Intent(BarActivity.this, MudarBebidaStatusActivity.class);
                startActivity(i1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void recuperarPedidosComBebidas() {
        mesaref = databaseReference.child("mesas");
        mesaref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pedidosFiltrado.clear();
                pedidos.clear();
                for (DataSnapshot dados : snapshot.getChildren()) {
                    Mesa mesa = dados.getValue(Mesa.class);
                    if (mesa.getPedidos() != null) {
                        pedidos = mesa.getPedidos();
                        for (Pedido pedido : pedidos) {
                            if (!pedido.getBebidaStauts().contains("pronto") && pedido.getBebida() != null) {
                                pedidosFiltrado.add(pedido);
                                listaPedidosAdapter.notifyDataSetChanged();
                            }
                            if (pedido.getBebidaStauts().contains("pronto") && pedidosFiltrado.contains(pedido)) {
                                pedidosFiltrado.remove(pedido);
                                listaPedidosAdapter.notifyDataSetChanged();
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


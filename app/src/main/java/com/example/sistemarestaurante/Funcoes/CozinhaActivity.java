package com.example.sistemarestaurante.Funcoes;

import android.content.Intent;
import android.os.Bundle;

import com.example.sistemarestaurante.Activitys.CadastroPratoActicity;
import com.example.sistemarestaurante.Activitys.MudarPratoStatusActivity;
import com.example.sistemarestaurante.Activitys.PedidoPratoActivity;
import com.example.sistemarestaurante.Adapters.ListaPedidosAdapter;
import com.example.sistemarestaurante.Cadastro_e_login.LoginActivity;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;


public class CozinhaActivity extends AppCompatActivity {

    //model
    private List<Pedido> pedidos = new ArrayList<>();
    private List<Pedido> pedidosFiltrado = new ArrayList<>();

    //XML
    private RecyclerView recyclerListaPratoPedidos;
    private FloatingActionButton fab;
    private ListaPedidosAdapter listaPedidosAdapter;

    //Firebase
    private FirebaseAuth auth = ConfiguracaoFirebase.getAuth();
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference mesaref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cozinha);

        //configurações iniciais
        fab = findViewById(R.id.fabAdicionarPrato);
        recyclerListaPratoPedidos = findViewById(R.id.recyclerListaPratosPedidos);



        recuperarListaPedidos();

        //configura adapter
        listaPedidosAdapter = new ListaPedidosAdapter(pedidosFiltrado,getApplicationContext(),"cozinha");
        //configurarecycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerListaPratoPedidos.setLayoutManager(layoutManager);
        recyclerListaPratoPedidos.setHasFixedSize(true);
        recyclerListaPratoPedidos.setAdapter(listaPedidosAdapter);

        recyclerListaPratoPedidos.addOnItemTouchListener(new RecyclerViewClickListener(getApplicationContext(),
                recyclerListaPratoPedidos,
                new RecyclerViewClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Pedido pedidoSelecionado = pedidosFiltrado.get(position);
                        Intent i = new Intent(getApplicationContext(), PedidoPratoActivity.class);
                        i.putExtra("pedido",pedidoSelecionado);
                        startActivity(i);
                        finish();
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
                Intent i = new Intent(getApplicationContext(), CadastroPratoActicity.class);
                startActivity(i);
                finish();
            }
        });


    }

    public void recuperarListaPedidos () {
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
                            for (Pedido pedido : pedidos){
                                if (!pedido.getComidaStauts().contains("pronto") && pedido.getComida() != null){
                                    pedidosFiltrado.add(pedido);
                                    listaPedidosAdapter.notifyDataSetChanged();
                                }
                                if(pedido.getComidaStauts().contains("pronto")  && pedidosFiltrado.contains(pedido)){
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

    //infla menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menucozinha,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //tratamento para opção selecionada do menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuSair:
                auth.signOut();
                Intent i = new Intent(CozinhaActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.menuStatus:
                Intent istatus = new Intent(CozinhaActivity.this , MudarPratoStatusActivity.class);
                startActivity(istatus);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
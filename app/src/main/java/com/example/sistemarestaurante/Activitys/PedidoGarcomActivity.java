package com.example.sistemarestaurante.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.sistemarestaurante.Adapters.BebidaPedidasAdapter;
import com.example.sistemarestaurante.Adapters.PratoPedidosAdapter;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Firebase.UsuarioFireBase;
import com.example.sistemarestaurante.Model.Base64Custom;
import com.example.sistemarestaurante.Model.Bebida;
import com.example.sistemarestaurante.Model.BebidaPedida;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.Model.Prato;
import com.example.sistemarestaurante.Model.PratoPedido;
import com.example.sistemarestaurante.Model.Usuario;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PedidoGarcomActivity extends AppCompatActivity {

    //XML
    private RecyclerView recyclerListaPratos,recyclerListaBebidas;
    private FloatingActionButton fabConfirmaPedido;
    private PratoPedidosAdapter pratoPedidosAdapter;
    private BebidaPedidasAdapter bebidaPedidasAdapter;
    private TextView textInfoMesa1;

    //firebase
    private final DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference mesaref;
    private DatabaseReference pratosref;
    private DatabaseReference garcomref;
    private ValueEventListener valueEventListenerPrato,valueEventListenerBebida;
    private FirebaseUser user = UsuarioFireBase.getUsuarioLogado();


    //model
    private List<PratoPedido> listaPratosPedidos = new ArrayList<>();
    private List<BebidaPedida> listabebidaPedidas = new ArrayList<>();
    private List<Prato> listaPratos = new ArrayList<>();
    private List <Bebida> listaBebidas = new ArrayList<>();
    private Mesa mesaSelecionada;
    private Usuario garçom;
    private Pedido pedido= new Pedido();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_garcom);

        //configurações inicias
        recyclerListaBebidas = findViewById(R.id.recyclerListaBebidas);
        recyclerListaPratos = findViewById(R.id.recyclerListaPratos);
        fabConfirmaPedido = findViewById(R.id.fabConfirmaPedido);
        textInfoMesa1 = findViewById(R.id.textInfoMesa1);
        garcomref = databaseReference.child("funcionarios").child(Base64Custom.codificarBase64(user.getEmail()));

        //recupera intent
        if (getIntent() != null){
            mesaSelecionada = (Mesa) getIntent().getExtras().getSerializable("mesa");
            configuraTextInfo(mesaSelecionada);
            mesaref = databaseReference.child("mesas").child(mesaSelecionada.getNumeroMesa());
        }
        //recupera o garçom
        recuperarGarçom(garcomref);

        //recupera lista de pratos e bebidas disponiveis
        recuperarpratos();
        recuperarBebidas();

        //configura adapters
        configurarRecyclerPratos(recyclerListaPratos);
        configurarRecyclerBebidas(recyclerListaBebidas);


        fabConfirmaPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pedido.setNomeGarçom(garçom.getNome());

                //recuperar a lista de pratos selecionados e filtra os que tem quantidade diferente de 0
                for (PratoPedido pratoPedido : pratoPedidosAdapter.getpedidos()){
                    if(pratoPedido.getQuantidade() > 0 ) {
                        listaPratosPedidos.add(pratoPedido);
                    }
                }
                //recuperar a lista de bebidas selecionados e filtra os que tem quantidade diferente de 0
                for (BebidaPedida bebidaPedida : bebidaPedidasAdapter.getBebidas()) {
                    if (bebidaPedida.getQuantidade() > 0) {
                        listabebidaPedidas.add(bebidaPedida);
                    }
                }
                    pedido.setComida(listaPratosPedidos);
                    pedido.setBebida(listabebidaPedidas);
                    pedido.setId(criarId());
                    pedido.setTime(getPedidoTime());
                    pedido.setNumeroMesa(mesaSelecionada.getNumeroMesa());

                    if (pedido.getBebida().isEmpty()){
                        pedido.setBebidaStauts("não tem");
                    }else{
                        pedido.setBebidaStauts("em aberto");
                    }

                    if (pedido.getComida().isEmpty()){
                        pedido.setComidaStauts("não tem");
                    }else {
                        pedido.setComidaStauts("em aberto");
                    }

                    if (mesaSelecionada.getPedidos() ==null){
                        List<Pedido> pedidos = new ArrayList<>();
                        pedidos.add(pedido);
                        mesaSelecionada.setPedidos(pedidos);
                    }else {
                        List<Pedido> pedidos = mesaSelecionada.getPedidos();
                        pedidos.add(pedido);
                        mesaSelecionada.setPedidos(pedidos);
                    }

                    mesaSelecionada.salvarmesa();
                    finish();
                }

        });


    }

    private void configuraTextInfo (@NonNull Mesa mesa){
        textInfoMesa1.setText("Mesa: " + mesa.getNumeroMesa() + ", Cliente: " + mesa.getNomeCliente());
    }

    private void configurarRecyclerPratos(@NonNull RecyclerView recyclerView){
        pratoPedidosAdapter = new PratoPedidosAdapter(listaPratos,getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(pratoPedidosAdapter);
    }
    private void configurarRecyclerBebidas(@NonNull RecyclerView recyclerView){
        bebidaPedidasAdapter = new BebidaPedidasAdapter(listaBebidas,getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(bebidaPedidasAdapter);
    }

    private void recuperarpratos () {
        pratosref = databaseReference.child("pratos");
        valueEventListenerPrato = pratosref.addValueEventListener(new ValueEventListener() {
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

    private void recuperarBebidas () {
        pratosref = databaseReference.child("bebidas");
        valueEventListenerBebida = pratosref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaBebidas.clear();
                for (DataSnapshot dados : snapshot.getChildren()) {
                    if (dados.getValue(Bebida.class).getIsDisponivel().contains("true")){
                        listaBebidas.add(dados.getValue(Bebida.class));
                    }
                    if (dados.getValue(Bebida.class).getIsDisponivel().contains("false") && listaPratos.contains(dados.getValue(Bebida.class)) ){
                        listaBebidas.remove(dados.getValue(Bebida.class));
                    }
                    bebidaPedidasAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void recuperarGarçom (@NonNull DatabaseReference garcomref){
        garcomref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    //recuperando dados do garçom que fezo pedido
                    garçom = task.getResult().getValue(Usuario.class);
                }
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
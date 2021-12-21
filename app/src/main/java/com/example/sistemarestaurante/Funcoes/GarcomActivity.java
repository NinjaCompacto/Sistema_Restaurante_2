package com.example.sistemarestaurante.Funcoes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.example.sistemarestaurante.Activitys.AtualizarNomeCliente;
import com.example.sistemarestaurante.Activitys.CadastrarNomeCliente;
import com.example.sistemarestaurante.Activitys.CadastroMesaActivity;
import com.example.sistemarestaurante.Activitys.FazerPedidosPratoActivity;
import com.example.sistemarestaurante.Adapters.MesasAdapter;
import com.example.sistemarestaurante.Cadastro_e_login.LoginActivity;
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.sistemarestaurante.Helper.RecyclerViewClickListener;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GarcomActivity extends AppCompatActivity {

    //XML
    private RecyclerView recyclerView;
    private MesasAdapter adapter;
    //firebase
    private FirebaseAuth auth = ConfiguracaoFirebase.getAuth();
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference mesasref;
    private ValueEventListener valueEventListener;
    //model
    List<Mesa> listaMesas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garcom);
        //configurações iniciais
        recyclerView = findViewById(R.id.recyclerMesas);
        //preenche a lista de mesas para ser passada para o adapter
        recuperarMesas();

        //configurando adpter
        adapter = new MesasAdapter(listaMesas,getApplicationContext());
        //configurando recycler
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        //seta evento de click nos itens do adapter
        recyclerView.addOnItemTouchListener(new RecyclerViewClickListener(getApplicationContext(), recyclerView, new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Mesa mesa = listaMesas.get(position);
                //abre a tela de atualização de cadastro caso não haja cliente cadastrado na mesa
                if (mesa.getNomeCliente().contains("Nenhum Cliente")) {
                    Intent i = new Intent(getApplicationContext(), CadastrarNomeCliente.class);
                    i.putExtra("mesa", mesa);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(getApplicationContext(), FazerPedidosPratoActivity.class);
                    i.putExtra("mesa", mesa);
                    startActivity(i);
                }
            }
            //seta opção de atualização do cliente que esta na mesa
            @Override
            public void onLongItemClick(View view, int position) {
                Mesa mesa = listaMesas.get(position);
                Intent i = new Intent(getApplicationContext(), AtualizarNomeCliente.class);
                i.putExtra("mesaatualizar",mesa);
                startActivity(i);
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menugarcom,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        //tratamento para cada opção selecionada no menu
        switch (item.getItemId()){
            case R.id.menuSair:
                auth.signOut();
                Intent i = new Intent(GarcomActivity.this, LoginActivity.class);
                //retorna a tela de login e desloga usuario
                startActivity(i);
                finish();
                break;
            case R.id.menuMesa:
               Intent imesa = new Intent(GarcomActivity.this,CadastroMesaActivity.class);
               //abrir a tela de cadastro de novas mesas
               startActivity(imesa);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public  void recuperarMesas (){
        listaMesas.clear();
        mesasref = databaseReference.child("mesas");
        valueEventListener = mesasref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaMesas.clear();
            for (DataSnapshot dados : snapshot.getChildren()){
                listaMesas.add(dados.getValue(Mesa.class));
            }
           adapter.notifyDataSetChanged();
            //notificarmesanova();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    //faz alerta por notificação para avisar de uma nova mesa
    public void notificarmesanova () {
        NotificationCompat.Builder notificação = new NotificationCompat.Builder(this,"1")
                .setSmallIcon(R.drawable.ic_baseline_add_24)
                .setContentTitle("Mesa Nova")
                .setContentText("Uma nova mesa foi adicionada !")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mn = NotificationManagerCompat.from(this);
        mn.notify(1,notificação.build());
    }

}
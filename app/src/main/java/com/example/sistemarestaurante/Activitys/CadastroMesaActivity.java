package com.example.sistemarestaurante.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.Model.Prato;
import com.example.sistemarestaurante.Model.PratoPedido;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CadastroMesaActivity extends AppCompatActivity {

    //XML
    private Button buttonAdicionarMesa;
    private EditText editTextNumeroMesa;

    //firebase
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private ValueEventListener mesasexistentesListener ;

    //model
    private Mesa mesa = new Mesa();
    private List<Pedido> pedidos = new ArrayList<>();
    private List<String> mesasExistente = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_mesa);

        //configurações iniciais
        buttonAdicionarMesa = findViewById(R.id.buttonAdicionarMesa);
        editTextNumeroMesa = findViewById(R.id.editNumeroMesa);

        //adiciona evento de click ao botão
        buttonAdicionarMesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validarCampo()){
                    //limpa lista de mesas para evitar repetição
                    mesasExistente.clear();
                    //seta parametros da classse mesa para ser salvo no bando de dados
                    mesa.setNumeroMesa(editTextNumeroMesa.getText().toString());
                    mesa.setNomeCliente("Nenhum Cliente");
                    mesa.setPedidos(pedidos);
                    DatabaseReference mesasref = databaseReference.child("mesas");

                    //adicionando listener para o ref , para iniciar verificação de existencia de mesa no banco de dados
                    mesasexistentesListener = mesasref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //for para adicionar mesas a uma lista
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                mesasExistente.add(dataSnapshot.getValue(Mesa.class).getNumeroMesa());
                            }

                            //verificação de existência da mesa no banco
                            if (mesasExistente.contains(editTextNumeroMesa.getText().toString())){
                                Toast.makeText(CadastroMesaActivity.this,"Esta mesa já existe !", Toast.LENGTH_LONG).show();
                                mesasref.removeEventListener(mesasexistentesListener);
                            }
                            //caso n houver , então, é adicionada uma nova mesa ao banco de dados
                            if(!mesasExistente.contains(editTextNumeroMesa.getText().toString())){
                                mesasref.child(mesa.getNumeroMesa()).setValue(mesa).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(CadastroMesaActivity.this,"Sucesso ao adicionar mesa !", Toast.LENGTH_LONG).show();
                                    }
                                });
                                mesasref.removeEventListener(mesasexistentesListener);
                                //retorna para tela inicial do garçom
                                finish();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




                }

            }
        });

    }

    //valida o preenchimento do campo de numero de mesa
    public boolean validarCampo () {

        if(editTextNumeroMesa.getText().toString() == null || editTextNumeroMesa.getText().toString().isEmpty()){
            Toast.makeText(CadastroMesaActivity.this,"Preencha o número da mesa !", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

}
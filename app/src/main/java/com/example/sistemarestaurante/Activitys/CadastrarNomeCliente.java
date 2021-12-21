package com.example.sistemarestaurante.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

public class CadastrarNomeCliente extends AppCompatActivity {

    //XML
    private Button buttonCadastrar;
    private EditText editNomeCliente;

    //Firebase
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference mesaref;

    //Model
    private Mesa mesa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_nome_cliente);

        //configurações inicias
        buttonCadastrar = findViewById(R.id.buttonCadastrarCliente);
        editNomeCliente = findViewById(R.id.editNomeCliente);

        //recuperando extras ( mesa )
        if (getIntent().getExtras() != null){
            mesa = (Mesa) getIntent().getExtras().getSerializable("mesa");
        }


        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeClienteNovo = editNomeCliente.getText().toString();
                mesa.setNomeCliente(nomeClienteNovo);
                mesaref = databaseReference.child("mesas").child(mesa.getNumeroMesa());
                mesaref.setValue(mesa).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Sucesso ao fazer cadastro !", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(),FazerPedidosPratoActivity.class);
                        i.putExtra("mesa",mesa);
                        startActivity(i);
                        finish();
                    }
                });

            }
        });

    }
}
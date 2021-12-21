package com.example.sistemarestaurante.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;

public class AtualizarNomeCliente extends AppCompatActivity {

    //XML
    private EditText editNomeCliente;
    private Button buttonAtualizar;
    //Firebase
    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference mesaref;

    //model
    private Mesa mesa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_nome_cliente);

        editNomeCliente = findViewById(R.id.editNomeCliente);


        if (getIntent().getExtras() != null){
            mesa = (Mesa) getIntent().getExtras().getSerializable("mesaatualizar");
        }



    }

    public void AtualizarCliente (View view) {

        if (!editNomeCliente.getText().toString().isEmpty()) {
            mesa.setNomeCliente(editNomeCliente.getText().toString());
            mesaref = databaseReference.child("mesas").child(mesa.getNumeroMesa());
            mesaref.setValue(mesa).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Sucesso ao atualizar nome do cliente !" , Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Erro ao atualizar nome do cliente !" , Toast.LENGTH_LONG).show();

                }
            });
        }
        finish();
    }
}
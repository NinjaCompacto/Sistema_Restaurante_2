package com.example.sistemarestaurante.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sistemarestaurante.Model.Prato;
import com.example.sistemarestaurante.R;

public class CadastroPratoActicity extends AppCompatActivity {

    //XML
    private EditText editNomePrato,editValorPrato,editInfoPrato;
    private Button buttonAdicionar;
    private Prato prato = new Prato();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_prato_acticity);

        //configurações iniciais
        editNomePrato = findViewById(R.id.editNomeBebida);
        buttonAdicionar = findViewById(R.id.buttonAdicionarBebida);
        editValorPrato = findViewById(R.id.editValorBebida);
        editInfoPrato = findViewById(R.id.editInfoPrato);

        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //validação de campos
                if (!editNomePrato.getText().toString().isEmpty()) {
                    if(!editValorPrato.getText().toString().isEmpty()) {
                        if (!editInfoPrato.getText().toString().isEmpty()) {
                                Intent i = new Intent(CadastroPratoActicity.this, CadastroFotoPrato.class);
                                String nomeprato = editNomePrato.getText().toString();
                                String valorprato = editValorPrato.getText().toString();
                                String infoPrato = editInfoPrato.getText().toString();
                                prato.setValor(valorprato);
                                prato.setNomePrato(nomeprato);
                                prato.setIsDisponivel("true");
                                prato.setInfo(infoPrato);
                                i.putExtra("prato", prato);
                                startActivity(i);
                                finish();
                        }else{
                            Toast.makeText(CadastroPratoActicity.this,"Prencha a informação do prato",Toast.LENGTH_LONG).show();
                        }
                    }else {
                        Toast.makeText(CadastroPratoActicity.this,"Prencha o valor do prato",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(CadastroPratoActicity.this,"Prencha o nome do prato",Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
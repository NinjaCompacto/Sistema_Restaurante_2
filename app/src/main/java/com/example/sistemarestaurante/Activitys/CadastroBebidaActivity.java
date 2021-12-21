package com.example.sistemarestaurante.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sistemarestaurante.Model.Bebida;
import com.example.sistemarestaurante.R;

public class CadastroBebidaActivity extends AppCompatActivity {

    //XML
    private EditText editNomeBebida,editValorBebida;
    private Button buttonAdicionarBebida;

    //Model
    private Bebida bebida = new Bebida();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_bebida);

        //configuração inicial
        editNomeBebida = findViewById(R.id.editNomeBebida);
        editValorBebida = findViewById(R.id.editValorBebida);
        buttonAdicionarBebida = findViewById(R.id.buttonAdicionarBebida);


        //seta ação de abrir tela de foto
        buttonAdicionarBebida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //setando valores da bebida
                String nomebebida = editNomeBebida.getText().toString();
                String valorbebida = editValorBebida.getText().toString();
                bebida.setNomeBebida(nomebebida);
                bebida.setValor(valorbebida);
                bebida.setIsDisponivel("true");

                //iniciando uma nova activity
                Intent i = new Intent(CadastroBebidaActivity.this,CadastroFotoBebida.class);
                i.putExtra("bebida",bebida);
                finish();
                startActivity(i);
            }
        });

    }
}
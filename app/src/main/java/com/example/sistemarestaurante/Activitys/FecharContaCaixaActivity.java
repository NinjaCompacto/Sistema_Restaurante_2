package com.example.sistemarestaurante.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sistemarestaurante.Adapters.HistoricoPedidosCaixaAdapater;
import com.example.sistemarestaurante.Model.BebidaPedida;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.Model.Prato;
import com.example.sistemarestaurante.Model.PratoPedido;
import com.example.sistemarestaurante.R;

import java.text.DecimalFormat;
import java.util.List;

public class FecharContaCaixaActivity extends AppCompatActivity {

    private Mesa mesaSelecionada;

    //XML
    private Button buttonFecharConta;
    private TextView textValorTotal;
    private RecyclerView recyclerListaPedidosTotal;
    private HistoricoPedidosCaixaAdapater adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fechar_conta_caixa);

        getExtras();
        setElementsXML();
        configuraRecycler();
        configuraButton();
        configuraTextValorTotal();

    }

    private void getExtras () {
        if (getIntent().getExtras() != null){
            mesaSelecionada = (Mesa) getIntent().getExtras().getSerializable("mesa");
        }
    }

    private void setElementsXML () {
        buttonFecharConta = findViewById(R.id.buttonFecharConta);
        textValorTotal = findViewById(R.id.textValorTotal);
        recyclerListaPedidosTotal = findViewById(R.id.recyclerListaPedidosTotal);
    }

    private void  configuraRecycler(){
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new HistoricoPedidosCaixaAdapater(mesaSelecionada.getPedidos(),getApplicationContext());
        recyclerListaPedidosTotal.setHasFixedSize(true);
        recyclerListaPedidosTotal.setLayoutManager(layoutManager);
        recyclerListaPedidosTotal.setAdapter(adapter);
    }

    private void configuraTextValorTotal (){
        float valorTotal = calculaValorTotal();
        DecimalFormat df = new DecimalFormat("#####.00");
        textValorTotal.setText("R$ " +  String.valueOf(df.format(valorTotal))) ;
    }

    private float calculaValorTotal (){
        float valorTotal = 0;
        List<Pedido> listpedidos = mesaSelecionada.getPedidos();

        for (Pedido pedido : listpedidos){

            List<PratoPedido>  listPratosPedidos = pedido.getComida();
            List<BebidaPedida> listBebidasPedidas = pedido.getBebida();

            if (listPratosPedidos != null){
              for (PratoPedido pratoPedido : listPratosPedidos) {
                  int quantidade = pratoPedido.getQuantidade();
                  float valorIndividual = Float.parseFloat(pratoPedido.getPrato().getValor());
                  valorTotal = valorTotal + (quantidade*valorIndividual);
              }
            }

            if (listBebidasPedidas != null){
                for (BebidaPedida bebidaPedida : listBebidasPedidas){
                    int quantidade = bebidaPedida.getQuantidade();
                    float valorIndividual = Float.parseFloat(bebidaPedida.getBebida().getValor());
                    valorTotal = valorTotal + (quantidade*valorIndividual);
                }
            }
        }

        return  valorTotal;
    }

    private void configuraButton () {
        buttonFecharConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"CONTA FECHADA", Toast.LENGTH_LONG).show();
            }
        });
    }

}
package com.example.sistemarestaurante.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.example.sistemarestaurante.Adapters.HistoricoPedidosCaixaAdapater;
import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.R;

import java.util.List;

public class HistoricoPedidosCaixaActivity extends AppCompatActivity {

    //xml
    private TextView textInfoMesa;
    private RecyclerView recycleHistorico;
    private HistoricoPedidosCaixaAdapater historicoPedidosCaixaAdapater;
    //model
    private Mesa mesaSelecionada;
    private List<Pedido> listPedidos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_pedidos_caixa);

        recuperarExtras();
        configurartextInfo();
        if(listPedidos != null) {
            configurarRecycler();
        }
    }


    public void recuperarExtras () {
        if (getIntent().getExtras() != null){
            mesaSelecionada = (Mesa) getIntent().getExtras().getSerializable("mesa");
            if(mesaSelecionada.getPedidos() != null) {
                listPedidos = mesaSelecionada.getPedidos();
            }
        }
    }
    public void configurartextInfo (){
        if(mesaSelecionada != null) {
            textInfoMesa = findViewById(R.id.textInfoMesaHistoricoCixa);
            String textInfo = "Mesa: " + mesaSelecionada.getNumeroMesa();
            textInfoMesa.setText(textInfo);
        }
    }
    public void configurarRecycler () {
        if(mesaSelecionada != null) {
            historicoPedidosCaixaAdapater  = new HistoricoPedidosCaixaAdapater(listPedidos,getApplicationContext());
            recycleHistorico = findViewById(R.id.recyclerHistoricoMesaCaixa);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            recycleHistorico.setHasFixedSize(true);
            recycleHistorico.setLayoutManager(layoutManager);
            recycleHistorico.setAdapter(historicoPedidosCaixaAdapater);

        }
    }
}
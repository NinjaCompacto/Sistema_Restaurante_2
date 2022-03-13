package com.example.sistemarestaurante.Adapters;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.net.ipsec.ike.ChildSaProposal;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sistemarestaurante.Funcoes.CaixaActivity;
import com.example.sistemarestaurante.Model.BebidaPedida;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.Model.Prato;
import com.example.sistemarestaurante.Model.PratoPedido;
import com.example.sistemarestaurante.R;
import com.google.firebase.database.collection.LLRBNode;

import java.text.DecimalFormat;
import java.util.List;

public class HistoricoPedidosCaixaAdapater extends RecyclerView.Adapter<HistoricoPedidosCaixaAdapater.MyViewHolder> {

    private List<Pedido> listaPedidos;
    private String textInfo = " ";
    private Context context;

    public HistoricoPedidosCaixaAdapater(List<Pedido> pedidos, Context c) {
        this.listaPedidos = pedidos;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.historico_pedidos_caixa_adapater,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Pedido pedido = listaPedidos.get(position);
        configurarTexts(pedido,holder);

    }

    public void configurarTexts (@NonNull Pedido pedido, MyViewHolder holder){

        //configura texto de Status do pedido
        String statusComida = pedido.getComidaStauts();
        String statusBebida = pedido.getBebidaStauts();

        if (statusComida.contains("não tem")){
            holder.textStatus.setText(statusBebida);
            switch (statusBebida){
                case "pronto":
                    holder.textStatus.setTextColor(Color.GREEN);
                    break;
                case "preparando":
                    holder.textStatus.setTextColor(Color.rgb(255, 193, 7));
                    break;
                case "em aberto":
                    holder.textStatus.setTextColor(Color.RED);
                    break;
            }
        }

        if (statusBebida.contains("não tem")){
            holder.textStatus.setText(statusComida);
            switch (statusComida){
                case "pronto":
                    holder.textStatus.setTextColor(Color.GREEN);
                    break;
                case "preparando":
                    holder.textStatus.setTextColor(Color.rgb(255, 193, 7));
                    break;
                case "em aberto":
                    holder.textStatus.setTextColor(Color.RED);
                    break;
            }
        }

        if (!statusBebida.equals("não tem") && !statusComida.equals("não tem")){
            if (statusBebida.contains("pronto") && statusComida.contains("pronto")){
                holder.textStatus.setText("Pronto");
                holder.textStatus.setTextColor(Color.GREEN);
            }
            else {
                holder.textStatus.setText("Comida: " + statusComida + " Bebida: " + statusBebida);
                holder.textStatus.setTextColor(Color.rgb(255, 193, 7));
            }
        }

        List<BebidaPedida> listBebidas = pedido.getBebida();
        List<PratoPedido> listPratos = pedido.getComida();

        if (listPratos != null){
            textInfo = textInfo + "Comida: ";

            for (PratoPedido pratoPedido : listPratos){
                String nome = pratoPedido.getPrato().getNomePrato();
                textInfo = textInfo + nome + ", ";
            }
        }
        if (listBebidas !=  null){
            textInfo = textInfo + "Bebidas: ";
            for (BebidaPedida bebidaPedida : listBebidas){
                String nome = bebidaPedida.getBebida().getNomeBebida();
                textInfo = textInfo + nome + ", ";
            }
        }
        holder.textInfo.setText(textInfo);


        //configura texto de Valor
        DecimalFormat df =  new DecimalFormat("#####.00");
        String valor = "R$" + String.valueOf(df.format(calcularValorPedido(pedido)));
        holder.textValor.setText(valor);
        textInfo = "";


    }

    public Float calcularValorPedido (@NonNull Pedido pedido) {
        float valorPedido = 0;

        List <BebidaPedida> listBebidasPedidas = pedido.getBebida();
        List <PratoPedido> listPratosPedidos = pedido.getComida();

        if (listPratosPedidos != null){
            for (PratoPedido  pratoPedido : listPratosPedidos){
                float valorIndividual = Float.parseFloat(pratoPedido.getPrato().getValor());
                int quantidade = pratoPedido.getQuantidade();
                valorPedido = valorPedido + (quantidade*valorIndividual);
            }
        }
        if (listBebidasPedidas != null){
            for (BebidaPedida bebidaPedida : listBebidasPedidas){
                float valorIndividual = Float.parseFloat(bebidaPedida.getBebida().getValor());
                int quantidade = bebidaPedida.getQuantidade();
                valorPedido = valorPedido + (quantidade*valorIndividual);
            }
        }

        return  valorPedido;
    }

    @Override
    public int getItemCount() {
        return listaPedidos.size();
    }

    class  MyViewHolder  extends RecyclerView.ViewHolder {
        private TextView textStatus,textInfo,textValor;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textStatus = itemView.findViewById(R.id.textStatusPedidoHistoricoCaixa);
            textInfo = itemView.findViewById(R.id.textInfoHistoricoCaixa);
            textValor = itemView.findViewById(R.id.textValorPedidoHistoricoCaixa);
        }
    }

}
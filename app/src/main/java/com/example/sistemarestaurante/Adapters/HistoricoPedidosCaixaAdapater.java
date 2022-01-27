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

        //configura texto informação
        if (pedido.getBebida() == null){
            textInfo = textInfo + "Comida: ";
            for (PratoPedido pratoPedido : pedido.getComida()){
                textInfo = textInfo + pratoPedido.getPrato().getNomePrato() + ", ";
            }
            holder.textInfo.setText(textInfo);
        }
        if (pedido.getComida() == null){
            textInfo = textInfo + "Bebida: ";
            for (BebidaPedida bebidaPedida : pedido.getBebida()){
                textInfo = textInfo + bebidaPedida.getBebida().getNomeBebida() + ", ";
            }
            holder.textInfo.setText(textInfo);
        }
        if(pedido.getBebida() != null && pedido.getComida() != null) {
            textInfo = textInfo + "Comida: ";
            for (PratoPedido pratoPedido : pedido.getComida()){
                textInfo = textInfo + pratoPedido.getPrato().getNomePrato() + ", ";
            }
            textInfo = textInfo + "Bebida: ";
            for (BebidaPedida bebidaPedida : pedido.getBebida()){
                textInfo = textInfo + bebidaPedida.getBebida().getNomeBebida() + ", ";
            }
            holder.textInfo.setText(textInfo);
        }


        //configura texto de Valor
        DecimalFormat df =  new DecimalFormat("#####.00");
        String valor = "R$" + String.valueOf(df.format(calcularValorPedido(pedido)));
        holder.textValor.setText(valor);


    }

    public Float calcularValorPedido (@NonNull Pedido pedido) {
        float valorPedido = 0;
        if (pedido.getBebida() == null){
            for (PratoPedido pratoPedido : pedido.getComida()){
                valorPedido = valorPedido + Float.valueOf(pratoPedido.getPrato().getValor());
            }
        }
        else if (pedido.getComida() == null){
            for(BebidaPedida bebidaPedida : pedido.getBebida()){
                valorPedido = valorPedido + Float.valueOf(bebidaPedida.getBebida().getValor());
            }
        }
        else{
            for (PratoPedido pratoPedido : pedido.getComida()){
                valorPedido = valorPedido + Float.valueOf(pratoPedido.getPrato().getValor());
            }
            for(BebidaPedida bebidaPedida : pedido.getBebida()){
                valorPedido = valorPedido + Float.valueOf(bebidaPedida.getBebida().getValor());
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
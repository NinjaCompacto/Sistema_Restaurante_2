package com.example.sistemarestaurante.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Model.BebidaPedida;
import com.example.sistemarestaurante.Model.Pedido;
import com.example.sistemarestaurante.Model.PratoPedido;
import com.example.sistemarestaurante.R;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class ListaPedidosAdapter extends RecyclerView.Adapter<ListaPedidosAdapter.MyViewHolder> {

    private List<Pedido> pedidos;
    private Context context;
    private String textinfo;
    private String função;

    private DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
    private DatabaseReference mesaref ;

    public ListaPedidosAdapter(List<Pedido> pedidos, Context c,String função) {
        this.context = c;
        this.pedidos = pedidos;
        this.função = função;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemlista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lista_pratos_pedidos_cozinha,parent,false);
        return new MyViewHolder(itemlista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        textinfo = null;
        Pedido pedido = pedidos.get(position);

        holder.textPedido.setText("Pedido: Mesa" + pedido.getNumeroMesa());
        holder.textTime.setText(pedido.getTime());

        //configurando status incial
        if (função.equals("cozinha")) {
            if (pedido.getComidaStauts().contains("preparando")) {
                holder.textStatus.setTextColor(Color.rgb(255, 193, 7));
                holder.textStatus.setText("Status: Preparando");
            }
            if (pedido.getComidaStauts().contains("em aberto")) {
                holder.textStatus.setTextColor(Color.RED);
                holder.textStatus.setText("Status: Em Aberto");
            }
            if (pedido.getComidaStauts().contains("pronto")) {
                holder.textStatus.setTextColor(Color.GREEN);
                holder.textStatus.setText("Status: Pronto");
            }
        }
        if (função.equals("bar")){
            if (pedido.getBebidaStauts().contains("preparando")) {
                holder.textStatus.setTextColor(Color.rgb(255, 193, 7));
                holder.textStatus.setText("Status: Preparando");
            }
            if (pedido.getBebidaStauts().contains("em aberto")) {
                holder.textStatus.setTextColor(Color.RED);
                holder.textStatus.setText("Status: Em Aberto");
            }
            if (pedido.getBebidaStauts().contains("pronto")) {
                holder.textStatus.setTextColor(Color.GREEN);
                holder.textStatus.setText("Status: Pronto");
            }
        }

        //configurando texto de informações do pedido
        if (função.equals("cozinha")) {
            for (PratoPedido pratoPedido : pedido.getComida()) {
                if (textinfo == null) {
                    textinfo = pratoPedido.getPrato().getNomePrato();
                } else {
                    textinfo = textinfo + ", " + pratoPedido.getPrato().getNomePrato();
                }
            }
            holder.textinfoPrato.setText(textinfo);
        }
        if(função.equals("bar")){
            for (BebidaPedida bebidaPedida : pedido.getBebida()) {
                if (textinfo == null) {
                    textinfo = bebidaPedida.getBebida().getNomeBebida();
                } else {
                    textinfo = textinfo + ", " + bebidaPedida.getBebida().getNomeBebida();
                }
            }
            holder.textinfoPrato.setText(textinfo);
        }



    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView textPedido, textStatus,textinfoPrato,textTime;
        Button buttonPreparando, buttonPronto;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textinfoPrato = itemView.findViewById(R.id.textInfoPrato);
            textPedido = itemView.findViewById(R.id.textPedidoMesaCozinha);
            textTime = itemView.findViewById(R.id.textTimePratoPedido);
            textStatus= itemView.findViewById(R.id.textStatusPratoCozinha);

        }
    }
}

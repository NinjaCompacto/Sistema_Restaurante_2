package com.example.sistemarestaurante.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sistemarestaurante.Model.PratoPedido;
import com.example.sistemarestaurante.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListaPratosPedidosAdapter extends RecyclerView.Adapter<ListaPratosPedidosAdapter.MyViewHolder> {

    private List<PratoPedido> listaPratos;
    private Context context;

    public ListaPratosPedidosAdapter(List<PratoPedido>listaPratos, Context context) {
        this.listaPratos = listaPratos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pedido_prato,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            PratoPedido pratoPedido = listaPratos.get(position);

            holder.textNomeComida.setText(pratoPedido.getPrato().getNomePrato());
            holder.textQuantidadeComida.setText(pratoPedido.getQuantidade() + "x");



            if (pratoPedido.getObs() != null) {
                holder.textObsComida.setVisibility(View.VISIBLE);
                holder.textObsComida.setText("OBS: " + pratoPedido.getObs());
            }else {
                holder.textObsComida.setVisibility(View.GONE);
            }

            if (pratoPedido.getPrato().getFoto() != null){
                Glide.with(context).load(pratoPedido.getPrato().getFoto()).into(holder.circleImageView);
            }
    }

    @Override
    public int getItemCount() {
        return listaPratos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textNomeComida,textQuantidadeComida,textObsComida;
        private CircleImageView circleImageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textNomeComida = itemView.findViewById(R.id.textNomeComida);
            textQuantidadeComida = itemView.findViewById(R.id.textQuantidadeComida);
            textObsComida = itemView.findViewById(R.id.textObsComida);
            circleImageView =  itemView.findViewById(R.id.circleImageComidaPedido);
        }
    }
}

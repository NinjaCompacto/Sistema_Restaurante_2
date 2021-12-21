package com.example.sistemarestaurante.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sistemarestaurante.Model.BebidaPedida;
import com.example.sistemarestaurante.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListaBebidasPedidasAdapter extends RecyclerView.Adapter<ListaBebidasPedidasAdapter.MyViewHolder>  {

    private List<BebidaPedida> listaBebidas;
    private Context context;

    public ListaBebidasPedidasAdapter(List<BebidaPedida> listaBebidas, Context context) {
        this.listaBebidas = listaBebidas;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pedido_prato,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BebidaPedida bebida = listaBebidas.get(position);

        //seta elementos xml
        holder.textNomeBebida.setText(bebida.getBebida().getNomeBebida());
        holder.textQuantidadeBebida.setText(String.valueOf(bebida.getQuantidade()));
        if (bebida.getObs() != null) {
            holder.textObsBebida.setVisibility(View.VISIBLE);
            holder.textObsBebida.setText("OBS: " + bebida.getObs());
        }else {
            holder.textObsBebida.setVisibility(View.GONE);
        }
        if (bebida.getBebida().getFoto() != null){
            Glide.with(context).load(bebida.getBebida().getFoto()).into(holder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return listaBebidas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textNomeBebida, textQuantidadeBebida, textObsBebida;
        private CircleImageView circleImageView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        textNomeBebida = itemView.findViewById(R.id.textNomeComida);
        textQuantidadeBebida = itemView.findViewById(R.id.textQuantidadeComida);
        textObsBebida = itemView.findViewById(R.id.textObsComida);
        circleImageView =  itemView.findViewById(R.id.circleImageComidaPedido);
    }
}
}


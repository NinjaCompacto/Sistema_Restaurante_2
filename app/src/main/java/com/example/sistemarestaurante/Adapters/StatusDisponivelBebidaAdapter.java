package com.example.sistemarestaurante.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sistemarestaurante.Model.Bebida;
import com.example.sistemarestaurante.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusDisponivelBebidaAdapter extends RecyclerView.Adapter<StatusDisponivelBebidaAdapter.MyViewHolderDisponivel> {

    private List<Bebida> bebidasDisponiveis;
    private Context context;

    public StatusDisponivelBebidaAdapter(List<Bebida>listapratos , Context c) {
        this.bebidasDisponiveis = listapratos;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolderDisponivel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemlista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_status_disponivel,parent,false);
        return new MyViewHolderDisponivel(itemlista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderDisponivel holder, int position) {
        Bebida bebida = bebidasDisponiveis.get(position);
        holder.texNomePrato.setText(bebida.getNomeBebida());
        holder.textValor.setText("R$ " + bebida.getValor());

        if(bebida.getFoto() != null){
            Glide.with(context).load(bebida.getFoto()).into(holder.circleImageView);
        }

    }

    @Override
    public int getItemCount() {
        return bebidasDisponiveis.size();
    }

    public class MyViewHolderDisponivel extends RecyclerView.ViewHolder {

        private CircleImageView circleImageView;
        private TextView texNomePrato, textValor;

        public MyViewHolderDisponivel(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.circleImagePratoStatus);
            texNomePrato = itemView.findViewById(R.id.textNomePratoDisponivel);
            textValor = itemView.findViewById(R.id.textValorPratoDisponivel);


        }
    }
}

package com.example.sistemarestaurante.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sistemarestaurante.Model.Prato;
import com.example.sistemarestaurante.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusDisponivelAdapter extends RecyclerView.Adapter<StatusDisponivelAdapter.MyViewHolderDisponivel> {

    private List<Prato> pratosDisponiveis;
    private Context context;

    public StatusDisponivelAdapter(List<Prato>listapratos , Context c) {
        this.pratosDisponiveis = listapratos;
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
            Prato prato = pratosDisponiveis.get(position);
            holder.texNomePrato.setText(prato.getNomePrato());
            holder.textValor.setText("R$ " + prato.getValor());

            if(prato.getFoto() != null){
                Glide.with(context).load(prato.getFoto()).into(holder.circleImageView);
            }

    }

    @Override
    public int getItemCount() {
        return pratosDisponiveis.size();
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

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
import com.example.sistemarestaurante.Firebase.ConfiguracaoFirebase;
import com.example.sistemarestaurante.Model.Bebida;
import com.example.sistemarestaurante.Model.Prato;
import com.example.sistemarestaurante.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatusIndisponivelBebidaAdapter  extends RecyclerView.Adapter<StatusIndisponivelBebidaAdapter.MyViewHolderIndisponivel> {

    private List<Bebida> BebidaIndisponiveis;
    private Context context;

    public StatusIndisponivelBebidaAdapter(List<Bebida> listaIndisponiveis , Context c) {
        this.context = c;
        this.BebidaIndisponiveis = listaIndisponiveis;

    }

    @NonNull
    @Override
    public MyViewHolderIndisponivel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemlista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_status_disponivel,parent,false);
        return new MyViewHolderIndisponivel(itemlista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolderIndisponivel holder, int position) {
        Bebida bebida = BebidaIndisponiveis.get(position);
        holder.texNomePrato.setText(bebida.getNomeBebida());
        holder.textValor.setText("R$ " + bebida.getValor());
        holder.textStatus.setTextColor(Color.RED);
        holder.textStatus.setText("Indisponível");


        if(bebida.getFoto() != null){
            Glide.with(context).load(bebida.getFoto()).into(holder.circleImageView);
        }

    }

    @Override
    public int getItemCount() {
        return BebidaIndisponiveis.size();
    }

    public class MyViewHolderIndisponivel extends RecyclerView.ViewHolder {
        private CircleImageView circleImageView;
        private TextView texNomePrato, textValor,textStatus;

        public MyViewHolderIndisponivel(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.circleImagePratoStatus);
            texNomePrato = itemView.findViewById(R.id.textNomePratoDisponivel);
            textValor = itemView.findViewById(R.id.textValorPratoDisponivel);
            textStatus = itemView.findViewById(R.id.textStatusPrato);
        }
    }
}

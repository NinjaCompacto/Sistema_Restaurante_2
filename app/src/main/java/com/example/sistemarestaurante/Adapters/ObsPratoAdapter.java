package com.example.sistemarestaurante.Adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sistemarestaurante.Model.PratoPedido;
import com.example.sistemarestaurante.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ObsPratoAdapter extends RecyclerView.Adapter<ObsPratoAdapter.MyViewHoderObsPratos> {

    Context context;
    List<PratoPedido> pedidos;
    List<PratoPedido> pedidosComObs = new ArrayList<>();

    public ObsPratoAdapter(List<PratoPedido> pratoPedidos , Context c) {
        this.context = c;
        this.pedidos = pratoPedidos;
    }

    @NonNull
    @Override
    public MyViewHoderObsPratos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemlista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_obs_prato,parent,false);
        return new MyViewHoderObsPratos(itemlista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoderObsPratos holder, @SuppressLint("RecyclerView") int position) {
        PratoPedido pratoPedido = pedidos.get(position);
        pedidosComObs.add(pratoPedido);
        holder.textNomePratoObs.setText(pratoPedido.getPrato().getNomePrato());
        holder.textValorPratoObs.setText("R$ " + pratoPedido.getPrato().getValor());
        if (pratoPedido.getPrato().getFoto() != null){
            Glide.with(context).load(pratoPedido.getPrato().getFoto()).into(holder.circleImagePratoObs);
        }

        holder.buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PratoPedido pratoPedido1 = pedidosComObs.get(position);
                pratoPedido1.setObs(holder.editTextObsPrato.getText().toString());
                Toast.makeText(context,"Observação Adicionada !", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return pedidos.size();
    }

    public class MyViewHoderObsPratos extends RecyclerView.ViewHolder {

        CircleImageView circleImagePratoObs;
        TextView textNomePratoObs, textValorPratoObs;
        EditText editTextObsPrato;
        Button buttonAdicionar;
        public MyViewHoderObsPratos(@NonNull View itemView) {
            super(itemView);

            circleImagePratoObs = itemView.findViewById(R.id.circleImagePratoObs);
            textNomePratoObs = itemView.findViewById(R.id.textNomepratoObs);
            textValorPratoObs = itemView.findViewById(R.id.textValorPratoObs);
            editTextObsPrato = itemView.findViewById(R.id.editTextObsPrato);
            buttonAdicionar = itemView.findViewById(R.id.buttonAdicionarObs);
        }
    }

    public List<PratoPedido> recuperaListaAtualizada () {
        return pedidosComObs;
    }

}
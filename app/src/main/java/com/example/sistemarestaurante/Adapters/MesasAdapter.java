package com.example.sistemarestaurante.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sistemarestaurante.Model.Mesa;
import com.example.sistemarestaurante.R;

import java.util.List;

public class MesasAdapter extends RecyclerView.Adapter<MesasAdapter.MyViewHolder> {

    private List<Mesa> mesas;
    private Context context;

    public MesasAdapter(List<Mesa> mesaList, Context c) {
        this.mesas = mesaList;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemlista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mesas,parent,false);
        return new MyViewHolder(itemlista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Mesa mesa =mesas.get(position);
        holder.textNumeromesa.setText(mesa.getNumeroMesa());

    }

    @Override
    public int getItemCount() {
        return mesas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textNumeromesa;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textNumeromesa = itemView.findViewById(R.id.textNumeroMesa);
        }
    }
}

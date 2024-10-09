package com.example.gestion_biblioteca.Gastos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion_biblioteca.R;

import java.util.List;

public class GastosAdapter extends RecyclerView.Adapter<GastosAdapter.GastoViewHolder> {

    private  List<Gastos> gastosList;
    private OnItemClickListener onItemClickListener;


    public interface OnItemClickListener {
        void onItemClick(Gastos gasto);
    }

    public GastosAdapter(List<Gastos> gastos,  OnItemClickListener listener) {
        this.gastosList = gastos;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public GastoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gasto, parent, false);
        return new GastoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GastoViewHolder holder, int position) {
        Gastos gasto = gastosList.get(position);
        holder.motivoTextView.setText(gasto.getMotivo());
        holder.montoTextView.setText(gasto.getMonto());
        holder.notaTextView.setText(gasto.getObservaciones());
        holder.fechaTextView.setText(gasto.getFecha().toString());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(gasto));
    }

    @Override
    public int getItemCount() {
        return gastosList.size();
    }


    public static class GastoViewHolder extends RecyclerView.ViewHolder {
         TextView motivoTextView,montoTextView,notaTextView,fechaTextView;

        public GastoViewHolder(@NonNull View itemView) {
            super(itemView);
            motivoTextView = itemView.findViewById(R.id.txtMotivo);
            montoTextView = itemView.findViewById(R.id.txtMonto);
            notaTextView = itemView.findViewById(R.id.txtNota);
            fechaTextView = itemView.findViewById(R.id.txtFecha);
        }
    }
}

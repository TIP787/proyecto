package com.example.gestion_biblioteca.Ingresos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion_biblioteca.R;

import java.util.List;

public class IngresosAdapter extends RecyclerView.Adapter<IngresosAdapter.IngresosViewHolder> {

    private List<Ingresos> ingresosList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Ingresos ingreso);
    }

    public IngresosAdapter(List<Ingresos> ingresos,  OnItemClickListener listener) {
        this.ingresosList = ingresos;
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public IngresosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingresos, parent, false);
        return new IngresosViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull IngresosViewHolder holder, int position) {
        Ingresos ingreso = ingresosList.get(position);
        holder.motivoTextView.setText(ingreso.getMotivo());
        holder.montoTextView.setText(String.valueOf(ingreso.getMonto()));
        holder.notaTextView.setText(ingreso.getObservaciones());
        holder.fechaTextView.setText(ingreso.getFecha());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(ingreso));
    }

    @Override
    public int getItemCount() {
        return ingresosList.size();
    }

    public static class IngresosViewHolder extends RecyclerView.ViewHolder {
        TextView motivoTextView, montoTextView,notaTextView,fechaTextView;

        public IngresosViewHolder(@NonNull View itemView) {
            super(itemView);
            motivoTextView = itemView.findViewById(R.id.motivoTextView);
            montoTextView = itemView.findViewById(R.id.montoTextView);
            notaTextView = itemView.findViewById(R.id.notaTextView);
            fechaTextView = itemView.findViewById(R.id.fechaTextView);
        }
    }
}

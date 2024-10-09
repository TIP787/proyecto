package com.example.gestion_biblioteca.Cuotas;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestion_biblioteca.R;
import java.util.List;

public class PagosAdapter extends RecyclerView.Adapter<PagosAdapter.PagoViewHolder>  {

    private List<Pagos> pagosList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Pagos pago);
    }

    public PagosAdapter(List<Pagos> pagosList, OnItemClickListener listener) {
        this.pagosList = pagosList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PagoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuotas, parent, false);
        return new PagoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PagoViewHolder holder, int position) {
        Pagos pago = pagosList.get(position);
        holder.tvNombre.setText(pago.getNombre());
        holder.tvUltimoPago.setText(pago.getFecha());
        holder.tvMonto.setText(pago.getMonto());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(pago));
    }

    @Override
    public int getItemCount() {
        return pagosList.size();
    }

    public static class PagoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvUltimoPago, tvMonto;

        public PagoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvUltimoPago = itemView.findViewById(R.id.tvUltimoPago);
            tvMonto = itemView.findViewById(R.id.tvMonto);
        }
    }
}
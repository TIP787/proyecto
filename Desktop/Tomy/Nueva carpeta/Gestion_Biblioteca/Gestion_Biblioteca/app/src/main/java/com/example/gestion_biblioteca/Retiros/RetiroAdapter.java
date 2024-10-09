package com.example.gestion_biblioteca.Retiros;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestion_biblioteca.R;
import java.util.List;


public class RetiroAdapter extends RecyclerView.Adapter<RetiroAdapter.RetiroViewHolder> {

    private List<Retiros> retiroList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Retiros retiro);
    }

    public RetiroAdapter(List<Retiros> retiroList, OnItemClickListener listener) {
        this.retiroList = retiroList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RetiroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_retiros, parent, false);
        return new RetiroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RetiroViewHolder holder, int position) {
        Retiros retiro = retiroList.get(position);
        holder.tvNombreLibro.setText((retiro.getLibro()));
        holder.tvNombreSocio.setText(retiro.getNombre());
        holder.tvFechaRetiro.setText(retiro.getFecha());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(retiro));
    }

    @Override
    public int getItemCount() {
        return retiroList.size();
    }

    public static class RetiroViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreLibro, tvNombreSocio, tvFechaRetiro;

        public RetiroViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreLibro = itemView.findViewById(R.id.tvNombreLibro);
            tvNombreSocio = itemView.findViewById(R.id.tvNombreSocio);
            tvFechaRetiro = itemView.findViewById(R.id.tvFechaRetiro);
        }
    }

}
package com.example.gestion_biblioteca.Categorias;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion_biblioteca.R;

import java.util.List;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.CategoriaViewHolder> {

    private List<Categorias> categoriaList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Categorias categoria);
    }

    public CategoriasAdapter(List<Categorias> categoriaList, OnItemClickListener listener) {
        this.categoriaList = categoriaList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoriaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categorias, parent, false);
        return new CategoriaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriaViewHolder holder, int position) {
        Categorias categoria = categoriaList.get(position);
        holder.tvNombreCategoria.setText(categoria.getCategoria());
        Log.d("CategoriaAdapter", "Categoría: " + categoria.getCategoria());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(categoria));
    }

    @Override
    public int getItemCount() {
        return categoriaList.size();
    }

    public static class CategoriaViewHolder extends RecyclerView.ViewHolder {
         TextView tvNombreCategoria;

        public CategoriaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreCategoria = itemView.findViewById(R.id.tvNombreCategoria);
        }
    }
}
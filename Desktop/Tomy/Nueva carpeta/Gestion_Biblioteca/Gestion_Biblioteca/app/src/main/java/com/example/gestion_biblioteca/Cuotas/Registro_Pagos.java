package com.example.gestion_biblioteca.Cuotas;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gestion_biblioteca.ApiService;
import com.example.gestion_biblioteca.R;
import com.example.gestion_biblioteca.RetrofitClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Registro_Pagos extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Pagos> pagoList = new ArrayList<>();
    private List<Pagos> filteredPagosList = new ArrayList<>();
    private PagosAdapter pagoAdapter;
    private EditText buscador;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_pagos);

        Retrofit retrofit = RetrofitClient.getClient("http://192.168.1.44:8080/Puente/");
        apiService = retrofit.create(ApiService.class);

        recyclerView = findViewById(R.id.recyclerViewRP);
        buscador = findViewById(R.id.buscadorRP);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        pagoAdapter = new PagosAdapter(filteredPagosList, pago -> {
            Intent intent = new Intent(Registro_Pagos.this, Pagos_Especificos.class);
            intent.putExtra("pago", pago);
            intent.putExtra("pagoId",pago.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(pagoAdapter);

        // Inicializa la lista de pagos
        obtenerDatosDesdeLaBaseDeDatos();

        buscador.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtenerDatosDesdeLaBaseDeDatos(); // Volver a cargar la lista de pagos al regresar a esta actividad
    }

    private void filter(String text) {
        filteredPagosList.clear();
        for (Pagos pagos : pagoList) {
            // Verifica si el nombre del socio contiene el texto buscado (ignorando mayúsculas/minúsculas)
            if (pagos.getNombre().toLowerCase().contains(text.toLowerCase())) {
                filteredPagosList.add(pagos); // Añadir el pago a la lista filtrada si coincide
            }
        }
        pagoAdapter.notifyDataSetChanged(); // Notificar al adaptador para actualizar la vista
    }

    private void obtenerDatosDesdeLaBaseDeDatos() {
        Call<List<Pagos>> call = apiService.getCuotas();
        call.enqueue(new Callback<List<Pagos>>() {
            @Override
            public void onResponse(Call<List<Pagos>> call, Response<List<Pagos>> response) {
                if (response.isSuccessful()) {
                    List<Pagos> pagos = response.body();
                    if (pagos != null) {
                        pagoList.clear();
                        pagoList.addAll(pagos);
                        filteredPagosList.clear();
                        filteredPagosList.addAll(pagos);
                        pagoAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("Registro_Pagos", "Error en la respuesta de la API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Pagos>> call, Throwable t) {
                Log.e("Registro_Pagos", "Error en la llamada a la API", t);
            }
        });
    }
}

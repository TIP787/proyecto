package com.example.gestion_biblioteca.Retiros;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

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

public class Libros_Retirados extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RetiroAdapter retiroAdapter;
    private EditText searchView;
    private List<Retiros> originalRetiroList = new ArrayList<>();
    private List<Retiros> filteredRetiroList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libros_retirados);

        // Inicializa Retrofit
        Retrofit retrofit = RetrofitClient.getClient("http://192.168.1.44:8080/Puente/");
        apiService = retrofit.create(ApiService.class);

        searchView = findViewById(R.id.buscadorRetiro);
        recyclerView = findViewById(R.id.recyclerViewRetiro);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        retiroAdapter = new RetiroAdapter(filteredRetiroList, retiro -> {
            Intent intent = new Intent(Libros_Retirados.this, Retiro_Especifico.class);
            intent.putExtra("retiro", retiro);
            intent.putExtra("retiroId", retiro.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(retiroAdapter);

        // Cargar los datos de retiros
        fetchRetiros();

        // Configurar el filtro de búsqueda
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchRetiros(); // Volver a cargar la lista de retiros al regresar a esta actividad
    }

    private void filter(String text) {
        filteredRetiroList.clear();
        for (Retiros retiro : originalRetiroList) {
            if (retiro.getLibro().toLowerCase().contains(text.toLowerCase())) {
                filteredRetiroList.add(retiro);
            }
        }
        retiroAdapter.notifyDataSetChanged();
    }

    private void fetchRetiros() {
        Call<List<Retiros>> call = apiService.getRetiros();
        call.enqueue(new Callback<List<Retiros>>() {
            @Override
            public void onResponse(Call<List<Retiros>> call, Response<List<Retiros>> response) {
                if (response.isSuccessful()) {
                    List<Retiros> retiros = response.body();
                    if (retiros != null) {
                        originalRetiroList.clear();
                        originalRetiroList.addAll(retiros);
                        filteredRetiroList.clear();
                        filteredRetiroList.addAll(retiros);
                        retiroAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("Lista_Retiros", "Error en la respuesta de la API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Retiros>> call, Throwable t) {
                Log.e("Lista_Retiros", "Error en la llamada a la API", t);
            }
        });
    }
}
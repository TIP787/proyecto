package com.example.gestion_biblioteca.Socios;

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

public class Lista_Socios extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SocioAdapter socioAdapter;
    private EditText searchView;
    private List<Socios> originalSociosList = new ArrayList<>();
    private List<Socios> filteredSociosList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_socios);

        // Inicializa Retrofit
        Retrofit retrofit = RetrofitClient.getClient("http://192.168.1.44:8080/Puente/");
        apiService = retrofit.create(ApiService.class);

        searchView = findViewById(R.id.buscador);
        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        socioAdapter = new SocioAdapter(filteredSociosList, socio -> {
            Intent intent = new Intent(Lista_Socios.this, Socio_Especifico.class);
            intent.putExtra("socio", socio);
            intent.putExtra("socioId", socio.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(socioAdapter);

        // Cargar los datos
        fetchSocios();

        // Configura la búsqueda
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
        fetchSocios(); // Volver a cargar la lista de socios al regresar a esta actividad
    }

    private void filter(String text) {
        filteredSociosList.clear();
        for (Socios socio : originalSociosList) {
            if (socio.getNombre().toLowerCase().contains(text.toLowerCase())) {
                filteredSociosList.add(socio);
            }
        }
        socioAdapter.notifyDataSetChanged();
    }

    private void fetchSocios() {
        Call<List<Socios>> call = apiService.getSocios();
        call.enqueue(new Callback<List<Socios>>() {
            @Override
            public void onResponse(Call<List<Socios>> call, Response<List<Socios>> response) {
                if (response.isSuccessful()) {
                    List<Socios> socios = response.body();
                    if (socios != null) {
                        originalSociosList.clear();
                        originalSociosList.addAll(socios);
                        filteredSociosList.clear();
                        filteredSociosList.addAll(socios);
                        socioAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("Lista_Socios", "Error en la respuesta de la API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Socios>> call, Throwable t) {
                Log.e("Lista_Socios", "Error en la llamada a la API", t);
            }
        });
    }
}

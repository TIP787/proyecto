package com.example.gestion_biblioteca.Inventarios;

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

public class Lista_Inventarios extends AppCompatActivity {

    private EditText buscadorI;
    private RecyclerView recyclerViewI;
    private LibrosAdapter libroAdapter;
    private List<Libros> listaLibros = new ArrayList<>();
    private List<Libros> listaLibrosFiltrados = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_inventarios);

        // Inicializa Retrofit
        Retrofit retrofit = RetrofitClient.getClient("http://192.168.1.44:8080/Puente/");
        apiService = retrofit.create(ApiService.class);

        buscadorI = findViewById(R.id.buscadorI);
        recyclerViewI = findViewById(R.id.recyclerViewI);
        recyclerViewI.setLayoutManager(new LinearLayoutManager(this));

        libroAdapter = new LibrosAdapter(listaLibrosFiltrados, libro -> {
            Intent intent = new Intent(Lista_Inventarios.this, Libros_Especificos.class);
            intent.putExtra("libro", libro);
            intent.putExtra("libroId", libro.getId());
            startActivity(intent);
        });

        recyclerViewI.setAdapter(libroAdapter);

        // Cargar libros desde la base de datos
        CargaInventarios();

        // Configurar el buscador
        buscadorI.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarLibros(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        CargaInventarios();
    }

    private void filtrarLibros(String text) {
        listaLibrosFiltrados.clear();
        for (Libros libro : listaLibros) {
            if (libro.getNombre().toLowerCase().contains(text.toLowerCase())) {
                listaLibrosFiltrados.add(libro);
            }
        }
        libroAdapter.notifyDataSetChanged();
    }
    private void CargaInventarios(){
        Call<List<Libros>> call = apiService.getLibros();
        call.enqueue(new Callback<List<Libros>>() {
            @Override
            public void onResponse(Call<List<Libros>> call, Response<List<Libros>> response) {
                if (response.isSuccessful()) {
                    List<Libros> libros = response.body();
                    if (libros != null) {
                        listaLibros.clear();
                        listaLibros.addAll(libros);
                        listaLibrosFiltrados.clear();
                        listaLibrosFiltrados.addAll(libros);
                        libroAdapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                    }
                } else {
                    Log.e("Lista_Libros", "Error en la respuesta de la API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Libros>> call, Throwable t) {
                Log.e("Lista_Libros", "Error en la llamada a la API", t);
            }
        });
    }

}

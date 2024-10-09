package com.example.gestion_biblioteca.Categorias;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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

public class FrmCategorias extends AppCompatActivity {

    private EditText etCategoria;
    private Button btnGuardarCategoria, btnEliminarCategoria;
    private RecyclerView recyclerViewCategorias;
    private CategoriasAdapter categoriaAdapter;
    private List<Categorias> OriginalCategoriaList = new ArrayList<>();
    private List<Categorias> filteredCategoriasList = new ArrayList<>();
    private Categorias categoriaSeleccionada = null;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_categorias);

        // Configurar Retrofit
        Retrofit retrofit = RetrofitClient.getClient("http://192.168.1.44:8080/Puente/");
        apiService = retrofit.create(ApiService.class);

        // Inicializar vistas
        etCategoria = findViewById(R.id.etCategoria);
        btnGuardarCategoria = findViewById(R.id.btnGuardarCategoria);
        btnEliminarCategoria = findViewById(R.id.btnEliminarCategoria);
        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias);

        // Configurar RecyclerView
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this));
        categoriaAdapter = new CategoriasAdapter(filteredCategoriasList, categoria -> {
            categoriaSeleccionada = categoria;
            etCategoria.setText(categoria.getCategoria());  // Mostrar el nombre en el EditText
        });
        recyclerViewCategorias.setAdapter(categoriaAdapter);

        // Añadir TextWatcher para la funcionalidad de búsqueda
        etCategoria.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchText = s.toString().trim();
                filterCategorias(searchText);
            }
        });

        cargarCategorias();

        // Configurar listeners para los botones
        btnGuardarCategoria.setOnClickListener(v -> {
            String categoria = etCategoria.getText().toString().trim();
            if (!categoria.isEmpty()) {
                guardarCategoria();
                etCategoria.setText("");  // Limpiar el campo después de guardar
                // El TextWatcher actualizará la lista mostrada
            } else {
                Toast.makeText(FrmCategorias.this, "Categoría inválida o ya existente", Toast.LENGTH_SHORT).show();
            }
        });

        btnEliminarCategoria.setOnClickListener(v -> {
            String nombreCategoria = etCategoria.getText().toString().trim();
            eliminarCategoria(nombreCategoria);
            etCategoria.setText("");  // Limpiar el campo después de eliminar
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarCategorias();
    }

    private void guardarCategoria() {
        String categoria = etCategoria.getText().toString().trim();

        // Verificar si el campo está vacío
        if (categoria.isEmpty()) {
            Toast.makeText(FrmCategorias.this, "La categoría no puede estar vacía", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si la categoría ya existe en la lista original
        for (Categorias existingCategoria : OriginalCategoriaList) {
            if (existingCategoria.getCategoria().equalsIgnoreCase(categoria)) {
                Toast.makeText(FrmCategorias.this, "La categoría ya existe", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Si la categoría no existe, proceder a guardarla
        Categorias nuevaCategoria = new Categorias(null, categoria);

        // Realizar la solicitud POST para crear una nueva categoría
        Call<Categorias> call = apiService.createCategoria(nuevaCategoria);

        call.enqueue(new Callback<Categorias>() {
            @Override
            public void onResponse(Call<Categorias> call, Response<Categorias> response) {
                if (response.isSuccessful()) {
                    Categorias categoriaCreada = response.body();
                    if (categoriaCreada != null) {
                        // Agregar la nueva categoría a la lista original
                        OriginalCategoriaList.add(categoriaCreada);
                        // Aplicar el filtro actual para actualizar la lista mostrada
                        String currentFilter = etCategoria.getText().toString().trim();
                        filterCategorias(currentFilter);
                        Toast.makeText(getApplicationContext(), "Categoría creada exitosamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error: No se pudo obtener la categoría creada", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Categorias", "Error en la respuesta: Código " + response.code() + " - " + response.message());
                    Toast.makeText(getApplicationContext(), "Error en la respuesta: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Categorias> call, Throwable t) {
                Log.e("Retrofit Error", "Error en la solicitud: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Error en la solicitud: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void eliminarCategoria(String nombreCategoria) {
        if (nombreCategoria.isEmpty()) {
            Toast.makeText(FrmCategorias.this, "El nombre de la categoría no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        // Buscar la categoría con el nombre especificado en la lista original
        Categorias categoriaAEliminar = null;
        for (Categorias categoria : OriginalCategoriaList) {
            if (categoria.getCategoria().equalsIgnoreCase(nombreCategoria)) {
                categoriaAEliminar = categoria;
                break;
            }
        }

        if (categoriaAEliminar != null) {
            // Realizar la solicitud DELETE para eliminar la categoría
            Call<Void> call = apiService.deleteCategoria(categoriaAEliminar.getId());
            Categorias finalCategoriaAEliminar = categoriaAEliminar;  // Para uso dentro del callback
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Eliminar la categoría de la lista original
                        OriginalCategoriaList.remove(finalCategoriaAEliminar);
                        // Aplicar el filtro actual para actualizar la lista mostrada
                        String currentFilter = etCategoria.getText().toString().trim();
                        filterCategorias(currentFilter);
                        Toast.makeText(getApplicationContext(), "Categoría eliminada exitosamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("FrmCategorias", "Error en la respuesta de la API: " + response.message());
                        Toast.makeText(getApplicationContext(), "Error al eliminar la categoría", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("FrmCategorias", "Error en la llamada a la API", t);
                    Toast.makeText(getApplicationContext(), "Error en la solicitud", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(FrmCategorias.this, "Categoría no encontrada", Toast.LENGTH_SHORT).show();
        }
    }

    private void cargarCategorias() {
        Call<List<Categorias>> call = apiService.getCategorias();
        call.enqueue(new Callback<List<Categorias>>() {
            @Override
            public void onResponse(Call<List<Categorias>> call, Response<List<Categorias>> response) {
                if (response.isSuccessful()) {
                    List<Categorias> categorias = response.body();
                    if (categorias != null) {
                        OriginalCategoriaList.clear();
                        OriginalCategoriaList.addAll(categorias);
                        // Aplicar el filtro actual para actualizar la lista mostrada
                        String currentFilter = etCategoria.getText().toString().trim();
                        filterCategorias(currentFilter);
                    }
                } else {
                    Log.e("FrmCategorias", "Error en la respuesta de la API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Categorias>> call, Throwable t) {
                Log.e("FrmCategorias", "Error en la llamada a la API", t);
            }
        });
    }

    private void filterCategorias(String searchText) {
        filteredCategoriasList.clear();
        if (searchText.isEmpty()) {
            filteredCategoriasList.addAll(OriginalCategoriaList);
        } else {
            String lowerCaseSearchText = searchText.toLowerCase();
            for (Categorias categoria : OriginalCategoriaList) {
                if (categoria.getCategoria().toLowerCase().contains(lowerCaseSearchText)) {
                    filteredCategoriasList.add(categoria);
                }
            }
        }
        categoriaAdapter.notifyDataSetChanged();
    }
}

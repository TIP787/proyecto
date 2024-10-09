package com.example.gestion_biblioteca.Inventarios;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestion_biblioteca.ApiService;
import com.example.gestion_biblioteca.Formulario_Principal;
import com.example.gestion_biblioteca.R;
import com.example.gestion_biblioteca.RetrofitClient;
import com.google.gson.Gson;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FrmInventarios extends AppCompatActivity {

    private EditText etNombre, etCodigo, etCategoria, etUbicacion;
    private Button btnAtras, btnGuardar, btnInventario;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_inventarios);

        // Inicializa los elementos de la vista
        etNombre = findViewById(R.id.etNombreI);
        etCodigo = findViewById(R.id.etCodigoI);
        etCategoria = findViewById(R.id.etCategoriaI);
        etUbicacion = findViewById(R.id.etUbicacionI);
        btnAtras = findViewById(R.id.btnAtrasI);
        btnGuardar = findViewById(R.id.btnGuardarI);
        btnInventario = findViewById(R.id.btnInventario);

        // Configurar Retrofit
        Retrofit retrofit = RetrofitClient.getClient("http://192.168.1.44:8080/Puente/");
        apiService = retrofit.create(ApiService.class);

        // Configura el botón "Atrás"
        btnAtras.setOnClickListener(v -> {
            Intent intent = new Intent(FrmInventarios.this, Formulario_Principal.class);
            startActivity(intent);
        });

        // Configura el botón "Guardar"
        btnGuardar.setOnClickListener(v -> {
            if (validateFields()) {
                guardarLibroEnBD();
                limpiarCampos();
            }
        });

        // Configura el botón "Inventario"
        btnInventario.setOnClickListener(v -> {
            Intent intent = new Intent(FrmInventarios.this, Lista_Inventarios.class);
            startActivity(intent);
        });
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etCodigo.setText("");
        etCategoria.setText("");
        etUbicacion.setText("");
    }

    private boolean validateFields() {
        String nombre = etNombre.getText().toString().trim();
        String codigo = etCodigo.getText().toString().trim();
        String categoria = etCategoria.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();

        if (nombre.length() < 3) {
            etNombre.setError("El nombre debe tener al menos 3 caracteres");
            return false;
        }

        if (TextUtils.isEmpty(codigo) || !Pattern.compile("\\d+").matcher(codigo).matches()) {
            etCodigo.setError("Código inválido");
            return false;
        }

        if (TextUtils.isEmpty(categoria)) {
            etCategoria.setError("La categoría no puede estar vacía");
            return false;
        }

        if (TextUtils.isEmpty(ubicacion)) {
            etUbicacion.setError("La ubicación no puede estar vacía");
            return false;
        }

        return true;
    }

    private void guardarLibroEnBD() {
        String nombre = etNombre.getText().toString().trim();
        String codigo = etCodigo.getText().toString().trim();
        String categoria = etCategoria.getText().toString().trim();
        String ubicacion = etUbicacion.getText().toString().trim();

        Libros libros = new Libros(null,nombre, codigo, categoria, ubicacion);

        Gson gson = new Gson();
        String json = gson.toJson(libros);
        Log.d("Libros", "Enviando datos del libro: " + json);

        // Realizar la solicitud POST utilizando Retrofit
        Call<Libros> call = apiService.createLibros(libros);

        // Encolar la llamada
        call.enqueue(new Callback<Libros>() {
            @Override
            public void onResponse(Call<Libros> call, Response<Libros> response) {
                if (response.isSuccessful()) {
                    Log.d("Libros", "Respuesta exitosa: " + response.body());
                    Toast.makeText(getApplicationContext(), "Libro creado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Libros", "Error en la respuesta: Código " + response.code() + " - " + response.message());
                    Toast.makeText(getApplicationContext(), "Error en la respuesta: " + response.message(), Toast.LENGTH_SHORT).show();
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Libros", "Cuerpo del error: " + errorBody);
                    } catch (Exception e) {
                        Log.e("Libros", "Error al leer el cuerpo del error: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Libros> call, Throwable t) {
                Log.e("Retrofit Error", "Error en la solicitud: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Error en la solicitud: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
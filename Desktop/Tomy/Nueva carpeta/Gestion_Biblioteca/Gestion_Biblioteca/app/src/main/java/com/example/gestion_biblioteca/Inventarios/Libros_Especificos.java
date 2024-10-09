package com.example.gestion_biblioteca.Inventarios;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestion_biblioteca.ApiService;
import com.example.gestion_biblioteca.R;
import com.example.gestion_biblioteca.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Libros_Especificos extends AppCompatActivity {

    private TextView tvNombreLE, tvCodigoLE, tvCategoriaLE, tvUbicacionLE;
    private EditText etNombreLE, etCodigoLE, etCategoriaLE, etUbicacionLE;
    private Button btnEditarLE, btnGuardarLE, btnEliminarLE;
    private Integer libroId; // ID del libro para operaciones en la base de datos
    private static final String TAG = "Libro_Especifico";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libros_especificos);

        // Inicializa los elementos de la vista
        tvNombreLE = findViewById(R.id.txtNombreLE);
        tvCodigoLE = findViewById(R.id.tvCodigoLE);
        tvCategoriaLE = findViewById(R.id.tvCategoriaLE);
        tvUbicacionLE = findViewById(R.id.tvUbicacionLE);

        etNombreLE = findViewById(R.id.etNombreLE);
        etCodigoLE = findViewById(R.id.etCodigoLE);
        etCategoriaLE = findViewById(R.id.etCategoriaLE);
        etUbicacionLE = findViewById(R.id.etUbicacionLE);

        btnEditarLE = findViewById(R.id.btnEditarLE);
        btnGuardarLE = findViewById(R.id.btnGuardarLE);
        btnEliminarLE = findViewById(R.id.btnEliminarLE);

        // Obtener el ID del libro desde el Intent
        Libros libro = (Libros) getIntent().getSerializableExtra("libro");
        libroId = getIntent().getIntExtra("libroId", -1);

        tvNombreLE.setText(libro.getNombre());
        tvCodigoLE.setText(libro.getCodigo());
        tvCategoriaLE.setText(libro.getCategoria());
        tvUbicacionLE.setText(libro.getUbicacion());

        // Configura el botón "Editar"
        btnEditarLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditing(true);
            }
        });

        // Configura el botón "Guardar"
        btnGuardarLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateFields()) {
                    GuardarLibro(libro, libroId);
                }
            }
        });

        // Configura el botón "Eliminar"
        btnEliminarLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarLibro(libroId);
            }
        });
    }

    // Habilitar la edición
    private void enableEditing(boolean isEditing) {
        tvNombreLE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etNombreLE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etNombreLE.setText(tvNombreLE.getText().toString());

        tvCodigoLE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etCodigoLE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etCodigoLE.setText(tvCodigoLE.getText().toString());

        tvCategoriaLE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etCategoriaLE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etCategoriaLE.setText(tvCategoriaLE.getText().toString());

        tvUbicacionLE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etUbicacionLE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etUbicacionLE.setText(tvUbicacionLE.getText().toString());

        btnEditarLE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        btnGuardarLE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
    }

    // Validar campos
    private boolean validateFields() {
        String nombre = etNombreLE.getText().toString().trim();
        String codigo = etCodigoLE.getText().toString().trim();
        String categoria = etCategoriaLE.getText().toString().trim();
        String ubicacion = etUbicacionLE.getText().toString().trim();

        if (TextUtils.isEmpty(nombre)) {
            etNombreLE.setError("Este campo no puede estar vacío");
            return false;
        }
        if (TextUtils.isEmpty(codigo)) {
            etCodigoLE.setError("Este campo no puede estar vacío");
            return false;
        }
        if (TextUtils.isEmpty(categoria)) {
            etCategoriaLE.setError("Este campo no puede estar vacío");
            return false;
        }
        if (TextUtils.isEmpty(ubicacion)) {
            etUbicacionLE.setError("Este campo no puede estar vacío");
            return false;
        }
        try {
            Integer.parseInt(codigo); // Validar que el código sea numérico
        } catch (NumberFormatException e) {
            etCodigoLE.setError("El código debe ser numérico");
            return false;
        }
        return true;
    }

    // AsyncTask para actualizar el libro
    private void GuardarLibro(Libros libro, Integer libroId) {
        if (libroId == null || libroId < 0) {
            Log.e(TAG, "ID de libro no válido.");
            return;
        }

        String nombre = etNombreLE.getText().toString();
        String codigo = etCodigoLE.getText().toString();
        String categoria = etCategoriaLE.getText().toString();
        String ubicacion = etUbicacionLE.getText().toString();

        Libros librosActualizados = new Libros();
        librosActualizados.setNombre(nombre);
        librosActualizados.setCodigo(codigo);
        librosActualizados.setCategoria(categoria);
        librosActualizados.setUbicacion(ubicacion);

        ApiService apiService = RetrofitClient.getClient("http://192.168.1.44/Puente/").create(ApiService.class);

        // Llamar al método updateLibros del ApiService
        Call<Libros> call = apiService.updateLibro(libroId, librosActualizados);

        call.enqueue(new Callback<Libros>() {
            @Override
            public void onResponse(Call<Libros> call, Response<Libros> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Libro actualizado exitosamente.");
                    finish(); // Regresar a la actividad anterior
                } else {
                    Log.e(TAG, "Error al actualizar el libro: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Libros> call, Throwable t) {
                Log.e(TAG, "Error en la solicitud de actualización: ", t);
                // Manejar el error
            }
        });
    }

    // AsyncTask para eliminar el libro
    private void EliminarLibro(Integer libroId) {
        if (libroId == -1) {
            Log.e(TAG, "ID de libro no válido.");
            return;
        }
        Log.d(TAG, "Eliminando libro con ID: " + libroId);
        ApiService apiService = RetrofitClient.getClient("http://192.168.1.44/Puente/").create(ApiService.class);
        Call<Void> call = apiService.deleteLibro(libroId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Libro eliminado exitosamente.");
                    finish(); // Regresar a la actividad anterior
                } else {
                    Log.e(TAG, "Error al eliminar el libro: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error en la solicitud de eliminación: ", t);
                // Manejar el error
            }
        });
    }
}

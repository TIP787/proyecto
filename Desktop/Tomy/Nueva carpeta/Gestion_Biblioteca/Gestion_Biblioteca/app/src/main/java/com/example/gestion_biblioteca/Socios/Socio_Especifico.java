package com.example.gestion_biblioteca.Socios;

import android.os.Bundle;
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

public class Socio_Especifico extends AppCompatActivity {
    private TextView tvNombre, tvDni, tvDireccion, tvTelefono, tvCorreo;
    private EditText etNombre, etDni, etDireccion, etTelefono, etCorreo;
    private Button btnEditar, btnGuardar, btnEliminar;
    private Integer idSocio; // ID del socio a mostrar
    private static final String TAG = "Socio_Especifico";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.socio_especifico);

        // Inicializar vistas
        tvNombre = findViewById(R.id.tvNombreSE);
        tvDni = findViewById(R.id.tvDniSE);
        tvDireccion = findViewById(R.id.tvDireccionSE);
        tvTelefono = findViewById(R.id.tvTelefonoSE);
        tvCorreo = findViewById(R.id.tvCorreoSE);

        etNombre = findViewById(R.id.etNombreSE);
        etDni = findViewById(R.id.etDniSE);
        etDireccion = findViewById(R.id.etDireccionSE);
        etTelefono = findViewById(R.id.etTelefonoSE);
        etCorreo = findViewById(R.id.etCorreoSE);

        btnEditar = findViewById(R.id.btnEditar);
        btnGuardar = findViewById(R.id.btnGuardarSE);
        btnEliminar = findViewById(R.id.btnEliminar);

        // Obtener ID del socio desde el Intent
        Socios socio = (Socios) getIntent().getSerializableExtra("socio");
        idSocio = getIntent().getIntExtra("socioId", -1);

        // Mostrar los datos en los TextView y EditText
        tvNombre.setText(socio.getNombre());
        tvDni.setText(socio.getDni());
        tvDireccion.setText(socio.getDireccion());
        tvTelefono.setText(socio.getTelefono());
        tvCorreo.setText(socio.getCorreo());

        // Configurar listeners para los botones
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditMode(true);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSocioData(socio, idSocio);
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteSocio(idSocio);
            }
        });
    }

    private void setEditMode(boolean isEditing) {
        Log.d(TAG, "Modo de edición: " + isEditing);
        // Muestra EditText y oculta TextView
        tvNombre.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etNombre.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etNombre.setText(tvNombre.getText());

        tvDni.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etDni.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etDni.setText(tvDni.getText());

        tvDireccion.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etDireccion.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etDireccion.setText(tvDireccion.getText());

        tvTelefono.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etTelefono.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etTelefono.setText(tvTelefono.getText());

        tvCorreo.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etCorreo.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etCorreo.setText(tvCorreo.getText());

        btnEditar.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        btnGuardar.setVisibility(isEditing ? View.VISIBLE : View.GONE);
    }


    private void saveSocioData(Socios socio, Integer idSocio){
        Log.d(TAG, "Guardando datos del socio...");

        // Verifica que el ID del socio sea válido
        if (idSocio == null || idSocio < 0) {
            Log.e(TAG, "ID de socio no válido.");
            return;
        }

        String nombre = etNombre.getText().toString();
        String dni = etDni.getText().toString();
        String direccion = etDireccion.getText().toString();
        String telefono = etTelefono.getText().toString();
        String correo = etCorreo.getText().toString();

        Socios socioActualizado = new Socios();
        // Obtener los datos desde los EditText
        socioActualizado.setNombre(nombre);
        socioActualizado.setDni(dni);
        socioActualizado.setDireccion(direccion);
        socioActualizado.setTelefono(telefono);
        socioActualizado.setCorreo(correo);



        // Crear un objeto Socios actualizado
        // Socios socioActualizado = new Socios(id, nombre, dni, telefono, direccion, correo, null);

        ApiService apiService = RetrofitClient.getClient("http://192.168.1.44/Puente/").create(ApiService.class);

        // Llamar al método updateSocio del ApiService
        Call<Socios> call = apiService.updateSocio(idSocio, socioActualizado);

        call.enqueue(new Callback<Socios>() {
            @Override
            public void onResponse(Call<Socios> call, Response<Socios> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Socio actualizado exitosamente.");
                    finish(); // Regresar a la actividad anterior
                } else {
                    Log.e(TAG, "Error al actualizar el socio: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Socios> call, Throwable t) {
                Log.e(TAG, "Error en la solicitud de actualización: ", t);
                // Manejar el error
            }
        });
    }


    private void deleteSocio(int socioId) {
        if (socioId == -1) {
            Log.e(TAG, "ID de socio no válido.");
            return;
        }

        Log.d(TAG, "Eliminando socio con ID: " + socioId);
        ApiService apiService = RetrofitClient.getClient("http://192.168.1.44/Puente/").create(ApiService.class);
        Call<Void> call = apiService.deleteSocio(socioId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Socio eliminado exitosamente.");
                    finish(); // Regresar a la actividad anterior
                } else {
                    Log.e(TAG, "Error al eliminar el socio: " + response.errorBody());
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
package com.example.gestion_biblioteca.Socios;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gestion_biblioteca.ApiService;
import com.example.gestion_biblioteca.Formulario_Principal;
import com.example.gestion_biblioteca.R;
import com.example.gestion_biblioteca.RetrofitClient;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FrmSocios extends AppCompatActivity {
    private EditText txtNombre, txtDni, txtDireccion, txtTelefono, txtCorreo, txtFecha;
    private Button btnSalir, btnVerSocios, btnGuardar;
    private ApiService apiService;

    private static final String TAG = "FrmSocios"; // Etiqueta para logs

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_socios);

        // Inicializar los EditText y Buttons
        txtNombre = findViewById(R.id.txtNombre);
        txtDni = findViewById(R.id.txtDni);
        txtDireccion = findViewById(R.id.txtDireccion);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtCorreo = findViewById(R.id.txtCorreo);
        txtFecha = findViewById(R.id.txtFecha);

        btnSalir = findViewById(R.id.btnSalir);
        btnVerSocios = findViewById(R.id.btnVerSocios);
        btnGuardar = findViewById(R.id.btnGuardar);

        // Configurar Retrofit
        Retrofit retrofit = RetrofitClient.getClient("http://192.168.1.44:8080/Puente/");
        apiService = retrofit.create(ApiService.class);

        Log.d(TAG, "Formulario de socios iniciado");

        // Acciones de los botones
        btnSalir.setOnClickListener(v -> {
            Log.d(TAG, "Botón 'Salir' presionado");
            Intent intent = new Intent(FrmSocios.this, Formulario_Principal.class);
            startActivity(intent);
        });

        btnVerSocios.setOnClickListener(v -> {
            Log.d(TAG, "Botón 'Ver Socios' presionado");
            Intent intent = new Intent(FrmSocios.this, Lista_Socios.class);
            startActivity(intent);
        });

        btnGuardar.setOnClickListener(v -> {
            Log.d(TAG, "Botón 'Guardar' presionado, validando campos...");
            if (validarCampos()) {
                Log.d(TAG, "Campos validados correctamente, procediendo a guardar socio en la BD");
                guardarSocioEnBD();
                limpiarCampos();
            } else {
                Log.d(TAG, "Error en la validación de los campos");
            }
        });
    }

    private void limpiarCampos() {
        Log.d(TAG, "Limpiando campos del formulario");
        txtNombre.setText("");
        txtDni.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtFecha.setText("");
    }

    private boolean validarCampos() {
        String nombre = txtNombre.getText().toString().trim();
        String dni = txtDni.getText().toString().trim();
        String direccion = txtDireccion.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String correo = txtCorreo.getText().toString().trim();
        String fecha = txtFecha.getText().toString().trim();

        Log.d(TAG, "Validando los campos ingresados: Nombre=" + nombre + ", DNI=" + dni +
                ", Dirección=" + direccion + ", Teléfono=" + telefono + ", Correo=" + correo + ", Fecha=" + fecha);

        if (nombre.length() < 3) {
            txtNombre.setError("El nombre debe tener al menos 3 caracteres");
            Log.e(TAG, "Error en el campo 'Nombre': debe tener al menos 3 caracteres");
            return false;
        }

        if (TextUtils.isEmpty(dni) || dni.length() != 8 || !dni.matches("\\d+")) {
            txtDni.setError("DNI inválido");
            Log.e(TAG, "Error en el campo 'DNI': valor inválido");
            return false;
        }

        if (TextUtils.isEmpty(direccion) || !Pattern.compile(".*\\d.*").matcher(direccion).find() || !Pattern.compile(".*[a-zA-Z].*").matcher(direccion).find()) {
            txtDireccion.setError("Dirección inválida");
            Log.e(TAG, "Error en el campo 'Dirección': formato inválido");
            return false;
        }

        if (TextUtils.isEmpty(telefono) || !telefono.matches("\\d+")) {
            txtTelefono.setError("Teléfono inválido");
            Log.e(TAG, "Error en el campo 'Teléfono': valor inválido");
            return false;
        }

        if (TextUtils.isEmpty(correo) || !android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            txtCorreo.setError("Correo inválido");
            Log.e(TAG, "Error en el campo 'Correo': formato de correo inválido");
            return false;
        }

        if (!validarFecha(fecha)) {
            txtFecha.setError("Fecha inválida");
            Log.e(TAG, "Error en el campo 'Fecha': formato inválido");
            return false;
        }

        Log.d(TAG, "Todos los campos validados correctamente");
        return true;
    }

    private boolean validarFecha(String fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(fecha);
            return true;
        } catch (ParseException e) {
            Log.e(TAG, "Formato de fecha inválido: " + fecha);
            return false;
        }
    }

    private void guardarSocioEnBD() {
        String nombre = txtNombre.getText().toString().trim();
        String dni = txtDni.getText().toString().trim();
        String direccion = txtDireccion.getText().toString().trim();
        String telefono = txtTelefono.getText().toString().trim();
        String correo = txtCorreo.getText().toString().trim();
        String fecha = txtFecha.getText().toString().trim();

        Socios socios = new Socios(null, nombre, dni, direccion, telefono, correo, fecha);

        Gson gson = new Gson();
        String json = gson.toJson(socios);
        Log.d(TAG, "Datos del socio en JSON: " + json);

        Call<Socios> call = apiService.createSocio(socios);

        call.enqueue(new Callback<Socios>() {
            @Override
            public void onResponse(Call<Socios> call, Response<Socios> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Socio creado exitosamente, respuesta del servidor: " + response.body());
                    Toast.makeText(getApplicationContext(), "Socio creado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "Error en la respuesta del servidor: Código " + response.code() + ", Mensaje: " + response.message());
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e(TAG, "Cuerpo del error del servidor: " + errorBody);
                    } catch (Exception e) {
                        Log.e(TAG, "Error al leer el cuerpo del error: " + e.getMessage());
                    }
                    Toast.makeText(getApplicationContext(), "Error en la respuesta: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Socios> call, Throwable t) {
                Log.e(TAG, "Error en la solicitud Retrofit: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Error en la solicitud: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

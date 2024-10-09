package com.example.gestion_biblioteca.Retiros;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestion_biblioteca.ApiService;
import com.example.gestion_biblioteca.Formulario_Principal;
import com.example.gestion_biblioteca.Inventarios.Lista_Inventarios;
import com.example.gestion_biblioteca.R;
import com.example.gestion_biblioteca.RetrofitClient;
import com.example.gestion_biblioteca.Socios.Lista_Socios;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FrmRetiros extends AppCompatActivity {

    private EditText txtFecha, txtNombreRT, txtLibro, tvTelefono, tvCodigo;
    private Button btnSocios, btnLibros, btnAtras, btnGuardar, btnRetiros;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_retiros);

        txtFecha = findViewById(R.id.txtFecha);
        txtNombreRT = findViewById(R.id.txtNombreRT);
        txtLibro = findViewById(R.id.txtLibro);
        tvTelefono = findViewById(R.id.tvTelefono);
        tvCodigo = findViewById(R.id.tvCodigo);
        btnSocios = findViewById(R.id.btnSocios);
        btnLibros = findViewById(R.id.btnLibros);
        btnAtras = findViewById(R.id.btnAtras);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnRetiros = findViewById(R.id.btnRetiros);

        btnSocios.setOnClickListener(v -> startActivity(new Intent(FrmRetiros.this, Lista_Socios.class)));
        btnLibros.setOnClickListener(v -> startActivity(new Intent(FrmRetiros.this, Lista_Inventarios.class)));
        btnAtras.setOnClickListener(v -> startActivity(new Intent(FrmRetiros.this, Formulario_Principal.class)));
        btnRetiros.setOnClickListener(v -> startActivity(new Intent(FrmRetiros.this, Libros_Retirados.class)));
        btnGuardar.setOnClickListener(view -> {if (validarCampos()) {guardarDatos();limpiarCampos();}});
        // Configurar Retrofit
        Retrofit retrofit = RetrofitClient.getClient("http://192.168.68.112:8080/Puente/");
        apiService = retrofit.create(ApiService.class);
    }


    private boolean validarCampos() {
         String fecha = txtFecha.getText().toString().trim();
         String nombre = txtNombreRT.getText().toString().trim();
         String libro = txtLibro.getText().toString().trim();
         String codigo = tvCodigo.getText().toString().trim();
         String telefono = tvTelefono.getText().toString().trim();

        if (fecha.isEmpty() || !esFechaValida(fecha)) {
            Toast.makeText(this, "Fecha inválida", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (nombre.isEmpty()) {
            Toast.makeText(this, "Nombre de socio vacío", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (libro.isEmpty()) {
            Toast.makeText(this, "Nombre de libro vacío", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (telefono.isEmpty()) {
            Toast.makeText(this, "Teléfono vacío", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (codigo.isEmpty()) {
            Toast.makeText(this, "Código vacío", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void guardarDatos() {
        String fecha = txtFecha.getText().toString().trim();
        String codigo = tvCodigo.getText().toString().trim();
        String libro = txtLibro.getText().toString().trim();
        String nombre = txtNombreRT.getText().toString().trim();
        String telefono = tvTelefono.getText().toString().trim();

        // Realizar las validaciones asíncronas
        verificarNombreSocio(fecha, codigo, libro, nombre, telefono);
        Retiros retiros = new Retiros(null,fecha,codigo,libro,nombre,telefono);

        Gson gson = new Gson();
        String json = gson.toJson(retiros);
        Log.d("Retiros", "Enviando datos del retiro: " + json);

        Call<Retiros> call = apiService.createRetiros(retiros);
        call.enqueue(new Callback<Retiros>() {
            @Override
            public void onResponse(Call<Retiros> call, Response<Retiros> response) {
                if (response.isSuccessful()) {
                    Log.d("Retiros", "Respuesta exitosa: " + response.body());
                    Toast.makeText(getApplicationContext(), "Retiro creado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Retiros", "Error en la respuesta: Código " + response.code() + " - " + response.message());
                    Toast.makeText(getApplicationContext(), "Error en la respuesta: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Retiros> call, Throwable t) {
                Log.e("Retrofit Error", "Error en la solicitud: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Error en la solicitud: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void verificarNombreSocio(String nombre, String libro, String telefono, String codigo, String fecha) {
        apiService.verificarNombreSocio(nombre).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonArray jsonArray = response.body();
                    boolean nombreValido = false;
                    for (JsonElement element : jsonArray) {
                        JsonObject jsonObject = element.getAsJsonObject();
                        String nombreSocio = jsonObject.get("Nombre").getAsString();
                        if (nombreSocio.equalsIgnoreCase(nombre)) {
                            nombreValido = true;
                            break;
                        }
                    }

                    if (nombreValido) {
                        verificarNombreLibro(libro, telefono, codigo, fecha);
                    } else {
                        Toast.makeText(FrmRetiros.this, "Nombre de socio no válido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Validación", "Error en la respuesta al verificar nombre de socio: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("Validación", "Error en la solicitud al verificar nombre de socio: " + t.getMessage());
            }
        });
    }

    private void verificarNombreLibro(String libro, String telefono, String codigo, String fecha){
        apiService.verificarNombreLibro(libro).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonArray jsonArray = response.body();
                    boolean libroExiste = false;
                    for (JsonElement element : jsonArray) {
                        JsonObject jsonObject = element.getAsJsonObject();
                        String nombreLibro = jsonObject.get("NOMBRE").getAsString();
                        if (nombreLibro.equalsIgnoreCase(libro)) {
                            libroExiste = true;
                            break;
                        }
                    }

                    if (libroExiste) {
                        verificarTelefono(telefono, codigo, fecha);
                    } else {
                        Toast.makeText(FrmRetiros.this, "El libro no existe en la base de datos.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Validación", "Error en la respuesta al verificar libro: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("Validación", "Error en la solicitud al verificar libro: " + t.getMessage());
            }
        });
    }

    private void verificarTelefono(String telefono, String codigo, String fecha) {
        apiService.verificarTelefono(telefono).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonArray jsonArray = response.body();
                    boolean codigoValido = false;

                    // Iterar sobre el JsonArray para verificar si el código existe
                    for (JsonElement element : jsonArray) {
                        JsonObject jsonObject = element.getAsJsonObject();
                        if (jsonObject.has("existe") && jsonObject.get("existe").getAsBoolean()) {
                            codigoValido = true;
                            break;
                        }
                    }

                    if (codigoValido) {
                        guardarDatos();
                    } else {
                        Toast.makeText(FrmRetiros.this, "Código no válido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Validación", "Error en la respuesta al verificar código: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("Validación", "Error en la solicitud al verificar código: " + t.getMessage());
            }
        });
    }

    private void verificarCodigo(String codigo, String fecha) {
        apiService.verificarCodigo(codigo).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonArray jsonResponse = response.body();
                    boolean codigoValido = false;

                    // Iterar sobre el JsonArray para verificar si el código existe
                    for (JsonElement element : jsonResponse) {
                        JsonObject jsonObject = element.getAsJsonObject();
                        if (jsonObject.has("existe") && jsonObject.get("existe").getAsBoolean()) {
                            codigoValido = true;
                            break;
                        }
                    }

                    if (codigoValido) {
                        guardarDatos();
                    } else {
                        Toast.makeText(FrmRetiros.this, "Código no válido", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Validación", "Error en la respuesta al verificar código: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.e("Validación", "Error en la solicitud al verificar código: " + t.getMessage());
            }
        });
    }


    private void limpiarCampos() {
        txtFecha.setText("");
        txtNombreRT.setText("");
        txtLibro.setText("");
        tvTelefono.setText("");
        tvCodigo.setText("");
    }

    private boolean esFechaValida(String fecha) {
        // Implementar lógica para validar el formato de la fecha
        return true;
    }
}
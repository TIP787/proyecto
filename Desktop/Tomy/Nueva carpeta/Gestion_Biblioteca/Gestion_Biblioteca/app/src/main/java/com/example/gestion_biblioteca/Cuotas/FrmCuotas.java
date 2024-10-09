package com.example.gestion_biblioteca.Cuotas;

import static android.content.ContentValues.TAG;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FrmCuotas extends AppCompatActivity {

    private EditText etUltimoPago, etNombrePC, etMontoPC;
    private Button btnAtrasPC, btnGuardarPC, btnRegistroPagosPC;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_cuotas);

        // Inicializa los elementos de la vista
        etUltimoPago = findViewById(R.id.etUltimoPago);
        etNombrePC = findViewById(R.id.etNombrePC);
        etMontoPC = findViewById(R.id.etMontoPC);
        btnAtrasPC = findViewById(R.id.btnAtrasPC);
        btnGuardarPC = findViewById(R.id.btnGuardarPC);
        btnRegistroPagosPC = findViewById(R.id.btnRegistroPagosPC);

        // Configurar Retrofit
        Retrofit retrofit = RetrofitClient.getClient("http://192.168.1.44:8080/Puente/");
        apiService = retrofit.create(ApiService.class);

        // Configura el botón "Atrás"
        btnAtrasPC.setOnClickListener(v -> {
            Intent intent = new Intent(FrmCuotas.this, Formulario_Principal.class);
            startActivity(intent);
        });

        // Configura el botón "Guardar"
        btnGuardarPC.setOnClickListener(v -> {
            if (validateFields()) {
                savePaymentData();
                clearFields();
            }
        });

        // Configura el botón "Registro de Pagos"
        btnRegistroPagosPC.setOnClickListener(v -> {
            Intent intent = new Intent(FrmCuotas.this, Registro_Pagos.class);
            startActivity(intent);
        });
    }

    // Método para validar los campos de texto
    private boolean validateFields() {
        String ultimoPago = etUltimoPago.getText().toString().trim();
        String nombre = etNombrePC.getText().toString().trim();
        String monto = etMontoPC.getText().toString().trim();

        if (TextUtils.isEmpty(ultimoPago)) {
            etUltimoPago.setError("Este campo no puede estar vacío");
            return false;
        } if (!validarFecha(ultimoPago)) {
            etUltimoPago.setError("Fecha invalida");
            return false;
        }

        if (TextUtils.isEmpty(nombre)) {
            etNombrePC.setError("Este campo no puede estar vacío");
            return false;
        }

        if (TextUtils.isEmpty(monto)) {
            etMontoPC.setError("Este campo no puede estar vacío");
            return false;
        }

        try {
            Double.parseDouble(monto); // Validar que el monto sea numérico
        } catch (NumberFormatException e) {
            etMontoPC.setError("Debe ser un número válido");
            return false;
        }

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


    // Método para guardar los datos en la base de datos a través de la API
    private void savePaymentData() {
        String nombre = etNombrePC.getText().toString().trim();
        String fecha = etUltimoPago.getText().toString().trim();
        String monto = etMontoPC.getText().toString().trim();

        Pagos pagos = new Pagos(null,nombre, fecha, monto);

        Gson gson = new Gson();
        String json = gson.toJson(pagos);
        Log.d("Cuotas", "Enviando datos de la cuota: " + json);

        // Realizar la solicitud POST utilizando Retrofit
        Call<Pagos> call = apiService.createCuotas(pagos);

        // Encolar la llamada
        call.enqueue(new Callback<Pagos>() {
            @Override
            public void onResponse(Call<Pagos> call, Response<Pagos> response) {
                if (response.isSuccessful()) {
                    Log.d("Cuotas", "Respuesta exitosa: " + response.body());
                    Toast.makeText(getApplicationContext(), "Pago registrado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Cuotas", "Error en la respuesta: Código " + response.code() + " - " + response.message());
                    Toast.makeText(getApplicationContext(), "Error en la respuesta: " + response.message(), Toast.LENGTH_SHORT).show();

                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Cuotas", "Cuerpo del error: " + errorBody);
                    } catch (Exception e) {
                        Log.e("Cuotas", "Error al leer el cuerpo del error: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Pagos> call, Throwable t) {
                Log.e("Retrofit Error", "Error en la solicitud: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Error en la solicitud: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        etUltimoPago.setText("");
        etNombrePC.setText("");
        etMontoPC.setText("");
    }
}
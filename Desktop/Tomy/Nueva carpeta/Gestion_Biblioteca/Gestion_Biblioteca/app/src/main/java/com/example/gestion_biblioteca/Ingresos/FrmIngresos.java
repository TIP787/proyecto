package com.example.gestion_biblioteca.Ingresos;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

public class FrmIngresos extends AppCompatActivity {

    private EditText txtFechaIG, txtMontoIG, txtObservacionesIG;
    private Spinner spinnerOpcionesIG;
    private Button btnSalirIG, btnVerIngresos, btnGuardarIG;
    private String selectedOption;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_ingresos);

        // Inicializa los elementos de la vista
        txtFechaIG = findViewById(R.id.txtFechaIG);
        txtMontoIG = findViewById(R.id.txtMontoIG);
        txtObservacionesIG = findViewById(R.id.txtObservacionesIG);
        spinnerOpcionesIG = findViewById(R.id.spinnerOpcionesIG);
        btnSalirIG = findViewById(R.id.btnSalirIG);
        btnVerIngresos = findViewById(R.id.btnVerIngresos);
        btnGuardarIG = findViewById(R.id.btnGuardarIG);

        // Configurar Retrofit
        Retrofit retrofit = RetrofitClient.getClient("http://192.168.1.44:8080/Puente/");
        apiService = retrofit.create(ApiService.class);

        // Configura el Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.opciones_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOpcionesIG.setAdapter(adapter);

        spinnerOpcionesIG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOption = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedOption = null;
            }
        });

        // Configura los botones
        btnSalirIG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrmIngresos.this, Formulario_Principal.class);
                startActivity(intent);
            }
        });

        btnVerIngresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrmIngresos.this, Lista_Ingresos.class);
                startActivity(intent);
            }
        });

        btnGuardarIG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    guardarIngresoEnBD();
                    limpiarCampos();
                }
            }
        });
    }

    private boolean validarCampos() {
        boolean valido = true;

        // Validar fecha
        String fecha = txtFechaIG.getText().toString();
        if (TextUtils.isEmpty(txtFechaIG.getText())) {
            txtFechaIG.setError("La fecha no puede estar vacía");
            valido = false;
        } if (!validarFecha(fecha)) {
            txtFechaIG.setError("Fecha invalida");
            return false;
        }

        // Validar monto
        String monto = txtMontoIG.getText().toString();
        if (TextUtils.isEmpty(monto)) {
            txtMontoIG.setError("El monto no puede estar vacío");
            valido = false;
        } else {
            try {
                Double.parseDouble(monto);
            } catch (NumberFormatException e) {
                txtMontoIG.setError("El monto debe ser un número válido");
                valido = false;
            }
        }

        // Validar Spinner
        if (selectedOption == null) {
            Toast.makeText(this, "Debe seleccionar una opción", Toast.LENGTH_SHORT).show();
            valido = false;
        }

        return valido;
    }

    private void limpiarCampos() {
        txtFechaIG.setText("");
        txtMontoIG.setText("");
        txtObservacionesIG.setText("");
        spinnerOpcionesIG.setSelection(0);
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

    private void guardarIngresoEnBD() {
        String fecha = txtFechaIG.getText().toString().trim();
        String monto = txtMontoIG.getText().toString().trim();
        String observaciones = txtObservacionesIG.getText().toString().trim();
        String motivo = selectedOption;

        Ingresos ingresos = new Ingresos( null, motivo, monto, observaciones, fecha);

        Gson gson = new Gson();
        String json = gson.toJson(ingresos);
        Log.d("Ingresos", "Enviando datos del ingreso: " + json);

        // Realizar la solicitud POST utilizando Retrofit
        Call<Ingresos> call = apiService.createIngreso(ingresos);

        // Encolar la llamada
        call.enqueue(new Callback<Ingresos>() {
            @Override
            public void onResponse(Call<Ingresos> call, Response<Ingresos> response) {
                if (response.isSuccessful()) {
                    Log.d("Ingresos", "Respuesta exitosa: " + response.body());
                    Toast.makeText(getApplicationContext(), "Ingreso guardado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Ingresos", "Error en la respuesta: Código " + response.code() + " - " + response.message());
                    Toast.makeText(getApplicationContext(), "Error en la respuesta: " + response.message(), Toast.LENGTH_SHORT).show();

                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("Ingresos", "Cuerpo del error: " + errorBody);
                    } catch (Exception e) {
                        Log.e("Ingresos", "Error al leer el cuerpo del error: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Ingresos> call, Throwable t) {
                Log.e("Retrofit Error", "Error en la solicitud: " + t.getMessage());
                Toast.makeText(getApplicationContext(), "Error en la solicitud: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

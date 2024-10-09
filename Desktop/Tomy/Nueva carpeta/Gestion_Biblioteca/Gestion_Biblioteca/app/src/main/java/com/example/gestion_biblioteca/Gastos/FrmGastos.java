package com.example.gestion_biblioteca.Gastos;

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

public class FrmGastos extends AppCompatActivity {

    private EditText txtFechaG, txtMontoG, txtObservacionesG;
    private Spinner spinnerOpciones;
    private Button btnSalirG, btnVerGastosG, btnGuardarG;
    private String selectedOption;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_gastos);

        // Inicializa los elementos de la vista
        txtFechaG = findViewById(R.id.txtFechaG);
        txtMontoG = findViewById(R.id.txtMontoG);
        txtObservacionesG = findViewById(R.id.txtObservacionesG);
        spinnerOpciones = findViewById(R.id.spinnerOpciones);
        btnSalirG = findViewById(R.id.btnSalirG);
        btnVerGastosG = findViewById(R.id.btnVerSociosG);
        btnGuardarG = findViewById(R.id.btnGuardarG);

        // Configurar Retrofit
        Retrofit retrofit = RetrofitClient.getClient("http://192.168.1.44:8080/Puente/");
        apiService = retrofit.create(ApiService.class);

        // Configura el Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.opciones_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOpciones.setAdapter(adapter);

        spinnerOpciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        btnSalirG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrmGastos.this, Formulario_Principal.class);
                startActivity(intent);
            }
        });

        btnVerGastosG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FrmGastos.this, Lista_Gastos.class);
                startActivity(intent);
            }
        });

        btnGuardarG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()) {
                    guardarGastoEnBD();
                    limpiarCampos();
                }
            }
        });
    }

    private void limpiarCampos() {
        txtFechaG.setText("");
        txtMontoG.setText("");
        txtObservacionesG.setText("");
        spinnerOpciones.setSelection(0);
    }

    private boolean validarCampos() {
        boolean valido = true;

        // Validar fecha
        String fecha = txtFechaG.getText().toString();
        if (TextUtils.isEmpty(txtFechaG.getText())) {
            txtFechaG.setError("La fecha no puede estar vacía");
            valido = false;
        } if (!validarFecha(fecha)) {
            txtFechaG.setError("Fecha invalida");
            return false;
        }

        // Validar monto
        String monto = txtMontoG.getText().toString();
        if (TextUtils.isEmpty(monto)) {
            txtMontoG.setError("El monto no puede estar vacío");
            valido = false;
        } else {
            try {
                Double.parseDouble(monto);
            } catch (NumberFormatException e) {
                txtMontoG.setError("El monto debe ser un número válido");
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

    private void guardarGastoEnBD() {
        String fecha = txtFechaG.getText().toString().trim();
        String monto = txtMontoG.getText().toString().trim();
        String observaciones = txtObservacionesG.getText().toString().trim();
        String motivo = selectedOption;

        Gastos gasto = new Gastos(null, fecha, motivo, monto, observaciones);

        Gson gson = new Gson();
        String json = gson.toJson(gasto);
        Toast.makeText(this, "Enviando datos del gasto: " + json, Toast.LENGTH_SHORT).show();

        // Realizar la solicitud POST utilizando Retrofit
        Call<Gastos> call = apiService.createGastos(gasto);

        // Encolar la llamada
        call.enqueue(new Callback<Gastos>() {
            @Override
            public void onResponse(Call<Gastos> call, Response<Gastos> response) {
                if (response.isSuccessful()) {
                    // Manejo de respuesta exitosa
                    Toast.makeText(getApplicationContext(), "Gasto creado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    // Manejo de errores específicos del servidor
                    Toast.makeText(getApplicationContext(), "Error en la respuesta: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Gastos> call, Throwable t) {
                // Captura de errores de la solicitud en general (ej. problemas de red)
                Toast.makeText(getApplicationContext(), "Error en la solicitud: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

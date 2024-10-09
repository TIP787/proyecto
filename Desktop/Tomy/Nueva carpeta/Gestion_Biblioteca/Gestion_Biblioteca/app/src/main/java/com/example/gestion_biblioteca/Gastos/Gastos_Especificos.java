package com.example.gestion_biblioteca.Gastos;

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

public class Gastos_Especificos extends AppCompatActivity {

    private TextView tvFechaGE, tvMotivosGE, tvMontoGE, tvObservacionesGE;
    private EditText etFechaGE, etMontoGE, etObservacionesGE, spinnerOpciones2;
    private Button btnEditarGE, btnGuardarGE, btnEliminarGE;
    private Integer gastoId;
    private static final String TAG = "Gasto_Especifico";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gastos_especificos);

        tvFechaGE = findViewById(R.id.tvFechaGE);
        tvMotivosGE = findViewById(R.id.tvMotivosGE);
        tvMontoGE = findViewById(R.id.tvMontoGE);
        tvObservacionesGE = findViewById(R.id.tvObservacionesGE);

        etFechaGE = findViewById(R.id.etFechaGE);
        etMontoGE = findViewById(R.id.etMontoGE);
        etObservacionesGE = findViewById(R.id.etObservacionesGE);
        spinnerOpciones2 = findViewById(R.id.spinnerOpciones2);

        btnEditarGE = findViewById(R.id.btnEditarGE);
        btnGuardarGE = findViewById(R.id.btnGuardarGE);
        btnEliminarGE = findViewById(R.id.btnEliminarGE);

        // Obtener el ID del gasto desde el Intent
        Gastos gasto = (Gastos) getIntent().getSerializableExtra("gasto");
        gastoId = getIntent().getIntExtra("gastoId", -1);


        tvFechaGE.setText(gasto.getFecha());
        tvMotivosGE.setText(gasto.getMotivo());
        tvMontoGE.setText(gasto.getMonto());
        tvObservacionesGE.setText(gasto.getObservaciones());


        btnEditarGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEditMode(true);
            }
        });

        btnGuardarGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarData(gasto, gastoId);
            }
        });

        btnEliminarGE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarData(gastoId);
            }
        });
    }

    private void eliminarData(Integer gastoId) {
        if (gastoId == -1) {
            Log.e(TAG, "ID de gasto no válido.");
            return;
        }

        Log.d(TAG, "Eliminando gasto con ID: " + gastoId);
        ApiService apiService = RetrofitClient.getClient("http://192.168.1.44/Puente/").create(ApiService.class);
        Call<Void> call = apiService.deleteGasto(gastoId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "gasto eliminado exitosamente.");
                    finish(); // Regresar a la actividad anterior
                } else {
                    Log.e(TAG, "Error al eliminar el gasto: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error en la solicitud de eliminación: ", t);
                // Manejar el error
            }
        });
    }

    private void guardarData(Gastos gasto, Integer gastoId) {
        if (gastoId == null || gastoId < 0) {
            return;
        }

        String fecha = etFechaGE.getText().toString();
        String motivo = spinnerOpciones2.getText().toString();
        String monto = etMontoGE.getText().toString();
        String observaciones = etObservacionesGE.getText().toString();

        Gastos gastoActualizado = new Gastos();
        gastoActualizado.setFecha(fecha);
        gastoActualizado.setMotivo(motivo);
        gastoActualizado.setMonto(monto);
        gastoActualizado.setObservaciones(observaciones);

        ApiService apiService = RetrofitClient.getClient("http://192.168.1.44/Puente/").create(ApiService.class);

        // Llamar al método updateSocio del ApiService
        Call<Gastos> call = apiService.updateGasto(gastoId, gastoActualizado);

        call.enqueue(new Callback<Gastos>() {
            @Override
            public void onResponse(Call<Gastos> call, Response<Gastos> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Socio actualizado exitosamente.");
                    finish(); // Regresar a la actividad anterior
                } else {
                    Log.e(TAG, "Error al actualizar el socio: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Gastos> call, Throwable t) {
                Log.e(TAG, "Error en la solicitud de actualización: ", t);
                // Manejar el error
            }
        });

    }

    private void setEditMode(boolean isEditing) {
        tvFechaGE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etFechaGE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etFechaGE.setText(tvFechaGE.getText());

        tvMotivosGE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        spinnerOpciones2.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        spinnerOpciones2.setText(tvMotivosGE.getText());

        tvMontoGE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etMontoGE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etMontoGE.setText(tvMontoGE.getText());

        tvObservacionesGE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etObservacionesGE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etObservacionesGE.setText(tvObservacionesGE.getText());

        btnEditarGE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        btnGuardarGE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
    }


        }

package com.example.gestion_biblioteca.Ingresos;

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

public class Ingresos_Especificos extends AppCompatActivity {
    private TextView tvFechaIE,tvMotivosIE,tvMontoIE,tvObservacionesIE;
    private EditText etFechaIE,spinnerOpciones3,etMontoIE,etObservacionesIE;
    private Button btnEditarIE, btnGuardarIE, btnEliminarIE;
    private Integer ingresoId;
    private static final String TAG = "Ingresos_Especifico";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingresos_especificos);

        tvFechaIE = findViewById(R.id.tvFechaIE);
        tvMotivosIE = findViewById(R.id.tvMotivosIE);
        tvMontoIE = findViewById(R.id.tvMontoIE);
        tvObservacionesIE = findViewById(R.id.tvObservacionesIE);

        etFechaIE = findViewById(R.id.etFechaIE);
        spinnerOpciones3 = findViewById(R.id.spinnerOpciones3);
        etMontoIE = findViewById(R.id.etMontoIE);
        etObservacionesIE = findViewById(R.id.etObservacionesIE);

        btnEditarIE = findViewById(R.id.btnEditarIE);
        btnGuardarIE = findViewById(R.id.btnGuardarIE);
        btnEliminarIE = findViewById(R.id.btnEliminarIE);

        Ingresos ingreso = (Ingresos) getIntent().getSerializableExtra("ingreso");
        ingresoId = getIntent().getIntExtra("ingresoId", -1);

        // Agregar log para verificar el valor del ingresoId
        Log.d(TAG, "ID de Ingreso recibido en onCreate: " + ingresoId);

        if (ingreso != null) {
            tvFechaIE.setText(ingreso.getFecha());
            tvMotivosIE.setText(ingreso.getMotivo());
            tvMontoIE.setText(ingreso.getMonto());
            tvObservacionesIE.setText(ingreso.getObservaciones());
        } else {
            Log.e(TAG, "El objeto Ingresos es null");
        }

        btnEditarIE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditmode(true);
            }
        });

        btnGuardarIE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuardarIngresos(ingreso, ingresoId);
            }
        });

        btnEliminarIE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BorrarIngresos(ingresoId);
            }
        });
    }

    private void BorrarIngresos(Integer ingresoId) {
        if (ingresoId == -1) {
            Log.e(TAG, "ID de Ingresos no válido.");
            return;
        }

        // Log antes de hacer la solicitud de eliminación
        Log.d(TAG, "Eliminando Ingresos con ID: " + ingresoId);
        ApiService apiService = RetrofitClient.getClient("http://192.168.1.44/Puente/").create(ApiService.class);
        Call<Void> call = apiService.deleteIngreso(ingresoId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Ingresos eliminado exitosamente.");
                    finish(); // Regresar a la actividad anterior
                } else {
                    Log.e(TAG, "Error al eliminar el Ingresos: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error en la solicitud de eliminación: ", t);
            }
        });
    }

    private void GuardarIngresos(Ingresos ingreso, Integer ingresoId) {
        if (ingresoId == null || ingresoId < 0) {
            Log.e(TAG, "ID de Ingresos no válido en GuardarIngresos.");
            return;
        }

        // Log para verificar el ID antes de guardar
        Log.d(TAG, "Guardando Ingresos con ID: " + ingresoId);

        String fecha = etFechaIE.getText().toString();
        String motivo = spinnerOpciones3.getText().toString();
        String monto = etMontoIE.getText().toString();
        String observaciones = etObservacionesIE.getText().toString();

        Ingresos ingresosActualizado = new Ingresos();
        ingresosActualizado.setFecha(fecha);
        ingresosActualizado.setMotivo(motivo);
        ingresosActualizado.setMonto(monto);
        ingresosActualizado.setObservaciones(observaciones);

        ApiService apiService = RetrofitClient.getClient("http://192.168.1.44/Puente/").create(ApiService.class);

        Call<Ingresos> call = apiService.updateIngreso(ingresoId, ingresosActualizado);

        call.enqueue(new Callback<Ingresos>() {
            @Override
            public void onResponse(Call<Ingresos> call, Response<Ingresos> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Ingresos actualizado exitosamente.");
                    finish(); // Regresar a la actividad anterior
                } else {
                    Log.e(TAG, "Error al actualizar el ingreso: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Ingresos> call, Throwable t) {
                Log.e(TAG, "Error en la solicitud de actualización: ", t);
            }
        });
    }

    private void setEditmode(boolean isEditing) {

        tvFechaIE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etFechaIE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etFechaIE.setText(tvFechaIE.getText());

        tvMotivosIE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        spinnerOpciones3.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        spinnerOpciones3.setText(tvMotivosIE.getText());

        tvMontoIE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etMontoIE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etMontoIE.setText(tvMontoIE.getText());

        tvObservacionesIE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etObservacionesIE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etObservacionesIE.setText(tvObservacionesIE.getText());

        btnEditarIE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        btnGuardarIE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
    }
}
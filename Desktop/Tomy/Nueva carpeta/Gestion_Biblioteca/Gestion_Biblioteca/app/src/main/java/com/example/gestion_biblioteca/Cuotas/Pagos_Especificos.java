package com.example.gestion_biblioteca.Cuotas;

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

public class Pagos_Especificos extends AppCompatActivity {

    private TextView tvFechaPE, tvMontoPE, tvSocioPE;
    private EditText etFechaPE, etMontoPE, etSocioPE;
    private Button btnEditarPE, btnGuardarPE, btnEliminarPE;
    private Integer pagoId;
    private static final String TAG = "Pago_Especifico";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pagos_especificos);

        // Inicializar vistas
        tvFechaPE = findViewById(R.id.tvFechaPE);
        tvMontoPE = findViewById(R.id.tvMontoPE);
        tvSocioPE = findViewById(R.id.tvSocioPE);
        etFechaPE = findViewById(R.id.etFechaPE);
        etMontoPE = findViewById(R.id.etMontoPE);
        etSocioPE = findViewById(R.id.etSocioPE);
        btnEditarPE = findViewById(R.id.btnEditarPE);
        btnGuardarPE = findViewById(R.id.btnGuardarPE);
        btnEliminarPE = findViewById(R.id.btnEliminarPE);

        Pagos pago = (Pagos) getIntent().getSerializableExtra("pago");
        pagoId = getIntent().getIntExtra("pagoId", -1);

        // Mostrar datos en TextViews
        tvFechaPE.setText(pago.getFecha());
        tvMontoPE.setText(pago.getMonto());
        tvSocioPE.setText(pago.getNombre());

        // Botón Editar
        btnEditarPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEditMode(true);
            }
        });
        btnGuardarPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            GuardarPagos(pago, pagoId);
            }
        });

        btnEliminarPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EliminarPagos(pagoId);
            }
        });

    }

    private void EliminarPagos(Integer pagoId) {
        if (pagoId == -1) {
            Log.e(TAG, "ID de socio no válido.");
            return;
        }

        Log.d(TAG, "Eliminando socio con ID: " + pagoId);
        ApiService apiService = RetrofitClient.getClient("http://192.168.1.44/Puente/").create(ApiService.class);
        Call<Void> call = apiService.deleteCuotas(pagoId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Pagos eliminado exitosamente.");
                    finish(); // Regresar a la actividad anterior
                } else {
                    Log.e(TAG, "Error al eliminar el Pagos: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Error en la solicitud de eliminación: ", t);
                // Manejar el error
            }
        });
    }

    private void GuardarPagos(Pagos pago, Integer pagoId) {
        if (pagoId == null || pagoId < 0) {
            return;
        }
        String fecha = etFechaPE.getText().toString();
        String monto = etMontoPE.getText().toString();
        String socio = etSocioPE.getText().toString();

        Pagos pagoActualizado = new Pagos();
        pagoActualizado.setFecha(fecha);
        pagoActualizado.setMonto(monto);
        pagoActualizado.setNombre(socio);

        ApiService apiService = RetrofitClient.getClient("http://192.168.1.44/Puente/").create(ApiService.class);

        // Llamar al método updateSocio del ApiService
        Call<Pagos> call = apiService.updateCuotas(pagoId, pagoActualizado);

        call.enqueue(new Callback<Pagos>() {
            @Override
            public void onResponse(Call<Pagos> call, Response<Pagos> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Pagos actualizado exitosamente.");
                    finish(); // Regresar a la actividad anterior
                } else {
                    Log.e(TAG, "Error al actualizar el Pagos: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Pagos> call, Throwable t) {
                Log.e(TAG, "Error en la solicitud de actualización: ", t);
                // Manejar el error
            }
        });

    }

    private void setEditMode(boolean isEditing) {


        tvFechaPE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etFechaPE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etFechaPE.setText(tvFechaPE.getText());

        tvMontoPE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etMontoPE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etMontoPE.setText(tvMontoPE.getText());

        tvSocioPE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etSocioPE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etSocioPE.setText(tvSocioPE.getText());

        btnEditarPE.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        btnGuardarPE.setVisibility(isEditing ? View.VISIBLE : View.GONE);
    }
    }
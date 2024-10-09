package com.example.gestion_biblioteca.Retiros;

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

public class Retiro_Especifico extends AppCompatActivity {

    private TextView tvFechaRL, tvCodigoRL, tvLibroRL, tvSocioRL, tvTelefonoRL;
    private EditText etFechaRL, etCodigoRL, etLibroRL, etSocioRL, etTelefonoRL;
    private Button btnEditarRL, btnGuardarRL, btnEliminarRL;
    private Integer idRetiro;
    private static final String TAG = "Retiro_Especifico";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retiro_especifico);

        // Inicializa las vistas
        tvFechaRL = findViewById(R.id.tvFechaRL);
        tvCodigoRL = findViewById(R.id.tvCodigoRL);
        tvLibroRL = findViewById(R.id.tvLibroRL);
        tvSocioRL = findViewById(R.id.tvSocioRL);
        tvTelefonoRL = findViewById(R.id.tvTelefonoRL);

        etFechaRL = findViewById(R.id.etFechaRL);
        etCodigoRL = findViewById(R.id.etCodigoRL);
        etLibroRL = findViewById(R.id.etLibroRL);
        etSocioRL = findViewById(R.id.etSocioRL);
        etTelefonoRL = findViewById(R.id.etTelefonoRL);

        btnEditarRL = findViewById(R.id.btnEditarRL);
        btnGuardarRL = findViewById(R.id.btnGuardarRL);
        btnEliminarRL = findViewById(R.id.btnEliminarRL);

        // Obtiene los datos pasados desde la actividad anterior
        Retiros retiro = (Retiros) getIntent().getSerializableExtra("retiro");
        idRetiro = getIntent().getIntExtra("retiroId", -1);

        // Muestra los datos en los TextViews
            tvFechaRL.setText(retiro.getFecha());
            tvCodigoRL.setText(retiro.getCodigo());
            tvLibroRL.setText(retiro.getLibro());
            tvSocioRL.setText(retiro.getNombre());
            tvTelefonoRL.setText(retiro.getTelefono());


        // Configura los botones
        btnEditarRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarRegistro(true);
            }
        });

        btnGuardarRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarRegistro(retiro, idRetiro);
            }
        });

        btnEliminarRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarRegistro(idRetiro);
            }
        });
    }

    private void editarRegistro(boolean isEditing) {
        // Oculta los TextViews y muestra los EditTexts
        tvFechaRL.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etFechaRL.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etFechaRL.setText(tvFechaRL.getText().toString());

        tvCodigoRL.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etCodigoRL.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etCodigoRL.setText(tvCodigoRL.getText().toString());

        tvLibroRL.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etLibroRL.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etLibroRL.setText(tvLibroRL.getText().toString());

        tvSocioRL.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etSocioRL.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etSocioRL.setText(tvSocioRL.getText().toString());

        tvTelefonoRL.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        etTelefonoRL.setVisibility(isEditing ? View.VISIBLE : View.GONE);
        etTelefonoRL.setText(tvTelefonoRL.getText().toString());

        btnEditarRL.setVisibility(isEditing ? View.GONE : View.VISIBLE);
        btnGuardarRL.setVisibility(isEditing ? View.VISIBLE : View.GONE);
    }

    private void guardarRegistro(Retiros retiro, Integer idRetiro) {
        // Obtener los valores de los EditTexts
        String fecha = etFechaRL.getText().toString();
        String codigo = etCodigoRL.getText().toString();
        String libro = etLibroRL.getText().toString();
        String socio = etSocioRL.getText().toString();
        String telefono = etTelefonoRL.getText().toString();

        // Actualiza los datos del objeto retiro
        Retiros retiroActualizado = new Retiros();
        retiroActualizado.setFecha(fecha);
        retiroActualizado.setCodigo(codigo);
        retiroActualizado.setLibro(libro);
        retiroActualizado.setNombre(socio);
        retiroActualizado.setTelefono(telefono);

        ApiService apiService = RetrofitClient.getClient("http://192.168.1.44/Puente/").create(ApiService.class);

        Call<Retiros> call = apiService.updateRetiros(idRetiro, retiroActualizado);

        call.enqueue(new Callback<Retiros>() {
            @Override
            public void onResponse(Call<Retiros> call, Response<Retiros> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Socio actualizado exitosamente.");
                    finish(); // Regresar a la actividad anterior
                } else {
                    Log.e(TAG, "Error al actualizar el Retiros: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Retiros> call, Throwable t) {
                Log.e(TAG, "Error en la solicitud de actualización: ", t);
                // Manejar el error
            }
        });
    }



    private void eliminarRegistro(int retiroId) {
        if (retiroId == -1) {
            Log.e(TAG, "ID de Retiros no válido.");
            return;
        }

        Log.d(TAG, "Eliminando Retiros con ID: " + retiroId);
        ApiService apiService = RetrofitClient.getClient("http://192.168.1.44/Puente/").create(ApiService.class);
        Call<Void> call = apiService.deleteRetiros(retiroId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Socio eliminado exitosamente.");
                    finish(); // Regresar a la actividad anterior
                } else {
                    Log.e(TAG, "Error al eliminar el Retiros: " + response.errorBody());
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

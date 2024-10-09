package com.example.gestion_biblioteca.Gastos;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestion_biblioteca.ApiService;
import com.example.gestion_biblioteca.R;
import com.example.gestion_biblioteca.RetrofitClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Lista_Gastos extends AppCompatActivity {

    private EditText fechaInicioLG, fechaFinLG;
    private Button btnFiltrarLG;
    private TextView montoTotalLG;
    private RecyclerView recyclerViewLG;
    private GastosAdapter gastosAdapter;
    private List<Gastos> listaGastos = new ArrayList<>();
    private List<Gastos> filteredGastosList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_gastos);

        Retrofit retrofit = RetrofitClient.getClient("http://192.168.1.44:8080/Puente/");
        apiService = retrofit.create(ApiService.class);

        // Inicializar vistas
        fechaInicioLG = findViewById(R.id.fechaInicioLG);
        fechaFinLG = findViewById(R.id.fechaFinLG);
        btnFiltrarLG = findViewById(R.id.btnFiltrarLG);
        montoTotalLG = findViewById(R.id.montoTotalLG);
        recyclerViewLG = findViewById(R.id.recyclerViewLG);

        // Configurar RecyclerView
        recyclerViewLG.setLayoutManager(new LinearLayoutManager(this));

        gastosAdapter = new GastosAdapter(filteredGastosList, gasto -> {
            Intent intent = new Intent(Lista_Gastos.this, Gastos_Especificos.class);
            intent.putExtra("gasto", gasto);
            intent.putExtra("gastoId", gasto.getId());
            startActivity(intent);
        });
        recyclerViewLG.setAdapter(gastosAdapter);

        // Cargar todos los gastos inicialmente
        CargarGastos();

        // Configurar el botón de filtrar
        btnFiltrarLG.setOnClickListener(v -> {
            String fechaInicio = fechaInicioLG.getText().toString();
            String fechaFin = fechaFinLG.getText().toString();

            if (TextUtils.isEmpty(fechaInicio)) {
                fechaInicioLG.setError("La fecha de inicio no puede estar vacía");
                return;
            }
            if (TextUtils.isEmpty(fechaFin)) {
                fechaFinLG.setError("La fecha de fin no puede estar vacía");
                return;
            }

                filtrarGastosPorFecha(fechaInicio, fechaFin);
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        CargarGastos(); // Volver a cargar la lista de gastos al regresar a esta actividad
    }

    private void CargarGastos() {
        Call<List<Gastos>> call = apiService.getGastos();
        call.enqueue(new Callback<List<Gastos>>() {
            @Override
            public void onResponse(Call<List<Gastos>> call, Response<List<Gastos>> response) {
                if (response.isSuccessful()) {
                    List<Gastos> gastos = response.body();
                    if (gastos != null) {
                        listaGastos.clear();
                        listaGastos.addAll(gastos);
                        filteredGastosList.clear();
                        filteredGastosList.addAll(gastos);
                        gastosAdapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                        actualizarMontoTotal(); // Actualizar monto total
                    }
                } else {
                    Log.e("Lista_Gastos", "Error en la respuesta de la API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Gastos>> call, Throwable t) {
                Log.e("Lista_Gastos", "Error en la llamada a la API", t);
            }
        });
    }

    private void filtrarGastosPorFecha(String fechaInicioStr, String fechaFinStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        filteredGastosList.clear();

        for (Gastos gasto : listaGastos) {
            try {
                Date fechaGasto = dateFormat.parse(gasto.getFecha());
                Date fechaInicio = dateFormat.parse(fechaInicioStr);
                Date fechaFin = dateFormat.parse(fechaFinStr);

                if ((fechaGasto.equals(fechaInicio) || fechaGasto.after(fechaInicio)) &&
                        (fechaGasto.equals(fechaFin) || fechaGasto.before(fechaFin))) {
                    filteredGastosList.add(gasto);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        gastosAdapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
        actualizarMontoTotal(); // Actualizar monto total
    }

    private void actualizarMontoTotal() {
        double montoTotal = 0;

        for (Gastos gasto : filteredGastosList) {
            montoTotal += Double.parseDouble(gasto.getMonto());
        }

        montoTotalLG.setText(String.format("Monto Total: %.2f", montoTotal));
    }
}

package com.example.gestion_biblioteca.Ingresos;

import android.content.Intent;
import android.os.Bundle;
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

public class Lista_Ingresos extends AppCompatActivity {

    private EditText fechaInicioLIG, fechaFinLIG;
    private Button btnFiltrarLIG;
    private TextView montoTotalLIG;
    private RecyclerView recyclerViewLIG;
    private IngresosAdapter IngresosAdapter;
    private List<Ingresos> listaIngresos = new ArrayList<>();
    private List<Ingresos> filteredIngresosList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_ingresos);

        Retrofit retrofit = RetrofitClient.getClient("http://192.168.1.44:8080/Puente/");
        apiService = retrofit.create(ApiService.class);

        // Inicializar vistas
        fechaInicioLIG = findViewById(R.id.fechaInicioLI);
        fechaFinLIG = findViewById(R.id.fechaFinLI);
        btnFiltrarLIG = findViewById(R.id.btnFiltrarLI);
        montoTotalLIG = findViewById(R.id.montoTotalLI);
        recyclerViewLIG = findViewById(R.id.recyclerViewLI);

        // Configurar RecyclerView
        recyclerViewLIG.setLayoutManager(new LinearLayoutManager(this));

        IngresosAdapter = new IngresosAdapter(filteredIngresosList, ingreso -> {
            Log.d("Lista_Ingresos", "Ingreso seleccionado: " + ingreso.getId());
            Intent intent = new Intent(Lista_Ingresos.this, Ingresos_Especificos.class);
            intent.putExtra("ingreso", ingreso);
            intent.putExtra("ingresoId", ingreso.getId());
            Log.d("Lista_Ingresos", "ID de ingreso enviado: " + ingreso.getId());
            startActivity(intent);
        });
        recyclerViewLIG.setAdapter(IngresosAdapter);

        // Cargar ingresos iniciales
        CargarIngresos();

        // Configurar botón de filtrar
        btnFiltrarLIG.setOnClickListener(v -> {
            // Validación para asegurarse de que los campos no estén vacíos
            String fechaInicioStr = fechaInicioLIG.getText().toString();
            String fechaFinStr = fechaFinLIG.getText().toString();

            if (fechaInicioStr.isEmpty()) {
                fechaInicioLIG.setError("La fecha de inicio no puede estar vacía");
                return; // Detener el proceso de filtrado
            }

            if (fechaFinStr.isEmpty()) {
                fechaFinLIG.setError("La fecha de fin no puede estar vacía");
                return; // Detener el proceso de filtrado
            }

            // Si las fechas no están vacías, proceder con el filtrado
            filtrarIngresosPorFecha(fechaInicioStr, fechaFinStr);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        CargarIngresos(); // Volver a cargar la lista de ingresos al regresar a esta actividad
    }

    private void CargarIngresos() {
        Call<List<Ingresos>> call = apiService.getIngresos();
        call.enqueue(new Callback<List<Ingresos>>() {
            @Override
            public void onResponse(Call<List<Ingresos>> call, Response<List<Ingresos>> response) {
                if (response.isSuccessful()) {
                    List<Ingresos> ingresos = response.body();
                    if (ingresos != null) {
                        listaIngresos.clear();
                        listaIngresos.addAll(ingresos);
                        filteredIngresosList.clear();
                        filteredIngresosList.addAll(ingresos);
                        IngresosAdapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
                        actualizarMontoTotal(); // Actualizar monto total
                    }
                } else {
                    Log.e("Lista_Ingresos", "Error en la respuesta de la API: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Ingresos>> call, Throwable t) {
                Log.e("Lista_Ingresos", "Error en la llamada a la API", t);
            }
        });
    }

    private void filtrarIngresosPorFecha(String fechaInicioStr, String fechaFinStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        filteredIngresosList.clear();

        for (Ingresos ingreso : listaIngresos) {
            try {
                Date fechaIngreso = dateFormat.parse(ingreso.getFecha());
                Date fechaInicio = dateFormat.parse(fechaInicioStr);
                Date fechaFin = dateFormat.parse(fechaFinStr);

                if ((fechaIngreso.equals(fechaInicio) || fechaIngreso.after(fechaInicio)) &&
                        (fechaIngreso.equals(fechaFin) || fechaIngreso.before(fechaFin))) {
                    filteredIngresosList.add(ingreso);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        IngresosAdapter.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
        actualizarMontoTotal(); // Actualizar monto total
    }

    private void actualizarMontoTotal() {
        double montoTotal = 0;

        for (Ingresos ingreso : filteredIngresosList) {
            montoTotal += Double.parseDouble(ingreso.getMonto());
        }

        montoTotalLIG.setText(String.format("Monto Total: %.2f", montoTotal));
    }
}

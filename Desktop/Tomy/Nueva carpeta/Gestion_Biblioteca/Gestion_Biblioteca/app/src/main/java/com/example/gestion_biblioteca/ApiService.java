package com.example.gestion_biblioteca;

import com.example.gestion_biblioteca.Categorias.Categorias;
import com.example.gestion_biblioteca.Cuotas.Pagos;
import com.example.gestion_biblioteca.Gastos.Gastos;
import com.example.gestion_biblioteca.Ingresos.Ingresos;
import com.example.gestion_biblioteca.Inventarios.Libros;
import com.example.gestion_biblioteca.Retiros.Retiros;
import com.example.gestion_biblioteca.Socios.Socios;
import com.google.gson.JsonArray;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    //Socios

    @GET("Socios.php")
    Call<List<Socios>> getSocios();

    // Para obtener un socio específico
    @GET("Socios.php/{id}")
    Call<Socios> getSocio(@Path("id") Integer id);

    // Para actualizar un socio
    @PUT("Socios.php/{id}")
    Call<Socios> updateSocio(@Path("id") Integer id, @Body Socios socio);

    // Para eliminar un socio
    @DELETE("Socios.php/{id}")
    Call<Void> deleteSocio(@Path("id") Integer id);

    // Para agregar un nuevo socio
    @POST("Socios.php")
    Call<Socios> createSocio(@Body Socios socio);

    // Categorias

    @GET("Categorias.php")
    Call<List<Categorias>> getCategorias();

    @GET("Categorias.php")
    Call<List<Categorias>> getCategorias(@Query("id") Integer id);

    @POST("Categorias.php")
    Call<Categorias> createCategoria(@Body Categorias categorias);

    @PUT("Categorias.php")
    Call<Void> updateCategoria(@Query("id") Integer id, @Body Categorias categorias);

    @DELETE("Categorias.php/{id}")
    Call<Void> deleteCategoria(@Path("id") Integer id);

    //RETIROS

    @GET("Retiros.php")
    Call<List<Retiros>> getRetiros();

    @GET("Socios.php/{Telefono}")
    Call<JsonArray> verificarTelefono(@Path("Telefono") String telefono);

    @GET("Inventarios.php/{CODIGO}")
    Call<JsonArray> verificarCodigo(@Path("CODIGO") String codigo);

    @GET("Socios.php/{Nombre}")
    Call<JsonArray> verificarNombreSocio(@Path("Nombre") String nombre);

    @GET("Inventarios.php/{NOMBRE}")
    Call<JsonArray> verificarNombreLibro(@Path("NOMBRE") String nombreLibro);


    @POST("Retiros.php")
    Call<Retiros> createRetiros(@Body Retiros retiros);

    @PUT("Retiros.php/{id}")
    Call<Retiros> updateRetiros(@Path("id") Integer id, @Body Retiros retiros);

    @DELETE("Retiros.php/{id}")
    Call<Void> deleteRetiros(@Path("id") Integer id);

    //Inventarios

    @GET("Inventarios.php")
    Call<List<Libros>> getLibros();

    // Para obtener un socio específico
    @GET("Inventarios.php/{id}")
    Call<Libros> getLibro(@Path("id") Integer id);

    // Para actualizar un socio
    @PUT("Inventarios.php/{id}")
    Call<Libros> updateLibro(@Path("id") Integer id, @Body Libros libro);

    // Para eliminar un socio
    @DELETE("Inventarios.php/{id}")
    Call<Void> deleteLibro(@Path("id") Integer id);

    // Para agregar un nuevo socio
    @POST("Inventarios.php")
    Call<Libros> createLibros(@Body Libros libro);

    //Ingresos

    @GET("Ingresos.php")
    Call<List<Ingresos>> getIngresos();

    // Para obtener un socio específico
    @GET("Ingresos.php/{id}")
    Call<Ingresos> getIngresos(@Path("id") Integer id);

    // Para actualizar un socio
    @PUT("Ingresos.php/{id}")
    Call<Ingresos> updateIngreso(@Path("id") Integer id, @Body Ingresos ingreso);

    // Para eliminar un socio
    @DELETE("Ingresos.php/{id}")
    Call<Void> deleteIngreso(@Path("id") Integer id);

    // Para agregar un nuevo socio
    @POST("Ingresos.php")
    Call<Ingresos> createIngreso(@Body Ingresos ingreso);

    //Gastos

    @GET("Gastos.php")
    Call<List<Gastos>> getGastos();

    // Para obtener un socio específico
    @GET("Gastos.php/{id}")
    Call<List<Gastos>> getGastos(@Path("id") Integer id);

    // Para actualizar un socio
    @PUT("Gastos.php/{id}")
    Call<Gastos> updateGasto(@Path("id") Integer id, @Body Gastos gasto);

    // Para eliminar un socio
    @DELETE("Gastos.php/{id}")
    Call<Void> deleteGasto(@Path("id") Integer id);

    // Para agregar un nuevo socio
    @POST("Gastos.php")
    Call<Gastos> createGastos(@Body Gastos gasto);

    //Cuotas

    @GET("Cuotas.php")
    Call<List<Pagos>> getCuotas();

    // Para obtener un socio específico
    @GET("Cuotas.php/{id}")
    Call<Pagos> getCuotas(@Path("id") Integer id);

    // Para actualizar un socio
    @PUT("Cuotas.php/{id}")
    Call<Pagos> updateCuotas(@Path("id") Integer id, @Body Pagos pago);

    // Para eliminar un socio
    @DELETE("Cuotas.php/{id}")
    Call<Void> deleteCuotas(@Path("id") Integer id);

    // Para agregar un nuevo socio
    @POST("Cuotas.php")
    Call<Pagos> createCuotas(@Body Pagos pago);
}

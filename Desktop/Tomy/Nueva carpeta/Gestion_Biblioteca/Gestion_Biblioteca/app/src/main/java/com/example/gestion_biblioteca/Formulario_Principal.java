package com.example.gestion_biblioteca;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gestion_biblioteca.Categorias.FrmCategorias;
import com.example.gestion_biblioteca.Cuotas.FrmCuotas;
import com.example.gestion_biblioteca.Gastos.FrmGastos;
import com.example.gestion_biblioteca.Ingresos.FrmIngresos;
import com.example.gestion_biblioteca.Inventarios.FrmInventarios;
import com.example.gestion_biblioteca.Retiros.FrmRetiros;
import com.example.gestion_biblioteca.Socios.FrmSocios;

public class Formulario_Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario_principal);

        Button btnSocios = findViewById(R.id.btnSocios);
        Button btnGastos = findViewById(R.id.btnGastos);
        Button btnCategorias = findViewById(R.id.btnCategorias);
        Button btnInventarios = findViewById(R.id.btnInventarios);
        Button btnRetiros = findViewById(R.id.btnRetiros);
        Button btnCuotas = findViewById(R.id.btnCuotas);
        Button btnIngresos = findViewById(R.id.btnIngresos);


        btnSocios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Formulario_Principal.this, FrmSocios.class);
                startActivity(intent);
            }
        });

        btnGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Formulario_Principal.this, FrmGastos.class);
                startActivity(intent);
            }
        });

        btnCategorias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Formulario_Principal.this, FrmCategorias.class);
                startActivity(intent);
            }
        });

        btnInventarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Formulario_Principal.this, FrmInventarios.class);
                startActivity(intent);
            }
        });

        btnRetiros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Formulario_Principal.this, FrmRetiros.class);
                startActivity(intent);
            }
        });

        btnCuotas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Formulario_Principal.this, FrmCuotas.class);
                startActivity(intent);
            }
        });

        btnIngresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Formulario_Principal.this, FrmIngresos.class);
                startActivity(intent);
            }
        });
    }
}

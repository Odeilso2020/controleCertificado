package br.com.progiv.appcontrolecertificado;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.progiv.appcontrolecertificado.DAO.CertificadosDAO;
import br.com.progiv.appcontrolecertificado.Model.CertificadosModel;

public class MainActivity extends AppCompatActivity {

    // Declarando as variaveis de CAST
    ListView listView;
    Button btnCadastrar;
    CertificadosDAO certificadosDAO;
    ArrayList<CertificadosModel> listaCertificados;
    ArrayAdapter adapter;
    CertificadosModel certificado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listaCertificados);
        btnCadastrar = (Button)findViewById(R.id.btnCadastrar);

        // Escuta o botão de cadastar
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Certificados.class);
                startActivity(intent);
            }
        });

        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                certificado = (CertificadosModel)adapterView.getItemAtPosition(i);
                Intent intent = new Intent(MainActivity.this, Certificados.class);
                intent.putExtra("selectCertificado", certificado);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                certificado = (CertificadosModel) parent.getItemAtPosition(position);
                return false;
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuItem menuItem = menu.add("Deletar Certificado");

        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                certificadosDAO = new CertificadosDAO(MainActivity.this);
                certificadosDAO.deletarCertificado(certificado);
                certificadosDAO.close();
                carregarListaCertificados();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarListaCertificados();
    }

    // Método para buscar os dados em BD
    public void carregarListaCertificados(){
        certificadosDAO = new CertificadosDAO(MainActivity.this);
        listaCertificados = certificadosDAO.listaCertificados();
        certificadosDAO.close();
        if(listaCertificados != null){
            adapter = new ArrayAdapter<CertificadosModel>(MainActivity.this, android.R.layout.simple_list_item_1, listaCertificados);
            listView.setAdapter(adapter);
        }
    }
}
package br.com.progiv.appcontrolecertificado.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import br.com.progiv.appcontrolecertificado.Certificados;
import br.com.progiv.appcontrolecertificado.Model.CertificadosModel;

public class CertificadosDAO extends SQLiteOpenHelper {

    // Constante para versionamento do BD.
    private static final String DATABASE = "cadCertificados";
    private static final int VERSION = 1;

    // Mensagem de erro
    public String msmErro = "";

    // Construtor
    public CertificadosDAO(Context context){
        super(context, DATABASE, null, VERSION);
        msmErro = "";
    }

    // Métodos de acesso ao BD.
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE  certificados(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "titulo TEXT NOT NULL, " +
                "descricao TEXT NOT NULL, " +
                "quantidade INTEGER NOT NULL DEFAULT 0," +
                "foto TEXT NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Verificar a versão do BD, caso for a mesma versão, não altera.
        if(oldVersion != newVersion){
            String sql = "DROP TABLE IF EXISTS certificados";
            db.execSQL(sql);
        }
    }

    // CRUD da aplicação
    // Método de inserir o certificado
    public boolean salvarCertificado(CertificadosModel certificado){
        try{
            ContentValues values = new ContentValues();
            values.put("titulo", certificado.getTitulo());
            values.put("descricao", certificado.getDescricao());
            values.put("quantidade", certificado.getQuantidade());
            values.put("foto", certificado.getFoto());

            // Escreva na tabela do BD
            getWritableDatabase().insert("certificados", null, values);
            return true;
        }catch (Exception ex){
            msmErro = ex.getMessage();
            return false;
        }
    }

    // Método de alterar o certificado
    public boolean alterarCertificado(CertificadosModel certificado){
        try{
            ContentValues values = new ContentValues();
            values.put("titulo", certificado.getTitulo());
            values.put("descricao", certificado.getDescricao());
            values.put("quantidade", certificado.getQuantidade());
            values.put("foto", certificado.getFoto());

            // Pegar o id
            String[] args = {certificado.getId().toString()};

            // Escreva na tabela do BD
            getWritableDatabase().update("certificados", values, "id=?", args);
            return true;
        }catch (Exception ex){
            msmErro = ex.getMessage();
            return false;
        }
    }

    // Método de deletar o certificado
    public boolean deletarCertificado(CertificadosModel certificado){
        try{
            // Pegar o id
            String[] args = {certificado.getId().toString()};
            getWritableDatabase().delete("certificados", "id=?", args);
            return true;
        }catch(Exception ex){
            msmErro = ex.getMessage();
            return false;
        }
    }

    // Método de listar os certificados
    public ArrayList<CertificadosModel> listaCertificados(){
        ArrayList<CertificadosModel> certificados = new ArrayList<>();
        String[] columns = {"id", "titulo", "descricao", "quantidade", "foto"};
        Cursor cursor = getWritableDatabase().query("certificados", columns, null, null, null, null, null);
        while(cursor.moveToNext()){
            CertificadosModel certificado = new CertificadosModel();
            certificado.setId(cursor.getLong(0));
            certificado.setTitulo(cursor.getString(1));
            certificado.setDescricao(cursor.getString(2));
            certificado.setQuantidade(cursor.getInt(3));
            certificado.setFoto(cursor.getString(4));
            certificados.add(certificado);
        }
        return certificados;
    }
}
package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> permisos = new ArrayList<String>();
        permisos.add(Manifest.permission.CAMERA);
        permisos.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permisos.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permisos.add(Manifest.permission.WRITE_CALENDAR);


        getPermission(permisos);
    }

    public void getPermission(ArrayList<String> permisosSolicitados){

        ArrayList<String> listPermisosNOAprob = getPermisosNoAprobados(permisosSolicitados);
        if (listPermisosNOAprob.size()>0)
             if (Build.VERSION.SDK_INT >= 23)
                  requestPermissions(listPermisosNOAprob.toArray(new String[listPermisosNOAprob.size()]), 1);

    }


    public ArrayList<String> getPermisosNoAprobados(ArrayList<String>  listaPermisos) {
        ArrayList<String> list = new ArrayList<String>();
        for(String permiso: listaPermisos) {
            if (Build.VERSION.SDK_INT >= 23)
                 if(checkSelfPermission(permiso) != PackageManager.PERMISSION_GRANTED)
                     list.add(permiso);

        }
        return list;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        String s="";
        if(requestCode==1)    {
            for(int i =0; i<permissions.length;i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    s=s + "OK " + permissions[i] + "\n";
                else
                    s=s + "NO  " + permissions[i] + "\n";
            }
            Toast.makeText(this.getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }
    }

    public void MostrarDescargas(View view){

        Intent intent = new Intent();
        intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(intent);

    }
    public String DataPath="";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case 9999:
                Log.i("Test", "Result URI " + data.getData());
                DataPath= data.getDataString();
                break;
        }
    }
    public void BajarDoc(View view){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
            i.addCategory(Intent.CATEGORY_DEFAULT);
            startActivityForResult(Intent.createChooser(i, "Choose directory"), 9999);
        }


        Toast toast = Toast.makeText(this, DataPath, Toast.LENGTH_SHORT);
        toast.show();
        System.out.println(DataPath);
        String url = "https://www.uteq.edu.ec/revistacyt/archivositio/instrucciones_arbitros.pdf";
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("PDF");
        request.setTitle("Pdf");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            }
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "filedownload.pdf");
        System.out.println(Environment.DIRECTORY_DOWNLOADS);
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        try {
            manager.enqueue(request);
        } catch (Exception e) {
            Toast.makeText(this.getApplicationContext(),"Error: "  + e.getMessage(),Toast.LENGTH_LONG).show();
        }

    }


}
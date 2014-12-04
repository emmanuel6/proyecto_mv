package com.example.navegador;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
//Jesús Emmanuel Arreola Mejia
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class Borrar extends Activity implements OnClickListener {
	
	Button btlisto, btborra;
	mysql objcon = new mysql(this, null, null, 1);
	EditText txtborra;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrar_layout);
        btlisto = (Button)findViewById(R.id.btlisto2);
        btborra = (Button)findViewById(R.id.btborra2);
        txtborra = (EditText)findViewById(R.id.txt_borra);
        
        btlisto.setOnClickListener(this);
        btborra.setOnClickListener(this);
        
        ArrayList<Favoritos> secciones=
        		new ArrayList<Favoritos>();
        
        try {
        	objcon = new mysql(this, null, null, 1);
        	
			SQLiteDatabase db = objcon.getReadableDatabase();
			Cursor rs = db.rawQuery("select * from Favoritos order by nombre ", null);
			//                              nombre
			Favoritos obj;
			while(rs.moveToNext()){
				obj=new Favoritos();
				obj.setNombre(rs.getString(0));
				obj.setUrl(rs.getString(1));
				secciones.add(obj);
				
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
        
        Spinner sp = (Spinner)findViewById(R.id.spinner1);
        ArrayAdapter<Favoritos> adaptador=
        		new ArrayAdapter<Favoritos>(this, android.R.layout.simple_list_item_1,
        				secciones);
        
        sp.setAdapter(adaptador);
        
        sp.setOnItemSelectedListener(new OnItemSelectedListener() {
        	public void onItemSelected(AdapterView<?> parentView, View selectedItemView,
        			int position, long id){
        		// mensaje toast
        		 //  obtiene la informacion del spinner seleccionado
        		String seleccion= parentView.getItemAtPosition(position).toString();
        		// creamos una condicion para ver si la opcion esta sola
        		if(seleccion.trim().length() == 0){
        		 //si no tiene campos la seleccion no hara nada
    			}else{ // y si el campo no esta vacio
    				
    			Toast.makeText(parentView.getContext(), "Has Seleccionado " +seleccion, Toast.LENGTH_SHORT).show(); 
        		SQLiteDatabase db = objcon.getReadableDatabase();
    			Cursor c = db.rawQuery("SELECT * FROM Favoritos WHERE nombre='"+ seleccion +"'", null);
    			
    			if(c.moveToFirst()){

				
				txtborra.setText(c.getString(0));
				
				//String borrar_fav = txtborra.getText().toString();
			
    				
				//Toast.makeText(parentView.getContext(), "Se ha borrado: " +seleccion+"de tus favoritos", Toast.LENGTH_SHORT).show(); 
    			}

    		 }
    			
        	}
        	public void onNothingSelected(AdapterView<?> parentView){
        		
        	}
        	
		});
        
        
        
    }


	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view==btborra)
    	{
			if(txtborra.getText().toString().trim().length()==0)
			{
				Toast.makeText(getApplicationContext(), "Error, No has seleccionado ningun favorito", Toast.LENGTH_SHORT).show(); 
			}else {
				SQLiteDatabase db = objcon.getReadableDatabase();
				db.execSQL("DELETE FROM Favoritos WHERE nombre='"+txtborra.getText()+"' ");	
				Toast.makeText(getApplicationContext(), "Borraste: "+txtborra.getText()+" de tus favoritos" , Toast.LENGTH_SHORT).show();
				Intent menu = new Intent(this, Main.class);
				startActivity(menu);
			}
			
    	}
		else if(view==btlisto){
			Intent menu = new Intent(this, Main.class);
			startActivity(menu);
		}
		
	}
	
}
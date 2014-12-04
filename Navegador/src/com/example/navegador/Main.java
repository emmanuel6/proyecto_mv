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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;


public class Main extends Activity implements OnClickListener {
	
	WebView webview;
	ImageButton btbuscar, btatras, btadelante, btactualizar, btaddfavorito, btconfigfav, btborrarhist;
	EditText txturl;
	mysql objcon = new mysql(this, null, null, 1);
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        // asignar id a cada boton, editText, Webview
        btbuscar = (ImageButton)findViewById(R.id.btbuscar);
        btatras = (ImageButton)findViewById(R.id.btvolver);
        btactualizar = (ImageButton)findViewById(R.id.btactualizar);
        btadelante = (ImageButton)findViewById(R.id.btadelante);
        btaddfavorito = (ImageButton)findViewById(R.id.btagregarfavorito);
        btconfigfav = (ImageButton)findViewById(R.id.btborrarfav);
        btborrarhist = (ImageButton)findViewById(R.id.btborrarhistorial);
        txturl = (EditText)findViewById(R.id.txtlink);
        webview = (WebView)findViewById(R.id.webView1);
        
        //configuracion del zoom en el webView
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setSupportZoom(true);
        
        // configuracion  para activar el jaavascript
        WebSettings configuracionWeb = webview.getSettings();
        configuracionWeb.setJavaScriptEnabled(true);
        
        //Crear cliente para despúes crear clase que 
        //  evite salir a un navegador externo
        webview.setWebViewClient(new clienteWeb());
        webview.loadUrl("https://www.google.com.mx/");
        
        //asignando el listener (acciones) a los botones
        btbuscar.setOnClickListener(this);
        btatras.setOnClickListener(this);
        btactualizar.setOnClickListener(this);
        btadelante.setOnClickListener(this);
        btaddfavorito.setOnClickListener(this);
        btconfigfav.setOnClickListener(this);
        btborrarhist.setOnClickListener(this);
        
   //Crear una variable que toma la url actual y la localiza en el edittext
        String nuevoLink = webview.getUrl();
        txturl.setText(nuevoLink);
        
        
        
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
        				
        			
    			//	Toast.makeText(getApplicationContext(), "nada por aqui", Toast.LENGTH_SHORT).show();
    			}else{ // y si el campo no esta vacio
    				
    			Toast.makeText(parentView.getContext(), "Has Seleccionado " +seleccion, Toast.LENGTH_SHORT).show(); 
        		SQLiteDatabase db = objcon.getReadableDatabase();
    			Cursor c = db.rawQuery("SELECT * FROM Favoritos WHERE nombre='"+ seleccion +"'", null);
    			if(c.moveToFirst()){
    				//txturl.setText("");
				txturl.invalidate();
				txturl.setText("");
				
				//se empiezan a mostrar los datos desde la pocision0
				// pero se necesita solo la pocision 1 (url)
				txturl.setText(c.getString(1));
				String cargafav = txturl.getText().toString();
				webview.loadUrl(cargafav);
    				
    				
    			}

    		 }
    			
        	}
        	public void onNothingSelected(AdapterView<?> parentView){
        		
        	}
        	
		});
        
    }// fin del oncreate

    
    
    
    /* clase que obliga a cargar los links en el webview */
    private class clienteWeb extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            
        	view.loadUrl(url);
            
            Toast.makeText(getApplicationContext(), "Cargando Página ", Toast.LENGTH_SHORT).show();
            String linkOriginal = webview.getUrl();
	        txturl.setText(linkOriginal);
	        
            return true;
        }
        
    }
    
	
    
    
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
    	
    	// uso del switch para los eventos segun el boton seleccionado
    	switch (v.getId()) {
    	
    	case R.id.btbuscar:
    		String SitioABuscar = txturl.getText().toString();
    		webview.setWebViewClient(new clienteWeb());
    		webview.loadUrl(SitioABuscar);
    		
    		Toast.makeText(getApplicationContext(), "Cargando:"+ SitioABuscar +"..", Toast.LENGTH_LONG).show();
    		
    		// mostrar url actual en el edit Text
    		String nuevoLink = webview.getUrl();
    		txturl.setText(nuevoLink);
    		
    		//esconder teclado despues de buscar el link
	        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(txturl.getWindowToken(), 0);
			break;
    		
			
    	case R.id.btvolver:
    		if(webview.canGoBack()){
    			webview.goBack();
    			/*
    			SitioABuscar = txturl.getText().toString();
        		webview.setWebViewClient(new clienteWeb());
        		webview.loadUrl(SitioABuscar);
        		*/
        		nuevoLink = webview.getUrl();
        		txturl.setText(nuevoLink);
    		}else{
    			Toast.makeText(getApplicationContext(), "No has visitado paginas previamente. ", Toast.LENGTH_SHORT).show();
    		}
    		break;
		
    	case R.id.btactualizar:
    		webview.reload();    		
    		//obtener  nuevos datos de la url
    		nuevoLink = webview.getUrl();
    		txturl.setText(nuevoLink);
    		
    		Toast.makeText(getApplicationContext(), "Actualizando...", Toast.LENGTH_SHORT).show();
    		break;
    		
    	case R.id.btadelante:
    		if(webview.canGoForward()){
    			webview.goForward();
    			
    			nuevoLink = webview.getUrl();
        		txturl.setText(nuevoLink);
    		}else{
    			Toast.makeText(getApplicationContext(), "No tienes páginas disponibles para ir.", Toast.LENGTH_SHORT).show();
    		}
    		break;
    		
    	case R.id.btborrarhistorial:
    		webview.clearHistory();
    		Toast.makeText(getApplicationContext(), "Borrando el historial...", Toast.LENGTH_LONG).show();
    		break;
    	case R.id.btagregarfavorito:
    		String link = txturl.getText().toString();
    		
    		Bundle parametros = new Bundle();
    		parametros.putString("nombre", link);
    		
    		Intent i = new Intent(this, AddFavorito.class);
    		i.putExtras(parametros);
    		startActivity(i);
    		break;
    		
    		
    	case R.id.btborrarfav:
    		Intent borra = new Intent(this, Borrar.class);
    		startActivity(borra);
    		break;
    	} // fin del switch
		
    	
	}//fin del on click 
    
    
    
    
    
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





	
}




















                                                                                                                        //Jesús Emmanuel Arreola Mejia

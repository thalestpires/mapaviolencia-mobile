package br.ufrj.dcc.mapaviolencia;

import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;

public class NoticiaListActivity extends ListActivity {

	public static final String NOTICIAS_EXTRA = "noticias";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_list);
		
		List<Noticia> noticias = App.getNoticias();
		
		setListAdapter(new NoticiaListViewAdapter(this, noticias));
	}

}
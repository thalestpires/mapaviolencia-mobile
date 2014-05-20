package br.ufrj.dcc.mapaviolencia;

import java.util.List;
import java.util.Locale;

import org.ocpsoft.pretty.time.PrettyTime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NoticiaListViewAdapter extends BaseAdapter {

	private static PrettyTime prettyTime = new PrettyTime(new Locale("pt"));
	
	private LayoutInflater mInflater;
	
	private List<Noticia> noticias;

	public NoticiaListViewAdapter(Context context, List<Noticia> noticias) {
		this.noticias = noticias;
		this.mInflater = LayoutInflater.from(context);
	}

	public int getCount() { 
		return noticias.size(); 
	} 

	public Noticia getItem(int position) { 
		return noticias.get(position); 
	} 

	public long getItemId(int position) { 
		return position; 
	}

	public View getView(int position, View view, ViewGroup parent) { 

		Noticia noticia = noticias.get(position);

		//infla o layout para podermos pegar as views 
		view = mInflater.inflate(R.layout.list_item, null); 

		TextView titulo = ((TextView) view.findViewById(R.id.list_item_titulo));
		titulo.setText(noticia.getTitulo());

		TextView data = ((TextView) view.findViewById(R.id.list_item_data)); 
		data.setText(noticia.getPrettyTime());

		//retorna a view com as informações 
		return view; 
	} 
}

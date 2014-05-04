package br.ufrj.dcc.mapaviolencia;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.ocpsoft.pretty.time.PrettyTime;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class MapInfoWindow implements InfoWindowAdapter {

	private static PrettyTime prettyTime = new PrettyTime(new Locale("pt"));
	
	private LayoutInflater inflater;
	private Map<String, Noticia> mapMarketNoticia;

	public MapInfoWindow(LayoutInflater inflater, Map<String, Noticia> mapMarketNoticia) {
		this.inflater = inflater;
		this.mapMarketNoticia = mapMarketNoticia;
	}
	
	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	@Override
	public View getInfoContents(Marker marker) {
		Noticia noticia = mapMarketNoticia.get(marker.getId());
		
		View view = this.inflater.inflate(R.layout.infowindow, null);
		
		TextView title = (TextView)view.findViewById(R.id.infowindow_title);
		title.setText(noticia.getTitulo());
		
		TextView subtitle = (TextView)view.findViewById(R.id.infowindow_subtitle);
		subtitle.setText(noticia.getSubTitulo());
		
		TextView date = (TextView)view.findViewById(R.id.infowindow_date);
		date.setText(prettyTime.format(new Date(noticia.getTimestamp())));
		
		TextView tag = (TextView)view.findViewById(R.id.infowindow_tag);
		Set<CategoriasViolencia> categorias = noticia.getCategoriasViolencia();
		tag.setText(toString(categorias));
		
		return view;
	}

	private String toString(Set<CategoriasViolencia> categorias) {
		StringBuilder builder = new StringBuilder();
		for (CategoriasViolencia categoria : categorias) {
			builder.append("#");
			builder.append(categoria.name());
			builder.append(" ");
		}
		return builder.toString();
	}


}

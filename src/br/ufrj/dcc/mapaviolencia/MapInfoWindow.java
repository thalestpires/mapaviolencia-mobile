package br.ufrj.dcc.mapaviolencia;

import java.util.Map;
import java.util.Set;

import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;

public class MapInfoWindow implements InfoWindowAdapter {

	private Map<String, Noticia> mapMarketNoticia;

	private MapActivity activity;

	public MapInfoWindow(MapActivity activity, Map<String, Noticia> mapMarketNoticia) {
		this.activity = activity;
		this.mapMarketNoticia = mapMarketNoticia;
	}
	
	@Override
	public View getInfoWindow(Marker marker) {
		return null;
	}

	@Override
	public View getInfoContents(Marker marker) {
		Noticia noticia = mapMarketNoticia.get(marker.getId());
		
		View view = activity.getLayoutInflater().inflate(R.layout.infowindow, null);
		
		TextView title = (TextView)view.findViewById(R.id.infowindow_title);
		title.setText(noticia.getTitulo());
		
		TextView subtitle = (TextView)view.findViewById(R.id.infowindow_subtitle);
		subtitle.setText(noticia.getSubTitulo());
		
		TextView date = (TextView)view.findViewById(R.id.infowindow_date);
		date.setText(noticia.getPrettyTime());
		
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

package br.ufrj.dcc.mapaviolencia;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import br.ufrj.dcc.mapaviolencia.FetchNoticiaTask.Since;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements ActionBar.OnNavigationListener, OnMapClickListener {
	
	private final LatLng defaultLocation = new LatLng(-22.762295, -43.15567);
	private GoogleMap map;
	Map<String, Noticia> mapMarkerNoticia = new HashMap<String, Noticia>();
	
	private Since since = Since.SINCE_LASTWEEK;
	
	private List<Noticia> noticias;
	
	private static final CategoriasViolencia[] CATEGORIAS = CategoriasViolencia.values();
	
	private boolean[] categoriasChecked = new boolean[CATEGORIAS.length];
	
	private LinkedHashSet<CategoriasViolencia> categoriasFilter = new LinkedHashSet<CategoriasViolencia>(
		Arrays.asList(CATEGORIAS)
	);
	
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_map);
		
		for (int i = 0; i < categoriasChecked.length; i++) {
			categoriasChecked[i] = true;
		}
		

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_section1),
								getString(R.string.title_section2), 
								getString(R.string.title_section3),
								getString(R.string.title_section4),
								}), this);
		
		setMap(((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap());
		getMap().setInfoWindowAdapter(new MapInfoWindow(getLayoutInflater(), mapMarkerNoticia));
		getMap().setOnInfoWindowClickListener(new OnInfoWindowClickListener() {          
			public void onInfoWindowClick(Marker marker) {
				Noticia noticia = (Noticia) mapMarkerNoticia.get(marker.getId());
				Uri uri = Uri.parse(noticia.getUrl());
				if (uri != null) {
					startActivity(new Intent(Intent.ACTION_VIEW, uri));
				}
			}
		});
		getMap().setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				return false;
			}

		}); 
		
		getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation , 15));
		getMap().animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

		doFetchNoticias();
	}

	private void doFetchNoticias() {
		new FetchNoticiaTask(this, since).execute();
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			doFetchNoticias();
			return true;
		} else if (id == R.id.action_about) {
			Toast.makeText(this, R.string.about_message, Toast.LENGTH_LONG).show();
		} else if (id == R.id.action_filter) {
			final CharSequence[] items = new CharSequence[CATEGORIAS.length];
			for (int i = 0; i < items.length; i++) {
				items[i] = CATEGORIAS[i].toString();
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Selecione as categorias");
			builder.setPositiveButton("OK", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					updateMapMarkers();
				}
			});
			builder.setMultiChoiceItems(items, categoriasChecked, new OnMultiChoiceClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which, boolean isChecked) {
					CharSequence charSequence = items[which];
					CategoriasViolencia categoria = CategoriasViolencia.valueOf(charSequence.toString());
					if (isChecked) {
						categoriasFilter.add(categoria);
					} else {
						categoriasFilter.remove(categoria);
					}
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
			
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		if (position == 0) since = Since.SINCE_LASTWEEK;
		else if (position == 1) since = Since.SINCE_ONE_MONTH;
		else if (position == 2) since = Since.SINCE_TWO_MONTH;
		else if (position == 3) since = Since.SINCE_THREE_MONTH;
		else throw new IllegalArgumentException();
		doFetchNoticias();
		return true;
	}
	
	public GoogleMap getMap() {
		return map;
	}

	public void setMap(GoogleMap map) {
		this.map = map;
	}
	
	public void clearMarkers() {
		this.mapMarkerNoticia.clear();
		getMap().clear();
	}
	
	public void updateMapMarkers() {
		clearMarkers();
		for (Noticia n : noticias) {
			if (! Collections.disjoint(n.getCategoriasViolencia(), categoriasFilter)) {
				addMapMarker(n);
			}
		}
	}
	
	public void addMapMarker(Noticia noticia) {
		LatLng newLocation = new LatLng(noticia.getLatitude(), noticia.getLongitude());
		Marker marker = getMap().addMarker(new MarkerOptions().position(newLocation));
		this.mapMarkerNoticia.put(marker.getId(), noticia);
	}

	public List<Noticia> getNoticias() {
		return noticias;
	}

	public void setNoticias(List<Noticia> noticias) {
		this.noticias = noticias;
	}

	@Override
	public void onMapClick(LatLng clickLocation) {
		// TODO Auto-generated method stub
		
	}
	
}
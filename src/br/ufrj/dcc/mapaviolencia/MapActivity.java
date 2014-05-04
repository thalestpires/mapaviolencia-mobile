package br.ufrj.dcc.mapaviolencia;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import br.ufrj.dcc.mapaviolencia.FetchNoticiaTask.Since;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity implements ActionBar.OnNavigationListener {
	
	private final LatLng defaultLocation = new LatLng(-22.762295, -43.15567);
	private GoogleMap map;
	Map<String, Object> mapMarkerId = new HashMap<String, Object>();
	
	private Since since = Since.SINCE_TODAY;
	
	private List<Noticia> noticias;
	
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_map);

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
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		if (position == 0) since = Since.SINCE_TODAY;
		else if (position == 1) since = Since.SINCE_YESTERDAY;
		else if (position == 2) since = Since.SINCE_LASTWEEK;
		else if (position == 3) since = Since.SINCE_LASTMONTH;
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
	
	public void addMapMarker(Noticia noticia) {
		String title = noticia.getTitulo();
		String url = noticia.getUrl();
		double lat = noticia.getLatitude();
		double longitude = noticia.getLongitude();
		
		LatLng newLocation = new LatLng(lat, longitude);
		Marker marker = getMap().addMarker(new MarkerOptions().position(newLocation).title(title));
		this.mapMarkerId.put(marker.getId(), url);
		this.getMap().setOnMarkerClickListener(new OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {
				return false;
			}

		});  
		this.getMap().setInfoWindowAdapter(new InfoWindowAdapter() {

			@Override
			public View getInfoWindow(Marker args) {
				return null;
			}

			@Override
			public View getInfoContents(Marker marker) {
				getMap().setOnInfoWindowClickListener(new OnInfoWindowClickListener() {          
					public void onInfoWindowClick(Marker marker) {
						String noticia = (String) mapMarkerId.get(marker.getId());
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(noticia)));
					}
				});
				return null;
			}
		});
	}

	public List<Noticia> getNoticias() {
		return noticias;
	}

	public void setNoticias(List<Noticia> noticias) {
		this.noticias = noticias;
	}

}

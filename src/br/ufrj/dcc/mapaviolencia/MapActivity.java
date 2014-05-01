package br.ufrj.dcc.mapaviolencia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

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
	
	final private LatLng defaultLocation = new LatLng(-19.92, -43.64);
	private GoogleMap map;
	private Map<String, Object> mapMarkerId = new HashMap<String, Object>();
	
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
		
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation , 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

		new RequestTask().execute();
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
			new RequestTask().execute();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		return true;
	}

	class RequestTask extends AsyncTask<String, String, String> {
		
		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(MapActivity.this, "", "Carregando...", true);
		}
		
		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
				calendar.clear(Calendar.HOUR_OF_DAY);
				calendar.clear(Calendar.MINUTE);
				calendar.clear(Calendar.SECOND);
				calendar.clear(Calendar.MILLISECOND);
				long timestamp = calendar.getTimeInMillis();
				
				response = httpclient.execute(new HttpGet("http://mapaviolencia.ufrj.cloudbees.net/rest/noticias/"));
				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode() == HttpStatus.SC_OK){
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else{
					//Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				//TODO Handle problems..
			} catch (IOException e) {
				//TODO Handle problems..
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			JSONArray array = null;
			try {
				array = new JSONArray(result);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			for (int i=0; i<array.length(); i++) {
				try {
					JSONObject jsonObject = array.getJSONObject(i);
					String title = jsonObject.getString("titulo");
					String url = jsonObject.getString("url");
					double latitude = jsonObject.getDouble("latitude");
					double longitude = jsonObject.getDouble("longitude");
					addMarker(title, url, latitude, longitude);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			dialog.dismiss();
		}
		
		private void addMarker(String title, String url, double lat, double longitude) {
			LatLng newLocation = new LatLng(lat, longitude);
			Marker marker = map.addMarker(new MarkerOptions().position(newLocation).title(title));
			mapMarkerId.put(marker.getId(), url);
			map.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker marker) {
					return false;
				}

			});  
			map.setInfoWindowAdapter(new InfoWindowAdapter() {

				@Override
				public View getInfoWindow(Marker args) {
					return null;
				}

				@Override
				public View getInfoContents(Marker marker) {
					map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {          
						public void onInfoWindowClick(Marker marker) {
							String noticia = (String) mapMarkerId.get(marker.getId());
							MapActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(noticia)));
						}
					});
					return null;
				}
			});
		}
	}

}

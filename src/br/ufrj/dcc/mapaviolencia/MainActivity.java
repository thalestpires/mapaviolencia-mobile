package br.ufrj.dcc.mapaviolencia;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {
	final private LatLng defaultLocation = new LatLng(-19.92, -43.64);
	private GoogleMap map;

	private Map<String, Object> mapMarkerId = new HashMap<String, Object>();

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		new RequestTask().execute();

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation , 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
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
						MainActivity.this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(noticia)));
					}
				});
				return null;
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	class RequestTask extends AsyncTask<String, String, String>{

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet("http://mapaviolencia.ufrj.cloudbees.net/rest/noticias"));
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
		}
	}
}
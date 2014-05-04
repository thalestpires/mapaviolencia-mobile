package br.ufrj.dcc.mapaviolencia;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class HttpUtil {

	public static String doGET(String url) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(url));
			return IOUtils.toString(response.getEntity().getContent());
		} catch (Exception e) {
			Log.e("[GET REQUEST]", "Network exception", e);
			return null;
		}
	}

}

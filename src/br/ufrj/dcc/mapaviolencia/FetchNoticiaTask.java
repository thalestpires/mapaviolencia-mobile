package br.ufrj.dcc.mapaviolencia;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

class FetchNoticiaTask extends AsyncTask<String, String, String> {

	public enum Since {
		SINCE_LASTWEEK,
		SINCE_ONE_MONTH,
		SINCE_TWO_MONTH,
		SINCE_THREE_MONTH,
	}

	private static final Type noticiasListType = new TypeToken<List<Noticia>>(){}.getType();

	private final Gson gson = new Gson();

	private final MapActivity mapActivity;
	private final Context mContext;
	private final Since since;

	private ProgressDialog dialog;


	protected FetchNoticiaTask(MapActivity mapActivity, Since since) {
		this.mapActivity = mapActivity;
		this.mContext = mapActivity;
		this.since = since;
	}

	private Long getTimestamp(Since since) {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		switch (since) {
		case SINCE_LASTWEEK:
			calendar.add(Calendar.WEEK_OF_YEAR, -1);
			break;
		case SINCE_ONE_MONTH:
			calendar.add(Calendar.MONTH, -1);
			break;
		case SINCE_TWO_MONTH:
			calendar.add(Calendar.MONTH, -2);
			break;
		case SINCE_THREE_MONTH:
			calendar.add(Calendar.MONTH, -3);
			break;
		default:
			throw new IllegalArgumentException();
		}
		return calendar.getTimeInMillis();
	}

	@Override
	protected void onPreExecute() {
		dialog = ProgressDialog.show(mContext, "", mapActivity.getString(R.string.carregando), true);
	}

	@Override
	protected String doInBackground(String... uri) {
		Long timestamp = getTimestamp(since);
		String url = mapActivity.getString(R.string.rest_api_url) + timestamp;
		return HttpUtil.doGET(url);
	}

	@Override
	protected void onPostExecute(String result) {
		List<Noticia> noticias = gson.fromJson(result, noticiasListType);
		App.setNoticias(new ArrayList<Noticia>(noticias));
		mapActivity.updateMapMarkers();
		dialog.dismiss();
	}

}
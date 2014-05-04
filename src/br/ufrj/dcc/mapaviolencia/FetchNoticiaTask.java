package br.ufrj.dcc.mapaviolencia;

import java.lang.reflect.Type;
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
		SINCE_TODAY,
		SINCE_YESTERDAY,
		SINCE_LASTWEEK,
		SINCE_LASTMONTH
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
		calendar.clear(Calendar.HOUR_OF_DAY);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);
		switch (since) {
		case SINCE_TODAY:
			break;
		case SINCE_YESTERDAY:
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			break;
		case SINCE_LASTWEEK:
			calendar.add(Calendar.WEEK_OF_YEAR, -1);
			break;
		case SINCE_LASTMONTH:
			calendar.add(Calendar.MONTH, -1);
			break;
		default:
			throw new IllegalArgumentException();
		}
		return calendar.getTimeInMillis();
	}

	@Override
	protected void onPreExecute() {
		dialog = ProgressDialog.show(mContext, "", mapActivity.getString(R.string.carregando), true);
		mapActivity.getMap().clear();
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
		for (Noticia noticia : noticias) {
			mapActivity.addMapMarker(noticia);
		}
		dialog.dismiss();
	}

}
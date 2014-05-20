package br.ufrj.dcc.mapaviolencia;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.ocpsoft.pretty.time.PrettyTime;

public class Noticia implements Serializable {

	private static final long serialVersionUID = 1L;

	private static PrettyTime prettyTime = new PrettyTime(new Locale("pt"));
	
	public static final String FIELD_TIMESTAMP = "timestamp";

	private String id;
	private Long timestamp;
	private String titulo;
	private String subTitulo;
	private String corpo;
	private String url;
	private String geolocalizacao;
	private double latitude;
	private double longitude;
	private NiveisLocalizacao nivelLocalizacao;
	private Set<CategoriasViolencia> categoriasViolencia = new HashSet<CategoriasViolencia>();

	public String getCorpo() {
		return corpo;
	}

	public void setCorpo(String assunto) {
		this.corpo = assunto;
	}

	public String getGeolocalizacao() {
		return geolocalizacao;
	}

	public void setGeolocalizacao(String geolocalizacao) {
		this.geolocalizacao = geolocalizacao;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getSubTitulo() {
		return subTitulo;
	}

	public void setSubTitulo(String subTitulo) {
		this.subTitulo = subTitulo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public Set<CategoriasViolencia> getCategoriasViolencia() {
		return categoriasViolencia;
	}

	public void setCategoriasViolencia(Set<CategoriasViolencia> categoriasViolencia) {
		this.categoriasViolencia = categoriasViolencia;
	}
	
	public NiveisLocalizacao getNivelLocalizacao() {
		return nivelLocalizacao;
	}

	public void setNivelLocalizacao(NiveisLocalizacao nivelLocalizacao) {
		this.nivelLocalizacao = nivelLocalizacao;
	}
	
	public String getPrettyTime() {
		return prettyTime.format(new Date(getTimestamp()));
	}
	
	@Override
	public String toString() {
		return "Noticia [id=" + id + ", timestamp=" + timestamp + ", titulo="+ titulo + "]";
	}
}

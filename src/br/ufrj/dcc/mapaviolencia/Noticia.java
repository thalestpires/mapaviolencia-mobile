package br.ufrj.dcc.mapaviolencia;

import java.util.HashSet;
import java.util.Set;

public class Noticia {

	private String id;
	private Long timestamp;
	private String titulo;
	private String subTitulo;
	private String corpo;
	private String geolocalizacao;
	private float latitude;
	private float longitude;
	private String url;
	private Set<String> tipoViolencia = new HashSet<String>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
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
	public String getCorpo() {
		return corpo;
	}
	public void setCorpo(String corpo) {
		this.corpo = corpo;
	}
	public String getGeolocalizacao() {
		return geolocalizacao;
	}
	public void setGeolocalizacao(String geolocalizacao) {
		this.geolocalizacao = geolocalizacao;
	}
	public float getLatitude() {
		return latitude;
	}
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}
	public float getLongitude() {
		return longitude;
	}
	public void setLongitude(float longitude) {
		this.longitude = longitude;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Set<String> getTipoViolencia() {
		return tipoViolencia;
	}
	public void setTipoViolencia(Set<String> tipoViolencia) {
		this.tipoViolencia = tipoViolencia;
	}

}

package br.ufrj.dcc.mapaviolencia;

import java.util.Date;

public class Noticia {

	private String corpo;
	private String geolocalizacao;
	private Date dataHora;
	private String titulo;
	private String subTitulo;
	private String url;
	private String id;
	public static final String FIELD_ASSUNTO = "assunto";
	public static final String FIELD_URL = "url";
	public static final String FIELD_ID = "id";
	public static final String FIELD_TITULO = "titulo";
	public static final String FIELD_SUB_TITULO = "subTitulo";

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

	public Date getDataHora() {
		return dataHora;
	}

	public void setDataHora(Date dataHora) {
		this.dataHora = dataHora;
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
	
	@Override
	public String toString() {
		return "Noticia [id="+id+", dataHora=" + dataHora + ", titulo=" + titulo +"]";
	}
}
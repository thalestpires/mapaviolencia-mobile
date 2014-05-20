package br.ufrj.dcc.mapaviolencia;

import java.util.ArrayList;
import java.util.List;

public class App  {

	private static List<Noticia> noticias;

	public static List<Noticia> getNoticias() {
		return noticias == null ? new ArrayList<Noticia>() : noticias;
	}

	public static void setNoticias(List<Noticia> news) {
		noticias = news;
	}
	
}

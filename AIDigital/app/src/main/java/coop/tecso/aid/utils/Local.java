package coop.tecso.aid.utils;


import android.graphics.drawable.Drawable;


public class Local {  

	private String localName; 
	private int localImage;  
	private Drawable img;
	private String descripcion;

	public Drawable getImg() {
		return  img;
	}

	public void setImg(Drawable img) {
		this.img = img;
	}

	public String getLocalName() {
		return localName;
	}
	public void setLocalName(String localName) {
		this.localName = localName;
	}
	public int getLocalImage() {
		return localImage;
	}
	public void setLocalImage(int i) {
		this.localImage = i;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
} 
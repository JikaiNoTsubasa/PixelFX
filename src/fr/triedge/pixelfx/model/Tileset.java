package fr.triedge.pixelfx.model;

import javax.xml.bind.annotation.XmlElement;

import fr.triedge.pixelfx.engine.Program;

public class Tileset implements OpenableItem{

	private String name;
	private String image;
	private int imageWidth, imageHeight;
	
	public String getImage() {
		return image;
	}
	@XmlElement
	public void setImage(String image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	@XmlElement
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public void open(Program prog) {
		prog.eventOpenTilsetNode(this);
	}
	
	@Override
	public String toString() {
		return getName();
	}
	public int getImageHeight() {
		return imageHeight;
	}
	@XmlElement
	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
	public int getImageWidth() {
		return imageWidth;
	}
	@XmlElement
	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}
}

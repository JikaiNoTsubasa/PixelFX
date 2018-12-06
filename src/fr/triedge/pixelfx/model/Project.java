package fr.triedge.pixelfx.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import fr.triedge.pixelfx.engine.Program;

@XmlRootElement
public class Project implements OpenableItem{

	private ArrayList<Tileset> tilesets = new ArrayList<>();
	private String name;

	public ArrayList<Tileset> getTilesets() {
		return tilesets;
	}

	@XmlElement(name="tileset")
	public void setTilesets(ArrayList<Tileset> tilesets) {
		this.tilesets = tilesets;
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
		prog.eventOpenProjectNode(this);
	}

	@Override
	public String toString() {
		return getName();
	}
}

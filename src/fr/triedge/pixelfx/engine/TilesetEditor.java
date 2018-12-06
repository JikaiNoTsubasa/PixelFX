package fr.triedge.pixelfx.engine;

import fr.triedge.pixelfx.model.Tileset;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.BorderPane;

public class TilesetEditor extends BorderPane{

	private Tileset tileset;
	private Canvas canvas;
	
	public TilesetEditor(Tileset tileset) {
		setTileset(tileset);
	}
	
	public void buildUI() {
		setCanvas(new Canvas());
		setCenter(getCanvas());
	}

	public Tileset getTileset() {
		return tileset;
	}

	public void setTileset(Tileset tileset) {
		this.tileset = tileset;
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas canvas) {
		this.canvas = canvas;
	}

	
}

package fr.triedge.pixelfx.engine;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.triedge.pixelfx.model.Tileset;
import fr.triedge.pixelfx.ui.PixelatedImageView;
import fr.triedge.pixelfx.utils.Utils;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class TilesetEditor extends BorderPane{

	private Tileset tileset;
	private Canvas canvas;
	private ToolBar toolbar;
	private Button btnSave;
	private ToggleButton btnPencil;
	private double currentScale = 0;
	private WritableImage img;
	private PixelatedImageView tmp;
	final DoubleProperty zoomProperty = new SimpleDoubleProperty(200);
	
	public TilesetEditor(Tileset tileset) {
		setTileset(tileset);
	}
	
	public void buildUI() {
		setCanvas(new Canvas(getTileset().getImageWidth(), getTileset().getImageHeight()));
		setToolbar(new ToolBar());
		
		// Set buttons
		setBtnPencil(new ToggleButton());
		setBtnSave(new Button());
		getBtnPencil().setGraphic(new ImageView(getClass().getResource("/icon_pencil_16.png").toExternalForm()));
		getBtnSave().setGraphic(new ImageView(getClass().getResource("/icon_save_16.png").toExternalForm()));
		getToolbar().getItems().addAll( getBtnSave(), getBtnPencil());
		
		// Add border
		GraphicsContext gc = getCanvas().getGraphicsContext2D();
		gc.setStroke(Color.RED);
		//gc.setFill(Color.BLUEVIOLET);
		gc.strokeRect(0, 0, getCanvas().getWidth(), getCanvas().getHeight());
		
		// Img
		img = new WritableImage(getTileset().getImageWidth(), getTileset().getImageHeight());
		tmp = new PixelatedImageView();
		tmp.setImage(img);
		tmp.setPreserveRatio(true);
		tmp.setPickOnBounds(true);
		PixelWriter w = img.getPixelWriter();
		for (int x = 0; x < 256; ++x) {
			w.setColor(x, 0, Color.RED);
			w.setColor(x, 255, Color.RED);
		}
		for (int y = 0; y < 256; ++y) {
			w.setColor(0, y, Color.RED);
			w.setColor(255, y, Color.RED);
		}
		setCenter(tmp);
		//setCenter(getCanvas());
		setTop(getToolbar());
	}
	
//	private void drawLine(int x, int y, int dx, int dy, Color color) {
//		
//	}
	
	public void buildActions() {
		getBtnSave().setOnAction(e -> {
			saveImage();
		});
		
		/*
		zoomProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable arg0) {
                tmp.setFitWidth(zoomProperty.get() * 4);
                tmp.setFitHeight(zoomProperty.get() * 3);
            }
        });
		
		setOnScroll(e -> {
			if (e.getDeltaY() > 0) {
                zoomProperty.set(zoomProperty.get() * 1.1);
            } else if (e.getDeltaY() < 0) {
                zoomProperty.set(zoomProperty.get() / 1.1);
            }
		});
		*/
		
//		setOnScroll(e -> {
//			double scale = 0.1;
//			if (e.getDeltaY() < 0)
//				scale = scale * -1;
//			getCanvas().setScaleX(getCanvas().getScaleX() +scale);
//			getCanvas().setScaleY(getCanvas().getScaleY() +scale);
//		});
		
		/*
		getCanvas().setOnMousePressed(e -> {
			PixelWriter w = getCanvas().getGraphicsContext2D().getPixelWriter();
			w.setColor((int)e.getX(), (int)e.getY(), Color.RED);
		});
		*/
		
		setOnScroll(e -> {
		double scale = 0.1;
		if (e.getDeltaY() < 0)
			scale = scale * -1;
		tmp.setScaleX(getCanvas().getScaleX() +scale);
		tmp.setScaleY(getCanvas().getScaleY() +scale);
	});
		
		tmp.setOnMousePressed(e -> {
			PixelWriter w = img.getPixelWriter();
			w.setColor((int)e.getX(), (int)e.getY(), Color.RED);
			System.out.println("Pressed: "+e.getX()+" "+e.getY());
		});
	}
	
	public void saveImage() {
		SnapshotParameters sp = new SnapshotParameters();
	    sp.setFill(Color.TRANSPARENT);
	    getCanvas().setScaleX(1);
	    getCanvas().setScaleY(1);
	    WritableImage wi = new WritableImage(getTileset().getImageWidth(),getTileset().getImageHeight());
		WritableImage img = getCanvas().snapshot(sp, wi);
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(img,null),"png",new File("storage/img.png"));
			Utils.info("Tileset saved!");
		} catch (IOException e) {
			Utils.error("Cannot write image to file", e);
		}
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

	public ToolBar getToolbar() {
		return toolbar;
	}

	public void setToolbar(ToolBar toolbar) {
		this.toolbar = toolbar;
	}

	public ToggleButton getBtnPencil() {
		return btnPencil;
	}

	public void setBtnPencil(ToggleButton btnPencil) {
		this.btnPencil = btnPencil;
	}

	public Button getBtnSave() {
		return btnSave;
	}

	public void setBtnSave(Button btnSave) {
		this.btnSave = btnSave;
	}

	public double getCurrentScale() {
		return currentScale;
	}

	public void setCurrentScale(double currentScale) {
		this.currentScale = currentScale;
	}

	
}

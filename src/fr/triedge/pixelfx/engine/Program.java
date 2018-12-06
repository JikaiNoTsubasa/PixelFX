package fr.triedge.pixelfx.engine;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import fr.triedge.pixelfx.model.OpenableItem;
import fr.triedge.pixelfx.model.Project;
import fr.triedge.pixelfx.model.Tileset;
import fr.triedge.pixelfx.ui.MainWindow;
import fr.triedge.pixelfx.utils.Utils;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.stage.Stage;

public class Program extends Application{
	
	private MainWindow mainWindow;
	private ArrayList<Project> projects = new ArrayList<>();
	
	public void initProgram() {
		// Load config
		try {
			Utils.config.load(new FileInputStream(new File(Utils.CONF_LOCATION)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage stage) throws Exception {
		initProgram();
		buildUI(stage);
		buildActions();
	}
	@Override
	public void stop() {
		actionExit(null);
	}

	private void buildUI(Stage stage) {
		String title = "PixelFX";
		double width = Double.parseDouble(Utils.config.getProperty("pixelfx.window.width", "800"));
		double height = Double.parseDouble(Utils.config.getProperty("pixelfx.window.height", "600"));
		String theme = Utils.config.getProperty("pixelfx.window.theme", "/theme_dark.css");
		String icon = Utils.config.getProperty("pixelfx.window.icon", "/default_icon.png");
		setMainWindow(new MainWindow(this, stage, title, width, height, theme, icon));
		getMainWindow().buildUI();
		getMainWindow().getRoot().setBottom(Utils.STATUS);
		getMainWindow().show();
	}
	
	private void buildActions() {
		if (getMainWindow() == null)
			return;
		getMainWindow().getItemExit().setOnAction(e -> actionExit(e));
		getMainWindow().getItemOpenProject().setOnAction(e -> actionOpenProject(e));
		getMainWindow().getItemNewProject().setOnAction(e -> actionNewProject(e));
	}

	private void actionNewProject(ActionEvent e) {
		
	}

	private void actionOpenProject(ActionEvent e) {
		File file = Utils.openFile(
				getMainWindow().getStage(),
				Utils.config.getProperty("pixelfx.workspace.location", "storage"),
				"PixelFX Project (.pixelfx)",
				"*.pixelfx");
		if (file == null || !file.exists()) {
			return;
		}
		try {
			Project prj = Utils.loadXml(Project.class, file);
			getProjects().add(prj);
			importProjectToTree(prj);
			Utils.updateStatus("Loaded project: "+prj.getName());
		} catch (JAXBException e1) {
			Utils.error("Cannot import project", e1);
		}
	}
	
	private void actionExit(ActionEvent e) {
		// Save config
		if (getMainWindow() != null) {
			Utils.config.setProperty("pixelfx.window.width", String.valueOf(getMainWindow().getStage().getScene().getWidth()));
			Utils.config.setProperty("pixelfx.window.height", String.valueOf(getMainWindow().getStage().getScene().getHeight()));
			Utils.config.setProperty("pixelfx.window.theme", getMainWindow().getTheme());
		}
		Utils.saveConfig();
		System.exit(0);
	}

	private void importProjectToTree(Project prj) {
		TreeItem<OpenableItem> root = getMainWindow().getTreeRoot();
		TreeItem<OpenableItem> itemPrj = new TreeItem<OpenableItem>(prj);
		itemPrj.setExpanded(true);
		root.getChildren().add(itemPrj);
		for (Tileset ts : prj.getTilesets()) {
			TreeItem<OpenableItem> itemTS = new TreeItem<OpenableItem>(ts);
			itemPrj.getChildren().add(itemTS);
			itemTS.setExpanded(true);
		}
	}
	
	public void openTab(String title ,Node node) {
		for (Tab t : getMainWindow().getTabPane().getTabs()) {
			if (t.getText().equalsIgnoreCase(title))
				return;
			
		}
		Tab tab = new Tab(title, node);
		getMainWindow().getTabPane().getTabs().add(tab);
		getMainWindow().getTabPane().getSelectionModel().select(tab);
	}
	
	public void eventOpenProjectNode(Project project) {
		// TODO Auto-generated method stub
		
	}
	
	public void eventOpenTilsetNode(Tileset tileset) {
		TilesetEditor editor = new TilesetEditor(tileset);
		editor.buildUI();
		editor.buildActions();
		openTab(tileset.getName(), editor);
	}

	public MainWindow getMainWindow() {
		return mainWindow;
	}

	public void setMainWindow(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public ArrayList<Project> getProjects() {
		return projects;
	}

	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}


}

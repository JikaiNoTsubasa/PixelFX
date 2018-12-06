package fr.triedge.pixelfx.ui;

import fr.triedge.pixelfx.engine.Program;
import fr.triedge.pixelfx.model.OpenableItem;
import fr.triedge.pixelfx.model.Project;
import fr.triedge.pixelfx.model.RootTree;
import fr.triedge.pixelfx.model.Tileset;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainWindow {

	private Stage stage;
	private Scene scene;
	private Program program;
	private String theme, icon;
	private BorderPane root;
	private double width, height;
	private SplitPane horPane, verPane;
	private TabPane tabPane;
	private TreeView<OpenableItem> treeProject;
	private TreeItem<OpenableItem> treeRoot;
	private ContextMenu treeMenuProject, treeMenuTileset;
	
	private MenuBar menubar;
	private Menu menuFile;
	private MenuItem itemExit, itemNewProject, itemOpenProject, itemContextNewTileset, itemContextRenameTileset, itemContextRenameProject;
	
	public MainWindow(Program program, Stage stage, String title, double width, double height, String theme, String icon) {
		setStage(stage);
		stage.setTitle(title);
		setWidth(width);
		setHeight(height);
		setIcon(icon);
		setTheme(theme);
		setProgram(program);
	}
	
	public void buildUI() {
		// Set Window
		setRoot(new BorderPane());
		setScene(new Scene(getRoot(), getWidth(), getHeight()));
		getStage().setScene(getScene());
		if (getIcon() != null)
			getStage().getIcons().add(new Image(getClass().getResourceAsStream(icon)));
		if (getTheme() != null)
			getRoot().getStylesheets().add(theme);
		
		// Set Content
		setTreeRoot(new TreeItem<OpenableItem>(new RootTree()));
		treeRoot.setExpanded(true);
		setTreeProject(new TreeView<>(treeRoot));
		getTreeProject().setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if(event.getClickCount() == 2)
		        {
					TreeItem<OpenableItem> item = getTreeProject().getSelectionModel().getSelectedItem();
					if (item != null)
						item.getValue().open(getProgram());
					
		        }
			}
		});
		
		// Set Context Menu
		setItemContextNewTileset(new MenuItem("New Tileset"));
		setItemContextRenameProject(new MenuItem("Rename Project"));
		setItemContextRenameTileset(new MenuItem("Rename Tileset"));
		setTreeMenuProject(new ContextMenu());
		setTreeMenuTileset(new ContextMenu());
		getTreeMenuProject().getItems().addAll(getItemContextRenameProject(),getItemContextNewTileset());
		getTreeMenuProject().setAutoHide(true);
		getTreeMenuProject().setHideOnEscape(true);
		getTreeMenuTileset().getItems().addAll(getItemContextRenameTileset());
		getTreeMenuTileset().setAutoHide(true);
		getTreeMenuTileset().setHideOnEscape(true);
		
		getTreeProject().setOnContextMenuRequested(e -> {
			OpenableItem selected = getTreeProject().getSelectionModel().getSelectedItem().getValue();
			if (selected instanceof Project) {
				getTreeMenuProject().show(getTreeProject(),e.getScreenX(), e.getScreenY());
			}else if (selected instanceof Tileset) {
				getTreeMenuTileset().show(getTreeProject(),e.getScreenX(), e.getScreenY());
			}
		});
		
		setHorPane(new SplitPane());
		setVerPane(new SplitPane());
		setTabPane(new TabPane());
		getHorPane().setOrientation(Orientation.HORIZONTAL);
		getVerPane().setOrientation(Orientation.VERTICAL);
		
		// Set MenuItems
		setItemExit(new MenuItem("Exit"));
		setItemNewProject(new MenuItem("New Project"));
		setItemOpenProject(new MenuItem("Open Project..."));
		
		getItemExit().setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
		
		// Set Menus
		setMenuFile(new Menu("File"));
		getMenuFile().getItems().addAll(
				getItemNewProject(),
				getItemOpenProject(),
				new SeparatorMenuItem(),
				getItemExit());
		
		// Set MenuBar
		setMenubar(new MenuBar());
		getMenubar().getMenus().addAll(getMenuFile());
		
		// Populate Content
		getVerPane().getItems().add(getTabPane());
		getHorPane().getItems().add(getTreeProject());
		getHorPane().getItems().add(getVerPane());
		getRoot().setCenter(getHorPane());
		getRoot().setTop(getMenubar());
		
		getHorPane().setDividerPositions(0.2);
		getVerPane().setDividerPositions(0.7);
	}
	
	public void show() {
		getStage().show();
	}
	
	public Stage getStage() {
		return stage;
	}
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	public Scene getScene() {
		return scene;
	}
	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public BorderPane getRoot() {
		return root;
	}

	public void setRoot(BorderPane root) {
		this.root = root;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public SplitPane getVerPane() {
		return verPane;
	}

	public void setVerPane(SplitPane verPane) {
		this.verPane = verPane;
	}

	public SplitPane getHorPane() {
		return horPane;
	}

	public void setHorPane(SplitPane horPane) {
		this.horPane = horPane;
	}

	public TreeView<OpenableItem> getTreeProject() {
		return treeProject;
	}

	public void setTreeProject(TreeView<OpenableItem> treeProject) {
		this.treeProject = treeProject;
	}

	public Menu getMenuFile() {
		return menuFile;
	}

	public void setMenuFile(Menu menuFile) {
		this.menuFile = menuFile;
	}

	public MenuItem getItemNewProject() {
		return itemNewProject;
	}

	public void setItemNewProject(MenuItem itemNewProject) {
		this.itemNewProject = itemNewProject;
	}

	public MenuItem getItemExit() {
		return itemExit;
	}

	public void setItemExit(MenuItem itemExit) {
		this.itemExit = itemExit;
	}

	public MenuItem getItemOpenProject() {
		return itemOpenProject;
	}

	public void setItemOpenProject(MenuItem itemOpenProject) {
		this.itemOpenProject = itemOpenProject;
	}

	public MenuBar getMenubar() {
		return menubar;
	}

	public void setMenubar(MenuBar menubar) {
		this.menubar = menubar;
	}

	public TabPane getTabPane() {
		return tabPane;
	}

	public void setTabPane(TabPane tabPane) {
		this.tabPane = tabPane;
	}

	public TreeItem<OpenableItem> getTreeRoot() {
		return treeRoot;
	}

	public void setTreeRoot(TreeItem<OpenableItem> treeRoot) {
		this.treeRoot = treeRoot;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public MenuItem getItemContextNewTileset() {
		return itemContextNewTileset;
	}

	public void setItemContextNewTileset(MenuItem itemContextNewTileset) {
		this.itemContextNewTileset = itemContextNewTileset;
	}

	public ContextMenu getTreeMenuProject() {
		return treeMenuProject;
	}

	public void setTreeMenuProject(ContextMenu treeMenuProject) {
		this.treeMenuProject = treeMenuProject;
	}

	public ContextMenu getTreeMenuTileset() {
		return treeMenuTileset;
	}

	public void setTreeMenuTileset(ContextMenu treeMenuTileset) {
		this.treeMenuTileset = treeMenuTileset;
	}

	public MenuItem getItemContextRenameTileset() {
		return itemContextRenameTileset;
	}

	public void setItemContextRenameTileset(MenuItem itemContextRenameTileset) {
		this.itemContextRenameTileset = itemContextRenameTileset;
	}

	public MenuItem getItemContextRenameProject() {
		return itemContextRenameProject;
	}

	public void setItemContextRenameProject(MenuItem itemContextRenameProject) {
		this.itemContextRenameProject = itemContextRenameProject;
	}
}

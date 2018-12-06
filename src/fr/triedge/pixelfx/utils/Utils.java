package fr.triedge.pixelfx.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Optional;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.controlsfx.control.StatusBar;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Utils {

	public static StatusBar STATUS = new StatusBar();
	public static Properties config = new Properties();
	public static final String CONF_LOCATION				= "conf/pixelfx.properties";
	
	public static <T> void storeXml(T el, File file) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(el.getClass());
		Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
		jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxbMarshaller.marshal(el, file);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T loadXml(Class<?> clazz, File file) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		return (T) jaxbUnmarshaller.unmarshal(file);
	}
	
	public static File openFile(Stage stage, String workspace) {
		FileChooser chooser = new FileChooser();
		if (workspace != null)
			chooser.setInitialDirectory(new File(workspace));
		File file = chooser.showOpenDialog(stage);
		return file;
	}
	
	public static File openFile(Stage stage, String workspace,String extDesc, String ext) {
		FileChooser chooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(extDesc, ext);
		chooser.getExtensionFilters().add(extFilter);
		if (workspace != null)
			chooser.setInitialDirectory(new File(workspace));
		File file = chooser.showOpenDialog(stage);
		return file;
	}
	
	public static void updateStatus(String status, int seconds) {
		STATUS.setText(status);
		new Timeline(new KeyFrame(Duration.seconds(seconds), e -> STATUS.setText("Ready"))).play();;
	}
	
	public static void updateStatus(String status) {
		updateStatus(status, 2);
	}
	
	public static void saveConfig() {
		try {
			config.store(new FileOutputStream(new File(CONF_LOCATION)), "Stored on "+new Date());
		} catch (IOException e) {
			Utils.error("Error storing config file", e);
		}
	}

	public static void info(String content) {
		Alert alert = new Alert(AlertType.INFORMATION, content, ButtonType.OK);
		alert.setTitle("INFO");
		alert.setHeaderText("");
		alert.show();
	}
	
	public static void error(String content) {
		Alert alert = new Alert(AlertType.ERROR, content, ButtonType.OK);
		alert.setTitle("ERROR");
		alert.setHeaderText("");
		alert.show();
	}
	
	public static String dialogText(String title, String content) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle(title);
		dialog.setHeaderText("Input Dialog");
		dialog.setContentText(content);

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    return result.get();
		}else {
			return null;
		}
		
	}
	
	public static void error(String content, Exception ex) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ERROR");
		alert.setHeaderText("");
		if (content != null)
			alert.setContentText(content);

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
}

package application;

import java.io.File;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import imageprocessing.ConnectedComponentImage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controller for the Landing.fxml
 * 
 * @author Thai Kha Le
 *
 */
public class LandingController {
	private ConnectedComponentImage processor;
	private Main app;
	private File file;
	@FXML
	private Text fileLocationText, status, componentCount, dimension, processingTime;
	@FXML
	private ChoiceBox thresholdMode;
	@FXML
	private Button browse, start, originalPic, binarizedPic, randomColorPic, highlight, countButton;
	private Button[] buttons;
	private boolean inRun = false;

	/**
	 * To refer to the Main object if needed
	 * 
	 * @param app Main object
	 */
	public void setMainApp(Main app) {
		this.app = app;
	}

	/**
	 * Setup the fields, automatically invoked by JavaFX
	 */
	@FXML
	public void initialize() {
		buttons = new Button[] { start, originalPic, binarizedPic, randomColorPic, highlight, countButton };
		changeButtons(0, button -> button.setDisable(true));
		thresholdMode.getItems().addAll("Mode 1 - Brighter is foreground", "Mode 2 - Darker is foreground");
		thresholdMode.getSelectionModel().selectFirst();
		thresholdMode.setDisable(true);

		browse.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				final FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open your image");
				File chosenFile = fileChooser.showOpenDialog(new Stage());
				if (chosenFile != null) {
					if (!inRun) {
						dimension.setText("");
						thresholdMode.setDisable(false);
						start.setDisable(false);
						status.setText("Choose mode and press start");
					} else if (!file.equals(chosenFile)) {
						changeButtons(1, b -> b.setDisable(true));
						componentCount.setText("");
						dimension.setText("");
						processingTime.setText("");
						status.setText("File changed, press start to continue");
						inRun = false;
					}
					file = chosenFile;
					fileLocationText.setText(file.getAbsolutePath());
				} else {
					status.setText("Error browsing file");
				}
			}
		});

		thresholdMode.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (!oldValue.equals(newValue) && inRun) {
					changeButtons(1, b -> b.setDisable(true));
					componentCount.setText("");
					processingTime.setText("");
					status.setText("Mode changed, press start to continue");
					inRun = false;
				}
			}
		});

		start.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent arg0) {
				
				int choice = thresholdMode.getSelectionModel().getSelectedIndex();
				try {
					processor = new ConnectedComponentImage(file.getAbsolutePath(),
							choice == 0 ? 0 : 1);
					dimension.setText("Width: " + processor.getWidth() + " Height: " + processor.getHeight());
					status.setText("File loadded sucessfully. \nNote: application "
							+ "will shutdown if the image processing time takes too long \n(might be due to"
							+ " the chosen mode, try changing it if termination occurs)");
					changeButtons(1, b -> b.setDisable(false));
					inRun = true;
				} catch (Exception e) {
					status.setText("Error occurred, please browse your picture again");
				}
			}
		});

		originalPic.setOnAction(e -> processor.getPicture().show());
		countButton.setOnAction(e -> doImageTask(p -> componentCount.setText(p.countComponents() + ""), false));
		binarizedPic.setOnAction(e -> doImageTask(p -> p.binaryComponentImage().show(), true));
		randomColorPic.setOnAction(e -> doImageTask(p -> p.colourComponentImage().show(), true));
		highlight.setOnAction(e -> doImageTask(p -> p.identifyComponentImage().show(), true));
	}

	/**
	 * Apply a function to the buttons in the buttons array
	 * 
	 * @param start
	 *            from which button
	 * @param toDo
	 *            the function to perform
	 */
	private void changeButtons(int start, Consumer<Button> toDo) {
		for (int i = start; i < buttons.length; i++) {
			toDo.accept(buttons[i]);
		}
	}

	/**
	 * Apply a function to the ConnectedComponentImage processor
	 * 
	 * @param consume
	 *            the function to perform
	 * @param setComponentCount
	 *            is true if componentCount field needs to be set
	 */
	private void doImageTask(ConsumerException<ConnectedComponentImage> consume, boolean setComponentCount) {
		try {
			status.setText("Processing...");
			if (setComponentCount)
				componentCount.setText(processor.countComponents() + "");
			consume.doTask(processor);
			processingTime.setText("Processed in " + processor.getProcessingTime() + " seconds");
			status.setText("Done");
		} catch (Exception e) {
			if (e instanceof TimeoutException) {
				System.exit(-1);
			} else {
				e.printStackTrace();
				status.setText("Error occurred");
			}
		}
	}
}
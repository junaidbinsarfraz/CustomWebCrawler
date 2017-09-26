package googlewebcrawler.main;
	
import googlewebcrawler.controller.MainController;
import googlewebcrawler.util.Constants;
import googlewebcrawler.util.Util;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The class Main is use to make the javaFX gui and test MainController
 * 
 * @author Junaid Sarfraz
 */
public class Main extends Application {
	
	MainController mainController = new MainController();
	Text status = new Text();
	TextField keywordTextField = new TextField();
	TextField keywordsTextField = new TextField();
	Button btn = new Button("Generate Report");
	ProgressIndicator progressIndicator = new ProgressIndicator();
	Text scenetitle = new Text("Welcome");
	Label keywordLabel = new Label("Keyword:");
	Label keywordsLabel = new Label("Comma seperated keys to be searched within HTML page:");
	GridPane grid = new GridPane();
	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			primaryStage.setTitle("Google Report Generator");
			
	        grid.setAlignment(Pos.CENTER);
	        grid.setHgap(10);
	        grid.setVgap(10);
	        grid.setPadding(new Insets(25, 25, 25, 25));
	        
	        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
	        grid.add(scenetitle, 0, 0, 2, 1);

	        grid.add(keywordLabel, 0, 1, 2, 1);

	        keywordTextField.setPrefWidth(250);
	        keywordTextField.setMinWidth(250);
	        grid.add(keywordTextField, 0, 2, 2, 1);

	        grid.add(keywordsLabel, 0, 3, 2, 1);

	        keywordsTextField.setPrefWidth(250);
	        keywordsTextField.setMinWidth(250);
	        grid.add(keywordsTextField, 0, 4, 2, 1);

	        HBox hbBtn = new HBox(10);
	        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
	        hbBtn.getChildren().add(btn);
	        grid.add(hbBtn, 0, 5, 2, 1);
	        
	        progressIndicator.setVisible(Boolean.FALSE);

	        grid.add(status, 0, 6, 1, 1);
	        grid.add(progressIndicator, 1, 6, 1, 1);
	        
			btn.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent e) {

					status.setText("");
					progressIndicator.setVisible(Boolean.FALSE);
					
					updateStatus(null); // To avoid UI redundancy

					if (Util.isNullOrEmpty(keywordTextField.getText()) || Util.isNullOrEmpty(keywordsTextField.getText())) {
						status.setFill(Color.FIREBRICK);
						status.setText("Both fields are required");

						return;
					}

					status.setFill(Color.DARKBLUE);
					status.setText("Processing ...");

					progressIndicator.setVisible(Boolean.TRUE);
					progressIndicator.setProgress(Constants.PROCESSING);
					
					updateStatus(null);

					new Thread() {
						public void run() {
							String reportStatus = mainController.makeReportUsingCSE(keywordTextField.getText(), keywordsTextField.getText());

							status.setText("");
							
							updateStatus(null); // To avoid UI redundancy
							
							if (Constants.SUCCESS.equals(reportStatus)) {
								status.setFill(Color.GREEN);
								status.setText("Report successfully generated");
							} else {
								status.setFill(Color.FIREBRICK);
								status.setText(reportStatus);
							}
							
							progressIndicator.setVisible(Boolean.FALSE);
							
							updateStatus(null);
						}
					}.start();;
					
				}
			});

	        Scene scene = new Scene(grid, 600, 275);
	        primaryStage.setScene(scene);
	        primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateStatus(final Throwable t) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
			}
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}

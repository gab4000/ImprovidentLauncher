package fr.gab400.improvidentlauncher;

import fr.gab400.improvidentlauncher.panel.IPanel;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

public class PanelManager {
	
	private ImprovidentLauncher core;
	private final Stage stage;
	private GridPane layout;
	private final GridPane contentPane = new GridPane();
	
	private final int HEIGHT = 720;
	private final int WIDTH = HEIGHT * 16 / 9;
	
	public PanelManager(ImprovidentLauncher core, Stage stage) {
		this.core = core;
		this.stage = stage;
	}
	
	public void init() {
		this.stage.setTitle("ImprovidentLauncher");
		this.stage.setWidth(WIDTH);
		this.stage.setHeight(HEIGHT);
		this.stage.resizableProperty().set(false);
		this.stage.initStyle(StageStyle.UNIFIED);
		this.stage.centerOnScreen();
		
		this.layout = new GridPane();
		
		Scene scene = new Scene(this.layout);
		JMetro jMetro = new JMetro(Style.DARK);
		jMetro.setScene(scene);
		this.layout.getStyleClass().add(JMetroStyleClass.BACKGROUND);
		
		this.stage.setScene(scene);
		
		this.layout.add(this.contentPane, 0, 0);
		GridPane.setVgrow(this.contentPane, Priority.ALWAYS);
		GridPane.setHgrow(this.contentPane, Priority.ALWAYS);
		
		this.stage.show();
	}
	
	public void showPanel(IPanel panel) {
		this.contentPane.getChildren().clear();
		this.contentPane.getChildren().add(panel.getLayout());
		panel.init(this);
		panel.onShow();
	}
	
	public Stage getStage() {
		return stage;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	public int getWidth() {
		return WIDTH;
	}
}

package fr.gab400.improvidentlauncher;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

public class PanelManager {
	
	private ImprovidentLauncher core;
	private Stage stage;
	private GridPane layout;
	
	private int HEIGHT = 720;
	private int WIDTH = HEIGHT * 16 / 9;
	
	public PanelManager(ImprovidentLauncher core, Stage stage) {
		this.core = core;
		this.stage = stage;
	}
	
	public void init() {
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
		this.stage.show();
	}
}

package fr.gab400.improvidentlauncher.panels.pages;

import fr.gab400.improvidentlauncher.PanelManager;
import fr.gab400.improvidentlauncher.panels.App;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Label;
import jfxtras.styles.jmetro.MDL2IconFont;

public class Settings extends ContentPanel {
	
	GridPane boxPane = new GridPane();
	private App app;
	@Override
	public String getName() {
		return "Parametres";
	}
	
	public Settings(App app) {
		this.app = app;
	}
	
	@Override
	public void init(PanelManager panelManager) {
		super.init(panelManager);
		
		ColumnConstraints columnConstraints = new ColumnConstraints();
		columnConstraints.setHalignment(HPos.CENTER);
		columnConstraints.setPrefWidth(panelManager.getWidth());
		this.layout.getColumnConstraints().addAll(columnConstraints, new ColumnConstraints());
		setCanTakeAllSize(boxPane);
		this.layout.add(boxPane, 0, 1);
		
		Button back = new Button();
		back.setGraphic(new MDL2IconFont("\uE72B"));
		setCenterV(back);
		setTop(back);
		setLeft(back);
		back.setTranslateX(15);
		back.setTranslateY(-32);
		back.setPrefHeight(40);
		back.setPrefWidth(40);
		back.setOnAction(e -> {
			app.setPage(new Home(app));
		});
		boxPane.getChildren().add(back);
		
		// Title
		Label title = new Label(getName());
		title.setFont(Font.font("Roboto", FontWeight.BOLD, FontPosture.REGULAR, 30f));
		setCanTakeAllSize(title);
		setTop(title);
		title.setTranslateX(70);
		title.setTranslateY(-30);
		boxPane.getChildren().add(title);
	}
}

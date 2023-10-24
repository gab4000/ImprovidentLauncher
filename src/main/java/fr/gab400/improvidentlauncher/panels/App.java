package fr.gab400.improvidentlauncher.panels;

import fr.gab400.improvidentlauncher.PanelManager;
import fr.gab400.improvidentlauncher.panel.Panel;
import fr.gab400.improvidentlauncher.panels.pages.ContentPanel;
import fr.gab400.improvidentlauncher.panels.pages.Home;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import jfxtras.styles.jmetro.MDL2IconFont;

public class App extends Panel {
	
	GridPane topMenu = new GridPane(), navContent = new GridPane();
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public void init(PanelManager panelManager) {
		super.init(panelManager);
		
		setCanTakeAllSize(this.layout);
		
		RowConstraints rowConstraints = new RowConstraints();
		rowConstraints.setValignment(VPos.TOP);
		rowConstraints.setMaxHeight(50);
		rowConstraints.setMinHeight(50);
		this.layout.getRowConstraints().addAll(rowConstraints, new RowConstraints());
		this.layout.add(topMenu, 0, 0);
		
		// TopMenu
		setCanTakeAllSize(this.layout);
		setTop(topMenu);
		setCenterH(topMenu);
		setCenterV(topMenu);
		
		// NavContent
		this.layout.add(navContent, 0, 1);
		setLeft(navContent);
		setCenterH(navContent);
		setCenterV(navContent);
	}
	
	@Override
	public void onShow() {
		super.onShow();
		setPage(new Home(this));
	}
	
	public void setPage(ContentPanel panel) {
		this.navContent.getChildren().clear();
		if (panel != null) {
			this.navContent.getChildren().add(panel.getLayout());
			panel.init(this.panelManager);
			panel.onShow();
		}
	}
}

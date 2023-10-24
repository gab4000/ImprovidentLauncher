package fr.gab400.improvidentlauncher.panel;

import fr.gab400.improvidentlauncher.PanelManager;
import javafx.scene.layout.GridPane;

public interface IPanel {
	void init(PanelManager panelManager);
	GridPane getLayout();
	void onShow();
	String getName();
}

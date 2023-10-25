package fr.gab400.improvidentlauncher.panel;

import fr.flowarg.flowlogger.ILogger;
import fr.gab400.improvidentlauncher.ImprovidentLauncher;
import fr.gab400.improvidentlauncher.PanelManager;
import javafx.animation.FadeTransition;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public abstract class Panel implements IPanel, IMovable, ITakePlace {
	
	protected final ILogger logger;
	protected GridPane layout = new GridPane();
	protected PanelManager panelManager;
	
	protected Panel() {
		this.logger = ImprovidentLauncher.getInstance().getLogger();
	}
	
	@Override
	public void init(PanelManager panelManager) {
		this.panelManager = panelManager;
		setCanTakeAllSize(this.layout);
	}
	
	@Override
	public GridPane getLayout() {
		return layout;
	}
	
	@Override
	public void onShow() {
		FadeTransition fadeTransition = new FadeTransition(Duration.millis(250), this.layout);
		fadeTransition.setFromValue(0);
		fadeTransition.setToValue(1);
		fadeTransition.setAutoReverse(true);
		fadeTransition.play();
	}
	
	@Override
	public abstract String getName();
	
	@Override
	public void setLeft(Node node) {
		GridPane.setHalignment(node, HPos.LEFT);
	}
	
	@Override
	public void setRight(Node node) {
		GridPane.setHalignment(node, HPos.RIGHT);
	}
	
	@Override
	public void setTop(Node node) {
		GridPane.setValignment(node, VPos.TOP);
	}
	
	@Override
	public void setBottom(Node node) {
		GridPane.setValignment(node, VPos.BOTTOM);
	}
	
	@Override
	public void setCenterH(Node node) {
		GridPane.setHalignment(node, HPos.CENTER);
	}
	
	@Override
	public void setCenterV(Node node) {
		GridPane.setValignment(node, VPos.CENTER);
	}
	
	@Override
	public void setBaseline(Node node) {
		GridPane.setValignment(node, VPos.BASELINE);
		
	}
}
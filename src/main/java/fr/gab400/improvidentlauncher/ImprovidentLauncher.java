package fr.gab400.improvidentlauncher;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.gab400.improvidentlauncher.utils.Helpers;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

public class ImprovidentLauncher extends Application {
	
	private PanelManager panelManager;
	private static ImprovidentLauncher instance;
	private final ILogger logger;
	private File launcherDir = Helpers.generateGamePath("Improvident");
	
	public ImprovidentLauncher() {
		instance = this;
		this.logger = new Logger("[ImprovidentLauncher]", new File(this.launcherDir, "launcher.log").toPath());
		this.launcherDir.mkdirs();
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.logger.info("Starting Improvident Launcher...");
		this.panelManager = new PanelManager(this, stage);
		this.panelManager.init();
	}
	
	public static ImprovidentLauncher getInstance() {
		return instance;
	}
	
	public ILogger getLogger() {
		return logger;
	}
}

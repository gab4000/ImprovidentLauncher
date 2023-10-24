package fr.gab400.improvidentlauncher;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.gab400.improvidentlauncher.panels.App;
import fr.gab400.improvidentlauncher.utils.Helpers;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class ImprovidentLauncher extends Application {
	
	private static ImprovidentLauncher instance;
	private final ILogger logger;
	private final File launcherDir = Helpers.generateGamePath(".improvident");
	
	public ImprovidentLauncher() {
		instance = this;
		this.logger = new Logger("[ImprovidentLauncher]", new File(this.launcherDir, "launcher.log").toPath());
		this.launcherDir.mkdirs();
	}
	
	@Override
	public void start(Stage stage) {
		this.logger.info("Starting Improvident Launcher...");
		PanelManager panelManager = new PanelManager(this, stage);
		panelManager.init();
		
		panelManager.showPanel(new App());
	}
	
	@Override
	public void stop() {
		Platform.exit();
		System.exit(0);
	}
	
	public static ImprovidentLauncher getInstance() {
		return instance;
	}
	
	public ILogger getLogger() {
		return logger;
	}
	
	public Path getLauncherDir() {
		return Path.of(launcherDir.getAbsolutePath());
	}
}

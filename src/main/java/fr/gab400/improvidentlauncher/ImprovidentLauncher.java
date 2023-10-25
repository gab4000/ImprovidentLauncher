package fr.gab400.improvidentlauncher;

import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.gab400.improvidentlauncher.panels.App;
import fr.gab400.improvidentlauncher.panels.Login;
import fr.gab400.improvidentlauncher.utils.Helpers;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;

public class ImprovidentLauncher extends Application {
	
	private static ImprovidentLauncher instance;
	private final ILogger logger;
	private final File launcherDir = Helpers.generateGamePath(".improvident");
	private final Saver saver;
	private AuthInfos authInfos = null;
	private PanelManager panelManager;
	
	public ImprovidentLauncher() {
		instance = this;
		this.logger = new Logger("[ImprovidentLauncher]", new File(this.launcherDir, "launcher.log").toPath());
		if (! this.launcherDir.exists()) {
			if (! this.launcherDir.mkdir()) {
				this.logger.err("Unable to create launcher folder");
			}
		}
		
		saver = new Saver(this.launcherDir.toPath().resolve("config.properties"));
		saver.load();
	}
	
	@Override
	public void start(Stage stage) {
		this.logger.info("Starting Improvident Launcher...");
		this.panelManager = new PanelManager(this, stage);
		this.panelManager.init();
		
		if (this.isUserAlreadyLoggedIn()) {
			this.panelManager.showPanel(new App());
		} else {
			this.panelManager.showPanel(new Login());
		}
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
	
	public boolean isUserAlreadyLoggedIn() {
		if (saver.get("msAccessToken") != null && saver.get("msRefreshToken") != null) {
			try {
				MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
				MicrosoftAuthResult response = authenticator.loginWithRefreshToken(saver.get("msRefreshToken"));
				
				saver.set("msAccessToken", response.getAccessToken());
				saver.set("msRefreshToken", response.getRefreshToken());
				saver.save();
				
				this.setAuthInfos(new AuthInfos(response.getProfile().getName(), response.getAccessToken(), response.getProfile().getId()));
				
				return true;
			} catch (MicrosoftAuthenticationException e) {
				saver.remove("msAccessToken");
				saver.remove("msRefreshToken");
				saver.save();
				this.logger.err(e.getMessage());
				return false;
			}
		}
		return false;
	}
	
	public AuthInfos getAuthInfos() {
		return authInfos;
	}
	
	public void setAuthInfos(AuthInfos authInfos) {
		this.authInfos = authInfos;
	}
	
	public Saver getSaver() {
		return saver;
	}
	public void hideWindow() {
		this.panelManager.getStage().hide();
	}
}

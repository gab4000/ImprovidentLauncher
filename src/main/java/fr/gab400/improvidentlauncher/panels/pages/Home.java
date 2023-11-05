package fr.gab400.improvidentlauncher.panels.pages;

import fr.flowarg.flowupdater.FlowUpdater;
import fr.flowarg.flowupdater.download.DownloadList;
import fr.flowarg.flowupdater.download.IProgressCallback;
import fr.flowarg.flowupdater.download.Step;
import fr.flowarg.flowupdater.versions.VanillaVersion;
import fr.gab400.improvidentlauncher.ImprovidentLauncher;
import fr.gab400.improvidentlauncher.Main;
import fr.gab400.improvidentlauncher.PanelManager;
import fr.gab400.improvidentlauncher.panels.App;
import fr.gab400.improvidentlauncher.panels.Login;
import fr.gab400.improvidentlauncher.utils.Unzip;
import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.MDL2IconFont;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Objects;

public class Home extends ContentPanel {
	
	GridPane boxPane = new GridPane();
	private App app;
	
	private Saver saver = ImprovidentLauncher.getInstance().getSaver();
	
	ProgressBar progressBar = new ProgressBar();
	Label stepLabel = new Label();
	Label fileLabel = new Label();
	boolean isDownloading = false;
	
	@Override
	public String getName() {
		return "Accueil";
	}
	
	public Home(App app) {
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
		
		// Title
		Label title = new Label("Improvident Launcher");
		title.setFont(Font.font("Roboto", FontWeight.BOLD, FontPosture.REGULAR, 30f));
		setCanTakeAllSize(title);
		setTop(title);
		setLeft(title);
		title.setTranslateX(25);
		title.setTranslateY(-30);
		this.layout.getChildren().add(title);
		
		Button logout = new Button();
		logout.setGraphic(new MDL2IconFont("\uF3B1"));
		logout.setPrefHeight(40);
		logout.setPrefWidth(40);
		setCenterV(logout);
		setTop(logout);
		setRight(logout);
		logout.setTranslateX(-130);
		logout.setTranslateY(-32);
		logout.setOnAction(e -> {
			if (this.isDownloading()) {
				return;
			}
			saver.remove("msAccessToken");
			saver.remove("msRefreshToken");
			saver.save();
			ImprovidentLauncher.getInstance().setAuthInfos(null);
			this.panelManager.showPanel(new Login());
		});
		
		Button settings = new Button("Parametres");
		setCenterV(settings);
		setTop(settings);
		setRight(settings);
		settings.setTranslateX(-15);
		settings.setTranslateY(-32);
		settings.setPrefHeight(40);
		settings.setPrefWidth(100);
		settings.setOnMouseClicked(event -> {
			this.app.setPage(new Settings(app));
		});
		
		this.layout.getChildren().addAll(logout, settings);
		
		setCenterH(progressBar);
		setBottom(progressBar);
		progressBar.setTranslateY(500);
		setCanTakeAllSize(progressBar);
		progressBar.setMaxWidth(1000);
		
		setCenterH(stepLabel);
		setBottom(stepLabel);
		stepLabel.setTranslateY(450);
		setCanTakeAllSize(stepLabel);
		stepLabel.setFont(new Font(20));
		
		setCenterH(fileLabel);
		setBottom(fileLabel);
		fileLabel.setTranslateY(475);
		setCanTakeAllSize(fileLabel);
		fileLabel.setFont(new Font(20));
		
		this.showPlayButton();
	}
	
	private void showPlayButton() {
		boxPane.getChildren().clear();
		Button play = new Button("Travailler/Jouer");
		play.setStyle("-fx-font-size: 15;-fx-background-color: #0078d7;");
		setCanTakeAllSize(play);
		setCenterH(play);
		setBottom(play);
		play.setTranslateY(500);
		play.setPrefWidth(200);
		play.setPrefHeight(50);
		play.setOnAction(e -> this.play());
		boxPane.getChildren().add(play);
	}
	
	private void play() {
		isDownloading = true;
		boxPane.getChildren().clear();
		setProgress(0, 0);
		boxPane.getChildren().addAll(progressBar, stepLabel, fileLabel);
		
		Platform.runLater(() -> new Thread(this::update).start());
	}
	
	public void update() {
		IProgressCallback callback = new IProgressCallback() {
			private final DecimalFormat decimalFormat = new DecimalFormat("#.#");
			private String stepTxt = "";
			private String percentTxt = "0.0%";
			
			@Override
			public void step(Step step) {
				Platform.runLater(() -> {
					stepTxt = StepInfo.valueOf(step.name()).getDetails();
					setStatus(String.format("%s - %s", percentTxt, stepTxt));
					ImprovidentLauncher.getInstance().getLogger().info(StepInfo.valueOf(step.name()).getDetails());
				});
			}
			
			public void update(DownloadList.DownloadInfo info) {
				Platform.runLater(() -> {
					percentTxt = decimalFormat.format(info.getDownloadedBytes() * 100.d / info.getTotalToDownloadBytes()) + "%";
					setStatus(String.format("%s - %s", percentTxt, stepTxt));
					setProgress(info.getDownloadedBytes(), info.getTotalToDownloadBytes());
				});
			}
			
			@Override
			public void onFileDownloaded(Path path) {
				Platform.runLater(() -> {
					String p = path.toString();
					fileLabel.setText(p.replace(ImprovidentLauncher.getInstance().getLauncherDir().toFile().getAbsolutePath(), ""));
				});
			}
		};
		
		try {
			File money = new File(ImprovidentLauncher.getInstance().getLauncherDir() + "/money.txt");
			if (! money.exists()) {
				FileWriter fileWriter = new FileWriter(money);
				fileWriter.write("0");
				fileWriter.close();
			}
			
			if (ImprovidentLauncher.getInstance().getSaver().get("map-created") == null) {
                try {

                    InputStream resourceStream = Main.class.getResourceAsStream("/Minecraft World Map.zip");
                    if (resourceStream == null) {
                        throw new FileNotFoundException("La ressource Minecraft World Map.zip n'a pas été trouvée.");
                    }


                    File target = new File(ImprovidentLauncher.getInstance().getLauncherDir() + "/saves/Minecraft World Map.zip");


                    FileUtils.copyInputStreamToFile(resourceStream, target);


                    Unzip unzip = new Unzip();
                    unzip.unzip(target, Path.of(ImprovidentLauncher.getInstance().getLauncherDir() + "/saves"));


                    target.delete();
                    

                    saver.set("map-created", "true");
                    saver.save();
                } catch (IOException e) {
                    logger.err(e.getMessage());
                    e.printStackTrace();
                }

            }
			
			final VanillaVersion vanillaVersion = new VanillaVersion.VanillaVersionBuilder()
					.withName("1.12")
					.withMCP("https://kizilando.is-a-frontend.dev/mcp.json")
					.build();
			
			final FlowUpdater updater = new FlowUpdater.FlowUpdaterBuilder()
					.withVanillaVersion(vanillaVersion)
					.withLogger(ImprovidentLauncher.getInstance().getLogger())
					.withProgressCallback(callback)
					.build();
			
			updater.update(ImprovidentLauncher.getInstance().getLauncherDir());
			this.startGame(updater.getVanillaVersion().getName());
		} catch (Exception exception) {
			ImprovidentLauncher.getInstance().getLogger().err(exception.toString());
			exception.printStackTrace();
			Platform.runLater(() -> panelManager.getStage().show());
		}
	}
	
	public void startGame(String gameVersion) {
		try {
			GameInfos infos = new GameInfos("improvident", true, new GameVersion(gameVersion, GameType.V1_8_HIGHER), new GameTweak[]{});
			
			ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(infos, GameFolder.FLOW_UPDATER, new AuthInfos(ImprovidentLauncher.getInstance().getAuthInfos().getUsername(), ImprovidentLauncher.getInstance().getAuthInfos().getAccessToken(), ImprovidentLauncher.getInstance().getAuthInfos().getUuid()));
			profile.getVmArgs().add("-Xmx2G");
			ExternalLauncher launcher = new ExternalLauncher(profile);
			
			Platform.runLater(() -> panelManager.getStage().hide());
			
			Process p = launcher.launch();
			ImprovidentLauncher.getInstance().getLogger().info("Lancement en cours...");
			
			p.waitFor();
			
		} catch (Exception e) {
			e.printStackTrace();
			ImprovidentLauncher.getInstance().getLogger().err(e.toString());
		}
	}
	
	public void setStatus(String status) {
		this.stepLabel.setText(status);
	}
	
	private void setProgress(double current, double max) {
		this.progressBar.setProgress(current / max);
	}
	
	public boolean isDownloading() {
		return isDownloading;
	}
	
	public enum StepInfo {
		INTEGRATION("Préparation..."),
		MOD_PACK("Téléchergement du modpack..."),
		READ("Lecture du fichier JSON..."),
		DL_LIBS("Téléchargement des librairies..."),
		DL_ASSETS("Téléchargement des ressources..."),
		EXTRACT_NATIVES("Extraction des natives..."),
		MOD_LOADER("Installation de Forge..."),
		MODS("Téléchargement des mods..."),
		EXTERNAL_FILES("Téléchargement des fichiers externes..."),
		POST_EXECUTIONS("Lancement en cours..."),
		END("Terminé !");
		String details;
		
		StepInfo(String details) {
			this.details = details;
		}
		
		public String getDetails() {
			return details;
		}
	}
}

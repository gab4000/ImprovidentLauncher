package fr.gab400.improvidentlauncher.panels;

import fr.gab400.improvidentlauncher.ImprovidentLauncher;
import fr.gab400.improvidentlauncher.PanelManager;
import fr.gab400.improvidentlauncher.panel.Panel;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.model.AuthProfile;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class Login extends Panel {
	
	Saver saver = ImprovidentLauncher.getInstance().getSaver();
	
	@Override
	public String getName() {
		return null;
	}
	
	@Override
	public void init(PanelManager panelManager) {
		super.init(panelManager);
		
		setCanTakeAllSize(this.layout);
		
		// Title
		Label title = new Label("Improvident Launcher");
		title.setFont(Font.font("Roboto", FontWeight.BOLD, FontPosture.REGULAR, 30f));
		setCanTakeAllSize(title);
		setTop(title);
		setLeft(title);
		title.setTranslateX(25);
		title.setTranslateY(25);
		this.layout.getChildren().add(title);
		
		GridPane loginWithMS = new GridPane();
		
		Label loginWith = new Label("SE CONNECTER AVEC");
		loginWith.setFont(new Font(25));
		setCenterH(loginWith);
		setCenterV(loginWith);
		loginWith.setTranslateY(-25);
		
		ImageView view = new ImageView(new Image("images/microsoft.png"));
		view.setPreserveRatio(true);
		view.setFitHeight(40);
		Button msLoginBtn = new Button();
		setCanTakeAllSize(msLoginBtn);
		setCenterH(msLoginBtn);
		setCenterV(msLoginBtn);
		msLoginBtn.setTranslateY(20);
		msLoginBtn.setMaxWidth(300);
		msLoginBtn.setGraphic(view);
		msLoginBtn.setOnAction(e -> this.authenticateMS());
		
		loginWithMS.setTranslateY(-100);
		loginWithMS.getChildren().addAll(loginWith, msLoginBtn);
		
		Separator separator = new Separator();
		setCanTakeAllSize(separator);
		setCenterV(separator);
		setCenterH(separator);
		
		GridPane loginOffline = new GridPane();
		loginOffline.setMaxHeight(60);
		
		TextField username = new TextField();
		username.setMaxWidth(300);
		setCanTakeAllSize(username);
		setCenterH(username);
		setCenterV(username);
		username.setPromptText("Pseudo");
		username.setTranslateY(-25);
		
		Button loginOfflineBtn = new Button("Se connecter");
		loginOfflineBtn.setStyle("-fx-font-size: 25px;");
		loginOfflineBtn.setPrefWidth(300);
		loginOfflineBtn.setPrefHeight(50);
		setCanTakeAllSize(loginOfflineBtn);
		setCenterV(loginOfflineBtn);
		setCenterH(loginOfflineBtn);
		loginOfflineBtn.setTranslateY(25);
		loginOfflineBtn.setDisable(username.getText().isEmpty());
		loginOfflineBtn.setOnAction(e -> {
			if (!username.getText().isEmpty()) {
				authenticateCrack(username.getText());
			}
		});
		
		username.setOnKeyTyped(e -> {
			loginOfflineBtn.setDisable(username.getText().isEmpty());
		});
		
		loginOffline.setTranslateY(100);
		loginOffline.getChildren().addAll(username, loginOfflineBtn);
		
		this.layout.getChildren().addAll(loginWithMS, separator, loginOffline);
	}
	
	private void authenticateCrack(String username) {
		AuthInfos infos = new AuthInfos(username, UUID.randomUUID().toString(), UUID.randomUUID().toString());
		saver.set("offline-username", infos.getUsername());
		saver.save();
		ImprovidentLauncher.getInstance().setAuthInfos(infos);
		
		panelManager.showPanel(new App());
	}
	
	private void authenticateMS() {
		
		MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
		authenticator.loginWithAsyncWebview().whenComplete((response, error) -> {
			
			if (error != null) {
				logger.err(error.getMessage());
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Error");
				alert.setContentText(error.getMessage());
				return;
			}
			
			saver.set("msAccessToken", response.getAccessToken());
			saver.set("msRefreshToken", response.getRefreshToken());
			saver.save();
			
			ImprovidentLauncher.getInstance().setAuthInfos(new AuthInfos(response.getProfile().getName(), response.getAccessToken(), response.getProfile().getId()));
			
			Platform.runLater(() -> panelManager.showPanel(new App()));
		});
	}
}

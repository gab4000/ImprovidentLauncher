package fr.gab400.improvidentlauncher.panels;

import fr.gab400.improvidentlauncher.ImprovidentLauncher;
import fr.gab400.improvidentlauncher.PanelManager;
import fr.gab400.improvidentlauncher.panel.Panel;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticationException;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class Login extends Panel {
	
	Saver saver = ImprovidentLauncher.getInstance().getSaver();
	Button msLoginBtn = new Button();
	
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
		setCanTakeAllSize(msLoginBtn);
		setCenterH(msLoginBtn);
		setCenterV(msLoginBtn);
		msLoginBtn.setTranslateY(20);
		msLoginBtn.setMaxWidth(300);
		msLoginBtn.setGraphic(view);
		msLoginBtn.setOnMouseClicked(e -> this.authenticateMS());
		
		loginWithMS.getChildren().addAll(loginWith, msLoginBtn);
		
		this.layout.getChildren().addAll(loginWithMS);
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

package connect4GamePackage;

import javafx.application.Application;
//import javafx.event.ActionEvent;
//import javafx.event.EventHandler;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;

public class gameMain extends Application {
	private gameController Controller;
	private String color="#ffffff";
	static MenuItem copyResetGame;

	private void musicSet1(){
		URL soundUrl = getClass().getClassLoader().getResource("music.wav");
		System.out.println("Set first url =  "+soundUrl);
		soundHandler.runMusic(soundUrl);
	}
	private void musicSet2(){
		URL soundUrl = getClass().getClassLoader().getResource("music.wav");
		System.out.println("Set second url =  "+soundUrl);
		soundHandler.checkMusic(soundUrl);
	}

	@Override
	public  void start(Stage primaryStage) throws Exception {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
		GridPane rootGridPane = loader.load();

		Controller = loader.getController();
		Controller.createPlayground();

		MenuBar menuBar = createMenu();
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

		Pane menuPane = (Pane) rootGridPane.getChildren().get(0);
		menuPane.getChildren().add(menuBar);

		Scene scene = new Scene(rootGridPane);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Connect Four");
		primaryStage.setResizable(false);
		primaryStage.getIcons().add(new Image("/icon/img.jpg"));
		primaryStage.show();

		//Play Music
		musicSet1();

	}

	private MenuBar createMenu(){
		//File Menu
		Menu fileMenu = new Menu("File");

		MenuItem newGame = new MenuItem("New Game");
		newGame.setOnAction(event -> Controller.resetGame());

		MenuItem resetGame = new MenuItem("Reset Game");
		resetGame.setOnAction(event -> Controller.resetGame());
		resetGame.setDisable(true);
		copyResetGame = resetGame;

		SeparatorMenuItem separatorMenuItem = new SeparatorMenuItem();
		MenuItem exitGame = new MenuItem("Exit Game");
		exitGame.setOnAction(event -> exitGame());

		fileMenu.getItems().addAll(newGame,resetGame,separatorMenuItem,exitGame);

		//Help Menu
		Menu helpMenu = new Menu("Help");

		MenuItem aboutGame = new MenuItem("About Game");
		aboutGame.setOnAction(event -> aboutConnect4());

		SeparatorMenuItem separator1 = new SeparatorMenuItem();
		MenuItem aboutMe = new MenuItem("About Me");
		aboutMe.setOnAction(event -> aboutDeveloper());

		helpMenu.getItems().addAll(aboutGame,separator1,aboutMe);

		//Setting Menu
		Menu settingMenu = new Menu("Setting");

		//Music Menu
		MenuItem music = new MenuItem("Music");
		music.setOnAction(event -> musicSet2());

		//Ground color Menu
		SeparatorMenuItem separator2 = new SeparatorMenuItem();
		MenuItem darkMode = new MenuItem("Dark Mode Screen");
		MenuItem lightMode = new MenuItem("Light Mode Screen");

		//By default color of the play ground
		if(Objects.equals(color, "#ffffff")) {
			lightMode.setDisable(true);
		}

		//Disable Menus method
		darkMode.setOnAction(event -> {
			color = gameController.colorPlayground1();
			if(Objects.equals(color, "#000000")) {
				darkMode.setDisable(true);
			}
			lightMode.setDisable(false);
		});

		lightMode.setOnAction(event -> {
			color = gameController.colorPlayground2();
			if(Objects.equals(color, "#ffffff")) {
				lightMode.setDisable(true);
			}
			darkMode.setDisable(false);
		});

		settingMenu.getItems().addAll(music,separator2,darkMode,lightMode);

		//Menu Bar
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu,helpMenu,settingMenu);

		return menuBar;

	}

	private void aboutDeveloper() {

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About The Developer");
		alert.setHeaderText("Kanhaiya Gupta");
		alert.setContentText("I love to play around with code and create games. " +
				"Connect-Four is one of them. In free time " +
				"I like to spend time with nears and dears.  ");
		alert.show();
	}

	private void aboutConnect4() {

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About Connect Four Game");
		alert.setHeaderText("How to play ?");
		alert.setContentText("Connect Four is a two-player connection game in which " +
						"the players first choose a color and then take turns dropping colored" +
						" discs from the top into a seven-column, six-row vertically suspended grid." +
						" The pieces fall straight down, occupying the next available space within the " +
						"column. The objective of the game is to be the first to form a horizontal, " +
						"vertical, or diagonal line of four of one's own discs. Connect Four is" +
						" a solved game. The first player can always win by playing the right moves.");
		alert.show();
	}

	private void exitGame() {
		Platform.exit();
		System.exit(0);
	}

	public static void main(String[] args){
		launch(args);
	}
}

package connect4GamePackage;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class gameController implements Initializable {

	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int CIRCLE_DIAMETER = 80;
	private static final String discColor1 = "#0000FF";
	private static final String discColor2 = "#800000";

	private static final String color1 = "#000000";
    private static final String color2 = "#ffffff";

	private static String PLAYER_ONE = "Player One";
	private static String PLAYER_TWO = "Player Two";
	private static Shape copyRectangle;

	private boolean isPlayerOneTurn = true;

	private Disc[][] insertedDiscArray = new Disc[ROWS][COLUMNS]; //For structural changes (for Developers)


	@FXML
	public GridPane rootGridPane;
	public Pane insertedDiskPane;
	public Label playerNameLabel;
	public TextField playerOneTextField, playerTwoTextField;
	public Button setNamesButton;


	private boolean isAllowedToInsert = true;
	private static boolean flag = false;


	void createPlayground(){

		Shape rectangleWithHoles = gameStructuralGrid();
		rootGridPane.add(rectangleWithHoles, 0, 1);
		copyRectangle = rectangleWithHoles;

		List<Rectangle> rectangleList = createClickableColumns();

		for (Rectangle rectangle:rectangleList) {
			rootGridPane.add(rectangle,0,1);
		}

		 setNamesButton.setOnAction((ActionEvent actionEvent) -> {
			 if(flag){
			 	checkPlayground();
			 }else {
			 	checkNames();
			 }
		 });

	}

	static String colorPlayground1(){
		//Black
		copyRectangle.setFill(Paint.valueOf(color1));
		 return color1;
	 }
	static String colorPlayground2(){
		//White
		copyRectangle.setFill(Paint.valueOf(color2));
		return color2;
	}

	private void checkPlayground() {
		if (null != insertedDiskPane) {
				groundError();
		} else {
			checkNames();
	   }

	}

	private void groundError() {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setTitle("Connect Four");
		alert.setHeaderText("Warning");
		alert.setContentText("You can't set the new names.\nPlease reset the game.");

		ButtonType ok = new ButtonType("OK");
		alert.getButtonTypes().setAll(ok);

			Optional<ButtonType> btnClicked = alert.showAndWait();
			if (btnClicked.isPresent() && btnClicked.get() == ok) {
				try {
					gameMain.copyResetGame.setDisable(false);
				}catch (NullPointerException e){
					System.out.println("Till now Reset button is enabled.");
				}
				System.out.println("Reset button is enabled.");
			}

	}

	private void checkNames(){

	 	String n1 = playerOneTextField.getText();
	 	String n2 = playerTwoTextField.getText();
		if(n1.equals("") || n2.equals("")){
			nameError();
		}else {
			setNames();
		}
	}

	private void nameError() {
	 	Alert alert = new Alert(Alert.AlertType.ERROR);
	 	alert.setTitle("Connect Four");
	 	alert.setHeaderText("Name Error");
	 	alert.setContentText("Please set valid names.");
	 	alert.show();
	}

	private void setNames() {
		flag=true;
		PLAYER_ONE = playerOneTextField.getText();
		PLAYER_TWO = playerTwoTextField.getText();
		playerNameLabel.setText(isPlayerOneTurn? PLAYER_ONE : PLAYER_TWO);
	}

	private Shape gameStructuralGrid(){

		Shape rectangleWithHoles =  new Rectangle((COLUMNS +1) * CIRCLE_DIAMETER, (ROWS +1) * CIRCLE_DIAMETER);

		for (int row = 0; row < ROWS; row++){
			for (int col = 0; col < COLUMNS; col++){

				Circle circle = new Circle();
				circle.setRadius(CIRCLE_DIAMETER/2);
				circle.setCenterX(CIRCLE_DIAMETER/2);
				circle.setCenterY(CIRCLE_DIAMETER/2);
				circle.setSmooth(true);

				circle.setTranslateX(col * (CIRCLE_DIAMETER+5) + CIRCLE_DIAMETER/4);
				circle.setTranslateY(row * (CIRCLE_DIAMETER+5) + CIRCLE_DIAMETER/4);

				rectangleWithHoles = Shape.subtract(rectangleWithHoles, circle);
			}
		}

		rectangleWithHoles.setFill(Color.WHITE);

		return rectangleWithHoles;
	}

	private List<Rectangle> createClickableColumns(){

		List<Rectangle> rectangleList = new ArrayList<>();

		for (int col = 0; col < COLUMNS; col++){
			Rectangle rectangle = new Rectangle(CIRCLE_DIAMETER,(ROWS + 1) * CIRCLE_DIAMETER);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(col * (CIRCLE_DIAMETER+5) + CIRCLE_DIAMETER/4);

			rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee20")));
			rectangle.setOnMouseExited(event ->  rectangle.setFill(Color.TRANSPARENT));

			final int column = col;
			rectangle.setOnMouseClicked((MouseEvent event) -> {

				    if(!flag) {
					    nameError();
				    }

					if(isAllowedToInsert){
						isAllowedToInsert = false;  //When disc is being dropped then no more disc will be inserted
						insertedDisc(new Disc(isPlayerOneTurn),column);
					}
					});
			rectangleList.add(rectangle);
		}

        return rectangleList;
	}

	private void insertedDisc(Disc disc, int column){

		int row = ROWS-1;
		while (row >= 0){
			if(getDiscIfPresent(row,column) == null)
				break;
			row --;
		}

		if(row<0)         //If it is full, we can't insert anymore disc
			return;

		insertedDiscArray[row][column] = disc;  // For structural change(For Developers)
		insertedDiskPane.getChildren().add(disc);

		disc.setTranslateX(column * (CIRCLE_DIAMETER+5) + CIRCLE_DIAMETER/4);
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5), disc);
		translateTransition.setToY(row * (CIRCLE_DIAMETER+5) + CIRCLE_DIAMETER/4);

		int currentRow = row;
		translateTransition.setOnFinished(event -> {

			isAllowedToInsert = true; //Finally, when disc is dropped allow next player to insert disc.
			if(gameEnded(currentRow, column)){
				gameOver();
				return;
			}
			isPlayerOneTurn = !isPlayerOneTurn;
			playerNameLabel.setText(isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO);

		});

		translateTransition.play();
	}

	private boolean gameEnded(int row, int column) {

		//Vertical Points, a small example: Player has inserted his last disc at row =>2, column =>3
		//range of values = 0,1,2,3,4,5
		//Index of each element present in column[row][column]: 0,3  1,3  2,3  3,3  4,3  5,3  ---> Point2D class x,y

		List<Point2D> verticalPoints = IntStream.rangeClosed(row-3,row+3)   //range of values = 0,1,2,3,4,5
				              .mapToObj(r->new Point2D(r,column))   //0,3  1,3  2,3  3,3  4,3  5,3  ---> Point2D
				                   .collect(Collectors.toList());

		List<Point2D> horizontalPoints = IntStream.rangeClosed(column-3,column+3)
				.mapToObj(c->new Point2D(row,c))  
				.collect(Collectors.toList());


		Point2D startPoint1 = new Point2D(row -3, column +3);
		List<Point2D> diagonal1Points = IntStream.rangeClosed(0,6)
				           .mapToObj(i -> startPoint1.add(i, -i))
				                   .collect(Collectors.toList());

		Point2D startPoint2 = new Point2D(row -3, column -3);
		List<Point2D> diagonal2Points = IntStream.rangeClosed(0,6)
				.mapToObj(i -> startPoint2.add(i, i))
				.collect(Collectors.toList());

//		boolean isEnded = checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
//				         || checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);
//
//		return isEnded;

		return checkCombinations(verticalPoints) || checkCombinations(horizontalPoints)
				         || checkCombinations(diagonal1Points) || checkCombinations(diagonal2Points);
	}

	private boolean checkCombinations(List<Point2D> points) {

		int chain = 0;
		for (Point2D point : points)
		{

			int rowIndexForArray = (int) point.getX();
			int colIndexForArray = (int) point.getY();

			Disc disc = getDiscIfPresent(rowIndexForArray,colIndexForArray);

			if(disc != null && disc.isPlayerOneMove == isPlayerOneTurn)
			{

				chain++;
				if (chain == 4)
					return true;

			}
			else
				chain = 0;

		}
		return false;

	}

	private Disc getDiscIfPresent(int row, int column){ //To prevent ArrayIndexOutOfBoundException

		if(row >= ROWS || row < 0 || column >= COLUMNS || column <0) //If row or column index is invalid
			return null;

		return insertedDiscArray[row][column];

	}

	private void gameOver() {

		String winner = isPlayerOneTurn ? PLAYER_ONE : PLAYER_TWO;

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect Four");
		alert.setHeaderText("The winner is "+winner+".");
		alert.setContentText("Want to play Again ?");

		ButtonType yesBtn = new ButtonType("Yes");
		ButtonType noBtn = new ButtonType("No, Exit");
		alert.getButtonTypes().setAll(yesBtn,noBtn);

		Platform.runLater( () ->{

			Optional<ButtonType> btnClicked = alert.showAndWait();
			if(btnClicked.isPresent() && btnClicked.get() == yesBtn){
				resetGame();
			}else {
				Platform.exit();
				System.exit(0);
			}
		});
	}

	void resetGame() {

		 flag = false;
		 playerOneTextField.clear();
		 playerTwoTextField.clear();
		 PLAYER_ONE = "Player One";
		 PLAYER_TWO = "Player Two";

		gameMain.copyResetGame.setDisable(true);

		insertedDiskPane.getChildren().clear();  //Remove all inserted disc from Pane
		for (int row=0; row<insertedDiscArray.length; row++){   //Structurally, Remove all inserted disc
			for(int col=0; col<insertedDiscArray[row].length; col++){
				insertedDiscArray[row][col] = null;
			}
		}
		isPlayerOneTurn = true; //Let player start the game
		playerNameLabel.setText(PLAYER_ONE);

		createPlayground(); //Prepare a fresh playground
	}

	private static class Disc extends Circle{

		private final boolean isPlayerOneMove;

		private Disc(boolean isPlayerOneMove){

			this.isPlayerOneMove=isPlayerOneMove;
			setRadius(CIRCLE_DIAMETER/2.4);
			setFill(isPlayerOneMove ? Color.valueOf(discColor1) : Color.valueOf(discColor2));
			setCenterX(CIRCLE_DIAMETER/2);
			setCenterY(CIRCLE_DIAMETER/2);
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}

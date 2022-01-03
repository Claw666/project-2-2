package Group1.GUI;

import Group1.FileReader.Scenario;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;


public class Menu extends Application{

    public void start(Stage primaryStage) throws Exception {
        //Define Window title
        primaryStage.setTitle("Team Number One - Prison Break");

        //initialize elements
        HBox menuBox = new HBox();
        VBox menuItemOneContainer = new VBox();
        VBox menuItemTwoContainer = new VBox();
        VBox menuItemThreeContainer = new VBox();
        VBox mainContainer = new VBox();
        VBox sliderBox = new VBox();
        Label menuOneLabel = new Label("Import Settings");
        Label menuTwoLabel = new Label("Start Game");
        Label menuThreeLabel = new Label("Rules");
        menuOneLabel.setPadding(new Insets(20,0,0,0));
        menuOneLabel.getStyleClass().add("title_2");
        menuTwoLabel.setPadding(new Insets(20,0,0,0));
        menuTwoLabel.getStyleClass().add("title_2");
        menuThreeLabel.setPadding(new Insets(20,0,0,0));
        menuThreeLabel.getStyleClass().add("title_2");

        //Menu items on home page
        ImageView iv_1 = new ImageView();
        iv_1.setImage(new Image(new FileInputStream("src/main/java/Group1/GUI/assets/cogs.png")));
        iv_1.setPreserveRatio(true);
        iv_1.setFitHeight(150);

        ImageView iv_2 = new ImageView();
        iv_2.setImage(new Image(new FileInputStream("src/main/java/Group1/GUI/assets/game.png")));
        iv_2.setPreserveRatio(true);
        iv_2.setFitHeight(150);

        ImageView iv_3 = new ImageView();
        iv_3.setImage(new Image(new FileInputStream("src/main/java/Group1/GUI/assets/question.png")));
        iv_3.setPreserveRatio(true);
        iv_3.setFitHeight(150);

        menuItemOneContainer.getChildren().addAll(iv_1,menuOneLabel);
        menuItemTwoContainer.getChildren().addAll(iv_2,menuTwoLabel);
        menuItemThreeContainer.getChildren().addAll(iv_3,menuThreeLabel);


        //Menu 1 Container: add style to it and set function on mouse click
        menuItemOneContainer.getStyleClass().add("button");
        menuItemOneContainer.setPadding(new Insets(10,10,10,10));
        menuItemOneContainer.setPickOnBounds(true); // allows click on transparent areas
        menuItemOneContainer.setAlignment(Pos.CENTER);
        //menuItemOneContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {

        //Menu 2 Container: add style to it and set function on mouse click
        menuItemTwoContainer.getStyleClass().add("button");
        menuItemTwoContainer.setPadding(new Insets(10,10,10,10));
        menuItemTwoContainer.setPickOnBounds(true); // allows click on transparent areas
        menuItemTwoContainer.setAlignment(Pos.CENTER);
        menuItemTwoContainer.setOnMouseClicked(e -> {
            GameMap gameMap = new GameMap(primaryStage);
        });

        //Menu 3 Container: add style to it and set function on mouse click
        menuItemThreeContainer.getStyleClass().add("button");
        menuItemThreeContainer.setPadding(new Insets(10,10,10,10));
        menuItemThreeContainer.setPickOnBounds(true); // allows click on transparent areas
        menuItemThreeContainer.setAlignment(Pos.CENTER);
        //menuItemOneContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {

        menuBox.getChildren().addAll(Util.createSpacer(),menuItemOneContainer,Util.createSpacer(), menuItemTwoContainer,Util.createSpacer(), menuItemThreeContainer,Util.createSpacer());
        menuBox.setPadding(new Insets(0,0,0,0));


        Scene scene = new Scene(mainContainer);

        //Create title for the home page
        Text title = new Text("Catch or be Caught!");
        title.getStyleClass().add("title");

        //Create top part of the home page
        sliderBox.setAlignment(Pos.CENTER);
        sliderBox.setPrefHeight(500);
        sliderBox.getChildren().addAll(title);

        mainContainer.getChildren().addAll(sliderBox,menuBox);
        mainContainer.getStyleClass().add("linear-grad");


        //Add external stylesheet
        scene.getStylesheets().add("Group1/GUI/Main.css");

        primaryStage.setScene(scene);

        Util.adjustScreenSizeToMax(primaryStage);

        //Show stage
        primaryStage.show();
    }
}

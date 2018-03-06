/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.Observable;
import java.util.Observer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 *
 * @author Oli Loades
 */
public class View extends Application implements Observer {

    private BranchGraph graph;
    private Model model;
    private Button newGraphButrton = new Button("New");
    private Button keyButton = new Button("Key");
    private Stage popUpStage;
    private Controller controller;
    private BorderPane root = new BorderPane();

    @Override
    public void start(Stage primaryStage) {
        model = new Model();
        controller = new Controller(this, model);

        disableButtons();

        newGraphButrton.setOnAction((ActionEvent event) -> {
            controller.createNewRepoPopUp();
        });
        keyButton.setOnAction((ActionEvent event) -> {
            controller.createKeyPopUp();
        });
        root.setTop(setTopLayout());
        Scene scene = new Scene(root, 1000, 1000);

        model.addObserver(this);

        primaryStage.setTitle("Branch Graph");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private HBox setTopLayout() {
        HBox topButtons = new HBox();

        topButtons.setSpacing(100);
        topButtons.getChildren().addAll(newGraphButrton);
        topButtons.getChildren().addAll(keyButton);
        topButtons.setAlignment(Pos.CENTER);

        return topButtons;
    }

    private void disableButtons() {
        keyButton.setDisable(true);
    }

    public void enableButtons() {
        keyButton.setDisable(false);
    }

    public void createNewRepoPopUp() {
        NewGraphPopUp popUp = new NewGraphPopUp(controller);
        popUpStage = new Stage();
        Scene secondaryScene = new Scene(popUp, 275, 75);
        popUpStage.setScene(secondaryScene);
        popUpStage.setResizable(false);
        popUpStage.show();
    }

    public void createKeyPopUp() {
        KeyPopUp popUp = new KeyPopUp(model, controller);
        popUpStage = new Stage();
        Scene secondaryScene = new Scene(popUp, popUp.getWidth(), popUp.getHeight());
        popUpStage.setScene(secondaryScene);
        popUpStage.setResizable(false);
        popUpStage.show();
    }

    public void closePopUp() {
        popUpStage.close();
    }

    @Override
    public void update(Observable o, Object arg) {
     //   graph = new BranchGraph(model, 500, 500);
        // root.setCenter(graph.getPane());
    }

    void emptyRepoAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Repository is empty");
        alert.showAndWait();
    }

}

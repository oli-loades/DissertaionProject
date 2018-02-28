/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Oli Loades
 */
public class NewGraphPopUp extends GridPane {

    private final double WINDOW_HEIGHT = 100;
    private final double WINDOW_WIDTH = 100;
    private final double HGAP = 10;
    private final double VGAP = 10;
    private Button OKButton;
    private Button CancelButton;
    private Label fieldName;
    private TextField URLField;
    private Controller controller;

    public NewGraphPopUp(Controller controller) {
        this.controller = controller;
        OKButton = new Button("OK");

        CancelButton = new Button("Cancel");

        fieldName = new Label("Repository Location");
        URLField = new TextField();

        setUp();

        OKButton.setOnAction((ActionEvent event) -> {
            this.controller.createNewRepo(URLField.getText());
        });

        CancelButton.setOnAction((ActionEvent event) -> {
            this.controller.close();
        });
    }

    private void setUp() {
        setHgap(HGAP);
        setVgap(VGAP);
        setPrefSize(WINDOW_HEIGHT, WINDOW_WIDTH);
        add(fieldName, 0, 0);
        add(URLField, 1, 0);
        add(OKButton, 0, 1);
        add(CancelButton, 1, 1);
        
        GridPane.setHalignment(OKButton, HPos.CENTER);
        GridPane.setValignment(OKButton, VPos.CENTER);
        GridPane.setHalignment(CancelButton, HPos.CENTER); 
        GridPane.setValignment(CancelButton, VPos.CENTER);
        
        this.setAlignment(Pos.CENTER);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.Iterator;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Oli Loades
 */
public class KeyPopUp extends BorderPane {

    private Model model;
    private final double HGAP = 5;
    private final double VGAP = 5;
    private Button OKButton;
    private final double BRANCH_COMPONENT_HEIGHT = 20;
    private final double MAX_BRANCH_LABEL_WIDTH = 200;
    private final double MAX_BRANCH_COLOUR_WIDTH = 20;
    private final double PREF_BUTTON_WIDTH = 50;
    private Controller controller;
    private GridPane content;
    private ScrollPane scroll;

    public KeyPopUp(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;

        OKButton = new Button("OK");
        OKButton.setPrefSize(PREF_BUTTON_WIDTH, BRANCH_COMPONENT_HEIGHT);

        content = new GridPane();
        content.setVgap(VGAP);
        content.setHgap(HGAP);

        this.setPadding(new Insets(5, 5, 5, 5));
        setPrefSize(250, 250);

        setUp();

        OKButton.setOnAction((ActionEvent event) -> {
            this.controller.close();
        });
    }

    private void setUp() {
        int row, col = 0;
        Iterator mapIter = model.getGraphModel().getBranchColours().entrySet().iterator();
        while (mapIter.hasNext()) {
            row = 0;
            Map.Entry branchColourPair = (Map.Entry) mapIter.next();

            Label branchName = new Label(branchColourPair.getKey().toString());
            branchName.setPrefSize(MAX_BRANCH_LABEL_WIDTH, BRANCH_COMPONENT_HEIGHT);

            content.add(branchName, row, col);

            GridPane.setHalignment(branchName, HPos.LEFT);
            GridPane.setValignment(OKButton, VPos.CENTER);

            row++;

            Rectangle branchColour = createRectangle((Color) branchColourPair.getValue());

            content.add(branchColour, row, col);

            GridPane.setHalignment(branchColour, HPos.LEFT);
            GridPane.setValignment(branchColour, VPos.CENTER);

            col++;
        }

        content.setAlignment(Pos.CENTER);
        scroll = new ScrollPane(content);
        scroll.setPrefSize(100, 100);

        this.setCenter(scroll);
        this.setBottom(OKButton);
        BorderPane.setAlignment(OKButton, Pos.BOTTOM_CENTER);
    }

    private Rectangle createRectangle(Color colour) {
        Rectangle r = new Rectangle();
        r.setFill(colour);
        r.setHeight(BRANCH_COMPONENT_HEIGHT);
        r.setWidth(MAX_BRANCH_COLOUR_WIDTH);
        return r;
    }

}

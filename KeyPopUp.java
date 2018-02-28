/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.Iterator;
import java.util.Map;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Oli Loades
 */
public class KeyPopUp extends GridPane {

    private Model model;
    private final double HGAP = 5;
    private final double VGAP = 5;
    private Button OKButton;
    private final double BRANCH_COMPONENT_HEIGHT = 20;
    private final double MAX_BRANCH_LABEL_WIDTH = 300;
    private final double MAX_BRANCH_COLOUR_WIDTH = 20;
    private final double PREF_BUTTON_WIDTH = 300;
    private Controller controller;

    public KeyPopUp(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;
        OKButton = new Button("OK");
        OKButton.setPrefSize(PREF_BUTTON_WIDTH, BRANCH_COMPONENT_HEIGHT);
        this.setVgap(VGAP);
        this.setHgap(HGAP);

        setUp();
        setPopUpSize();

        OKButton.setOnAction((ActionEvent event) -> {
            this.controller.close();
        });
    }

    private void setPopUpSize() {
        int branchCount = model.getGraphModel().getModel().getNumBranches();
        setHeight(branchCount * 40);
        setWidth((VGAP * 3) + MAX_BRANCH_LABEL_WIDTH + MAX_BRANCH_COLOUR_WIDTH);
    }

    private void setUp() {
        int row, col = 0;
        Iterator mapIter = model.getGraphModel().getBranchColours().entrySet().iterator();
        while (mapIter.hasNext()) {
            row = 0;
            Map.Entry branchColourPair = (Map.Entry) mapIter.next();

            Label branchName = new Label(branchColourPair.getKey().toString());
            branchName.setPrefSize(MAX_BRANCH_LABEL_WIDTH, BRANCH_COMPONENT_HEIGHT);
            GridPane.setMargin(branchName, new Insets(5, 5, 5, 5));
            add(branchName, row, col);

            row++;

            Rectangle branchColour = createRectangle((Color) branchColourPair.getValue());
            GridPane.setMargin(branchColour, new Insets(5, 5, 5, 5));

            add(branchColour, row, col);
            col++;
        }
        GridPane.setMargin(OKButton, new Insets(10, 50, 10, 50));
        add(OKButton, 0, col);
    }

    private Rectangle createRectangle(Color colour) {
        Rectangle r = new Rectangle();
        r.setFill(colour);
        r.setHeight(BRANCH_COMPONENT_HEIGHT);
        r.setWidth(MAX_BRANCH_COLOUR_WIDTH);
        return r;
    }

}
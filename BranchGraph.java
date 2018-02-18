/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;

/**
 *
 * @author Oli Loades
 */
public class BranchGraph {

    private GraphModel graphModel;
    private ZoomableScrollPane pane;
    private Group canvas;
    
    private LayoutManager layout;

    public BranchGraph(Model model) {
        graphModel = new GraphModel(model);
        canvas = new Group();
        addCompoenets();      
        pane = new ZoomableScrollPane(canvas);
        pane.setPrefSize(900, 900);
        pane.getStylesheets().add(BranchGraph.class.getResource("ScrollPaneStyle.css").toExternalForm());
    }

    public GraphModel getGraphModel() {
        return graphModel;
    }

    public void setGraphModel(GraphModel graphModel) {
        this.graphModel = graphModel;
    }

    public ZoomableScrollPane getPane() {
        return pane;
    }

    public void setPane(ZoomableScrollPane pane) {
        this.pane = pane;
    }

    public Group getCanvas() {
        return canvas;
    }

    public void setCanvas(Group canvas) {
        this.canvas = canvas;
    }

    private void addCompoenets() {
      canvas.getChildren().addAll(graphModel.getNodeList());
      canvas.getChildren().addAll(graphModel.getCommitEdgeList());
      canvas.getChildren().addAll(graphModel.getMergeEdgeList());
    }

}

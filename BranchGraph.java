/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;


/**
 *
 * @author Oli Loades
 */
public class BranchGraph {

    private GraphModel graphModel;
    private ScrollPane pane;
    private Group canvas;

    private LayoutManager layout;

    public BranchGraph(Model model) {
        graphModel = new GraphModel(model);
        canvas = new Group();

        layout = new LayoutManager(graphModel);
        setUp();
        graphModel.printEdgeList();
        pane.setPrefSize(900, 900);
        pane.getStylesheets().add(BranchGraph.class.getResource("ScrollPaneStyle.css").toExternalForm());
    }

    public GraphModel getGraphModel() {
        return graphModel;
    }

    public void setGraphModel(GraphModel graphModel) {
        this.graphModel = graphModel;
    }

    public ScrollPane getPane() {
        return pane;
    }

    public void setPane(ScrollPane pane) {
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

    private void setUp() {
        graphModel.populateCommitList();
        graphModel.populateMergeList();
        addCompoenets();
        pane = new ScrollPane(canvas);
    }

}

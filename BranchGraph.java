/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.Observable;
import java.util.Observer;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;

/**
 *
 * @author Oli Loades
 */
public class BranchGraph implements Observer {

    private Model model;
    private ZoomableScrollPane pane;
    private Group canvas;
    private final double HEIGHT;
    private final double WIDTH;

    public BranchGraph(Model model, double w, double h) {
        this.model = model;
        HEIGHT = h;
        WIDTH = w;
      

        this.model.addObserver(this);
        update(null, null);
    }

    public Model getModel() {
        return model;
    }

    public void newLayout() {
        LayoutManager layout = new LayoutManager(model.getGraphModel(), HEIGHT);
    }

    public ScrollPane getPane() {
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
        canvas.getChildren().addAll(model.getGraphModel().getNodeList());
        canvas.getChildren().addAll(model.getGraphModel().getCommitEdgeList());
        canvas.getChildren().addAll(model.getGraphModel().getMergeEdgeList());
        for (CommitNode node : model.getGraphModel().getNodeList()) {
            node.toFront();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        canvas = new Group();
        newLayout();
        addCompoenets();
        setUpPane();
        //pane.setCanvas(canvas);
        newLayout();
    }

    private void setUpPane() {
         pane = new ZoomableScrollPane(canvas);
        pane.setPrefSize(HEIGHT, WIDTH);
        pane.getStylesheets().add(BranchGraph.class.getResource("ScrollPaneStyle.css").toExternalForm());
    }

}

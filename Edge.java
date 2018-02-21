/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import javafx.scene.shape.Line;

/**
 *
 * @author Oli Loades
 */
public class Edge extends Line {

    private CommitNode source;
    private CommitNode target;

    public Edge(CommitNode source, CommitNode target) {
        super();
        this.source=source;
        this.target = target;
    }

    public CommitNode getSource() {
        return source;
    }

    public void setSource(CommitNode source) {
        this.source = source;
    }

    public CommitNode getTarget() {
        return target;
    }

    public void setTarget(CommitNode target) {
        this.target = target;
    }

    public void update() {
         setEndX(target.getLayoutX());
        setEndY(target.getLayoutY());
        setStartX(source.getLayoutX());
        setStartY(source.getLayoutY());   
    }
}

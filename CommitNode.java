/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 *
 * @author Oli Loades
 */
public class CommitNode extends Circle {

    private String name;
    private String branch;

    
    public CommitNode(String n, String b){
        super(5);
        name = n;
        branch = b;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLeftXConnectionPoint() {
        return getCenterX() - getRadius();
    }

    public double getRightXConnectionPoint() {
        return getCenterX() + getRadius();
    }

}

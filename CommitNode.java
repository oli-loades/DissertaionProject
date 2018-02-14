/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import javafx.scene.shape.Circle;

/**
 *
 * @author Oli Loades
 */
public class CommitNode extends Circle {

    private String name;

    public CommitNode(String n, double r, int x, int y) {
        super(r, x, y);
        name = n;
    }

    public CommitNode(String n, int x, int y) {
        super(10, x, y);
        name = n;
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

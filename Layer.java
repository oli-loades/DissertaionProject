/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.time.LocalDate;

/**
 *
 * @author Oli Loades
 */
public class Layer {

    private LocalDate date;
    private double startX;
    private double endX;
    private final double NODE_WIDTH;
    private double currentX;

    public Layer(LocalDate date, double width, double start, int numNodes) {
        this.date = date;
        NODE_WIDTH = width;
        startX = start;
        currentX = startX;
        endX = startX + (numNodes * NODE_WIDTH);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getStartX() {
        return startX;
    }

    public void setStartX(double startX) {
        this.startX = startX;
    }

    public double getEndX() {
        return endX;
    }

    public void setEndX(double endX) {
        this.endX = endX;
    }

    public double nextX() {
        currentX = currentX + NODE_WIDTH;
        return currentX;
    }

    public void resetCurrentX() {
        currentX = startX;
    }

}

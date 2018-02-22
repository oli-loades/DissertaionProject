/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author Oli Loades
 */
public class CommitNode extends Circle {
    
    private CommitStat commit;
    private String branch;
    private final Tooltip toolTip;
    
    public CommitNode(CommitStat com, String b, Color col) {
        super(5);
        commit = com;
        branch = b;
        toolTip = new Tooltip();
        setFill(col);
        addToolTip();
    }
    
    public double getLeftXConnectionPoint() {
        return getLayoutX() - getRadius();
    }
    
    public double getRightXConnectionPoint() {
        return getLayoutX() + getRadius();
    }
    
    public double getTopXConnectionPoint() {
        return getLayoutY() + getRadius();
    }
    
    public double getBottomXConnectionPoint() {
        return getLayoutY() - getRadius();
    }
    
    public String getBranch() {
        return branch;
    }
    
    public void setBranch(String branch) {
        this.branch = branch;
    }
    
    public CommitStat getCommit() {
        return commit;
    }
    
    public void setCommit(CommitStat commit) {
        this.commit = commit;
    }
    
    private void addToolTip() {        
        toolTip.setText(commit.getName() + "\n" + commit.getDate().toString());
        Tooltip.install(this, toolTip);
    }
    
}

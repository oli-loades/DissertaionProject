/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import javafx.scene.control.Tooltip;
import javafx.scene.shape.Circle;

/**
 *
 * @author Oli Loades
 */
public class CommitNode extends Circle {

    private CommitStat commit;
    private String branch;
    private Tooltip toolTip;

    public CommitNode(CommitStat c, String b) {
        super(5);
        commit = c;
        branch = b;
        toolTip = new Tooltip(commit.getName());
        addToolTip();
    }

    public double getLeftXConnectionPoint() {     
        return getLayoutX() - getRadius();
    }

    public double getRightXConnectionPoint() {
        return getLayoutX() + getRadius();
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
        Tooltip.install(this, toolTip);
    }

}

    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import javafx.scene.control.Tooltip;

/**
 *
 * @author Oli Loades
 */
public class MergeEdge extends Edge {

    public MergeEdge(CommitNode source, CommitNode target) {
        super(source, target);
        getStrokeDashArray().addAll(5.0, 10.0);//dotted line
    }

    @Override
    public void setColour() {
        setStroke(getSource().getFill());
    }

    @Override
    public void addTooltip() {
        Tooltip tooltip = new Tooltip();
        tooltip.setText(getSource().getBranch());
        Tooltip.install(this, tooltip);
    }
}

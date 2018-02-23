/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

/**
 *
 * @author Oli Loades
 */
public class MergeEdge extends Edge {

    public MergeEdge(CommitNode source, CommitNode target) {
        super(source, target);
        getStrokeDashArray().addAll(5.0, 10.0);
    }

    @Override
    public void setColour() {
        setStroke(getSource().getFill());
    }
}

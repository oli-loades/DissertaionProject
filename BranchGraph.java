/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.List;
import javafx.scene.Group;

/**
 *
 * @author Oli Loades
 */
public class BranchGraph {

    private Model model;
    private Group canvas;
    private List<CommitNode> nodeList;
    private List<Edge> commitEdgelist;
    private List<MergeEdge> mergeEdgelist;

    public BranchGraph(Model model) {
        this.model = model;

    }

    private void buildGraph() {

    }

}

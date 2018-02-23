/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

/**
 *
 * @author Oli Loades
 */
public class GraphModel {

    private Model model;
    private List<CommitNode> nodeList;
    private List<Edge> commitEdgeList;
    private List<MergeEdge> mergeEdgeList;
    private ColourManager colourManager;

    public GraphModel(Model model) {
        this.model = model;
        nodeList = new ArrayList<>();
        commitEdgeList = new ArrayList<>();
        mergeEdgeList = new ArrayList<>();
        colourManager = new ColourManager(model.getNumBranches());
        populateLists();
    }

    private void populateLists() {
        populateNodeList();
        populateCommitList();
        populateMergeList();
    }

    private void populateNodeList() {
        for (BranchStat branch : getModel().getBranchList()) {
            Color colour = colourManager.getNextColour();
            for (CommitStat commit : branch.getCommitList()) {
                if (!containsNode(commit.getName())) {
                    CommitNode newNode = new CommitNode(commit, branch.getBranchName(), colour);
                    nodeList.add(newNode);
                }
            }
        }
    }

    public void populateMergeList() {
        for (BranchStat branch : getModel().getBranchList()) {
            for (MergeStat merge : branch.getMergeList()) {
                for (int i = 0; i < merge.getNumSources(); i++) {
                    CommitNode target = findNode(merge.getName());
                    CommitNode source = findNode(merge.getSource(i));
                    if (source != null && target != null) {
                        if (!containsMergeEdge(source.getCommit().getName(), target.getCommit().getName())) {
                            MergeEdge edge = new MergeEdge(source, target);
                            mergeEdgeList.add(edge);
                        }
                    }
                }
            }
        }
    }

    public void populateCommitList() {
        for (BranchStat branch : getModel().getBranchList()) {
            int j = 0;
            for (int i = 1; i < branch.getCommitList().size(); i++) {
                CommitNode target = findNode(branch.getCommit(i).getName());
                CommitNode source = findNode(branch.getCommit(j).getName());
                if (source != null && target != null) {
                    if (!containsCommitEdge(source.getCommit().getName(), target.getCommit().getName())) {
                        Edge edge = new Edge(source, target);
                        commitEdgeList.add(edge);
                    }
                }
                j++;
            }
        }
    }

    private CommitNode findNode(String name) {
        for (CommitNode node : getNodeList()) {
            if (node.getCommit().getName().equals(name)) {
                return node;
            }
        }
        return null;
    }

    public List<CommitNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<CommitNode> nodeList) {
        this.nodeList = nodeList;
    }

    public List<Edge> getCommitEdgeList() {
        return commitEdgeList;
    }

    public void setCommitEdgeList(List<Edge> commitEdgeList) {
        this.commitEdgeList = commitEdgeList;
    }

    public List<MergeEdge> getMergeEdgeList() {
        return mergeEdgeList;
    }

    public void setMergeEdgeList(List<MergeEdge> mergeEdgeList) {
        this.mergeEdgeList = mergeEdgeList;
    }

    public CommitNode getNode(int index) {
        return nodeList.get(index);
    }

    public MergeEdge getMergeEdge(int index) {
        return mergeEdgeList.get(index);
    }

    public Edge getCmmitEdge(int index) {
        return commitEdgeList.get(index);
    }

    private boolean containsNode(String name) {
        boolean found = false;
        for (CommitNode node : nodeList) {
            if (node.getCommit().getName().equals(name)) {
                found = true;
            }
        }
        return found;
    }

    private boolean containsCommitEdge(String sourceName, String targetName) {
        boolean found = false;
        for (Edge commitEdge : commitEdgeList) {
            if (commitEdge.getSource().getCommit().getName().equals(sourceName)) {
                if (commitEdge.getTarget().getCommit().getName().equals(targetName)) {
                    found = true;
                }
            }
        }
        return found;
    }

    private boolean containsMergeEdge(String sourceName, String targetName) {
        boolean found = false;
        for (MergeEdge mergeEdge : mergeEdgeList) {
            if (mergeEdge.getSource().getCommit().getName().equals(sourceName)) {
                if (mergeEdge.getTarget().getCommit().getName().equals(targetName)) {
                    found = true;
                }
            }
        }
        return found;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public double getNodeRadius() {
        return nodeList.get(0).getRadius();
    }
}

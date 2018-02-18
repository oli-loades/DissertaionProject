/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author WinPC01
 */
public class GraphModel {

    private Model model;
    private List<CommitNode> nodeList;
    private List<Edge> commitEdgeList;
    private List<MergeEdge> mergeEdgeList;

    public GraphModel(Model model) {
        this.model = model;
        nodeList = new ArrayList<>();
        commitEdgeList = new ArrayList<>();
        mergeEdgeList = new ArrayList<>();
        populateLists();
    }

    private void populateLists() {
        populateNodeList();
        System.out.println(nodeList.size());
        populateCommitList();
        populateMergeList();
    }

    private void populateNodeList() {
        for (BranchStat branch : model.getBranchList()) {
            for (CommitStat commit : branch.getCommitList()) {     
                if (!containsNode(commit.getName())) {
                    CommitNode newNode = new CommitNode(commit.getName(),branch.getBranchName());
                    nodeList.add(newNode);
                }
            }
        }
    }

    private void populateMergeList() {
        for (BranchStat branch : model.getBranchList()) {
            for (MergeStat merge : branch.getMergeList()) {
                for (int i = 0; i < merge.getNumSources(); i++) {
                    CommitNode target = findNode(merge.getName());
                    CommitNode source = findNode(merge.getSource(i));
                    if (source != null && target != null) {
                        MergeEdge edge = new MergeEdge(findNode(merge.getName()), findNode(merge.getSource(i)));
                        if (!mergeEdgeList.contains(edge)) {
                            mergeEdgeList.add(edge);
                        }
                    }
                }

            }
        }
    }

    private void populateCommitList() {
        for (BranchStat branch : model.getBranchList()) {
            int j = 0;
            for (int i = 1; i < branch.getCommitList().size(); i++) {
                Edge edge = new Edge(getNodeList().get(j), getNodeList().get(i));;
                if (!commitEdgeList.contains(edge)) {
                    commitEdgeList.add(edge);
                }
                j++;
            }
        }
    }

    private CommitNode findNode(String name) {
        for (CommitNode node : getNodeList()) {
            if (node.getName().equals(name)) {
                return node;
            }
        }
        return null;
    }

    private double calcX(Date commitDate) {
        int x = commitDate.compareTo(model.getEarliestDate());
        double dx;
        if (x == 0) {
            dx = 100;
        } else {
            dx = x * 100;
        }
        return dx;
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
    
    public CommitNode getNode(int index){
        return nodeList.get(index);
    }
    
    public MergeEdge getMergeEdge(int index){
        return mergeEdgeList.get(index);
    }
    
    public Edge getCmmitEdge(int index){
        return commitEdgeList.get(index);
    }
    
    private boolean containsNode(String name){
        boolean found = false;
        for(CommitNode node : nodeList){
            if(node.getName().equals(name)){
                found = true;
            }
        }
        return found;
    }
}

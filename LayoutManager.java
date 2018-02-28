/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Oli Loades
 */
public class LayoutManager {

    private GraphModel graphModel;
    private final int HGAP = 100;
    private final double NODE_WIDTH;
    private List<Layer> layerList;
    private final double SIZE;

    public LayoutManager(GraphModel g, double size) {
        graphModel = g;
        NODE_WIDTH = graphModel.getNodeRadius() * 2;
        layerList = new ArrayList<>();
        SIZE = size;
        createLayerList();
        setUp();
    }

    private void setUp() {
        moveNodes();
        connectEdges();
    }

    private void connectEdges() {
        connectCommits();
        connectMerges();
    }

    private void moveNodes() {
        double x;
        double y = SIZE / 2;
        double aboveY = y;
        double belowY = y;
        boolean aboveMaster = false;
        boolean branchChange;

        String currentBranch = graphModel.getNode(0).getBranch();

        for (CommitNode node : graphModel.getNodeList()) {
            if (!currentBranch.equals(node.getBranch())) {
                branchChange = true;
                if (aboveMaster) {
                    aboveY = aboveY + HGAP;
                    y = aboveY;
                } else {
                    belowY = belowY - HGAP;
                    y = belowY;
                }
                aboveMaster = !aboveMaster;
                currentBranch = node.getBranch();
            } else {
                branchChange = false;
            }
            x = calcX(node.getCommit().getDate(), branchChange);

            node.relocate(x, y);
        }
    }

    private void createLayerList() {
        Layer prevLayer = null;
        double start = 0;
        int numNodes;
        for (LocalDate date : sortNodeList()) {

            if (!containsLayer(date)) {
                if (prevLayer != null) {
                    Duration duration = Duration.between(prevLayer.getDate().atStartOfDay(), date.atStartOfDay());
                    long x = Math.abs(duration.toDays());
                    start = prevLayer.getEndX() + (NODE_WIDTH * x);
                }

                numNodes = getNumNodesInLayer(date);
                Layer layer = new Layer(date, NODE_WIDTH, start, numNodes);
                layerList.add(layer);
                prevLayer = layer;
            }
        }
    }

    private List<LocalDate> sortNodeList() {
        List<LocalDate> nodesSortedByDate = new ArrayList<>();
        for (CommitNode node : graphModel.getNodeList()) {
            nodesSortedByDate.add(node.getCommit().getDate());
        }
        Collections.sort(nodesSortedByDate);

        return nodesSortedByDate;
    }

    private int getNumNodesInLayer(LocalDate date) {
        int numNodes = 0;
        for (LocalDate nodeDate : sortNodeList()) {
            if (nodeDate.isEqual(date)) {
                numNodes++;
            }
        }
        return numNodes;
    }

    private void connectCommits() {
        for (Edge commitEdge : graphModel.getCommitEdgeList()) {
            commitEdge.update();
        }
    }

    private void connectMerges() {
        for (MergeEdge mergeEdge : graphModel.getMergeEdgeList()) {
            mergeEdge.update();
        }
    }

    private double calcX(LocalDate commitDate, boolean branchChange) {
        Layer layer = getLayer(commitDate);
        if (branchChange) {
            layer.resetCurrentX();
        }
        return layer.nextX();

    }

    private boolean containsLayer(LocalDate date) {
        boolean contains = false;
        for (Layer layer : layerList) {
            if (layer.getDate().equals(date)) {
                contains = true;
            }
        }
        return contains;
    }

    private Layer getLayer(LocalDate date) {
        Layer l = null;
        for (Layer layer : layerList) {
            if (layer.getDate().equals(date)) {
                l = layer;
            }
        }
        return l;
    }

}

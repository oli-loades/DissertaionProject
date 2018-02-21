/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author WinPC01
 */
public class LayoutManager {

    private GraphModel graphModel;

    public LayoutManager(GraphModel g) {
        graphModel = g;
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
        double x = 0;
        double y = 500;
        double aboveY = y;
        double belowY = y;
        boolean aboveMaster = false;
        LocalDate prevCommitDate = null;

        String currentBranch = graphModel.getNode(0).getBranch();

        for (CommitNode node : graphModel.getNodeList()) {
            if (!currentBranch.equals(node.getBranch())) {
                if (aboveMaster) {
                    aboveY = aboveY + 100;
                    y = aboveY;
                } else {
                    belowY = belowY - 100;
                    y = belowY;
                }
                aboveMaster = !aboveMaster;
                currentBranch = node.getBranch();
            }

            if (prevCommitDate != null && node.getCommit().getDate().isEqual(prevCommitDate)) {
                x = x + 10;
            } else {
                x = calcX(node.getCommit().getDate());
            }

            prevCommitDate = node.getCommit().getDate();

            node.relocate(x, y);
        }
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

    private double calcX(LocalDate commitDate) {
        LocalDate start = graphModel.getModel().getEarliestDate();
        Duration duration = Duration.between(start.atStartOfDay(), commitDate.atStartOfDay());
        long x = Math.abs(duration.toDays());
        double dx;
        if (x == 0) {
            dx = 20;
        } else {
            dx = x * 30;
        }
        return dx;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 *
 * @author Oli Loades
 */
public class Model extends Observable {

    private List<BranchStat> branches;
    private GitHandler gitHandler;
    private GraphModel graphModel;

    public Model() {
        branches = new ArrayList<>();
    }

    public void newRepo(String url) {
        gitHandler = new GitHandler(url);
        branches = gitHandler.createBranchStatList();
        graphModel = new GraphModel(this);
        setChanged();
        notifyObservers();
    }

    public BranchStat getBranch(int index) {
        return branches.get(index);
    }

    public List<BranchStat> getBranchList() {
        return branches;
    }

    public LocalDate getEarliestDate() {
        return branches.get(0).getCommit(0).getDate();
    }

    public int getNumBranches() {
        return branches.size();
    }

    public GraphModel getGraphModel() {
        return graphModel;
    }

}

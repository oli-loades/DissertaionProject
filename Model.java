/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;


import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Oli Loades
 */
public class Model {

    private List<BranchStat> branches;
    private final GitHandler gitHandler;

    public Model(String URL) {
        branches = new ArrayList<>();
        gitHandler = new GitHandler(URL);
        setUp();
    }

    private void setUp() {
        branches = gitHandler.createBranchStatList();
    }

    public BranchStat getBranch(int index) {
        return branches.get(index);
    }

    public List<BranchStat> getBranchList() {
        return branches;
    }

}

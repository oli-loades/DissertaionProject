/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;

/**
 *
 * @author WinPC01
 */
public class Model {

    private List<BranchStat> branches;
    private final GitHandler gitHandler;

    Model(String URL) {
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

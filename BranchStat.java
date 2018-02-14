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
 * @author WinPC01
 */
public class BranchStat {

    private String branchName;
    private String pathName;
    private final List<MergeStat> merges;
    private final List<CommitStat> commits;

    BranchStat(String branchName, String pathName) {
        this.branchName = branchName;
        this.pathName = pathName;
        merges = new ArrayList<>();
        commits = new ArrayList<>();
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String newBranchName) {
        branchName = newBranchName;
    }

    public void addMerge(MergeStat merge) {
        merges.add(merge);
    }

    public MergeStat getMerge(int index) {
        return merges.get(index);
    }

    public void addCommit(CommitStat newCommit) {
        commits.add(newCommit);
    }

    public CommitStat getCommit(int index) {
        return commits.get(index);
    }

    public void setCommitList(List<CommitStat> newCommitsList) {
        commits.clear();
        for (CommitStat commit : newCommitsList) {
            commits.add(commit);
        }
    }

    public List<CommitStat> getCommitList() {
        return commits;
    }

    public void removeCommit(int index) {
        commits.remove(index);
    }

    public int numCommits() {
        return commits.size();
    }

    public String getPathName() {
        return pathName;
    }

    void setPathName(String pathName) {
        this.pathName = pathName;
    }

}

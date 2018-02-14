/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import org.eclipse.jgit.api.Git;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.DepthWalk.RevWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.filter.RevFilter;

/**
 *
 * @author Oli Loades
 */
public class GitHandler {

    private Git git;

    GitHandler(String URL) {
        try {
            git = cloneRepo(URL);
        } catch (IOException | GitAPIException ex) {
            System.out.println("error: " + ex);
        }
    }

    private void cleanDir(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                cleanDir(file);
            }
            file.delete();
        }
    }

    private Git cloneRepo(String URL) throws GitAPIException, IOException {
        String directory = System.getProperty("user.dir");
        directory = directory + "\\temp";

        File localPath = new File(directory);

        cleanDir(localPath);

        try (Git tempGit = Git.cloneRepository().setURI(URL).setDirectory(localPath).setCloneAllBranches(true)
                .call()) {
            return tempGit;
        }
    }

    private String split(String branchName) {
        String nameSplit[] = branchName.split("/");
        return nameSplit[nameSplit.length - 1];
    }

    public List<BranchStat> createBranchStatList() {
        List<BranchStat> branches = new ArrayList<>();
        try {
            for (Ref branch : git.branchList().setListMode(ListMode.ALL).call()) {
                if (!branch.getName().equals("refs/remotes/origin/master")) {
                    String name = split(branch.getName());
                    BranchStat newBranch = new BranchStat(name, branch.getName());
                    newBranch = createCommitList(newBranch);
                    newBranch = createMergeList(newBranch);
                    branches.add(newBranch);
                }
            }
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
        return branches;
    }

    private String findLatestCommit(String branchPath) throws IOException, GitAPIException {
        Repository repository = git.getRepository();
        List<RevCommit> commits = new ArrayList<>();
        try {
            for (RevCommit commit : git.log().add(repository.resolve(branchPath)).call()) {
                commits.add(commit);
            }
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
        return commits.get(0).getName();
    }

    public void printMerges() throws GitAPIException, IOException {
        Repository repository = git.getRepository();
        for (Ref branch : git.branchList().setListMode(ListMode.ALL).call()) {
            System.out.println(branch.getName());
            for (RevCommit commit : git.log().add(repository.resolve(branch.getName())).setRevFilter(RevFilter.ONLY_MERGES).call()) {
                System.out.println(commit.getName());
                System.out.println(commit.getAuthorIdent().getWhen());
            }
            System.out.println("");
        }
    }

    public void printAllCommits() throws GitAPIException, IOException {
        Repository repository = git.getRepository();
        for (Ref branch : git.branchList().setListMode(ListMode.ALL).call()) {
            System.out.println(branch.getName());
            for (RevCommit commit : git.log().not(repository.resolve("master")).add(repository.resolve(branch.getName())).call()) {
                System.out.println(commit.getName());
            }
            System.out.println("");
        }
    }

    private BranchStat createMergeList(BranchStat branch) throws GitAPIException, IOException {
        try {
            Repository repository = git.getRepository();
            ObjectId id = repository.resolve(findLatestCommit(branch.getPathName()));
            RevWalk revWalk = new RevWalk(repository, 0);
            RevCommit head = revWalk.parseCommit(id);
            RevCommit current = head;
            while (current.getParentCount() != 0) {
                if (current.getParentCount() >= 2) {
                    MergeStat newMerge = new MergeStat(current.getName(), current.getAuthorIdent().getWhen());
                    for (int i = 1; i < current.getParentCount(); i++) {
                        newMerge.addSource(current.getParent(i).getName());
                    }
                    branch.addMerge(newMerge);
                }
                current = revWalk.parseCommit(current.getParent(0).getId());
            }
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
        return branch;
    }

    private BranchStat createCommitList(BranchStat branch) throws GitAPIException, IOException {
        try {
            Repository repository = git.getRepository();
            ObjectId id = repository.resolve(findLatestCommit(branch.getPathName()));
            RevWalk revWalk = new RevWalk(repository, 0);
            RevCommit head = revWalk.parseCommit(id);

            List<CommitStat> commits = new ArrayList<>();
            RevCommit current = head;
            CommitStat newCommit;

            while (current.getParentCount() != 0) {
                newCommit = new CommitStat(current.getName(), current.getAuthorIdent().getWhen());
                commits.add(newCommit);
                current = revWalk.parseCommit(current.getParent(0).getId());
            }

            if (current.getParentCount() == 0 && !commits.isEmpty()) {//root commit
                newCommit = new CommitStat(current.getName(), current.getAuthorIdent().getWhen());
                commits.add(newCommit);
                Collections.reverse(commits);
            } else if (commits.isEmpty()) {//only commit
                newCommit = new CommitStat(head.getName(), head.getAuthorIdent().getWhen());
                commits.add(newCommit);
            }

            branch.setCommitList(commits);
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
        return branch;
    }

}

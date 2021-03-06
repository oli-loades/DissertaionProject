/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import org.eclipse.jgit.api.Git;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.eclipse.jgit.api.ListBranchCommand.ListMode;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.DepthWalk.RevWalk;
import org.eclipse.jgit.revwalk.RevCommit;

/**
 *
 * @author Oli Loades
 */
public class GitHandler {

    private Git git;

    public GitHandler(URL url) {
        try {

            git = cloneRepo(url);
            git.getRepository().close();
            git.close();
        } catch (IOException | GitAPIException ex) {
            System.out.println("error: " + ex);
        }
    }

    public void forceClose() {

        git.getRepository().close();
        git.close();
        git.gc();
        git = null;
    }

    private void cleanDir(File dir) {
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                cleanDir(file);
            }
            file.delete();
        }
    }

    private Git cloneRepo(URL url) throws GitAPIException, IOException {
        String directory = System.getProperty("user.dir");
        directory = directory + "\\temp";

        File localPath = new File(directory);

        cleanDir(localPath);//fdletes existing git

        try (Git tempGit = Git.cloneRepository().setURI(url.toString()).setDirectory(localPath).setCloneAllBranches(true)
                .call()) {
            tempGit.getRepository().close();
            tempGit.close();
            return tempGit;
        }
    }

    public boolean isEmpty() {
        boolean empty = git.getRepository().isBare();
        git.getRepository().close();
        git.close();
        return empty;
    }

    private String getName(String name) {
        String nameSplit[] = name.split("/");
        return nameSplit[nameSplit.length - 1];
    }

    public List<BranchStat> createBranchStatList() {
        List<BranchStat> branches = new ArrayList<>();
        try {
            for (Ref branch : git.branchList().setListMode(ListMode.ALL).call()) {
                if (!branch.getName().equals("refs/remotes/origin/master")) {
                    String name = getName(branch.getName());
                    BranchStat newBranch = new BranchStat(name, branch.getName());
                    newBranch = createCommitList(newBranch);
                    newBranch = createMergeList(newBranch);
                    branches.add(newBranch);
                }
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        git.getRepository().close();
        git.close();
        return branches;
    }

    private String findLatestCommit(String branchPath) throws GitAPIException, IOException {
        List<RevCommit> commits = new ArrayList<>();
        try (Repository repository = git.getRepository()) {
            ObjectId id = repository.resolve(branchPath);
            for (RevCommit commit : git.log().add(id).call()) {
                commits.add(commit);
            }

            git.getRepository().close();
            git.close();

        }
        return commits.get(0).getName();//first commit
    }// 

    private BranchStat createMergeList(BranchStat branch) {
        try (Repository repository = git.getRepository()) {
            RevWalk revWalk = new RevWalk(repository, 0);
            ObjectId id = repository.resolve(findLatestCommit(branch.getPathName()));//issue here
            RevCommit current = revWalk.parseCommit(id); //issue here
            while (current.getParentCount() != 0) {//is not first commit
                if (current.getParentCount() >= 2) {//is merge
                    MergeStat newMerge = new MergeStat(current.getName(), convertDate(current.getAuthorIdent().getWhen()));
                    for (int i = 1; i < current.getParentCount(); i++) {
                        newMerge.addSource(current.getParent(i).getName());//adds each parent
                    }
                    branch.addMerge(newMerge);
                }
                current = revWalk.parseCommit(current.getParent(0).getId());
            }

        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }

        git.getRepository().close();
        git.close();
        return branch;
    }// | GitAPIException

    private BranchStat createCommitList(BranchStat branch) {
        try (Repository repository = git.getRepository()) {
            ObjectId id = repository.resolve(findLatestCommit(branch.getPathName()));

            try (RevWalk revWalk = new RevWalk(repository, 0)) {
                List<CommitStat> commits = new ArrayList<>();
                RevCommit head = revWalk.parseCommit(id);//first commit
                RevCommit current = head;
                CommitStat newCommit;
                while (current.getParentCount() != 0) {//has parent
                    newCommit = new CommitStat(current.getName(), convertDate(current.getAuthorIdent().getWhen()));
                    commits.add(newCommit);
                    current = revWalk.parseCommit(current.getParent(0).getId());//gets first parewnts
                }
                if (current.getParentCount() == 0 && !commits.isEmpty()) {//root commit
                    newCommit = new CommitStat(current.getName(), convertDate(current.getAuthorIdent().getWhen()));
                    commits.add(newCommit);
                    Collections.reverse(commits);
                } else if (commits.isEmpty()) {//only commit
                    newCommit = new CommitStat(head.getName(), convertDate(head.getAuthorIdent().getWhen()));
                    commits.add(newCommit);
                }

                branch.setCommitList(commits);
            }
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }

        git.getRepository().close();
        git.close();
        return branch;
    }

    private LocalDate convertDate(Date commitDate) {
        return commitDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}

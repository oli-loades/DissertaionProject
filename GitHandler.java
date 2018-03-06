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
import org.eclipse.jgit.revwalk.filter.RevFilter;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.internal.storage.file.WindowCache;
import org.eclipse.jgit.storage.file.WindowCacheConfig;

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
        //       File f = new File(fileName);
//        try {
//            FileUtils.cleanDirectory(f); //clean out directory (this is optional -- but good know)
//            FileUtils.forceDelete(f); //delete directory
//            FileUtils.forceMkdir(f); //create directory
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return f;
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

        cleanDir(localPath);

        try (Git tempGit = Git.cloneRepository().setURI(url.toString()).setDirectory(localPath).setCloneAllBranches(true)
                .call()) {
            tempGit.getRepository().close();
            tempGit.close();
            return tempGit;
        }
    }
    
    public boolean isEmpty(){
        boolean empty = git.getRepository().isBare();
        git.getRepository().close();
        git.close();
        return empty;
    }

    private String getName(String name, String seperator, int num) {
        String nameSplit[] = name.split(seperator);
        return nameSplit[nameSplit.length - num];
    }

    public List<BranchStat> createBranchStatList() {
        List<BranchStat> branches = new ArrayList<>();
        try {
            for (Ref branch : git.branchList().setListMode(ListMode.ALL).call()) {
                if (!branch.getName().equals("refs/remotes/origin/master")) {
                    String name = getName(branch.getName(), "/", 1);
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
       // return null;
         return branches;
    }

    private String findLatestCommit(String branchPath) {
        List<RevCommit> commits = new ArrayList<>();
        try (Repository repository = git.getRepository()) {
            //   ObjectId id = repository.resolve("d4cf0d622116f218d9df7276e54335e6a674da75") ;
            ObjectId id = repository.resolve(branchPath);//
            Iterable<RevCommit> logs = git.log().add(id).call();//issue is here
            for (RevCommit commit : logs) {
               commits.add(commit);          
            }
            git.getRepository().close();
            git.close();
            
            
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }

          return commits.get(0).getName();
      //  return "d4cf0d622116f218d9df7276e54335e6a674da75";
    }// 

    public void printMerges() throws GitAPIException, IOException {
        try (Repository repository = git.getRepository()) {
            for (Ref branch : git.branchList().setListMode(ListMode.ALL).call()) {
                System.out.println(branch.getName());
                for (RevCommit commit : git.log().add(repository.resolve(branch.getName())).setRevFilter(RevFilter.ONLY_MERGES).call()) {
                    System.out.println(commit.getName());
                    System.out.println(commit.getAuthorIdent().getWhen());
                }
                System.out.println("");
            }
        }
        git.getRepository().close();
        git.close();
    }

    public void printAllCommits() throws GitAPIException, IOException {
        try (Repository repository = git.getRepository()) {
            for (Ref branch : git.branchList().setListMode(ListMode.ALL).call()) {
                System.out.println(branch.getName());
                for (RevCommit commit : git.log().not(repository.resolve("master")).add(repository.resolve(branch.getName())).call()) {
                    System.out.println(commit.getName());
                }
                System.out.println("");
            }
        }
        git.getRepository().close();
        git.close();
    }

    private BranchStat createMergeList(BranchStat branch) {
        try (Repository repository = git.getRepository()) {
            RevWalk revWalk = new RevWalk(repository, 0);
            ObjectId id = repository.resolve(findLatestCommit(branch.getPathName()));//issue here
           //  ObjectId id = repository.resolve("d4cf0d622116f218d9df7276e54335e6a674da75");
            RevCommit current = revWalk.parseCommit(id); //issue here
                while (current.getParentCount() != 0) {
                    if (current.getParentCount() >= 2) {
                        MergeStat newMerge = new MergeStat(current.getName(), convertDate(current.getAuthorIdent().getWhen()));
                        for (int i = 1; i < current.getParentCount(); i++) {
                            newMerge.addSource(current.getParent(i).getName());
                        }
                        branch.addMerge(newMerge);
                    }
                    current = revWalk.parseCommit(current.getParent(0).getId());
                }

        } catch (IOException e) {
            e.printStackTrace();
        }

        //}
        return branch;
    }// | GitAPIException

    private BranchStat createCommitList(BranchStat branch) {
        try (Repository repository = git.getRepository()) {

            ObjectId id = repository.resolve(findLatestCommit(branch.getPathName()));
            RevWalk revWalk = new RevWalk(repository, 0);
            RevCommit head = revWalk.parseCommit(id);

            List<CommitStat> commits = new ArrayList<>();
            RevCommit current = head;
            CommitStat newCommit;

            while (current.getParentCount() != 0) {
                newCommit = new CommitStat(current.getName(), convertDate(current.getAuthorIdent().getWhen()));
                current.getAuthorIdent().getTimeZone();
                commits.add(newCommit);
                current = revWalk.parseCommit(current.getParent(0).getId());
            }

            if (current.getParentCount() == 0 && !commits.isEmpty()) {//root commit
                newCommit = new CommitStat(current.getName(), convertDate(current.getAuthorIdent().getWhen()));
                commits.add(newCommit);
                Collections.reverse(commits);
            } else if (commits.isEmpty()) {//only commit
                newCommit = new CommitStat(head.getName(), convertDate(head.getAuthorIdent().getWhen()));
                commits.add(newCommit);
            }
            revWalk.close();
            branch.setCommitList(commits);
        } catch (IOException e) {
            e.printStackTrace();
        }

        git.getRepository().close();
        git.close();
        return branch;
    }// | GitAPIException

    private LocalDate convertDate(Date commitDate) {
        return commitDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

}

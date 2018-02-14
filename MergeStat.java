        /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.eclipse.jgit.lib.ObjectId;

/**
 *
 * @author WinPC01
 */
public class MergeStat extends CommitStat{

   
    private List<String> sources;

    MergeStat(String name, Date date) {
        super(name,date);
        sources = new ArrayList<>();
    }

    public String getParent(int index) {
        return sources.get(index);
    }

    public void addParent(String parent) {
        sources.add(parent);
    }

    public int getNumParent() {
        return sources.size();
    }
}

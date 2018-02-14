        /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Oli Loades
 */
public class MergeStat extends CommitStat {

    private List<String> sources;

    public MergeStat(String name, Date date) {
        super(name, date);
        sources = new ArrayList<>();
    }

    public String getSource(int index) {
        return sources.get(index);
    }

    public void addSource(String parent) {
        sources.add(parent);
    }
    
    public void setSourceList(List<String> newSourceList){
        sources = newSourceList;
    }
    
    public int getNumSources() {
        return sources.size();
    }
}

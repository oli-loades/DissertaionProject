/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

/**
 *
 * @author Oli Loades
 */
public class Controller {
    private View view;
    private Model model;
    
    public Controller(View v, Model m){
        view = v;
        model = m;
    }
    
    public void createNewRepo(String url){
        model.newRepo(url);
        view.enableButtons();
        close();
    }
    
    public void close(){
        view.closePopUp();
    }

    void createNewRepoPopUp() {
       view.createNewRepoPopUp();
    }

    void createKeyPopUp() {
       view.createKeyPopUp();
    }
}

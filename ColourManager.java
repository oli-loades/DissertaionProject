/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javafx.scene.paint.Color;

/**
 *
 * @author Oli Loades
 */
public class ColourManager {
    private final List<Color> colours;
    private ListIterator colourIter;
    private final int numColours;
    private final double BRIGHTNESS = 1.0;
    private final double SATURATION = 1.0;
    private final double UPPER = 360.0;
    
    public ColourManager(int numColours){
        this.numColours = numColours;
        colours = new ArrayList<>();
        generateColours();
        colourIter = colours.listIterator();  
    }
    
    private void generateColours(){
        double hue = 0;
        double gap = UPPER / (double) numColours;
        for(int i = 0; i< numColours;i++){
            hue = hue + gap;
            colours.add(Color.hsb(hue, SATURATION, BRIGHTNESS));
        }
    }
    
    public Color getNextColour(){
        return (Color) colourIter.next();
    }
}

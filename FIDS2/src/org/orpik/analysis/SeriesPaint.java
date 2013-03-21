/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.analysis;

import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Chemweno
 */
public class SeriesPaint {

    private ArrayList<Color> seriesColors = new ArrayList<>();
    
    public SeriesPaint() {
        try{
        //Sky blue color for average bars
        seriesColors.add(new Color(176, 196, 240)); 
        //Red for first series
        seriesColors.add(new Color(255,0 ,0));
        //Blue for second series
        seriesColors.add(new Color(0, 0, 255));
        //Green for third series
        seriesColors.add(new Color(0, 255, 0));
        //Magenta for fourth series
        seriesColors.add(Color.magenta);
        //Pink for fifth series
        seriesColors.add(new Color(138, 43, 226));
        //Additional
        seriesColors.add(new Color(255, 102, 51));
        //More
        seriesColors.add(new Color(163, 234, 98));
        }catch(Exception exception){
        exception.printStackTrace();
        }
    }
    
     public Color getSeriesColor(int index) {
        return seriesColors.get(index);
    }

    public void setSeriesColor(Color color) {
        seriesColors.add(color);
    }    
}

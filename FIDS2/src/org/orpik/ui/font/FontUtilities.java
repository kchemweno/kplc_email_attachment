/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.orpik.ui.font;

import java.awt.Font;

/**
 *
 * @author chemweno
 */
public class FontUtilities {

    private String[] splitRgbColor = {};
    private int rColor = 0;
    private int gColor = 0;
    private int bColor = 0;
    private java.awt.Font font = null;

    private String[] splitRgbCode(String rgbColor) {
        try {
            splitRgbColor = rgbColor.split(",");
        } catch (Exception exception) {
        }
        return splitRgbColor;
    }

    public int getRColor(String rgbColor) {
        try {
            rColor = Integer.parseInt(rgbColor.split(",")[0]);
        } catch (Exception exception) {
        }
        return rColor;
    }

    public int getGColor(String rgbColor) {
        try {
            gColor = Integer.parseInt(rgbColor.split(",")[1]);
        } catch (Exception exception) {
        }
        return gColor;
    }

    public int getBColor(String rgbColor) {
        try {
            bColor = Integer.parseInt(rgbColor.split(",")[2]);
        } catch (Exception exception) {
        }
        return bColor;
    }

    public int getFontStyle(int bold, int italic) {
        int style = 0;
        try {
            if (bold == 0 && italic == 0) {
                style = Font.PLAIN;
            } else if (bold == 0 && italic == 1) {
                style = Font.ITALIC;
            } else if (bold == 1 && italic == 0) {
                style = Font.BOLD;
            } else if (bold == 1 && italic == 1) {
                style = Font.BOLD | Font.ITALIC;
            }
        } catch (Exception exception) {
        }
        return style;
    }
    
  public java.awt.Font getFont(String fontName, int bold, int italic, int fontsize){
      try{          
         font =  new Font(fontName, getFontStyle(bold, italic), fontsize);
      }catch(Exception exception){}
      return font;
  } 
  
}

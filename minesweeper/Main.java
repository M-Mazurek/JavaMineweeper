package minesweeper;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import java.awt.Font;
import java.util.Enumeration;

class Main {
    public static void setUIFont (FontUIResource f){
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof FontUIResource)
                UIManager.put(key, f);
            }
        } 

    public static void main(String[] args) {
        setUIFont(new FontUIResource("Ebrima", Font.BOLD, 12));
        new Minesweeper();
    }
}
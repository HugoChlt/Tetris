package tetris;

//CHOULOT Hugo

import java.awt.event.*;
import javax.swing.*;

public class Difficulty extends JButton implements ActionListener{

    public enum Level {
        Easy, Medium, Hard, Legendary;
    }

    public Difficulty(){
        this.setText(""+Game.mod);
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e){

        switch (Game.mod){
            case Easy :
                Game.mod=Level.Medium;
                Game.main(null);
                break;
            case Medium :
                Game.mod=Level.Hard;
                Game.main(null);
                break;
            case Hard :
                Game.mod=Level.Legendary;
                Game.main(null);
                break;
            case Legendary :
                Game.mod=Level.Easy;
                Game.main(null);
                break;
        }

        this.setText(""+Game.mod);
    }
}

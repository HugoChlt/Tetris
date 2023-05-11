package tetris;

//CHOULOT Hugo

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Game extends JFrame{

    public static Difficulty.Level mod;

    private JPanel labels;
    private JPanel buttons;

    private JLabel lines;
    private JLabel counter;
    private JLabel score;
    private JLabel paused;

    private JButton restart;
    private JButton mode;
    private JButton close;

    int count;

    Timer timer;

    public Game() {

        GUI();

    }

    private void GUI() {

        mod = getMode();

        paused = new JLabel();
        add(paused, BorderLayout.NORTH);

        labels = new JPanel();
        lines = new JLabel(" Lines : 0 ");
        score = new JLabel(" Score = 0 ");
        counter = new JLabel();
        timer = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                count++;
                if (count < 100000) {
                    counter.setText(" Time : "+Integer.toString(count)+" s ");
                } else {
                    ((Timer) (e.getSource())).stop();
                }
            }
        });
        timer.setInitialDelay(0);
        timer.start();
        labels.add(counter);
        labels.add(lines);
        labels.add(score);
        labels.setLayout(new GridLayout(3,1));
        add(labels, BorderLayout.EAST);

        buttons = new JPanel();
        close = new JButton("X");
        restart = new JButton("Restart");
        mode = new Difficulty();
        buttons.add(restart);
        buttons.add(mode);
        buttons.add(close);
        add(buttons, BorderLayout.SOUTH);

        var grid = new Grid(this);
        add(grid);
        grid.setBackground(Color.BLACK);

        switch(mod){
            case Easy :
                grid.start(800);
                break;
            case Medium :
                grid.start(600);
                break;
            case Hard :
                grid.start(400);
                break;
            case Legendary :
                grid.start(200);
                break;
        }

        restart.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                main(null);
            }
        });

        close.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });

        setTitle("Tetris");
        setSize(588, 892);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    JLabel getLines() {

        return lines;
    }

    JLabel getPaused() {

        return paused;
    }

    JLabel getScore() {

        return score;
    }

    Difficulty.Level getMode(){
        if(mod == null){
            mod = Difficulty.Level.Easy;
        }
        return mod;
    }
    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            var game = new Game();
            game.setVisible(true);
        });
    }
}

package minesweeper;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class Minesweeper extends JFrame {
    Minefield mf;
    int height = 20;
    int width = 20;
    int difficulty = 1;

    class Theme {
        public Color bg;
        public Color tileVis;
        public Color tileInvis;
        public Color nums;
        public Color win;
        public Color lost;

        public Theme (Color bg, Color tileVis, Color tileInvis, Color nums, Color win, Color lost) {
            this.bg = bg;
            this.tileVis = tileVis;
            this.tileInvis = tileInvis;
            this.nums = nums;
            this.win = win;
            this.lost = lost;
        }
    }

    final Theme classicTheme = new Theme(Color.DARK_GRAY, Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY, Color.GREEN, Color.RED);
    final Theme darkTheme = new Theme(Color.decode("#171010"), Color.decode("#2B2B2B"), Color.decode("#241818"), Color.decode("#423F3E"), Color.decode("#B8600B"), Color.decode("#4E0003"));

    public Minesweeper() throws ArrayIndexOutOfBoundsException {
        setBounds(100, 100, 900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);
        setResizable(false);
        setTitle("Minesweeper");
        
        newGame();

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu options = new JMenu("Opcje");
        menuBar.add(options);

        JMenuItem newGame = new JMenuItem("Nowa Gra", KeyEvent.VK_N);
        options.add(newGame);

        JMenuItem size = new JMenuItem("Zmień planszę");
        options.add(size);

        JMenu diffMenu = new JMenu("Zmień poziom trudności");
        ButtonGroup diffGroup = new ButtonGroup();
        JRadioButtonMenuItem easyDiff = new JRadioButtonMenuItem("Łatwy");
        JRadioButtonMenuItem mediumDiff = new JRadioButtonMenuItem("Średni");
        JRadioButtonMenuItem hardDiff = new JRadioButtonMenuItem("Trudny");
        diffGroup.add(easyDiff);
        diffGroup.add(mediumDiff);
        diffGroup.add(hardDiff);
        diffMenu.add(easyDiff);
        diffMenu.add(mediumDiff);
        diffMenu.add(hardDiff);
        easyDiff.setSelected(true);
        options.add(diffMenu);

        JMenu theme = new JMenu("Zmień motyw");
        ButtonGroup themeGroup = new ButtonGroup();
        JRadioButtonMenuItem classicThemeItem = new JRadioButtonMenuItem("Klasyczny");
        JRadioButtonMenuItem darkThemeItem = new JRadioButtonMenuItem("Ciemny");
        themeGroup.add(classicThemeItem);
        themeGroup.add(darkThemeItem);
        theme.add(classicThemeItem);
        theme.add(darkThemeItem);
        classicThemeItem.setSelected(true);
        options.add(theme);
        
        newGame.addActionListener(e -> newGame());

        size.addActionListener(e -> {
            int w = 0, h = 0;
            do {
                try {
                    w = Integer.parseInt(JOptionPane.showInputDialog(null, "Podaj szerokość (min. 20)(max. 100)"));
                } catch (NumberFormatException nfe) {
                    continue;
                }
            } while (w < 20 || w > 100);
            do {
                try {
                    h = Integer.parseInt(JOptionPane.showInputDialog(null, "Podaj wysokość (min. 10)(max. 40)"));
                } catch (NumberFormatException nfe) {
                    continue;
                }
            } while (h < 10 || h > 40);
            width = w;
            height = h;
            newGame();
        });

        easyDiff.addActionListener(e -> {
            difficulty = 1;
            newGame();
        });

        mediumDiff.addActionListener(e -> {
            difficulty = 2;
            newGame();
        });

        hardDiff.addActionListener(e -> {
            difficulty = 3;
            newGame();
        });

        classicThemeItem.addActionListener(e -> mf.setTheme(classicTheme));
        darkThemeItem.addActionListener(e -> mf.setTheme(darkTheme));

        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));

        mf.setBounds(0, 0, getWidth(), getHeight());
        add(mf);

        setVisible(true);
    }

    void newGame() {
        Theme temp = classicTheme;
        try {
        remove(mf);
        temp = mf.theme;
        } catch (NullPointerException npe) {}
        mf = new Minefield(width, height, (JFrame)this, difficulty * 7);
        mf.setTheme(temp);
        add(mf);
        mf.repaint();
    }
}

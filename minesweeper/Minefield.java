package minesweeper;

import javax.swing.*;
import javax.swing.event.MouseInputListener;

import minesweeper.Minesweeper.Theme;

import javax.imageio.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Minefield extends JPanel {
    int x, y;

    final int tileSize = 20;
    final int margin = 2;
    int[][] field;
    boolean[][] fieldVis;
    List<Dimension> flaggedTiles;
    Theme theme;
    boolean pressedMine = false;

    public Minefield(int x, int y, JFrame frame, int mines) {
        this.x = x;
        this.y = y;
        mines = x * y * mines / 100;
        field = new int[x][y];
        fieldVis = new boolean[x][y];
        flaggedTiles = new ArrayList<Dimension>();

        frame.setBounds(frame.getX(), 
                        frame.getY(), 
                        tileSize * x + margin * x + margin + 16, 
                        tileSize * y + margin * y + margin + 62);
        setBounds(0, 0, frame.getWidth(), frame.getHeight());

        for (int i = 0; i < mines; i++) {
            int rX, rY;
            do {
                rX = new Random().nextInt(x);
                rY = new Random().nextInt(y);
            } while (field[rX][rY] == 9);
            field[rX][rY] = 9;
        }

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                if (field[i][j] == 9) 
                    continue;
                int v = 0;
                for (int _x = -1; _x < 2; _x++) {
                    for (int _y = -1; _y < 2; _y++) {
                        try {
                            if (field[i + _x][j + _y] == 9)
                                v++;
                        } catch (Exception e) {}
                    }
                }
                field[i][j] = v;
            }
        }

        addMouseListener(new MouseInputListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {
                if (hasEnded()) return;
                int xPos = (int)Math.ceil(e.getX() / (tileSize + margin));
                int yPos = (int)Math.ceil(e.getY() / (tileSize + margin));
                if (e.getButton() == MouseEvent.BUTTON1) {
                    RevealTile(xPos, yPos);
                } 
                else if (e.getButton() == MouseEvent.BUTTON3) {
                    if (fieldVis[xPos][yPos]) return;
                    if (flaggedTiles.contains(new Dimension(xPos, yPos)))
                        flaggedTiles.remove(new Dimension(xPos, yPos));
                    else 
                        flaggedTiles.add(new Dimension(xPos, yPos));
                }
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
            @Override
            public void mouseDragged(MouseEvent e) {}
            @Override
            public void mouseMoved(MouseEvent e) {}
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(theme.bg);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < y; i++) {
            for (int j = 0; j < x; j++) {
                int xD = j * margin + j * tileSize + margin;
                int yD = i * margin + i * tileSize + margin;
                if (!fieldVis[j][i]) {
                    g.setColor(hasEnded() ? theme.win : theme.tileInvis);
                    if (pressedMine) {
                        if (flaggedTiles.contains(new Dimension(j, i)))
                            g.setColor(field[j][i] == 9 ? theme.win : theme.lost);
                        else {
                            g.setColor(field[j][i] == 9 ? theme.lost : theme.tileInvis);
                        }

                    }
                    g.fillRect(xD, yD, tileSize, tileSize);
                    if (flaggedTiles.contains(new Dimension(j, i))) {
                        g.drawImage(ImageFromFile("flag.png"), xD, yD, null);
                    } else if (field[j][i] == 9 && hasEnded()) {
                        g.drawImage(ImageFromFile("mine.png"), xD, yD, null);
                    }
                }
                else {
                    g.setColor(theme.tileVis);
                    g.fillRect(xD, yD, tileSize, tileSize);
                    if (field[j][i] == 9) {
                        if (pressedMine) {
                            g.setColor(theme.lost);
                            g.fillRect(xD, yD, tileSize, tileSize);
                        }
                        g.drawImage(ImageFromFile("mine.png"), xD, yD, null);
                    }
                    else if (field[j][i] == 0) {
                        for (int _x = -1; _x < 2; _x++) {
                            for (int _y = -1; _y < 2; _y++) {
                                try {
                                    if (!fieldVis[j + _x][i + _y]) 
                                        RevealTile(j + _x, i + _y);
                                } catch (Exception e) {}
                            }
                        }
                    }
                    else {
                        g.setColor(theme.nums);
                        g.setFont(new Font("Impact", Font.PLAIN, 20));
                        g.drawString(String.valueOf(field[j][i]), 
                                    j * tileSize + j * margin + margin + tileSize / 4, 
                                    i * tileSize + i * margin + margin + tileSize - 2);
                    }
                }
            }
        }
    }

    private void RevealTile(int xPos, int yPos) {
        if (flaggedTiles.contains(new Dimension(xPos, yPos))) 
            return;
        fieldVis[xPos][yPos] = true;
        if (field[xPos][yPos] == 9) 
            pressedMine = true;
        repaint();
    }

    private boolean hasEnded() {
        if (pressedMine) return true;
        for (int x = 0; x < this.x; x++) {
            for (int y = 0; y < this.y; y++) {
                if (!fieldVis[x][y] && field[x][y] != 9)
                    return false;
            }
        }
        return true;
    }

    private BufferedImage ImageFromFile(String path) {
        try {
            return ImageIO.read(new File(new File("").getAbsolutePath().concat("\\images\\" + path)));
        } catch (IOException e) {
            System.out.println("Can't find file at path: " + new File("").getAbsolutePath().concat("\\images\\" + path));
            return null;
        }
    }

    public void setTheme(Theme theme) {
        this.theme = theme;
        repaint();
    }
}

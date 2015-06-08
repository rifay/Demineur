/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;

/**
 *
 * @author rifayath.david
 */
public class GridBoard extends Observable {

    public final int PARTIE_EN_COURS = 0;
    public final int GAME_OVER = -1;
    public final int PARTIE_GAGNE = 1;

    Case[][] grille;
    int lenght;
    int height;
    Map<Object, Point> coordonnesMap;
    long tempsDebut;
    long tempsActuel;
    int nbBombes;
    int nbCases;
    int cptUsedCase;
    int etatPartie;

    public GridBoard(int height, int lenght) {
        this.height = height;
        this.lenght = lenght;
        cptUsedCase = 0;
        coordonnesMap = new HashMap<Object, Point>();
        tempsDebut = System.currentTimeMillis();
        grille = new Case[height][lenght];
        etatPartie = PARTIE_EN_COURS;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < lenght; j++) {
                grille[i][j] = new Case(this);
                coordonnesMap.put(grille[i][j], new Point(j, i));
            }
        }
        nbCases = this.lenght * this.height;
        nbBombes = lenght - 1;
        int i = 0;
        while (i < nbBombes) {
            Random rnd = new Random();
            int x = rnd.nextInt(lenght);
            int y = rnd.nextInt(height);
            if (!grille[y][x].getBombe()) {
                grille[y][x].setBombe(true);
                i++;
            }
        }

    }

    public void updateValue(int x, int y) {
        int process = grille[x][y].checkCase();
        if (process == -1) {
            etatPartie = GAME_OVER;
        } else if (getNbCases() - getCptUsedCase() == getNbBombes()) {
            System.out.println("Gagné !");
            etatPartie= PARTIE_GAGNE;
        } else if (process == 0) {
            etatPartie = PARTIE_EN_COURS;
        }
        setChanged();
        notifyObservers();
    }

    public List getVoisins(Case c) {
        List<Case> listVoisins = new ArrayList();
        Point p = coordonnesMap.get(c);
        if (p != null) {
            int debX = (int) (p.getX() - 1 < 0 ? 0 : p.getX() - 1);
            int finX = (int) (p.getX() + 1 >= lenght ? lenght - 1 : 1 + p.getX());
            int debY = (int) (p.getY() - 1 < 0 ? 0 : p.getY() - 1);
            int finY = (int) (p.getY() + 1 >= height ? height - 1 : p.getY() + 1);

            for (int i = debY; i <= finY; i++) {
                for (int j = debX; j <= finX; j++) {
                    if (i != p.getY() || j != p.getX()) {
                        listVoisins.add(grille[i][j]);
                    }
                }
            }
        } else {
            System.err.println("Element non retrouvé dans l'hashmap !");
            return null;
        }

        return listVoisins;
    }

    public List<Case> getVoisinsDePropagation(Case c) {
        List<Case> listPropagationVoisins = new ArrayList();
        Point p = coordonnesMap.get(c);
        if (p != null) {
            int wX = (int) (p.getX() - 1);
            int eX = (int) (p.getX() + 1);
            int nY = (int) (p.getY() - 1);
            int sY = (int) (p.getY() + 1);

            if (wX >= 0) {
                listPropagationVoisins.add(grille[(int) p.getY()][wX]);
            }
            if (eX < lenght) {
                listPropagationVoisins.add(grille[(int) p.getY()][eX]);
            }
            if (sY < height) {
                listPropagationVoisins.add(grille[sY][(int) p.getX()]);
            }
            if (nY >= 0) {
                listPropagationVoisins.add(grille[nY][(int) p.getX()]);
            }
        } else {
            System.err.println("Element non retrouvé dans l'hashmap !");
            return null;
        }

        return listPropagationVoisins;

    }

    public void updateFlag(int x, int y) {
        if (grille[x][y].getStatus() == Case.CASE_NOUVELLE) {
            grille[x][y].setStatus(Case.CASE_DRAPEAU);
            this.nbBombes = nbBombes - 1;
        } else if (grille[x][y].getStatus() == Case.CASE_DRAPEAU) {
            grille[x][y].setStatus(Case.CASE_INTERROGATIVE);
            this.nbBombes = nbBombes + 1;
        } else if (grille[x][y].getStatus() == Case.CASE_INTERROGATIVE) {
            grille[x][y].setStatus(Case.CASE_NOUVELLE);
            this.nbBombes = nbBombes + 1;
        }
        setChanged();
        notifyObservers();
    }

    public boolean incrementTime() {
        tempsActuel = System.currentTimeMillis() - tempsDebut;
        if (tempsActuel >= 120000) {
            return false;
        }
        setChanged();
        notifyObservers();
        return true;
    }

    public long getTimeActuel() {
        return tempsActuel;
    }

    public void startChrono() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                boolean process = true;
                while (process) {
                    process = incrementTime();
                }

            }
        });
        t.start();
    }

    public int getNbCases() {
        return nbCases;
    }

    public void setNbCases(int nbCases) {
        this.nbCases = nbCases;
    }

    public int getNbBombes() {
        return nbBombes;
    }

    public void setNbBombes(int nbBombes) {
        this.nbBombes = nbBombes;
    }

    public int getCptUsedCase() {
        return cptUsedCase;
    }

    public void incrementCptUsedCase() {
        this.cptUsedCase = this.cptUsedCase + 1;
    }

    public Case[][] getGrille() {
        return grille;
    }

    public int getEtatPartie() {
        return etatPartie;
    }

    public void setEtatPartie(int etatPartie) {
        this.etatPartie = etatPartie;
    }

}

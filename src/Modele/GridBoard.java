/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rifayath.david
 */
public class GridBoard extends Observable implements Serializable {

    public final int PARTIE_EN_COURS = 0;
    public final int GAME_OVER = -1;
    public final int PARTIE_GAGNE = 1;
    public final int NB_BOMBES;
    Case[][] grille;
    int lenght;
    int height;
    Map<Object, Point> coordonnesMap;
    long tempsDebut;
    long tempsActuel;
    int nbFlagLeft;
    int nbCases;
    int cptUsedCase;
    int etatPartie;
    Thread chronoThread;
    Map<String, List<Score>> allScore;
    List<Score> facileScore = null;
    List<Score> moyenScore = null;
    List<Score> difficileScore = null;
    int niveau;

    public GridBoard(int niveauPartie) {
        this.allScore = new HashMap<String, List<Score>>();
        niveau = niveauPartie;
        if (niveau == 1) {
            height = 9;
            lenght = 9;
            NB_BOMBES = 10;
        } else if (niveau == 2) {
            height = 16;
            lenght = 16;
            NB_BOMBES = 40;
        } else {
            height = 30;
            lenght = 16;
            NB_BOMBES = 99;
        }
        
        initGrille();
    }

    public int getLenght() {
        return lenght;
    }

    public int getHeight() {
        return height;
    }

    public void initGrille() {
        cptUsedCase = 0;
        nbFlagLeft = NB_BOMBES;
        coordonnesMap = new HashMap<Object, Point>();
        tempsDebut = System.currentTimeMillis();
        startChrono();
        grille = new Case[height][lenght];
        etatPartie = PARTIE_EN_COURS;
        allScore = chargerScore();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < lenght; j++) {
                grille[i][j] = new Case(this);
                coordonnesMap.put(grille[i][j], new Point(j, i));
            }
        }
        nbCases = this.lenght * this.height;
        int i = 0;
        while (i < nbFlagLeft) {
            Random rnd = new Random();
            int x = rnd.nextInt(lenght);
            int y = rnd.nextInt(height);
            if (!grille[y][x].getBombe()) {
                grille[y][x].setBombe(true);
                i++;
            }
        }
        setChanged();
        notifyObservers();
    }

    public void updateValue(int x, int y) {
        if (grille[x][y].getStatus() == Case.CASE_NOUVELLE) {
            int process = grille[x][y].checkCase();
            if (process == -1) {
                etatPartie = GAME_OVER;
                stopChrono();
            } else if (getNbCases() - getCptUsedCase() <= NB_BOMBES) {
                System.out.println("Gagné !");
                etatPartie = PARTIE_GAGNE;
                stopChrono();
            } else if (process == 0) {
                etatPartie = PARTIE_EN_COURS;
            }
            setChanged();
            notifyObservers();
        }


    }

    public void stopChrono() {
        chronoThread.stop();
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
            this.nbFlagLeft = nbFlagLeft - 1;
        } else if (grille[x][y].getStatus() == Case.CASE_DRAPEAU) {
            grille[x][y].setStatus(Case.CASE_INTERROGATIVE);
            this.nbFlagLeft = nbFlagLeft + 1;
        } else if (grille[x][y].getStatus() == Case.CASE_INTERROGATIVE) {
            grille[x][y].setStatus(Case.CASE_NOUVELLE);
        }
        setChanged();
        notifyObservers();
    }

    public boolean incrementTime() {
        tempsActuel = System.currentTimeMillis() - tempsDebut;
        /*if (tempsActuel >= 120000) {
         return false;
         }*/
        setChanged();
        notifyObservers();
        return true;
    }

    public long getTimeActuel() {
        return tempsActuel;
    }

    public Map<String, List<Score>> getAllScore() {
        return allScore;
    }

    public void startChrono() {
        chronoThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean process = true;
                while (process) {
                    process = incrementTime();
                }

            }
        });
        chronoThread.start();
    }

    public int getNbCases() {
        return nbCases;
    }

    public void setNbCases(int nbCases) {
        this.nbCases = nbCases;
    }

    public int getNbFlagLeft() {
        return nbFlagLeft;
    }

    public void setNbFlagLeft(int nbFlagLeft) {
        this.nbFlagLeft = nbFlagLeft;
    }

    /**
     * Nombre de case visibles
     *
     * @return
     */
    public int getCptUsedCase() {
        return cptUsedCase;
    }

    public void incrementCptUsedCase() {
        this.cptUsedCase = this.cptUsedCase + 1;
    }

    public Case[][] getGrille() {
        return grille;
    }

//    public List<Score> getAllScores() {
//        return allScores;
//    }
    public int getEtatPartie() {
        return etatPartie;
    }

    public void setEtatPartie(int etatPartie) {
        this.etatPartie = etatPartie;
    }

    public Map<String, List<Score>> chargerScore() {
        try {
            FileInputStream fileIn = new FileInputStream("score.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            allScore = (Map<String, List<Score>>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
            return null;
        } catch (ClassNotFoundException c) {
            System.out.println("Score class not found");
            c.printStackTrace();
            return null;
        }

        return allScore;
    }

    public void sauvegarder(String name) {
        Score newScore = new Score(name, getTimeActuel(), niveau);
        List<Score> scores = null;
        if (allScore != null) {
            scores = allScore.get(String.valueOf(niveau));
        } else {
            allScore = new HashMap<String, List<Score>>();
        }
        if (isBetterScore(newScore, scores)) {
            if (scores != null) {
                if (scores.size() == 10) {
                    scores.remove(9);
                }
                scores.add(newScore);
                Collections.sort(scores);
            } else {
                scores = new ArrayList<>();
                scores.add(newScore);
            }
            allScore.put(String.valueOf(niveau), scores);
            try {
                FileOutputStream score = new FileOutputStream(new File("score.ser"));
                ObjectOutputStream out = new ObjectOutputStream(score);
                out.writeObject(allScore);
                out.close();
                score.close();
            } catch (IOException i) {
                i.printStackTrace();
            }

        }
    }

    private boolean isBetterScore(Score newScore, List<Score> listScore) {
        boolean existingLevel = false;
        if (listScore != null) {
            for (Score score : listScore) {
                if (score.getNiveau() == newScore.getNiveau()) {
                    existingLevel = true;
                    if (score.compareTo(newScore) > 0) {
                        return true;
                    }
                }
            }
            if (!existingLevel) {
                return true; //On a pas de score enregistré pour ce niveau, donc il faut enregistrer
            } else {
                return false;
            }

        }
        return true;
    }

    private void remplirList() {
        for (Map.Entry<Object, Point> entrySet : coordonnesMap.entrySet()) {
            Object key = entrySet.getKey();
            Point value = entrySet.getValue();

        }
    }
}

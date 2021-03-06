/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.awt.Point;

/**
 *
 * @author rifayath.david
 */
public class GridBoardReseau extends GridBoard {

    public static final int SERVEUR = 0;
    public static final int CLIENT = 1;
    public static final int ACTION_LEFT = 1;
    public static final int ACTION_RIGHT = 2;
    ObjetConnecte connexion;

    public GridBoardReseau(int niveauPartie, int typeConnexion) {
        super();
        if (typeConnexion == CLIENT) {
            connexion = new Client(this);
            connexion.startGame();
            Case.grille = this;
            initNiveau();
            initEnvironnement();
            nbCases = height * lenght;
        } else {
            connexion = new Serveur(this);
            niveau = niveauPartie;
            initNiveau();
            grille = new CaseReseau[height][lenght];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < lenght; j++) {
                    grille[i][j] = new CaseReseau(this);
                }
            }
            nbCases = this.lenght * this.height;
            initGrille();
            connexion.startGame();
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < lenght; j++) {
                coordonnesMap.put(grille[i][j], new Point(j, i));
            }
        }


        //Listener du clique droit
        Thread listenerRight = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int coord[] = connexion.receiveNextCoordonnees(ACTION_RIGHT);
                    //Si la partie est en cours
                    if (etatPartie == PARTIE_EN_COURS) {
                        //On met a jour la grille
                        updateFlagOther(coord[0], coord[1]);
                        //Si la partie est perdu suite � l'action de l'adversaire :
                        if (etatPartie == GAME_OVER) {
                            //On gagne la partie !
                            etatPartie = PARTIE_GAGNE;
                        } else if (etatPartie == PARTIE_GAGNE) //Si la partie est gagn�e suite � l'action de l'adversaire :
                        {
                            //On perd la partie ! :'(
                            etatPartie = GAME_OVER;
                        }
                    }
                    setChanged();
                    notifyObservers();
                }

            }
        });
        //Listener du clique gauche
        Thread listenerLeft = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    int coord[] = connexion.receiveNextCoordonnees(ACTION_LEFT);
                    //Si la partie est en cours
                    if (etatPartie == PARTIE_EN_COURS) {
                        //On met a jour la grille
                        updateValue(coord[0], coord[1]);
                        //Si la partie est perdu suite � l'action de l'adversaire :
                        if (etatPartie == GAME_OVER) {
                            //On gagne la partie !
                            etatPartie = PARTIE_GAGNE;
                        } else if (etatPartie == PARTIE_GAGNE) //Si la partie est gagn�e suite � l'action de l'adversaire :
                        {
                            //On perd la partie ! :'(
                            etatPartie = GAME_OVER;
                        }
                    }
                    setChanged();
                    notifyObservers();
                }

            }
        });
        listenerRight.start();
        listenerLeft.start();
    }

    public void updateAll(int x, int y, int action) {

        connexion.sendCoordonnees(x, y, action);
        if (action == ACTION_LEFT) {
            updateValue(x, y);
        } else if (action == ACTION_RIGHT) {
            updateFlag(x, y);
        }
          setChanged();
            notifyObservers();


    }

    public void updateOther(int x, int y) {
        if (grille[x][y].getStatus() == Case.CASE_NOUVELLE) {
            int process = grille[x][y].checkCase();
            if (process == -1) {
                etatPartie = GAME_OVER;
                stopChrono();
            } else if (getNbCases() - getCptUsedCase() <= NB_BOMBES) {
                System.out.println("Gagn� !");
                etatPartie = PARTIE_GAGNE;
                stopChrono();
            } else if (process == 0) {
                etatPartie = PARTIE_EN_COURS;
            }
            setChanged();
            notifyObservers();
        }


    }

    private void updateFlagOther(int x, int y) {


        updateFlag(x, y);
        ((CaseReseau) grille[x][y]).setNumeroPlayer(CaseReseau.PLAYER_OTHER);
        setChanged();
        notifyObservers();
    }

    void setGrille(CaseReseau[][] grille) {
        this.grille = grille;
    }

    CaseReseau[][] setGrille() {
        return (CaseReseau[][]) grille;
    }

    void setNiveau(int niv) {
        niveau = niv;
    }

    private void initNiveau() {
        if (niveau == LVL_FACILE) {
            height = 9;
            lenght = 9;
            NB_BOMBES = 10;
        } else if (niveau == LVL_MOYEN) {
            height = 16;
            lenght = 16;
            NB_BOMBES = 40;
        } else {
            height = 30;
            lenght = 16;
            NB_BOMBES = 99;
        }

        nbCases = this.lenght * this.height;
    }

    @Override
    public synchronized void updateValue(int x, int y) {
        if (grille[x][y].getStatus() == Case.CASE_NOUVELLE) {
            int process = grille[x][y].checkCase();
            if (process == -1) {
                etatPartie = GAME_OVER;
                stopChrono();
            } else if (getNbCases() - getCptUsedCase() <= NB_BOMBES) {
                System.out.println("Gagn� !");
                etatPartie = PARTIE_GAGNE;
                stopChrono();
            } else if (process == 0) {
                etatPartie = PARTIE_EN_COURS;
            }

        }


    }
    
    
    @Override
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
    }
}

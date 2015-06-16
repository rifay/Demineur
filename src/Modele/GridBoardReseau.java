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
            connexion = new Client();
        } else {
            connexion = new Serveur();
        }
        niveau = niveauPartie;
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
        grille = new CaseReseau[height][lenght];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < lenght; j++) {
                grille[i][j] = new CaseReseau(this);
                coordonnesMap.put(grille[i][j], new Point(j, i));
            }
        }
        nbCases = this.lenght * this.height;
        initGrille();
        connexion.startGame((CaseReseau[][])grille);
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
                        //Si la partie est perdu suite à l'action de l'adversaire :
                        if (etatPartie == GAME_OVER) {
                            //On gagne la partie !
                            etatPartie = PARTIE_GAGNE;
                        } else if (etatPartie == PARTIE_GAGNE) //Si la partie est gagnée suite à l'action de l'adversaire :
                        {
                            //On perd la partie ! :'(
                            etatPartie = GAME_OVER;
                        }
                    }
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
                        //Si la partie est perdu suite à l'action de l'adversaire :
                        if (etatPartie == GAME_OVER) {
                            //On gagne la partie !
                            etatPartie = PARTIE_GAGNE;
                        } else if (etatPartie == PARTIE_GAGNE) //Si la partie est gagnée suite à l'action de l'adversaire :
                        {
                            //On perd la partie ! :'(
                            etatPartie = GAME_OVER;
                        }
                    }
                }

            }
        });
        listenerRight.start();
        listenerLeft.start();
    }

    public void updateAll(int x, int y, int action) {

        connexion.sendCoordonnees(x, y, action);
        if (action == ACTION_LEFT) {
            super.updateValue(x, y);
        } else if (action == ACTION_RIGHT) {
            super.updateFlag(x, y);
        }



    }

    public void updateOther(int x, int y) {
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

    private void updateFlagOther(int x, int y) {


        super.updateFlag(x, y);
        ((CaseReseau) grille[x][y]).setNumeroPlayer(CaseReseau.PLAYER_OTHER);
        setChanged();
        notifyObservers();
    }
}

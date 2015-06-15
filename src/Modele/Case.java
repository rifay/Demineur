/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rifayath.david
 */
public class Case {

    public static final int CASE_NOUVELLE = 0;
    public static final int CASE_AFFICHER = 1;
    public static final int CASE_INUTILE = 2;
    public static final int CASE_DRAPEAU = 3;
    public static final int CASE_INTERROGATIVE = 4;
    private int value;
    private int status;
    private boolean bombe;
    private GridBoard grille;

    public Case(GridBoard grille) {
        this.status = CASE_NOUVELLE;
        value = 0;
        this.grille = grille;
    }

    public boolean isBombe() {
        return bombe;
    }

    public void setBombe(boolean bombe) {
        this.bombe = bombe;
    }

    public boolean getBombe() {
        return bombe;
    }

    public int getValue() {
        return value;
    }

    public int getStatus() {
        return status;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int checkCase() {
        if (this.bombe) {
            //On vient cliqué sur une bombe, partie terminé
            status = CASE_AFFICHER;
            return -1;
        } else {
            //la case ne contient pas de bombes, donc demander la propagation
            propagerCase();
            return 0; //Partie en cours
        }
    }

    public void propagerCase() {
        if (status == CASE_NOUVELLE) {
            List<Case> listeVoisins = new ArrayList();
            List<Case> listePropagationVoisins = new ArrayList();
            listeVoisins = grille.getVoisins(this);
            for (Case voisin : listeVoisins) {
                if (voisin.getBombe()) {
                    this.value++;
                }
            }

            if (this.value == 0) {
                this.status = CASE_INUTILE;
                grille.incrementCptUsedCase();
                listePropagationVoisins = grille.getVoisinsDePropagation(this);
                for (Case propagationCase : listePropagationVoisins) {
                    if (propagationCase.getStatus() != CASE_INUTILE && propagationCase.getStatus() != CASE_DRAPEAU) {
                        propagationCase.propagerCase();
                    }
                }
            } else {
                this.status = CASE_AFFICHER;
                grille.incrementCptUsedCase();
            }
        }
    }
}

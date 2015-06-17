/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.Serializable;

/**
 *
 * @author Arsenalol69
 */
public class CaseReseau extends Case implements Serializable{

    public final static int PLAYER_ME=0;
    public final static int PLAYER_OTHER=1;
    int numeroPlayer;
    
    public CaseReseau(){
        super();
    }
    public CaseReseau(GridBoard grille) {
        super(grille);
        numeroPlayer = PLAYER_ME;
    }

    public int getNumeroPlayer() {
        return numeroPlayer;
    }

    public void setNumeroPlayer(int numeroPlayer) {
        this.numeroPlayer = numeroPlayer;
    }
    
    @Override
    void init() {
        super.init();
        numeroPlayer=PLAYER_ME;
    }
}

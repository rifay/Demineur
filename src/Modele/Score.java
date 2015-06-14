/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import java.io.Serializable;

/**
 *
 * @author rifayath.david
 */
public class Score implements Serializable, Comparable<Score>{
    private long score;
    private String name;
    private int niveau;
    
    public Score(String name, long score, int niveau) {
        this.score = score;
        this.name = name;
        this.niveau = niveau;
    }

    public int getNiveau() {
        return niveau;
    }
    
    public long getScore() {
        return score;
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Score otherScore) {
        if (niveau == otherScore.getNiveau())
            return Long.compare(score, otherScore.getScore());
        else 
            return -1;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modele;

import Vue.FenetreReseau;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rifayath.david
 */
public class GridBoardReseau extends GridBoard {

    private final int PORT_NUMBER = 2525;
    private final int longueurMax = 8;
    private InetAddress adresseIP_adversaire;
    private DatagramSocket ds;

    public GridBoardReseau(int niveauPartie) {
        super(niveauPartie);
        try {
            ds = new DatagramSocket(PORT_NUMBER);
        } catch (SocketException ex) {
            Logger.getLogger(FenetreReseau.class.getName()).log(Level.SEVERE, null, ex);
        }

        Thread listener = new Thread(new Runnable() {

            @Override
            public void run() {
                byte[] buffer = new byte[longueurMax];
                DatagramPacket dp = new DatagramPacket(buffer, longueurMax);
                try {
                    ds.receive(dp);
                } catch (IOException ex) {
                    System.err.println(ex);
                }
                byte[] data = dp.getData();
                int x = Integer.valueOf(new String(Arrays.copyOfRange(data, 0, 4)));
                int y = Integer.valueOf(new String(Arrays.copyOfRange(data, 4, 8)));
                updateOther(x, y);
            }
        });
        listener.start();
    }

    public void updateAll(int x, int y) {
        super.updateValue(x, y);
        byte[] dataX = ByteBuffer.allocate(4).putInt(x).array();
        byte[] dataY = ByteBuffer.allocate(4).putInt(y).array();
        byte[] buffer = new byte[dataX.length + dataY.length];
        System.arraycopy(dataX, 0, buffer, 0, dataX.length);
        System.arraycopy(dataY, 0, buffer, dataY.length, dataY.length);
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length, adresseIP_adversaire, PORT_NUMBER);
        try {
            ds.send(dp);
        } catch (IOException ex) {
            Logger.getLogger(FenetreReseau.class.getName()).log(Level.SEVERE, null, ex);
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

}

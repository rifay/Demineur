package Modele;

import Vue.FenetreReseau;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Serveur extends ObjetConnecte {

    public Serveur() {
        super();
        try {
            ds_left = new DatagramSocket(PORT_S_LEFT);
            ds_right = new DatagramSocket(PORT_S_RIGHT);
            System.out.println("Port utilisé : " + ds_left.getLocalPort());
            startConnexion();
        } catch (SocketException ex) {
            System.err.println("Port déjà occupé : " + ex);
        }
    }

    @Override
    public void startConnexion() {
        DatagramPacket dp = receiveData();
        byte[] data = dp.getData();
        int portright = ByteBuffer.wrap(Arrays.copyOfRange(data, 0, 4)).getInt();
        PORT_C_LEFT = dp.getPort();
        PORT_C_RIGHT = portright;
        adresseIPClient = dp.getAddress();
        System.out.println("Client connecté IP=" + adresseIPClient.toString() + "/PortL : " + PORT_C_LEFT);

    }

    @Override
    protected void sendRequest(byte[] data, int action) {
        if (action == GridBoardReseau.ACTION_LEFT) {
            DatagramPacket dp = new DatagramPacket(data, data.length, adresseIPClient, PORT_C_LEFT);//portsend
            try {
                ds_left.send(dp);
            } catch (IOException ex) {
                Logger.getLogger(FenetreReseau.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action == GridBoardReseau.ACTION_RIGHT) {

            DatagramPacket dp = new DatagramPacket(data, data.length, adresseIPClient, PORT_C_RIGHT);//portsend
            try {
                ds_right.send(dp);
            } catch (IOException ex) {
                Logger.getLogger(FenetreReseau.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    @Override
    public void startGame(CaseReseau[][] grille)
    {
        ObjectOutputStream out = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            out = new ObjectOutputStream(bos);
            out.writeObject(grille);
            byte[] data = bos.toByteArray();
            //On envoie la grille générée.
            sendRequest(data, GridBoardReseau.ACTION_LEFT);
        } catch (IOException ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            out.close();
        } catch (IOException ex) {
            Logger.getLogger(Serveur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
package Modele;

import Vue.FenetreReseau;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client extends ObjetConnecte {


    public Client(GridBoardReseau grille) {
        super(grille);
        try {
            ds_left = new DatagramSocket();
            ds_right = new DatagramSocket();
            PORT_C_LEFT=ds_left.getLocalPort();
            PORT_C_RIGHT=ds_right.getLocalPort();
            System.out.println("Port utilisé : " + ds_left.getLocalPort());
            startConnexion();
        } catch (SocketException ex) {
            System.err.println("Port déjà occupé : " + ex);
        }
    }
   


    @Override
    protected void sendRequest(byte[] data, int action) {
        if (action == GridBoardReseau.ACTION_LEFT) {
            DatagramPacket dp = new DatagramPacket(data, data.length, adresseIPServeur, PORT_S_LEFT);//portsend
            try {
                ds_left.send(dp);
            } catch (IOException ex) {
                Logger.getLogger(FenetreReseau.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (action == GridBoardReseau.ACTION_RIGHT) {

            DatagramPacket dp = new DatagramPacket(data, data.length, adresseIPServeur, PORT_S_RIGHT);//portsend
            try {
                ds_right.send(dp);
            } catch (IOException ex) {
                Logger.getLogger(FenetreReseau.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    @Override
    public void startConnexion() {
        byte[] data = ByteBuffer.allocate(4).putInt(PORT_C_RIGHT).array();
        sendRequest(data, GridBoardReseau.ACTION_LEFT);
        System.out.println("Client connecté /PortR : " + PORT_C_RIGHT);

    }
    
    @Override
    public void startGame()
    {
        try {
            System.out.println("Client wait to start game...");
            DatagramPacket dp = receiveData();
            System.out.println("Client start game!");
            byte[] data = dp.getData();
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInput in = new ObjectInputStream(bis);
            grille.setNiveau((Integer) in.readObject());
            CaseReseau[][] grilleServ = (CaseReseau[][])in.readObject();
            System.out.println("Grille recu !");
            grille.setGrille(grilleServ);
        } catch (Exception ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
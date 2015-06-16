package Modele;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.*;

public abstract class ObjetConnecte {

    protected static InetAddress adresseIPServeur;
    protected static InetAddress adresseIPClient;
    protected final int PORT_S_RIGHT = 2525;
    protected final int PORT_S_LEFT = 2526;
    protected int PORT_C_RIGHT;
    protected int PORT_C_LEFT;
    protected static int longueurMax = 8;
    protected DatagramSocket ds_right;
    protected DatagramSocket ds_left;
    protected boolean connexionEtablie = false;

    /**
     * Cr?e un objet connect? et initialise l'InetAddress du serveur s'il cela
     * n'a pas d?j? ?t? fait.
     */
    public ObjetConnecte() {
        if (adresseIPServeur == null) {
            try {
                adresseIPServeur = InetAddress.getByName("localhost");
            } catch (UnknownHostException ex) {
                System.err.println(ex);
            }
        }
    }

    /**
     * Retourne la liste des ports libres
     *
     * @param d?but D?but de l'intervalle de port ? scanner
     * @param fin Fin de l'intervalle de port ? scanner
     * @return List<Integer> contenant les ports libres
     */
    public List<Integer> portScanner(int début, int fin) {
        DatagramSocket ds = null;
        boolean b = true;
        List<Integer> portsLibres = new ArrayList<Integer>();
        for (int i = début; i < fin + 1; i++) {
            b = true;
            try {
                ds = new DatagramSocket(i);
            } catch (SocketException e) {
                b = false;
            }

            if (b) {
                portsLibres.add(i);
                ds.close();
            }
        }
        return portsLibres;
    }

    /**
     * Permet d'envoyer un message qui sera encapsul? ? un destinataire
     *
     * @param txt Message qui sera encapsul? et envoy?
     * @param adresseIP Adresse IP du destinaire
     * @param portDest Port du destinataire
     */
    abstract protected void sendRequest(byte[] data, int action);

    /**
     * Permet de recevoir les coordonné envoyé.
     *
     * @return retourne les coordonnées recus.
     */
    public int[] receiveNextCoordonnees(int action)
    {
         byte[] buffer = new byte[longueurMax];
                DatagramPacket dp = new DatagramPacket(buffer, longueurMax);
                try {
                    if(action == GridBoardReseau.ACTION_RIGHT)
                    {
                       ds_right.receive(dp);
                       
                    }else {
                        ds_left.receive(dp);
                    }
                    
                } catch (IOException ex) {
                    System.err.println(ex);
                }
                byte[] data = dp.getData();
                int x = ByteBuffer.wrap(Arrays.copyOfRange(data, 0, 4)).getInt();
                int y = ByteBuffer.wrap(Arrays.copyOfRange(data, 4, 8)).getInt();
                
               return new int[]{x,y};
    
    }
    
    public void sendCoordonnees(int x, int y, int action)
    {
        byte[] dataX = ByteBuffer.allocate(4).putInt(x).array();
        byte[] dataY = ByteBuffer.allocate(4).putInt(y).array();
        byte[] buffer = new byte[dataX.length + dataY.length];
        System.arraycopy(dataX, 0, buffer, 0, dataX.length);
        System.arraycopy(dataY, 0, buffer, dataY.length, dataY.length);
        sendRequest(buffer, action);
    }
    
    public abstract void startConnexion();
   
    public abstract void startGame(CaseReseau[][] grille);
    
    public DatagramPacket receiveData()
    {
        
        byte[] buffer = new byte[longueurMax];
        DatagramPacket dp = new DatagramPacket(buffer, longueurMax);
        try {
            ds_left.receive(dp);


        } catch (IOException ex) {
            System.err.println(ex);
        }
        return dp;
    
    }

}
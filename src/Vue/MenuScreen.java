/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vue;

import Modele.GridBoard;
import java.awt.Frame;
import javax.swing.JOptionPane;

/**
 *
 * @author rifayath.david
 */
public class MenuScreen extends javax.swing.JDialog {

    private int niveauPartie;
    private Frame parent;
    /**
     * Creates new form MenuScreen
     */
    public MenuScreen(Frame parent, boolean modal) {
        super(parent, modal);
        niveauPartie = -1;
        this.parent=parent;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jRadioButton_NiveauF = new javax.swing.JRadioButton();
        jRadioButton_NiveauM = new javax.swing.JRadioButton();
        jRadioButton_NiveauD = new javax.swing.JRadioButton();
        jButton_lancerPartie = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jItemModeMulti = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assets/mine.png"))); // NOI18N
        jLabel1.setText("D�mineur");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Niveau"));
        jPanel1.setToolTipText("Niveau");
        jPanel1.setInheritsPopupMenu(true);
        jPanel1.setName(""); // NOI18N

        buttonGroup1.add(jRadioButton_NiveauF);
        jRadioButton_NiveauF.setSelected(true);
        jRadioButton_NiveauF.setText("Facile");

        buttonGroup1.add(jRadioButton_NiveauM);
        jRadioButton_NiveauM.setText("Moyen");

        buttonGroup1.add(jRadioButton_NiveauD);
        jRadioButton_NiveauD.setText("Difficile");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jRadioButton_NiveauF)
                .addGap(82, 82, 82)
                .addComponent(jRadioButton_NiveauM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                .addComponent(jRadioButton_NiveauD)
                .addGap(23, 23, 23))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton_NiveauF)
                    .addComponent(jRadioButton_NiveauM)
                    .addComponent(jRadioButton_NiveauD))
                .addGap(0, 24, Short.MAX_VALUE))
        );

        jButton_lancerPartie.setText("Lancer la partie");
        jButton_lancerPartie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_lancerPartieActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        jItemModeMulti.setText("Mode Multijoueur");
        jItemModeMulti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jItemModeMultiActionPerformed(evt);
            }
        });
        jMenu1.add(jItemModeMulti);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(158, 158, 158)
                .addComponent(jButton_lancerPartie)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(31, 31, 31)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton_lancerPartie)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton_lancerPartieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_lancerPartieActionPerformed
        if (jRadioButton_NiveauF.isSelected()) {
            niveauPartie = 1;
        } else if (jRadioButton_NiveauM.isSelected()) {
            niveauPartie = 2;
        } else if (jRadioButton_NiveauD.isSelected()) {
            niveauPartie = 3;
        }
        if (niveauPartie == -1) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez choisir le niveau de la partie",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            dispose();
            new FenetrePrincipale(niveauPartie).setVisible(true);
        }


    }//GEN-LAST:event_jButton_lancerPartieActionPerformed

    private void jItemModeMultiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jItemModeMultiActionPerformed
        this.setVisible(false);
        new MenuScreenReseau(parent, true, this).setVisible(true);
    }//GEN-LAST:event_jItemModeMultiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MenuScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MenuScreen dialog = new MenuScreen(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton_lancerPartie;
    private javax.swing.JMenuItem jItemModeMulti;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton_NiveauD;
    private javax.swing.JRadioButton jRadioButton_NiveauF;
    private javax.swing.JRadioButton jRadioButton_NiveauM;
    // End of variables declaration//GEN-END:variables
}

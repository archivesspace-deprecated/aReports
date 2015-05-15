/*
 * A frame to perform basic searching
 */
package areports;

import java.sql.Connection;
import java.sql.SQLException;
import quick.dbtable.DBTable;

/**
 *
 * @author nathan
 */
public class SearchFrame extends javax.swing.JFrame {

    private DBTable dBTable;

    /**
     * Creates new form SearchFrame
     * @param connection
     */
    public SearchFrame(Connection connection) {
        initComponents();
        addQuickTable(connection);
        
        // add a test sql select statement
        String testSQL = "SELECT\n"
                + "     resource.`id` AS resourceId,\n"
                + "     resource.`repo_id` AS repo_id,\n"
                + "     resource.`title` AS title,\n"
                + "     resource.`identifier` AS resourceIdentifier,\n"
                + "     GetResourceCreator(resource.`id`) AS displayCreator,\n"
                + "     GetEnumValueUF(resource.`language_id`) AS languageCode,\n"
                + "     GetEnumValueUF(resource.`level_id`) AS level,\n"
                + "     GetResourceDateExpression(resource.`id`) AS dateExpression,\n"
                + "     GetResourceExtent(resource.`id`) AS extentNumber,\n"
                + "     GetResourceExtentType(resource.`id`) AS extentType,\n"
                + "     GetResourceContainerSummary(resource.`id`) As containerSummary,\n"
                + "     resource.`restrictions` AS restrictionsApply\n"
                + "FROM\n"
                + "     `resource` resource";
        
        sqlSelectTextArea.setText(testSQL);
    }

    /**
     * Method to add the quicktable component
     */
    private void addQuickTable(Connection connection) {
        dBTable = new quick.dbtable.DBTable();

        //add to frame
        getContentPane().add(dBTable);

        dBTable.setConnection(connection);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        searchButton = new javax.swing.JButton();
        reportButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        sqlSelectTextArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Search Form");

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        reportButton.setText("Print Screen Report");
        reportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(searchButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reportButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 482, Short.MAX_VALUE)
                .addComponent(closeButton))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeButton)
                    .addComponent(searchButton)
                    .addComponent(reportButton)))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jLabel1.setText("SQL Select");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

        sqlSelectTextArea.setColumns(20);
        sqlSelectTextArea.setRows(5);
        jScrollPane1.setViewportView(sqlSelectTextArea);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 732, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(68, 68, 68))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     * Method to just dispose of Window
     * @param evt 
     */
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeButtonActionPerformed
    
    /**
     * Method used to run the select statement
     * @param evt 
     */
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        dBTable.setSelectSql(sqlSelectTextArea.getText());

        try {
            //fetch the data from database to fill the table
            dBTable.refresh();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_searchButtonActionPerformed
    
    /**
     * Method to do a sprint screen report
     * @param evt 
     */
    private void reportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportButtonActionPerformed
        
    }//GEN-LAST:event_reportButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton reportButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextArea sqlSelectTextArea;
    // End of variables declaration//GEN-END:variables

}
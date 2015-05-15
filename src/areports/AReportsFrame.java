/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package areports;

import areports.model.JasperReportInfo;
import areports.model.RepositoryInfo;
import areports.utils.ReportUtils;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author nathan
 */
public class AReportsFrame extends javax.swing.JFrame {

    private JFileChooser chooser;

    private TreeMap<String, Set<JasperReportInfo>> reportsMap;

    private JasperReportInfo currentReportInfo;

    // the mysql database connection
    private Connection connection = null;

    // used for printing the jasper reports
    private JasperReport jasperReport;
    private JasperPrint jasperPrint;

    // THe file chooser to select 
    // preference object for storing preferences
    private Preferences preferences;

    private File reportsDirectory;
    
    /**
     * Creates new form AReportsFrame
     */
    public AReportsFrame() {
        initComponents();
    }

    /**
     * Method to load stored stored preferences
     * @param reportsDirectory
     * @param localASpaceDirectory
     */
    public void loadPreferences(File reportsDirectory, String localASpaceDirectory) {
        this.reportsDirectory = reportsDirectory;
        
        preferences = Preferences.userRoot().node(this.getClass().getName());
        
        String defaultDatabaseURL = "jdbc:mysql://test.archivesspace.org/archivesspace";
        String value = preferences.get("jdbc.url", defaultDatabaseURL);
        
        // check to make sure we not connecting to the old tracer. If so point to the
        // new host
        if(value.contains("tracerdb.cyo37z0ucix8")) {
            value = defaultDatabaseURL;
        }
        
        jdbcURLTextField.setText(value);

        value = preferences.get("jdbc.username", "aspace");
        jdbcUsernameTextField.setText(value);

        value = preferences.get("jdbc.password", "clubfoots37@freakiest");
        jdbcPasswordField.setText(value);

        value = preferences.get("sftp.host", "localhost:22");
        sftpHostTextField.setText(value);

        value = preferences.get("sftp.username", "aspace");
        sftpUsernameTextField.setText(value);

        value = preferences.get("sftp.password", null);
        sftpPasswordField.setText(value);

        value = preferences.get("aspace.directory", localASpaceDirectory);
        aspaceDirectoryTextField.setText(value);
        
        // get the reports files
        value = preferences.get("reports.directory", null);
        
        if(value != null) {
            File directory = new File(value);
            
            if(directory.exists()) {
                this.reportsDirectory = directory;    
            }
        }
        
        value = this.reportsDirectory.getAbsolutePath();
        reportsDirectoryTextField.setText(value);          
    }

    /**
     * Method to store the preferences
     */
    private void storePreferences() {
        preferences.put("jdbc.url", jdbcURLTextField.getText());
        preferences.put("jdbc.username", jdbcUsernameTextField.getText());
        preferences.put("jdbc.password", new String(jdbcPasswordField.getPassword()));

        preferences.put("sftp.host", sftpHostTextField.getText());
        preferences.put("sftp.username", sftpUsernameTextField.getText());
        preferences.put("sftp.password", new String(sftpPasswordField.getPassword()));

        preferences.put("aspace.directory", aspaceDirectoryTextField.getText());

        preferences.put("reports.directory", reportsDirectoryTextField.getText());
    }

    /**
     * Method to set the map containing the reports file
     */
    public void loadReportsMap() {
        this.reportsMap = ReportUtils.findJasperReports(reportsDirectory, null, null);

        // now update the User interface components
        recordTypeComboBox.removeAllItems();

        for (String reportType : reportsMap.keySet()) {
            Set reportsSet = reportsMap.get(reportType);
            if (!reportsSet.isEmpty()) {
                recordTypeComboBox.addItem(reportType);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        closeButton = new javax.swing.JButton();
        connectButton = new javax.swing.JButton();
        disconnectButton = new javax.swing.JButton();
        jdbcPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jdbcURLTextField = new javax.swing.JTextField();
        jdbcUsernameTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jdbcPasswordField = new javax.swing.JPasswordField();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        sftpHostTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        sftpUsernameTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        sftpPasswordField = new javax.swing.JPasswordField();
        aspaceDirectoryTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        recordTypeComboBox = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        reportsDirectoryTextField = new javax.swing.JTextField();
        browseButton = new javax.swing.JButton();
        reportsComboBox = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        reportDescriptionTextArea = new javax.swing.JTextArea();
        jLabel10 = new javax.swing.JLabel();
        previewButton = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        repositoryComboBox = new javax.swing.JComboBox();
        pushButton = new javax.swing.JButton();
        statusTextField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        searchButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("aReports -- A Desktop Archivesspace Reports Engine (v0.3.0 05/15/2015)");

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        disconnectButton.setText("Disconnect");
        disconnectButton.setEnabled(false);
        disconnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                disconnectButtonActionPerformed(evt);
            }
        });

        jdbcPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("JDBC Connection Information"));

        jLabel1.setText("JDBC URL");

        jLabel2.setText("Username");

        jLabel3.setText("Password");

        javax.swing.GroupLayout jdbcPanelLayout = new javax.swing.GroupLayout(jdbcPanel);
        jdbcPanel.setLayout(jdbcPanelLayout);
        jdbcPanelLayout.setHorizontalGroup(
            jdbcPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdbcPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jdbcPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jdbcPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jdbcPanelLayout.createSequentialGroup()
                        .addComponent(jdbcUsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jdbcPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jdbcURLTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 631, Short.MAX_VALUE)))
        );
        jdbcPanelLayout.setVerticalGroup(
            jdbcPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jdbcPanelLayout.createSequentialGroup()
                .addGroup(jdbcPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jdbcURLTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jdbcPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jdbcUsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jdbcPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("sFTP Information"));

        jLabel4.setText("Host");

        jLabel5.setText("Username");

        jLabel6.setText("Password");

        jLabel7.setText("Archivesspace Directory");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(aspaceDirectoryTextField))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(sftpHostTextField)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(sftpUsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sftpPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(sftpHostTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(sftpUsernameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(sftpPasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(aspaceDirectoryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Run Reports"));

        jLabel8.setText("Select Reports");

        recordTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Agents", "Subjects", "Accessions", "Digital Objects", "Resources" }));
        recordTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                recordTypeComboBoxActionPerformed(evt);
            }
        });

        jLabel9.setText("Reports Directory");

        reportsDirectoryTextField.setText("/reports");

        browseButton.setText("Browse");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        reportsComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        reportsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportsComboBoxActionPerformed(evt);
            }
        });

        reportDescriptionTextArea.setEditable(false);
        reportDescriptionTextArea.setColumns(20);
        reportDescriptionTextArea.setLineWrap(true);
        reportDescriptionTextArea.setRows(5);
        reportDescriptionTextArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(reportDescriptionTextArea);

        jLabel10.setText("Report Description");

        previewButton.setText("Preview");
        previewButton.setEnabled(false);
        previewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                previewButtonActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel11.setText("Status: ");

        statusLabel.setText("Not compiled ...");

        jLabel13.setText("Select Repository");

        repositoryComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "No Repositories Loaded ..." }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9)
                    .addComponent(jLabel13))
                .addGap(30, 30, 30)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(repositoryComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(previewButton))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(reportsDirectoryTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 463, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(browseButton))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(recordTypeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(reportsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 304, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(statusLabel)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(reportsDirectoryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(browseButton))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(recordTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reportsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(statusLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(previewButton)
                    .addComponent(jLabel13)
                    .addComponent(repositoryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pushButton.setText("Push Reports");
        pushButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pushButtonActionPerformed(evt);
            }
        });

        statusTextField.setText("Not Connected ...");
        statusTextField.setEnabled(false);

        jLabel12.setText("Status");

        searchButton.setText("Search");
        searchButton.setEnabled(false);
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jdbcPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(connectButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(disconnectButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(searchButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pushButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeButton))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addGap(57, 57, 57)
                .addComponent(statusTextField)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jdbcPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(closeButton)
                    .addComponent(connectButton)
                    .addComponent(disconnectButton)
                    .addComponent(pushButton)
                    .addComponent(searchButton)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Method to copy the jasper file and ASpace report model to the server
     *
     * @param evt
     */
    private void pushButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pushButtonActionPerformed
        String host = sftpHostTextField.getText();
        String username = sftpUsernameTextField.getText();
        String password = new String(sftpPasswordField.getPassword());
        String remoteDirectory = aspaceDirectoryTextField.getText();

        // open a connection to the remote host if needed
        ReportUtils.connectToSFTPHost(host, username, password, remoteDirectory);

        for (String key : reportsMap.keySet()) {
            Set<JasperReportInfo> reportSet = reportsMap.get(key);
            for (JasperReportInfo reportInfo : reportSet) {
                // save the report model to the file
                ReportUtils.saveReportConfigFile(reportInfo);

                // now copy the file to the server
                ReportUtils.copyReportFile(reportInfo);
            }
        }
    }//GEN-LAST:event_pushButtonActionPerformed

    /**
     * Used to generate a report preview
     *
     * @param evt
     */
    private void previewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_previewButtonActionPerformed
        if (currentReportInfo == null || connection == null) {
            return;
        }

        try {
            HashMap<String, Object> parameterMap = new HashMap<String, Object>();
            parameterMap.put("basePath", currentReportInfo.getParentDirectoryName());
            
            RepositoryInfo repositoryInfo = (RepositoryInfo)repositoryComboBox.getSelectedItem();
            parameterMap.put("repositoryId", repositoryInfo.getRepositoryId());
            
            jasperReport = JasperCompileManager.compileReport(currentReportInfo.getFileName());
            jasperPrint = JasperFillManager.fillReport(jasperReport, parameterMap, connection);

            JasperViewer jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setVisible(true);
        } catch (JRException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Jasper Report Error",
                    JOptionPane.ERROR_MESSAGE);

            Logger.getLogger(AReportsFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_previewButtonActionPerformed

    /**
     * populate the reports drop down with info objects
     */
    private void recordTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_recordTypeComboBoxActionPerformed
        reportsComboBox.removeAllItems();

        String key = (String) recordTypeComboBox.getSelectedItem();
        if (key != null) {
            Set<JasperReportInfo> reportsSet = reportsMap.get(key);
            for (JasperReportInfo reportInfo : reportsSet) {
                reportsComboBox.addItem(reportInfo);
            }
        }
    }//GEN-LAST:event_recordTypeComboBoxActionPerformed

    /**
     * Load the selected report information
     *
     * @param evt
     */
    private void reportsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportsComboBoxActionPerformed
        currentReportInfo = (JasperReportInfo) reportsComboBox.getSelectedItem();
        if (currentReportInfo != null) {
            reportDescriptionTextArea.setText(currentReportInfo.getDescription());

            if (currentReportInfo.isCompiled()) {
                statusLabel.setText("Compiled");
            } else {
                statusLabel.setText("Not compiled ...");
            }
        }
    }//GEN-LAST:event_reportsComboBoxActionPerformed

    /**
     * Connect to the ASpace database
     *
     * @param evt
     */
    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        try {
            String password = new String(jdbcPasswordField.getPassword());

            Class.forName("com.mysql.jdbc.Driver");

            connection = DriverManager.getConnection(
                    jdbcURLTextField.getText(),
                    jdbcUsernameTextField.getText(),
                    password);

            // update the UI components
            previewButton.setEnabled(true);
            disconnectButton.setEnabled(true);
            searchButton.setEnabled(true);
            statusTextField.setText("Connected to Databae ...");
            
            // now load the repositories
            loadRepositories();
        } catch (Exception e) {
            Logger.getLogger(AReportsFrame.class.getName()).log(Level.SEVERE, null, e);
        }
    }//GEN-LAST:event_connectButtonActionPerformed
    
    /**
     * Method to load the repositories from ASpace
     */
    private void loadRepositories() {
        try {
            Statement stmt = connection.createStatement();
            String sql = "SELECT id, name FROM repository WHERE id != 1 ORDER BY name";
            ResultSet result = stmt.executeQuery(sql);

            //Print the data to the console
            repositoryComboBox.removeAllItems();
            
            // add a repository 
            RepositoryInfo repositoryInfo = new RepositoryInfo(-1, "GLOBAL REPOSITORY (FOR TESTING)");
            repositoryComboBox.addItem(repositoryInfo);
            
            while(result.next()){
                int id = result.getInt("id");
                String name = result.getString("name");
                repositoryComboBox.addItem(new RepositoryInfo(id, name));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AReportsFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Method to disconnect from the database connection
     *
     * @param evt
     */
    private void disconnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_disconnectButtonActionPerformed
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                previewButton.setEnabled(false);
                disconnectButton.setEnabled(false);
                statusTextField.setText("Disconnected ...");
            } catch (SQLException ex) {
                Logger.getLogger(AReportsFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_disconnectButtonActionPerformed

    /**
     * Method to close the application
     *
     * @param evt
     */
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(AReportsFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        // save the program preferences
        storePreferences();

        System.exit(0);
    }//GEN-LAST:event_closeButtonActionPerformed

    /**
     * Method to select the reports directory
     *
     * @param evt
     */
    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(reportsDirectory);
        chooser.setDialogTitle("Select Reports Directory");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            reportsDirectory = chooser.getSelectedFile();
            reportsDirectoryTextField.setText(reportsDirectory.getAbsolutePath());
            loadReportsMap();
        } else {
            System.out.println("No Directry Selected");
        }
    }//GEN-LAST:event_browseButtonActionPerformed
    
    /**
     * Display the search Frame
     * @param evt 
     */
    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        if(connection != null) {
            SearchFrame searchFrame = new SearchFrame(connection);
            searchFrame.setSize(800, 600);
            searchFrame.setVisible(true);
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField aspaceDirectoryTextField;
    private javax.swing.JButton browseButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton connectButton;
    private javax.swing.JButton disconnectButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel jdbcPanel;
    private javax.swing.JPasswordField jdbcPasswordField;
    private javax.swing.JTextField jdbcURLTextField;
    private javax.swing.JTextField jdbcUsernameTextField;
    private javax.swing.JButton previewButton;
    private javax.swing.JButton pushButton;
    private javax.swing.JComboBox recordTypeComboBox;
    private javax.swing.JTextArea reportDescriptionTextArea;
    private javax.swing.JComboBox reportsComboBox;
    private javax.swing.JTextField reportsDirectoryTextField;
    private javax.swing.JComboBox repositoryComboBox;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField sftpHostTextField;
    private javax.swing.JPasswordField sftpPasswordField;
    private javax.swing.JTextField sftpUsernameTextField;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField statusTextField;
    // End of variables declaration//GEN-END:variables
}

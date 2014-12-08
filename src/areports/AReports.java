/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package areports;

import areports.utils.ReportUtils;
import java.io.File;

/**
 *
 * @author nathan
 */
public class AReports {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String currentDirectoryName = System.getProperty("user.dir");
        File reportsDirectory = new File(currentDirectoryName + "/reports");
        File modelDirectory = new File(currentDirectoryName + "/reports/model");
        String localASpaceDirectory = System.getProperty("user.home") + "/ASpace/archivesspace";
        
        // set the model directory for file util
        ReportUtils.modelDirectory = modelDirectory;
        
        /* DEBUG code testing connect to the remote directory. Remove before commiting
        // to remote
        System.out.println("Report Directory " + reportsDirectory.getAbsolutePath());
        TreeMap<String, Set<JasperReportInfo>> reportsMap = ReportUtils.findJasperReports(reportsDirectory, null, null);
        
        ReportUtils.connectToSFTPHost("54.235.231.8:22", "aspace", "migrat34un", "ASpace4/archivesspace");
        
        for(String key: reportsMap.keySet()) {
            Set<JasperReportInfo> reportSet = reportsMap.get(key);
            for(JasperReportInfo reportInfo: reportSet) {
                // save the report model to the file
                ReportUtils.saveReportModelToFile(reportInfo);
                
                // now copy the file to the server
                ReportUtils.copyReportFile(reportInfo);
            }
        }*/
        
        AReportsFrame frame = new AReportsFrame();
        frame.loadPreferences(reportsDirectory, localASpaceDirectory);
        frame.loadReportsMap();
        frame.pack();
        frame.setVisible(true);        
    }

}

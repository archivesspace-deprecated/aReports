/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package areports;

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
        String localASpaceDirectory = System.getProperty("user.home") + "/ASpace/archivesspace";
        
        AReportsFrame frame = new AReportsFrame();
        frame.loadPreferences(reportsDirectory, localASpaceDirectory);
        frame.loadReportsMap();
        frame.setVisible(true);        
    }

}

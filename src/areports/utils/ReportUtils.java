package areports.utils;

import areports.model.JasperReportInfo;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * A utility class for doing file related task
 *
 * @author nathan
 */
public class ReportUtils {

    //private static String aspaceReportsDir = "/"
    private static String sftpHost;
    private static String aspaceDirectory;
    private static ChannelSftp sftpChannel = null;

    /**
     * Test method to list all reports
     *
     * @param dir
     * @param reportsDir
     * @param reportsMap
     * @return HashMap containing
     */
    public static TreeMap<String, Set<JasperReportInfo>> findJasperReports(File dir, String reportsDir,
            TreeMap<String, Set<JasperReportInfo>> reportsMap) {

        // initialize the reports map if needed
        if (reportsMap == null) {
            reportsMap = new TreeMap<String, Set<JasperReportInfo>>();
            reportsDir = dir.getAbsolutePath();
        }

        File[] files = dir.listFiles();
        for (File file : files) {
            String fileName = file.getName();

            if (fileName.startsWith(".")) {
                continue;
            }

            if (file.isDirectory()) {
                String dirName = file.getAbsolutePath();
                //System.out.println("\nDirectory: " + dirName + "\n");

                TreeSet<JasperReportInfo> reportsSet = new TreeSet<JasperReportInfo>();
                String key = dirName.replace(reportsDir, "");
                reportsMap.put(key, reportsSet);

                // make a recursive call
                findJasperReports(file, reportsDir, reportsMap);
            } else {
                if (fileName.endsWith(".jrxml")) {
                    if (fileName.startsWith("sub_") || fileName.contains("_sub")) {
                        //System.out.println("Sub-Report\t-- " + fileName);
                    } else {
                        String key = file.getParent().replace(reportsDir, "");
                        Set<JasperReportInfo> reportsSet = reportsMap.get(key);

                        String reportName = fileName.replace(".jrxml", "");
                        String description = getReportDescription(file);

                        File compiledFile = new File(file.getParentFile(), reportName + ".jasper");
                        boolean compiled = compiledFile.exists();

                        JasperReportInfo jasperReportInfo = new JasperReportInfo(
                                reportName, file, description, compiled);

                        reportsSet.add(jasperReportInfo);
                    }
                }
            }
        }

        return reportsMap;
    }

    /**
     * Method to read the description information from a jasper report file
     *
     * @param file
     * @return
     */
    private static String getReportDescription(File file) {
        String description = "";

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("property");

            for (int i = 0; i < nList.getLength(); i++) {
                Element element = (Element) nList.item(i);
                String name = element.getAttribute("name");
                if (name.equalsIgnoreCase("reportDescription")) {
                    description = element.getAttribute("value");
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        // return the description
        return description;
    }

    /**
     * Method to save the ASpace report model to a file
     *
     * @param reportInfo
     */
    public static void saveReportConfigFile(JasperReportInfo reportInfo) {
        String filename = reportInfo.getParentDirectoryName() + File.separator + reportInfo.getConfigFilename();
        File file = new File(filename);

        try {
            FileUtils.writeStringToFile(file, reportInfo.getReportConfig());
        } catch (IOException ex) {
            Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method to connect to the sftp host
     *
     * @param host
     * @param username
     * @param password
     * @param remoteDirectory
     */
    public static boolean connectToSFTPHost(String host, String username, String password, String remoteDirectory) {
        sftpHost = host;
        aspaceDirectory = remoteDirectory;

        // check to see if we are not just connecting to localhost
        if (!sftpHost.contains("localhost")) {
            JSch jsch = new JSch();
            Session session;
            try {
                String[] sa = host.split(":");
                host = sa[0];
                int port = Integer.parseInt(sa[1]);

                session = jsch.getSession(username, host, port);
                session.setConfig("StrictHostKeyChecking", "no");
                session.setPassword(password);
                session.connect();

                Channel channel = session.openChannel("sftp");
                channel.connect();
                sftpChannel = (ChannelSftp) channel;
                sftpChannel.cd(remoteDirectory);

                System.out.println("Connected to: " + host);
                return true;
            } catch (Exception ex) {
                Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return false;
    }

    /**
     * Method to close the sftp connection
     */
    public static void disconnectFromSFTPHost() {
        if (sftpChannel != null) {
            sftpChannel.disconnect();
        }
    }

    /**
     * Method to copy the remote file either to the remote server or local
     * machine
     *
     * @param reportInfo
     */
    public static void copyReportFile(JasperReportInfo reportInfo) {
        if (!reportInfo.isCompiled()) {
            System.out.println("Report " + reportInfo + " not compiled ...");
            return;
        }

        if (sftpHost.contains("localhost")) {
            copyToLocalhost(reportInfo);
        } else {
            copyToRemoteHost(reportInfo);
        }
    }

    /**
     * Copy to localhost by just using apace fileutils to copy directory and
     * report related files
     *
     * @param reportInfo
     */
    private static void copyToLocalhost(JasperReportInfo reportInfo) {
        File reportDir = new File(reportInfo.getParentDirectoryName());

        String dirName = aspaceDirectory + File.separator + "reports" + File.separator + reportInfo.getReportName();
        File aspaceReportsDir = new File(dirName);

        try {
            // copy the directory containing the report
            FileUtils.copyDirectory(reportDir, aspaceReportsDir);
        } catch (IOException ex) {
            Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method to copy a file to the remote directory
     *
     * @param reportInfo
     */
    private static void copyToRemoteHost(JasperReportInfo reportInfo) {
        // first create the remote directory to hold all the report related files 
        String remoteReportDirectory = "reports/" + reportInfo.getReportName();

        try {
            // get the report and sub report files
            ArrayList<File> reportFiles = getReportFiles(reportInfo);

            // now create the directory and copy all the files
            if (!reportDirectoryExist(remoteReportDirectory)) {
                sftpChannel.mkdir(remoteReportDirectory);
                System.out.println("Created Reports Directory " + remoteReportDirectory);
            }

            // now copy the report and sub report files to the remote server
            for (File file : reportFiles) {
                String remoteFilename = remoteReportDirectory + "/" + file.getName();
                sftpChannel.put(new FileInputStream(file), remoteFilename, ChannelSftp.OVERWRITE);
                System.out.println("Copied Report File " + remoteFilename);
            }
        } catch (Exception ex) {
            Logger.getLogger(ReportUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method to return all report related files in a directory
     *
     * @param reportInfo
     * @return
     */
    public static ArrayList<File> getReportFiles(JasperReportInfo reportInfo) {
        ArrayList<File> reportFiles = new ArrayList<File>();

        // get any sub reports
        File directory = new File(reportInfo.getParentDirectoryName());

        for (File file : directory.listFiles()) {
            if (file.isFile() && (file.getName().contains(".jasper")
                    || file.getName().contains(".jrxml") 
                    || file.getName().contains(".yml"))) {
                reportFiles.add(file);
            }
        }

        return reportFiles;
    }

    /**
     * Method to check to see if the report directory on remote server exist
     * before creating
     *
     * @param reportDirectory
     * @return
     */
    private static boolean reportDirectoryExist(String reportDirectory) {
        try {
            sftpChannel.lstat(reportDirectory);
            return true;
        } catch (SftpException ex) {
            return false;
        }
    }
}

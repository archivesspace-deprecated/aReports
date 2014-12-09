package areports.model;

import java.io.File;
import org.apache.commons.lang3.StringUtils;

/**
 * A class which stores basic information about the jasper report
 * @author nathan
 */
public class JasperReportInfo implements Comparable {
    private String reportName = "";
    private File file = null;
    private String description = "";
    private boolean compiled = false;

    public JasperReportInfo(String reportName, File file, String description, boolean compiled) {
        this.reportName = reportName;
        this.file = file;
        this.description = description;
        this.compiled = compiled;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getFileName() {
        return file.getAbsolutePath();
    }
    
    public String getParentDirectoryName() {
        return file.getParent();
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isCompiled() {
        return compiled;
    }
    
    /**
     * Get a short name
     * @return 
     */
    public String getShortDescription() {
        String shortDescription = StringUtils.join(
                StringUtils.splitByCharacterTypeCamelCase(reportName), " ");
        return shortDescription;
    }
        
    public String getReportURI() {
        String uri = "";
        
        String[] sa = StringUtils.splitByCharacterTypeCamelCase(reportName);
        
        for(int i = 0; i < (sa.length-1); i++) {
            if(i == 0) {
                uri = sa[i].toLowerCase();
            } else {
                uri += "_" + sa[i].toLowerCase();
            }
        }
        
        return uri;
    }
    
    /**
     * Method to return the ASpace report model
     */
    public String getReportConfig() {
        String config = "report_type: jdbc\n" + 
                "uri_suffix: " + getReportURI() + "\n" + 
                "description: \"" +  getDescription() + "\"\n";
        
        return config;
    }
    
    public String getConfigFilename() {
        return "report_config.yml";
    }
    
    @Override
    public String toString() {
        return reportName;
    }

    @Override
    public int compareTo(Object o) {
        JasperReportInfo reportInfo  = (JasperReportInfo)o;
        String name = reportInfo.getReportName();
        return reportName.compareTo(name);
    }
}

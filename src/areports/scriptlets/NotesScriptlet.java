package areports.scriptlets;

/**
 * A Scriptlet for processing notes which come from mysql database as JSON
 *
 * @author nathan
 */
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

public class NotesScriptlet extends JRDefaultScriptlet {
    public NotesScriptlet() {}
    
    @Override
    public void afterReportInit() throws JRScriptletException {
        System.out.println("call afterReportInit()");
        
        String jsonString = "JSONText: " + (String)getFieldValue("note_notes");
        
        System.out.println("4 jsonText: " + jsonString);
        setVariableValue("jsonText", "Variable modified by NotesScript");
        // this.setVariableValue("someVar", new String("This variable value was modified by the scriptlet."));
    }

    public String hello() throws JRScriptletException {
        return "Hello! I'm the report's scriptlet object.";
    }
}

package areports.scriptlets;

/**
 * A Scriptlet for processing notes which come from mysql database as JSON
 *
 * @author nathan
 */
import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;
import org.json.JSONArray;
import org.json.JSONObject;

public class NotesScriptlet extends JRDefaultScriptlet {

    /**
     * Default constructor
     */
    public NotesScriptlet() {
    }

    /**
     * This method extract out the relevant Information from the notes JSON
     * record and sets the parameters in the script
     *
     * @throws JRScriptletException
     */
    @Override
    public void afterDetailEval() throws JRScriptletException {
        String jsonString = (String) getFieldValue("notesJSON");
        String reportName = (String) getParameterValue("reportName");

        JSONObject noteJS = new JSONObject(jsonString);

        if (reportName.equalsIgnoreCase("NamesRecordReport")) {
            updateNamesRecordReport(noteJS);
        } else if (reportName.equalsIgnoreCase("DigitalObjectRepeatingData")) {
            updateDigitalObjectNotesReport(noteJS);
        } else {
            System.out.println("Unsupported report: " + reportName);
        }
    }

    /**
     * Method to set variable for the NamesReport
     *
     * @param noteJS
     * @throws JRScriptletException
     */
    private void updateNamesRecordReport(JSONObject noteJS) throws JRScriptletException {
        setVariableValue("descriptionType", noteJS.getString("label"));

        JSONArray subnotesJA = noteJS.getJSONArray("subnotes");

        for (int i = 0; i < subnotesJA.length(); i++) {
            JSONObject subnoteJS = subnotesJA.getJSONObject(i);

            String type = subnoteJS.getString("jsonmodel_type");

            if (type.equals("note_text")) {
                setVariableValue("descriptionNote", subnoteJS.getString("content"));
            } else {
                String content = getNoteContent(subnoteJS);
                setVariableValue("citation", content);
            }
        }
    }

    /**
     * Method to update the digital object note report with the relevant data
     *
     * @param noteJS
     */
    private void updateDigitalObjectNotesReport(JSONObject noteJS) throws JRScriptletException {
        setVariableValue("type", noteJS.getString("type"));
        setVariableValue("title", noteJS.getString("label"));
        
        String content = getNoteContent(noteJS);
        setVariableValue("content", content.trim());
    }
    
    /**
     * Method to extract the content from a note object
     * 
     * @param noteJS
     * @return 
     */
    private String getNoteContent(JSONObject noteJS) {
        JSONArray contentJA = noteJS.getJSONArray("content");
        String content = "";

        for (int j = 0; j < contentJA.length(); j++) {
            content += " " + contentJA.getString(j);
        }
        
        return content.trim();
    }
}

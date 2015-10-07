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
        } else if (reportName.equalsIgnoreCase("ResourceRepeatingData")) {
            updateResourceNotesReport(noteJS);
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
        if(noteJS.has("label")) {
            setVariableValue("descriptionType", noteJS.getString("label"));
        } else {
            setVariableValue("descriptionType", "Unlabeled");
        }

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
        // check to see we have a label, if not just return
        if(!noteJS.has("label")) { return; }
        
        setVariableValue("type", noteJS.getString("type"));
        setVariableValue("title", noteJS.getString("label"));

        String content = getNoteContent(noteJS);
        setVariableValue("content", content);
    }

    /**
     * Method to extract the content from a note object
     *
     * @param noteJS
     * @return
     */
    private String getNoteContent(JSONObject noteJS, String spacer) {
        JSONArray contentJA = noteJS.getJSONArray("content");
        String content = "";

        for (int j = 0; j < contentJA.length(); j++) {
            content += " " + contentJA.getString(j);
        }

        return content.trim() + spacer;
    }
    
    /**
     * Return the note content
     * 
     * @param noteJS
     * @return 
     */
    private String getNoteContent(JSONObject noteJS) {
        return getNoteContent(noteJS, "");
    }
    
    /**
     * Process
     *
     * @param noteJS
     */
    private void updateResourceNotesReport(JSONObject noteJS) throws JRScriptletException {
        // check to see we have a label, if not just return
        if(!noteJS.has("label")) { return; }
        
        String modelType = noteJS.getString("jsonmodel_type");
        
        setVariableValue("title", noteJS.getString("label"));
        
        // set a default content for debugging
        String content = "";

        if (modelType.equals("note_singlepart")) {
            content = getNoteContent(noteJS);
            setVariableValue("type", noteJS.getString("type"));
            setVariableValue("content", content);
        } else if (modelType.equals("note_multipart")) {
            setVariableValue("type", "Multi-Part");
            JSONArray subnotesJA = noteJS.getJSONArray("subnotes");

            for (int i = 0; i < subnotesJA.length(); i++) {
                JSONObject subnoteJS = subnotesJA.getJSONObject(i);
                String subnoteModelType = subnoteJS.getString("jsonmodel_type");

                if (subnoteModelType.equals("note_text")) {
                    content += subnoteJS.getString("content") + "\n";
                } else if (subnoteModelType.equals("note_chronology")) {
                    String events = concatEvents(subnoteJS.getJSONArray("items"));
                    content += getSubnoteTile(subnoteJS) + events + "\n";
                } else if (subnoteModelType.equals("note_definedlist")) {
                    String list = concatDefinedList(subnoteJS.getJSONArray("items"));
                    content += getSubnoteTile(subnoteJS) + list + "\n";
                } else if (subnoteModelType.equals("note_orderedlist")) {
                    String list = concatOrderedList(subnoteJS.getJSONArray("items"));
                    content += getSubnoteTile(subnoteJS) + list + "\n";
                } else {
                    content += subnoteModelType + "\n";
                }
            }

            setVariableValue("content", content);
        } else if (modelType.equals("note_index")) {
            setVariableValue("type", "Index Item");
            String items = concatIndexItems(noteJS.getJSONArray("items"));
            content += getNoteContent(noteJS, " -- ") + items + "\n";
            setVariableValue("content", content);
        } else if (modelType.equals("note_bibliography")) {
            setVariableValue("type", "Bibliography");
            String items = concatOrderedList(noteJS.getJSONArray("items"));
            content += getNoteContent(noteJS, " -- ") + items + "\n";
            setVariableValue("content", content);
        } else {
            setVariableValue("type", "NOT PROCESSED");
            setVariableValue("content", "CONTENT NOT PROCESSED ...");
        }
    }
    
    /**
     * Method to return the title of a subnote if it has it
     * @param subnoteJS
     * @return 
     */
    private String getSubnoteTile(JSONObject subnoteJS) {
        if(subnoteJS.has("title")) {
            return subnoteJS.getString("title") + " -- ";
        } else {
            return "";
        }
    }
    
    /**
     * Method to concat all events into a string
     *
     * @param jsonArray
     * @return
     */
    private String concatEvents(JSONArray itemsJA) {
        String combinedEvents = "";

        for (int i = 0; i < itemsJA.length(); i++) {
            JSONObject eventJS = itemsJA.getJSONObject(i);
            String eventDate = eventJS.getString("event_date");
            String event = eventJS.getJSONArray("events").getString(0);
            combinedEvents += event + ": " + eventDate + ", ";
        }
        
        if(combinedEvents.length() > 3) {
            return combinedEvents.substring(0, (combinedEvents.length() - 2));
        } else {
            return "";
        }
    }

    /**
     * Method to concat items from a defined list note
     * @param itemsJA
     * @return 
     */
    private String concatDefinedList(JSONArray itemsJA) {
        String combinedList = "";

        for (int i = 0; i < itemsJA.length(); i++) {
            JSONObject itemJS = itemsJA.getJSONObject(i);
            String label = itemJS.getString("label");
            String value = itemJS.getString("value");
            combinedList += label + ": " + value + ", ";
        }
        
        if(combinedList.length() > 3) {
            return combinedList.substring(0, (combinedList.length() - 2));
        } else {
            return "";
        }
    }
    
    /**
     * Method to concat items from a ordered list note
     * @param itemsJA
     * @return 
     */
    private String concatOrderedList(JSONArray itemsJA) {
        String combinedList = "";

        for (int i = 0; i < itemsJA.length(); i++) {
            String value = itemsJA.getString(i);
            combinedList += value + ", ";
        }
        
        if(combinedList.length() > 3) {
            return combinedList.substring(0, (combinedList.length() - 2));
        } else {
            return "";
        }
    }

    /**
     * Method to concat index items 
     * 
     * @param jsonArray
     * @return 
     */
    private String concatIndexItems(JSONArray itemsJA) {
        String combinedItems = "";

        for (int i = 0; i < itemsJA.length(); i++) {
            JSONObject itemJS = itemsJA.getJSONObject(i);
            String type = itemJS.getString("type");
            String value = itemJS.getString("value");
            combinedItems += type + ": " + value + ", ";
        }

        return combinedItems.substring(0, (combinedItems.length() - 2));
    }
}

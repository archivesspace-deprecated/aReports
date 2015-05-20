/**
 * @author Lee Mandell Date: Dec 28, 2005 Time: 12:44:46 PM
 */
package areports.report;

import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.design.*;
import net.sf.jasperreports.engine.JRException;
import java.awt.*;
import quick.dbtable.Column;
import quick.dbtable.DBTable;

public class JRReturnScreen {

    private static final int WIDTH = 576;

    private JasperDesign designTemplate;
    private DBTable dBTable;

    public JRReturnScreen(JasperDesign designTemplate, DBTable dBTable) {
        this.designTemplate = designTemplate;
        this.dBTable = dBTable;
    }

    public void createHeaderAndDetail() throws JRException {
        JRDesignStaticText staticText;

        JRDesignBand columnHeaderBand = (JRDesignBand) designTemplate.getColumnHeader();

        JRDesignBand detailBand = (JRDesignBand) ((JRSection) designTemplate.getDetailSection()).getBands()[0];
        
        // get the total width of the JTable columns for calculating the column width
        int[] columnWidths = getColumnWidths();
        
        int numberOfColumns = dBTable.getColumnCount();
        int xLocation = 0;
        
        String fieldName;
        JRDesignField jrDesignField;
        JRDesignTextField textField;
        JRDesignExpression textFieldExpression;
        
        
        for (int i = 0; i < numberOfColumns; i++) {
            Column column = dBTable.getColumn(i);
            String columnName = column.getHeaderValue().toString();
            int columnWidth = columnWidths[i] - 10;
                    
            staticText = new JRDesignStaticText();
            staticText.setText(columnName);
            staticText.setY(5);       
            staticText.setX(xLocation);
            staticText.setWidth(columnWidth);
            staticText.setHeight(15);
            staticText.setForecolor(Color.black);
            staticText.setBackcolor(Color.white);
            staticText.setBold(true);
            columnHeaderBand.addElement(staticText);

            fieldName = columnName;

            jrDesignField = new JRDesignField();
            jrDesignField.setName(fieldName);
            jrDesignField.setValueClass(String.class);
            designTemplate.addField(jrDesignField);

            textField = new JRDesignTextField();
            textFieldExpression = new JRDesignExpression();
            textFieldExpression.setText("$F{" + fieldName + "}");
            textField.setExpression(textFieldExpression);
            textField.setY(0);
            textField.setX(xLocation);
            textField.setWidth(columnWidth);
            textField.setHeight(15);
            textField.setStretchWithOverflow(true);
            textField.setForecolor(Color.black);
            textField.setBackcolor(Color.white);
            textField.setPrintRepeatedValues(true);
            detailBand.addElement(textField);
            
            // increment the xLocation variable
            xLocation += columnWidths[i];
        }
    }
    
    /**
     * Get the column widths by doing two loops.
     * 
     * @return 
     */
    private int[] getColumnWidths() {
        int numberOfColumns = dBTable.getColumnCount();
        int columnWidths[] = new int[numberOfColumns];
        
        
        System.out.println("Table Header: " + dBTable.getTableHeader().getWidth());
        
        // first loop find the total column widths
        float totalWidth = 0;  
        for (int i = 0; i < numberOfColumns; i++) {
            Column column = dBTable.getColumn(i);
            totalWidth += column.getWidth();
        }
        
        // now figure out the column widths based on their percent in the jtable
        for (int i = 0; i < numberOfColumns; i++) {
            Column column = dBTable.getColumn(i);
            float percent = column.getWidth()/totalWidth;
            int width = (int) (percent * WIDTH);
            columnWidths[i] = width;
            System.out.println("Relative Column Width: " + width);
        }
        
        return columnWidths;
    }
}

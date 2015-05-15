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

        int numberOfColumns = dBTable.getColumnCount();
        int columnWidth = WIDTH / numberOfColumns;

        String fieldName;
        Class columnClass;
        JRDesignField jrDesignField;
        JRDesignTextField textField;
        JRDesignExpression textFieldExpression;
        JRDesignVariable jrDesignVariable;
        JRDesignExpression variableExpression;
        
       
        for (int i = 0; i < numberOfColumns; i++) {
            
            Column column = dBTable.getColumn(i);
            String columnName = column.getHeaderValue().toString();
            
            
            staticText = new JRDesignStaticText();
            staticText.setText(columnName);
            staticText.setY(5);
            staticText.setX(i * columnWidth);
            staticText.setHeight(15);
            staticText.setWidth(columnWidth);
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
            textField.setX(i * columnWidth);
            textField.setHeight(15);
            textField.setWidth(columnWidth);
            textField.setStretchWithOverflow(true);
            textField.setForecolor(Color.black);
            textField.setBackcolor(Color.white);
            textField.setPrintRepeatedValues(true);
            detailBand.addElement(textField);
        }
    }
}

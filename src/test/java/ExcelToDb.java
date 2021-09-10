import java.io.FileInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelToDb {
    public static void main( String [] args ) {
        String fileName="F:\\exceldata\\Predictions_sprint_6.xlsx";
        Vector dataHolder=read(fileName);
        saveToDatabase(dataHolder);
    }
    public static Vector read(String fileName)    {
        Vector cellVectorHolder = new Vector();
        try{
            FileInputStream myInput = new FileInputStream(fileName);
            //POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
            XSSFWorkbook myWorkBook = new XSSFWorkbook(myInput);
            XSSFSheet mySheet = myWorkBook.getSheetAt(3);
            Iterator rowIter = mySheet.rowIterator();
            while(rowIter.hasNext()){
                XSSFRow myRow = (XSSFRow) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                //Vector cellStoreVector=new Vector();
                List list = new ArrayList();
                while(cellIter.hasNext()){
                    XSSFCell myCell = (XSSFCell) cellIter.next();
                    list.add(myCell);
                }
                cellVectorHolder.addElement(list);
            }
        }catch (Exception e){e.printStackTrace(); }
        return cellVectorHolder;
    }
    private static void saveToDatabase(Vector dataHolder) {
        String sprintid="";
        String usid="";
        String tcid="";
        String   tagline="";
        String testcasetile="";
        String teststeps = "";
        String probabilityfailure = "";
        String predictions = "";

        for(Iterator iterator = dataHolder.iterator();iterator.hasNext();) {
            List list = (List) iterator.next();
            sprintid = list.get(0).toString();
            usid = list.get(1).toString();
            tcid = list.get(2).toString();
            tagline = list.get(3).toString();
            testcasetile = list.get(4).toString();
            teststeps = list.get(5).toString();
            probabilityfailure = list.get(6).toString();
            predictions = list.get(7).toString();

            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/automationdb", "root", "root");
                System.out.println("connection made...");
                PreparedStatement stmt=con.prepareStatement("INSERT INTO predictions(sprintid,usid,tcid,tagline,testcasetile,teststeps,probabilityfailure,predictions) VALUES(?,?,?,?,?,?,?,?)");
                stmt.setString(1, sprintid);
                stmt.setString(2, usid);
                stmt.setString(3, tcid);
                stmt.setString(4, tagline);
                stmt.setString(5, testcasetile);
                stmt.setString(6, teststeps);
                //stmt.setFloat(7, Float.parseFloat(probabilityfailure));
                stmt.setString(7, probabilityfailure);
                stmt.setString(8, predictions);
                stmt.executeUpdate();

                System.out.println("Data is inserted");
                stmt.close();
                con.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }



    }
}
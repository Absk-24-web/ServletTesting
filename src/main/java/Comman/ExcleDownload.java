package Comman;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import DataBase.JDBCUtils;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

public class ExcleDownload {
	private static PasswordEncryptDcrypt passwordEncryptDcrypt;

//    public static void main(String[] args) {
//        new ExcleDownload().export();
//    }
	static {
		 passwordEncryptDcrypt = new PasswordEncryptDcrypt();
	}

    public void export() {
   
        String excelFilePath = "C:\\Users\\df-dev16\\Desktop\\Users.xlsx";

        try (Connection connection = JDBCUtils.getConnection()) {
            String sql = "SELECT * FROM users";

            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery(sql);

            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Users");

            writeHeaderLine(sheet);

            writeDataLines(result, workbook, sheet);

            FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            workbook.write(outputStream);
//            workbook.close();

            statement.close();

        } catch (SQLException e) {
            System.out.println("Datababse error:");
            e.printStackTrace();
        } catch (IOException e) {
//            JOptionPane.showMessageDialog(this, "Please close the previous use file");
            System.out.println("File IO error:");
            e.printStackTrace();
        }
    }

    private void writeHeaderLine(XSSFSheet sheet) {

        Row headerRow = sheet.createRow(0);

        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Id");

        headerCell = headerRow.createCell(1);
        headerCell.setCellValue("Name");

        headerCell = headerRow.createCell(2);
        headerCell.setCellValue("E-mail");

        headerCell = headerRow.createCell(3);
        headerCell.setCellValue("Password");
    }

    private void writeDataLines(ResultSet result, XSSFWorkbook workbook,
                                XSSFSheet sheet) throws SQLException {
        int rowCount = 1;

        while (result.next()) {
            String id = result.getString("id");
            String name = result.getString("name");
            String email = result.getString("email");
            String password = passwordEncryptDcrypt.decrpyt(result.getString("password"));

            Row row = sheet.createRow(rowCount++);

            int columnCount = 0;
            Cell cell = row.createCell(columnCount++);
            cell.setCellValue(id);

            cell = row.createCell(columnCount++);

            CellStyle cellStyle = workbook.createCellStyle();
            CreationHelper creationHelper = workbook.getCreationHelper();
            cellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd-MM-yyyy HH:mm:ss"));
            cell.setCellStyle(cellStyle);

            cell.setCellValue(name);

            cell = row.createCell(columnCount++);
            cell.setCellValue(email);

            cell = row.createCell(columnCount++);
            cell.setCellValue(password);

        }
    }

}
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ManageFile {

    public static void createExcelFile(Map<String, Legend> legendsData) throws IOException {

        //create blank workbook
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Create a blank sheet
        XSSFSheet sheet = workbook.createSheet("Legends.xlsx");

        int rowCount = 0;
        for (Map.Entry<String, Legend> el : legendsData.entrySet())
        {
            Row row = sheet.createRow(rowCount++);

            ArrayList<String> legend = el.getValue().createArrayList();
            int cellnum = 0;
            for (String x : legend)
            {
                Cell cell = row.createCell(cellnum++);
                cell.setCellValue(x);
            }
        }
        try
        {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("Legends.xlsx"));
            workbook.write(out);
            out.close();
            System.out.println("Legends.xlsx has been created successfully");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static HashMap<String, Legend> readExcelFile() throws IOException {
        FileInputStream f = new FileInputStream(new File("Legends.xlsx"));

        Workbook workbook = new XSSFWorkbook(f);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();

        HashMap<String, Legend> readLegendData = new HashMap<>();
        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();
            ArrayList<String> legend = new ArrayList<>();
            while (cellIterator.hasNext()) {

                Cell cell = cellIterator.next();

                legend.add(cell.getStringCellValue());
//                System.out.println(cell.getStringCellValue());
//                System.out.print(" ; ");
            }
//            System.out.println();

            Legend l = new Legend(legend.get(0), legend.get(1), legend.get(2), legend.get(3), legend.get(4), legend.get(5), legend.get(6), legend.get(7), legend.get(8));

            readLegendData.put(legend.get(0), l);
        }

        f.close();

        return readLegendData;
    }
}

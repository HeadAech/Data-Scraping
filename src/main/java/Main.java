import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

//klasy do działania z plikami
import javax.swing.*;
import java.io.*;
//


import java.lang.reflect.Array;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
//        jframeInit();
//        readLegendData();

        createExcelFile();
        readExcelFile();
    }

    private static Legend getLegendInfo(String name) throws IOException {
        Document legendPage = null;
        try{
            legendPage = Jsoup.connect("https://apexlegends.gamepedia.com/" + name).get();
        }
            catch(IOException e){
            System.out.println("Wystąpił błąd: " + e);
        }
        Element infobox = legendPage.getElementsByClass("infobox").first();
        Elements rows = infobox.getElementsByClass("infobox-row");

        Map<String, String> legendInfo = new TreeMap<>();
        legendInfo.put("Nickname", name);
        for(int i = 0; i < rows.size(); i++){
            String head = rows.get(i).getElementsByClass("infobox-row-name").first().text();
            String value = rows.get(i).getElementsByClass("infobox-row-value").first().text();
            legendInfo.put(head, value);
        }

        return new Legend(legendInfo.get("Nickname"),legendInfo.get("Real Name"), legendInfo.get("Gender"), legendInfo.get("Age"), legendInfo.get("Weight"), legendInfo.get("Height"), legendInfo.get("Legend Type"), legendInfo.get("Home World"));
    }

    private static void jframeInit(){
        JFrame frame = new JFrame("Apex Legends Info");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280,720);
        JButton button = new JButton("Press");
        frame.getContentPane().add(button); // Adds Button to content pane of frame
        frame.setVisible(true);
    }

    public static Map<String, Legend> legendsData() throws IOException {
        Document doc = null;

        try{
            doc = Jsoup.connect("https://apexlegends.gamepedia.com/Apex_Legends_Wiki").get();
        }
        catch(IOException e){
            System.out.println("Wystąpił błąd: " + e);
        }

        Element section = doc.getElementById("fp-2");
        Elements legendsImages = section.getElementsByClass("fplink-inner").select("img");

        ArrayList<String> legends = new ArrayList<>();

        for (int i = 0; i < legendsImages.size(); i++){
            String attr = legendsImages.get(i).attr("alt");
            int index = attr.indexOf(" ");
            legends.add(attr.substring(0, index));
        }
        HashMap<String, Legend> legendsData = new HashMap<>();

        for(int i = 0; i < legends.size(); i++){
            legendsData.put(legends.get(i).toLowerCase(), getLegendInfo(legends.get(i)));
        }

        System.out.println(legendsData);

        boolean cont = false;
//        while (!cont){
//            System.out.println(legends);
//            System.out.println("O jakiej legendzie chcesz wyświetlić informacje?");
//
//            Scanner scan = new Scanner(System.in);
//            String legend = scan.nextLine();
//            legend = legend.toLowerCase();
//            if(legendsData.get(legend)==null){
//                System.out.println("Nie ma takiej legendy!");
//            }else{
//                System.out.println(legendsData.get(legend));
//                cont = true;
//            }
//            scan.close();
//
//        }
        return legendsData;
    }

    public static void createExcelFile() throws IOException {
       Map<String, Legend> legendsData = legendsData();

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

    private static void readExcelFile() throws IOException {
        FileInputStream inputStream = new FileInputStream(new File("Legends.xlsx"));

        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet firstSheet = workbook.getSheetAt(0);
        Iterator<Row> iterator = firstSheet.iterator();

        while (iterator.hasNext()) {
            Row nextRow = iterator.next();
            Iterator<Cell> cellIterator = nextRow.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();

                switch (cell.getCellType()) {
                    case Cell.CELL_TYPE_STRING:
                        System.out.print(cell.getStringCellValue());
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        System.out.print(cell.getBooleanCellValue());
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        System.out.print(cell.getNumericCellValue());
                        break;
                }
                System.out.print(" - ");
            }
            System.out.println();
        }

        inputStream.close();
    }

}

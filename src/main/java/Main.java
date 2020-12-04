import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    public static void main(String[] args) throws IOException {
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
        Map<String, Legend> legendsData = new TreeMap<>();

        for(int i = 0; i < legends.size(); i++){
            legendsData.put(legends.get(i).toLowerCase(), getLegendInfo(legends.get(i)));
        }

        boolean cont = false;
        while (!cont){
            System.out.println(legends);
            System.out.println("O jakiej legendzie chcesz wyświetlić informacje?");

            Scanner scan = new Scanner(System.in);
            String legend = scan.nextLine();
            legend = legend.toLowerCase();
            if(legendsData.get(legend)==null){
                System.out.println("Nie ma takiej legendy!");
            }else{
                System.out.println(legendsData.get(legend));
                cont = true;
            }
        }
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

}

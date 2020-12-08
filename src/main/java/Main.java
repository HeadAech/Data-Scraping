

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


//klasy do działania z plikami
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
//


import java.net.URL;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {

        JFrame frame = new JFrame("Apex Legends Info");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(1200, 800));
        frame.setBounds(100, 100, 1200, 800);
//        frame.setSize(1280,720);

        Object[] options = {"Tak",
                "Nie, wczytaj plik Excel"};
        int n = JOptionPane.showOptionDialog(frame,
                "Pobrać dane na nowo?",
                "Synchronizacja danych",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        HashMap<String, Legend> legendsData = new HashMap<>();

        if(n == 0){
            ManageFile.createExcelFile(legendsData());
        }
        try{
            legendsData = ManageFile.readExcelFile();
        }
        catch(FileNotFoundException e){
            System.out.println("Wystąpił błąd: Nie znaleziono pliku, proszę zsynchronizować dane przy uruchomieniu programu." + e);
            JOptionPane.showMessageDialog(frame,
                    "Nie znaleziono pliku, proszę zsynchronizować dane przy uruchomieniu programu.",
                    "Wystąpił błąd",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        System.out.println(legendsData);
        Container container = frame.getContentPane();
        frame.setLayout(new GridLayout(0, 3));

        for (Map.Entry<String, Legend> el : legendsData.entrySet()){
            try {
                String path = el.getValue().getImage_src();
                System.out.println("Get Image from " + path);
                URL url = new URL(path);
                BufferedImage image = ImageIO.read(url);
                System.out.println("Load image into frame...");

                //test

                Image dimg = image.getScaledInstance(150, 150,
                        Image.SCALE_SMOOTH);
                //
                JButton btn = new JButton(new ImageIcon(dimg));

                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame legendFrame = new JFrame();
                        JPanel legendInfoContainer = new JPanel();
                        JPanel dataContainer = new JPanel();
                        JPanel imageContainer = new JPanel();

                        legendInfoContainer.setLayout(new GridLayout(0, 2));
                        dataContainer.setLayout(new GridLayout(0, 1));
                        ArrayList<JLabel> labels = new ArrayList<>();

                        labels.add(new JLabel("Pseudonim: " + el.getValue().getNickname()));
                        labels.add(new JLabel("Imię i nazwisko: " + el.getValue().getName()));
                        labels.add(new JLabel("Płeć: " + el.getValue().getGender()));
                        labels.add(new JLabel("Wiek: " + el.getValue().getAge()));
                        labels.add(new JLabel("Waga: " + el.getValue().getWeight()));
                        labels.add(new JLabel("Wzrost: " + el.getValue().getHeight()));
                        labels.add(new JLabel("Klasa: " + el.getValue().getType()));
                        labels.add(new JLabel("Pochodzenie: " + el.getValue().getHome_world()));

                        for(JLabel label : labels){
                            label.setFont(new Font("Verdana", Font.PLAIN, 18));

                            dataContainer.add(label);
                        }
                        imageContainer.add(new JLabel(new ImageIcon(image)));
                        legendInfoContainer.add(dataContainer);
                        legendInfoContainer.add(imageContainer);
                        legendFrame.add(legendInfoContainer);
                        legendFrame.setPreferredSize(new Dimension(800, 600));
                        legendFrame.setBounds(100, 100, 800, 600);
                        legendFrame.pack();
                        legendFrame.setVisible(true);
                    }
                });

                container.add(btn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        frame.pack();
        frame.setVisible(true);
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
        String img = infobox.select("img").first().attr("src");
        System.out.println(img);
        Elements rows = infobox.getElementsByClass("infobox-row");

        Map<String, String> legendInfo = new TreeMap<>();
        legendInfo.put("Nickname", name);
        for(int i = 0; i < rows.size(); i++){
            String head = rows.get(i).getElementsByClass("infobox-row-name").first().text();
            String value = rows.get(i).getElementsByClass("infobox-row-value").first().text();
            legendInfo.put(head, value);
        }
        legendInfo.put("Image Src", img);
        return new Legend(legendInfo.get("Nickname"),legendInfo.get("Real Name"), legendInfo.get("Gender"), legendInfo.get("Age"), legendInfo.get("Weight"), legendInfo.get("Height"), legendInfo.get("Legend Type"), legendInfo.get("Home World"), legendInfo.get("Image Src"));
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

        return legendsData;
    }


}

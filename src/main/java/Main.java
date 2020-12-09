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
        frame.setMinimumSize(new Dimension(1200, 800));
        frame.setBounds(100, 100, 1200, 800);

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
            ManageFile.createExcelFile(Data.legendsData());
        }else if (n == -1){
            System.exit(0);
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

        Container BtnsContainer = frame.getContentPane();
        frame.setLayout(new GridLayout(0, 3));

        for (Map.Entry<String, Legend> el : legendsData.entrySet()){
            try {
                String path = el.getValue().getImage_src();
                System.out.println("Pobieranie zdjęcia... URL: " + path);
                URL url = new URL(path);
                BufferedImage image = ImageIO.read(url);

                //skalowanie
                Image dimg = image.getScaledInstance(150, 150, Image.SCALE_SMOOTH);

                JButton btn = new JButton(new ImageIcon(dimg));
                btn.setBackground(new Color(229, 225, 216));

                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFrame legendFrame = new JFrame(el.getValue().getNickname());
                        JPanel legendInfoContainer = new JPanel();
                        JPanel dataContainer = new JPanel();
                        JPanel imageContainer = new JPanel();

                        legendInfoContainer.setLayout(new GridLayout(0, 2));

                        dataContainer.setLayout(new GridLayout(0, 1));
                        ArrayList<JLabel> labels = new ArrayList<>();

                        labels.add(new JLabel("<html><div style='margin: 10px;'><b>Pseudonim:</b> " + el.getValue().getNickname() + "</div></html>"));
                        labels.add(new JLabel("<html><div style='margin: 10px;'><b>Imię i nazwisko:</b> " + el.getValue().getName() + "</div></html>"));
                        labels.add(new JLabel("<html><div style='margin: 10px;'><b>Płeć:</b> " + el.getValue().getGender() + "</div></html>"));
                        labels.add(new JLabel("<html><div style='margin: 10px;'><b>Wiek:</b> " + el.getValue().getAge() + "</div></html>"));
                        labels.add(new JLabel("<html><div style='margin: 10px;'><b>Waga:</b> " + el.getValue().getWeight() + "</div></html>"));
                        labels.add(new JLabel("<html><div style='margin: 10px;'><b>Wzrost:</b> " + el.getValue().getHeight() + "</div></html>"));
                        labels.add(new JLabel("<html><div style='margin: 10px;'><b>Klasa:</b> " + el.getValue().getType() + "</div></html>"));
                        labels.add(new JLabel("<html><div style='margin: 10px;'><b>Pochodzenie:</b> " + el.getValue().getHome_world() + "</div></html>"));

                        for(JLabel label : labels){
                            label.setFont(new Font("Verdana", Font.PLAIN, 18));

                            dataContainer.add(label);
                        }
                        imageContainer.add(new JLabel(new ImageIcon(image)));

                        legendInfoContainer.setBackground(new Color(229, 225, 216));
                        dataContainer.setBackground(new Color(229, 225, 216));
                        imageContainer.setBackground(new Color(229, 225, 216));

                        legendInfoContainer.add(dataContainer);
                        legendInfoContainer.add(imageContainer);

                        legendFrame.add(legendInfoContainer);
                        legendFrame.setPreferredSize(new Dimension(800, 600));
                        legendFrame.setMinimumSize(new Dimension(800, 600));
                        legendFrame.setBounds(100, 100, 800, 600);
                        legendFrame.pack();
                        legendFrame.setVisible(true);
                    }
                });

                BtnsContainer.add(btn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        frame.pack();
        frame.setVisible(true);
    }

}

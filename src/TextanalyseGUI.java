import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Hauptklasse der Applikation mit dem GUI und main-Methode, welche
 * nichts anderes macht, als das GUI zu starten.
 *
 */
public class TextanalyseGUI {

    private static final String VERSION = "Version 0.1";
    private static JFileChooser dateiauswahldialog = 
            new JFileChooser(System.getProperty("user.dir"));
    private static ArrayList<Member> memberliste;
    static final String rootpath = "/reviewsbymembers50000";
    private static TextStatistik statistik;
    private static ArrayList<Bewertung> treffer;
    private JPanel contentPane;
    private JFrame fenster;
    private JLabel statusLabel;
    private JPanel hauptInhalt;
    private JPanel rechterInhalt;
    private JScrollPane scrollText;
    
    public TextanalyseGUI() {
        fensterErzeugen();
        treffer = new ArrayList<Bewertung>();
    }
    
    public static void main(String[] args) throws IOException {      
        TextanalyseGUI gui = new TextanalyseGUI();                 
    }
    
    private void fensterErzeugen() {
        fenster = new JFrame("Textanalyse");
        contentPane = (JPanel)fenster.getContentPane();
        contentPane.setBorder(new EmptyBorder(12, 12, 12, 12));
        menuezeileErzeugen(fenster);  
        contentPane.setLayout(new BorderLayout(6, 6));  
        statusLabel = new JLabel(VERSION);
        contentPane.add(statusLabel, BorderLayout.SOUTH);
        
        erstelleButtons();
         
        hauptInhalt = new JPanel();
        hauptInhalt.setLayout(new GridLayout(0,1));
        scrollText = new JScrollPane(new JTextArea("Keine Treffer", 50, 80));
        hauptInhalt.add(scrollText);
        contentPane.add(hauptInhalt, BorderLayout.CENTER);
        
        rechterInhalt = new JPanel();
        rechterInhalt.setLayout(new GridLayout(0,1));
        contentPane.add(rechterInhalt, BorderLayout.EAST);
        
        
        fenster.pack();
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        fenster.setLocation(d.width/2 - fenster.getWidth()/2, 
                            d.height/2 - fenster.getHeight()/2);
        fenster.setVisible(true);
    }
        
    private void dateiOeffnen() {
        dateiauswahldialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dateiauswahldialog.setAcceptAllFileFilterUsed(false);
        
        int ergebnis = dateiauswahldialog.showOpenDialog(fenster);
        if (ergebnis != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File selektiertesVerzeichnis = dateiauswahldialog.getSelectedFile();
        statusAnzeigen("Datei wird geladen...");
        statistik = new TextStatistik();
        
        TextEinlesen textlesen = new TextEinlesen(statistik);
        memberliste = textlesen.datenEinlesen(selektiertesVerzeichnis.getPath());
        statusAnzeigen("Datei geladen.");
        fenster.pack();     
    }
    
    private void statusAnzeigen(String text)
    {
        statusLabel.setText(text);
    }
    
    private void beenden() {
        System.exit(0);
    }
    
    private void statistikAnzeigen() {
        
        JOptionPane.showMessageDialog(fenster, 
                "Anzahl Dokumente: " + statistik.getAnzahlDokumente() + "\n" +
                "Gesamtgroesse in KB: " + statistik.getGesamtGroesse()/1024 + "\n" +
                "Anzahl unterschiedliche Wörter: " + statistik.getWortliste().size() + "\n" +
                "Anzahl Wörter: " + statistik.getAnzahlWoerter() + "\n" +
                "Durchschnittliche Textlänge: " + 
                statistik.getAnzahlZeichen()/statistik.getAnzahlDokumente() +
                " Zeichen",
                "Statistik", 
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void suche() {
        String eingabe = JOptionPane.showInputDialog("Suche nach");
        ArrayList<Bewertung> treffer = statistik.sucheWort(eingabe);
        trefferAnzeigen(treffer);
        System.out.println("Suche ergab " + treffer.size() + " Treffer.");
        System.out.println("Erster Treffer: \n" + treffer.get(0).getTitel() + "\n" + 
                           treffer.get(0).getInhalt());
    }
    
    private void sucheAusser() {
        String eingabe = JOptionPane.showInputDialog("Suche nach ... ohne ...");
        ArrayList<Bewertung> treffer = statistik.sucheAusser(eingabe);
        trefferAnzeigen(treffer);
        System.out.println("Suche ergab " + treffer.size() + " Treffer.");
        System.out.println("Erster Treffer: \n" + treffer.get(0).getTitel() + "\n" + 
                           treffer.get(0).getInhalt());
    }
      
    private void sucheApproximativ() {
        String eingabe = JOptionPane.showInputDialog("Approximative Suche nach:");
        ArrayList<Bewertung> treffer = statistik.fuzzySearch(eingabe);
        trefferAnzeigen(treffer);    
    }
    
    private void filternMinimum() {
        String eingabe = JOptionPane.showInputDialog("Filtern nach Länge - Minimum");
        treffer = statistik.filterDokumenteNachMinimum(Integer.parseInt(eingabe), treffer);
        trefferAnzeigen(treffer);
    }

    private void filternMaximum() {
        String eingabe = JOptionPane.showInputDialog("Filtern nach Länge - Maximum");
        treffer = statistik.filterDokumenteNachMaximum(Integer.parseInt(eingabe), treffer);
        trefferAnzeigen(treffer);
    }
    
    private void filternMinMax() {
        String eingabe = JOptionPane.showInputDialog("Filtern nach Länge - Minimum und Maximum");
        String[] eingabewerte = eingabe.split(" ");
        if (eingabewerte.length == 2) {
        treffer = statistik.filterDokumenteNachMaxUndMin(Integer.parseInt(eingabewerte[0]), Integer.parseInt(eingabewerte[1]), treffer);
        trefferAnzeigen(treffer);
        }
    }
    
    private void trefferAnzeigen(ArrayList<Bewertung> treffer) {
        statusAnzeigen(treffer.size() + " Treffer");
        this.treffer = treffer;
        hauptInhalt.removeAll();
        if(treffer == null || treffer.isEmpty()) {       
            hauptInhalt.add(new JTextArea("Keine Treffer", 50, 80));
        } else {
            String[] titelListe = new String[treffer.size()];
            Bewertung[] bewertungListe = new Bewertung[treffer.size()];
            int i = 0;
            for(Bewertung b : treffer) {
                titelListe[i] = b.getTitel();
                bewertungListe[i] = b;
                i++;
            }
            JList<Bewertung> list = new JList<Bewertung>(bewertungListe);
            
            JScrollPane scrollPane = new JScrollPane(list);
            DefaultListSelectionModel m = new DefaultListSelectionModel();
            m.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            m.setLeadAnchorNotificationEnabled(false);
            list.setSelectionModel(m);
                    
            MouseListener mouseListener = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {

                       Bewertung selectedItem = list.getSelectedValue();
                       
                       JTextArea selectedText = new JTextArea(selectedItem.getInhalt());
                       selectedText.setLineWrap(true);
                       selectedText.setWrapStyleWord(true);
                       selectedText.setSize(400,40);
                       
                       JOptionPane.showMessageDialog(fenster, 
                               selectedText,
                               selectedItem.getTitel(), 
                               JOptionPane.INFORMATION_MESSAGE);
                     }
                }
            };
            list.addMouseListener(mouseListener);          
            
            hauptInhalt.add(scrollPane);
            hauptInhalt.setSize(50, 80);         
        }
        fenster.pack();
    } 
    
    private void zeigListeWoerter() {
        JList<String> list = new JList(statistik.getWortliste().keySet().toArray());   
        JScrollPane scrollPane = new JScrollPane(list);
        DefaultListSelectionModel m = new DefaultListSelectionModel();
        m.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        m.setLeadAnchorNotificationEnabled(false);
        list.setSelectionModel(m);
             
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                   ArrayList<Bewertung> selectedItem = statistik.sucheWort(list.getSelectedValue());
                   trefferAnzeigen(selectedItem);
                }
            }
        };
        list.addMouseListener(mouseListener);
        rechterInhalt.removeAll();
        rechterInhalt.add(scrollPane);
        rechterInhalt.setSize(50, 80);
        fenster.pack();
    }
    
    private void menuezeileErzeugen(JFrame fenster) {
        JMenuBar menuezeile = new JMenuBar();
        fenster.setJMenuBar(menuezeile);
        
        JMenu menue;
        JMenuItem eintrag;
        
        menue = new JMenu("Datei");
        menuezeile.add(menue);
        
        eintrag = new JMenuItem("Textverzeichnis wählen...");
        eintrag.addActionListener(e -> { 
            dateiOeffnen(); 
            statistikAnzeigen();
        });
        menue.add(eintrag);
        menue.addSeparator();
        
        eintrag = new JMenuItem("Wortliste anzeigen");
        eintrag.addActionListener(e -> zeigListeWoerter());
        menue.add(eintrag);
        menue.addSeparator();
        
        eintrag = new JMenuItem("Programm beenden");
        eintrag.addActionListener(e -> beenden()); 
        menue.add(eintrag);
        
        menue = new JMenu("Statistik");
        menuezeile.add(menue);
        
        eintrag = new JMenuItem("Statistik anzeigen");
        eintrag.addActionListener(e -> statistikAnzeigen());
        menue.add(eintrag);
        
        menue = new JMenu("Suche");
        menuezeile.add(menue);
        
        eintrag = new JMenuItem("Suche nach...");
        eintrag.addActionListener(e -> suche());
        menue.add(eintrag);
        
        eintrag = new JMenuItem("Suche nach... ohne ...");
        eintrag.addActionListener(e -> sucheAusser());
        menue.add(eintrag);
        
        eintrag = new JMenuItem("Approximative Suche");
        eintrag.addActionListener(e -> sucheApproximativ());
        menue.add(eintrag);
        menue.addSeparator();
        
        eintrag = new JMenuItem("Filtern nach minimaler Länge");
        eintrag.addActionListener(e -> filternMinimum());
        menue.add(eintrag);
        
        eintrag = new JMenuItem("Filtern nach maximaler Länge");
        eintrag.addActionListener(e -> filternMaximum());
        menue.add(eintrag);
        
        eintrag = new JMenuItem("Filtern nach minimaler und maximaler Länge");
        eintrag.addActionListener(e -> filternMinMax());
        menue.add(eintrag);
    }
    
    private void erstelleButtons() {
        JPanel werkzeugleiste = new JPanel();
        werkzeugleiste.setLayout(new GridLayout(0, 1));
        
        JButton einfacheSucheKnopf = new JButton("Suche nach Wort");
        einfacheSucheKnopf.addActionListener(e -> suche());
        werkzeugleiste.add(einfacheSucheKnopf);
        
        JButton sucheAusserKnopf = new JButton("Suche Wort1 ohne Wort2");
        sucheAusserKnopf.addActionListener(e -> sucheAusser());
        werkzeugleiste.add(sucheAusserKnopf);
        
        JButton sucheApproximativKnopf = new JButton("Approximative Suche");
        sucheApproximativKnopf.addActionListener(e -> sucheApproximativ());
        werkzeugleiste.add(sucheApproximativKnopf);
        
        JButton listeWoerter = new JButton("Liste aller Wörter");
        listeWoerter.addActionListener(e -> zeigListeWoerter());
        werkzeugleiste.add(listeWoerter);
        
        JButton minFilter = new JButton("Filtern nach Minimum");
        minFilter.addActionListener(e -> filternMinimum());
        werkzeugleiste.add(minFilter);
        
        JButton maxFilter = new JButton("Filtern nach Maximum");
        maxFilter.addActionListener(e -> filternMaximum());
        werkzeugleiste.add(maxFilter);
        
        JButton minMaxFilter = new JButton("Filtern nach Minimum und Maximum");
        minMaxFilter.addActionListener(e -> filternMinMax());
        werkzeugleiste.add(minMaxFilter);
            
        JPanel flow = new JPanel();
        flow.add(werkzeugleiste);  
        contentPane.add(flow, BorderLayout.WEST);
    }
}

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.io.*;

/**
 * Liest Text aus Dateien eines Verzeichnisses ein.
 * 
 *
 */

public class TextEinlesen {
    private int anzahlDokumente;
    private long gesamtGroesse;
    private long anzahlZeichen;
    private HashMap<String, ArrayList<Bewertung>> wortliste;
    private TextStatistik statistik;
    
    public TextEinlesen(TextStatistik statistik) {
        this.statistik = statistik;
        wortliste = new HashMap<String, ArrayList<Bewertung>>();
    }
    
    public ArrayList<Member> datenEinlesen(String foldername) {
        ArrayList<Member> memberverzeichnis = new ArrayList<Member>();    
        File folder = new File(foldername);
        
        // Namen aller Verzeichnisse erstellen und durchiterieren, 
        // um Bewertungen einzulesen      
        File[] listOfFolders = folder.listFiles();
        for (File file : listOfFolders) {       
             
            String memberpfad = foldername + "/" + file.getName();          
            File memberBewertungen = new File(memberpfad);            
            Member member = new Member(file.getName());
            
            // Dateinamen aller Bewertungen erstellen und durchiterieren, 
            // um Inhalt auszulesen
            ArrayList<Bewertung> bewertungen = new ArrayList<Bewertung>();
            File[] listOfReviews = memberBewertungen.listFiles();
            for (File bewertungDatei : listOfReviews) {
                String inhalt = "";
                
                //Pfad zu einzelner Bewertung
                String bewertungspfad = memberpfad + "/" + bewertungDatei.getName();
                            
                // Inhalt der einzelnen Bewertung auslesen, Bewertung in Liste
                // hinzufügen
                try(BufferedReader reader = 
                        new BufferedReader(new FileReader(bewertungspfad))) {
                    inhalt = reader.readLine();
                    String line = reader.readLine();
                    while (line != null) {
                        inhalt = inhalt + "\n" + line;
                    }               
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                
                String titel = bewertungDatei.getName();
                titel = titel.substring(0, titel.lastIndexOf('.'));           
                Bewertung bewertung = new Bewertung(titel, inhalt, member);
                bewertung.setLaenge(bewertungDatei.length());
                generiereStatistik(bewertungDatei, bewertung, inhalt);      
                bewertungen.add(bewertung);
                statistik.dokumentliste.add(bewertung);
            }
            
            // zu jedem Member die Nummer und alle Bewertungen festhalten und 
            // ins Memberverzeichnis aufnehmen
            member.addBewertungen(bewertungen);
            memberverzeichnis.add(member);
        }
        
        statistik.setAnzahlDokumente(anzahlDokumente);
        statistik.setGesamtGroesse(gesamtGroesse);
        statistik.setWortliste(wortliste);
        statistik.setAnzahlZeichen(anzahlZeichen);
        return  memberverzeichnis;      
    }
    
    private void generiereStatistik(File bewertungDatei, Bewertung bewertung, String inhalt) {
        inhalt = inhalt.toLowerCase();
        gesamtGroesse += bewertungDatei.length();
        anzahlDokumente++;
        anzahlZeichen += inhalt.length();
        Set<String> woerter = new HashSet<String>(Arrays.asList(inhalt.split("\\P{L}+")));
        
        for(String wort : woerter) {
            if (wortliste.containsKey(wort)) {
                ArrayList<Bewertung> bewertungen = wortliste.get(wort);           
                bewertungen.add(bewertung);
                wortliste.put(wort, bewertungen);
            } else {
                ArrayList<Bewertung> bewertungen = new ArrayList<Bewertung>();
                bewertungen.add(bewertung);
                wortliste.put(wort, bewertungen);
            }
        }      
    }
}

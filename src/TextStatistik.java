import java.util.*;
/**
 * Klasse, welche die Wortliste und die Dokumentliste hält und Statistiken dazu
 * speichert. Die Klasse bietet ausserdem Methoden an,
 * um die Daten zu durchsuchen und zu filtern.
 *
 */

public class TextStatistik {
    private int anzahlDokumente;
    private long gesamtGroesse;
    private long anzahlZeichen;
    private HashMap<String, ArrayList<Bewertung>> wortliste;
    public ArrayList<Bewertung> dokumentliste = new ArrayList<>();
    
    public TextStatistik() {
        
    }
    
    public TextStatistik(int anzahlDokumente, long gesamtGroesse) {
        this.anzahlDokumente = anzahlDokumente;
        this.gesamtGroesse = gesamtGroesse;
    }
    
    public int getAnzahlDokumente() {
        return anzahlDokumente;
    }
    
    public void setAnzahlDokumente(int anzahlDokumente) {
        this.anzahlDokumente = anzahlDokumente;
    }

    public long getGesamtGroesse() {
        return gesamtGroesse;
    }

    public void setGesamtGroesse(long gesamtGroesse) {
        this.gesamtGroesse = gesamtGroesse;
    }

    public HashMap<String, ArrayList<Bewertung>> getWortliste() {
        return wortliste;
    }

    public void setWortliste(HashMap<String, ArrayList<Bewertung>> wortliste) {
        this.wortliste = wortliste;
    }

    public long getAnzahlZeichen() {
        return anzahlZeichen;
    }

    public void setAnzahlZeichen(long anzahlZeichen) {
        this.anzahlZeichen = anzahlZeichen;
    }
    
    public long getAnzahlWoerter() {
        long anzWoerter = 0;
        Collection<ArrayList<Bewertung>> worthauefigkeiten = getWortliste().values();
        for (ArrayList<Bewertung> b : worthauefigkeiten) {
            anzWoerter += b.size();
        }
        return anzWoerter;
    }
       
    public ArrayList<Bewertung> sucheWort(String wort) {
        ArrayList<Bewertung> treffer = null;
        if (wortliste.containsKey(wort)) {
            treffer = wortliste.get(wort);
        }   
        return treffer;
    }

    public ArrayList<Bewertung> sucheAusser(String eingabe) {
        String[] worte = eingabe.split(" ");
        String wort1 = worte[0];
        String wort2 = worte[1];
        List<Bewertung> treffer1 = sucheWort(wort1);
        List<Bewertung> treffer2 = sucheWort(wort2);
        ArrayList<Bewertung> treffer = new ArrayList<>();
        for(Bewertung bewertung : treffer1) {
            if (!treffer2.contains(bewertung)) {
                    treffer.add(bewertung);
            }
        }
        return treffer;
    }
    
    public ArrayList<Bewertung> filterDokumenteNachMinimum(long minimum, 
                                                           ArrayList<Bewertung> treffer) {  
        ArrayList<Bewertung> gefiltertNachLaenge = new ArrayList<>();
            for (int i = 0; i < treffer.size(); i++) {
                if (minimum < treffer.get(i).getLaenge()) {
                    gefiltertNachLaenge.add(treffer.get(i));
                }
            }     
        return gefiltertNachLaenge;
    }

    public ArrayList<Bewertung> filterDokumenteNachMaximum(long maximum, 
                                                           ArrayList<Bewertung> treffer) {
        ArrayList<Bewertung> gefiltertNachLaenge = new ArrayList<>();
        for (int i = 0; i < treffer.size(); i++) {
            if (maximum > treffer.get(i).getLaenge()) {
                gefiltertNachLaenge.add(treffer.get(i));
            }
        }
        return gefiltertNachLaenge;
    }

    public ArrayList<Bewertung> filterDokumenteNachMaxUndMin(
            long minimum, 
            long maximum, ArrayList<Bewertung> treffer) {
        ArrayList<Bewertung> min = filterDokumenteNachMinimum(minimum, treffer);
        ArrayList<Bewertung> max = filterDokumenteNachMaximum(maximum, treffer);
        treffer = new ArrayList<>();
        for(Bewertung bewertung : min) {
            if (max.contains(bewertung)) {
                treffer.add(bewertung);
            }
        }
        return treffer;
    }

    public ArrayList<Bewertung> getBewertungsListe() {
        for (String key : wortliste.keySet()) {
            for (Bewertung bewertung : wortliste.get(key)) {
                if (!dokumentliste.contains(bewertung)) {
                    dokumentliste.add(bewertung);
                }
            }
            System.out.println(dokumentliste.size());
        }
            return dokumentliste;
    }

    public ArrayList<Bewertung> fuzzySearch(String suchWort) {
        int minDist = Integer.MAX_VALUE;
        ArrayList<Bewertung> resultate = new ArrayList<>();
        for(String wort : wortliste.keySet()) {
            int dist = Levenshtein.distance(suchWort, wort);
            if (dist == minDist) {
                resultate.addAll(sucheWort(wort));
            } else if (dist < minDist) {
                minDist = dist;
                resultate = new ArrayList<>();
                resultate.addAll(sucheWort(wort));
            }
        }
        return resultate;
    }
}

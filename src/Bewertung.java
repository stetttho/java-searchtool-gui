/**
 * 
 * In der Klasse Bewertung werden die einzelnen Bewertungen gespeichert, inklusive
 * dem Member, zu dem die einzelne Bewertung gehört.
 *
 */
public class Bewertung {
    private String titel;
    private String inhalt;
    private Member member;
    private long textLaenge;
    
    public Bewertung(String titel, String inhalt, Member member) {
        this.titel = titel;
        this.inhalt = inhalt;
        this.member = member;
    }
    
    public String getTitel() {
        return titel;
    }
    
    public String getInhalt() {
        return inhalt;
    }
    
    public Member getMember() {
        return member;
    }
    
    public void setLaenge(long laenge) {
        textLaenge = laenge;
    }

    public long getLaenge() {
        return textLaenge;
    }
    
    public String toString() {
        String ausgabe = "";
        ausgabe = titel + ": " + inhalt;
        return ausgabe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bewertung bewertung = (Bewertung) o;

        if (textLaenge != bewertung.textLaenge) return false;
        if (titel != null ? !titel.equals(bewertung.titel) : bewertung.titel != null) return false;
        if (inhalt != null ? !inhalt.equals(bewertung.inhalt) : bewertung.inhalt != null) return false;
        return member != null ? member.equals(bewertung.member) : bewertung.member == null;
    }

    @Override
    public int hashCode() {
        int result = titel != null ? titel.hashCode() : 0;
        result = 31 * result + (inhalt != null ? inhalt.hashCode() : 0);
        result = 31 * result + (member != null ? member.hashCode() : 0);
        return result;
    }
}

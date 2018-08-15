import java.util.ArrayList;

/**
 * Die Klasse Member speichert die einzelnen Member, inklusive Verweis
 * auf die Bewertungen, die ein Member geschrieben hat.
 *
 */
public class Member {
    private String titel;
    private ArrayList<Bewertung> bewertungen;
    
    public Member(String titel, ArrayList<Bewertung> bewertungen) {
        this.titel = titel;
        this.bewertungen = bewertungen;
    }
    
    public Member(String titel) {
        this.titel = titel;
    }

    public String getTitel() {
        return titel;
    }
    
    public void addBewertungen(ArrayList<Bewertung> bewertungen) {
        this.bewertungen = bewertungen;
    }

    public ArrayList<Bewertung> getBewertungen() {
        return bewertungen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        return titel != null ? titel.equals(member.titel) : member.titel == null;
    }

    @Override
    public int hashCode() {
        return titel != null ? titel.hashCode() : 0;
    }
}

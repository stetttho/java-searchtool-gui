/**
 * Hilfsklasse zur Berechnung der Levenshtein-Distanz zwischen zwei Wörtern.
 *
 */

public class Levenshtein {
   public static int distance(String wort1, String wort2) {
       int m = wort1.length(), n = wort2.length();
       int[][] dist = new int[m + 1][n + 1];
       int i, j;

       for (i = 0; i <= m; i++) dist[i][0] = i;
       for (j = 0; j <= n; j++) dist[0][j] = j;
       for (i = 1; i <= m; i++) {
           for (j = 1; j <= n; j++) {
               int cost = (wort1.charAt(i - 1) == wort2.charAt(j - 1) ? 0 : 1);
               dist[i][j] = Math.min(
                       Math.min(dist[i - 1][j] + 1,
                               dist[i][j - 1] + 1),
                       dist[i - 1][j - 1] + cost);
           }
       }
       return dist[m][n];
   }

    public static void main(String[] args) {
        System.out.println(distance("quantität", "qualität"));
    }
}

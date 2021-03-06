import java.util.*;

public class GraphTools {

    private static List<Integer> sommets = new ArrayList<>();

    private static int[][] generateGraphe(int size) {
        int[][] graphe = new int[size][size];
        for (int i=0; i<size; i++) {
            for (int j=0; j<size; j++) {
                int rand = (int)(Math.random()*(20)-7);
                if (i==j || rand < 0) {
                    graphe[i][j] = -1;
                } else {
                    graphe[i][j] = rand;
                }
            }
        }
        return graphe;
    }

    private static int[][] generateurGrapheComplet(int size){
        int[][] graphe = new int[size][size];
        for (int i=0; i<size; i++) {
            graphe[i][i] = -1;
            for (int j=i+1; j<size; j++) {
                int rand = (int)(Math.random()*(20)+1);
                graphe[i][j] = rand;
                graphe[j][i] = rand;
            }
        }
        return graphe;
    }

    private static List<Integer> getChildren(int sommet, int[][] graphe){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < graphe[sommet].length; i++) {
            if (graphe[sommet][i]>=0){
                list.add(i);
            }
        }
        return list;
    }

    private static void parcoursProfondeur(int sommet, int[][] graphe){
        sommets.add(sommet);
        for (int s : getChildren(sommet, graphe))
        {
            if (!sommets.contains(s)){
                parcoursProfondeur(s, graphe);
            }
        }
    }

    private static boolean connexe(int[][] graphe){
        parcoursProfondeur(0, graphe);
        return sommets.size()==graphe.length;
    }

    private static int[] dijkstra(int[][] graphe, int depart){
        int[] ponderation = new int[graphe.length];
        List<Integer> sommetsTraite = new ArrayList<>();
        int sommetEnCours = depart;
        for (int i = 0; i < ponderation.length; i++) {
            ponderation[i]=-1;
        }
        while (sommetsTraite.size()!=graphe.length){
            List<Integer> voisins = getChildren(sommetEnCours, graphe);
            for (int voisin : voisins){
                if (!sommetsTraite.contains(voisin)){
                    int poids = graphe[sommetEnCours][voisin]+Math.max(ponderation[sommetEnCours], 0);
                    if (poids<ponderation[voisin] || ponderation[voisin]==-1){
                        ponderation[voisin] = poids;
                    }
                }
            }
            sommetsTraite.add(sommetEnCours);
            sommetEnCours = getNewSommet(ponderation, sommetsTraite);
        }
        return ponderation;
    }

    private static int getNewSommet(int[] ponderation, List<Integer> sommetsTraite) {
        int m = 0, r=-1;
        for (int i:ponderation) {
            if (i>m) m = i;
        }
        for (int i=0; i<ponderation.length; i++) {
            if (ponderation[i]>=0 && !sommetsTraite.contains(i) && ponderation[i]<=m) {
                m = ponderation[i];
                r = i;
            }
        }
        return r;
    }

    public static int[][] getDistances(int[][] graphe){
        int[][] distances = new int[graphe.length][graphe.length];
        for (int i = 0; i < graphe.length; i++) {
            distances[i] = dijkstra(graphe, i);
        }
        return distances;
    }

    private static int[] naiveVersion(int[][] graphe, int depart){
        int[] chemin = new int[graphe.length+1];
        int cheminEnCours = depart;
        for (int i = 0; i < graphe.length; i++) {
            chemin[i] = cheminEnCours;
            cheminEnCours = min(graphe[cheminEnCours], cheminEnCours, chemin);
        }
        chemin[graphe.length] = depart;
        return chemin;
    }

    private static int min(int[] ints, int index, int[] chemin) {
        int sommet = (index+1)%ints.length;
        int min = max(ints);
        for (int i = 0; i < ints.length; i++) {
            if (!contains(chemin, i)){
                if (ints[i]!=-1 && ints[i]<min){
                    min = ints[i];
                    sommet = i;
                }
            }
        }
        return sommet;
    }

    private static int max(int[] a) {
        int max = -1;
        for (int i : a) if (i>=max) max=i;
        return max;
    }

    private static boolean contains(int[] array, int val) {
        for (int i : array) if (i==val) return true;
        return false;
    }

    private static void printGraphe(int[][] graphe) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("\n");
        for (int[] t : graphe) {
            sb.append(Arrays.toString(t));
            sb.append("\n");
        }
        sb.append("]");
        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
        /*
        * Graphe pour lequel la version naïve ne donne pas le chemin le plus court :
        * int[][] test = {{-1, 1, 2, 3},{1, -1, 1, 3},{2, 1, -1, 20},{3, 3, 20, -1}};
        * */

//        int[][] test = {{-1,-1,2,-1,-1},{-1,-1,-1,1,3},{-1,1,-1,3,-1},{-1,-1,4,-1,-1},{2,-1,-1,-1,-1}};
        int[][] test = generateurGrapheComplet(10);
//        int[][] test = {{-1, 1, 7, 16, 7}, {1, -1, 7, 19, 17}, {7, 7, -1, 17, 8}, {16, 19, 17, -1, 13}, {7, 17, 8, 13, -1}};
//        while (!connexe(test)){
//            test = generateurGrapheComplet(5);
//        }

//        System.out.println(getChildren(3, test));
//        System.out.println(getNewSommet(test[0], new ArrayList<>()));
        printGraphe(test);
        System.out.println(connexe(test));
        System.out.println(Arrays.toString(naiveVersion(test, 0)));
    }
}

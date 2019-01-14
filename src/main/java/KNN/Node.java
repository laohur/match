package KNN;


//节点系结构
public class Node implements  Comparable{
    double[] properties;
    String label;
    double distance;
    Node(double[] properties, String label) {
        this.properties = properties;
        this.label = label;
    }
    @Override
    public int compareTo(Object e) {
        return (int) (this.distance-((Node)e).distance);
    }
}

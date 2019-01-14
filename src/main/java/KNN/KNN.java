package KNN;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/*
*
knn k临近算法
思想：“物以群分”，预测某一个类别，看它邻居多属于哪一类，距离加权。
距离函数：L2，cosine，皮尔逊相关系数、Jaccard相关系数
加权表决：
* */
public class KNN {

    public static void main(String[] args) throws Exception{
        long start = System.currentTimeMillis();
        String path="data\\Iris.txt";
        int k=5;
        double trainRate=0.8;
        Node[] dataSet=readData(path,",");
        System.out.println("记录总数：" + dataSet.length);
        long end = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (end - start));

        normalize(dataSet);

        Node[][] splitedData=dataSplit(dataSet,trainRate);
        Node[] trainSet=splitedData[0];
        Node[] testSet=splitedData[1];
        double errorRate=predict(trainSet,testSet,k);
        end = System.currentTimeMillis();

        System.out.println("错误率:" + errorRate);
        System.out.println("程序运行时间：" + (end - start));

    }

    static double predict(Node[] trainSet, Node[] testSet, int k){
        int wrong=0;
        Node[] neighbors = null;
        String label = null;
        for(Node node:testSet){
            neighbors=getNeighbors(node,k,trainSet);
            label=getLabel(neighbors);
            if(!label.equals(node.label))  {
                wrong++;
                System.out.println("错误： predicted: "+label+"   real:"+node.label);
            }
        }
        return (double)wrong/testSet.length;
    }

    //L2距离函数
    static double getDistance(Node A, Node B){
        double distance=0;
        for(int i=0; i<A.properties.length; i++){
            distance+=Math.pow((A.properties[i]-B.properties[i]),2);
        }
        return Math.pow(distance,0.5);
    }
    //取得k个邻居
    static Node[] getNeighbors(Node node, int k, Node[] trainSet){
        for(Node e:trainSet){
            e.distance=getDistance(node,e);
        }

        Arrays.sort(trainSet, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if(o1.distance<o2.distance)  return -1;
                else if(o1.distance>o2.distance)  return 1;
                else return 0;
            }
        });
        return Arrays.copyOfRange(trainSet,0,k);
    }
    //邻居中取得最广泛得标签
    static String getLabel(Node[] neighbors){
        Map<String,Integer> countMap = new HashMap<>();
        String label=null;
        int max=0;
        for(Node node:neighbors){
            countMap.put(node.label,countMap.getOrDefault(node.label,0)+1);
            if(countMap.get(node.label)>max){
                label=node.label;
                max=countMap.get(node.label);
            }
        }
        return label;
    }
    //读取数据集，为便于理解没有优化
    static Node[] readData(String path, String splitSymbol) throws Exception{
        List<Node> dataSet=new ArrayList<Node>();
        BufferedReader br=new BufferedReader(new FileReader(path));
        int dimensions;
        String line;
        double[] properties;
        String label;
        while((line=br.readLine())!=null){
            String[] tokens=line.split(splitSymbol);
            dimensions=tokens.length-1;  //求维度数量
            properties=new double[dimensions];
            label=tokens[dimensions];
            for(int i=0; i<dimensions; i++){
                properties[i]=Double.valueOf(tokens[i]);
            }
            Node node =new Node(properties,label);
            dataSet.add(node);
        }
        br.close();
        return dataSet.toArray(new Node[dataSet.size()]);
    }
    //归一化处理
    static void normalize(Node[] dataSet) {
        int dimensions = dataSet[0].properties.length;
        double[] min = new double[dimensions];
        double[] max = new double[dimensions];
        for (int i = 0; i < dimensions; i++) {
            min[i] = Double.MAX_VALUE;
            max[i] = Double.MIN_VALUE ;
        }
        for (Node node : dataSet) {
            for (int i = 0; i < dimensions; i++) {
               //System.out.println("122node "+i+" "+node.properties[i]);
                min[i] = Math.min(min[i], node.properties[i]);
                max[i] = Math.max(max[i], node.properties[i]);
                //System.out.println(min[i]);
            }
        }
        double[] range = max;
        for (int i = 0; i < dimensions; i++) {
            range[i] = max[i] - min[i];
        }
        for (Node node : dataSet) {
            for (int i = 0; i < dimensions; i++) {
                node.properties[i] = (node.properties[i] - min[i]) / range[i];
            }
        }
    }
    //数据集分割
    static Node[][] dataSplit(Node[] dataSets, double rate){
        ArrayList<Node> train=new ArrayList<>();
        ArrayList<Node> test=new ArrayList<>();
        for(Node node:dataSets){
            if(Math.random()<rate)  train.add(node);
            else test.add(node);
        }
        Node[][] splitedData=new Node[2][];
        splitedData[0]=train.toArray(new Node[train.size()]);
        splitedData[1]=test.toArray(new Node[test.size()]);
        return splitedData;
    }

}


package NLP;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class MM {
    static  Set<String> dict=null;
    public static  void main(String[] args) throws IOException {
        dict=Dictionary.getDict("dict.txt");
        RMM.dict=dict;
        System.out.println("词典加载了"+dict.size()+"条");
        String str="双向最大匹配算法就是比较正向最大匹配算法和逆向最大匹配算法的结果，若结果一样，则直接输出，否则输出分词结果含有非字典词最少，或者单字最少的那个结果";
        String line2=Dictionary.pure(str," ");
        System.out.println(line2);
        String[] strs=line2.split(" ");
        for(String line:strs){
            System.out.print(" MM:   ");
            List<String> tokens = MM.MM(line,dict);
            for(String s:tokens)
                System.out.printf(" "+s+" ");
            System.out.println();
  
            System.out.print(" RMM: ");
            List<String> tokens2 = RMM.RMM(line);
            for(String s:tokens2)
                System.out.printf(" "+s+" ");
            System.out.println();

            if(true)
                System.out.println("---MMBetter---"+MMBetter(tokens,tokens2));
        }
    }

    //正向匹配主函数
    public static List<String> MM(String line, Set<String> dict){
        List<String> result=new LinkedList<String>();
        tokenize(line,result,0);
        return result;
    }
    //正向匹配分词函数
    public static void tokenize(String line, List<String> result, int index){
        for(int i=line.length(); i>index; i--){
            if(i-index<1) {  //仅剩单字
                result.add(line.substring(index, i));
                break;
            }
            String tmpword=line.substring(index,i);
            if(dict.contains(tmpword)) {  //匹配成功
                result.add(tmpword);
                tokenize(line, result, index + tmpword.length());
                break;
            }
            //没匹配到，缩减长度
        }
    }
    static boolean MMBetter(List<String> a,List<String> b){
        if(a.size()!=b.size()){
            return a.size()<b.size()?true:false;
        }else{
            return singleCount(a)<singleCount(b)?true:false;
        }
    }

    static int singleCount(List<String> a){
        int count=0;
        for(String s:a){
            if(s.length()==1)
                count++;
        }
        return count;
    }
}


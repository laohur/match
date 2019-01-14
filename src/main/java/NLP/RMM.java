package NLP;

import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static java.util.Collections.reverse;

public class RMM {
    static  Set<String> dict=null;
    public static  void main(String[] args) throws IOException {
        dict= Dictionary.getDict("dict.txt");
        System.out.println("词典加载了"+dict.size()+"条");
        String str0="双向最大匹配算法就是比较正向最大匹配算法和逆向最大匹配算法的结果，若结果一样，则直接输出，否则输出分词结果含有非字典词最少，或者单字最少的那个结果";
        String str="否则输出分词结果含有非字典词最少";
        String line2=Dictionary.pure(str," ");
        System.out.println(line2);
        String[] strs=line2.split(" ");
        for(String line:strs){
            System.out.println(" ---- ");
            List<String> tokens = RMM.RMM(line);
            for(String s:tokens)
                System.out.printf(" "+s+" ");
        }
    }

    //正向匹配主函数
    public static List<String> RMM(String line){
        List<String> result=new LinkedList<String>();
        tokenize(line,result,line.length());
        Collections.reverse(result);
        return result;
    }
    //正向匹配分词函数
    public static void tokenize(String line, List<String> result, int index){
        for(int i=0; i<index; i++){
            if(index-i<1) {  //仅剩单字
                result.add(line.substring(i, index));
//                break;
            }
            String tmpword=line.substring(i,index);
//            System.out.println(i+tmpword+index);
            if(dict.contains(tmpword)) {  //匹配成功
                result.add(tmpword);
                tokenize(line, result, index - tmpword.length());
                break;
            }
            //没匹配到，缩减长度
        }
    }
}


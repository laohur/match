package NLP;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class Dictionary {
    public  static  void main(String[] args){
        Set<String>  dict = null;
        try {
            dict = Dictionary.getDict("dict.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(dict.size());
    }

    public static Set<String> getDict(String path) throws IOException {
        Set<String>  dict = new HashSet<String>();
        BufferedReader reader = new BufferedReader( new FileReader(new File(path)) );
        String line="";
        while( (line=reader.readLine())!=null ){
            dict.add(line.trim());
        }
        return dict;
    }

    //移除符号
    public static String pure(String line,String replacement){
        line=line.replaceAll("\\p{P}" , " ");
        return line;
    }
}
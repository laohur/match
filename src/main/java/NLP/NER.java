package NLP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NER {
    public static  void main(String[] args){}

    public static Set<String> getDict(String path) throws IOException {
        Set<String>  dict = new HashSet<String>();
        BufferedReader reader = new BufferedReader( new FileReader(new File(path)) );
        String line="";
        while( (line=reader.readLine())!=null ){
            dict.add(line.trim());
        }
        return dict;
    }

}

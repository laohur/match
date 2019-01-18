/*
* 朴素贝叶斯  假设维度独立则
* 后验概率：P(type|words)=P(type*words)/P(words)=p(words|type)P(type)/P(words)
* P(type|words)=P(type)multiplay( P(wordi|type) )
* 遍历typex，取概率最大者
* P(typex)=DocTypex.count/totalCount
* P(wordi|typex)=Doc[Typex & Wordi].count/totalCount
* 维度是某类下该词词频，
* 拉普拉斯平滑  (a+l)/(b+nl)  n为训练集数  https://zhuanlan.zhihu.com/p/24291822
* 任务：垃圾短信识别
* */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NaiveBayes {
    public static void main(String[] args) throws Exception {
        String dataPath = "data\\SMSSpamCollection.txt";
        Message[] dataSet=getData(dataPath);
        Message[][] splitedData=split(dataSet,0.8);
        Message[] trainSet=splitedData[0];
        Message[] testSet=splitedData[1];
        Model model=train(trainSet);
        double errorRate=test(model,testSet);
        System.out.println("--预测错误率"+errorRate);
    }
    static double test(Model model, Message[] testSet){
        int error=0;
        for(Message message:testSet){
            if(!predict(model,message.Content).equals(message.Type))  error++;
        }
        return error*1.0/testSet.length;
    }
    static String predict(Model model, String[] words){
        double hamRate=model.hamProbabability;
        double spamRate=model.spamProbability;
        for(String word:words){
            if(model.hamMap.containsKey(word))    hamRate+=model.hamMap.get(word);
            else hamRate+=model.defaultProbility;
            if(model.spamMap.containsKey(word))    spamRate+=model.spamMap.get(word);
            else spamRate+=model.defaultProbility;
        }
        if(spamRate>hamRate)  return "spam";
        return "ham";
    }
    static Model train(Message[] trainSet){
        int hamCount=0;
        int spamCount=0;
        Map<String,Double> hamWord=new HashMap<>();
        Map<String,Double> spamWord=new HashMap<>();
        for(Message message:trainSet){
            if(message.Type.equals("ham")){
                hamCount++;
                for(String word:message.Content){
                    hamWord.put(word,hamWord.getOrDefault(word,0.0)+1);
                }
            }else{
                spamCount++;
                for(String word:message.Content){
                    spamWord.put(word,spamWord.getOrDefault(word,0.0)+1);
                }
            }
        }
        Model model=new Model();
        model.hamProbabability=Math.log(hamCount*1.0/trainSet.length);
        model.spamProbability=Math.log(spamCount*1.0/trainSet.length);
        model.defaultProbility=Math.log(1.0/(hamWord.size()*+spamWord.size()));
        int hamWordSum=hamWord.size()+2;  //拉普拉斯平滑
        int spamWordSum=spamWord.size()+2;
        for(Map.Entry<String, Double> entry:hamWord.entrySet()){
            entry.setValue(Math.log(entry.getValue()*1.0/hamWordSum));
        }
        for(Map.Entry<String, Double> entry:spamWord.entrySet()){
            entry.setValue(Math.log(entry.getValue()*1.0/spamWordSum));
        }
        model.hamMap=hamWord;
        model.spamMap=spamWord;
        System.out.println("---54 训练完成,训练记录总数:"+trainSet.length);
        return model;
    }
    static Message[][] split(Message[] dataSets, double rate){
        ArrayList<Message> train=new ArrayList<>();
        ArrayList<Message> test=new ArrayList<>();
        for(Message message:dataSets){
            if(Math.random()<rate)  train.add(message);
            else test.add(message);
        }
        Message[][] splitedData=new Message[2][];
        splitedData[0]=train.toArray(new Message[train.size()]);
        splitedData[1]=test.toArray(new Message[test.size()]);
        return splitedData;
    }
    static Message[] getData(String dataPath) throws Exception{
        ArrayList<Message> messages=new ArrayList<>();
        BufferedReader br=new BufferedReader(new FileReader(dataPath));
        String line=null;
        while((line=br.readLine())!=null){
            Message message=new Message();
            String[] strs=line.split("\t");
            if(strs[0]=="")  continue;
            message.Type=strs[0];
            message.Content=clean(strs[1]);
            messages.add(message);
        }
        System.out.println("---108  读取记录总数："+messages.size());
        return messages.toArray(new Message[messages.size()]);
    }
    static String[] clean(String line){
        ArrayList<String> result=new ArrayList<>();
        line=line.replaceAll("\\p{P}"," ");
        String[] words=line.split(" ");
        for(String word:words){
            if(word.length()<3)  continue;
            result.add(word);
        }
        return result.toArray(new String[result.size()]);
    }
}
//模型参数，此任务只有两类，每类分别计算。一般任务仍用map计数
class Model{  //都用对数表示
    double hamProbabability; //
    double spamProbability;
    double defaultProbility;// 陌生词概率，应该取最小值
    Map<String,Double> hamMap;  //技巧，开始计数，而后转化概率
    Map<String,Double> spamMap;
}
class Message{
    public String Type;  //ham spam
    public String[] Content;
}
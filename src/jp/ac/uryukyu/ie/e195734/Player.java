package jp.ac.uryukyu.ie.e195734;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;


public class Player {
    private String name;
    private String nickName;
    private LinkedHashMap<String,Integer> stat = new LinkedHashMap<>();

    Player(String stringData){
        for(String sd:stringData.split(",")){
            String[] spd = sd.split(":");
            if(spd[0].equals("name")){
                this.name = spd[1];
            }
            else{
                this.stat.put(spd[0],Integer.parseInt(spd[1]));
            }
        }
    }

    public void save(){
        String text ="";
        text = text+"name:"+this.name+",";
        for(String key:stat.keySet()){
            text = text+key+":"+stat.get(key)+",";
        }
        try {
            FileWriter fw = new FileWriter("./"+ this.name+".txt");
            fw.write(text);
            fw.close();
            System.out.println("プレイヤー"+this.name+"のデータを"+this.name+".txtとして保存しました。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getStat(String key){
        try{
            return this.stat.get(key);
        }
        catch (NullPointerException e){
            return -1;
        }
    }

    public void addStat(String key,int value){
        this.stat.put(key,value);
    }

    public void removeStat(String key){
        this.stat.remove(key);
    }

    public String getNickName(){
        return this.nickName;
    }

    public void setNickName(String nickName){ this.nickName = nickName; }

}

package jp.ac.uryukyu.ie.e195734;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Command {
    int mode = 0;
    public  ArrayList<Player> players = new ArrayList<>();
    private ArrayList<String> sc;
    private ArrayList<Player> targetPl;

    public void start(){
        Scanner scanner = new Scanner(System.in);
        //noinspection InfiniteLoopStatement
        while(true){
            this.sc = new ArrayList<>(Arrays.asList(scanner.nextLine().split(" ")));
            this.targetPl = null;
            if(this.sc.get(0).equals("")){
                continue;
            }
            switch (sc.get(0)) {
                case "help":
                    help();
                    break;
                case "load":
                    load();
                    break;
                case "fin.":
                    fin();
                    break;
                case "save":
                    save();
                    break;
                case "stat":
                    stat();
                    break;
                case "roll":
                    this.mode = 1;
                    roll();
                    break;
                default:
                    System.out.println("コマンド "+sc.get(0)+"は存在しません。");
                    break;
            }
        }
    }


    private ArrayList<Player> compensatePlayer() {
        ArrayList<Player> plArr = new ArrayList<>();
        int target = 1;

        if(this.sc.get(0).equals("san")&&this.mode==1){
            target = 2;
            if(this.sc.size()==2){
                plArr.addAll(this.players);
            }

        }
        if(this.sc.size()==target){
            plArr.addAll(this.players);
        } else if(sc.get(target).equals("all")){
            plArr.addAll(this.players);
        } else{
            for (String pl : sc.get(target).split(",")) {
                boolean stat = false;
                for(Player targetPl : this.players){
                    if(pl.equals(targetPl.getName())||pl.equals(targetPl.getNickName())){
                        plArr.add(targetPl);
                        stat = true;
                        break;
                    }
                }
                if(!stat){
                    System.out.println("名前/ニックネーム "+pl+"が見つかりません。");
                }
            }
        }
        return plArr;
    }

    private void help(){
        if(this.sc.size()==2) {
            System.out.println("rollモード時のコマンドの使用法 \n <技能名> [プレイヤー名] [難易度] \n san <減少値> [プレイヤー名] \n プレイヤー名はカンマで区切って複数名記述可能 \n 難易度は n h e \n 減少値は 0/1d6+1 などのように記述");
        } else {
            System.out.println("使用可能なコマンド一覧 \n roll -rollモードに入ります。(help roll 参照) \n load <ファイルパス> -プレイヤーデータを読み込みます \n save [プレイヤー名] -プレイヤーデータをセーブします \n stat [プレイヤー名] [技能名] -プレイヤーのステータスを表示します \n fin. [bool] -セーブして終了します。true でセーブを行いません\n");
        }
    }

    private void save(){
        this.targetPl = compensatePlayer();
        for(Player player:this.targetPl){
            player.save();
        }
    }

    private void stat(){
        this.targetPl = compensatePlayer();
        if(sc.size()==3){
            for(Player player:this.targetPl){
                int stat = player.getStat(this.sc.get(2));
                if(stat!=-1){
                    System.out.println(stat);
                } else{
                    System.out.println("プレイヤー "+player.getName()+"に技能"+this.sc.get(2)+"はありません。");
                }

            }
        } else{
            System.out.println("正しく入力してください。\n使用例:stat <技能値> [プレイヤー名]");
        }

    }

    private void load(){
        if(sc.size()!=1){
            File file = new File("./"+sc.get(1)+".txt");
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String text = br.readLine();
                this.players.add(new Player(text));
                System.out.println("プレイヤーデータ "+sc.get(1)+" を読み込みました。");
            } catch (IOException e) {
                System.out.println(sc.get(1)+".txtが見つかりません。");
            }
        } else{
            System.out.println("正しく入力してください。\n使用法: load ファイル名");
        }
    }

    private void fin(){
        if (sc.size()==2){
            if(sc.get(1).equals("true")){
                System.out.println("保存せずに終了します。");
            }
        } else {
            System.out.println("保存して終了します。");
            save();
            System.out.println("保存が完了しました。");
        }
        System.exit(0);
    }

    private void roll(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("ロールモードに入ります。");
        while(true){
            this.sc = new ArrayList<>(Arrays.asList(scanner.nextLine().split(" ")));
            int dif =0;
            if(sc.get(0).equals("exit")){
                System.out.println("ロールモードを終了します。");
                this.mode = 0;
                break;
            }
            if(sc.size()==3){
                if(sc.get(2).equals("h")){
                    dif = 1;
                } else if(sc.get(2).equals("e")){
                    dif = 2;
                }
            }
            this.targetPl = compensatePlayer();
            Roll roll = new Roll(5,96,dif);
            if(this.sc.get(0).equals("san")){
                for(Player player:this.players){
                    roll.sanRun(this.sc.get(1),player);
                }
            } else{
                for(Player player:this.players){
                    roll.run(this.sc.get(0),player);
                }
            }
        }
    }
}

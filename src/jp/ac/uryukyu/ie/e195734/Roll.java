package jp.ac.uryukyu.ie.e195734;

import java.util.Random;

public class Roll {
    int critical;
    int fumble;
    int dif;

    Roll(int critical,int fumble,int dif){
        this.critical = critical;
        this.fumble = fumble;
        this.dif = dif;
    }

    public void run(String rollName,Player player){
        int stat = player.getStat(rollName);
        if(stat==-1){
            System.out.println("技能値を正しく入力して下さい。");
            return;
        }
        int dice = Dice();
        int SuccessNum = getSuccessNum(stat);
        int result = check(SuccessNum,dice);
        PrintResult(player.getName(),dice,SuccessNum,result);
    }

    public void sanRun(String formula,Player player){
        int stat = player.getStat("san");
        int dice = Dice();
        String[] formulas = formula.split("/");
        int result = check(stat,dice);
        int decrease = 0;
        if(result<=1){
            decrease = calculate(formulas[0]);
        }
        else{
            decrease = calculate(formulas[1]);
        }
        player.addStat("san",player.getStat("san")-decrease);
        PrintResult(player.getName(),dice,stat,result);
        System.out.println("SAN値が "+decrease+"減少しました。");
    }


    public int calculate(String formula){
        String[] term = formula.split("\\+");
        int sum = 0;
        for(String i:term){
            String[] d = i.split("d");
            if(d.length==2){
                int num1;
                int num2;
                try{
                    num1 = Integer.parseInt(d[0]);
                    num2 = Integer.parseInt(d[1]);
                    sum += Dice(num1,num2);
                }
                catch (Exception e){
                    return -1;
                }
            }
            else if(d.length==1){
                int num3;
                try{
                    num3 = Integer.parseInt(d[0]);
                    sum += num3;
                }
                catch (Exception e){
                    return -1;
                }
            }
            else{ return-1; }
        }
        return sum;
    }

    private int Dice(int num,int diceType){
        int sum = 0;
        for(int i = 0;i<num;i++){
            Random random = new Random();
            sum += random.nextInt(diceType) + 1;
        }
        return sum;
    }

    private int Dice(){
        return Dice(1,100);
    }

    public int check(int successNum,int dice){
        if(dice<=successNum){
            if(dice<=this.critical){
                return 0;
            }
            else{
                return 1;
            }
        }
        else {
            if(dice>=this.fumble){
                return 3;
            }
            else{
                return 2;
            }
        }
    }

    private int getSuccessNum(int stat){
        int con =stat;
        switch (this.dif) {
            case 1:
                con = stat/2;
                break;
            case 2:
                con = stat/5;
                break;
        }
        return con;
    }
    private void PrintResult(String name,int dice,int need,int result){
        final String[] resultList = {"クリティカル","成功","失敗","ファンブル"};
        System.out.println(name+":"+resultList[result]+"(ダイス: "+dice+" ,成功値: "+need+")");
    }
}
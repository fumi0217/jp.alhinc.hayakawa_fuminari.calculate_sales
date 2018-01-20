package jp.alhinc.hayakawa_fuminari.calculate_sales;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


class calculate{
    public static void main(String args[])throws IOException{

        //declearing all the variables that I'm gonna need
        Map<String,String> branch = new HashMap<String,String>();//branch<branch code, branch name>
        Map<String,String> commodity = new HashMap<String,String>();//commodity<commodity code, commodity name>
        Map<String, Long> branchSales = new HashMap<String, Long>(); //branchSales<branch code, branch's sales sum>
        Map<String, Long> commoditySales = new HashMap<String, Long>(); //commoditySales<commodity code, commodity's sales sum>

        Map<String,String> aSale = new HashMap<String,String>();//aSale<"支店", branch code><"商品", commodity code><"売上額", how much is sold>
        List<Map> sales = new ArrayList<Map>();//sales<aSale, aSale,,,>

        List<Integer> fileNum = new ArrayList<Integer>();//for storing sales files' number in
        List<File> salesFile = new ArrayList<File>();//for putting sales files in

        int fileMin;
        int counter = 1;
        int numOfDigits = 8;//number of digits for rcd files

        String str = null;
        String name;
        String complement = "";
        String[] keys = {"支店", "商品", "売上額"};

        //getting files from the command line
        File file = new File("jp\\alhinc\\hayakawa_fuminari\\calculate_sales\\"+args[0]);
        File[] files = file.listFiles();
    	File branchFile = new File("jp\\alhinc\\hayakawa_fuminari\\calculate_sales\\"+args[0]+"\\branch.lst");
    	File commodityFile = new File("jp\\alhinc\\hayakawa_fuminari\\calculate_sales\\"+args[0]+"\\commodity.lst");

        //loading a file named "branch.lst" and storing its data in a variables named "branch"
        if(branchFile.exists()){
            try{
                BufferedReader br = new BufferedReader(new FileReader(branchFile));

                //branch<branch code, branch name>
                while((str=br.readLine())!=null){
                    String[] strAry = str.split(",");
                    branch.put(strAry[0],strAry[1]);
                	branchSales.put(strAry[0],0L);

                    //adding error messages if the format is unnapropriate
                    String regex = "\\d\\d\\d";
                    if(!strAry[0].matches(regex)){
                        System.out.println("支店定義ファイルのフォーマットが不正です");
                        return;
                    }
                }

                //printing out what is inside branch HashMap
                for(Map.Entry<String,String> e : branch.entrySet()){
                    System.out.println(e.getKey()+":"+e.getValue());
                }
                br.close();
            }catch(FileNotFoundException e){
                System.out.println("予期せぬエラーが発生しました。");
                return;
            }catch(IOException e){
                System.out.println("予期せぬエラーが発生しました。");
                return;
            }
        }else{
            //if the target file cannot be found in the directory provided, error messages will be shown
            System.out.println("支店定義ファイルが存在しません");
            return;
        }

        //loading a file named "commodity.lst" and storing its data in a variables named "commodity"
        if(commodityFile.exists()){
            try{
                BufferedReader br = new BufferedReader(new FileReader(commodityFile));

                //commodity<commodity code, commodity name>
                while((str=br.readLine())!=null){
                    String[] strAry = str.split(",");
                    commodity.put(strAry[0],strAry[1]);
                	commoditySales.put(strAry[0], 0L);

                    //adding error messages if the format is unnapropriate
                    String regex = "[0-9A-Z][0-9A-Z][0-9A-Z][0-9A-Z][0-9A-Z][0-9A-Z][0-9A-Z][0-9A-Z]";
                    if(!strAry[0].matches(regex)){
                        System.out.println("商品定義ファイルのフォーマットが不正です");
                        return;
                    }
                }
                br.close();
            }catch(FileNotFoundException e){
                System.out.println("予期せぬエラーが発生しました。");
            }catch(IOException e){
                System.out.println("予期せぬエラーが発生しました。");
            }
        }else{
        	//if the target file cannot be found in the directory provided, error messages will be shown
            System.out.println("商品定義ファイルが存在しません");
            return;
        }

        //storing the number of rcd files in List variables named "fileNum"
        for(File fNames : files){
            name = fNames.getName();
            if(name.endsWith(".rcd")){
                fileNum.add(Integer.parseInt(name.substring(0, name.length()-4)));
            }
        }

        //sorting "fileNum" into the decending order
        Collections.sort(fileNum);

        //displaying error messages if the numbers in "fileNum" are not sequential
        fileMin = fileNum.get(0);
        for(int i = 1; i < fileNum.size(); i++){
            fileMin++;
            if(fileMin != fileNum.get(i)){
                System.out.println("売上ファイル名が連番になっていません");
                return;
            }
        }

        //storing rcd files in a variable named "salesFile"
        for(int num : fileNum){
            //adding additional zero "0" to make the length of the file name appropriate
            if(num >= 10000000){
                counter = 8;
            }else if(num >= 1000000){
                counter = 7;
            }else if(num >= 100000){
                counter = 6;
            }else if(num >= 10000){
                counter = 5;
            }else if(num >= 1000){
                counter = 4;
            }else if(num >= 100){
                counter = 3;
            }else if(num >= 10){
                counter = 2;
            }
            numOfDigits = numOfDigits - counter;
            for(int i = 0; i < numOfDigits; i++){
                complement += '0';
            }

            salesFile.add(new File(new String("jp\\alhinc\\hayakawa_fuminari\\calculate_sales\\"+args[0] + "/" + complement + num + ".rcd")));

            numOfDigits = 8;
            complement = "";
        }

        //putting sales data in
        //data structure : sales List<aSale Map[branch code, product code, how much costs in total],,,,,>
        //String[] keys = {"支店", "商品", "売上額"};
        counter = 0;
        for(int i = 0;  i < salesFile.size(); i++){
             BufferedReader br = new BufferedReader(new FileReader(salesFile.get(i)));
             while((str = br.readLine()) != null){
                aSale.put(keys[counter], str);
                counter++;
                //displaying an error if more than 3 lines in sales file are found
                if(counter > 3){
                    System.out.println(salesFile.get(i) + "のフォーマットが不正です");
                    return;
                }
            }
            sales.add(aSale);
            aSale = new HashMap<String, String>();
            counter = 0;
        }

        //branchSales<branch code, branch name>
        //differentiating sales data according to which branch it is, and storing it in a variables named "branchSales"
        for(int i = 0; i < sales.size(); i++){
        	String branchCode = sales.get(i).get("支店").toString();
            if(branchSales.containsKey(branchCode)){
                branchSales.put(branchCode, Long.parseLong(sales.get(i).get("売上額").toString()) + branchSales.get(branchCode));
            	
            //displaying an error if a branch code in "salesFile" is not registered in "branchSales"
            }else{
                System.out.println(salesFile.get(i) + "の支店コードが不正です");
                return;
            }
        }

        //sorting total sales of "branchSales" into the decending order and renaming it "branchEntries"
        List<Entry<String, Long>> branchEntries = new ArrayList<Entry<String, Long>>(branchSales.entrySet());
        Collections.sort(branchEntries, new Comparator<Entry<String, Long>>(){
            public int compare(Entry<String, Long> obj1, Entry<String, Long> obj2){
                return obj2.getValue().compareTo(obj1.getValue());
            }
        });

        //displaying an error if any sum is too large
        for(Entry entries : branchEntries){
            Long sum = (Long)entries.getValue();
            if(sum >= 1000000000){
                System.out.println("合計金額が10桁を超えました");
                return;
            }
        }

        //creating a new file named "branch.out" for the output(sales for each branch)
        File outBranch = new File("jp\\alhinc\\hayakawa_fuminari\\calculate_sales\\"+args[0] + "/" + "branch.out");
        outBranch.createNewFile();
        FileWriter branchWriter = new FileWriter(outBranch);
        for(Entry<String, Long> entry : branchEntries){
            String branchCode = entry.getKey();
            branchWriter.write(branchCode + "," + branch.get(branchCode) + "," + entry.getValue() + "\n");
        }
        branchWriter.close();


        //commoditySales<commodity code, commodity name>
        //differentiating sales data according to which commodity it is, and storing it in a variables named "commoditySales"

        for(int i = 0; i < sales.size(); i++){
        	String commodityCode = sales.get(i).get("商品").toString();
            if(commoditySales.containsKey(commodityCode)){
            	commoditySales.put(commodityCode, Long.parseLong(sales.get(i).get("売上額").toString()) +
                commoditySales.get(commodityCode));
            		
            //displaying an error if a commodity code in "salesFile" is not registered in "commodity"
            }else{
                System.out.println(salesFile.get(i) + "の商品コードが不正です");
                return;
            }
        }
        System.out.println(commoditySales);

        //sorting total sales of "commoditySales" into the decending order and renaming it "commodityEntries"
        List<Entry<String, Long>> commodityEntries = new ArrayList<Entry<String, Long>>(commoditySales.entrySet());
        Collections.sort(commodityEntries, new Comparator<Entry<String, Long>>(){
            public int compare(Entry<String, Long> obj1, Entry<String, Long> obj2){
                return obj2.getValue().compareTo(obj1.getValue());
            }
        });

        //displaying an error if any sum is too large
        for(Entry entries : commodityEntries){
            Long sum = (Long)entries.getValue();
            if(sum >= 1000000000){
                System.out.println("合計金額が10桁を超えました");
                return;
            }
        }


        //sorting a new file named "commodity.out" for the output(sales for each commodity)
        File outCommodity = new File("jp\\alhinc\\hayakawa_fuminari\\calculate_sales\\"+args[0] + "/" + "commodity.out");
        outCommodity.createNewFile();
        FileWriter commodityWriter = new FileWriter(outCommodity);
        for(Entry<String, Long> entry : commodityEntries){
            String commodityCode = entry.getKey();
            commodityWriter.write(commodityCode + "," + commodity.get(commodityCode) + "," + entry.getValue() + "\n");
        }
        commodityWriter.close();
    }
}
package jp.alhinc.hayakawa_fuminari.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


class Calculate{


	public static Boolean downloadingData(String format, String fileName, String path, Map<String,String> definitionMap, Map<String,Long> salesMap) throws IOException{
		File definitionFile = new File(path,fileName);
		String str;
		if(!definitionFile.exists()){
			//if the target file cannot be found in the directory provided, error messages will be shown
	        System.out.println(fileName + "が存在しません");
	        return false;
		}
		BufferedReader br = null;
		try{
			br = new BufferedReader(new FileReader(definitionFile));

            while((str=br.readLine())!=null){
                String[] fileContents = str.split(",");

                //adding error messages if the format is unnapropriate
                if(fileContents.length != 2 || !fileContents[0].matches(format)){
                    System.out.println(fileName + "のフォーマットが不正です");
        	        return false;
                }

                definitionMap.put(fileContents[0],fileContents[1]);
                salesMap.put(fileContents[0],0L);
            }
        }catch(IOException e){
            System.out.println("予期せぬエラーが発生しました");
	        return false;
        }finally{
            if(br != null){
                br.close();
            }else{
                System.out.println("予期せぬエラーが発生しました");
    	        return false;
            }
        }
		return true;

    }

	public static Boolean writingOutputs(String fileName,String path,Map definitionMap, List<Entry<String,Long>> totalSales) throws IOException{
		File resultFile = new File(path, fileName);
		BufferedWriter bWriter = null;
		try{
			bWriter = new BufferedWriter(new FileWriter(resultFile));
            for(Entry<String, Long> entry : totalSales){
                String Code = entry.getKey();
                bWriter.write(Code + "," + definitionMap.get(Code) + "," + entry.getValue());
                bWriter.newLine();
            }
        }finally{
            if(bWriter != null){
                bWriter.close();
            }else{
                System.out.println("予期せぬエラーが発生しました");
    	        return false;
            }
        }
		return true;
	}

    public static void main(String args[])throws IOException{

        //declearing all the variables that I'm gonna need
        Map<String,String> branch = new HashMap<String,String>();//branch<branch code, branch name>
        Map<String,String> commodity = new HashMap<String,String>();//commodity<commodity code, commodity name>
        Map<String, Long> branchSales = new HashMap<String, Long>(); //branchSales<branch code, branch's sales sum>
        Map<String, Long> commoditySales = new HashMap<String, Long>(); //commoditySales<commodity code, commodity's sales sum>
        List<Integer> fileNum = new ArrayList<Integer>();//for storing sales files' number in
        List<File> saleFiles = new ArrayList<File>();//for putting sales files in
        String str = null;

        //making sure that the number of command-line arguments is one
        if(args.length != 1){
            System.out.println("予期せぬエラーが発生しました");
            return;
        }

        //getting files from the command line
        File file = new File(args[0]);
        File[] files = file.listFiles();
        String branchRegex = "^\\d{3}$";
        String commodityRegex = "^\\w{8}$";
        String branchInput = "branch.lst";
        String commodityInput = "commodity.lst";
        String branchOutput = "branch.out";
        String commodityOutput = "commodity.out";
        String path = args[0];

        //loading a file named "branch.lst" and storing its data in a variables named "branch"
        if(!downloadingData(branchRegex,branchInput,path,branch,branchSales)){
        	return;
        }
        //loading a file named "commodity.lst" and storing its data in a variables named "commodity"
        if(!downloadingData(commodityRegex,commodityInput,path,commodity,commoditySales)){
        	return;
        }

        //storing rcd files in a variable named "salesFile"
        //storing the number of rcd files in List variables named "fileNum"
        for(File rcdFile : files){
            if(rcdFile.getName().matches("^\\d{8}.rcd$") && rcdFile.isFile()){
                saleFiles.add(rcdFile);
                fileNum.add(Integer.parseInt(rcdFile.getName().substring(0, 8)));
            }
        }

        if(fileNum.size()!=0){
        	//sorting "fileNum" into the accending order
            Collections.sort(fileNum);
            int min = fileNum.get(0);
            int max = fileNum.get(fileNum.size()-1);

            //displaying error messages if the numbers in "fileNum" are not sequential
            if(max-min+1!=fileNum.size()){
            	System.out.println("売上ファイル名が連番になっていません");
            	return;
            }
        }





        for(int i = 0; i < fileNum.size()-1; i++){
            int diff = fileNum.get(i+1) - fileNum.get(i);
            if(diff != 1){
                System.out.println("売上ファイル名が連番になっていません");
                return;
            }
        }

        //putting sales data in
        //data structure : sales List<aSale Map[branch code, product code, how much costs in total],,,,,>
        //String[] keys = {"支店", "商品", "売上額"};

        BufferedReader br = null;

        for(int i = 0; i < saleFiles.size(); i++){
            try{
                br = new BufferedReader(new FileReader(saleFiles.get(i)));
                List<String> rcdContents = new ArrayList<String>();
                while((str = br.readLine()) != null){
                    rcdContents.add(str);
                }

                //checking if the variable "rcdContents" has the right values
                if(rcdContents.size() != 3 || !(rcdContents.get(2).matches("\\d*$"))){
                    System.out.println("予期せぬエラーが発生しました");
                    return;
                }

                String branchCode = rcdContents.get(0);
                String commodityCode = rcdContents.get(1);
                String sales = rcdContents.get(2);

                //displaying an error if a branch code in "salesFile" is not registered in "branchSales"
                if(!branchSales.containsKey(branchCode)){
                	System.out.println(saleFiles.get(i).getName() + "の支店コードが不正です");
                    return;
                }

                Long sum = Long.parseLong(sales) + branchSales.get(branchCode);

                if(sum > 9999999999L){
                    System.out.println("合計金額が10桁を超えました");
                    return;
                }

                branchSales.put(branchCode, sum);

                if(!commoditySales.containsKey(commodityCode)){
                	System.out.println(saleFiles.get(i).getName() + "の商品コードが不正です");
                    return;
                }

                sum = Long.parseLong(sales) + commoditySales.get(commodityCode);

                if(sum > 9999999999L){
                    System.out.println("合計金額が10桁を超えました");
                    return;
                }

                commoditySales.put(commodityCode, sum);
            }catch(IndexOutOfBoundsException e){
                System.out.println(saleFiles.get(i).getName() + "のフォーマットが不正です");
                return;
            }finally{
                if(br != null){
                    br.close();
                }else{
                    System.out.println("予期せぬエラーが発生しました");
                    return;
                }
            }
        }

        //sorting total sales of "branchSales" into the decending order and renaming it "branchEntries"
        List<Entry<String, Long>> branchEntries = new ArrayList<Entry<String, Long>>(branchSales.entrySet());
        Collections.sort(branchEntries, new Comparator<Entry<String, Long>>(){
            public int compare(Entry<String, Long> obj1, Entry<String, Long> obj2){
                return obj2.getValue().compareTo(obj1.getValue());
            }
        });

        //creating a new file named "branch.out" for the output(sales for each branch)
        if(!writingOutputs(branchOutput,path,branch,branchEntries)){
        	return;
        }

        //sorting total sales of "commoditySales" into the decending order and renaming it "commodityEntries"
        List<Entry<String, Long>> commodityEntries = new ArrayList<Entry<String, Long>>(commoditySales.entrySet());
        Collections.sort(commodityEntries, new Comparator<Entry<String, Long>>(){
            public int compare(Entry<String, Long> obj1, Entry<String, Long> obj2){
                return obj2.getValue().compareTo(obj1.getValue());
            }
        });

        //creating a new file named "commodity.out" for the output(sales for each commodity)
        if(!writingOutputs(commodityOutput,path,commodity,commodityEntries)){
        	return;
        }

    }
}
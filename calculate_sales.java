import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class calculate_sales{
    public static void main(String args[]){
        //accepting command lines
        File file = new File(args[0]);
        File file2 = new File(args[1]);

        //adding error messages if files cannot be found in the URL provided
        if(!file.exists()){
            System.out.println("支店定義ファイルが存在しません");
            System.exit(0);
        }else if(!file2.exists()){
            System.out.println("商品定義ファイルが存在しません");
            System.exit(0);
        }else{
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));

                String str = null;

                    //creating HashMap to store data about branch and commodity
                    Map<Integer,String> branch = new HashMap<Integer,String>();
                    Map<String,String> commodity = new HashMap<String,String>();

                //putting what we get from the first command line into branch HashMap
                while((str=br.readLine())!=null){
                    String[] strAry = str.split(",");
                    Integer num = Integer.parseInt(strAry[0]);
                    branch.put(num,strAry[1]);

                    //adding error messages if the format is unnapropriate
                    String regex = "\\d\\d\\d";

                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(strAry[0]);


                    if(!m.find()){
                        System.out.println("支店定義ファイルのフォーマットが不正です");
                        System.exit(0);
                    }
                }
                System.out.println("支店定義ファイルを読み込みました");

                //printing out what is inside branch HashMap
                for(Map.Entry<Integer,String> e : branch.entrySet()){
                    System.out.println(e.getKey()+":"+e.getValue());
                }

                //putting what we get from the second command line into commodity HashMap
                br = new BufferedReader(new FileReader(file2));
                while((str=br.readLine())!=null){
                    String[] strAry = str.split(",");
                    commodity.put(strAry[0],strAry[1]);

                    //adding error messages if the format is unnapropriate
                    String regex = "[0-9A-Z][0-9A-Z][0-9A-Z][0-9A-Z][0-9A-Z][0-9A-Z][0-9A-Z][0-9A-Z]";

                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(strAry[0]);

                    if(!m.find()){
                        System.out.println("商品定義ファイルのフォーマットが不正です");
                        System.exit(0);
                    }
                }
                System.out.println("商品定義ファイルを読み込みました");

                //printing out what is inside branch HashMap
                for(Map.Entry<String,String> e : commodity.entrySet()){
                    System.out.println(e.getKey()+":"+e.getValue());
                }

                br.close();
            }catch(FileNotFoundException e){
                System.out.println(e);
            }catch(IOException e){
                System.out.println(e);
            }

            System.out.println("ダウンロード完了");
        }
    }
}

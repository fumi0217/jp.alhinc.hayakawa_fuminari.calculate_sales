import java.io.*;
import java.util.HashMap;
import java.util.Map;

class calculate_sales{
    public static void main(String args[]){
        File file = new File(args[0]);
        File file2 = new File(args[1]);

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

                    Map<Integer,String> branch = new HashMap<Integer,String>();
                    Map<String,String> commodity = new HashMap<String,String>();

                while((str=br.readLine())!=null){
                    String[] strAry = str.split(",");
                    Integer num = Integer.parseInt(strAry[0]);
                    branch.put(num,strAry[1]);
                }
                System.out.println("支店定義ファイルを読み込みました");

                for(Map.Entry<Integer,String> e : branch.entrySet()){
                    System.out.println(e.getKey()+":"+e.getValue());
                }

                br = new BufferedReader(new FileReader(file2));
                while((str=br.readLine())!=null){
                    String[] strAry = str.split(",");
                    commodity.put(strAry[0],strAry[1]);
                }
                System.out.println("商品定義ファイルを読み込みました");

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

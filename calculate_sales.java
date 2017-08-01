import java.io.*;

class calculate_sales{
    public static void main(String args[]){
        File file = new File(args[0]);
        File file2 = new File(args[1]);
        try{
            BufferedReader br = new BufferedReader(new FileReader(file));

            String str = null;

            while((str=br.readLine())!=null){
                System.out.println(str);
            }

            BufferedReader br2 = new BufferedReader(new FileReader(file2));
            while((str=br2.readLine())!=null){
                System.out.println(str);
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

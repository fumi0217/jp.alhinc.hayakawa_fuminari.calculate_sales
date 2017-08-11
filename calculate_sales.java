import java.io.*;

class calculate_sales{
    public static void main(String args[]){

        int i = 0;

        while(args[i]!=null){
            File file = new File(args[i]);
            try{
                BufferedReader br = new BufferedReader(new FileReader(file));

                String str = null;

                while((str=br.readLine())!=null){
                    System.out.println(str);
                }
                br.close();
            }catch(FileNotFoundException e){
                System.out.println(e);
            }catch(IOException e){
                System.out.println(e);
            }

            i++;

        }


        System.out.println("ダウンロード完了");
    }
}

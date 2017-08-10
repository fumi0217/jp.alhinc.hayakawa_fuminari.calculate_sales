import java.io.*;

class calculate_sales{
    public static void main(String args[]){

        int i = 0;
        File file = new File(args[i]);

        do{

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

            file = new File(args[i]);
        }while(file.exists());


        System.out.println("ダウンロード完了");
    }
}

import java.net.*;
import java.io.*;

import java.util.*;
import static java.util.stream.Collectors.toList;

public class ReverseClient1 {
   //client connection
    public static void main(String[] args) throws InterruptedException {
        Random rand=new Random();
        int r = rand.nextInt((2 - 0) + 1) + 0;
        String[] message = new String[3];
        int[] ports=new int[3];
        ports[0]=12111;
        ports[1]=12112;
        ports[2]=12113;
        message[0]="enquiry";
        message[1]="read";
        message[2]="write";
        String hostname = "localhost";
        int port = ports[r];
        int count=1;
        try (Socket socket = new Socket(hostname, port)) {
             //create input ouput objects
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output));

            Console console = System.console();
            String text,lin;int k=2;
            List<String> myList = new ArrayList<>();
            String sen_text="",msg="";
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            do {


                if (msg.equalsIgnoreCase("busy")){
                  Thread.sleep(10000);
                }
                else if (msg!="") {
                   //enquires about list of files
                    if(sen_text.equals("enquiry")){
                      System.out.println("===============================================================");
                      System.out.println("The list of files:");
                      //gets list of files
                      if (myList.isEmpty()){
                        String t=msg;
                         String[] temp=t.split(",");
                         myList=Arrays.asList(temp);

                        System.out.println(myList);

                      }
                      else{

                          System.out.println(myList);

                      }
                      System.out.println("===============================================================");
                    }
                    //modifies a random file
                    else if (sen_text.equals("write")) {
                     int wNum = rand.nextInt((2 - 0) + 1) + 0;


                      System.out.println("===============================================================");
                      System.out.println("The Modification sent to"+myList.get(wNum));
                      writer.println(myList.get(wNum));
                      writer.flush();
                      //checks if some program is using the file
                       String y=reader.readLine();
                      if(y.equalsIgnoreCase("inuse")){

                        System.out.println("Sorry Some other file is trying to write");
                      }
                      else{
                        writer.println("This is the "+k+"th line.\r\n");
                        writer.flush();
                        k=k+1;
                      }
                      System.out.println("===============================================================");
                    }
                    //reads from a random file
                    else{
                      int wNum = rand.nextInt((2 - 0) + 1) + 0;
                      System.out.println("===============================================================");
                      System.out.println("The data of the file requested:"+myList.get(wNum));
                      writer.println(myList.get(wNum));
                      writer.flush();
                      String u=reader.readLine();
                      //checks if some program is using the file
                      if(u.equalsIgnoreCase("inuse")){
                        lin="Is being used try later";
                      }else{
                       lin=u;
                      }
                      System.out.println(lin);
                      System.out.println("===============================================================");
                      lin="";
                    }
                    msg="";
                }

                  int randomNum = rand.nextInt((2 - 0) + 1) + 0;
                  sen_text =message[randomNum];
                  if(count==1){
                    sen_text="enquiry";count=2;
                  }
                  count++;
                  if (count==30) {
                      sen_text="quit";
                      writer.println(sen_text);
                      writer.flush();
                      break;
                  }
                  writer.println(sen_text);
                  writer.flush();
                  System.out.println(sen_text);

                  Thread.sleep(1000);

                msg = reader.readLine();

                // System.out.println(text);

            } while (!msg.equals("bye"));

            socket.close();

        } catch (UnknownHostException ex) {

            System.out.println("Server not found: " + ex.getMessage());

        } catch (IOException ex) {

            System.out.println("I/O error: " + ex.getMessage());
        }
    }
}

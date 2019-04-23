import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer2 {
  public static void main(String[] args) throws Exception {
    ServerSocket m_ServerSocket = new ServerSocket(12112);
    int id = 0;
    //creates sockets
    while (true) {
      Socket clientSocket = m_ServerSocket.accept();
      ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++);
      cliThread.start();
    }
  }
}
//thread for each client
class ClientServiceThread extends Thread {
  Socket clientSocket;
  int clientID = -1;
  boolean running = true;
  String[][] read ={{"abc.txt","def.txt","ghi.txt"},{"0","0","0"}};

  ClientServiceThread(Socket s, int i) {
    clientSocket = s;
    clientID = i;
  }

  public void run() {

    //accepts clent connection
    System.out.println("Accepted Client : ID - " + clientID + " : Address - "
        + clientSocket.getInetAddress().getHostName());
    try {
      BufferedReader   in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      PrintWriter   out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
      while (running) {
        String clientCommand = in.readLine();
        System.out.println("Client "+clientID+":" + clientCommand);
        if (clientCommand.equalsIgnoreCase("quit")) {
          running = false;
          System.out.print("Stopping client thread for client : " + clientID);
        }
        //processes enquiry command
        else if (clientCommand.equalsIgnoreCase("enquiry")) {
          File folder = new File("server1");
          File[] listOfFiles = folder.listFiles();
          String msg="";

          for (int i = 0; i < listOfFiles.length; i++) {
              if (listOfFiles[i].isFile()) {
                  msg=msg+listOfFiles[i].getName()+",";
                } else if (listOfFiles[i].isDirectory()) {
                  msg=msg+listOfFiles[i].getName()+",";
                }
         }

          out.println(msg);
          out.flush();
        }
        //processes read command
        else if (clientCommand.equalsIgnoreCase("read")) {
          String line = "",x="",file=""; String t="0"; int l=0;

          try{
              out.println("read");
              out.flush();
              String tem=in.readLine();
              file="server1\\"+tem;

              for(int k=0;k<3;k++){
                if(read[0][k].equals(tem)){
                  t=read[1][k];

                  l=k;
                }
              }
              if(t=="0"){

                read[1][l]="1";
                  FileReader fileReader = new FileReader(file);
                  BufferedReader fReader = new BufferedReader(fileReader);
                  while((x = fReader.readLine()) != null) {
                      line=line+x;
                  }

                  out.println(line);
                  out.flush();

                  // Always close files.
                  fReader.close();
                  read[1][l]="0";
              }else{
                // System.out.println("inuse");
                out.println("inuse");
                out.flush();
              }
          }
          catch(Exception e){
            e.printStackTrace();
          }


        }
        //processes write command
        else if (clientCommand.equalsIgnoreCase("write")) {
          String p="",file="",file1="",file2="",file3="",mg="";
          String[] temp; int s=0;
          try{
              out.println("file modified");
              out.flush();
              file=in.readLine();
              String g=""; String[] file_stat;

              FileReader fileRead = new FileReader("RW\\write.txt");
              BufferedReader fRed = new BufferedReader(fileRead);

              g=fRed.readLine();
              fRed.close();
              //reading write file
              file_stat=g.split(",");
              for(int k=0;k<3;k++){
                temp=file_stat[k].split(":");
                if(temp[0].equals(file))
                {
                  if(temp[1].equals("0")){
                    mg="notinuse";
                    s=k;
                    String te="";
                    for(int h=0;h<3;h++){
                      if(h!=k){
                       te=te+file_stat[h]+",";
                      }else{
                       te=te+temp[0]+":0,";
                      }
                    }
                    FileWriter fileWrite0 = new FileWriter("RW\\write.txt",false);
                    BufferedWriter fWrite0 = new BufferedWriter(fileWrite0);

                    fWrite0.write(te);
                    fWrite0.close();
                    out.println(mg);
                    out.flush();
                  }else{
                    mg="inuse";

                    out.println(mg);
                    out.flush();
                  }
                }
              }

              if(mg.equals("notinuse")){

                p=in.readLine();
                file1="server1\\"+file;
                FileWriter fileWrite1 = new FileWriter(file1,true);
                BufferedWriter fWrite1 = new BufferedWriter(fileWrite1);
                 fWrite1.write(p);
                // Always close files.
                fWrite1.close();
                file2="server2\\"+file;

                FileWriter fileWrite2 = new FileWriter(file2,true);
                BufferedWriter fWrite2 = new BufferedWriter(fileWrite2);
                fWrite2.write(p);
                // Always close files.
                fWrite2.close();
                file3="server3\\"+file;

                FileWriter fileWrite3 = new FileWriter(file3,true);
                BufferedWriter fWrite3 = new BufferedWriter(fileWrite3);
                fWrite3.write(p);
                // Always close files.
                fWrite3.close();

              }
              FileReader fileRea = new FileReader("RW\\write.txt");
              BufferedReader fRe = new BufferedReader(fileRea);

              g=fRe.readLine();
              fRe.close();
              //reading write file
              file_stat=g.split(",");
              for(int j=0;j<3;j++){
                temp=file_stat[j].split(":");
                String ted="";
                if(j==s){
                  temp[1]="0";
                  for(int w=0;w<3;w++){
                    if(w!=s){
                     ted=ted+file_stat[w]+",";
                    }else{
                     ted=ted+temp[0]+":1,";
                    }
                  }
                  FileWriter fileWrit0 = new FileWriter("RW\\write.txt",false);
                  BufferedWriter fWrit0 = new BufferedWriter(fileWrit0);

                  fWrit0.write(ted);
                  fWrit0.close();
                }

             }

          }
          catch(IOException e){
            e.printStackTrace();
          }
        }

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

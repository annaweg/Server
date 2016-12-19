import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by anna on 2016-05-03.
 */
public class WebServer {

    private static ServerSocket serverSocket;
    private static final int PORT = 8000;


    public static void main(String[] args) throws Exception {

        try {
            serverSocket = new ServerSocket(PORT);                  //serverSocket listens to port 8000
            System.out.println("Waiting for connections on port: " + PORT);

        } catch (IOException ioExc) {
            ioExc.printStackTrace();
            System.exit(1);
        }
        while(true){
            Socket connection = serverSocket.accept();              //block socket until client connects
            ServerThread thread = new ServerThread (connection);
            thread.start();                                         //start thread
        }
    }

    public static class ServerThread extends Thread {               //inner class
        private Socket newConnection;                               //the connection
        BufferedReader reader = null;                               //pick up message from client
        String httpRequest = null;
        String headerLine = null;
        StringTokenizer tokenizer = null;                           //parsing http request message
        PrintWriter out = null;
        File file = null;

        public ServerThread(Socket connection) throws Exception {   //constructor for inner class
            this.newConnection = connection;

            try {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                httpRequest = reader.readLine();                    //reads 1st line of http request message
            } catch (IOException ioEx) {
                ioEx.printStackTrace();
            }
        }

        public void run() {
            String serverDetail = " Server: Java HTTP/1.0 Server\n";
            String contentType = "Content Type: text/html\r\n";         //return + new empty line for protocol
            Date date = new Date();
            headerLine = httpRequest + serverDetail + date.toString() + "\n" + contentType;    //response message

            System.out.println(httpRequest);
            tokenizer = new StringTokenizer(httpRequest, " /.");

            if(tokenizer.nextToken().equals("GET")) {
                String nextToken = tokenizer.nextToken();

                if (nextToken.equals("HTTP") || nextToken.equals("index")) {
                    System.out.println("1st test");

                    try {
                        file = new File("/Users/anna/Dropbox/kea/3 semester/network/Red Assignment/Server/src/MyFiles/index.html");
                        reader = new BufferedReader(new FileReader(file));
                        out = new PrintWriter(newConnection.getOutputStream());

                        System.out.println(headerLine);
                        out.println(reader.readLine());
                        out.close();

                    } catch (IOException ioExc) {
                        ioExc.printStackTrace();
                    }

                } else {
                    System.out.println("2nd test");

                    try {
                        file = new File("/Users/anna/Dropbox/kea/3 semester/network/Red Assignment/Server/src/MyFiles/404.html");
                        reader = new BufferedReader(new FileReader(file));
                        out = new PrintWriter(newConnection.getOutputStream());

                        System.out.println(headerLine);
                        out.println(reader.readLine());
                        out.close();
                    } catch (IOException ioExc) {
                        ioExc.printStackTrace();
                    }
                }

            } else {
                System.out.println("last test");

                try {
                    file = new File("/Users/anna/Dropbox/kea/3 semester/network/Red Assignment/Server/src/MyFiles/501.html");
                    reader = new BufferedReader(new FileReader(file));
                    out = new PrintWriter(newConnection.getOutputStream());

                    out.println(headerLine);
                    out.println(reader.readLine());
                    out.close();
                } catch (IOException ioExc) {
                    ioExc.printStackTrace();
                }
            }
        }

        private static double getFileSize (File file){
            double bytes = file.length();
            return bytes/1024;
        }
    }
}


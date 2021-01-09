// imports
import java.io.*;
import java.util.Scanner;
import java.net.*;

public class MultiClientSide {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);

        // takes the host name and port as user input and creates a new socket to connect to a server
        try {
            System.out.println("Welcome to the Client! To get started, please enter a host and port to search for a Server.");
            System.out.println("");

            System.out.printf("Host Name: ");
            String hostName = userInput.nextLine();

            System.out.printf("Port: ");
            int port = userInput.nextInt();

            Socket socket = new Socket(InetAddress.getByName(hostName), port);

            System.out.println("");
            System.out.println("A Server has been found! You can start making requests.");
            System.out.println("");

            // creating data input and data output stream to communicate back and forth with the server
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            // asks client how many requests they would like to make
            System.out.println("How may requests would you like to make?");

            // takes user input for number of requests and sends it to the server
            PrintWriter reply = new PrintWriter(output, true);
            int request = userInput.nextInt();
            reply.println(request);

            while (true) {
                // prints menu options for the client
                System.out.println("");

                System.out.printf("Please make a selection from the menu below:\n" +
                        "1. View Date and Time\n" +
                        "2. View Server Run Time\n" +
                        "3. View Server Memory Usage\n" +
                        "4. View Network Connections\n" +
                        "5. View Current Users\n" +
                        "6. View Current Running Processes\n" +
                        "7. Exit\n");

                // takes user input for menu option and sends it to the server
                int menuOption = userInput.nextInt();
                output.writeUTF(Integer.toString(menuOption));
                System.out.println("");

                // if the user inputs 7, then the connection will be closed
                if(menuOption == 7) {
                    System.out.println("Total Elapsed Time for each Request: \n");

                    BufferedReader toRead = new BufferedReader(new InputStreamReader(input));

                    String[] result = new String[request];
                    for(int k = 0; k < request; k++) {
                        result[k] = toRead.readLine();
                        System.out.println(result[k]);
                     }
                     System.out.println("");

                     System.out.println(toRead.readLine());
                     System.out.println(toRead.readLine());

                    System.out.println("\nClosing the connection. Bye for now!");
                    break;
                }

                // reads output based on menu option the client selected
                if (menuOption == 4) {
                    String temp = "";
                    BufferedReader toRead = new BufferedReader(new InputStreamReader(input));

                    String[] store = new String[25];
                    for(int k = 0; k < 25; k++) {
                        store[k] = toRead.readLine();
                        for (int j = 0; j < request; j++) {
                            System.out.println(store[k]);
                        }
                    }
                } else {
                    for (int i = 0; i < request; i++) {
                        System.out.println(input.readUTF());
                    }
                }
            }
            input.close();
            output.close();
        } catch(Exception e) {
            System.out.println("Server cannot be found. Please try again.");
            e.printStackTrace();
        }
        userInput.close();
    }
}




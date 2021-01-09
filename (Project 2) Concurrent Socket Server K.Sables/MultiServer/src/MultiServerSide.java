// imports
import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Date;
import java.util.Scanner;
import java.net.*;

// ServerSide Class
public class MultiServerSide {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);

        System.out.println("Welcome to the Server!");
        System.out.println("");

        System.out.printf("Please enter a port to get started: ");
        // takes a port as user input
        int port = userInput.nextInt();

        try {
            // making a server socket - waiting for clients to connect
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Please stand by ...");

            while(true) {
                // accepts client connection and waits for the client to make requests
                Socket socket = serverSocket.accept();
                System.out.println("");
                System.out.println("A connection has been established with: " + socket);
                System.out.println("Please wait for client requests ...");

                // making a data input and output stream in order to communicate back and forth with the client
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                DataInputStream input = new DataInputStream(socket.getInputStream());

                // reads the number of requests made from the client
                BufferedReader readRequests = new BufferedReader(new InputStreamReader(input));
                int numOfRequests = Integer.parseInt(readRequests.readLine());

                System.out.println("");
                System.out.println("Making " + numOfRequests + " thread(s) according to client request.");

                long[] startTime = new long[numOfRequests];
                long[] endTime = new long[numOfRequests];

                // start of calculating client elapsed time (in milliseconds)
                for (int i = 0; i < numOfRequests; i++) {
                    startTime[i] = System.currentTimeMillis();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        System.out.println("Something got interrupted. Please try again.");
                    }
                }

                // server making and executing threads at once based on the client's number of requests
                String inputGet = null;
                while (inputGet == null) {
                    String outputSend;
                    inputGet = input.readUTF();

                    switch (inputGet) {
                        // operation to send client the server's current date and time
                        case "1":
                            outputSend = new Date().toString();
                            for (int i = 0; i < numOfRequests; i++) {
                                new Thread(() -> {
                                    try {
                                        output.writeUTF("Server Date: " + outputSend);
                                    } catch (Exception e) {
                                        System.out.println("A server error has occurred. Please try again.");
                                        e.printStackTrace();
                                    }
                                }).start();
                            }
                            inputGet = null;
                            break;

                        // operation to send client the server's current uptime since last boot up
                        case "2":
                            RuntimeMXBean serverUpTime = ManagementFactory.getRuntimeMXBean();
                            long uptime = serverUpTime.getUptime();

                            outputSend = Long.toString(uptime);

                            for (int i = 0; i < numOfRequests; i++) {
                                new Thread(() -> {
                                    try {
                                        output.writeUTF("Server's Uptime: " + outputSend + " ms");
                                    } catch (Exception e) {
                                        System.out.println("A server error has occurred. Please try again.");
                                        e.printStackTrace();
                                    }
                                }).start();
                            }
                            inputGet = null;
                            break;

                        // operation to send client the server's memory usage
                        case "3":
                            Runtime memory = Runtime.getRuntime();

                            long freeMemory = memory.freeMemory();
                            long totalMemory = memory.totalMemory();

                            String usedMemory = Long.toString(totalMemory - freeMemory);

                            for (int i = 0; i < numOfRequests; i++) {
                                new Thread(() -> {
                                    try {
                                        output.writeUTF("Used Memory: " + usedMemory + " bytes");
                                    } catch (Exception e) {
                                        System.out.println("A server error has occurred. Please try again.");
                                        e.printStackTrace();
                                    }
                                }).start();
                            }
                            inputGet = null;
                            break;

                        // operation to send client the server's connections
                        /* NOTE ON THIS COMMAND: Only printed the first 25 lines as the netstat command
                                                was too long of a list and we wanted to avoid the program
                                                from crashing.
                        */
                        case "4":
                            Process netstat = Runtime.getRuntime().exec("netstat -ano");

                            PrintWriter sendNetStat = new PrintWriter(output, true);
                            BufferedReader getNetstat = new BufferedReader(new InputStreamReader(netstat.getInputStream()));

                            String[] netstatArray = new String [25];

                            for (int i = 0; i < 25; i++) {
                                netstatArray[i] = getNetstat.readLine();
                                sendNetStat.println(netstatArray[i]);
                            }
                            inputGet = null;
                            break;

                        // operation to send client the server's current users
                        case "5":
                            Process currentUsers = Runtime.getRuntime().exec("whoami");

                            BufferedReader getCurrentUsers = new BufferedReader(new InputStreamReader(currentUsers.getInputStream()));
                            outputSend = getCurrentUsers.readLine();

                            for (int i = 0; i < numOfRequests; i++) {
                                new Thread(() -> {
                                    try {
                                        output.writeUTF("Current Users: \n" + outputSend);
                                        System.out.println("");
                                    } catch (Exception e) {
                                        System.out.println("A server error has occurred. Please try again.");
                                        e.printStackTrace();
                                    }
                                }).start();
                            }
                            inputGet = null;
                            break;

                        // operation to send client the server's current running processes
                        /* NOTE ON THIS COMMAND: Was not able to get "ps" or "ps -a" to work for this case;
                                                 Also tried other alternatives such as "Get-Process", which
                                                 also did not work. Suspect that it might be the library that
                                                 might be causing the issue of why the command does not want to
                                                 execute. While we have a string placeholder for this command, below
                                                 is also the commented code to show the attempt made to execute the
                                                 command.
                        */
                        case "6":
                            //  Process processes = Runtime.getRuntime().exec("ps -a");

                            /* PrintWriter sendProcesses = new PrintWriter(output, true);
                            BufferedReader getProcesses = new BufferedReader(new InputStreamReader(processes.getInputStream()));
                            while ((outputSend = getProcesses.readLine()) != null) {
                                sendProcesses.println(outputSend);
                            } */
                            for (int i = 0; i < numOfRequests; i++) {
                                new Thread(() -> {
                                    try {
                                        output.writeUTF("COMMAND PLACEHOLDER");
                                    } catch (Exception e) {
                                        System.out.println("A server error has occurred. Please try again.");
                                        e.printStackTrace();
                                    }
                                }).start();
                            }
                            inputGet = null;
                            break;

                        // operation to return elapsed time and close the connection when the client exits
                        case "7":
                            // end of calculating client elapsed time
                            for (int i = 0; i < numOfRequests; i++) {
                                endTime[i] = System.currentTimeMillis();
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    System.out.println("Something got interrupted. Please try again.");
                                }
                            }

                            long[] elapsedTime = new long[numOfRequests];
                            long totalElapsedTime = 0;
                            long averageElapsedTime = 0;

                            PrintWriter sendResult = new PrintWriter(output, true);

                            for (int i = 0; i < numOfRequests; i++) {
                                elapsedTime[i] = endTime[i] - startTime[i];
                                totalElapsedTime += elapsedTime[i];

                                sendResult.println("Request " + (i + 1) + ": " + elapsedTime[i] + " ms");
                            }
                            averageElapsedTime = totalElapsedTime / numOfRequests;

                            sendResult.println("Total Elapsed Time: " + totalElapsedTime + " ms");
                            sendResult.println("Average Elapsed Time: " + averageElapsedTime + " ms");

                            inputGet = "0";
                            break;

                        // default case for entering an invalid menu option
                        default:
                            output.writeUTF("Invalid input. Please try again.");
                            inputGet = null;
                            break;
                    }
                    if (inputGet == "7" && inputGet != null) {
                        socket.close();
                        break;
                    }
                }
                System.out.println("All requests finished executing. Back to listening for clients . . .");
            }
        } catch (Exception e) {
            System.out.println("A server error has occurred. Please try again.");
            e.printStackTrace();
        }
        userInput.close();
    }
}
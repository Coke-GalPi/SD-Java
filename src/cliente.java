import java.net.*;
import java.io.*;
import java.util.Scanner;

public class cliente {

    public static void mostrarMenu() {
        System.out.println("-- Menu --");
        System.out.println("1.- Dar Significado.");
        System.out.println("0.- Salir");
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("\n\nIngrese la palabra: ");
            String palabra = scanner.nextLine();
            if (palabra.isEmpty()) break;

            try {
                DatagramSocket unSocket = new DatagramSocket();
                InetAddress unHost = InetAddress.getByName("localhost");
                String mensaje = "buscar:" + palabra;
                byte[] m = mensaje.getBytes();
                DatagramPacket peticion = new DatagramPacket(m, m.length, unHost, 6789);
                unSocket.send(peticion);

                byte[] bufer = new byte[1000];
                DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
                unSocket.receive(respuesta);
                String respuestaString = new String(respuesta.getData(), 0, respuesta.getLength()).trim();

                if (respuestaString.equals("Palabra no encontrada")) {
                    System.out.println(respuestaString);
                    mostrarMenu();
                    System.out.print("Ingrese su opción: ");
                    int opcion = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    if (opcion == 1) {
                        System.out.print("Ingrese el significado: ");
                        String significado = scanner.nextLine();
                        mensaje = "agregar:" + palabra + "," + significado;
                        m = mensaje.getBytes();
                        peticion = new DatagramPacket(m, m.length, unHost, 6789);
                        unSocket.send(peticion);
                        unSocket.receive(respuesta);
                        System.out.println(new String(respuesta.getData(), 0, respuesta.getLength()).trim());
                    } else if (opcion == 0) {
                        System.out.println("Saliendo del programa...");
                        break;
                    }
                } else {
                    System.out.println(respuestaString);
                }
                unSocket.close();
            } catch (SocketException e) {
                System.out.println("Socket Error: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO Error: " + e.getMessage());
            }
        }
        scanner.close();
    }
}

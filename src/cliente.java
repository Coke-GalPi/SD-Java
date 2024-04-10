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
        System.out.print("\n\nIngrese la palabra: ");
        String mensaje = scanner.nextLine();
        String significado;
        String nombreServidor = "localhost";
        try {
            DatagramSocket unSocket = new DatagramSocket();
            byte[] m = mensaje.getBytes();
            InetAddress unHost = InetAddress.getByName(nombreServidor);
            int puertoServidor = 6789;
            DatagramPacket peticion = new DatagramPacket(m, mensaje.length(), unHost, puertoServidor);
            unSocket.send(peticion);
            byte[] bufer = new byte[1000];
            DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
            unSocket.receive(respuesta);
            // String res = new String(respuesta.getData());
            // System.out.println("-> " + res);
            String respuestaString = new String(respuesta.getData()).trim();
            String respuestaEsperada = "La palabra no fue encontrada en la base de datos.";
            if (respuestaString.equalsIgnoreCase(respuestaEsperada)) {
                System.out.println(new String(respuesta.getData()));
                int opcion;
                do {
                    mostrarMenu();
                    System.out.print("Ingrese su opción: ");
                    opcion = scanner.nextInt();
                    switch (opcion) {
                        case 1:
                            System.out.print("Dale significado: ");
                            significado = scanner.nextLine();
                            break;
                        case 0:
                            System.out.println("Saliendo del programa...");
                            break;
                        default:
                            System.out.println("Opción no válida. Por favor, seleccione nuevamente.");
                    }
                } while (opcion != 0);
            }
            unSocket.close();
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
        scanner.close();
    }
}
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class cliente {
    public static void mostrarMenuApp() {
        System.out.println("\n\n-- Menu de APP --");
        System.out.println("1.- Diccionario");
        System.out.println("2.- Cambio moneda.");
        System.out.println("0.- Cerrar App");
    }

    public static void mostrarMenu() {
        System.out.println("\n-- Menu --");
        System.out.println("1.- Dar Significado.");
        System.out.println("0.- Salir");
    }

    public static void mostrarMenuMoneda() {
        System.out.println("\n-- Menu --");
        System.out.println("1.- Clp -> Internacional");
        System.out.println("2.- Internacional -> Clp");
        System.out.println("0.- Cerrar App");
    }

    public static void main(String args[]) {
        Scanner scanner = new Scanner(System.in);
        int opcionApp;
        do {
            mostrarMenuApp();
            System.out.print("Ingrese una opcion: ");
            opcionApp = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (opcionApp) {
                case 1:
                    System.out.println("\nDiccionario.");
                    String palabra;
                    do {
                        System.out.println("\nDeje vacío para volver al menú");
                        System.out.print("Ingrese la palabra: ");
                        palabra = scanner.nextLine();
                        if (!palabra.isEmpty()) {
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
                                String respuestaString = new String(respuesta.getData(), 0, respuesta.getLength())
                                        .trim();
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
                                        System.out.println(
                                                new String(respuesta.getData(), 0, respuesta.getLength()).trim());
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
                    } while (!palabra.isEmpty());
                    break;
                case 2:
                    int opcionMoneda;
                    double montoOriginal, valorMoneda;
                    String moneda;
                    do {
                        mostrarMenuMoneda();
                        System.out.print("Ingrese un opcion: ");
                        opcionMoneda = scanner.nextInt();
                        switch (opcionMoneda) {
                            case 1:
                                System.out.println("\nClp -> Internacional.");
                                do {
                                    System.out.println("Ingrese monto en pesos");
                                    System.out.print("Ingrese monto de su moneda: $");
                                    montoOriginal = scanner.nextDouble();
                                    scanner.nextLine();
                                    System.out.print("Tipo de moneda de cambio: ");
                                    moneda = scanner.nextLine();
                                    System.out.print("Ingrese el valor de la moneda de cambio: $");
                                    valorMoneda = scanner.nextDouble();
                                    try {
                                        DatagramSocket unSocket = new DatagramSocket();
                                        InetAddress unHost = InetAddress.getByName("localhost");
                                        String mensaje = "Internacional:" + montoOriginal + "," + valorMoneda;
                                        byte[] m = mensaje.getBytes();
                                        DatagramPacket peticion = new DatagramPacket(m, m.length, unHost, 6789);
                                        unSocket.send(peticion);
                                        byte[] bufer = new byte[1000];
                                        DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
                                        unSocket.receive(respuesta);
                                        String respuestaString = new String(respuesta.getData(), 0,
                                                respuesta.getLength())
                                                .trim();
                                        System.out.println(respuestaString + " " + moneda);
                                        unSocket.close();
                                    } catch (SocketException e) {
                                        System.out.println("Socket Error: " + e.getMessage());
                                    } catch (IOException e) {
                                        System.out.println("IO Error: " + e.getMessage());
                                    }
                                } while (montoOriginal < 0.0 && valorMoneda < 0.0 && !moneda.isEmpty());
                                break;
                            case 2:
                                System.out.println("\nInternacional -> Clp");
                                do {
                                    System.out.print("Tipo de moneda de cambio: ");
                                    moneda = scanner.nextLine();
                                    scanner.nextLine();
                                    System.out.print("Ingrese monto de su moneda: $");
                                    montoOriginal = scanner.nextDouble();
                                    System.out.print("Ingrese el valor de la moneda de cambio: $");
                                    valorMoneda = scanner.nextDouble();
                                    try {
                                        DatagramSocket unSocket = new DatagramSocket();
                                        InetAddress unHost = InetAddress.getByName("localhost");
                                        String mensaje = "CLP:" + montoOriginal + "," + valorMoneda;
                                        byte[] m = mensaje.getBytes();
                                        DatagramPacket peticion = new DatagramPacket(m, m.length, unHost, 6789);
                                        unSocket.send(peticion);
                                        byte[] bufer = new byte[1000];
                                        DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
                                        unSocket.receive(respuesta);
                                        String respuestaString3 = new String(respuesta.getData(), 0,
                                                respuesta.getLength())
                                                .trim();
                                        System.out.println(respuestaString3 + " " + moneda);
                                        unSocket.close();
                                    } catch (SocketException e) {
                                        System.out.println("Socket Error: " + e.getMessage());
                                    } catch (IOException e) {
                                        System.out.println("IO Error: " + e.getMessage());
                                    }
                                } while (montoOriginal < 0.0 && valorMoneda < 0.0 && !moneda.isEmpty());
                                break;
                            default:
                                System.out.println("\n\nSaliendo de la APP...\n");
                                break;
                        }
                    } while (opcionMoneda != 0);
                    break;
                case 0:
                    System.out.println("\n\nSaliendo de la APP...\n");
                    break;
                default:
                    System.out.println("Opcion no valida");
            }
        } while (opcionApp != 0);
        scanner.close();
    }
}

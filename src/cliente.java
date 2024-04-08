import java.net.*;
import java.io.*;

public class cliente {
    public static void main(String args[]) {
        // ? args proporciona el mensaje y el nombre del servidor
        try {
            DatagramSocket unSocket = new DatagramSocket();
            byte[] m = args[0].getBytes();
            InetAddress unHost = InetAddress.getByName(args[1]);
            int puertoServidor = 6789;
            DatagramPacket peticion = new DatagramPacket(m, args[0].length(), unHost, puertoServidor);
            unSocket.send(peticion);
            byte[] bufer = new byte[1000];
            DatagramPacket respuesta = new DatagramPacket(bufer, bufer.length);
            unSocket.receive(respuesta);
            System.out.println("Respuesta: " + new String(respuesta.getData()));
            unSocket.close();
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}

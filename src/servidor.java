import java.net.*;
import java.io.*;

public class servidor {
    public static void main(String args[]) {
        try {
            DatagramSocket unSocket = new DatagramSocket(6789);
            byte[] bufer = new byte[1000];
            while (true) {
                DatagramPacket peticion = new DatagramPacket(bufer, bufer.length);
                unSocket.receive(peticion);
                DatagramPacket respuesta = new DatagramPacket(peticion.getData(),
                        peticion.getLength(), peticion.getAddress(), peticion.getPort());
                unSocket.send(respuesta);
                System.out.println("Mensaje Recibido: " + new String(peticion.getData()));
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}

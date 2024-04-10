import java.net.*;
import java.io.*;
import java.sql.*;

public class servidor {
    public static void main(String args[]) {
        try {
            DatagramSocket unSocket = new DatagramSocket(6789);
            byte[] bufer = new byte[1000];
            while (true) {
                DatagramPacket peticion = new DatagramPacket(bufer, bufer.length);
                unSocket.receive(peticion);
                String mensajeRecibido = new String(peticion.getData()).trim();
                System.out.println("\n\nPalabra para buscar: " + mensajeRecibido);
                String respuestaMensaje = "";
                try {
                    final String url = "jdbc:mysql://localhost:3306/diccionario";
                    final String username = "root";
                    final String password = "";
                    Connection con = DriverManager.getConnection(url, username, password);
                    String query = "SELECT SIGNIFICADO FROM palabras WHERE PALABRA = ?";
                    PreparedStatement statement = con.prepareStatement(query);
                    statement.setString(1, mensajeRecibido);
                    ResultSet resultSet = statement.executeQuery();
                    if (resultSet.next()) {
                        respuestaMensaje = "Significado: " + resultSet.getString("SIGNIFICADO");
                    } else {
                        // respuestaMensaje = "La palabra no fue encontrada en la base de datos.";
                        respuestaMensaje = null;
                    }
                    con.close();
                } catch (Exception e) {
                    System.out.println("Error: " + e);
                }
                byte[] respuestaBytes = respuestaMensaje.getBytes();
                DatagramPacket respuesta = new DatagramPacket(respuestaBytes, respuestaBytes.length,
                        peticion.getAddress(), peticion.getPort());
                unSocket.send(respuesta);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}

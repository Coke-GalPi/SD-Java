import java.net.*;
import java.io.*;
import java.sql.*;
import java.text.DecimalFormat;

public class servidor {
    public static void main(String args[]) {
        try {
            DatagramSocket unSocket = new DatagramSocket(6789);
            byte[] bufer = new byte[1000];

            while (true) {
                DatagramPacket peticion = new DatagramPacket(bufer, bufer.length);
                unSocket.receive(peticion);
                String mensajeRecibido = new String(peticion.getData(), 0, peticion.getLength()).trim();

                String[] partes = mensajeRecibido.split(":", 2);
                String accion = partes[0];
                String contenido = partes.length > 1 ? partes[1] : "";

                String respuestaMensaje = "";

                try {
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/diccionario", "root", "");
                    if (accion.equals("buscar")) {
                        PreparedStatement statement = con
                                .prepareStatement("SELECT SIGNIFICADO FROM palabras WHERE PALABRA = ?");
                        statement.setString(1, contenido);
                        ResultSet resultSet = statement.executeQuery();
                        if (resultSet.next()) {
                            respuestaMensaje = "Significado: " + resultSet.getString("SIGNIFICADO");
                        } else {
                            respuestaMensaje = "Palabra no encontrada";
                        }
                    } else if (accion.equals("agregar")) {
                        PreparedStatement statement = con
                                .prepareStatement("INSERT INTO palabras (PALABRA, SIGNIFICADO) VALUES (?, ?)");
                        String[] datos = contenido.split(",", 2);
                        if (datos.length < 2) {
                            respuestaMensaje = "Error: formato incorrecto para agregar.";
                        } else {
                            statement.setString(1, datos[0]);
                            statement.setString(2, datos[1]);
                            statement.executeUpdate();
                            respuestaMensaje = "Palabra agregada exitosamente";
                        }
                    } else if (accion.equals("cambio")) {
                        double montoOriginal, valorMoneda, total;
                        String totalFormato;
                        String[] datos = contenido.split(",", 2);
                        if (datos.length < 2) {
                            respuestaMensaje = "Error: formato incorrecto para agregar.";
                        } else {
                            montoOriginal = Double.parseDouble(datos[0]);
                            valorMoneda = Double.parseDouble(datos[1]);
                            total = (montoOriginal / valorMoneda);
                            DecimalFormat formato = new DecimalFormat("#.##");
                            totalFormato = formato.format(total);
                            respuestaMensaje = "El cambio es de: " + totalFormato;
                        }
                    }
                    con.close();
                } catch (SQLException e) {
                    System.out.println("SQL Error: " + e.getMessage());
                    respuestaMensaje = "Error en base de datos";
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    respuestaMensaje = "Error en el servidor";
                }

                byte[] respuestaBytes = respuestaMensaje.getBytes();
                DatagramPacket respuesta = new DatagramPacket(respuestaBytes, respuestaBytes.length,
                        peticion.getAddress(), peticion.getPort());
                unSocket.send(respuesta);
            }
        } catch (SocketException e) {
            System.out.println("Socket Error: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        }
    }
}

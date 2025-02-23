package ejercicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Exercises {

    // declaro primero la conexion
    private Connection connection;

    public void openConnection(String bd, String servidor, String usuario, String password)
            throws ClassNotFoundException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            String url = String.format("jdbc:mariadb://%s:3306/%s", servidor, bd);
            // establecer conexion a la bd :D
            this.connection = DriverManager.getConnection(url, usuario, password);
            if (this.connection != null) {
                System.out.println("Conectado a " + bd + " en " + servidor);
            } else {
                System.out.println("No conectado a " + bd + " en servidor");
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getLocalizedMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("Código error: " + e.getErrorCode());
        }
    }

    /*
     * Consultar alumnos que contengan una cadena de caracteres en su nombre
     * ademas visualizar el numero de resultados , con un consulta
     */
    public void consultaAlumnos(String character) {
        int counter = 0;

        try (Statement st = connection.createStatement()) {
            String query = String.format("SELECT * FROM ALUMNOS WHERE nombre like '%%s%'", character);
            ResultSet resultSet = st.executeQuery(query);

            while (resultSet.next()) {

                System.out.printf("%s %n Student : %", "----------------", resultSet.getString("nombre"));
            }
            System.out.printf("%d total students with this pattern %s", counter, character);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Exercises exercises = new Exercises();
        try {
            exercises.openConnection("add", "localhost", "root", "");
            exercises.consultaAlumnos("a");

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

}

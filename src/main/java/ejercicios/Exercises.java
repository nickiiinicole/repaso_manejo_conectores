package ejercicios;

import java.awt.Taskbar.State;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.checkerframework.checker.units.qual.s;

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
     * ademas visualizar el numero de resultados
     */
    public void consultaAlumnos(String character) {
        int counter = 0;

        String query = "SELECT * FROM alumnos WHERE nombre like ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, "%" + character + "%");
            ResultSet resultSet = pst.executeQuery();

            System.out.println("----------------------");
            while (resultSet.next()) {
                counter++;
                System.out.printf("Student : %s %n", resultSet.getString("nombre"));
            }
            System.out.printf("%d total students with this pattern %s %n", counter, character);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void darAltaAlumno(String name, String surname, int tall, int numberRoom) {

        String query = "INSERT INTO ALUMNOS (nombre, apellidos, altura, aula) VALUES (?,?,?,?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {

            pst.setString(1, name);
            pst.setString(2, surname);
            pst.setInt(3, tall);
            pst.setInt(4, numberRoom);

            int rowAffected = pst.executeUpdate();
            System.out.println("Rows affected " + rowAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void darAltaAsignatura(int id, String name) {

        String query = "INSERT INTO ASIGNATURAS (cod, nombre) VALUES (?,?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            pst.setString(2, name);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Rows affected " + rowsAffected);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void darBajaAlumno(int id) {

        String query = "DELETE FROM ALUMNOS WHERE codigo = ?  ";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);

            int rowAffected = pst.executeUpdate();
            System.out.println("Rows affected " + rowAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void darBajaAsignatura(int id) {

        String query = "DELETE FROM ASIGNATURAS WHERE cod = ?  ";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);

            int rowAffected = pst.executeUpdate();
            System.out.println("Rows affected " + rowAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void nombreAulasConAlumnos() {

        String query = "select alumnos.nombre, aulas.nombreAula from alumnos JOIN aulas ON aulas.numero = alumnos.codigo; ";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            System.out.println("---------------- -------------------------");
            while (resultSet.next()) {
                System.out.printf("Student: %s Class: %s %n", resultSet.getString(1), resultSet.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Nombre de los alumnos, de las asignaturas y notas de aquellos alumnos que
     * han aprobado alguna asignatura.
     */

    public void nombreAlumnoAprobado() {

        String query = "SELECT alumnos.nombre, notas.NOTA, asignaturas.NOMBRE FROM alumnos JOIN notas ON notas.alumno = alumnos.codigo JOIN asignaturas ON notas.asignatura = asignaturas.COD WHERE notas.NOTA >= 5";

        try (Statement st = connection.createStatement()) {

            ResultSet resultSet = st.executeQuery(query);

            System.out.println("---------------------");
            while (resultSet.next()) {
                System.out.printf("%s %d %s %n", resultSet.getString(1), resultSet.getInt(2),
                        resultSet.getString("nombre"));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void nombreAsignaturasSinAlumnos() {
        String query = "SELECT asignaturas.NOMBRE FROM asignaturas WHERE asignaturas.COD NOT IN (SELECT notas.asignatura FROM notas);";

        // .....repetir elstatement

    }

    public void nombreAlumnoPatronAltura(String pattern, int height) {

        String query = "SELECT alumnos.nombre FROM ALUMNOS WHERE nombre like ? and altura>?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, "%" + pattern + "%");
            pst.setInt(2, height);
            ResultSet resultSet = pst.executeQuery();
            while (resultSet.next()) {
                System.out.printf("Student: %s %n", resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void añadirNuevaColumna(String table, String nameField, String typeField, String description) {
        String query = "ALTER TABLE ? add ? ? ? ";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, table);
            pst.setString(2, nameField);
            pst.setString(3, typeField);
            pst.setString(4, description);
            int rowsAffected = pst.executeUpdate();
            System.out.println("Rows affected " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    

    public static void main(String[] args) {

        Exercises exercises = new Exercises();
        try {
            exercises.openConnection("add", "localhost", "root", "");
            // exercises.consultaAlumnos("a");
            // exercises.darAltaAlumno("nicole", "diaz", 161, 21);
            // exercises.darAltaAsignatura(9, "Empresa");
            // exercises.darBajaAlumno(6);
            // exercises.nombreAulasConAlumnos();
            // exercises.nombreAlumnoAprobado();
            // exercises.nombreAlumnoPatronAltura("nicole", 159);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

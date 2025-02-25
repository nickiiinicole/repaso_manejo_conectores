package ejercicios;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Exercises {

    // declaro primero la conexion
    private Connection connection;
    private Connection connectionSQLite;

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

    public void openConnectionSQLite(String bd, String server, String user, String password) {
        try {
            // Class.forName("org.sqlite.Driver");
            String url = String.format("jdbc:sqlite:", bd);
            this.connectionSQLite = DriverManager.getConnection(url);
            if (this.connectionSQLite != null) {
                System.out.println("conectado a la " + bd);
            } else {
                System.out.println("No conectado a " + bd);
            }

        } catch (SQLException e) {
            e.printStackTrace();
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

    public void obtencionInfoBD() {
        try {
            DatabaseMetaData data = connection.getMetaData();
            System.out.println("Driver: " + data.getDriverName());
            System.out.println("Version Driver: " + data.getDriverVersion());
            System.out.println("Url de conexion " + data.getURL());
            System.out.println("name del SGBD " + data.getDatabaseProductName());
            System.out.println("version del SGBD " + data.getDatabaseProductVersion());
            System.out.println("Palabras reservadas en SGDB " + data.getSQLKeywords());

            ResultSet catalogs = data.getCatalogs();
            while (catalogs.next()) {
                System.out.println("Catalogs " + catalogs.getString(1));

            }

            System.out.println("---------------TABLES------------");
            ResultSet tables = data.getTables("add", null, null, null);
            while (tables.next()) {
                System.out.println("Nombre de la tabla " + tables.getString("TABLE_NAME"));
                System.out.println("Tipo de tabla: " + tables.getString("TABLE_TYPE"));

            }

            System.out.println("-------------VIEWS---------------");
            ResultSet views = data.getTables("add", null, null, new String[] { "VIEW" });
            while (views.next()) {
                System.out.println("Nombre de la vista " + views.getString("TABLE_NAME"));
                System.out.println("Tipo de la tabla " + views.getString("TABLE_TYPE"));
            }

            System.out.println("----------------PROCEDIMIENTOS-------------");
            ResultSet procedures = data.getProcedures("add", null, null);
            while (procedures.next()) {
                System.out.println("Nombre del procedimiento " + procedures.getString("PROCEDURE_NAME"));
                System.out.println("Tipo de procedimiento " + procedures.getString("PROCEDURE_TYPE"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void consultarAlumnosPatron(String patron) {

        String query = "select * from alumnos where nombre like ?";
        int counter = 0;
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, "%" + patron + "%");
            ResultSet resultSet = pst.executeQuery();

            while (resultSet.next()) {
                System.out.println("Nombre del alumno " + resultSet.getString("nombre"));
                counter++;
            }
            System.out.println("Total de alumnos con ese patron  " + counter);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void getColumns() {
        try {
            DatabaseMetaData dbmt = connection.getMetaData();
            int position = 0;
            ResultSet columns = dbmt.getColumns("add", null, null, null);
            while (columns.next()) {

                System.out.println("Posicion de la columna " + position);
                System.out.println("Nombre de la columna " + columns.getString("COLUMN_NAME"));
                System.out.println("Nombre del tipo de dato de la columna" + columns.getString("TYPE_NAME"));
                System.out.println("Tamaño de la columna " + columns.getString("COLUMN_SIZE"));
                System.out.println("Si permite nulos " + columns.getString("NULLABLE"));
                // System.out.println("Tabla " + columns.getMetaData().getTableName(position));
                System.out.println("Si permite AUTOICNREMENTADOS " + columns.getString("IS_AUTOINCREMENT"));
                System.out.println("-------------------------------------------------------");
                position++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Queremos obtener los siguientes datos de las columnas devueltas por la
     * consulta
     * "select *, nombre as non from alumnos": Nombre de la columna, alias de la
     * columna,
     * nombre del tipo de dato usado en la columna, si es autoincrementado y si
     * permite
     * nulos
     */
    public void obtenerDatosConsulta() {

        try (Statement st = connection.createStatement()) {

            ResultSet rs = st.executeQuery("select  nombre as non from alumnos;");
            ResultSetMetaData rsData = rs.getMetaData();
            System.out.println("Nombre de la columna " + rsData.getColumnName(1));
            System.out.println("Alias de la columna " + rsData.getColumnLabel(1));
            System.out.println("Nombre del tipo de dato " + rsData.getColumnType(1));
            System.out.println("Columna autoincrementable : " + rsData.isAutoIncrement(1));
            System.out.println("Columna de nulos permite " + rsData.isNullable(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * Realiza un metodo que permita buscar una cadena de texto en cualquier columna
     * de tiopo cahr o varchar
     * de cualquier tabla de una base de datos dada, dee indicar la base de datos,
     * tabla y columna donde se encontró la coincidencia, y el texto completo del
     * campo
     * 
     * @param args
     */

    public void buscarCadena(String text, String bd) {
        boolean encontrado = false;

        try {
            DatabaseMetaData metaData = connection.getMetaData();

            ResultSet tables = metaData.getTables(bd, null, "%", new String[] { "TABLE" });

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");

                ResultSet columns = metaData.getColumns(bd, null, tableName, "%");
                while (columns.next()) {
                    // Obtenemos el nombre y el tipo de la columna
                    String columnName = columns.getString("COLUMN_NAME");
                    String typeName = columns.getString("TYPE_NAME");

                    // Comprobamos si el tipo es CHAR o VARCHAR (puedes ampliar a otros si lo
                    // necesitas)
                    if (typeName.equalsIgnoreCase("CHAR") || typeName.equalsIgnoreCase("VARCHAR")) {
                        // Construimos una consulta dinámica para buscar la cadena en esta columna.
                        // Notar el uso de comillas invertidas para evitar problemas con nombres
                        // reservados.
                        String query = String.format(
                                "SELECT `%s` FROM `%s`.`%s` WHERE `%s` LIKE '%%%s%%'",
                                columnName, bd, tableName, columnName, text);

                        Statement stmt = connection.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        // Recorremos los resultados
                        while (rs.next()) {
                            String fieldValue = rs.getString(columnName);
                            if (fieldValue != null) {
                                System.out.printf("Database: %s, Table: %s, Column: %s, Value: %s%n",
                                        bd, tableName, columnName, fieldValue);
                                encontrado = true;
                            }
                        }

                        rs.close();
                        stmt.close();
                    }
                }
                columns.close();
            }
            tables.close();

            if (!encontrado) {
                System.out.println("No se encontró la cadena: " + text);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /***
     * 
     * Realiza un
     * método que
     * permita insertar
     * datos en
     * aulas en
     * función de
     * su código*
     * aunque este
     * 
     * ya exista (no se puede usar update)
     * 
     * @param code
     * @param name
     * @param puesto
     */

    public void insertDataSQLite(int code, String name, int puesto) {

        // coomo no puedo usar el update , vamos borrar si encuentra la clave , borramos
        String queryExist = "DELETE FROM aulas where numero = ?  ";
        try (PreparedStatement pst = connectionSQLite.prepareStatement(queryExist)) {
            pst.setInt(1, code);
            int rowAffected = pst.executeUpdate();
            System.out.println("Rows affected " + rowAffected);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String query = "INSERT INTO AULAS (numero, nombreAula, puestos) VALUES (?,?,?)";
        try (PreparedStatement pst = connectionSQLite.prepareStatement(query)) {
            pst.setInt(1, code);
            pst.setString(2, name);
            pst.setInt(3, puesto);

            int rowAffected = pst.executeUpdate();
            System.out.println("Rows affected " + rowAffected);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Realiza inserciones en ambas bases de datos.
     * Si una falla, se deshacen los cambios en ambas.
     * 
     * @throws SQLException
     */
    public void insertarAmbasTablas(String nombreAula, int codigo, int puestos) throws SQLException {
        String query = "INSERT INTO AULAS (numero, nombreAula, puestos) VALUES (?, ?, ?)";

        // Desactivar autocommit en ambas bases de datos para manejar la transacción
        // manualmente
        connection.setAutoCommit(false);
        connectionSQLite.setAutoCommit(false);

        try (PreparedStatement pstMysql = connection.prepareStatement(query);
                PreparedStatement pstSqlite = connectionSQLite.prepareStatement(query)) {

            // Insertar en MariaDB
            pstMysql.setInt(1, codigo);
            pstMysql.setString(2, nombreAula);
            pstMysql.setInt(3, puestos);
            int rowsMysql = pstMysql.executeUpdate();

            // Insertar en SQLite
            pstSqlite.setInt(1, codigo);
            pstSqlite.setString(2, nombreAula);
            pstSqlite.setInt(3, puestos);
            int rowsSqlite = pstSqlite.executeUpdate();

            connection.commit();
            connectionSQLite.commit();

            System.out.println(
                    "Inserciones exitosas. Filas afectadas: MariaDB = " + rowsMysql + ", SQLite = " + rowsSqlite);

        } catch (SQLException e) {
            connection.rollback();
            connectionSQLite.rollback();
            System.err.println("Error en la inserción, se han revertido los cambios: " + e.getMessage());
        } finally {
            connection.setAutoCommit(true);
            connectionSQLite.setAutoCommit(true);
        }
    }

    public static void main(String[] args) {

        Exercises exercises = new Exercises();
        try {
            // exercises.openConnection("add", "localhost", "root", "");
            exercises.openConnectionSQLite("add", "localhost", "root", "");

            // exercises.consultaAlumnos("a");
            // exercises.darAltaAlumno("nicole", "diaz", 161, 21);
            // exercises.darAltaAsignatura(9, "Empresa");
            // exercises.darBajaAlumno(6);
            // exercises.nombreAulasConAlumnos();
            // exercises.nombreAlumnoAprobado();
            // exercises.nombreAlumnoPatronAltura("nicole", 159);
            // exercises.obtencionInfoBD();
            // exercises.consultarAlumnosPatron("n");
            // exercises.getColumns();
            // exercises.obtenerDatosConsulta();
            // exercises.buscarCadena("nicole", "add");
            exercises.insertDataSQLite(11, "Matemáticas aplicadas", 10);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

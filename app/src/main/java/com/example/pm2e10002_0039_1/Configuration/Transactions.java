package com.example.pm2e10002_0039_1.Configuration;

public class Transactions {

    public static final String NameDataBase = "PM01EXAMEN1";

    public static final String TablaPaises = "PAISES";

    public static final String id = "id";
    public static final String nombre = "nombre";
    public static final String codigo = "codigo";

    public static final String CreateTablePaises = "CREATE TABLE PAISES "+
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, codigo INTEGER)";

    //public static final String InsertPaises = "INSERT INTO  PAISES(nombre, codigo) VALUES (HONDURAS, 504)";
    //public static final String InsertPaises = "INSERT INTO  PAISES(nombre, codigo) VALUES (HONDURAS, 504), (EL SALVADOR, 503), (COSTA RICA, 506), (GUATEMALA, 502);";

    public static final String DropTablePaises = "DROP TABLE IF EXIST PAISES";

    public static final String SelectTablePaises = "SELECT * FROM "+TablaPaises;


    public static final String TablaContactos = "CONTACTOS";

    public static final String idC = "id";
    public static final String nombreC = "nombre";
    public static final String telefonoC = "telefono";
    public static final String paisC = "pais";
    public static final String imagenC = "imagen";
    public static final String notaC = "nota";


    public static final String CreateTableContactos = "CREATE TABLE CONTACTOS "+
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, telefono INTEGER, pais TEXT, imagen BLOB, nota TEXT)";


    public static final String DropTableContactos = "DROP TABLE IF EXIST CONTACTOS";

    public static final String SelectTableContactos = "SELECT c.id, c.nombre, c.telefono, c.nota, c.pais, c.imagen FROM "+TablaContactos+" c";





}

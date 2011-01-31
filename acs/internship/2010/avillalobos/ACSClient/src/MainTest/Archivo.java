package MainTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Clase archivo creada por mi, lee y escribe un archivo
 * Al momento de escribir no lo borra escribe luego de lo que ya tiene escrito jajaj es inteligente =)
 * @author andrew
 */
public class Archivo {

    /**
     * @param archivo Variable que almacena el archivo para ser declarado como lectura o escritura
     */
    private RandomAccessFile archivo;
    /**
     * @param file Archivo usado para leer datos desde un archivo
     */
    private File file;
    /**
     * @param lineaSiguiente Al declararse el archivo como Lectura, se lee inmediatamente
     * la primera linea, para que cuando se acaben las lineas, se puede marcar el archivo
     * como fin de archivo. Con este funcionamiento, puedo preguntar directamente a archivo
     * si es o no fin de archivo y no esperar a leer la linea para darme cuenta que es null,
     * si me situo en ese caso, entonces no podria marcar el flag de fin de archivo porque
     * estaria sucediendo en el mismo instante, y dentro del ciclo que lee el archivo,
     * por lo tanto estaria leyendo un null que no es lo que necesito. Esto esta hecho
     * basicamente para poder leer en el mismo ciclo while o for
     */
    private String lineaSiguiente;
    /**
     * @param FinArchivo flag utilizado para marcar si el archivo se acabó o no
     */
    private boolean FinArchivo;
    /**
     * @param Lectura flag utilizado para marcar si el archivo está declarado como lectura
     */
    private boolean Lectura;
    /**
     * @param Escritura flag utilizado para marcar si el archivo está declarado como lectura
     */
    private boolean Escritura;
    /**
     * @param Largo indica el largo del archivo en lineas
     */
    private int Largo;
    private String path;

    /**
     * Constructor de la clase Archivo
     * @param ruta dirección del archivo que se va a leer
     * @exception NullPointerException Si la ruta indicada no contiene un archivo
     */
    public Archivo(String ruta) throws NullPointerException {
        try {
            this.file = new File(ruta);

        } catch (NullPointerException npe) {
            throw new NullPointerException("La ruta indicada no contiene el archivo especificado \n " + npe.getMessage());
        }
        this.path = ruta;
        this.FinArchivo = false;
        this.Lectura = false;
        this.Escritura = false;
        this.lineaSiguiente = "";
        this.Largo = 0;
    }

    /**
     * Constructor alternativo de la clase archivo que no necesita ruta, pero
     * por esto el archivo no es accesible por lo tanto ningún método funcionará
     * hasta que se le agregue una ruta de archivo para el correcto funcionamiento del archivo
     */
    public Archivo() {
        this.Reset();
    }

    /**
     * Método que declara el archivo como de lectura
     * @return <li> true si se pudo declarar como lectura</li>
     *         <li> false si el archivo no puede ser declarado como lectura, probablemente nunca retorne false, porque antes de retornar false, lanzará la excepción correspondiente </li>
     * @throws <li> Exception Archivo, error de tipo, archivo declarado como de lectura </li>
     *         <li> Exception Archivo, error de tipo, este archivo ya ha sido modificado como escritura </li>
     *         <li> Exception Archivo, error de la clase en cuanto a la lógica, la clase archivo debe ser modificada si este error es lanzado </li>
     *         <li> FileNotFoundException si el archivo no ha podido ser encontrado </li>
     *         <li> IOException si ocurrio un problema tanto en la entrada como en la salida de datos</li>
     */
    public boolean Lectura() throws Exception {
        if (this.file != null && this.file.exists()) {
            try {
                if (!this.Lectura && !this.Escritura) {
                    this.Lectura = true;
                    archivo = new RandomAccessFile(file, "r");
                    this.lineaSiguiente = archivo.readLine();
                    this.Lectura = true;
                    return true;
                } else {
                    if (this.Lectura && !this.Escritura) {
                        throw new Exception("Este archivo ya ha sido modificado como lectura!");
                    } else if (!this.Lectura && this.Escritura) {
                        throw new Exception("Este archivo ya ha sido modificado como escritura!");
                    } else {
                        throw new Exception("Error de la clase archivo!, probablemente porque ha reiniciado el archivo y no seteo las propiedades correctamente");
                    }
                }
            } catch (FileNotFoundException e) {
                throw new Exception("Este archivo no ha sido encontrado!, error: \n" + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception("Hubo un problema en la entrada o salida de datos!, error: \n" + e.getMessage());
            }
        } else {
            return false;
        }
    }

    /**
     * Método que declara el archivo solo de escritura
     * @return <li> true si el archivo puede ser declarado como lectura </li>
     *         <li> false si el archivo no puede ser declarado como lectura, probablemente nunca retorne false, porque antes de retornar false, lanzará la excepción correspondiente </li>
     * @throws <li> Exception Archivo, error de tipo, archivo declarado como de lectura </li>
     *         <li> Exception Archivo, error de tipo, este archivo ya ha sido modificado como escritura </li>
     *         <li> Exception Archivo, error de la clase en cuanto a la lógica, la clase archivo debe ser modificada si este error es lanzado </li>
     *         <li> FileNotFoundException si el archivo no ha podido ser encontrado </li>
     *         <li> IOException si ocurrio un problema tanto en la entrada como en la salida de datos</li>
     */
    public boolean Escritura() throws Exception {
        if (this.file != null && !this.file.exists()) {
            throw new FileNotFoundException("El archivo ha sido removido de su ubicación original o el archivo no ha sido creado mediante alguna ruta especifica, por lo tanto actualemnte el archivo no es accesible, regrese el archivo al destino inicial o cree un nuevo archivo");
        } else {
            try {
                if (!this.Lectura && !this.Escritura) {
                    this.Escritura = true;
                    this.archivo = new RandomAccessFile(file, "rw");
                } else {
                    if (this.Lectura && !this.Escritura) {
                        throw new Exception("Este archivo ya ha sido modificado como lectura!");
                    } else if (!this.Lectura && this.Escritura) {
                        throw new Exception("Este archivo ya ha sido modificado como escritura!");
                    } else {
                        throw new Exception("Error de la clase archivo!, probablemente porque ha reiniciado el archivo y no seteo las propiedades correctamente");
                    }
                }
                return true;
            } catch (FileNotFoundException e) {
                throw new Exception("Este archivo no ha sido encontrado!, error: \n" + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                throw new Exception("Hubo un problema en la entrada o salida de datos!, error: \n" + e.getMessage());
            }
        }
    }

    /**
     * Método que cierra el archivo para que otro programa lo pueda usar
     *
     * @return <li>true si el archivo pudo ser cerrado </li>
     *         <li>false si el archivo no pudo ser cerrado </li>
     * @exception IOException, Exception asociadas a que si el archivo fue encontrado o si ocurrio un erro en la clase archivo
     */
    public boolean Close() throws IOException, Exception {
        if (this.file != null && !this.file.exists()) {
            throw new FileNotFoundException("El archivo ha sido removido de su ubicación original o el archivo no ha sido creado mediante alguna ruta especifica, por lo tanto actualemnte el archivo no es accesible, regrese el archivo al destino inicial o cree un nuevo archivo");
        }
        // archivo nunca fue declarado como lectura o escritura de datos
        if (!this.Escritura && !this.Escritura) {
            return false;
        } else if ((this.Lectura && !this.Escritura) || (!this.Lectura && this.Escritura)) {
            try {
                this.archivo.close();
                return true;
            } catch (IOException e) {
                throw e;
            }
        } else {
            throw new Exception("Error de la clase archivo!, probablemente porque ha reiniciado el archivo y no seteo las propiedades correctamente");
        }
    }

    /**
     * Método que cierra el archivo para que otro programa lo pueda usar, Si el random access file esta en nulo
     * no es necesario cerrarlo porque nunca fue abierto, y en ese sentido para este método es algo bueno
     * porque se supone que se cierra para no mantenerlo vinculado y si nunca fue vinculado mucho mejor
     *
     * @param flag Indica si se debe forzar o no el cierre del archivo
     * @return <li>true si el archivo pudo ser cerrado </li>
     *         <li>false si el archivo no pudo ser cerrado </li>
     * @exception IOException, Exception asociadas a que si el archivo fue encontrado o si ocurrio un erro en la clase archivo
     */
    public boolean Close(boolean flag) throws IOException, Exception {
        if (this.file != null && !this.file.exists()) {
            throw new FileNotFoundException("El archivo ha sido removido de su ubicación original o el archivo no ha sido creado mediante alguna ruta especifica, por lo tanto actualemnte el archivo no es accesible, regrese el archivo al destino inicial o cree un nuevo archivo");
        }
        // archivo nunca fue declarado como lectura o escritura de datos y si no se va a forzar el cierre del archivo
        if (!flag && (!this.Escritura && !this.Escritura)) {
            return false;
            // si el archivo se va a forzar para cerrar y fue declarado como lectura o como escritura, entonces se debe cerrar
        } else if (flag || ((this.Lectura && !this.Escritura) || (!this.Lectura && this.Escritura))) {
            if (this.archivo != null) {
                try {
                    this.archivo.close();
                    return true;
                } catch (IOException e) {
                    throw e;
                }
            } else {
                return true;
            }
        } else {
            throw new Exception("Error de la clase archivo!, probablemente porque ha reiniciado el archivo y no seteo las propiedades correctamente");
        }
    }

    /**
     * Método que lee una linea del archivo y la devuelve como String
     *
     * @return Una linea del archivo, si el archivo es fin de archivo entonces retorna null, pero no se llega a este punto si se usa
     * la variable fin de archivo para indicar cuando se terminó de leer el archivo
     * @exception Exception Si el archivo no ha sido declarado como de lectura
     * @exception IOException Si ocurrio un error en el flujo de datos entre el archivo
     */
    public String Leer() throws Exception {
        if (this.file == null || !this.file.exists()) {
            throw new FileNotFoundException("El archivo ha sido removido de su ubicación original o no ha sido creado mediante una ruta especifica, por lo tanto actualemnte el archivo no es accesible, regrese el archivo al destino inicial o cree un nuevo archivo");
        } else {
            try {
                if (!this.FinArchivo && this.Lectura) {
                    // se guarda la linea anterior, porque será esta la que se retornará
                    String temp = this.lineaSiguiente;
                    // se lee la siguiente linea
                    this.lineaSiguiente = this.archivo.readLine();
                    if (this.lineaSiguiente == null) {
                        this.FinArchivo = true;
                    }
                    return temp;
                } else if (!this.Lectura) {
                    throw new Exception("Este archivo no ha sido declarado como lectura");
                } else {
                    return null;
                }
            } catch (IOException e) {
                throw e;
            }
        }
    }

    /**
     * Método que lee una linea del archivo y la devuelve como un vector estático de String
     *
     * @return Una linea del archivo separada de acuerdo al tokeneizer en un vector estatico de String,
     * si el archivo es fin de archivo entonces retorna null, pero no se llega a este punto si se usa
     * la variable fin de archivo para indicar cuando se terminó de leer el archivo
     * @exception Exception Si el archivo no ha sido declarado como de lectura
     * @exception IOException Si ocurrio un error en el flujo de datos entre el archivo
     */
    public String[] Leer(String tokeneizer) throws Exception {
        if (this.file == null || !this.file.exists()) {
            throw new FileNotFoundException("El archivo ha sido removido de su ubicación original o no ha sido creado mediante una ruta especifica, por lo tanto actualemnte el archivo no es accesible, regrese el archivo al destino inicial o cree un nuevo archivo");
        } else {
            //StringTokenizer st = new StringTokenizer(tokeneizer);
            try {
                if (!this.FinArchivo && this.Lectura) {
                    // se guarda la linea anterior, porque será esta la que se retornará
                    String temp = this.lineaSiguiente;
                    // se lee la siguiente linea
                    this.lineaSiguiente = this.archivo.readLine();
                    if (this.lineaSiguiente == null) {
                        this.FinArchivo = true;
                    }
                    return temp.split(tokeneizer);
                } else if (!this.Lectura) {
                    throw new Exception("Este archivo no ha sido declarado como lectura");
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * Método que lee el archivo Completo y retorna una LinkedList de vectores estáticos de String de acuerdo al tokeneizer enviado
     *
     * @return una lista con nexo de tipo LinkedList que contiene un vector estático de String que se genera separando cada linea
     * del archivo leida en el tokeneizer enviado por parametro y de acuerdo a ese tokeneizer se forma una vector de string que se
     * guarda en la lista con nexo retornada.
     * @exception Exception Si el archivo no ha sido declarado como de lectura
     * @exception IOException Si ocurrio un error en el flujo de datos entre el archivo
     */
    public LinkedList<String[]> LeerCompleto(String tokeneizer) throws FileNotFoundException, IOException, Exception {
        if (this.file == null || !this.file.exists()) {
            throw new FileNotFoundException("El archivo ha sido removido de su ubicación original o no ha sido creado mediante una ruta especifica, por lo tanto actualemnte el archivo no es accesible, regrese el archivo al destino inicial o cree un nuevo archivo");
        } else {
            this.Reset();
            if (this.Close(true) && this.Lectura()) {
                LinkedList<String[]> Lista = new LinkedList<String[]>();
                while (!this.FinArchivo) {
                    this.Largo++;
                    Lista.add(this.Leer(tokeneizer));
                }
            }
        }
        return null;
    }

    /**
     * Método que lee el archivo Completo y retorna una LinkedList de lineas String
     *
     * @return una lista con nexo de tipo LinkedList que contiene una lista de lineas de tipo string
     * @exception Exception Si el archivo no ha sido declarado como de lectura
     * @exception IOException Si ocurrio un error en el flujo de datos entre el archivo
     */
    public LinkedList<String> LeerCompleto() throws FileNotFoundException, IOException, Exception {
        if (this.file == null || !this.file.exists()) {
            throw new FileNotFoundException("El archivo ha sido removido de su ubicación original o no ha sido creado mediante una ruta especifica, por lo tanto actualemnte el archivo no es accesible, regrese el archivo al destino inicial o cree un nuevo archivo");
        } else {
            this.Reset();
            if (this.Close(true) && this.Lectura()) {
                LinkedList<String> Lista = new LinkedList<String>();
                while (!this.FinArchivo) {
                    this.Largo++;
                    Lista.add(this.Leer());
                }
                return Lista;
            } else {
                return null;
            }
        }
    }

    /**
     * Método que Escribe una linea de tipo String en el archivo
     *
     * @param linea Linea a escribir en el archivo
     * @exception FileNotFoundException Si el archjivo a sido movido del lugar donde se encontraba cuando se
     * instancio la clase
     * @exception Exception Si el archivo fue declarado como lectura o simplemente no fue declarado como algun tipo
     * valido de archivo para ser usado
     * @exception IOException Si el ocurrio un error en el flujo de datos entre el archivo
     */
    public void Escribir(String linea) throws IOException, FileNotFoundException, Exception {
        if (this.file == null || !this.file.exists()) {
            throw new FileNotFoundException("El archivo ha sido removido de su ubicación original o no ha sido creado mediante una ruta especifica, por lo tanto actualemnte el archivo no es accesible, regrese el archivo al destino inicial o cree un nuevo archivo");
        } else {
            try {
                if (this.Escritura && !this.Lectura) {
                    this.archivo.writeBytes(linea + "\n");
                } else {
                    throw new Exception("Este archivo no ha sido declarado como escritura o no tiene las propiedas correctas para ser leido, probablemente porque ha reiniciado el archivo");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método que Escribe una linea de tipo int en el archivo
     *
     * @param valor valor a escribir en el archivo de tipo int
     * @exception FileNotFoundException Si el archjivo a sido movido del lugar donde se encontraba cuando se
     * instancio la clase
     * @exception Exception Si el archivo fue declarado como lectura o simplemente no fue declarado como algun tipo
     * valido de archivo para ser usado
     * @exception IOException Si el ocurrio un error en el flujo de datos entre el archivo
     */
    public void Escribir(int valor) throws Exception {
        if (this.file == null || !this.file.exists()) {
            throw new FileNotFoundException("El archivo ha sido removido de su ubicación original o no ha sido creado mediante una ruta especifica, por lo tanto actualemnte el archivo no es accesible, regrese el archivo al destino inicial o cree un nuevo archivo");
        } else {
            try {
                if (this.Escritura && !this.Lectura) {
                    this.archivo.write(valor);
                    this.archivo.writeBytes("\n");
                } else {
                    throw new Exception("Este archivo no ha sido declarado como escritura o no tiene las propiedas correctas para ser leido, probablemente porque ha reiniciado el archivo");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método que Escribe una linea de tipo double en el archivo
     *
     * @param valor Linea a escribir en el archivo de tipo double
     * @exception FileNotFoundException Si el archjivo a sido movido del lugar donde se encontraba cuando se
     * instancio la clase
     * @exception Exception Si el archivo fue declarado como lectura o simplemente no fue declarado como algun tipo
     * valido de archivo para ser usado
     * @exception IOException Si el ocurrio un error en el flujo de datos entre el archivo
     */
    public void Escribir(double valor) throws Exception {
        if (this.file == null || !this.file.exists()) {
            throw new FileNotFoundException("El archivo ha sido removido de su ubicación original o no ha sido creado mediante una ruta especifica, por lo tanto actualemnte el archivo no es accesible, regrese el archivo al destino inicial o cree un nuevo archivo");
        } else {
            try {
                if (this.Escritura && !this.Lectura) {
                    this.archivo.writeDouble(valor);
                    this.archivo.writeBytes("\n");
                } else {
                    throw new Exception("Este archivo no ha sido declarado como escritura o no tiene las propiedas correctas para ser leido, probablemente porque ha reiniciado el archivo");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Método que Escribe una lista de lineas de tipo string en el archivo
     *
     * @param Lista Una lista que contiene lineas de tipo string a escribir en el archivo
     * @exception FileNotFoundException Si el archjivo a sido movido del lugar donde se encontraba cuando se
     * instancio la clase
     * @exception Exception Si el archivo fue declarado como lectura o simplemente no fue declarado como algun tipo
     * valido de archivo para ser usado
     * @exception IOException Si el ocurrio un error en el flujo de datos entre el archivo
     */
    public void Escribir(LinkedList<String> Lista) throws FileNotFoundException, IOException, Exception {
        if (this.file == null || !this.file.exists()) {
            throw new FileNotFoundException("El archivo ha sido removido de su ubicación original o no ha sido creado mediante una ruta especifica, por lo tanto actualemnte el archivo no es accesible, regrese el archivo al destino inicial o cree un nuevo archivo");
        } else {
            // me aseguro de resetear el buffer para poder escribir desde el principio, en cierta manera es como si se instanciara
            // el archivo nuevamente
            this.Reset();
            // si el archivo se puede cerrar y declarar como lectura entonces se escribe
            if (this.Close(true) && this.Escritura()) {
                Iterator<String> i = Lista.iterator();
                while (i.hasNext()) {
                    this.Escribir(i.next());
                }
            } else {
                throw new Exception("El archivo no pudo ser reiniciado o bien no pudo ser decladaro como de Escritura");
            }
        }
    }

    /**
     * Método que pregunta si se terminó de leer el archivo o no
     * 
     * @return <li>true: Si el archivo se ha leido por completo</li>
     *         <li>false: Si el archivo aun no ha sido leido en su totalidad</li>
     */
    public boolean FinArchivo() {
        return FinArchivo;
    }

    /**
     * Método que pregunta al archivo si esta marcado como Escritura
     * 
     * @return <li>true: Si esta marcado para ser escrito</li>
     *         <li>false: Si no esta marcado para ser escrito</li>
     */
    public boolean ArchivoDeEscritura() {
        return Escritura;
    }

    /**
     * Método que pregunta al archivo si esta marcado como Lectura
     * 
     * @return <li>true: Si esta marcado para ser leido</li>
     *         <li>false: Si no esta marcado para ser leido</li>
     */
    public boolean ArchivoDeLectura() {
        return Lectura;
    }

    /**
     * ¡¡ADVERTENCIA, EN ARCHIVOS MUY LARGOS ESTE MÉTODO SERÁ COSTOSO PORQUE LEE EL ARCHIVO COMPLETO PARA SABER CUANTAS LINEAS TIENE!!
     *
     * Método que obtiene la cantidad de lineas que posee el archivo solo si este
     * ha sido declarado como lectura.
     *
     * @return Largo retorna el valor del largo si el archivo ha sido declarado como de lectura,
     * en cualquier otro caso retornaŕa -1 para indicar que no se puede utilizar esta opción
     */
    public int getLargo() {
        if (this.Largo == 0) {
            try {
                LargoArchivo();
            } catch (FileNotFoundException ex) {
                return -1;
            } catch (IOException ex) {
                return -1;
            }
        }
        if (this.Lectura && !this.Escritura) {
            return this.Largo;
        } else {
            return -1;
        }
    }

    /**
     * ¡¡ADVERTENCIA, EN ARCHIVOS MUY LARGOS ESTE MÉTODO SERÁ COSTOSO PORQUE LEE EL ARCHIVO COMPLETO PARA SABER CUANTAS LINEAS TIENE!!
     *
     * Método que lee el archivo para contar las lineas contenidas en él
     *
     * @return Largo cantidad de lineas contenidas en el archivo
     * @exception FileNotFoundException si el archivo no es encontrado
     * @exception IOException si ocurrio un error al tratar de leer el archivo en cualquier parte!
     */
    private int LargoArchivo() throws FileNotFoundException, IOException {
        try {
            if (this.Largo == 0) {
                RandomAccessFile raf = new RandomAccessFile(file, "r");
                String linea = raf.readLine();
                while (linea != null) {
                    this.Largo++;
                    linea = raf.readLine();
                }
            } else {
                return this.Largo;
            }
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException("Archivo no ha sido encontrado, porfavor ubicar el archivo " + this.file.getName() + " para que el programa pueda funcionar con normalidad, error : \n" + ex.getMessage());
        } catch (IOException ioe) {
            throw new IOException("No se ha podido leer el archivo, porbablemnte porque el archivo no se ha encontrado, porfavor ubicar el archivo " + this.file.getName() + " para que el programa pueda funcionar con normalidad, error : \n" + ioe.getMessage());
        }
        return 0;
    }

    /**
     * Este método es arbitrario y reinicia el archivo dejandolo como en el constructor, no esta declarado
     * como lectura ni como escritura, por lo tanto no se podrá leer con el a menos que las propiedas
     * sea seteadas nuevamente
     */
    public void Reset() {
        this.FinArchivo = false;
        this.Lectura = false;
        this.Escritura = false;
        this.lineaSiguiente = "";
        this.Largo = 0;
    }

    /**
     * Obitiene la ruta que corresponde al archivo que se esta leyendo actualmente por la clase archivo
     *
     * @return La ruta del archivo actualmente accesible por la clase archivo
     */
    public String getPath() {
        return path;
    }

    /**
     * Modifica la ruta del archivo a leer, con esto es posible utilizar una instancia de la clase archivo
     * para leer muchos archivos disintos solo cambiando la ruta a leer
     *
     * @param ruta del archivo a leer
     * @return <li>true: Si la ruta era correcta y efectivamente contenia un archivo </li>
     *         <li>false: Si la ruta no era correcta o la ruta no contenia un archivo </li>
     * @exception NullPointerException Si la ruta indicada no contiene un archivo
     */
    public boolean setPath(String path)throws NullPointerException {
        try {
            this.file = new File(path);

        } catch (NullPointerException npe) {
            throw new NullPointerException("La ruta indicada no contiene el archivo especificado \n " + npe.getMessage());
        }
        if (this.file.exists()) {
            this.path = path;
            this.Reset();
            return true;
        } else {
            return false;
        }
    }
}

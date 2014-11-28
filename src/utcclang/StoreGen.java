package utcclang;

/**
 * @author Misael Viveros Castro
 * clase Abstract para implementar el store de  Utcc.
 * S -Tipo del Store
 * C -Tipo de Constraint
 * I -Tipo de enteros a manejar
 * @version {@value}
 */


import utccEnums.Vars;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class  StoreGen<S,C,I> {
    /*
     * filename
     * Nombre del archivo para grabar el valor de las variables despues de cada unidad de tiempo.
     */
	private static String filename = "StoreFile.txt";
    /*
     * sConsistency
     * Registra la consistencia en el Store.
     */
    private static volatile AtomicBoolean sConsistency = new AtomicBoolean(true);

    public static Boolean getsConsistency() {
        return sConsistency.get();
    }

    public static void setsConsistency(Boolean sConsistency) {
        StoreGen.sConsistency.set(sConsistency);
    }

    /**
    *  reifiVars, es una lista de variables utilizadas para la construir reificadas 
    *  o variables de verificacion de constraint con rango de [0..1]
    */
	private volatile static LinkedList<Object> reifiVars =  new  LinkedList<Object>();
	
	// En la implementacion debe creacer  la variable que implemente el store el cual debe ser estatico
	// por ejemplo --> private volatile static CPModel modelStore = new CPModel()
	// 
	
    /**
     * Crear varibles a partir de la clase Ambient.Vars.
     * @param vars --> Arreglo Ambient.Vars
     */
	public void creavars(List<Vars> vars ) {
	 	for (Vars v :vars ) {
	 		switch  (v.vtype) {
	 			case '1': this.creavars(v.varname); break;
		 		case '2': this.creavars(v.varname, v.vinf, v.vsup); break;
		 		case '3': this.creavars(v.varname, v.values ); break;
	 		}
	 		 
	 	}
		
	}
	
	
	/**
	  * Crear una variable en el store con valores MAX y MIN por defecto.
	  * @param name --> String nombre de la varible
	  */
	public abstract void creavars(final String name, final int[] values);
	
	/**
	  * Crear una variable en el store con valores MAX y MIN por defecto.
	  * @param name --> String nombre de la varible
	  */
	public abstract void creavars(final String name);
		
        /**
         *  Crear una variable en el store con valores valinf, valsup.
         * @param name --> String nombre de la varible
         * @param valinf --> Valor Inferior
         * @param valsup --> Valor Superior
         */
	public abstract void creavars(final String name, final int valinf, final int valsup);
	
	 /**
	   * Crear una variable en el store con valores MAX y MIN por defecto.
	   * @param name --> String nombre de la varible
	   * @return I -->  Retornando el tipo de la varible creada en el Store. 
	   */
	public abstract I creavarsRetor(final String name);
	
       /**
         * Crear de 1 a n variables en el store.
         * @param name --> String nombre de la varible
         * @param valinf --> Valor Inferior
         * @param valsup --> Valor Superior
         * @return I -->  Retornando el tipo de la varible creada en el Store.
         */
	public abstract I creavarsRetor(final String name,final int valinf, final int valsup);

    /**
     * Crear de 1 a n variables en el store.
     * @param name --> String nombre de la varible
     * @param values --> Arreglo int Valores de la variable
     * @return I -->  Retornando el tipo de la varible creada en el Store.
     */
    public abstract I creavarsRetor(final String name, int... values);
	
        /**
         * Agrega una variable al store.
         * @param vars --> de tipo I vars
         */
	public abstract void addvars(I vars); 
	
        /**
         * Agregar un Constraint al Store.
         * @param cons --> Constraint de tipo C
         */
	//public abstract void addConstra(C cons);

    //public abstract void addConstra(C cons, int lockNum);
	
	/**
	 * Metodo que determina si el Store es Consistente.
	 * @return Boolean 
	 */
	
	public abstract Boolean isConsistent();
		
	/**
	  * Metodo que Valida un constraint en el Store
	  * @param cons --> Constraint de tipo C
	  * @return Boolean --> 1 true , 0 false
	  */
	//public abstract Boolean validateCons(C cons);
	
     /**
      * Escribe en un Archivo Texto las varibles del Store.
     * @throws IOException 
      *
      */
	///public abstract void writerStore() throws IOException;
   
	/**
	 * @return the filename
	 */
	public final static String getFilename() {
		return filename;
	}


	/**
	 * @param filename the filename to set
	 */
	public final static void setFilename(String filename) {
		StoreGen.filename = filename;
	}


	/**
     * Imprime las varibles del store.
     *
     */
	//public  abstract void prinVars();
	
	/**
     * Asignar el modelo del store.
     * @param stStore --> Tipo S - the model Store to set
     */
	//public abstract void setModelStore(S stStore);


    /**
	 * Release Store
	 * Borrar el modelo del store.
	 */
	//public abstract void deleteModelStore();

		


        /**
         * @param reifiCons
         */
        @SuppressWarnings("unchecked")
		public void setVarsReifi(LinkedList<I> reifiCons) {
        	StoreGen.reifiVars = (LinkedList<Object>) reifiCons;
	}


        /**
         * @return reifiCons2 --> como un "LinkedList[I]"
         */
        @SuppressWarnings("unchecked")
        public LinkedList<I> getVarsReifi() {
	    
        	LinkedList<I> reifiCons2 = (LinkedList<I>) reifiVars;
            return reifiCons2;
	}
	
	
}
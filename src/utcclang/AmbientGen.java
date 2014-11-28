
package utcclang;
import utccEnums.Vars;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;




/**
 * Clase que implementa la Creacion del entorno de Ntcc
 * @author  Misael Viveros Castro
 *
 * @param <S> --> Store model
 * @param <C> --> Constraint
 */

public abstract class AmbientGen<S,C> {
	private volatile S storetmp;
    private static volatile ArrayList<AmbientC> varsEnviromen = new ArrayList<AmbientC>();
    private volatile static List<Vars>  varutcc = Collections.synchronizedList(new ArrayList<Vars>());

    
	/**
	 * Agregar una una variable del entorno 
	 * @param name  --> String the varutcc to set
	 */
	public static void addVarutcc(final String name) {
		Vars var = new Vars();
		var.varname = name;var.vinf = Integer.MIN_VALUE/2;var.vsup = Integer.MAX_VALUE/2;var.vtype = '1';
		synchronized (AmbientGen.getArrayutcc()) {
			AmbientGen.getArrayutcc().add(var);
		}
	}
    
    /**
	 * Agregar  una variable del entorno
	 * @param name --> String the varutcc to set
	 * @param valini --> Valor inferior
	 * @param valfin --> Valor superior
	 */
	public static void addVarutcc(final String name, int valini, int valfin) {
		Vars var = new Vars();
		var.varname = name;var.vinf = valini;var.vsup = valfin;var.vtype = '2';
		synchronized (AmbientGen.getArrayutcc()) {
			AmbientGen.getArrayutcc().add(var);
		}
	}
    
    /**
	 * Agregar una una variable del entorno
	 * @param name --> String the varutcc to set
	 * @param values --> int[] Rango de valores enteros
	 */
	public static void addVarutcc(final String name, int...values) {
		Vars var = new Vars();
		var.varname = name;var.values = values;var.vtype = '3';
		synchronized (AmbientGen.getArrayutcc()) {
			AmbientGen.getArrayutcc().add(var);
		}
		
	}
    
    /**
     * Metodo para Retorna la lista de variables del entorno
	 * @return the varutcc
	 */
	public static List<Vars> getArrayutcc() {
		return AmbientGen.varutcc;
		
	}
	
	/**
	 * Metodo para Buscar una variable en el entorno
	 * @param var - String
	 * @return found --> boolean
	 */
	public boolean findVars(String var) {
		 boolean found = false;
		for (Vars vars: getArrayutcc() ){
			if 	(vars.varname.equals(var)) {
				found = true;
			}
		}
		return found;
	}
	
	/**
	 * Metodo para Buscar una variable en el entorno y retornar su contenido
	 * @param var - String
	 * @return Vars -->[] Arreglo de type Vars
	 */
	public Vars getValue(String var) {
		Vars varsvalue = new Vars();
		synchronized (AmbientGen.getArrayutcc()) {
			for (Vars varis: AmbientGen.getArrayutcc() ){
				if 	(varis.varname.equals(var)) {
					varsvalue = varis;
					break;
				}
			}
			return varsvalue;
		}
	}

    /**
     * Metodo para verificar el numero de variables definidas en el ambiente
     * para luego proceder a realizar su verificación en el entorno (Verificar que esten definidas).
     * @param var - AmbientC Class     ->> Esta definidad para la estrutura de Jacop constraint
     * @return nfound --> int numero de variables definidas maximo 3 Aplicable a Jacop Constraint
     */
    public int nvarDef(AmbientC var) {
        int nfound = 0;
            if 	(!var.getVarname1().equals(null)) {nfound++;}
            if 	(!var.getVarname2().equals(null)) {nfound++;}
            if 	(!var.getVarname3().equals(null)) {nfound++;}

        return nfound;
    }


    /**
     *  Permite agregar constraint de iniciacion a una variable.
     *  Los constraint de iniciacion se ejecutan antes de la ejecucion de los procesos
     *  @param  var  ->> String
     *  @param cons  --> C Constraint
     */
    public abstract void addIniC(String var,C cons);

    /**
     *  Permite agregar constraint de iniciacion a una variable.
     *  Los constraint de iniciacion se ejecutan antes de la ejecucion de los procesos
     *
     */
    public abstract void addStoreConstIni();

	/**
	 * oldValueVar() Metodo que retorna el valor anterior (oldest) de una variable.
	 * @param var --> String
	 * @return --> int
	 */
	public  int oldValueVar(String var) {
		int valorvar = 0;
		Vars valueVars = this.getValue(var);
		synchronized (this) {
			if (valueVars.vtype == '3') {
				valorvar = valueVars.values[0];
			} else {
				valorvar = valueVars.vinf;
			}
		}	
			return valorvar;
		
	}
	
	/**
	 * ChangeValueVar() Metodo que permite cambiar el valor de una variable
	 * el resultado se refleja en la siguiente unidad de Tiempo
	 * @param var1 --> String
	 * @param var2 --> String
	 */
		public  void ChangeValueVar(String var1, String var2) {
				Vars valvars2 = new Vars(); valvars2 =  this.getValue(var2);
				for (Vars vars: AmbientGen.getArrayutcc() ){
					if 	(vars.varname.equals(var1)) {
						synchronized (AmbientGen.getArrayutcc()) {
							vars.values = valvars2.values ;
							vars.vinf = valvars2.vinf ;
							vars.vsup = valvars2.vsup ;
							vars.vtype = valvars2.vtype;
							break;
						}
					}
				}
		}
	/**
	 * ChangeValueVar() Metodo que permite cambiar el valor de una variable
	 * el resultado se refleja en la siguiente unidad de Tiempo
	 * @param var1 --> String
	 * @param var2 --> []int
	 */
		public void ChangeValueVar(String var1, int... var2) {
			
				for (Vars vars: AmbientGen.getArrayutcc() ){
					if 	(vars.varname.equals(var1)) {
						synchronized (AmbientGen.getArrayutcc()) {
							vars.values = var2 ;
							vars.vinf = 0 ;
							vars.vsup = 0 ;
							vars.vtype = '3';
							break;
						}
					}
				}
		}
	/**
	 * ChangeValueVar() Metodo que permite cambiar el valor de una variable
	 * el resultado se refleja en la siguiente unidad de Tiempo
	 * @param var1 --> String
	 * @param var2 --> int
	 */
		public void ChangeValueVar(String var1, int var2) {
			
				for (Vars vars: AmbientGen.getArrayutcc() ){
					if 	(vars.varname.equals(var1)) {
						synchronized (AmbientGen.getArrayutcc()) {
							//System.out.println("Valor de Cambio "+ var2);
							//System.out.println("Valor de "+vars.varname + " es "+ vars.vinf);
							vars.vinf = var2 ;
							vars.vsup = var2 ;
							vars.vtype = '2';
							break;
						}
					}
				}
		}
	//**************************************************** Constraint
	
	/**
	 * Metodo que permite cargar los Constraint  del ambiente al store
	 * @param timeUnit - int -> unidad de tiempo para cargar los constraint
	 */
	public  abstract void addStore(int timeUnit);
	
	/**
	 * Metodo que retorna la lista de Constraint del Ambiente
	 * @return the varsEnviromen
	 */
	public static ArrayList<AmbientC> getVarsEnviromen() {
		return varsEnviromen;
	}

	/**
	 * @return the storetmp
	 */
	public S getStoretmp() {
		return storetmp;
	}

	/**
	 * @param storetmp the storetmp to set
	 */
	public void setStoretmp(S storetmp) {
		this.storetmp = storetmp;
	}

	/**
	 * Metodo para asignar las variables al ambiente
	 * @param varsEnviromen the varsEnviromen to set
	 */
	public static void setVarsEnviromen(ArrayList<AmbientC> varsEnviromen) {
		AmbientGen.varsEnviromen = varsEnviromen;
		
	}
    
	/**
	 * Metodo para agregar un constraint  al entorno
	 * @param clasname -Nombbre de la Clase que identifica el Contraint
	 * @param parvarn1 -String Parametro del Constraint 
	 * @param parvarn2 -String Parametro del Constraint
	 * @param unit	- Int Unidad de Tiempo en la que se agregara el constraint
	 */
	public abstract void  addEnviroment(String clasname,String parvarn1,String parvarn2, Integer unit) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;
	
	
	/**
	 * Metodo para agregar un constraint al entorno
	 * @param clasname -Nombbre de la Clase que identifica el Contraint
	 * @param parvarn1 -String Parametro del Constraint 
	 * @param parvarn2 -Integer Parametro para representar uns constante
	 * @param unit - Integer Unidad de Tiempo en la que se agregara el constraint
	 */
	public abstract void  addEnviroment(String clasname,String parvarn1,Integer parvarn2, Integer unit) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;

    /**
     * Metodo para agregar un constraint  al entorno
     * @param clasname -Nombbre de la Clase que identifica el Contraint
     * @param parvarn1 -String Parametro del Constraint
     * @param parvarn2 -String Parametro del Constraint
     * @param parvarn3 -String Parametro del Constraint
     * @param unit	- Int Unidad de Tiempo en la que se agregara el constraint
     */
    public abstract void  addEnviroment(String clasname,String parvarn1,String parvarn2, String parvarn3, Integer unit) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;

    /**
     * Metodo para agregar un constraint  al entorno
     * @param clasname -Nombbre de la Clase que identifica el Contraint
     * @param parvarn1 -String Parametro del Constraint
     * @param parvarn2 -Integer Parametro del Constraint
     * @param parvarn3 -String Parametro del Constraint
     * @param unit	- Int Unidad de Tiempo en la que se agregara el constraint
     */
    public abstract void  addEnviroment(String clasname,String parvarn1,Integer parvarn2, String parvarn3, Integer unit) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;


    /**
	 * Metodo para convertir un constraint de Strint to C
	 * @param classname
	 * @param parvarn1
	 * @param parvarn2
	 * @return C - Constraint type C
	 */
	public abstract C converConstrain(String classname,String parvarn1,String parvarn2) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;
	
	/**
	 * Metodo para convertir un constraint de Strint to C
	 * @param classname
	 * @param parvarn1
	 * @param parvarn2
	 * @return C - Constraint type C
	 */
	public abstract C converConstrain(String classname,String parvarn1,Integer parvarn2) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;


    /**
     * Metodo para convertir un constraint de Strint to C
     * @param classname
     * @param parvarn1
     * @param parvarn2
     * @param parvarn3
     * @return C - Constraint type C
     */
    public abstract C converConstrain(String classname,String parvarn1,String parvarn2, String parvarn3) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;


    /**
     * Metodo para convertir un constraint de Strint to C
     * @param classname
     * @param parvarn1
     * @param parvarn2
     * @param parvarn3
     * @return C - Constraint type C
     */
    public abstract C converConstrain(String classname,String parvarn1,Integer parvarn2, String parvarn3) throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;

	/**
	 * Metodo para escribir las variables del Ambiente en formato XML
	 * @param filename
	 */
	public abstract void writerVarsXml(String filename);
	
	/**
	 * Metodo para leer las variables del Ambiente en formato XML
	 * @param filename
	 */
	public abstract  void readerVarsXml(String filename); 
	
	
	
}


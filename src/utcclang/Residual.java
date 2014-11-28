package utcclang;

/**
 * @author Misael Viveros Castro
 * clase  que implementa los procesos residuales.
 * @version {@value}
 */


import java.util.LinkedList;

public class Residual {
	private volatile static LinkedList<Procesos> residual = new LinkedList<Procesos>();
	public volatile static Object lockWrite;
	public volatile static Object lockget;

	/**
	  * Retorma el Proceso residual, y lo elimina de la lista de procesos
	  * @return Proceso residual
	  */
	public static Procesos getresidual() {
		synchronized (lockget) {
			return Residual.residual.poll();
		}
		     
	  }
	  
	/**
	   * Retorma el Proceso residual, y lo elimina de la lista de procesos
	   * @return Proceso residual
	   */
	public static LinkedList<Procesos> getresidualAll() {
		     return  Residual.residual;
	  }
	  /**
	   * Agrega un proceso residual, al final de lista de procesos
	   * @param proce
	   */

	  public synchronized static  void addresiduaLast(Procesos proce) {
		  	Residual.residual.addLast(proce);
		}

	  /**
	   * Agrega un proceso residual, al comienzo  de lista de procesos
	   * @param proce
	   */
	  public synchronized static void addresiduaFirst(Procesos proce) {
			  Residual.residual.addFirst(proce);
	  }

	  public static int getnumProResidual() {
		return Residual.residual.size();
		  
	  }
	  
	  /**
	   * Borra todos los procesos residuales 
	   * @return void
	   */
	  public static  void releseResidual() {
		  Residual.residual.clear();
	  }

	
}

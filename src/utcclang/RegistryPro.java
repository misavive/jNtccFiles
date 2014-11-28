package utcclang;

import utccEnums.EjecutionProc;
import utccEnums.NameProce;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase que registra e identifica el estate (Creado, Ejecutado, Terminado) de
 * cada uno de los procesos durante la ejecucion
 * @author MISAEL VIVEROS CASTRO
 * @version 1.0
 * @updated 31-oct-2012 07:04:57 p.m.
 */

public class RegistryPro {

	/**
	 * Lista que identifica el estado de cada uno de los procesos (Ask, Tell, Par etc.)
	 */
	private static volatile List<StrucStateproc> listStadoProc = Collections.synchronizedList(new ArrayList<RegistryPro.StrucStateproc>()) ;

			/**
			 * Estructura de para el registro de cada uno de procesos en ejecucion
			 * 
			 * @author MISAEL VIVEROS CASTRO
			 * @version 1.0
			 * @created 27-feb-2012 05:05:22 p.m.
			 */
			private static class StrucStateproc {
		
				/**
				 * Constante que identifica el tipo de proceso (_ASK, _TELL , _PAR ...)
				 */
				private NameProce namePro;
				/**
				 * Constante de Estado de los procesos registrados  _CREATE, _RUNNING, _FINISH 
				 */
				private EjecutionProc statePro;
		
				
				/**
				 * Proceso registrado
				 */
				private Procesos proce;
				/**
				 * Metodo para agregar Procesos a la estructura de Seguimiento
				 * @param namePro --> type Constante NameProce
				 * @param statePro --> type Constante EjecutionProc
				 * @return --> StrucStateproc -- Estructura de Procesos
				 */
				public StrucStateproc addStruct(NameProce namePro, EjecutionProc statePro) {
					this.namePro = namePro;
					this.statePro = statePro;
					return this;
				}
				
				/** Metodo para agregar Procesos a la estructura de Seguimiento
				 * @param namePro --> type Constante NameProce
				 * @param statePro --> type Constante EjecutionProc
				 * @return --> StrucStateproc -- Estructura de Procesos
				 */
				public StrucStateproc addStruct(NameProce namePro, EjecutionProc statePro, Procesos proce) {
					this.namePro = namePro;
					this.statePro = statePro;
					this.proce = proce;
					return this;
				}
				
			}// end StrucSta	
			
		
						
			

			
	/*@Override
	public void finalize() throws Throwable {
		
	 }*/
	
	/**
	 * Metodo para Cambiar el estado  registro de procesos
	 * 
	 * @param idProc --> Es numero de indice de la lista de registro de Procesos.
	 * @param statePro --> _CREATE, _RUNNING, FINISH
	 */
	public static void ChangeState(int idProc, EjecutionProc statePro){
		synchronized (RegistryPro.getlistStadoProc()) { //RegistryPro.lockWrite 
			StrucStateproc statePr = RegistryPro.getlistStadoProc().get(idProc);
			statePr.statePro = statePro;
			RegistryPro.getlistStadoProc().set(idProc, statePr);
		}
	}

	/**
	 * Lista que identifica el estado de cada uno de los procesos (Ask, Tell, Par etc.)
	 */
	public static List<StrucStateproc> getlistStadoProc(){
		return RegistryPro.listStadoProc;
	}

	/**
	 * Retorna el Numero de Procesos registrados con 'X' stado
	 * 
	 * @param statePro
	 * @return int ->> Numero de Estados
	 */
	public static int numProState(EjecutionProc statePro){
		int staPro=0;
		synchronized (RegistryPro.getlistStadoProc()) {
			for (StrucStateproc eProce : RegistryPro.getlistStadoProc()) {
				if (eProce.statePro.equals(statePro)) {
					staPro++;
				}
			} 
			return staPro;
		}
	}

	/**
	 * Retorna el Numero de Procesos registrados con 'X' stado  y nombre de proceso 'Z'
	 * 
	 * @param statePro
	 * @param namePro
	 * @return int
	 */
	public  static int numProStateTipo(EjecutionProc statePro, NameProce namePro){
		int staPro=0;
		synchronized (RegistryPro.getlistStadoProc()) {
			for (StrucStateproc eProce : RegistryPro.getlistStadoProc()) {
				if (eProce.statePro.equals(statePro) && eProce.namePro.equals(namePro)) {
					staPro++;
				}
			} 
			return staPro;
		}
		
	}

	/**
	 * Retorna el Numero de Procesos registrados con state _RUNNING  and exclude name process 'Z'
	 * 
	 * @param namePro
	 * @return int
	 */
	public  static int numProExcludeName(NameProce namePro){
		int staPro=0;
		synchronized (RegistryPro.getlistStadoProc()) {
			for (StrucStateproc eProce : RegistryPro.getlistStadoProc()) {
				if (!eProce.namePro.equals(namePro) && eProce.statePro.equals(EjecutionProc._RUNNING)) {
					staPro++;
				}
			} 
			return staPro;
		}
	}
	
	/**
	 * Retorna el Numero de Procesos registrados con state _RUNNING  and exclude name process 'Z' and exclude _unless
	 * 
	 * @param namePro
	 * @return int
	 */
	public static int numProExcludeU(NameProce namePro){
		int staPro=0;
		synchronized (RegistryPro.getlistStadoProc()) {
			for (StrucStateproc eProce : RegistryPro.getlistStadoProc()) {
				if (!eProce.namePro.equals(namePro) && !eProce.namePro.equals(NameProce._UNLESS) && eProce.statePro.equals(EjecutionProc._RUNNING)) {
					staPro++;
				}
			} 
			return staPro;
		}
	}
	
	/**
	 * Retorna el Numero de Procesos registrados con 'Z' nombre
	 * 
	 * @param namePro
	 * @return int
	 */
	public static int numProTipo(NameProce namePro){
		int staPro=0;
		synchronized (RegistryPro.getlistStadoProc()) {
			for (StrucStateproc eProce : RegistryPro.getlistStadoProc()) {
				if ( eProce.namePro.equals(namePro)) {
					staPro++;
				}
			} 
			return staPro;
		}
	}

	/**
	 * Metodo que registra un proceso en la lista de Estados de ejecucion 
	 * 
	 * @param namePro --> (_ASK, _TELL , _PAR ...) 
	 * @param statePro --> _CREATE, _RUNNING, FINISH
	 * @return Retorna la posicion en la lista donde fue registrado.
	 */
	public static int registry(EjecutionProc statePro, NameProce namePro){
		synchronized (RegistryPro.getlistStadoProc()) {
				StrucStateproc strupro = new StrucStateproc().addStruct(namePro, statePro);
				RegistryPro.listStadoProc.add(strupro);
				return RegistryPro.getlistStadoProc().size()-1;
		}
	}
	
	/**
	 * Metodo que registra un proceso en la lista de Estados de ejecucion 
	 * 
	 * @param namePro --> (_ASK, _TELL , _PAR ...) 
	 * @param statePro --> _CREATE, _RUNNING, FINISH
	 * @param proce --> proceso registrado, generalmente un Ask para Validar los Constraint
	 * @return Retorna la posicion en la lista donde fue registrado.
	 */
	public static int registry(EjecutionProc statePro, NameProce namePro, Procesos proce){
		synchronized (RegistryPro.getlistStadoProc()) {
				StrucStateproc strupro = new StrucStateproc().addStruct(namePro, statePro,proce);
				RegistryPro.listStadoProc.add(strupro);
				return RegistryPro.getlistStadoProc().size()-1;
		}
	}
	
	
	
	/**
	 * Imprime la lista de procesos registrados
	 */
	public static void printList() {
        synchronized (RegistryPro.getlistStadoProc()) {
            for (StrucStateproc eProce : RegistryPro.getlistStadoProc()) {
                System.out.println("Name" +	eProce.namePro + " Status "+ eProce.statePro );
            }
        }
	}
	
	public static void unRunning() {
        synchronized (RegistryPro.getlistStadoProc()) {
            for (StrucStateproc eProce : RegistryPro.getlistStadoProc()) {
                if ( eProce.statePro.equals(EjecutionProc._RUNNING)) {
                    eProce.statePro = EjecutionProc._FINISH;
                }
            }
        }
	}
	
	/**
	 * Retorna una lista con los identificadores (idProce) de los procesos con estado _RUNNING
	 * @param nameproce --> Nombre del proceso
	 * @return  idProce --> ArrayList<Integer>
	 */
	public  static ArrayList<Integer> listProRunning(NameProce nameproce) {
		ArrayList<Integer> idproce = new  ArrayList<Integer>();
		synchronized (RegistryPro.getlistStadoProc()) {
			for ( Integer i = 0; i < RegistryPro.getlistStadoProc().size(); i++ ) {
				StrucStateproc eProce = (StrucStateproc) RegistryPro.getlistStadoProc().get(i);
				if (eProce.namePro.equals(nameproce) && eProce.statePro.equals(EjecutionProc._RUNNING)) {
					idproce.add(i);
				}
			}
			System.out.println(" (Registry) - >>> Numero de Ask Running" + idproce.size() );
			return idproce;
		}
	}


	/**
	 * Metodo para retornar el proceso alamacenado  en la estructura
	 * @param idProc identificador unico de proceso
	 * @return Procesos --> Retorna el Proceso registrado. 
	 */
	public static Procesos getProR(int idProc){
		synchronized (RegistryPro.getlistStadoProc()) { 
			StrucStateproc statePr = RegistryPro.getlistStadoProc().get(idProc);
			return statePr.proce;
		}
	}
	
	
	/**
	 * Lista que registra el estado de cada uno de los procesos (Ask, Tell, Par etc.
	 * )
	 * 
	 * @param newVal
	 */
	public static void setlistStadoProc(ArrayList<StrucStateproc> newVal){
		listStadoProc = newVal;
	}

	/**
	 * 
	 * @param idProc
	 */
	public synchronized static void unregistry(int idProc){
		RegistryPro.listStadoProc.remove(idProc);
	}

	public static RegistryPro getRegistry() {
		return new RegistryPro();
	}
	
	

	
}//end RegistryPro
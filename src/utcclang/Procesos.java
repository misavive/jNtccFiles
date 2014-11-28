package utcclang;

/**
 * @author  Misael Viveros Castro
 * Interfaz Procesos
 * @version 1 
 */
import utccEnums.*;


public abstract class Procesos implements Runnable {
        /**
         * namePro -> Estructura que indentifica los procesos
         */
		 private NameProce namePro;
        /**
         * idProc -> indentificador de procesos
         */
		 private int idProc; // Indentificador de Procesos
        /**
         * idlock -> indentificador numerico de sicronizacion de ejecucion
         *        -> colocado por los choice para controlar la ejecucion de los procesos
         */
         private int idlock;
	
	
        /**
         * Nombre constante que identifica cada Proceso para ser registrado
		 * @return the namePro
		 */
		public NameProce getName() {
			return namePro;
		}


		/**
		 * @param namePro the namePro to set
		 */
		public void setName(NameProce namePro, EjecutionProc statePro) {
			this.namePro = namePro;
			this.setIdProc(RegistryPro.registry(statePro, namePro));
		}

		
		/**
		 * @param namePro  the namePro to set
		 * @param statePro
		 * @param proce
		 */
		public void setName(NameProce namePro, EjecutionProc statePro,Procesos proce) {
			this.namePro = namePro;
			this.setIdProc(RegistryPro.registry(statePro, namePro,proce));
		}
		
		/**
		 * Numero de Id que identifica cada Proceso en lista de Registro
		 * @return the idProc
		 */
		public int getIdProc() {
			return idProc;
		}


		/**
		 * @param idProc the idProc to set
		 */
		public void setIdProc(int idProc) {
			this.idProc = idProc;
		}



        /**
         * @return the idlock
         */
        public  int getIdlock() {return idlock; }

        /**
         * @param idlock the idlock to set
         */
        public  void setIdlock(int idlock) {this.idlock = idlock; }


		/**
         * Ejecuta los procesos en cada clase.
         */
	@Override
	public abstract void run();
	
	
	
}
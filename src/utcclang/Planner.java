package utcclang;

/**
 * @author Misael Viveros Castro
 * clase  que implementa la ejecucion de los procesos.
 * @version {@value}
 */

import JaCoP.constraints.*;
import utccEnums.EjecutionProc;
import utccEnums.LocksC;
import utccEnums.NameProce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;


public class  Planner implements Runnable {
	private static  List<Procesos> proceAct = Collections.synchronizedList(new ArrayList<Procesos>());
	private volatile static  int timeUnit;
	private volatile static AtomicInteger nextUnit = new AtomicInteger(1);
    private volatile static AtomicInteger numproRun = new AtomicInteger(0);
    private final static ReentrantLock lock = new ReentrantLock();
    //Calcular el tiempo de Ejecucion.
    public volatile long  tInicio = Calendar.getInstance().getTimeInMillis();public volatile long tFin = Calendar.getInstance().getTimeInMillis();

    /**
     *  Identificador de Lock para planner.
     *  Utilizado para validar los constraint de los Ask.
     */
    private volatile static AtomicInteger idLock =  new AtomicInteger(LocksC.getlockStruc("Planner"));
	/**
	 *  Variable global para controlar la ejecucion procesos de Ejecucion,Validacion, Terminacion
	 */
		public volatile static AtomicBoolean proEjecution= new AtomicBoolean(false);

	/**
	 * variable  para indicar la finalizacion de valiendPro
	 */
		public volatile static AtomicBoolean endVerify = new AtomicBoolean(false);
		
	
	/**
	 *  Variable para controlar el cambio de unidad 
	 */
		public volatile static AtomicBoolean changeUniTime= new AtomicBoolean(false); // Variable para controlar el cambio de unidad
	
	/**
	 *  Variable para controlar la ejecucion de los procesos 
	 */
		public volatile static AtomicBoolean proceRun= new AtomicBoolean(true);
	
	/**
	 *  Variable para controlar la ejecucion de los unless 
	 */	
		public volatile static AtomicBoolean runUnless = new AtomicBoolean(false);

    /**
     * variable  para indicar la finalizacion de los Ask (Salidad del Ciclo)
     */
        public volatile static AtomicBoolean endWaitAsk = new AtomicBoolean(false);


    /**
	 *  Variable para controlar las interaciones de validacion 
	 */
			public volatile static AtomicBoolean nextIntera= new AtomicBoolean(false);
	
	/**
	 * objeto para sincronizar los Ask
	 */
		public  static AtomicReference<Object> lockAsk = new AtomicReference<Object>(new Object());
	
	/**
	 * objeto para sincronizar los Unless
	 */
		public static AtomicReference<Object> lockUnl = new AtomicReference<Object>(new Object());
		
	/**
	 * objeto para sincronizar los Choice
	 */
		public static AtomicReference<Object> lockCho = new AtomicReference<Object>(new Object());

    /**
     * objeto para sincronizar los procesos
     */
     private static AtomicReference<Object> lockPro = new AtomicReference<Object>(new Object());
	
	/**
	 * objeto para sincronizar los procesos de Ejecucion,Validacion, Terminacion
	 */
		public  static AtomicReference<Object> syncroPro = new AtomicReference<Object>(new Object());
		
	/**
	 * Objeto para sincronizar los procesos no bloqueantes con el Verificador
	 */	
		public  static AtomicReference<Object> syncroValid = new AtomicReference<Object>(new Object());
	
	/**
	 * Objeto para sincronizar los procesos Ask con el Verificador
	 */	
		public  static AtomicReference<Object> syncroAskValid = new AtomicReference<Object>(new Object());
			
	/**
	* Objeto para sincronizar los procesos Ask con el Verificador
	*/	
		public  static AtomicReference<Object> syncroUnlValid = new AtomicReference<Object>(new Object());
		
	/**
	 * nIntera  --> Numero de Interaciones validando la terminacion de Procesos.
	 */
		private volatile static AtomicInteger nIntera = new AtomicInteger(1);



    /**
     *  nfile --> Nombre del archivo a que contiene  los constraint del
     */

       private String nfileXML;




    /**
     * Service Ejecution
     */
      private static volatile ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue(1000);

      private static volatile ThreadPoolExecutor vmpool = new ThreadPoolExecutor(500,1000,1, TimeUnit.NANOSECONDS,queue);
     //private static volatile ForkJoinPool vmpool = new ForkJoinPool();//1000,2000,1,TimeUnit.NANOSECONDS,queue);
        public static ArrayBlockingQueue<Runnable> getQueue() {
            return queue;
        }

        public static void setQueue(ArrayBlockingQueue<Runnable> queue) {
            Planner.queue = queue;
        }

        public static ThreadPoolExecutor getVmpool() {
            return vmpool;
        }

        public static void setVmpool(ThreadPoolExecutor vmpool) {
            Planner.vmpool = vmpool;
        }

      /*ForkJoinPool.ForkJoinWorkerThreadFactory factory = new ForkJoinPool.ForkJoinWorkerThreadFactory() {
          @Override
          public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
              return newThread(pool);  //To change body of implemented methods use File | Settings | File Templates.
          }
      };*/


    //private ForkJoinPool.ForkJoinWorkerThreadFactory threadGroup ;
	//	private ForkJoinPool serviceExecution = new ForkJoinPool(1);
 
//    private ForkJoinPool serviceExecution = new ForkJoinPool(60, new ForkJoinPool.ForkJoinWorkerThreadFactory() {
//        @Override
//        public ForkJoinWorkerThread newThread(ForkJoinPool pool) {
//            return newThread(pool) ;  //To change body of implemented methods use File | Settings | File Templates.
//        }
//    }, null, false);





	//************** innert Class para la ejecucion ****************

			/**
			 * runningPro implementa la ejecucion de executionProc()
			 * @author  Misael Viveros Castro
			 *
			 */
			public  class runningPro implements Runnable {
		
				@Override
				public void run() {
					while (!Planner.proEjecution.get()) {
						try {
							Planner.this.executionProc() ;
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			/**
			 * innert Class para la ejecucion de validEndPro
			 * @author  Misael Viveros Castro
			 *
			 */
			public  class execVerifiEnd implements Runnable {
				
				@Override
				public  void run() {
					while (!Planner.proEjecution.get()) {
							try {
								Planner.this.valideEndPro();
							} catch (Exception e) {
								e.printStackTrace();
							}
							//	System.out.println("(Run) Siguiente Unidad");
							//  Planner.nIntera.getAndIncrement();
						}		
					
					

				}
			}// end  execVerifiEnd
			
					
			/**
			 * innert Class para la ejecucion de changetUnit()
			 * @author  Misael Viveros Castro
			 *
			 */
			public  class runningChange implements Runnable {
				
				@Override
				public  void run() {
					while (!Planner.proEjecution.get()) {
							try {
								Planner.this.changetUnit();
							} catch (IOException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
					}
				}
			}
	//*****************		/// ****************************				
	


	/**
	 * Agregar un proceso al final de la lista de ejecucion de procesos.
	 * @param proce --> Un Proceso a ejecutar
	 */
	public void addProce(Procesos proce) {
			// pendiente -->proce.getClass().getName() para determinar cuantos tell faltan por ejecutar
        Planner.proceAct.add(proce);
        
	}
	
    
	
   /**
	* Numero de procesos  por ejecutar.
	* @return int --> numero de  Procesos  por ejecutar
	*/
	public  int getnumProce() {
		   return Planner.getProceAct().size();
		}
	

	@Override
	public  void run() {
				

        //System.out.println(" **** Cargando los Contraint del Ambiente ***");
        //if(!this.nfileXML.isEmpty()){Ambient.getAmbient().readerVarsXml(this.getNfileXML());}
        this.tInicio = Calendar.getInstance().getTimeInMillis();
        System.out.println(" **** Ejecutando El Modelo ***");
		new Thread(new Planner.runningPro()).start();
		new Thread(new Planner.execVerifiEnd()).start();
		new Thread(new Planner.runningChange()).start();
	}

	//******************************  Ejecucion  *****************************************
	
	
	/**
     * Ejecuta la lista  de  procesos Activos que son retornados por
     * getProceAct() --> Lista de Procesos.
     * @throws IOException 
     * @throws InterruptedException 
     * 
     */ 
	
	public  void executionProc() throws IOException, InterruptedException {
		synchronized (Planner.syncroPro.get()) {
				while (!Planner.proceRun.get()) {
					try {
						System.out.println("executionProc() Esperando Ejecucion");
						Planner.syncroPro.get().wait();
					} catch(InterruptedException e) {
						System.out.println("InterruptedException " +e);
					}
				}
			
			if ( Store.getsConsistency() == true) {
							System.out.println("*** (executionProc) Store Valido :-)" + "Unidad a Procesar "+ Planner.getNextUnit());
						if (Planner.getProceAct().size() > 0) {
								
								for (Procesos proce: Planner.getProceAct() ) { 
									System.out.println("*** (executionProc) Ejecutando el Proceso No.. ( "+ Planner.numproRun.get() + " ) Name .."+  proce.toString()+"  idProce : " + proce.getIdProc());
									synchronized (this) {RegistryPro.ChangeState(proce.getIdProc(), EjecutionProc._RUNNING);}
									this.addServiceExec(proce);
									Planner.numproRun.incrementAndGet();
								} // Fin de la Ejecucion
							
							/*
							 * inicio del Seguimiento de procesos 
							 */
							System.out.println("*** (executionProc) Autorizando Seguimiento  *** ");
							Planner.endVerify.set(true);
						} else {
								Planner.setChangeUniTime(true);
								System.out.println("(*** (executionProc) No hay procesos a Ejecutar ---> Se Autoriza el cambio de Unidad");
						
						}	
			} else {
						Planner.setChangeUniTime(true);
						System.out.println("*** (executionProc) Invalid Store :-( (Planner) --> Se Autoriza el cambio de Unidad");
			}

		} // fin synchronize
				Planner.proceRun.set(false);
				synchronized (Planner.syncroPro.get()) {Planner.syncroPro.get().notifyAll(); }
				System.out.println("*** (executionProc) --> Notificando los Procesos ");
			
			
	} // fin executionProc()


	
	
	/**
	 * Validacion de la terminacion de los procesos.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public  void valideEndPro() throws InterruptedException, IOException{
            synchronized (Planner.syncroPro.get()) {
                while (!Planner.endVerify.get()) {
                    try {
                        System.out.println("ValideEndPro() Esperando Ejecucion");
                        Planner.syncroPro.get().wait();
                    } catch(InterruptedException e) {
                        System.out.println("InterruptedException " +e);
                    }
                }
            }//syncroPro

                //System.out.println("(valideEnd) ->>> Numero de procesos Running Sin Contar los Ask  " + RegistryPro.numProExcludeU(NameProce._ASK));
            synchronized (Planner.syncroValid.get()) {
                while (RegistryPro.numProExcludeU(NameProce._ASK) > 0) {
                    try {
                        //System.out.println("ValideEndPro() Esperando Terminacion de Procesos ->" + RegistryPro.numProExcludeU(NameProce._ASK));
                        Planner.syncroValid.get().wait();
                    } catch(InterruptedException e) {
                        System.out.println("InterruptedException " +e);
                    }
                }
            } // syncroValid

                if (RegistryPro.numProStateTipo(EjecutionProc._RUNNING,NameProce._ASK)> 0) {
                                    //System.out.println("(valideEnd) ->>> Notyficando Ask !  ");
                                    synchronized (Planner.lockAsk.get()) {Planner.lockAsk.get().notifyAll(); }
                                   // System.out.println("(valideEnd) ->>> Iniciando Verificacion de Ejecucion Ask!  ");

                            synchronized (Planner.syncroAskValid.get()) {
                                    while (0 < Planner.this.askvalideList(RegistryPro.listProRunning(NameProce._ASK))) {
                                        try {
                                            //System.out.println("ValideEndPro() Esperando Terminacion de Ask no Bloqueados");
                                            Planner.syncroAskValid.get().wait();
                                        } catch(InterruptedException e) {
                                            System.out.println("InterruptedException " +e);
                                        }
                                    }

                            }// syncroAskValid
                                    if (Planner.this.askvalideList(RegistryPro.listProRunning(NameProce._ASK)) == 0) {
                                       // System.out.println("Liberando Guardas de los Ask Bloqueados ");
                                        Planner.endWaitAsk.set(true);  // Liberando los Lock de los Ask
                                        synchronized (Planner.lockAsk.get()) {Planner.lockAsk.get().notifyAll(); }  // Liberando los Lock de los Ask
                                    }
                } else {
                    System.out.println("  ValideEndPro()  [  Not Running Ask  ] \n" );
                }  //Fin valide Ask >0


                                //|| (vmpool.getActiveCount() > (RegistryPro.numProStateTipo(EjecutionProc._RUNNING,NameProce._ASK) - Planner.this.askvalideList(RegistryPro.listProRunning(NameProce._ASK)) ) )) {
                    if ( (RegistryPro.numProExcludeName(NameProce._UNLESS) > 0 ) ) {
                            Planner.nextIntera.set(true);
                            if ((RegistryPro.numProStateTipo(EjecutionProc._RUNNING,NameProce._ASK) > 0) || (RegistryPro.numProStateTipo(EjecutionProc._CREATE,NameProce._ASK ) > 0)  ) {
                              //  System.out.println("  ValideEndPro() Numero de Ask Running " + RegistryPro.numProStateTipo(EjecutionProc._RUNNING, NameProce._ASK) + "Numero de Ask Create " + RegistryPro.numProStateTipo(EjecutionProc._CREATE, NameProce._ASK));
                            }

                    }else {
                                System.out.println("Comenzando la Verificacion de los Unless");
                                Planner.nextIntera.set(false);
                            if (RegistryPro.numProStateTipo(EjecutionProc._RUNNING,NameProce._UNLESS)> 0) {
                                       // System.out.println("(valideEnd) ->>> Notyficando Unless Para Ejecucion ");
                                        Planner.setRunUnless(true); // Se Autoriza la Ejecucion de los Unless
                                        synchronized (Planner.lockUnl.get()) {Planner.lockUnl.get().notifyAll(); }
                                        //System.out.println("Numero de Unless Ejecutandoce " + RegistryPro.numProStateTipo(EjecutionProc._RUNNING, NameProce._UNLESS));
                                        synchronized (Planner.syncroUnlValid.get()) {
                                            while (RegistryPro.numProStateTipo(EjecutionProc._RUNNING, NameProce._UNLESS) > 0 ) {
                                                try {
                                                    System.out.println("ValideEndPro() Esperando Terminacion de Unless ");
                                                    Planner.syncroUnlValid.get().wait();
                                                    } catch(InterruptedException e) {
                                                        System.out.println("InterruptedException " +e);
                                                    }
                                            }
                                        }// fin 	syncroUnlValid
                            } else {
                                System.out.println("  ValideEndPro()  [  Not Running Unless  ] \n" );
                            }   // fin unless > 0

                    }//end else corren otros procesos diferentes de Ask y Unless
				
                         //} // fin syncroAskValid
                       //} // fin syncroValid synchronize
                    //} //syncroPro
						
					
						//RegistryPro.printList();
			
			if (Planner.nextIntera.get()){
				Planner.setEndVerify(true);
				nIntera.getAndIncrement();
				System.out.println("(valideEnd) Siguiente  (Interacion) Validadando... "+ nIntera.get() );
			}else {
						nIntera.set(1);
						System.out.println("(valideEnd) <<<<< Finalizando la Verificacion >>>>>>>>>> " );
						Planner.setEndVerify(false);
						Planner.setChangeUniTime(true);
						System.out.println("(valideEnd) Notificando el Cambio de Unidad... ");
						synchronized (Planner.syncroPro.get()) {Planner.syncroPro.get().notifyAll(); }
            }

	}
	
	
	/**
	 * ChangetUnit Permite cambiar la unidad de tiempo
	 * borra el entorno actual   
	 * Incrementa las unidades de tiempo
	 * Borra el Store
	 * Pasa los Procesos Residuales a Activos
	 * @throws IOException 
	 * @throws InterruptedException 
	 *
	 **/
	public  void changetUnit() throws IOException, InterruptedException {
		synchronized (Planner.syncroPro.get()) {
            while (!Planner.changeUniTime.get()) {
                try {
                    System.out.println("changetUnit() Esperando Ejecucion");
                    Planner.syncroPro.get().wait();
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException " + e);
                }
            }
        } //  synchronize
        synchronized (this) {
            if (Store.getsConsistency()) {
                if (Planner.isChangeUniTime()) {
                    System.out.println(" ***************** INICIANDO CAMBIO DE UNIDAD ********************** "+RegistryPro.getlistStadoProc().size());

                    Planner.vmpool.purge(); // Eliminando la cola de procesos
                    System.out.println("(changetUnit) :( Procesos Completados :-) " + Planner.vmpool.getCompletedTaskCount() + "Numero de Procesos Activos :-( " + Planner.vmpool.getActiveCount());
                    RegistryPro.unRunning();
                   // System.out.println(" :(changetUnit) 		 ---> Ajustando el  estado de los Locks ");
                    LocksC.restoreLock();
                   // System.out.println(" :(changetUnit) Ajustando el  estado del Registro de Procesos  :)");
                    Store.writerStoreCsv();
                    Store.writerStore();
                    //Ambient.updateVars();
                    Store.restoreLevel();
                    Planner.nextUnit.getAndIncrement();
                    Planner.getProceAct().clear();
                    //System.gc();
                    if (Planner.nextUnit.get() <= Planner.getTimeUnit()) {
                        if (Residual.getnumProResidual() > 0) {
                            Planner.endWaitAsk.set(false);   // Restaurando el Lock de los Ask Para Permitir su Ejecucion
                            Planner.runUnless.set(false);   //// Restaurando el Lock de los unless Para Permitir su Ejecucion
                          //  System.out.println("(changetUnit) Aplicando los Constraint de Iniciacion");
                            //System.out.println("(changetUnit)               Estado del Store ");
                            //Store.prinVars();
                            // Aplicacion de los Constraint de Iniciacion si existen se agregan
                            ///synchronized (Store.getLockDelete()) {
                                if (Ambient.getArrayutcc().size() > 0) {
                                    Ambient.getAmbient().addStoreConstIni();
                                }
                                // si el ambiente tiene elementos se agregan
                                if (Ambient.getAmbient().getVarsEnviromen().size() > 0) {
                                    Ambient.getAmbient().addStore(Planner.nextUnit.get());
                                }
                            //}
                            System.out.println("(changetUnit) Numero de Procesos Residuales A Ejecutar -> " + Residual.getnumProResidual());
                            Planner.setProceAct(Residual.getresidualAll()); // Carga de los Procesos Residuales
                            System.out.println("(changetUnit) Numero de Procesos Cargados :) " + this.getnumProce());
                            Residual.releseResidual();
                            System.out.println(" ********************************************************************** \n");

                            System.out.println(" *** (changetUnit) La Siguiente unidad es: " + Planner.nextUnit.get() + " *** \n");
                            // Procesamos la siguiente unidad
                            System.out.println(" ********************************************************************** \n");
                            Planner.proceRun.set(true);
                        } else {
                            System.out.println("(changetUnit) Lo siento!! No hay Procesos Residuales :( \n (changetUnit) Termina El Modelo *** ");
                            System.exit(0);
                        }


                    } else {
                        this.tFin = Calendar.getInstance().getTimeInMillis();
                        System.out.println("+++++++++ (changetUnit) Fin de las Unidades a Ejecutar :(" + " Second Duration -> " + ((this.tFin - this.tInicio) / 1000));
                        System.exit(0);
                    }

                } else {
                    System.out.println("(changeUnit) +++ CAMBIO DE UNIDAD NO AUTORIZADO +++");
                    System.exit(0);
                }
            } else {
                System.out.println("(changeUnit) +++ Jntcc El Store ya No es Consistente (FIN DE LA EJECUCIoN) +++ :( :(");
                System.exit(0);
            }
        }  // synchronize(this)
	  //}  // synchronize (Planner.syncroPro.get())
		
		Planner.changeUniTime.set(false);
		System.out.println("*** (changeUnit) --> Notificando El Inicio de Ejecucion ");
		synchronized (Planner.syncroPro.get()) {
            Planner.syncroPro.get().notifyAll(); };
	}
	
	
   
   	
	
	
	//**********************************************************************
	/**
	 * Metodo para agregar un proceso Externo al Servicio de ejecucion
	 * @param proce --> Runnable
	 */
	public static void addServiceExec(Procesos proce){
		synchronized(Planner.lockPro) {
			Planner.vmpool.execute(proce);
          // System.out.println("Numero de Procesos en cola " + Planner.vmpool.getPoolSize() + "Numero de tareas " + Planner.vmpool.getTaskCount() + "Numero de Hilos Activos" + Planner.vmpool.getActiveCount());
		}
		
	}
	
	/**
	 * Metodo que asigna la lista de procesos a Ejecutar.
	 * @param proceAct the proceAct to set
	 */
	public static void setProceAct(List<Procesos> proceAct) {
		synchronized(Planner.getProceAct()) {
			Planner.proceAct.clear();
	        Planner.proceAct.addAll(proceAct);
		}
	}

	/**
	 * Metodo que retorna la lista activa de procesos a ejcutar
	 * @return the proceAct -> LinkedList<Procesos>
	 */
	public static List<Procesos> getProceAct() {
		return proceAct;
	}
	
	
   /**
    * Metodo que fija las unidades de tiempo a ejecutar
     * @param tUnit the timeUnit to set
     */
	public static void setTimeUnit(int tUnit) {
		Planner.timeUnit = tUnit;
	}

	/**
	 * Metodo que retorna las unidades de tiempo a ejecutar
	 * @return the timeUnit
	 */
	public static int getTimeUnit() {
		return Planner.timeUnit;
	}

	/**
	 * Metodo que retorna las unidades de tiempo Ejecutadas
	 * @return int --> the nextUnit
	 */
	public static int getNextUnit() {
		return Planner.nextUnit.get();
	}


    /**
     * Metodo que retorna el nombre del Archivo de Costraint que se cargan al entorno
     * @return int --> the nextUnit
     */
    public  String getNfileXML() {
        return this.nfileXML;
    }

    /**
     * Metodo que asigna el nombre del Archivo de Costraint que se cargan al entorno
     * @return int --> the nextUnit
     */
    public void setNfileXML(String nfile) {
        this.nfileXML = nfile;
    }


	/**
	 * Metodo para permitir la ejecucion de los unless
	 * @param runUnless the runUnless to set
	 */
	public static void setRunUnless(boolean runUnless) {
		Planner.runUnless.set(runUnless);
	}

	/**
	 * @return the changeUniTime
	 */
	public static boolean isChangeUniTime() {
		return changeUniTime.get();
	}

	/**
	 * @param changeUniTime the changeUniTime to set
	 */
	public static void setChangeUniTime(boolean changeUniTime) {
		Planner.changeUniTime.set(changeUniTime);
	}
	
	/**
	 * Metodo que valida los constraint de los Ask RUNNING
	 * Par verificar si estan bloqueados.
	 * @param idProce
	 * @return valide --> bolean
	 */
	@SuppressWarnings("rawtypes")
	public  boolean stateAsk(int idProce) { // se quito el syncrhonize
        boolean valide = false;
        lock.lock();
        try {

            AskGen<?> proceAsk = (AskGen) RegistryPro.getProR(idProce);
            if (Store.validateCons((Constraint) proceAsk.getC())) {

                valide = true;
                //System.out.println("***|||||*** (stateAsk) Constrain Validado Exitosamente " + proceAsk.getIdProc());
            }
        }  catch (Exception e) {
            System.out.println("error " +e.toString());
        }  finally {
            lock.unlock();
        }
				return valide;
	}
	
	/**
	 * Metodo para validar las guardas de una Lista de Procesos Ask 
	 * @param lisIdproce --> Arraylist con los idProce de los Ask que Estan corriendo.
	 * @return int
	 */
	public  int askvalideList(ArrayList<Integer> lisIdproce) {
		int valide = 0;
		Boolean state = false;
		synchronized(this){        // lisIdproce
			for (Integer idproce: lisIdproce ) {
				state = Planner.this.stateAsk(idproce);
				if (state) {
					valide++;
				}
			}
            //System.out.println("***(Planner)-> (askvalideList) Numero de Guardas Validadas " + valide);
			
			return valide;
		}
		
	}

    /**
     * Get idLock
     * @return idLock - int
     */

    public static int getIdLock() {
        return idLock.get();
    }

    /**
     * Set idLock
     * @return the endVerify
     */
    public static void setIdLock(int idLock) {
        Planner.idLock.set(idLock);
    }


    /**
     * Set idLock
     * @return the endVerify
     */
    private void espere() {
        try {
            System.out.println(" --A--");
            LocksC.getIdLock(Planner.getIdLock()).getCountDownLock().await();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    /**
	 * Metodo que indica que si la verificacion de la terminacion de los procesos finalizo
	 * @return the endVerify
	 */
	public static boolean isEndVerify() {
		return Planner.endVerify.get();
	}

	/**
	 * Metodo que indica que si la verificacion de la terminacion de los procesos debe concluir
	 * @param endVerify the endVerify to set
	 */
	public static void setEndVerify(boolean endVerify) {
		Planner.endVerify.set(endVerify);
	}


    public static Planner getPlanner() {
        return new Planner();
    }
}

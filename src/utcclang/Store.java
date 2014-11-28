package utcclang;

/**
 * @author  Misael Viveros Castro
 *  implementacion de la clase StoreGen de Utcc en Jacop Solver.
 * S -Tipo del Store
 * C -Tipo de Constraint
 * I -Tipo de enteros a manejar
 * @version {@value}
 */


import JaCoP.constraints.*;
import JaCoP.core.BooleanVar;
import JaCoP.core.IntVar;
import utccEnums.CsvWriter;
import utccEnums.LocksC;
import utccEnums.Vars;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Store extends StoreGen<JaCoP.core.Store, Constraint,IntVar> {
    private final  static AtomicInteger numStore = new AtomicInteger(2);
    private volatile static AtomicReference<Object> lockValConst = new AtomicReference<Object>(new Object());
    private volatile  static AtomicReference<Object> lockDelete = new AtomicReference<Object>(new Object());
    //private final static ReentrantLock lock = new ReentrantLock(); // Change 03/06 lenta ejecucion
    private final static AtomicReference<Object> lock = new AtomicReference<Object>(new Object());
    //private final static ReentrantLock lockDelete = new ReentrantLock();
    private volatile static AtomicReference<JaCoP.core.Store> modelStore = new AtomicReference<JaCoP.core.Store>(new JaCoP.core.Store());
    private volatile static AtomicReference<Object> lockAddVar = new AtomicReference<Object>(new Object());
    private volatile static BooleanVar varAuxtemp01 =  new BooleanVar(Store.getModelStore(),"varAuxtemp01", 0,1);


    /*
	 * (non-Javadoc)
	 * @see StoreGen#creavars(java.lang.String)
	 */
	@Override
	public void creavars(String name) {
        Store.getModelStore().setLevel(1);
        synchronized (Store.lockAddVar.get()) {
            IntVar temp_1970_05 = new IntVar();temp_1970_05.id = name;  //"Prefijo que altera el nombre de la variable"
            Store.getModelStore().putVariable(temp_1970_05);
        }
		//new IntVar(Store.getModelStore(), name,Integer.MIN_VALUE/2,Integer.MAX_VALUE/2);
		
	}

	/*
	 * (non-Javadoc)
	 * @see StoreGen#creavars(java.lang.String, int, int)
	 */
	@Override
	public void creavars(String name, int valinf, int valsup) {
        Store.getModelStore().setLevel(1);
		new IntVar(Store.getModelStore(), name,valinf,valsup);
        Ambient.addVarutcc(name, valinf, valsup);
	}
	
	@Override
	public void creavars(String name, int[] values) {
        Store.getModelStore().setLevel(1);
		IntVar temp_1970_05 = new IntVar();
        temp_1970_05.id = name;
        for (int val:values) {
            temp_1970_05.addDom(val, val);
        }
		Store.getModelStore().putVariable(temp_1970_05);
        Ambient.addVarutcc(name, values);
	}
	 
	@Override
	public IntVar creavarsRetor(String name) {
        Store.getModelStore().setLevel(1);
        Ambient.addVarutcc(name);
		return new IntVar(Store.getModelStore(), name,Integer.MIN_VALUE,Integer.MAX_VALUE);
	}
	
	@Override
	public IntVar creavarsRetor(String name,int valinf, int valsup ) {
        Store.getModelStore().setLevel(1);
        Ambient.addVarutcc(name,valinf,valsup);
		return new IntVar(Store.getModelStore(), name,valinf,valsup);
	}

    @Override
    public IntVar creavarsRetor(String name,int... values ) {
        Store.getModelStore().setLevel(1);
        Ambient.addVarutcc(name,values);
        IntVar temp_1970_05 = new IntVar();
        temp_1970_05.id = name;
        for (int val:values) {
            temp_1970_05.addDom(val, val);
        }
        Store.getModelStore().putVariable(temp_1970_05);
        return (IntVar) Store.getModelStore().findVariable(name) ;
    }

	@Override
	public void addvars(IntVar vars) {
        Store.getModelStore().setLevel(1);
		synchronized (Store.lockAddVar.get()) {
			Store.getModelStore().putVariable(vars);
			Store.lockAddVar.get().notifyAll();
		}
		
		
	}
    /**
     * Metodo que Valida un constraint en el Store
     * @param cons --> Constraint de tipo C
     *
     */
    public static void addConstra(Constraint cons) {

        Constraint c =  cons;
      //  c.atomicExecution = true;

        //Store.lock.lock();   Change
        synchronized (Store.getLock()) {
            try {
                Store.getModelStore().setLevel(Store.numStore.get());
                Store.getModelStore().imposeWithConsistency(c);
                //Store.getModelStore().consistency();
               // System.out.print("[Store] Imprimiendo  Estado"  );
               // Store.prinVars();

                //lock.notify();
            } catch (Exception e) {
                if (Store.getsConsistency() == false) {
                    Store.setsConsistency(false);
                    System.out.print("Constraint Error - Store Inconsistente " + e);
                }  else {
                    System.out.print(" Store (addConstra)  Error - Store " + e);
                }
            }
            //finally {
            //    Store.lock.unlock();
            }


        synchronized (Planner.lockAsk.get()) { Planner.lockAsk.get().notifyAll(); }



    }



    /**
     * Metodo que Valida un constraint en el Store, es utilizado por los tell que requiren notificacion
     * @param cons --> Constraint de tipo C
     * @param lockNum --> Numero que indentifica la posicion en la lista Locks,  para notificar al objeto
     */
	public static void addConstra(Constraint cons, int lockNum) {


        Constraint c =  cons;
        c.atomicExecution = true;

		synchronized (Store.getLock()) {
            try {
                Store.getModelStore().setLevel(Store.numStore.get());

                Store.getModelStore().imposeWithConsistency(c);
                //Store.getModelStore().consistency();
                Store.prinVars();
                //Ambient.updateVars();
               //lock.notify();

            } catch (Exception e) {
                if (Store.getsConsistency() == false) {
                    System.out.print(" Store (addConstra)  Error - Store Inconsistente" + e);
                } else {
                    System.out.print(" Store (addConstra)  Error - Store " + e);
                }
            }
            //finally {
            //    lock.unlock();
        }


            LocksC.getIdLock(lockNum).getCountDownLock().countDown();
            synchronized (Planner.lockAsk.get()) {
                Planner.lockAsk.get().notifyAll(); }



    }

    /**
     * Metodo que Valida un constraint en el Store, es utilizado por los tell que requiren notificacion
     * @param cons --> Constraint de tipo C
     * @return Boolean --> identifica si fue posible validar el constraint en el store
     */

	/*public static  Boolean validateCons(Constraint cons,int lockNum) {
			int valuevar; boolean valretu = false;
            //Store.getModelStore().setLevel(Store.numStore.get());
            PrimitiveConstraint c = (PrimitiveConstraint) cons;
            c.atomicExecution = true;
               // reentrante lock
        lock.lock();
        try{
            Store.getModelStore().setLevel(Store.numStore.get());

            Store.getModelStore().imposeWithConsistency(new Reified(c, varAuxtemp01) );
            //Store.getModelStore().consistency();
		    valuevar = Store.getVarValue(varAuxtemp01);
			valretu = (valuevar == 1) ? true:false;
            //System.out.println("== [Store] [ValidateCons()]==  valor constraint " + valretu + "Aplicación Exitosa -- > " + c.satisfied());
            varAuxtemp01.remove(Store.numStore.get());

            LocksC.getIdLock(lockNum).getCountDownLock().countDown();

		} catch (Throwable e) {

            if (Store.getsConsistency() == false) {
                Store.setsConsistency(false);
                System.out.print("([ValidateConst] - Constraint Error in Store  - Inconsistente " + e);
            } else {
                System.out.print(" Store (addConstra)  Error - Store " + e);
            }
        }

        finally {
            lock.unlock();
        }
        return valretu;
	}*/

    /* Metodo que Valida un constraint en el Store, es utilizado por los tell que requiren notificacion
    * @param cons --> Constraint de tipo C
    * @return Boolean --> identifica si fue posible validar el constraint en el store
    */

    public static  Boolean validateCons(Constraint cons) {
        boolean valretu = false;


        // reentrante lock
        //lock.lock();   // change 03/06 many Locks
        synchronized (Store.getLock()) {
        try{

            PrimitiveConstraint c = (PrimitiveConstraint) cons;
            c.atomicExecution = true;

            Store.getModelStore().setLevel(Store.numStore.get());

            Store.getModelStore().imposeWithConsistency(new Reified(c, varAuxtemp01) ); //varAuxtemp01
            //Store.getModelStore().consistency();

            valretu = (varAuxtemp01.value() == 1) ? true:false;
            //System.out.println("== [Store] [ValidateCons()] " + valretu + "satis " + c.satisfied());
            varAuxtemp01.remove(Store.numStore.get());



        } catch (Throwable e) {

            if (Store.getsConsistency() == false) {
                System.out.print("([ValidateConst] - Constraint Error in Store  - Inconsistente " + e);
            } else {
                System.out.print(" Store (ValidateConst)  Error - Store " + e);
            }
        }

         // finally {
         //   lock.unlock();
        }
        return valretu;
    }

	public static void prinVars() {
		Store.getModelStore().print();
	}


	public  void setModelStore(JaCoP.core.Store stStore) {
        synchronized (Store.modelStore) {
            Store.modelStore.set(stStore);
        }
	}

    public static void upLevel() {
       Store.getModelStore().setLevel(Store.getModelStore().level + 1);
    }

    public static void restoreLevelSin() {
        int levelS = Store.getLevel();
        int levelf = levelS -1;
        synchronized (Store.lockDelete) {
            if (levelf >= 1) {
                Store.getModelStore().removeLevel(levelS);
                Store.getModelStore().setLevel(Store.numStore.get());

            } else {
                System.out.println("Lo siento No hay nigun Nivel de Store creado ");
            }
        }
    }

    public static void restoreLevel() {
             int levelS = Store.getLevel();
             int levelf = levelS -1;

        //Store.getLock().lock();
        synchronized (Store.getLock()) {
        try{
            if (levelf >= 1) {
                Store.getModelStore().removeLevel(levelS);
                Store.getModelStore().setLevel(Store.numStore.get());

            } else {
                System.out.println("Lo siento No hay nigun Nivel de Store creado ");
            }
        } catch (Throwable e) {
            System.out.print(" [Store][restoreLevel]  Error -  " + e);
        }
       // finally {
       //     Store.getLock().unlock();
        }
    }

    public static int getLevel() {
        return Store.getModelStore().level ;
    }

	public static JaCoP.core.Store getModelStore() {
        synchronized (lockValConst.get()) {
		    return Store.modelStore.get();
        }
	}

    /*public static ReentrantLock getLockDelete() {
        return Store.lockDelete;
    }*/

   // public static ReentrantLock getLock() {
   //     return Store.lock;
   // }

     public static Object getLock() {
         return lock.get();
     }



	protected static int getVarValue(IntVar var) {
        synchronized(var) {
            return var.value();
        }
	}

	public static Store getStore() {
		return new Store();
	}

	@Override
	public Boolean isConsistent() {
	
		return Store.getModelStore().consistency();
	}

	

	public static void writerStoreCsv() throws IOException {
        String nameFile = Store.getStore().getFilename() +".csv";

        boolean fileExists = new File(nameFile).exists();
        CsvWriter csvOutput = new CsvWriter(new FileWriter(nameFile, true), ',');


		if (!fileExists) {

            csvOutput.write("DateTime");//Calendar.getInstance().getTime().toString()
            csvOutput.write("UTiempo"); //+ Planner.getNextUnit()
			for (Vars vars : AmbientGen.getArrayutcc()) {
                csvOutput.write(Store.getModelStore().findVariable(vars.varname).id());
			}
            csvOutput.endRecord();
		} else {
            csvOutput.write(Calendar.getInstance().getTime().toString());
            csvOutput.write(String.valueOf(Planner.getNextUnit()));
			for (Vars vars : AmbientGen.getArrayutcc()) {
                IntVar tempo = (IntVar) Store.getModelStore().findVariable(vars.varname); String valor = String.valueOf(tempo.value());
                csvOutput.write(valor);
			}
            csvOutput.endRecord();
            csvOutput.close();
		}
		
			
	}

    public static void writerStore() throws IOException {
        File file = new File(Store.getStore().getFilename());


        if (!file.exists()) {
            PrintWriter writeSt = new PrintWriter(new FileWriter(file));
            writeSt.println(" *** Avispa Research Group - Misael Viveros Castro *** " );
            writeSt.println("  *** jNtcc Virtual Machine - Datos del Store *** "  );
            writeSt.println("\n Unidad de Tiempo --> "+ Planner.getNextUnit() + " Generada... "+ Calendar.getInstance().getTime() );

            for (Vars vars : AmbientGen.getArrayutcc()) {
                writeSt.println(" Varname :" + Store.getModelStore().findVariable(vars.varname));
            }
            writeSt.println("\n **** Fin de la Unidad **** ");
            writeSt.close();
        } else {
            PrintWriter writeSt = new PrintWriter(new FileWriter(file, true));
            writeSt.println("\n Unidad de Tiempo --> "+ Planner.getNextUnit() +" Generada... "+ Calendar.getInstance().getTime() );
            writeSt.println("Procesos ejecutados En la Unidada :"+ Planner.getPlanner().getnumProce() );
            writeSt.println("Numero de Procesos Ejecutados :"+ Planner.getVmpool().getCompletedTaskCount() );
            for (Vars vars : AmbientGen.getArrayutcc()) {
                writeSt.println(" Varname :" + Store.getModelStore().findVariable(vars.varname));
            }
            writeSt.println("\n **** Fin de la Unidad **** "   );
            writeSt.close();
        }


    }
	

}

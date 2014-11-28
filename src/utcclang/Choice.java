package utcclang;

import utccEnums.EjecutionProc;
import utccEnums.LocksC;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Clase que implementa la instrucion de Choice de Ntcc
 * @author  Misael Viveros Castro
 *
 */
public class Choice extends ChoiceGen<AskChoice> {

	
	/**
	 * 
	 */
	public Choice(AskChoice...asks ) {
		this.setAsk(asks);
		this.locknum = LocksC.getlockStruc(this.toString());
	}
	
	public static Choice getChoice() {
		return new Choice();
	}

	/* (non-Javadoc)
	 * @see Procesos#run()
	 */
	
	@Override
	public  void run() {
                List<AskChoice> temp = new LinkedList<AskChoice>(); //arreglo temporal para desordenar la lista
			synchronized (this) {
					//System.out.println(" Iniciando ---> "+ this.getIdProc()+ " Proce Name "+ this.toString());


					LocksC.SetCountlock(this.locknum, this.getAsk().length);
			        for (AskChoice ask : this.getAsk()) {
			            try {
			            	ask.setIdlock(this.locknum);
			            	//System.out.println(" (Choice)..  Ejecutando el Proceso No.---> "+ ask.getIdProc()+ " Proce Name "+ ask.toString());
                            temp.add(ask);
			            	RegistryPro.ChangeState(ask.getIdProc(), EjecutionProc._RUNNING);
			            	Planner.addServiceExec(ask);
			            } catch (Exception e) { System.out.print("Error"+e); }
			            
			        }
			       // System.out.println(" (Choice).. Locknum (numero del Lock) --------->" + this.locknum + " Size List Askchoice " + this.getAsk().length );
			        //System.out.println("(Esperando Terminacion de los AskChoice " + LocksC.getCountlock(this.locknum).getCount());

                    try {
                        LocksC.getIdLock(this.locknum).getCountDownLock().await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

	  		 
	        
				      	//System.out.println(" (Choice)...                :) Notyficando Los AskChoice Para su ejecucion ---->" );
				        LocksC.StarteLock(this.locknum); // indica que los askchoice pueden ejecutarse
			  	  }
                    //desordenado la lista de ejecución
                    Collections.shuffle(temp);AskChoice[] newarray = new AskChoice[this.getAsk().length] ;
                    for (int i=0 ;i< temp.size() ; i++) { newarray[i] = temp.get(i) ;} ;this.setAsk(newarray);
				    synchronized (LocksC.getStarObject(this.locknum)) {LocksC.getStarObject(this.locknum).notifyAll(); };
                    //System.out.print("Validacion --- > Estado de las guardas de AskChoice " + LocksC.getStar(this.locknum));
			        synchronized (Planner.syncroValid.get()) {RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
                        Planner.syncroValid.get().notifyAll(); };
			        
			        try {
						this.finalize();
					} catch (Throwable e) {
						e.printStackTrace();
					}
		//}
	}
	

}

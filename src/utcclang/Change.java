package utcclang;

import JaCoP.constraints.*;
import JaCoP.core.IntVar;
import utccEnums.EjecutionProc;
import utccEnums.OpCons;
import utccEnums.Operation;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Change permite cambiar el valor de una variable en la siguiente unidad de tiempo
 * @author Misael Viveros Castro 18/05/2012
 * 
 */
public class Change extends ChangeGen<IntVar> {
    private volatile static AtomicReference<Object> lockCh1 = new AtomicReference<Object>(new Object());
    private volatile static AtomicReference<Object> lockCh2 = new AtomicReference<Object>(new Object());
    private volatile static AtomicReference<Object> lockCh3 = new AtomicReference<Object>(new Object());
    private volatile static AtomicReference<Object> lockCh4 = new AtomicReference<Object>(new Object());

    /**
     *
     */
    public Change(IntVar varnameX, IntVar varnameY) {
        super(varnameX, varnameY);
    }

    public Change(IntVar varnameX, int value) {
        super(varnameX, value);
    }

    public Change(IntVar varnameX, Operation ope, int value) {
        super(varnameX, ope, value);
    }

    /**
     *
     */


    @Override
	public  void run() {
		//System.out.println("Change Ejecuntandose ..." + this.toString());
        Constraint chConst;
			try {

				switch (this.oc) {

					case 1:
						synchronized (lockCh1) {Ambient.getAmbient().ChangeValueVar(this.getVarX().id(), this.getVarY().id());}
                        chConst =  OpCons.aSingVarC(this.getVarX(),this.getVarY());
                        Ambient.getAmbient().addIniC(this.getVarX().id(),chConst);  // revizar si se puede colocar un metodo estatico
						break;
					case 2:
						synchronized (lockCh2) {Ambient.getAmbient().ChangeValueVar(this.getVarX().id(), this.value);}
                        chConst =  OpCons.aSingVarC(this.getVarX(), this.value);
                        Ambient.getAmbient().addIniC(this.getVarX().id(),chConst);
						break;

					case 3:
						switch (this.op) {
						case _INCRE:
							synchronized (lockCh3) {

								int temp; temp = this.getVarX().value();
								temp = temp + this.value;
								Ambient.getAmbient().ChangeValueVar(this.getVarX().id(), temp);
                                chConst =  OpCons.aSingVarC(this.getVarX(),temp);
                                //System.out.println("         ------- _Incremento del Valor > -----  " + temp + "Variable " + this.getVarX().id() + "Constraint " + chConst.toString());
                                Ambient.getAmbient().addIniC(this.getVarX().id(),chConst);
								break;
							}
						case _DECRE:
							synchronized (lockCh4) {

                                int temp; temp = this.getVarX().value();
                                temp = temp - this.value;
                                Ambient.getAmbient().ChangeValueVar(this.getVarX().id(), temp);
                                chConst = OpCons.aSingVarC(this.getVarX(),temp);
                                //System.out.println("         ------- Decremento del Valor a > -----  " + temp + "Variable " + this.getVarX().id() + "Constraint " + chConst.toString() );
                                Ambient.getAmbient().addIniC(this.getVarX().id(),chConst);
								break;
							}
						default:
		
							break;
						}
						break;
					default:
						break;
					}


				} finally {
					RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
					synchronized (Planner.syncroValid.get()) {
                        Planner.syncroValid.get().notifyAll();}
				
				}
		 
		 	
			//System.out.println("        (Change) Notyficando  A PLANNER...");
			synchronized (Planner.syncroValid.get()) {RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
                Planner.syncroValid.get().notifyAll();}
			try {
				this.finalize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		 
	}

}

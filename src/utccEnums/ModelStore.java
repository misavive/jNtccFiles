package utccEnums;

import JaCoP.constraints.PrimitiveConstraint;
import JaCoP.core.*;
import JaCoP.core.Store;

import java.util.concurrent.atomic.AtomicReference;



/**
 * Created with IntelliJ IDEA.
 * User: misaelviveroscastro
 * Date: 1/11/12
 * Time: 10:36
 * To change this template use File | Settings | File Templates.
 */
public class ModelStore {
    private  static volatile JaCoP.core.Store smodel = new  JaCoP.core.Store();
    public  static AtomicReference<Object> lockreading = new AtomicReference<Object>(new Object());

    public JaCoP.core.Store getSmodel() {
        synchronized (lockreading) {
            return smodel;
        }
    }

    public void setSmodel(Store smodel) {
        this.smodel = smodel;
    }

    public synchronized void addC(PrimitiveConstraint constraint) {
        getSmodel().impose(constraint);
    }

    public void addC(PrimitiveConstraint constraint, int idLockNum) {
        PrimitiveConstraint constraint1 = constraint;
        constraint1.atomicExecution = true;
        synchronized (smodel)  {
            getSmodel().impose(constraint1);
        }
        LocksC.getIdLock(idLockNum).getCountDownLock().countDown();
    }

    public synchronized void addCWithConsistency(PrimitiveConstraint constraint) {
         getSmodel().imposeWithConsistency(constraint);
    }

    public void addCWithConsistency(PrimitiveConstraint constraint, int idLockNum) {
       synchronized (smodel) {
        getSmodel().imposeWithConsistency(constraint);
       }
        LocksC.getIdLock(idLockNum).getCountDownLock().countDown();
    }
}

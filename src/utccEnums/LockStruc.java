package utccEnums;

/**
 * Created with IntelliJ IDEA.
 * User: misael viveros castro
 * Date: 30/10/12
 * Time: 10:28
 */

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class LockStruc {
    /**
     * Controla que solo se pueda ejecutar un Askchoice
     */
    private volatile AtomicBoolean cLock = new AtomicBoolean(false);
    /**
     * objeto de Synchronization entre los Askchoice
     */
    private volatile AtomicReference<Object> cOjectLock = new AtomicReference<Object>(new Object());
    /**
     * Objecto de synchronization Para Permitir la ejecucion de los Askchoice
     */
    private volatile AtomicReference<Object> startLock = new AtomicReference<Object>(new Object());
    /**
     * autoriza la execution de los Askchoice
     */
    private volatile AtomicBoolean starte = new AtomicBoolean(false);
    /**
     * Implementacion de CountDownLock Synchronization entre Choice y Askchoice
     */
    private volatile CountDownLatch countDownLock;// = new CountDownLatch(0);
    /**
     * Objeto  de Synchronization entre Choice y Askchoice
     */
    private volatile AtomicReference<Object>  synObjectC = new AtomicReference<Object>(new Object());
    /**
     *Nombre de La clase que toma el lock
     */
    private String nameClass;
    /**
     * @return the cLock
     */
    public Boolean getcLock() {
        return cLock.get();
    }
    /**
     * @param cLock the cLock to set
     */
    public void setcLock(Boolean cLock) {
        this.cLock.set(cLock) ;
    }
    /**
     * @return the cOjectLock
     */
    public Object getcOjectLock() {
        return cOjectLock;
    }
    /**
     *
     */
    public void setcOjectLock() {
        this.cOjectLock.set(new AtomicReference<Object>(new Object()));
    }
    /**
     * @return the nameClass
     */
    public String getNameClass() {
        return nameClass;
    }
    /**
     * @param nameClass the nameClass to set
     */
    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }
    /**
     * @return the startLock
     */
    public Object getStartLock() {
        return this.startLock;
    }
    /**
     *
     */
    public void setStartLock() {
        this.startLock.set(new AtomicReference<Object>(new Object()));
    }
    /**
     * @return the starte
     */
    public Boolean getStarte() {
        return starte.get();
    }
    /**
     * @param starte the starte to set
     */
    public void setStarte(Boolean starte) {
        this.starte.set(starte);
    }
    /**
     * @return the countDownLock
     */
    public CountDownLatch getCountDownLock() {
        return countDownLock;
    }
    /**
     * @param countDownLock the countDownLock to set
     */
    public void setCountDownLock(int countDownLock) {
        this.countDownLock = new CountDownLatch(countDownLock);
    }
    /**
     * Objeto de Synchronization entre Choice y Askchoice
     * @return the synObjectC
     */
    public Object getSynObjectC() {
        return synObjectC.get();
    }
    /**
     * @param synObjectC the synObjectC to set
     */
    public void setSynObjectC(Object synObjectC) {
        this.synObjectC.set(new AtomicReference<Object>(new Object()));
    }

    public LockStruc getLchoice() {
        return new LockStruc();
    }
}

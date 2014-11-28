package utccEnums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author  Misael Viveros Castro
 *
 */

public class LocksC {
	private volatile static List<LockStruc> listLock = Collections.synchronizedList(new ArrayList<LockStruc>()) ;
    /**
     * Controla el acceso concurrente a los metodos
     */
	private volatile static AtomicReference<Object> lockgetObj = new AtomicReference<Object>(new Object());
    private volatile static AtomicReference<Object> lockgetYes = new AtomicReference<Object>(new Object());
    private volatile static AtomicReference<Object> lockChan   = new AtomicReference<Object>(new Object());
    private volatile static AtomicReference<Object> lockStart  = new AtomicReference<Object>(new Object());
    private volatile static AtomicReference<Object> locktarObj = new AtomicReference<Object>(new Object());

		
	/**
	 * @return the listLock
	 */
	public static int getlockStruc(String nameClass) {
		synchronized (LocksC.listLock) {
            LockStruc struclock = new LockStruc();  struclock.setNameClass(nameClass);
            LocksC.getListLock().add(struclock);
			return LocksC.getListLock().size()-1;
		}
	}
	
	/**
	 * 
	 * @param idProc
	 */
	public synchronized static void unLockC(int idProc){
		LocksC.getListLock().remove(idProc);
	}
	
	

	/**
	 * @return the listLock
	 */
	public static List<LockStruc> getListLock() {
		return listLock;
	}

	/**
	 * @param listLock the listLock to set
	 */
	public static void setListLock(List<LockStruc> listLock) {
		LocksC.listLock = listLock;
	}
	
	
	public static LockStruc getIdLock(int lock) {
		synchronized (LocksC.listLock) {
			LockStruc struch = LocksC.listLock.get(lock);
			return struch;
		}
	}
	
	public static Object getObject(int lock) {
        synchronized (lockgetObj.get()) {
			LockStruc struch = LocksC.getIdLock(lock);
			return struch.getcOjectLock();
        }
	}
	
	public static Boolean getYeslock(int lock) {
        synchronized (lockgetYes.get()) {
			LockStruc struch = LocksC.getIdLock(lock);
			return struch.getcLock();
        }
	}
	
	public static void changeLock(int lock) {
		synchronized (lockChan.get()) {
			LockStruc struch = LocksC.getIdLock(lock);
			struch.setcLock(true);
		}
	}
	
	public static Boolean getStar(int lock){
        synchronized (lockStart.get()) {
			LockStruc struch = LocksC.getIdLock(lock);
			return struch.getStarte();
        }
	}
	
	public static Object getStarObject(int lock){
        synchronized (locktarObj.get()) {
			LockStruc struch = LocksC.getIdLock(lock);
			return struch.getStartLock();
        }
	}
	
	/**
	 * Metodo que autoriza la ejeccion de un proceso Askchoice en espera 
	 * Cambia a true --> starte
	 * @param lock --> int numero que indentifica en el arreglos de Locks
	 */
	public static void StarteLock(int lock){
		synchronized (LocksC.listLock) {
			LockStruc struch = LocksC.getIdLock(lock);
		    struch.setStarte(true);
		}
	}
	
	/**
	 * Restaura los Lock al estado Original
	 */
	public static void restoreLock() {
		synchronized (LocksC.listLock) {
			for (LockStruc eLocks : LocksC.getListLock()) {
				eLocks.setcLock(false);
				eLocks.setStarte(false);
			}
		}
	}
	
	// manejo de CoutdownLocks
	
	public static Object synObjCountdown(int lock){
        synchronized (LocksC.listLock) {
			LockStruc struch = LocksC.getIdLock(lock);
			return struch.getSynObjectC();
        }
	}
	
	public static CountDownLatch getCountlock(int idLock) {
        synchronized (LocksC.listLock) {
			LockStruc struch = LocksC.getIdLock(idLock);
			return struch.getCountDownLock();
        }
	}
	
	/*public static void decreCountlock(int idLock) {
		synchronized (LocksC.listLock) {
			LockStruc struch = LocksC.getIdLock(idLock);
			struch.getCountDownLock().decrementAndGet();
		}
	}
	
	public static void increCountlock(int idLock) {
		synchronized (LocksC.listLock) {
			LockStruc struch = LocksC.getIdLock(idLock);
			struch.getCountDownLock().incrementAndGet();
		}
	}*/
	
	public static void SetCountlock(int idLock, int value) {
		synchronized (LocksC.listLock) {
			LockStruc struch = LocksC.getIdLock(idLock);
			struch.setCountDownLock(value);
		}
	}


	/**
	 * Imprime la lista de Locks registrados
	 */
	public static void printList() {
		for (LockStruc eLocks : LocksC.getListLock()) {
			System.out.println("Name " +	eLocks.getNameClass() + " Status "+ eLocks.getcLock() );
		}
	}
	

}

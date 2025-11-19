package utcclang;

import utccEnums.EjecutionProc;
import utccEnums.NameProce;
import java.util.Random;

/**
 * @author  Misael Viveros Castro
 *27/05/2011
 */


public class Star extends BangGen {

    // Cada Star debe tener su propia unidad de ejecución
    private int unitInit;

    private static int ntime = 1;

    /**
     * Star no acotado: ejecución eventualmente entre 1 y el número de unidades del Planner.
     */
    public Star(Procesos p) {
        int finalnum = Planner.getTimeUnit();
        int inicio   = 1;
        Random rnd   = new Random();
        this.setUnitInit(rnd.nextInt(finalnum - inicio + 1) + inicio); // [1, timeUnit]
        this.setProce(p);
        this.setName(NameProce._BANG, EjecutionProc._CREATE, this);
    }

    /**
     * Star acotado con ejecución eventualmente entre unidadIni y ftime
     * -> inevitablemente se ejecutará en algún instante de tiempo entre unidadIni y ftime
     *
     * @param p         Proceso a ejecutar
     * @param unidadIni Unidad mínima de ejecución
     * @param ftime     Unidad máxima de ejecución
     */
    public Star(Procesos p, final int unidadIni, final int ftime) {
        this.setProce(p);
        int finalnum = ftime;
        int inicio   = unidadIni;
        Random rnd   = new Random();
        this.setUnitInit(rnd.nextInt(finalnum - inicio + 1) + inicio); // [unidadIni, ftime]
        this.setName(NameProce._BANG, EjecutionProc._CREATE, this);
    }

    @Override
    public void run() {

        synchronized (this) {
            try {

                // Compara la unidad ACTUAL (nextUnit) con la unidad programada (unitInit)
                if (Planner.getNextUnit() == this.getUnitInit()) {
                    System.out.println(
                            " Iniciando Star ---> " + this.getIdProc() +
                                    " Proce Name " + this.toString() +
                                    " Unidad Ejecucion " + this.getUnitInit()
                    );

                    RegistryPro.ChangeState(this.getProce().getIdProc(), EjecutionProc._RUNNING);
                    Planner.addServiceExec(this.getProce());

                    System.out.println(" Fin Star ---> " + this.toString() + " Unidad Ejecucion " + this.getUnitInit()
                    );
                } else {
                    // No es todavía su unidad → se deja como residual para la siguiente unidad
                    Residual.addresiduaLast(this);
                    System.out.println(
                            " Star Esperando  ---> " + this.toString() +
                                    " - Unidad Programada " + this.getUnitInit() +
                                    " - Unidad Actual " + Planner.getNextUnit()
                    );
                }

            } finally {
                RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
                synchronized (Planner.syncroValid.get()) {
                    Planner.syncroValid.get().notify();
                }
            }
        }

        synchronized (Planner.syncroValid.get()) {
            RegistryPro.ChangeState(this.getIdProc(), EjecutionProc._FINISH);
            Planner.syncroValid.get().notify();
        }

        // finalize() está deprecado y no hace nada útil aquí, podrías eliminarlo.
        try {
            this.finalize();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    // ------------ getters/setters corregidos ------------

    public int getUnitInit() {
        return unitInit;
    }

    public void setUnitInit(int unitInit) {
        this.unitInit = unitInit;
    }

    public int getNtime() {
        return ntime;
    }

    public void setNtime(int ntime) {
        Star.ntime = ntime;
    }
}
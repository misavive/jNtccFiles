package utcclang;

/**
 * @author  Misael Viveros Castro
 *27/05/2011
 */





public abstract class BangGen extends Procesos {
		private Procesos proce;


    /**
	 * @param proce the proce to set
	 */
	public void setProce(Procesos proce) {
		this.proce = proce;
	}

	/**
	 * @return the proce
	 */
	public Procesos getProce() {
		return proce;
	}

	/* (non-Javadoc)
	 * @see proyectUtcc.Procesos#run()
	 */
	@Override
	public  abstract void run();

}

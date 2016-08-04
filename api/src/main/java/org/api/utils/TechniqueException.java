package org.api.utils;

public class TechniqueException extends Exception {

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * Constructeur de la classe
	 * 
	 * @param pMessage
	 *            message a faire remonter
	 * @param pThrowable
	 *            erreur precedemment levee
	 */
	public TechniqueException(final String pMessage, final Throwable pThrowable) {
		super(pMessage, pThrowable);
	}

	/**
	 * 
	 * Constructeur de la classe
	 * 
	 * @param pMessage
	 *            message a faire remonter
	 */
	public TechniqueException(final String pMessage) {
		super(pMessage);
	}

	/**
	 * Constructeur de la classe
	 */
	public TechniqueException() {
		super("Erreur innatendue");
	}
}
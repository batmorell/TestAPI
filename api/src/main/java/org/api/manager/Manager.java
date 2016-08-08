package org.api.manager;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


import javax.annotation.Resource;
import javax.sql.DataSource;

import org.api.utils.TechniqueException;

public abstract class Manager implements Serializable{

	@Resource(mappedName = "java:/mydb")
	private DataSource mDataSource;

	protected interface Preparator<P> {
		void invoke(P p) throws SQLException;
	}

	protected interface Fetcher<P, R> {
		R apply(P p) throws SQLException, TechniqueException;
	}

	protected static void printStackTrace(Exception e) {
		StackTraceElement lElement = Thread.currentThread().getStackTrace()[3];
		System.err.println(lElement.getClassName() + "." + lElement.getMethodName() + " [" + lElement.getLineNumber() + "] : " + e.getMessage());
	}

	protected <R> R select(String pQuery, Preparator<PreparedStatement> pPreparator, Fetcher<ResultSet, R> pFetcher) throws TechniqueException {
		try (Connection lConnection = mDataSource.getConnection(); PreparedStatement lStatement = lConnection.prepareStatement(pQuery)) {
			if (pPreparator != null) {
				pPreparator.invoke(lStatement);
			}
			ResultSet lResultSet = lStatement.executeQuery();
			return pFetcher.apply(lResultSet);
		} catch (SQLException e) {
			printStackTrace(e);
			throw new TechniqueException("Erreur lors de r�cup�ration de donn�es.");
		}

	}

	protected <R> R select(String pQuery, Preparator<PreparedStatement> pPreparator, Fetcher<ResultSet, R> pFetcher, boolean pNeedManipulation) throws TechniqueException {
		if (!pNeedManipulation) {
			return select(pQuery, pPreparator, pFetcher);
		}
		try (Connection lConnection = mDataSource.getConnection(); PreparedStatement lStatement = lConnection.prepareStatement(pQuery)) {
			if (pPreparator != null) {
				pPreparator.invoke(lStatement);
			}
			if (lStatement.execute()) {
				ResultSet lResultSet = lStatement.getResultSet();
				return pFetcher.apply(lResultSet);
			} else {
				return null;
			}
		} catch (SQLException e) {
			printStackTrace(e);
			throw new TechniqueException("Erreur lors de r�cup�ration de donn�es.");
		}

	}

	protected int update(String pQuery, Preparator<PreparedStatement> pPreparator) throws TechniqueException {
		return update(pQuery, pPreparator, Statement.NO_GENERATED_KEYS);
	}

	protected int update(String pQuery, Preparator<PreparedStatement> pPreparator, int pGeneratedKeysReturnMode) throws TechniqueException {
		try (Connection lConnection = mDataSource.getConnection(); PreparedStatement lStatement = lConnection.prepareStatement(pQuery, pGeneratedKeysReturnMode)) {
			if (pPreparator != null) {
				pPreparator.invoke(lStatement);
			}
			lStatement.executeUpdate();
			if (pGeneratedKeysReturnMode == Statement.RETURN_GENERATED_KEYS) {
				ResultSet lResultSet = lStatement.getGeneratedKeys();
				if (lResultSet.next()) {
					return lResultSet.getInt(1);
				}
			}
		} catch (SQLException e) {
			printStackTrace(e);
			throw new TechniqueException("Erreur lors de la mise � jour de donn�es.");
		}
		return 0;
	}

	public void setDataSource(DataSource pDataSource) {
		mDataSource = pDataSource;
	}

}

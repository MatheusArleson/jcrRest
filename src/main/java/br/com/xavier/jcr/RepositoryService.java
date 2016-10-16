package br.com.xavier.jcr;

import javax.jcr.Repository;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.springframework.stereotype.Service;

@Service
public class RepositoryService {

	//XXX CONSTANTS
	private static final String JNDI_REPOSITORY_NAME = "jcr/repository";
		
	// XXX PROPERTIES
	private Repository repositoryInstance;

	// XXX CONSTRUCTOR
	public RepositoryService() {}

	// XXX METHODS
	public Repository getRepositoryInstance() throws Exception {
		if (repositoryInstance == null) {
			repositoryInstance = fetchRepositoryInstance();
		}
		
		return repositoryInstance;
	}

	private Repository fetchRepositoryInstance() throws Exception {
		Repository repo = null;
		try {
			
			repo = doJNDILookup(JNDI_REPOSITORY_NAME);
			
			if(repo == null){
				throw new NullPointerException("Could not get repository instance.");
			}
			
			return repo;
		} catch (Exception e) {
			throw e;
		}
	}

	private Repository doJNDILookup(String jndiName) throws NamingException {
		InitialContext context = new InitialContext();
		Context environment = (Context) context.lookup("java:comp/env");
		Repository repository = (Repository) environment.lookup(jndiName);
		
		return repository;
	}

}

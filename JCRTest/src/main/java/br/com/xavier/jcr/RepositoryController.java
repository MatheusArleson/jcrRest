package br.com.xavier.jcr;

import javax.jcr.Repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RepositoryController {
	
	// XXX DEPENDENCIES
	@Autowired
	private RepositoryService repositoryService;

	// XXX CONSTRUCTOR
	public RepositoryController() {	}

	//XXX METHODS
	@RequestMapping(value = "/repo", method = RequestMethod.GET)
	public String isRespositoryReady() throws Exception {
		Repository repo = getRepositoryService().getRepositoryInstance();
		boolean isRepoOk = repo != null;
		return String.valueOf(isRepoOk);
	}

	// XXX GETTERS/SETTERS
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}
	
}

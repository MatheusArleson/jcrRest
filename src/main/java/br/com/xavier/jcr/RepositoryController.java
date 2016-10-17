package br.com.xavier.jcr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.Workspace;
import javax.servlet.annotation.MultipartConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@MultipartConfig(fileSizeThreshold = 20971520) // 20MB
public class RepositoryController {

	// XXX DEPENDENCIES
	@Autowired
	private RepositoryService repositoryService;

	// XXX CONSTRUCTOR
	public RepositoryController() {
	}

	// XXX METHODS
	@RequestMapping(value = "/repo/status", method = RequestMethod.GET)
	public String isRespositoryReady() throws Exception {
		Repository repo = getRepositoryService().getRepositoryInstance();
		boolean isRepoOk = repo != null;
		return new String("Repository is OK ? => " + isRepoOk);
	}

	@RequestMapping(value = "/repo/metadata", method = RequestMethod.GET)
	public String fetchRepositoryMetadata() throws Exception {
		Repository repo = getRepositoryService().getRepositoryInstance();

		Map<String, Collection<String>> descriptorMap = new LinkedHashMap<>();

		String[] descKeys = repo.getDescriptorKeys();
		for (String descKey : descKeys) {
			LinkedList<String> descValues = new LinkedList<>();

			for (Value value : repo.getDescriptorValues(descKey)) {
				descValues.add(value.getString());
			}

			descriptorMap.put(descKey, descValues);
		}

		StringBuffer sb = new StringBuffer();
		for (String key : descKeys) {
			sb.append(key + " => ");
			sb.append("\n\n");
			sb.append(descriptorMap.get(key));
			sb.append("\n\n");
		}

		return sb.toString();
	}

	@RequestMapping(value = "/repo/workspaces", method = RequestMethod.GET)
	public String fetchRepositoryWorkspaces() throws Exception {
		Session session = null;
		try {
			Repository repo = getRepositoryService().getRepositoryInstance();
			session = repo.login();
			Workspace workspace = session.getWorkspace();

			String[] workspacesNames = workspace.getAccessibleWorkspaceNames();

			StringBuffer sb = new StringBuffer();
			for (String wsName : workspacesNames) {
				sb.append(wsName);
				sb.append(", ");
			}

			return sb.toString();
		} finally {
			if (session != null) {
				session.logout();
			}
		}
	}

	@RequestMapping(value = "/repo/{workspace}/upload", method = RequestMethod.POST)
	public String uploadFile(
		@PathVariable(value = "workspace", required = true) String workspaceName,
		@RequestParam(value = "fileName", required = true) String fileName,
		@RequestBody String encodedFile
	) {
		StringBuffer sb = new StringBuffer();
		sb.append("workspace => " + workspaceName);
		sb.append("fileName => " + fileName);
		sb.append("encodedFile => " + encodedFile);
		return sb.toString();
		
//		// Get name of uploaded file.
//		String fileName = uploadedFileRef.getOriginalFilename();
//
//		// Path where the uploaded file will be stored.
//		String path = "D:/" + fileName;
//
//		// This buffer will store the data read from 'uploadedFileRef'
//		byte[] buffer = new byte[1000];
//
//		// Now create the output file on the server.
//		File outputFile = new File(path);
//
//		FileInputStream reader = null;
//		FileOutputStream writer = null;
//		int totalBytes = 0;
//		try {
//			outputFile.createNewFile();
//
//			// Create the input stream to uploaded file to read data from it.
//			reader = (FileInputStream) uploadedFileRef.getInputStream();
//
//			// Create writer for 'outputFile' to write data read from
//			// 'uploadedFileRef'
//			writer = new FileOutputStream(outputFile);
//
//			// Iteratively read data from 'uploadedFileRef' and write to
//			// 'outputFile';
//			int bytesRead = 0;
//			while ((bytesRead = reader.read(buffer)) != -1) {
//				writer.write(buffer);
//				totalBytes += bytesRead;
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				reader.close();
//				writer.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		return "File uploaded successfully! Total Bytes Read=" + totalBytes;
	}

	// XXX GETTERS/SETTERS
	public RepositoryService getRepositoryService() {
		return repositoryService;
	}

}

package wang.igood.gmvc.common;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import wang.igood.gmvc.Constant;
import wang.igood.gmvc.context.client.upload.RequestFile;
import wang.igood.gmvc.context.client.upload.UploadRequestResolver;
import wang.igood.gmvc.context.client.upload.UploadRequestResolver.MultipartParsingResult;
import wang.igood.gmvc.util.XssConverter;


public class GFHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private Map<String, String[]> multipartParameters;

	private Map<String, LinkedList<RequestFile>> multipartFiles;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public GFHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);

		UploadRequestResolver uploadRequestResolver = new UploadRequestResolver(request.getSession().getServletContext());
		uploadRequestResolver.setDefaultEncoding(Constant.ENCODING);

		try {
			MultipartParsingResult pasingResult = uploadRequestResolver.resolveMultipart(request);
			this.multipartFiles = pasingResult.getMultipartFiles();
			this.multipartParameters = pasingResult.getMultipartParameters();
		} catch (Exception e) {
			logger.error("can not init UploadRequest", e);
		}
	}

	public HttpServletRequest getOriginRequest() {
		return (HttpServletRequest) super.getRequest();
	}

	protected Map<String, LinkedList<RequestFile>> getMultipartFiles() {
		if (this.multipartFiles == null) {
			throw new IllegalStateException("Multipart request not initialized");
		}
		return this.multipartFiles;
	}

	protected Map<String, String[]> getMultipartParameters() {
		if (this.multipartParameters == null) {
			throw new IllegalStateException("Multipart request not initialized");
		}
		return this.multipartParameters;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		Set<String> paramNames = new HashSet<String>();
		Enumeration<String> paramEnum = super.getParameterNames();
		while (paramEnum.hasMoreElements()) {
			paramNames.add((String) paramEnum.nextElement());
		}
		paramNames.addAll(getMultipartParameters().keySet());
		return Collections.enumeration(paramNames);
	}

	@Override
	public String getParameter(String name) {
		String[] values = getMultipartParameters().get(name);
		if (values != null) {
			return (values.length > 0 ? values[0] : null);
		}
		return XssConverter.convert(super.getParameter(name));
	}

	@Override
	public String[] getParameterValues(String name) {

		String[] values = getMultipartParameters().get(name);

		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				String temp = values[i];
				if (temp != null) {
					values[i] = XssConverter.convert(temp);
				}
			}
		}

		return values;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> paramMap = new HashMap<String, String[]>();
		paramMap.putAll(super.getParameterMap());
		paramMap.putAll(getMultipartParameters());
		return paramMap;
	}

	public Iterator<String> getFileNames() {
		return getMultipartFiles().keySet().iterator();
	}

	public RequestFile getFile(String name) {
		return getMultipartFiles().get(name).getFirst();
	}

	public List<RequestFile> getFiles(String name) {
		List<RequestFile> multipartFiles = getMultipartFiles().get(name);

		if (multipartFiles == null) {
			return Collections.emptyList();
		}

		return multipartFiles;
	}

	public static boolean isMultipart(HttpServletRequest request) {
		return (request != null && ServletFileUpload.isMultipartContent(request));
	}

	public static HttpServletRequest wrapper(HttpServletRequest request) {
		return isMultipart(request) ? new GFHttpServletRequestWrapper(request) : request;
	}
}

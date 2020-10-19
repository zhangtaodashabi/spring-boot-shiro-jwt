package com.yyy.common.utils.pagination.wrapper;




import com.yyy.common.utils.pagination.WafKit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author luohuiqi
 * @Description:
 * @Date: 2019/5/29 10:17
 */
public class WafRequestWrapper extends HttpServletRequestWrapper {
	private boolean filterXSS;
	private boolean filterSQL;

	public WafRequestWrapper(HttpServletRequest request, boolean filterXSS, boolean filterSQL) {
		super(request);
		this.filterXSS = true;
		this.filterSQL = true;
		this.filterXSS = filterXSS;
		this.filterSQL = filterSQL;
	}

	public WafRequestWrapper(HttpServletRequest request) {
		this(request, true, true);
	}

	@Override
	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null) {
			return null;
		} else {
			int count = values.length;
			String[] encodedValues = new String[count];

			for (int i = 0; i < count; ++i) {
				encodedValues[i] = this.filterParamString(values[i]);
			}

			return encodedValues;
		}
	}

	@Override
	public Map getParameterMap() {
		Map<String, String[]> primary = super.getParameterMap();
		Map<String, String[]> result = new HashMap(primary.size());
		Iterator var3 = primary.entrySet().iterator();

		while (var3.hasNext()) {
			Map.Entry<String, String[]> entry = (Map.Entry) var3.next();
			result.put(entry.getKey(), this.filterEntryString((String[]) entry.getValue()));
		}

		return result;
	}

	protected String[] filterEntryString(String[] rawValue) {
		for (int i = 0; i < rawValue.length; ++i) {
			rawValue[i] = this.filterParamString(rawValue[i]);
		}

		return rawValue;
	}

	@Override
	public String getParameter(String parameter) {
		return this.filterParamString(super.getParameter(parameter));
	}

	@Override
	public String getHeader(String name) {
		return this.filterParamString(super.getHeader(name));
	}

//	@Override
//	public Cookie[] getCookies() {
//		Cookie[] existingCookies = super.getCookies();
//		if (existingCookies != null) {
//			for (int i = 0; i < existingCookies.length; ++i) {
//				Cookie cookie = existingCookies[i];
//				String cookieValue = cookie.getValue();
//				Pattern pa = Pattern.compile(Constant.REGEX_NUM);
//				Matcher ma = pa.matcher(cookieValue);
//				if(ma.find()){
//					cookieValue = ma.replaceAll("");
//				}
//				cookie.setValue(this.filterParamString(cookieValue));
//			}
//		}
//
//		return existingCookies;
//	}

	protected String filterParamString(String rawValue) {
		if (null == rawValue) {
			return null;
		} else {
			String tmpStr = rawValue;
			if (this.filterXSS) {
				tmpStr = WafKit.stripXSS(rawValue);
			}

			if (this.filterSQL) {
				tmpStr = WafKit.stripSqlInjection(tmpStr);
			}

			return tmpStr;
		}
	}
}

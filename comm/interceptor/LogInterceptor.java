package com.siweidg.comm.interceptor;

import java.lang.reflect.Method;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.siweidg.comm.annotation.AddLog;
import com.siweidg.comm.init.Constants;
import com.siweidg.comm.init.SessionInfo;

import com.siweidg.comm.utils.ReflectionUtils;

import com.siweidg.sys.auth.entry.SysLog;
import com.siweidg.sys.auth.service.SysLogService;
import com.siweidg.sys.auth.service.UserService;

@Component
public class LogInterceptor {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SysLogService sysLogService;
	
	public void afterReturn(JoinPoint joinPoint) throws NoSuchMethodException {
		HttpServletRequest req = ServletActionContext.getRequest();
		String action = joinPoint.getTarget().getClass().getSimpleName();
		String method = joinPoint.getSignature().getName();
		Object obj = joinPoint.getTarget();
		Method m = getMethod(joinPoint);
		AddLog ano = m.getAnnotation(AddLog.class);
		SysLog log = new SysLog();
		log.setIp(req.getRemoteAddr());
		log.setOptionCode(req.getRequestURI());
		String uri = req.getRequestURI();
		SessionInfo info = (SessionInfo) req.getSession().getAttribute(Constants.SESSION_INFO);
		if(info!=null) {
			log.setUser(userService.getUserById(info.getId()));
		}
		log.setOptionName(ano.desc());	
		log.setType(ano.type().name);
		
		if(StringUtils.isNotBlank(ano.objectKey())){
			Object logsObject = ReflectionUtils.getFieldValue(obj, ano.objectKey());
			if(logsObject!=null) {
			log.setOptionId(logsObject.toString());}
		}
		
		log.setOptionTime(new Date());
		sysLogService.save(log);
	}
	

	private Class<? extends Object> getClass(JoinPoint jp)
			throws NoSuchMethodException {
		return jp.getTarget().getClass();
	}
	
	private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
		Signature sig = jp.getSignature();
		MethodSignature msig = (MethodSignature) sig;
		return getClass(jp).getMethod(msig.getName(), msig.getParameterTypes());
	}
}

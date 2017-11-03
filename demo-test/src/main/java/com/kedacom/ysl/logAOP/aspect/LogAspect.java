package com.kedacom.ysl.logAOP.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.kedacom.ysl.logAOP.annotation.AnnoMethod;

@Aspect //表示当前POJO类为切面
@Component
public class LogAspect {
    private Logger logger = LoggerFactory.getLogger("ysl_log_module");

    @Pointcut("@annotation(com.kedacom.ysl.logAOP.annotation.AnnoMethod)")
    public void pointCutMethod() {
    }

    /**
     * 
     * @param joinPoint
     *            切点
     */
    @Before("pointCutMethod()")
    public void doBefore(JoinPoint joinPoint) {
	try {
	    String methodType = getMethodName(joinPoint);
	    logger.debug(String.format("[doBefore] methodType:%s", methodType));
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    @After(value = "pointCutMethod()")
    public void doAfter(JoinPoint joinPoint) {

    }

    /**
     * @Description:切点返回数据之后调用方法
     * @date
     * @since v0.1
     * @author ysl
     */
    @AfterReturning(value = "pointCutMethod()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, int result) {
	logger.debug("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
    }

    /**
     * @Description:切点返回数据之后调用方法
     * @date
     * @since v0.1
     * @author ysl
     */
    @AfterReturning(value = "pointCutMethod()")
    public void doAfterReturning(JoinPoint joinPoint) {
	try {
	    String methodType = getMethodName(joinPoint);
	    logger.debug(String.format("[doAfterReturning] methodType:%s", methodType));
	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    /**
     * @Description:根据实参和形参以及类型获取描述方法
     * @date
     * @since v0.1
     * @author ysl
     */
    @SuppressWarnings("rawtypes")
    private String getParam(Class[] clazzs, Object[] param, String methodType) {
	StringBuilder parame = new StringBuilder();
	if (methodType.contains("edit")) {
	    for (int i = 0; i < param.length; i++) {
		Object object = clazzs[i];
		object = param[i];
		if (i == 0) {
		    parame.append("修改之前");
		    parame.append(object.toString());
		} else {
		    parame.append("修改之后");
		    parame.append(object.toString());
		}

	    }
	} else {
	    for (int i = 0; i < param.length; i++) {
		Object object = clazzs[i];
		object = param[i];
		parame.append(object.toString());

	    }
	}
	return parame.toString();
    }

    /**
     * 异常通知 用于拦截service层记录异常日志
     * 
     * @param joinPoint
     * @param e
     */
    @SuppressWarnings("rawtypes")
    @AfterThrowing(pointcut = "pointCutMethod()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
	System.out.println("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
	try {
	    String methodName = getMethodName(joinPoint);
	    /* 获取目标对象的类型 */
	    Class classN = joinPoint.getTarget().getClass();
	    /* 获取目标对象类型的所有方法 */
	    Method[] ms = classN.getMethods();
	    Class[] clazzs = null;
	    for (Method m : ms) {
		if (m.getName().equals(joinPoint.getSignature().getName())) {
		    /* 获取参数类型 */
		    clazzs = m.getParameterTypes();
		}
	    }
	    String operParamer = this.getParam(clazzs, joinPoint.getArgs(), methodName);
	    logger.debug(String.format("[doAfterThrowing] operParamer:%s", operParamer));
	} catch (Exception ex) {
	    e.printStackTrace();
	}

    }

    // 获取方法的中文备注____用于记录用户的操作日志描述
    @SuppressWarnings("rawtypes")
    public static String getMthodRemark(JoinPoint joinPoint) throws Exception {
	String targetName = joinPoint.getTarget().getClass().getName();
	String methodName = joinPoint.getSignature().getName();
	Object[] arguments = joinPoint.getArgs();
	Class targetClass = Class.forName(targetName);
	Method[] method = targetClass.getMethods();
	String methode = getMethodE(methodName, arguments, method);
	return methode;
    }

    // 获取方法操作名称
    public static String getMethodName(JoinPoint joinPoint) throws Exception {
	String targetName = joinPoint.getTarget().getClass().getName();
	@SuppressWarnings("rawtypes")
	Class targetClass = Class.forName(targetName);
	Method[] methods = targetClass.getMethods();
	Object[] arguments = joinPoint.getArgs();
	String methodName = joinPoint.getSignature().getName();
	String methodE = getMethodE(methodName, arguments, methods);
	return methodE;
    }

    @SuppressWarnings("rawtypes")
    private static String getMethodE(String methodName, Object[] arguments, Method[] method) {
	String methode = "";
	for (Method m : method) {
	    if (m.getName().equals(methodName)) {
		Class[] tmpCs = m.getParameterTypes();
		if (tmpCs.length == arguments.length) {
		    AnnoMethod methodCache = m.getAnnotation(AnnoMethod.class);
		    if (methodCache != null) {
			methode = methodCache.type();
		    }
		    break;
		}
	    }
	}
	return methode;
    }

}

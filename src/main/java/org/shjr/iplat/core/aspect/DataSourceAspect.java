package org.shjr.iplat.core.aspect;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 切换数据源
 */
@Component
@Aspect
public class DataSourceAspect {
	Logger Logger = LogManager.getLogger();

	@Pointcut("execution(* org.shjr.plat.mybatis.*.dao.*.*(..))")
	public void aspect() {
	}

	/**
	 * 配置前置通知,使用在方法aspect()上注册的切入点
	 */
	@Before("aspect()")
	public void before(JoinPoint point) {
		String method = point.getSignature().getName();
		Logger.info(method + "(" + StringUtils.join(point.getArgs(), ",") + ")");
		try {
			for (String key : ChooseDataSource.METHODTYPE.keySet()) {
				for (String type : ChooseDataSource.METHODTYPE.get(key)) {
					if (method.startsWith(type)) {
						HandleDataSource.putDataSource(key);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}


package org.apdoer.common.service.config;

import org.springframework.context.annotation.Configuration;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * @author apdoer
 */
@MapperScan("org.apdoer.common.service.mapper")
@Configuration
public class MapperConfig {
	
}

package pro.sky.newmagicschool.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class InfoService {
    Logger logger = LoggerFactory.getLogger(InfoService.class);
    @Value("${server.port}")
    private Integer serverPort;

    public Integer getPort() {
        logger.info("getPort method was invoked");
        return serverPort;
    }
}

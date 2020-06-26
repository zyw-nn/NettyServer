package com.zyw.netty;

import com.zyw.netty.server.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class NettyserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyserverApplication.class, args);
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public String startListen(@RequestParam(name = "port") int port) {
        System.out.println("Start server listening 【" + port + "】");
        new NettyServer(port).start();
        return "success";
    }
}

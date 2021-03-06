package com.pzwm.netty.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "tcp")
public class PortConfig {

    private List<Integer> ports=new ArrayList<>();

    private List<Integer> others=new ArrayList<>();

    public List<Integer> getPorts() {
        return ports;
    }

    public void setPorts(List<Integer> ports) {
        this.ports = ports;
    }

    public List<Integer> getOthers() {
        return others;
    }

    public void setOthers(List<Integer> others) {
        this.others = others;
    }
}

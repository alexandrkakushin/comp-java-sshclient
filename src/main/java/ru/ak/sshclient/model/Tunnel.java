package ru.ak.sshclient.model;

/**
 * @author a.kakushin
 */
public class Tunnel {

    private int localPort;
    private int remotePort;
    private String host;

    public Tunnel() {
    }

    public Tunnel(int localPort, int remotePort, String host) {
        this.localPort = localPort;
        this.remotePort = remotePort;
        this.host = host;
    }

    public int getLocalPort() {
        return localPort;
    }

    public void setLocalPort(int localPort) {
        this.localPort = localPort;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }
}

package ru.ak.sshclient;

import com.jcraft.jsch.*;
import ru.ak.model.Connection;
import ru.ak.model.Response;
import ru.ak.model.Tunnel;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.HashMap;
import java.util.UUID;

/**
 * Web-сервис для реализации интерфейса взаимодействия с SSH-сервером
 * @author akakushin
 */
@WebService(name = "SshClient", serviceName = "SshClient", portName = "SshClientPort")
public class SshClient {

    private HashMap<UUID, Session> sessions = new HashMap<>();

    /**
     * Получение сессии по уникальному идентификатору
     * @param uuid идентификатор
     * @return Session
     */
    private Session getSession(UUID uuid) {
        return sessions.get(uuid);
    }

    /**
     * Добавление канала в список
     * @param session Session
     * @return UUID
     */
    private UUID addSession(Session session) {
        UUID uuid = UUID.randomUUID();
        sessions.put(uuid, session);
        return uuid;
    }

    private Session openSession(Connection connection) throws JSchException {
        JSch ssh = new JSch();
        Session session = ssh.getSession(
                connection.getUser(), connection.getHost(), connection.getPort());

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        session.setPassword(connection.getPassword());
        session.connect();

        return session;
    }


    // Интерфейс взаимодействия

    @WebMethod
    public Response connect(@WebParam(name = "connection") Connection connection) {
        Response response = new Response();
        try {
            response.setResult(addSession(openSession(connection)));

        }  catch (JSchException ex) {
            response.setError(true);
            response.setDescription(ex.getLocalizedMessage());
        }

        return response;
    }

    @WebMethod
    public Response disconnect(String uuid) {
        Response response = new Response();
        Session session = getSession(UUID.fromString(uuid));
        if (session != null) {
            session.disconnect();
            response.setResult(true);

            sessions.remove(uuid);
        }
        return response;
    }

    // Туннели (Local, Remote)

    @WebMethod(operationName = "setPortForwarding")
    public Response setPortForwarding(
            @WebParam(name = "uuid") String uuid,
            @WebParam(name = "isLocal") boolean isLocal,
            @WebParam(name = "tunnel") Tunnel tunnel) {
        Response response = new Response();
        Session session = getSession(UUID.fromString(uuid));
        try {
            if (session != null) {
                if (isLocal) {
                    session.setPortForwardingL(
                            tunnel.getLocalPort(),
                            tunnel.getHost(),
                            tunnel.getRemotePort());

                } else {
                    session.setPortForwardingR(
                            tunnel.getRemotePort(),
                            tunnel.getHost(),
                            tunnel.getLocalPort());
                }

                response.setResult(true);
            }
        } catch (Exception ex) {
            response.setDescription(ex.getLocalizedMessage());
        }
        return response;
    }

    @WebMethod(operationName = "delPortForwarding")
    public Response delPortForwarding(
            @WebParam(name = "uuid") String uuid,
            @WebParam(name = "isLocal") boolean isLocal,
            @WebParam(name = "port") int port) {

        Response response = new Response();
        Session session = getSession(UUID.fromString(uuid));
        try {
            if (session != null) {
                if (isLocal) {
                    session.delPortForwardingL(port);
                } else {
                    session.delPortForwardingR(port);
                }

                response.setResult(true);
            }
        } catch (Exception ex) {
            response.setDescription(ex.getLocalizedMessage());
        }
        return response;
    }
}
package com.bsb.rpc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author: zengshuaizhi
 * @Date: 2019-05-24 17:18
 */
public class RpcExporter {

    static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static class ExporterTask implements Runnable {

        Socket client = null;

        public ExporterTask(Socket socket) {
            this.client = socket;
        }

        @Override
        public void run() {
            ObjectInputStream objectInputStream = null;
            ObjectOutputStream objectOutputStream = null;

            try {
                objectInputStream = new ObjectInputStream(client.getInputStream());
                String interfaceName = objectInputStream.readUTF();
                Class<?> service = Class.forName(interfaceName);
                String methodName = objectInputStream.readUTF();
                Class<?>[] parameterTypes = (Class<?>[]) objectInputStream.readObject();
                Object[] arguments = (Object[]) objectInputStream.readObject();
                Method method = service.getMethod(methodName, parameterTypes);
                Object result = method.invoke(service.newInstance(), arguments);
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                objectOutputStream.writeObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // ... 关闭资源
            }
        }
    }

    public static void exporter(String hostName, int port) throws IOException {

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(hostName, port));
        try {
            while (true) {
                executor.execute(new ExporterTask(serverSocket.accept()));
            }
        } finally {
            serverSocket.close();
        }

    }
}

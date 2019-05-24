package com.bsb.rpc.client;

import com.bsb.rpc.server.IEchoService;
import com.bsb.rpc.server.RpcExporter;
import com.bsb.rpc.server.impls.EchoServiceImpl;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @Author: zengshuaizhi
 * @Date: 2019-05-24 17:35
 */
public class RpcTest {


    public static void main(String[] args) {

        new Thread(() -> {
            try {
                RpcExporter.exporter("localhost", 8088);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        RpcImporter<IEchoService> importer = new RpcImporter<>();
        IEchoService echoService = importer.importer(EchoServiceImpl.class,
                new InetSocketAddress("localhost", 8088));
        System.out.println(echoService.echo("Are you ok?"));
    }
}

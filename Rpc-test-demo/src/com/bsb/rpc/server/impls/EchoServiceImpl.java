package com.bsb.rpc.server.impls;

import com.bsb.rpc.server.IEchoService;

/**
 * @Author: zengshuaizhi
 * @Date: 2019-05-24 17:15
 */
public class EchoServiceImpl implements IEchoService {


    @Override
    public String echo(String ping) {
        return ping != null ? ping + " --> Ok." : " ping is null";
    }
}

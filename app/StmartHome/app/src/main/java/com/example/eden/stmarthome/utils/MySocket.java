package com.example.eden.stmarthome.utils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Eden on 2016/7/19.
 */
public class MySocket extends Socket {
   public MySocket mSocket = null ;

   public MySocket(String url,int port) throws IOException {
       super(url,port);
    }

    public MySocket getSocket() {
        if (this.mSocket != null) return this.mSocket;
        try {
            this.mSocket = new MySocket(Constant.url, Constant.port);
           return  this.mSocket;
        }
        catch (UnknownHostException var2_1) {
            var2_1.printStackTrace();
            return this.mSocket;
        }
        catch (IOException var1_2) {
            var1_2.printStackTrace();
            return this.mSocket;
        }
    }
}

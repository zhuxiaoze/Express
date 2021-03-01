package server;

import bean.Express;
import dao.ExpressDao;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private final int PORT = 8888;
    ExpressDao expressDao;

    {
        try {
            expressDao = new ExpressDao();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    private ServerSocket server;

    public void start() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new ServerSocket(PORT);
                    System.out.println("服务器已准备就绪，当前服务端口：" + PORT);
                    while (true) {
                        Socket client = server.accept();
                        System.out.println("客户端" + client.hashCode() + "已连接");
                        //为每个客户端单开一个线程 来 处理请求
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //处理客户端的请求 并 反馈
                                handlerRequest(client);
                            }
                        }).start();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void handlerRequest(Socket client) {
        OutputStream os = null;
        ObjectOutputStream objOut = null;//序列化和反序列化的流对象
        InputStream is = null;
        ObjectInputStream objIn = null;

        try {
            is = client.getInputStream();
            os = client.getOutputStream();
            objOut = new ObjectOutputStream(os);
            objIn = new ObjectInputStream(is);
            //给请求打上标志位
            String flag = objIn.readUTF();
            System.out.println("用户请求类型：" + flag + "\t" + Thread.currentThread().getName());
            if ("findAll".equals(flag)) {
                ArrayList<Express> expresses = expressDao.getExpressList();
                if (expresses == null)
                    objOut.writeObject("当前暂无快递！");
                else
                    objOut.writeObject(expresses.toString());
            } else if ("add".equals(flag)) {
                Express express = (Express) objIn.readObject();
                Express e = expressDao.addExpress(express);
                objOut.writeObject(e);
            } else if ("update".equals(flag)) {
                String number = (String) objIn.readObject();//原快递单号
                int oldNum = Integer.parseInt(number);
                Express e = expressDao.findByNumber(oldNum);//原快递
                String newNum = (String) objIn.readObject();//新快递单号
                int num = Integer.parseInt(newNum);
                String company = (String) objIn.readObject();//新快递公司
                boolean b = expressDao.updateExpress(e,num,company);
                objOut.writeObject(b);
            } else if ("del".equals(flag)) {
                String number = (String) objIn.readObject();
                int oldNum = Integer.parseInt(number);
                Express e = expressDao.findByNumber(oldNum);
                boolean b = expressDao.removeExpress(e);
                objOut.writeObject(b);
            } else if ("pick".equals(flag)) {
                String number = (String) objIn.readObject();//传单号
                int oldNum = Integer.parseInt(number);
                Express e = expressDao.findByCode(oldNum);//查询此快递
                if (e == null) {
                    objOut.writeObject("暂无此快递");
                } else {
                    boolean a = expressDao.removeExpress(e);
                    objOut.writeObject(a);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objIn != null)
                    objIn.close();
                if (objOut != null)
                    objOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

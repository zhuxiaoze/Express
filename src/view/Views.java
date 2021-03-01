package view;

import bean.Express;
import io.zzax.jadeite.lang.Obj;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Views {

    private final int PORT = 8888;
    private Socket client;
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        OutputStream os;
        ObjectOutputStream objOut = null;
        InputStream is;
        ObjectInputStream objIn = null;

        try {
            while (true) {
                if (client == null || client.isClosed()) {
                    client = new Socket("127.0.0.1", PORT);
                    os = client.getOutputStream();
                    is = client.getInputStream();
                    objIn = new ObjectInputStream(is);
                    objOut = new ObjectOutputStream(os);
                    System.out.println("服务器连接成功");
                    switch (Menu(objOut, objIn)) {
                        case 0 :
                            return;
                        case 1:
                            dispatcher(objOut, objIn);
                            break;
                        case 2:
                            user(objOut, objIn);
                            break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (objIn != null)
                    objIn.close();
                if (objOut != null)
                    objOut.close();
                if (client != null)
                    client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void dispatcher(ObjectOutputStream objOut, ObjectInputStream objIn) {
        int dispatcherMenu;
        while (true) {
            dispatcherMenu = dispatcherMenu();
            try {
                switch (dispatcherMenu) {
                    case 0:
                        return;//退出
                    case 1:
                        addExpress(objOut, objIn);
                        return;
                    case 2:
                        delete(objOut, objIn);//快递删除
                        return;
                    case 3:
                        update(objOut, objIn);
                        return;//快递修改
                    case 4:
                        findAll(objOut, objIn);//查询所有
                        return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (objIn != null)
                        objIn.close();
                    if (objOut != null)
                        objOut.close();
                    if (client != null)
                        client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void user(ObjectOutputStream objOut, ObjectInputStream objIn) {
        int userMenu;
        while (true) {
            userMenu = userMenu();
            switch (userMenu) {
                case 0:
                    return;
                case 1:
                    pick(objOut,objIn);
                    break;
            }
            try {
                if (objIn != null)
                    objIn.close();
                if (objOut != null)
                    objOut.close();
                if (client != null)
                    client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }




    /**
     * 初始菜单
     *
     * @return
     */
    public int Menu(ObjectOutputStream objOut, ObjectInputStream objIn) {
        int num;
        while (true) {
            System.out.println("=====欢迎使用朱巡快递柜=====");
            System.out.println("1.快递员");
            System.out.println("2.用户");
            System.out.println("0.退出");
            String input = scanner.nextLine();
            try {
                num = Integer.parseInt(input);
                if (num > 2 || num < 0) {
                    System.out.println("输入有误");
                    continue;
                }
                return num;
            } catch (NumberFormatException e) {

            }
        }
    }

    /**
     * 用户菜单
     *
     * @return
     */
    public int userMenu() {
        int num;
        while (true) {
            System.out.println("1.取件");
            System.out.println("0.退出");
            String input = scanner.nextLine();
            try {
                num = Integer.parseInt(input);
                if (num > 1 || num < 0) {
                    System.out.println("输入有误");
                    continue;
                }
                return num;
            } catch (NumberFormatException e) {

            }
        }
    }

    /**
     * 快递员菜单
     *
     * @return
     */
    public int dispatcherMenu() {
        int num;
        while (true) {
            System.out.println("1.快递录入");
            System.out.println("2.快递删除");
            System.out.println("3.快递修改");
            System.out.println("4.显示所有快递");
            System.out.println("0.退出");
            String input = scanner.nextLine();
            try {
                num = Integer.parseInt(input);
                if (num > 4 || num < 0) {
                    System.out.println("输入有误");
                    continue;
                }
                return num;
            } catch (NumberFormatException e) {

            }
        }
    }

    /**
     * 4.查询所有
     * @param objOut
     * @param objIn
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void findAll(ObjectOutputStream objOut, ObjectInputStream objIn) throws IOException, ClassNotFoundException {
        System.out.println("===查询所有快递===");
        objOut.writeUTF("findAll");
        objOut.flush();
        Object object = objIn.readObject();
        if (object instanceof ArrayList) {
            ArrayList<Express> expresses = (ArrayList<Express>) object;
            for (Express express : expresses) {
                System.out.println(express);
            }
        } else if (object instanceof String) {
            System.out.println((String) object);
        } else {
            System.out.println(object);
        }
    }

    /**
     * 1.录入快递
     */
    public void addExpress(ObjectOutputStream objOut, ObjectInputStream objIn) {
        System.out.println("===快递录入===");
        Express express = new Express();
        System.out.println("请输入快递公司");
        String company = scanner.nextLine();
        express.setCompany(company);
        System.out.println("请输入快递单号");
        int num;
        try {
            num = Integer.parseInt(scanner.nextLine());
            express.setNumber(num);
            objOut.writeUTF("add");
            objOut.writeObject(express);
            objOut.flush();
            Object object = objIn.readObject();
            if (object instanceof Express) {
                System.out.println("快递录入成功，存储位置："+ ((Express) object).getLocation());
            } else if (object instanceof String) {
                System.out.println("添加失败，"+ object);
            } else {
                System.out.println("添加失败，"+ object);
            }
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * 2.删除快递
     */
    public void delete(ObjectOutputStream objOut, ObjectInputStream objIn) {
        System.out.println("===快递删除===");
        try {
            objOut.writeUTF("del");
            System.out.println("请输入需要删除的快递单号：");
            String s = scanner.nextLine();
            objOut.writeObject(s);//传单号
            Object object = objIn.readObject();
            if (object instanceof Boolean) {
                boolean a = (boolean) object;
                if (a) {
                    System.out.println("删除成功");
                }else
                    System.out.println("删除失败");
            } else if (object instanceof String) {
                System.out.println("删除失败，"+ object);
            } else {
                System.out.println("删除失败，"+ object);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * 3.修改快递
     * @param objOut
     * @param objIn
     */
    public void update(ObjectOutputStream objOut, ObjectInputStream objIn) {
        System.out.println("===快递修改===");
        try {
            objOut.writeUTF("update");
            System.out.println("请输入原快递单号");
            String oldNum = scanner.nextLine();
            objOut.writeObject(oldNum);
            System.out.println("请输入新快递单号");
            String newNum = scanner.nextLine();
            objOut.writeObject(newNum);
            System.out.println("请输入新快递公司");
            String company = scanner.nextLine();
            objOut.writeObject(company);
            objOut.flush();
            Object object = objIn.readObject();
            if (object instanceof Boolean) {
                boolean b = (boolean) object;
                if (b) {
                    System.out.println("修改成功");
                } else {
                    System.out.println("修改失败");
                }
            } else if (object instanceof String) {
                System.out.println("修改失败，"+ object);
            } else {
                System.out.println("修改失败，"+ object);
            }
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }
    /**
     * 取件
     */
    public void pick(ObjectOutputStream objOut, ObjectInputStream objIn) {
        System.out.println("===用户取件===");
        try {
            objOut.writeUTF("pick");
            System.out.println("请输入取件码：");
            String s = scanner.nextLine();
            objOut.writeObject(s);
            Object object = objIn.readObject();
            if (object instanceof Boolean) {
                boolean a = (boolean) object;
                if (a) {
                    System.out.println("取件成功");
                } else {
                    System.out.println("取件失败");
                }
            } else if (object instanceof String) {
                System.out.println("取件失败，"+ object);
            } else {
                System.out.println("取件失败，"+ object);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getExpressNumber() {
        int num; //接收用户输入的快递单号
        //获取快递单号
        while (true) {
            System.out.println("\n请输入快递单号:");
            try {
                num = Integer.parseInt(scanner.nextLine());
                return num;
            } catch (NumberFormatException e) {
                //若输入不合法导致转型失败,则重新输入
            }
        }
    }

    /**
     * 输出快递信息,必须有取件码
     */
    public void showExpress(Express express) {
        if (express == null)
            noExpress();
        else
            System.out.println("\n快递信息:快递单号: " + express.getNumber()
                    + " 快递公司: " + express.getCompany()
                    + " 取件码: " + express.getCode());
    }

    public void showAllExpress(Express[] expresses, int num) {
        if (expresses == null || num == 0) {
            lockerEmpty();
        } else {
            for (Express express : expresses) {
                if (express != null)
                    showExpress(express);
            }

        }
    }

    /**
     * 快递柜中已经有该快递,输出错误提示信息
     */
    public void hasExpress() {
        System.out.println("\n该快递已经被存入,请核对快递单号");
    }

    /**
     * 快递柜已经满载,输出错误提示信息
     */
    public void alreadyFull() {
        System.out.println("\n快递柜已满,无法存入快递");
    }

    /**
     * 没有该快递,输出错误提示信息
     */
    public void noExpress() {
        System.out.println("\n没有该快递,请核对输入的快递信息");
    }

    /**
     * 操作成功(删除,修改),输出提示信息
     */
    public void success() {
        System.out.println("\n操作成功");
    }

    /**
     * 操作失败(删除,修改),输出提示信息
     */
    public void failed() {
        System.out.println("\n操作失败");
    }

    public void lockerEmpty() {
        System.out.println("当前快递柜没有快递");
    }

    public void expressExist() {
        System.out.println("快递已存在");
    }

    public void expressUnknown() {
        System.out.println("输入有误，快递不存在，请重新输入");
    }

    public void errorRead() {
        System.out.println("文件损坏，无法读取");
    }

    public void errorWrite() {
        System.out.println("文件写入失败");
    }

    public int getNewNumber(int number) {
        System.out.println("请输入新的快递单号：");
        int NewNumber;
        m:
        while (true) {
            String s = scanner.nextLine();
            try {
                NewNumber = Integer.parseInt(s);
                if (NewNumber == number) {
                    System.out.println("你输入的是原单号，请重新输入:");
                    continue m;
                }
                return NewNumber;
            } catch (NumberFormatException e) {

            }
        }
    }

    public String getNewCompany() {
        System.out.println("请输入新的快递公司：");
        String newCompany;
        while (true) {
            newCompany = scanner.nextLine();
            return newCompany;
        }
    }

    public void printExistExpress() {
        System.out.println("您输入的快递已存在，请重新输入！");
    }

}

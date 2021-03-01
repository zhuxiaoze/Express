/*
package main;

import bean.Express;
import dao.ExpressDao;
import view.Views;

import java.io.IOException;

public class Main {

    static Views v = new Views();
    static ExpressDao expressDao;

    static {
        try {
             expressDao = new ExpressDao();
        }catch (IOException e ) {
            v.errorRead();
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int menu;
        m:while (true) {
            menu = v.Menu();
            switch (menu) {
                case 0: {
                    try {
                        expressDao.close();
                    }catch (IOException e ) {
                        v.errorWrite();
                        e.printStackTrace();
                    }
                    break m;
                }
                //快递员
                case 1:
                    depatcher();
                    break ;
                //用户
                case 2:
                    user();
                    break;
            }
        }
    }

    private static void depatcher() {
        int depatcherMenu;
        while (true) {
            depatcherMenu = v.depatcherMenu();
            switch (depatcherMenu) {
                case 0:
                    return;//返回上一级
                //快递录入
                case 1:
                    addExpress();
                    break ;
                //删除
                case 2:
                    delete();
                    break ;
                //更改
                case 3:
                    changeExpress();
                    break ;
                //查看所有
                case 4:{
                    Express[] expresses = expressDao.getExpresses();
                    int num = expressDao.getSize();
                    v.showAllExpress(expresses,num);
                }
            }
        }



    }

    */
/*private static void printAll() {
        Express[] expresses = expressDao.getExpresses();
        int num = expressDao.getSize();
        expressDao.printExpress();
    }*//*


    */
/**
     * 改
     *//*

    */
/*private static void update() {
        //拿到快递单号
        int num = v.delete();
        Express express = expressDao.findByNumber(num);
        if (express != null) {
            //找到快递，删除，添加
            Express newExpress = v.addExpress();
            try {
                expressDao.updateExpress(express,newExpress);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*//*

    public static void changeExpress() {
        //输出提示并获取快递单号
        int number = v.getExpressNumber();
        //查询是否有该快递
        Express express = expressDao.findByNumber(number);
        if (express == null) {
            //没有该快递
            v.noExpress();
        } else {
            //有该快递,则输出提示信息,并接收新的快递单号和快递公司
            //先印根据单号查询快递找到的快递
            expressDao.printExpress(express);
//            Express expressNew = v.addExpress();
            int newNumber = v.getNewNumber(express.getNumber());
            Express e1 = expressDao.findByNumber(newNumber);
            if (e1 == null) {
                String newCompany = v.getNewCompany();
                try {
                    if (expressDao.updateExpress(express,newNumber,newCompany)) {
                        //操作成功
//                    expressDao.addExpress(expressNew);
                        v.success();
                    }else{
                        //操作失败
                        v.failed();
                    }
                } catch (IOException e) {
                    v.errorWrite();
                    e.printStackTrace();
                }
            } else {
                //e1 != null
                v.printExistExpress();
            }
            //删除旧快递

        }

    }

    */
/**
     * 2.删
     *//*

    private static void delete() {
        //拿到快递单号
        int num = v.delete();
        Express express = expressDao.findByNumber(num);
        if ( express == null) {
            //没有快递
            v.expressUnknown();
        } else {
            try {
                expressDao.removeExpress(express);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    */
/**
     * 1.存
     *//*

    private static void addExpress() {
        //拿到单号和公司
        Express express = v.addExpress();
        //若未存
        if(expressDao.findByNumber(express.getNumber()) == null) {
            //存一下
            try {
                expressDao.addExpress(express);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //已存
            v.expressExist();
        }

    }

    */
/**
     * 取件
     *//*

    private static void user() {
        int code = v.pick();
        Express express = expressDao.findByCode(code);
        if(express != null) {
            try {
                expressDao.removeExpress(express);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("柜中暂无此快递");
        }

    }
}
*/

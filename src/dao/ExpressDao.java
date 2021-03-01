package dao;

import bean.Express;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class ExpressDao {
    private ArrayList<Express> expresses;
    private Location location;
//    private static int size = 0;

    private static String path = "a.txt";
    private ObjectOutputStream oos;

    /**
     * 文件信息初始化，显示快递柜中已存信息
     */
    public ExpressDao() throws IOException{
        File file = new File(path);
        //若文件存在，直接读取文件
        if(file.exists()) {
            try(ObjectInputStream ois = new ObjectInputStream((new FileInputStream(path)))) {
                expresses = (ArrayList<Express>)ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                expresses = new ArrayList<>();
                throw new IOException("无法从文件中读取集合");
            }
        } else
        //若文件不存在，创建新的集合
            expresses = new ArrayList<>();
    }

    Random random = new Random();

    /**
     * 随机生成取件码
     * @return
     */
    public int Code() {
        int code = random.nextInt(899999) + 100000;
        while(findByCode(code) != null) {
            break;
        }
        return code;
    }

    /**
     * 快递存储位置
     */
    public Location Location() {
        location = new Location();
        int i = -1;//行row
        int j = -1;//列col
        do{
            i = random.nextInt(10);
            j = random.nextInt(10);
            location.setCol(i);
            location.setRow(j);
        }
        //true-->被用了，不能用，再生成； false-->没被用，可以用
        while(locationIsUsed(location));
        return location;
    }

    /**
     * 位置是否被占用
     * @param location
     * @return
     */
    public boolean locationIsUsed(Location location) {
        for (Express express : expresses) {
            //已经占用了
            if(express.getLocation() == location) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据取件码找快递
     * @param code
     * @return
     */
    public Express findByCode(int code) {
        for (Express express: expresses) {
            //找到了
            if(express.getCode() == code) {
                return express;
            }
        }
        //没找到
        return null;
    }

    /**
     * 根据单号找快递
     * @param number
     * @return
     */
    public Express findByNumber(int number) {
        for (Express express:expresses) {
            if(express.getNumber() == number) {
                return express;
            }
        }
        return null;
    }

    /**
     * 1.添加快递
     * @param express
     * @return
     */
    public Express addExpress(Express express) throws IOException {
        if(express == null) {
            return null;
        }
        express.setCode(Code());
        express.setLocation(Location());
        boolean b = expresses.add(express);
        if(b) {
            this.write();
//            size++;
        }
        return express;
    }

    /**
     * 输出流关闭
     * @throws IOException
     */
    public void close() throws IOException {
        this.write();
        oos.close();
    }

    /**
     * 写文件
     * @throws IOException
     */
    private void write() throws IOException {
        if(oos == null) {
            oos = new ObjectOutputStream(new FileOutputStream(path));
        }
        oos.writeObject(expresses);
    }

    /**
     * 2.删除快递
     *   //  取件
     * @param express
     * @return
     * @throws IOException
     */
    public boolean removeExpress(Express express) throws IOException {
        boolean b = expresses.remove(express);
        if(b) {
            this.write();
//            size--;
        }
        return b;
    }

    /**
     * 3.更新快递
     * @param e 旧快递
     * @param newNumber 新单号
     * @param newCompany 新公司  不改变取件码
     * @return
     * @throws IOException
     */
    public boolean updateExpress(Express e, int newNumber, String newCompany) throws IOException {
        e.setNumber(newNumber);
        e.setCompany(newCompany);
        return true;
    }

    /**
     * 4.显示快递
     */
    /*public void printExpress() {
        for (Express express: expresses) {
            express.toString();
        }
    }*/

    public Express[] getExpresses() {
        return expresses.toArray(new Express[this.getSize()]);
    }

    public int getSize() {
        return expresses.size();
    }

    public void printExpress(Express e) {
        System.out.println("当前查询快递：快递单号：" + e.getNumber() + "，快递公司:" + e.getCompany() +
                "，取件码：" + e.getCode());
    }

    public ArrayList<Express> getExpressList() {
        return expresses;
    }

    /*public String getExpressList(Express e) {
        return "当前查询快递：快递单号：" + e.getNumber() + "，快递公司:" + e.getCompany() +
                "，取件码：" + e.getCode();
    }*/




}

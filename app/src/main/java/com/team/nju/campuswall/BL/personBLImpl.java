package com.team.nju.campuswall.BL;

import com.team.nju.campuswall.BLService.personBLService;
import com.team.nju.campuswall.Bean.personBean;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Created by user on 2016/7/8.
 */
public class personBLImpl implements personBLService{
    @Override
    public boolean login(String account, String password) {
        boolean result = false;
        try {
            Socket socket = new Socket("121.42.54.202",12345);
            //建立输入流
            ObjectOutputStream oos = new ObjectOutputStream(socket
                    .getOutputStream());
            InputStream is = socket.getInputStream();
            Scanner in = new Scanner(is);
            personBean user = new personBean();
            user.setUsername("account");
            user.setPassword("password");
            user.setCmd("search");
            //输入对象, 一定要flush（）
            oos.writeObject(user);
            oos.flush();
            oos.close();
            if(in.hasNext())
                result=new Boolean(in.nextLine());
            is.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean signUp(personBean pb) {
        boolean result = false;
        try {
            Socket socket = new Socket("121.42.54.202",12345);
            //建立输入流
            ObjectOutputStream oos = new ObjectOutputStream(socket
                    .getOutputStream());
            InputStream is = socket.getInputStream();
            Scanner in = new Scanner(is);
            personBean user = new personBean();
            user.setUsername("account");
            user.setPassword("password");
            user.setSchool("nju");
            user.setCmd("add");
            //输入对象, 一定要flush（）
            oos.writeObject(user);
            System.out.print("往数据库传输ing");
            oos.flush();
            oos.close();
            if(in.hasNext())
            result=new Boolean(in.nextLine());
            is.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

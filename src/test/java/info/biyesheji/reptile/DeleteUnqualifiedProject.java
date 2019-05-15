package info.biyesheji.reptile;


import org.junit.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class DeleteUnqualifiedProject {

    @Test
    public void delete() throws ClassNotFoundException, SQLException {
        String URL = "jdbc:mysql://176.122.157.201:3306/sheji?useUnicode=true&characterEncoding=utf8&serverTimezone=UTC&useSSL=false";
        String USER = "root";
        String PASSWORD = "root123";
        //1.加载驱动程序
        Class.forName("com.mysql.jdbc.Driver");
        //2.获得数据库链接
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        //3.通过数据库的连接操作数据库，实现增删改查（使用Statement类）
        Statement st = conn.createStatement();
        String sql = "update t_reptile_log set status = 3 where id = ";
        st.executeUpdate(sql);


        st.close();
        conn.close();
    }

    @Test
    public void test1() throws IOException, URISyntaxException {
        File gitProject = new File("D:\\gitTest");
        File[] files = gitProject.listFiles();
        for (File file : files) {
            int gitid = 0;
            boolean isDelete = true;
            for (File file1 : file.listFiles()) {
                for (File file2 : file1.listFiles()) {
                    if (file2.getName().equalsIgnoreCase("pom.xml"))
                        isDelete = false;
                }
                for (File file2 : file1.listFiles()) {
                    boolean isDelete1 = true;
                    if (file2.getName().equalsIgnoreCase(".git")) {
                        BufferedReader reader = new BufferedReader(new FileReader(file2.getAbsolutePath() + "\\FETCH_HEAD"));
                        String str = reader.readLine();
                        String gitUrl = str.substring(str.indexOf("https://github.com"), str.indexOf(".git") + 4);
                        gitid = gitUrl.hashCode();

                    }
                    if (file2.isDirectory() && !file2.getName().startsWith(".")) {
                        for (File file3 : file2.listFiles()) {
                            if (file3.getName().equalsIgnoreCase("pom.xml")){
                                isDelete = false;
                                isDelete1 = false;
                            }
                        }
                    }
                    if (isDelete1 && isDelete && !file2.getName().startsWith(".")) {
                        System.err.println(file2.getAbsolutePath());
                        deleteAll(file2);
                    }
                }
            }
            if (isDelete) {
                if (!deleteAll(file)){
                    File newFile = new File(file.getParent() + "\\delete-"+file.getName());
                    newFile.mkdir();
                    Path newPath = Paths.get(newFile.getAbsolutePath());
                    Path orderPath = Paths.get(file.getAbsolutePath());
                    Files.move(newPath,orderPath,REPLACE_EXISTING);
                    file.renameTo(newFile);
                }
            }
        }
    }

    private boolean deleteAll(File file) throws URISyntaxException, IOException {
        if(!file.exists()){
            return false;
        }
        if(file.isFile()){
            return file.delete();
        }
        File[] files = file.listFiles();
        for (File f : files) {
            if(f.isFile()){
                if(!f.delete())
                    return false;

            }else{
                if(!this.deleteAll(f)){
                    return false;
                }
            }
        }
        return file.delete();
    }

    @Test
    public void test2() {
        String string = "5c061e446db32927459bea8e75f086fef07e4621\tnot-for-merge\tbranch 'master' of https://github.com/2018CC/ssh.git";
        System.err.println(string.length());
        System.err.println(string.indexOf("https://github.com"));
        System.err.println(string.indexOf(".git"));
        System.err.println(string.substring(string.indexOf("https://github.com"), string.indexOf(".git") + 4));
    }
}

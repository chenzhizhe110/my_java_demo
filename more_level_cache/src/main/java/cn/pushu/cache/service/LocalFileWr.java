package cn.pushu.cache.service;

import cn.pushu.cache.entity.PushTimeBar;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenzhizhe<br><br>
 * <b>title:</b> LocalFileWr<br>
 * <b>projectName:</b> my_java_demo<br>
 * <b>description:</b> 一个测试本地文件读写的示例 <br>
 * <b>date:</b> 2024/4/2213:52
 */

@Service
public class LocalFileWr {

    public static void writeKline(List<PushTimeBar>  timeBars) throws IOException {
        // Write remaining 2GB of data to a local file
        File file = ResourceUtils.getFile("/data/timeBars.txt");
        List<byte[]> dataList=new ArrayList<>();
        for (PushTimeBar data : timeBars) {
            dataList.add(data.toByte());
          // FileUtils.write(file, data.toString(), "UTF-8", true);
        }
        FileUtils.writeByteArrayToFile(file,addBytes(dataList),true);
    }

    public static List<PushTimeBar> readKline() throws IOException {
        // Try to read data from cache
        // If cache is empty, read from file
            File file = ResourceUtils.getFile("/data/timeBars.txt");
            //List<String> lines = FileUtils.readLines(file, "UTF-8");
          byte[] bytes = FileUtils.readFileToByteArray(file);
          return PushTimeBar.toData(bytes);

        // Convert lines to Data objects
            // Assume there is a method to parse String to Data object

    }

    public static byte[] addBytes(List<byte[]> data) {
        // 计算需要创建的 byte[] 数组的长度
        int length = 0;
        for (byte[] bytes : data) {
            length += bytes.length;
        }
        // 创建 byte[] 数组
        byte[] result = new byte[length];

        // 将 List<byte[]> 中的数据添加到 byte[] 中
        int position = 0;
        for (byte[] bytes : data) {
            System.arraycopy(bytes, 0, result, position, bytes.length);
            position += bytes.length;
        }
        return result;
    }

    //写10000 总共用时:20981
//写1000000 总共用时:2040410   批量处理:总共用时:2811
//读100000 总共用时 148ms
    public static void main(String[] args)throws IOException  {
        long s=System.currentTimeMillis();
//
//        List<PushTimeBar>  timeBars=new ArrayList<PushTimeBar>();
//        for(int i=0;i<=1000000;i++){
//            PushTimeBar timeBar=new PushTimeBar("600519.SS_"+i,(byte)2,1500,1600,
//                    1700,1800,2000,202404191030L);
//            timeBars.add(timeBar);
//        }
//       writeKline(timeBars);


       List<PushTimeBar> readKlines = readKline();
      //System.out.println("数据总量:"+readKlines.size());
        readKlines.forEach(x->System.out.println(x));

        System.out.println("总共用时:"+(System.currentTimeMillis()-s));
    }
}

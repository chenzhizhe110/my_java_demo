package cn.pushu.cache.entity;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author chenzhizhe
 * @title: KlineBars
 * @projectName pushu_2022
 * @description: k线行情推送的数据结构
 * @date 2022/1/21 13:18
 */

public class PushTimeBar implements Serializable, Comparable<Long> {


    private Integer prodCodeInt;  //合约号 用于int 类型比如股票
    private String prodCode;  //合约号，客户展示用
    private byte period;    //周期
    private Byte exchangeId;  //交易所id

    private float open;//
    private  float high;//
    private float low;//
    private float close;//
    private long amount;// 成交量r() {

    private long minTime;  //时间

    public PushTimeBar() {
    }

    public PushTimeBar(String prodCode, Byte period, float high, float open, float low,
                       float close, long amount, long minTime) {
        this.prodCode=prodCode;
        this.period=period;
        this.high = high;
        this.open = open;
        this.low = low;
        this.close = close;
        this.amount = amount;
        this.minTime = minTime;
    }

    public Integer getProdCodeInt() {
        return prodCodeInt;
    }

    public void setProdCodeInt(Integer prodCodeInt) {
        this.prodCodeInt = prodCodeInt;
    }

    public String getProdCode() {
        if(prodCode==null){
            return  String.valueOf(prodCodeInt);
        }
        return prodCode;
    }

    /**
     * <p>
       description: 根据字段属性名获取字段的值 <br>
     　 author chenzhizhe<br>
     　 date 2022/8/15 13:29 <br>
     *<p/>
       * @param fieldName  字段属性名
     　 @return float
    　 @throws
    　*/
    public float getFieldValue(String fieldName) {
        if(fieldName==null) {
            return this.getClose();
        }
        if("close".equals(fieldName)) {
            return this.getClose();
        }else if("high".equals(fieldName)) {
            return this.getHigh();
        }else if("low".equals(fieldName)) {
            return this.getLow();
        }else if("open".equals(fieldName)) {
            return this.getOpen();
        }else if("amount".equals(fieldName)) {
            return (float)this.getAmount();
        }else {
            return this.getClose();
        }
    }



    public void setProdCode(String prodCode) {
        this.prodCode = prodCode;
    }

    public byte getPeriod() {
        return period;
    }

    public void setPeriod(byte period) {
        this.period = period;
    }

    public Byte getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Byte exchangeId) {
        this.exchangeId = exchangeId;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public long getMinTime() {
        return minTime;
    }

    public void setMinTime(long minTime) {
        this.minTime = minTime;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PushTimeBar{");
        sb.append(", prodCode='").append(getProdCode()).append('\'');
        sb.append(", period=").append(period);
        sb.append(", open=").append(open);
        sb.append(", high=").append(high);
        sb.append(", low=").append(low);
        sb.append(", close=").append(close);
        sb.append(", amount=").append(amount);
        sb.append(", minTime=").append(minTime);
        sb.append('}');
        return sb.toString();
    }

    public byte[] toByte() {
        byte[]  prodCodeBytes= prodCode.getBytes();
        byte prodCodeSize= (byte) prodCodeBytes.length;
        ByteBuffer buffer = ByteBuffer.allocate(35+prodCodeSize); // Allocate buffer with fixed size
        // Convert each field to byte representation and put into buffer
        buffer.put(prodCodeSize); //1
        buffer.put(prodCodeBytes); // prodCodeSize
        buffer.put(period); // 1 byte
        buffer.put(exchangeId != null ? exchangeId : 0); // 1 byte
        buffer.putFloat(open); // 4 bytes
        buffer.putFloat(high); // 4 bytes
        buffer.putFloat(low); // 4 bytes
        buffer.putFloat(close); // 4 bytes
        buffer.putLong(amount); // 8 bytes
        buffer.putLong(minTime); // 8 bytes
        return buffer.array();
    }

    public static  List<PushTimeBar> toData(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        // Read data from buffer and create Data object
        List<PushTimeBar>  timeBars=new ArrayList<>();
        while (buffer.hasRemaining()) {
            PushTimeBar data = new PushTimeBar();
            byte prodCodeSize = buffer.get();
            byte[] prodCodeBytes = new byte[prodCodeSize];
            buffer.get(prodCodeBytes);
            data.setProdCode(new String(prodCodeBytes));
            data.setPeriod(buffer.get());
            data.setExchangeId(buffer.get());
            data.setOpen(buffer.getFloat());
            data.setHigh(buffer.getFloat());
            data.setLow(buffer.getFloat());
            data.setClose(buffer.getFloat());
            data.setAmount(buffer.getLong());
            data.setMinTime(buffer.getLong());
            timeBars.add(data);
        }
        return timeBars;
    }



    public int compareTo(Long o) {
        return 0;
    }
}

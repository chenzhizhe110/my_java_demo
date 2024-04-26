package cn.pushu.cache.entity;

import java.util.List;

/**
 * 脚本处理器数据源参数
 * @author 48928
 *
 */
public class ScriptInvokDataSourceParam implements Cloneable {


	/**
	 * 交易所Id用于区分不同的市场
	 */
	private byte exchangeId;
	/**
	 * 合约(股票，期货 。。等的代码)如果需要多合合约
	 * 中间用逗号分隔。示例  600000.SS,600004.SS
	 */
	private String prodCode;

	/**
	 * 周期
	 */
	private byte period=-1;
	
	/**
	 * 开始时间
	 */
	private String startDate;
	/**
	 * 结束时间
	 */
	private String endDate;
	 /**
	  * 从开始时间向前取多个数据的数量
	 */
	private int count=-1;

	/**
	 * 数据源id
	 */
	private int dataSourceId;
	 /**
	  * 数据源类型
	  */
	private String dataSourceType;
	/**
	 * 数据源的名称
	 */
	private String dataSourceName;
	/**
	 * 数据源的 http的地址
	 */
	private List<String> httpUrl;
	 /**
	  * 数据源的字段名称
	  */
	private String fieldName;

	/**
	 * 信号时间
	 */
	private long signalTime;

	public ScriptInvokDataSourceParam() {
	}

	public ScriptInvokDataSourceParam(String prodCode, byte period) {
		this.prodCode = prodCode;
		this.period = period;
	}

	public String getProdCode() {
		return prodCode;
	}
	public void setProdCode(String prodCode) {
		this.prodCode = prodCode;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public int getDataSourceId() {
		return dataSourceId;
	}
	public void setDataSourceId(int dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	public String getDataSourceType() {
		return dataSourceType;
	}
	public void setDataSourceType(String dataSourceType) {
		this.dataSourceType = dataSourceType;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}

	public List<String> getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(List<String> httpUrl) {
		this.httpUrl = httpUrl;
	}

	public byte getPeriod() {
		return period;
	}
	public void setPeriod(byte period) {
		this.period = period;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public byte getExchangeId() {
		return exchangeId;
	}

	public void setExchangeId(byte exchangeId) {
		this.exchangeId = exchangeId;
	}

	public long getSignalTime() {
		return signalTime;
	}

	public void setSignalTime(long signalTime) {
		this.signalTime = signalTime;
	}


	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ScriptInvokDataSourceParam [prodCode=");
		builder.append(prodCode);
		builder.append(", period=");
		builder.append(period);
		builder.append(", startDate=");
		builder.append(startDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append(", count=");
		builder.append(count);
		builder.append(", dataSourceId=");
		builder.append(dataSourceId);
		builder.append(", dataSourceType=");
		builder.append(dataSourceType);
		builder.append(", dataSourceName=");
		builder.append(dataSourceName);
		builder.append(", httpUrl=");
		builder.append(httpUrl);
		builder.append(", fieldName=");
		builder.append(fieldName);
		builder.append("]");
		return builder.toString();
	}
}

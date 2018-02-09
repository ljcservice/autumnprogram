package com.ts.util;

class ExcelData{
	private String value;//单元格的值
	private int colSpan = 1;//单元格跨几列
	private int rowSpan = 1;//单元格跨几行
	private boolean alignCenter;//单元格是否居中，默认不居中，如果选择是，则水平和上下都居中
	public boolean isAlignCenter() {
		return alignCenter;
	}
	public void setAlignCenter(boolean alignCenter) {
		this.alignCenter = alignCenter;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getColSpan() {
		return colSpan;
	}
	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}
	public int getRowSpan() {
		return rowSpan;
	}
	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}
}
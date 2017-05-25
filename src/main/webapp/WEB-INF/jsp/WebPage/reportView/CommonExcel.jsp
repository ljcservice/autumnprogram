<%@ page contentType="text/html;charset=utf-8"%>
<%!
	/**
	  * 计算列名
	  * <p>
	  * @param col	
	  * @return strRe 
	 */
	public String getColName(int col){
		String str [] = new String [] {
			"A", "B", "C", "D", "E"
			, "F", "G", "H", "I", "J"
			, "K", "L", "M", "N", "O"
			, "P", "Q", "R", "S", "T"
			, "U", "V", "W", "X", "Y"
			, "Z"};
		String strRe = "";
		int temCol = col;
		while(temCol > 26){
	        int i = (temCol % 26);
	        if(i > 0){
	            i = i - 1;
	        }
	        else{
	            i = 25;
	        }
			strRe = str[i] + strRe;
			temCol = (int)((temCol - i + 1) / 26);
		}
		strRe = str[temCol - 1] + strRe;
		return strRe;
	}
	/**
	  *设置位置
	 */
	public String getCell(int col, int row){
	    return getColName(col) + row;
	}
%>
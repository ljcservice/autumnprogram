package com.hitzd.his.Utils;


import javax.servlet.http.HttpServletRequest;
/**
 * 
 * @author tyl
 * 
 */
public class CompositionSQL {
	/**
	 * 执行更新操作，需要提供query对象、request对象、表明、字段列表
	 * 
	 * @param query
	 * @param request
	 * @param table
	 * @param fileList
	 * @return 更新状态
	 */
	public static String update(HttpServletRequest request, String tableName,
			String fileList, String identity) {

		StringBuilder sb = new StringBuilder(" update ");
		sb.append(tableName);
		sb.append(" set ");
		String id = request.getParameter(identity);
		String values = null;
		if (fileList.contains(",")) {
			String[] fileArray = fileList.split(",");
			for (int i = 0; i < fileArray.length; i++) {
				values = request.getParameter(fileArray[i].trim());
				if (i == fileArray.length - 1)
					sb.append(fileArray[i]).append("='")
							.append(getValus(values)).append("' where ")
							.append(identity).append("='").append(id)
							.append("'");
				else
					sb.append(fileArray[i]).append("='")
							.append(getValus(values)).append("',");
			}
		} else {
			values = request.getParameter(fileList);
			sb.append(fileList).append("='").append(values).append("' where ")
					.append(identity).append("='").append(id).append("'");
		}
		return sb.toString();
	}

	/**
	 * 执行插入操作，需要提供query对象、request对象、表明、字段列表(不包含ID)、序列名
	 * 
	 * @param query
	 * @param request
	 * @param table
	 * @param fileList
	 * @param xuLeiName
	 * @return 插入状态
	 */
	public static String insert(HttpServletRequest request, String table,
			String fileList) {
		StringBuilder sb = new StringBuilder("insert into  ");
		sb.append(table);
		sb.append(" (");
		sb.append(fileList);
		sb.append(") values(");
		String values = null;
		if (fileList.contains(",")) {
			String[] fileArray = fileList.split(",");
			for (int i = 0; i < fileArray.length; i++) {
				values = request.getParameter(fileArray[i].trim());
				if (i == fileArray.length - 1)
					sb.append("'").append(values).append("')");
				else
					sb.append("'").append(values).append("',");
			}
		} else {
			values = request.getParameter(fileList.trim());
			sb.append("'").append(values).append("')");
		}
		return sb.toString();
	}

	/***
	 * 根据组成查询语句、提供表明、request、字段列表、条件字符串数组
	 * 
	 * @param request
	 * @param tableName
	 * @param fieldsList
	 * @param idName
	 * @return 查询所用字符串
	 */
	public static String query(HttpServletRequest request, String tableName,String fieldsList, String fieldsWheres) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ").append(fieldsList).append(" from ").append(tableName).append(" where 1=1 ");
		if (fieldsWheres.contains(",")) {
			String wid = null;
			String[] w = fieldsWheres.split(",");
			for (String s : w) {
				wid = request.getParameter("Query" + s);
				if (!"".equals(wid) && wid != null) {
					sb.append(" and ").append(s).append("='").append(wid).append("'");
				}
			}
		} else {
			sb.append(" and ").append(fieldsWheres).append("='").append(request.getParameter("Query" + fieldsWheres)).append("'");
		}
		return sb.toString();
	}

	/**
	 * 根据ID组成删除语句，提供表明、request、主键名字
	 * 
	 * @param query
	 * @param request
	 * @param table
	 * @return 删除所用字符串
	 */
	public static String delete(HttpServletRequest request, String tableName,
			String idName) {
		String id = request.getParameter(idName);
		StringBuilder sb = new StringBuilder();
		if (!"".equals(id) && id != null) {
			sb.append("delete ").append(tableName).append(" where ")
					.append(idName).append("='").append(id).append("'");
		}
		return sb.toString();
	}

	public static String getValus(String values) {
		if (values == null || "".equals(values)) {
			return "";
		}
		return values;
	}

	/***
	 * 根据表identity的值 组成sql语句，例如identity=userid 则 where userid=页面参数userid的值
	 * 
	 * @param request
	 * @param tableName
	 * @param fieldsList
	 * @param fieldsWheres
	 * @return
	 */
	public static String modify(HttpServletRequest request, String tableName,
			String fieldsList, String identity) {
		StringBuilder sb = new StringBuilder();
		sb.append("select ").append(fieldsList).append(" from ")
				.append(tableName).append(" where 1=1 ");
		sb.append(" and ").append(identity).append("='")
				.append(request.getParameter(identity)).append("'");
		return sb.toString();
	}
}

package com.westar.core.dao;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.lob.LobHandler;

import com.alibaba.fastjson.JSONObject;
import com.westar.base.annotation.Filed;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.StringUtil;
import com.westar.core.web.PaginationContext;

public class BaseDao {

	private static Logger logger = Logger.getLogger(BaseDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private LobHandler lobHandler;

	public LobHandler getLobHandler() {
		return lobHandler;
	}

	public void setLobHandler(LobHandler lobHandler) {
		this.lobHandler = lobHandler;
	}

	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	protected JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	protected NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		if (this.namedParameterJdbcTemplate == null) {
			namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
		}
		return namedParameterJdbcTemplate;
	}

	/**
	 * 拼装插入Sql 语句
	 * @param tableName
	 * @param fields
	 * @return
	 */
	private String createInsertSql(String tableName, String[] fields) {
		StringBuffer sql = new StringBuffer();
		sql.append("insert into ").append(tableName).append(" (");
		for (int i = 0; i < fields.length; i++) {
			sql.append(fields[i]);
			if (i != fields.length - 1) {
				sql.append(",");
			}
		}
		sql.append(") values (");
		for (int i = 0; i < fields.length; i++) {
			sql.append(":").append(fields[i]);
			if (i != fields.length - 1) {
				sql.append(",");
			}
		}
		sql.append(")");
		return sql.toString();
	}

	/**
	 * 插入数据
	 * @param tableName
	 * @param fields
	 * @param object
	 * @return
	 */
	public Integer add(final String tableName, final String[] fields, Object object) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(object);
		this.getNamedParameterJdbcTemplate().update(createInsertSql(tableName, fields), beanPropertySqlParameterSource, keyHolder, new String[] { "id" });
		return keyHolder.getKey().intValue();
	}

	/**
	 * 插入数据
	 * @param object
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Integer add(Object object) {
		// 分析对象的属性和名称（对应表中的字段和表名）
		List<String> listFields = new ArrayList<String>();
		Class c = object.getClass();
		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {
			if (field.getAnnotation(Filed.class) != null) {
				listFields.add(field.getName());
			}
		}
		String tableName = c.getSimpleName().toLowerCase();
		String[] fieldsStrArray = listFields.toArray(new String[0]);
		return add(tableName, fieldsStrArray, object);
	}

	/**
	 * 拼装删除Sql 语句
	 * @param tableName
	 * @param fields
	 * @return
	 */
	private String createDeleteSql(String tableName, String[] fields) {
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(tableName).append(" where 1=1 ");
		for (int i = 0; i < fields.length; i++) {
			sql.append(" and ").append(fields[i]).append("=?");
		}
		return sql.toString();
	}

	/**
	 * 根据id删除数据
	 * @param tableName
	 * @param id
	 */
	public void delById(String tableName, Integer id) {
		this.getJdbcTemplate().update(createDeleteSql(tableName, new String[] { "id" }), id);
	}

	@SuppressWarnings({"rawtypes" })
	public void delById(Class c, Integer id) {
		String tableName = c.getSimpleName().toLowerCase();
		this.delById(tableName, id);
	}

	/**
	 * 根据id批量删除数据
	 * @param tableName
	 * @param ids
	 */
	public void delById(String tableName, Integer[] ids) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (int i = 0; i < ids.length; i++) {
			Object[] cxslit = new Object[] { ids[i] };
			batchArgs.add(cxslit);
		}
		this.getJdbcTemplate().batchUpdate(createDeleteSql(tableName, new String[] { "id" }), batchArgs);
	}

	@SuppressWarnings("rawtypes")
	public void delById(Class c, Integer[] ids) {
		String tableName = c.getSimpleName().toLowerCase();
		this.delById(tableName, ids);
	}

	/**
	 * 根据属性删除数据，匹配方式为相等
	 * @param tableName
	 * @param field
	 * @param fieldValue
	 */
	public void delByField(String tableName, String field, Object fieldValue) {
		this.getJdbcTemplate().update(createDeleteSql(tableName, new String[] { field }), fieldValue);
	}

	public void delByField(String tableName, String[] fields, Object[] fieldValues) {
		this.getJdbcTemplate().update(createDeleteSql(tableName, fields), fieldValues);
	}
	/**
	 * 批量删除
	 * @param tableName
	 * @param fields
	 * @param fieldValues
	 */
	public void delBatchByField(String tableName, String[] fields, Object[] fieldValues) {
		List<Object[]> batchArgs = new ArrayList<Object[]>();
		for (int i = 0; i < fieldValues.length; i++) {
			Object[] cxslit = new Object[] { fieldValues[i] };
			batchArgs.add(cxslit);
		}
		this.getJdbcTemplate().batchUpdate(createDeleteSql(tableName, fields), batchArgs);
	}

	/**
	 * 删除整个表
	 * @param tableName 要删除的表的表名
	 */
	public void delTable(String tableName) {
		this.getJdbcTemplate().update("delete from " + tableName);
	}

	/**
	 * 拼装更新Sql 语句
	 * @param tableName
	 * @param fields
	 * @return
	 */
	private String createUpdateSql(String tableName, String[] fields) {
		StringBuffer sql = new StringBuffer();
		sql.append("update ").append(tableName).append(" set ");
		for (int i = 0; i < fields.length; i++) {
			sql.append(fields[i]).append("=:").append(fields[i]);
			if (i != fields.length - 1) {
				sql.append(",");
			}
		}
		sql.append(" where id=:id");
		return sql.toString();
	}

	/**
	 * 根据sql更新
	 * @param sql
	 * @param object
	 */
	protected void update(StringBuffer sql, Object object) {
		BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(object);
		this.getNamedParameterJdbcTemplate().update(sql.toString(), beanPropertySqlParameterSource);
	}

	/**
	 * 根据id更新数据
	 * @param tableName
	 * @param fields
	 * @param object
	 */
	public void update(String tableName, String[] fields, Object object) {
		String sql = createUpdateSql(tableName, fields);
		BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(object);
		this.getNamedParameterJdbcTemplate().update(sql, beanPropertySqlParameterSource);
	}
	/**
	 * 根据id更新数据
	 * @param object
	 */
	public void update(String sql,Object object) {
		BeanPropertySqlParameterSource beanPropertySqlParameterSource = new BeanPropertySqlParameterSource(object);
		this.getNamedParameterJdbcTemplate().update(sql, beanPropertySqlParameterSource);
	}

	/**
	 * 根据id更新数据
	 * @param object
	 */
	@SuppressWarnings("rawtypes")
	public void update(Object object) {
		// 分析对象的属性和名称（对应表中的字段和表名）
		List<String> listFields = new ArrayList<String>();
		Class c = object.getClass();
		Field[] fields = c.getDeclaredFields();
		for (Field field : fields) {
			if (field.getAnnotation(Filed.class) != null) {
				field.setAccessible(true);
				try {
					if (field.get(object) != null) {
						listFields.add(field.getName());
					}
				} catch (Exception e) {
					logger.error("插入操作时，获取属性值失败", e);
				}
			}
		}
		String tableName = c.getSimpleName().toLowerCase();
		String[] fieldsStrArray = listFields.toArray(new String[0]);
		this.update(tableName, fieldsStrArray, object);
	}

	/**
	 * 拼装分页Sql 查询分页数据（SQL SERVER）
	 * @param sql
	 * @param orderby
	 * @return
	 */
	@SuppressWarnings("unused")
	private String createPaginationSql(String sql, String orderby) {
		sql = sql.toLowerCase();
		int cutPoint = sql.indexOf("from");
		String head = sql.substring(0, cutPoint).trim();
		String tail = sql.substring(cutPoint).trim();
		StringBuffer pagedQueryString = new StringBuffer();
		pagedQueryString.append("select *  from ( ");
		pagedQueryString.append(head).append(",row_number() over (").append("order by ").append(orderby).append(") as row_number ").append(tail);
		pagedQueryString.append(" ) t where row_number>").append(PaginationContext.getOffset()).append(" and row_number<=").append(PaginationContext.getOffset() + PaginationContext.getPageSize());
		return pagedQueryString.toString();
	}

	/**
	 * 拼装分页Sql 查询分页数据（Oracle）
	 * @param sql
	 * @param orderby
	 * @return
	 */
	private String createOraclePaginationSql(String sql, String orderby) {
		StringBuffer pagedQueryString = new StringBuffer();
		pagedQueryString.append("select outer.*  from (");
		pagedQueryString.append("select inner.*,rownum as rowno from(");
		pagedQueryString.append(sql).append(null==orderby?"":" order by "+orderby);
		pagedQueryString.append(") inner where rownum <=").append(PaginationContext.getOffset() + PaginationContext.getPageSize());
		pagedQueryString.append(") outer where outer.rowno>").append(PaginationContext.getOffset());
		return pagedQueryString.toString();
	}

	/**
	 * 拼装分页Sql 查询总记录数
	 * @param sql
	 * @return
	 */
	private String createCountSql(String sql) {
//		int cutPoint = sql.indexOf("from");
//		String tail = sql.substring(cutPoint).trim();
//		String countQueryString = "select  count(*) " + tail;
		String countQueryString = "select  count(*) from ( "+sql+" )";
		
		return countQueryString;
	}

	/**
	 * 分页查询
	 * @param sql
	 * @param orderby
	 * @param c
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List pagedQuery(String sql, String orderby, Object[] args, Class c) {
		int totalCount = this.countQuery(createCountSql(sql), args);
		if (totalCount < 1) {
			return new ArrayList();
		}
		RowMapper rowMapper = ParameterizedBeanPropertyRowMapper.newInstance(c);
		List data = this.getJdbcTemplate().query(this.createOraclePaginationSql(sql, orderby), args, rowMapper);
		// 将查询到的记录总数保存到LocalThread中
		PaginationContext.setTotalCount(totalCount);
		return data;
	}
	
	/**
	 *分页茶尊，自己写记录查询语句 
	 * @param sql
	 * @param sqlCount
	 * @param orderby
	 * @param args
	 * @param c
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List pagedQuery(String sql, String sqlCount, String orderby, Object[] args, Class c) {
		int totalCount = this.countQuery(sqlCount, args);
		if (totalCount < 1) {
			return new ArrayList();
		}
		RowMapper rowMapper = ParameterizedBeanPropertyRowMapper.newInstance(c);
		List data = this.getJdbcTemplate().query(this.createOraclePaginationSql(sql, orderby), args, rowMapper);
		// 将查询到的记录总数保存到LocalThread中
		PaginationContext.removeTotalCount();
		PaginationContext.setTotalCount(totalCount);
		return data;
	}
	/**
	 *分页茶尊，自己写记录查询语句 
	 * @param sql
	 * @param sqlCount
	 * @param orderby
	 * @param args
	 * @param c
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List pagedQuery(String sql, String sqlCount, String orderby, Object[] args,Object[] argsCount, Class c) {
		int totalCount = this.countQuery(sqlCount, argsCount);
		if (totalCount < 1) {
			return new ArrayList();
		}
		RowMapper rowMapper = ParameterizedBeanPropertyRowMapper.newInstance(c);
		List data = this.getJdbcTemplate().query(this.createOraclePaginationSql(sql, orderby), args, rowMapper);
		// 将查询到的记录总数保存到LocalThread中
		PaginationContext.removeTotalCount();
		PaginationContext.setTotalCount(totalCount);
		return data;
	}
	/**
	 * 条件查询
	 * @param sql
	 * @param args
	 * @param c
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List listQuery(String sql, Object[] args, Class c) {
		RowMapper rowMapper = ParameterizedBeanPropertyRowMapper.newInstance(c);
		List data = this.getJdbcTemplate().query(sql, args, rowMapper);
		return data;
	}

	/**
	 * 查询数量
	 * @param sql
	 * @param args
	 * @return
	 */
	protected int countQuery(String sql, Object[] args) {
		return this.getJdbcTemplate().queryForObject(sql, Integer.class, args);
	}

	/**
	 * 查询数量
	 * @param c
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int countQuery(Class c) {
		String tableName = c.getSimpleName().toLowerCase();
		String sql = "select count(id) from " + tableName;
		return countQuery(sql, new Object[] {});
	}

	/**
	 * 根据id查询对象
	 * @param c
	 * @param id
	 * @return
	 */
	@SuppressWarnings({"rawtypes" })
	public Object objectQuery(Class c, Integer id) {
		String tableName = c.getSimpleName().toLowerCase();
		String sql = "select * from " + tableName + " where id=?";
		try {
			return this.objectQuery(sql, new Object[] { id }, c);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 根据id查询对象（部分字段）
	 * @param fields
	 * @param c
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object objectQuery(String[] fields, Class c, Integer id) {
		String tableName = c.getSimpleName().toLowerCase();
		StringBuffer fieldsQueryStr = new StringBuffer();
		for (int i = 0; i < fields.length; i++) {
			fieldsQueryStr.append(fields[i]);
			if (i != fields.length - 1) {
				fieldsQueryStr.append(",");
			}
		}
		String sql = "select " + fieldsQueryStr + " from " + tableName + " where id=?";
		try {
			return this.objectQuery(sql, new Object[] { id }, c);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 根据条件查询对象
	 * @param sql
	 * @param args
	 * @param c
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object objectQuery(String sql, Object[] args, Class c) {
		try {
			RowMapper rowMapper = ParameterizedBeanPropertyRowMapper.newInstance(c);
			return this.getJdbcTemplate().queryForObject(sql, args, rowMapper);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	/**
	 * 根据条件查询JSON对象
	 * @param sql
	 * @param args
	 * @param c
	 * @return
	 */
	protected JSONObject objectQueryJSON(String sql, Object[] args) {
		try {
			return this.getJdbcTemplate().queryForObject(sql, args, new RowMapper<JSONObject>() {
				@Override
				public JSONObject mapRow(ResultSet rs, int columnIndex)
						throws SQLException {
					JSONObject jsonObject = new JSONObject();
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					for (int index = 1; index <= columnCount; ++index) {
						String column = JdbcUtils.lookupColumnName(rsmd, index);
						jsonObject.put(column, rs.getString(index));
					}
					return jsonObject;
				}
			} );
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	/**
	 * 根据条件查询JSON对象
	 * @param sql
	 * @param args
	 * @param c
	 * @return
	 */
	protected List<JSONObject> listQueryJSON(String sql, Object[] args) {
		try {
			return this.getJdbcTemplate().query(sql, args,new RowMapper<JSONObject>() {

				@Override
				public JSONObject mapRow(ResultSet rs, int columnIndex)
						throws SQLException {
					JSONObject jsonObject = new JSONObject();
					ResultSetMetaData rsmd = rs.getMetaData();
					int columnCount = rsmd.getColumnCount();
					for (int index = 1; index <= columnCount; ++index) {
						String column = JdbcUtils.lookupColumnName(rsmd, index);
						jsonObject.put(column, rs.getString(index));
						
					}
					return jsonObject;
				}} );
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	/**
	 * 拼装Sql条件  
	 * @param value
	 * @param sql
	 * @param appendSql
	 */
	protected void addSqlWhere(Object value, StringBuffer sql, List<Object> args, String appendSql) {
		this.addSqlWhere(value, sql, args, appendSql, 1);
	}

	/**
	 * 拼装Sql条件  
	 * @param value
	 * @param sql
	 * @param appendSql
	 * @param repetCount
	 */
	protected void addSqlWhere(Object value, StringBuffer sql, List<Object> args, String appendSql, int repetCount) {
		if (value != null && (value instanceof String ? !StringUtil.isBlank(value.toString()) : true)) {
			sql.append(appendSql);
			for (int i = 0; i < repetCount; i++) {
				args.add(value);
			}
		}
	}

	/**
	 * 拼装Sql条件  like
	 * @param value
	 * @param sql
	 * @param appendSql
	 */
	protected void addSqlWhereLike(Object value, StringBuffer sql, List<Object> args, String appendSql) {
		this.addSqlWhereLike(value, sql, args, appendSql, 1);
	}

	/**
	 * 拼装Sql条件  like
	 * @param value
	 * @param sql
	 * @param args
	 * @param appendSql
	 * @param repetCount
	 */
	protected void addSqlWhereLike(Object value, StringBuffer sql, List<Object> args, String appendSql, int repetCount) {
		if (value != null && (value instanceof String ? !StringUtil.isBlank(value.toString()) : true)) {
			sql.append(appendSql);
			for (int i = 0; i < repetCount; i++) {
				args.add("%" + value + "%");
			}
		}
	}

	/**
	 * 拼装Sql条件  like
	 * @param value
	 * @param sql
	 * @param appendSql
	 */
	protected void addSqlWhereLeftLike(Object value, StringBuffer sql, List<Object> args, String appendSql) {
		if (value != null && (value instanceof String ? !StringUtil.isBlank(value.toString()) : true)) {
			sql.append(appendSql);
			args.add("%" + value);
		}
	}

	/**
	 * 拼装Sql条件  like
	 * @param value
	 * @param sql
	 * @param appendSql
	 */
	protected void addSqlWhereRightLike(Object value, StringBuffer sql, List<Object> args, String appendSql) {
		if (value != null && (value instanceof String ? !StringUtil.isBlank(value.toString()) : true)) {
			sql.append(appendSql);
			args.add(value + "%");
		}
	}

	/**
	 * 拼装Sql条件  in 或者 not in
	 * @param values
	 * @param sql
	 * @param appendSql
	 */
	protected void addSqlWhereIn(Object[] values, StringBuffer sql, List<Object> args, String appendSql) {
		if (values != null && values.length > 0) {
			StringBuffer str = new StringBuffer();
			str.append("(");
			for (int i = 0; i < values.length; i++) {
				str.append("?");
				if (i != values.length - 1) {
					str.append(",");
				}
				args.add(values[i]);
			}
			str.append(")");
			sql.append(appendSql.replace("?", str.toString()));
		}
	}

	/**
	 * 打印SQL
	 * @param sql
	 * @throws Exception
	 */
	protected void printSql(String sql, Object[] args) {
		if (args != null) {
			for (Object o : args) {
				sql = sql.replaceFirst("[?]", "'" + String.valueOf(o) + "'");
			}
		}
		System.out.println(sql + "\n\n");
	}

	/**
	 * 打印SQL
	 * @param sql
	 * @param args
	 */
	protected void printSql(StringBuffer sql, List<Object> args) {
		this.printSql(sql.toString(), args.toArray());
	}

	/**
	 * 处理业务时把附件复制到项目目录下
	 * @param list
	 */
	// public void copyFile(List<Upfiles> list) {
	// if (list != null) {
	// for (Upfiles upfiles : list) {
	// this.copyFile(upfiles.getId());
	// }
	// }
	// }

	/**
	 * 处理业务时把附件复制到项目目录下
	 * 目前更改附件上传后在ROOT目录下，所以无需再进行复制。代码保留作为多线程处理的示例
	 * @param fileid
	 */
	// public void copyFile(Integer fileid) {
	// if (fileid != null) {
	// Upfiles upfiles = (Upfiles) objectQuery(Upfiles.class, fileid);
	// if (upfiles != null) {
	// HttpServletRequest request = RequestContextHolderUtil.getRequest();
	// File p = new
	// File(request.getSession().getServletContext().getRealPath(File.separator)).getParentFile();
	// String oldBasePath = p.getParentFile().getPath() + File.separator +
	// p.getName() + "_upload";
	// String newBasePath = new
	// File(request.getSession().getServletContext().getRealPath(File.separator)).getPath();
	// new Thread(new FileCopyThread(oldBasePath + upfiles.getFilepath(),
	// newBasePath + upfiles.getFilepath())).start();
	// }
	// }
	// }
	/**
	 * 执行SQL
	 * @param sql
	 * @param args
	 * @return
	 */
	public Integer excuteSql(String sql,Object[] args){
		return this.getJdbcTemplate().update(sql, args);
	}
	
	/**
	 * 分页查询
	 * @param sql
	 * @param orderby
	 * @param c
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T> PageBean<T> pagedBeanQuery(String sql, String orderby, Object[] args, Class c) {
		int totalCount = this.countQuery(createCountSql(sql), args);
		//封装到分页实体类里面
		PageBean<T> pageBean = new PageBean<T>();
		if (totalCount < 1) {
			pageBean.setRecordList(new ArrayList());
			pageBean.setTotalCount(totalCount);
			return pageBean;
		}
		RowMapper rowMapper = ParameterizedBeanPropertyRowMapper.newInstance(c);
		List data = this.getJdbcTemplate().query(this.createOraclePaginationSql(sql, orderby), args, rowMapper);
		// 将查询到的记录总数保存到LocalThread中
		PaginationContext.setTotalCount(totalCount);
		
		pageBean.setRecordList(data);
		pageBean.setTotalCount(PaginationContext.getTotalCount());
				
		return pageBean;
	}
}

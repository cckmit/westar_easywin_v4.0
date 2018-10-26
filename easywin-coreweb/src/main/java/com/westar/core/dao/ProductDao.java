package com.westar.core.dao;
import java.util.ArrayList;
import java.util.List;

import com.westar.base.model.ProUpFiles;
import com.westar.base.model.Product;
import com.westar.base.model.UserInfo;
import com.westar.base.pojo.PageBean;
import com.westar.base.util.CommonUtil;
import com.westar.base.util.ConstantInterface;

import org.springframework.stereotype.Repository;

@Repository
public class ProductDao extends BaseDao {

    /**
     * 分页获取产品
     * @param product
     * @param userInfo
     * @return
     */
	@SuppressWarnings("unchecked")
	public List<Product> listPagedPro(Product product, UserInfo userInfo,boolean isAuthorized){
	    StringBuffer sql = new StringBuffer();
	    List<Object> args = new ArrayList<>();

	    sql.append("\n select a.*,case when c.id is null then 0 else 1 end as attentionState from product a ");
        //查询关注的
        if(null != product && null != product.getAttention() && product.getAttention() == 1){
            sql.append("\n inner join attention b on a.id = b.busId and a.comId = b.comId and b.userId = ? and b.busType = '" + ConstantInterface.TYPE_PRODUCT + "'");
            args.add(userInfo.getId());
        }
        //查询关注状态
        sql.append("\n left join attention c on a.id = c.busId and a.comId = c.comId and c.userId = ? and c.busType = '" + ConstantInterface.TYPE_PRODUCT + "'");
        args.add(userInfo.getId());

        sql.append("\n where 1 = 1 and a.comId = ? ");
	    args.add(userInfo.getComId());

	    String orderBy = " a.id desc";

	    if(null != product){
            //查询自己的
            if(null != product.getOwnerType() && product.getOwnerType() == 1){
                this.addSqlWhere(userInfo.getId(),sql,args,"\n and a.creator = ?");
            }
            //查询自己负责的
            if(null != product.getPrincipal() && product.getPrincipal() > 0){
                this.addSqlWhere(product.getPrincipal(),sql,args,"\n and a.principal = ?");
            }
            //查询自己管理的
            if(null != product.getManager() && product.getManager() > 0){
                this.addSqlWhere(product.getManager(),sql,args,"\n and a.manager = ?");
            }

	        //产品名称
            this.addSqlWhereLike(product.getName(),sql,args,"\n and a.name like ? ");

            //状态
            this.addSqlWhere(product.getState(),sql,args,"\n and a.state = ? ");

            //类型
            this.addSqlWhere(product.getType(),sql,args,"\n and a.type = ? ");

            //日期筛选
            this.addSqlWhere(product.getStartDate(),sql,args,"and subStr(a.recordCreateTime,0,10) >= ?");
            this.addSqlWhere(product.getEndDate(),sql,args,"and subStr(a.recordCreateTime,0,10) <= ?");

            //创建人筛选
            if(null != product.getCreators() && product.getCreators().size() > 0){
                List<Object> creators = new ArrayList<>();
                for(UserInfo creator : product.getCreators()){
                    creators.add(creator.getId());
                }
                this.addSqlWhereIn(creators.toArray(),sql,args," and a.creator in ? ");
            }

            //负责人筛选
            if(null != product.getPrincipals() && product.getPrincipals().size() > 0){
                List<Object> principals = new ArrayList<>();
                for(UserInfo principal : product.getPrincipals()){
                    principals.add(principal.getId());
                }
                this.addSqlWhereIn(principals.toArray(),sql,args," and a.principal in ? ");
            }

            //产品经理筛选
            if(null != product.getManagers() && product.getManagers().size() > 0){
                List<Object> managers = new ArrayList<>();
                for(UserInfo manager : product.getManagers()){
                    managers.add(manager.getId());
                }
                this.addSqlWhereIn(managers.toArray(),sql,args," and a.manager in ? ");
            }

            //排序
            if(!CommonUtil.isNull(product.getOrderBy())){
                if("timeAsc".equals(product.getOrderBy())){
                    orderBy = " a.recordCreateTime asc";
                }else if("typeDesc".equals(product.getOrderBy())){
                    orderBy = " a.type Desc";
                }else if("typeAsc".equals(product.getOrderBy())){
                    orderBy = " a.type asc";
                }else if("timeDesc".equals(product.getOrderBy())){
                    orderBy = " a.recordCreateTime desc";
                }
            }
        }
	    return this.pagedQuery(sql.toString(),orderBy,args.toArray(),Product.class);
    }

    /**
     * 根据id获取需要展示的详情
     * @param id
     * @return
     */
    public Product getProductById(Integer id,Integer comId){
        StringBuffer sql = new StringBuffer();
        List<Object> args = new ArrayList<>();

        sql.append("\n select a.*,b.userName as managerName,c.userName as principalName,d.msgNum,g.userName as creatorName, ");
        sql.append("\n (nvl(e.upNum,0) + nvl(f.talkNum,0)) as docNum ");
        sql.append("\n from product a ");
        sql.append("\n left join userInfo b on a.manager = b.id ");
        sql.append("\n left join userInfo c on a.principal = c.id ");
        //留言数
        sql.append("\n left join (select count(id) as msgNum,proId from proTalk where proId = ? and comId = ? group by proId) d on a.id = d.proId ");
        args.add(id);
        args.add(comId);
        //文档数
        sql.append("\n left join (select count(id) as upNum,proId from proUpFiles where proId = ? and comId = ? group by proId) e on a.id = e.proId ");
        args.add(id);
        args.add(comId);
        //留言文档数
        sql.append("\n left join (select count(id) as talkNum,proId from proTalkUpfile where proId = ? and comId = ? group by proId) f on a.id = f.proId ");
        args.add(id);
        args.add(comId);
        sql.append("\n left join userInfo g on a.creator = g.id ");
        sql.append("\n where 1 = 1 and a.id = ? and a.comId = ? ");
        args.add(id);
        args.add(comId);
        return (Product) this.objectQuery(sql.toString(),args.toArray(),Product.class);
    }

	/**
	 * 分页查询用于选择的产品
	 * @author hcj 
	 * @date: 2018年10月16日 上午10:00:59
	 * @param product
	 * @param userInfo
	 * @return
	 */
	public PageBean<Product> listPagedProductForSelect(Product product, UserInfo userInfo) {
		StringBuffer sql = new StringBuffer();
	    List<Object> args = new ArrayList<>();
	    sql.append("\n select a.*,b.userName creatorName,c.userName principalName,d.userName managerName ");
	    sql.append("\n from product a  ");
	    sql.append("\n left join userInfo b on b.id=a.creator ");
	    sql.append("\n left join userInfo c on c.id=a.principal ");
	    sql.append("\n left join userInfo d on d.id=a.manager ");
        sql.append("\n where 1 = 1 and a.comId = ? ");
	    args.add(userInfo.getComId());
	    if(null != product){
	        //产品名称
	        this.addSqlWhereLike(product.getName(),sql,args,"\n and a.name like ? ");
	
	        //日期筛选
	        this.addSqlWhere(product.getStartDate(),sql,args,"and subStr(a.recordCreateTime,0,10) >= ?");
	        this.addSqlWhere(product.getEndDate(),sql,args,"and subStr(a.recordCreateTime,0,10) <= ?");
	        this.addSqlWhere(product.getCreator(), sql, args, " and a.creator=? ");
        }
	    return this.pagedBeanQuery(sql.toString(),"a.id desc",args.toArray(),Product.class);
	}

    /**
     * 获取所有文档
     * @param proUpFiles
     * @param comId
     * @return
     */
	public List<ProUpFiles> listProUpFiles(ProUpFiles proUpFiles,Integer comId){
        List<Object> args = new ArrayList<Object>();
        StringBuffer sql = new StringBuffer();
        sql.append("select a.* from( \n");
        sql.append("select a.* from \n");
        sql.append("( \n");
        sql.append("select a.id,a.upFileId,b.comId,b.name as proName,b.id as proId,c.filename,c.uuid,a.recordCreateTime,0 proTalkId,a.uploader,c.fileExt, \n");
        sql.append("d.username as uploaderName,d.gender,f.uuid as userUuid,f.filename userFileName,c.sizem,'pro' as type \n");
        sql.append("from proUpFiles a inner join product b on a.comId = b.comId and a.proId = b.id \n");
        sql.append("inner join upFiles c on a.comId = c.comId and a.upFileId = c.id \n");
        sql.append("left join userInfo d on  a.uploader = d.id \n");
        sql.append("inner join userOrganic e on d.id =e.userId and a.comId=e.comId\n");
        sql.append("left join upFiles f on  e.mediumHeadPortrait = f.id \n");
        sql.append(") a \n");
        sql.append("where a.comId=? and a.proId=? \n");
        args.add(comId);
        args.add(proUpFiles.getProId());
        sql.append("union all  \n");
        sql.append("select a.* from \n");
        sql.append("( \n");
        sql.append("select a.id,a.upFileId,b.comId,b.name as proName,b.id as proId,c.filename,c.uuid,a.recordCreateTime,0 proTalkId,a.userId as uploader,c.fileExt, \n");
        sql.append("d.username as uploaderName,d.gender,f.uuid as userUuid,f.filename userFileName,c.sizem,'talk' as type \n");
        sql.append("from proTalkUpfile a inner join product b on a.comId = b.comId and a.proId = b.id \n");
        sql.append("inner join upFiles c on a.comId = c.comId and a.upFileId = c.id \n");
        sql.append("left join userInfo d on  a.userId = d.id \n");
        sql.append("inner join userOrganic e on d.id =e.userId and a.comId=e.comId\n");
        sql.append("left join upFiles f on  e.mediumHeadPortrait = f.id \n");
        sql.append(") a \n");
        sql.append("where a.comId=? and a.proId=? \n");
        args.add(comId);
        args.add(proUpFiles.getProId());
        sql.append(") a where 1 = 1 \n");

        this.addSqlWhereLike(proUpFiles.getFilename(),sql,args,"\n and a.filename like ? ");

        String order = " a.id desc ";
        if("fileExt".equals(proUpFiles.getFileExt())){
            order = " a.fileExt ";
        }else if("uploader".equals(proUpFiles.getFileExt())){
            order = " a.uploader ";
        }


	    return this.pagedQuery(sql.toString(),order,args.toArray(),ProUpFiles.class);
    }

}

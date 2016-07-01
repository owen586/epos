package com.tinytrustframework.epos.dao.impl;

import com.tinytrustframework.epos.common.utils.page.Page;
import com.tinytrustframework.epos.common.utils.page.PageUtil;
import com.tinytrustframework.epos.dao.BaseDao;
import com.tinytrustframework.epos.dao.UserDao;
import com.tinytrustframework.epos.entity.Role;
import com.tinytrustframework.epos.entity.Terminal;
import com.tinytrustframework.epos.entity.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author owen
 * @version [版本号, 2015-7-28]
 */
@Repository
public class UserDaoImpl extends BaseDao implements UserDao {

    /**
     * 新增或编辑用户信息
     *
     * @param user 用户信息
     */
    public void saveOrUpdateUser(User user) {
        this.hibernateTemplate.saveOrUpdate(user);
    }


    /**
     * 查询角色信息列表
     *
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询参数
     */
    public Map<String, Object> queryRoleList(final Map<String, Object> businessParams, final Page pageParams) {
        return this.hibernateTemplate.execute(new HibernateCallback<Map<String, Object>>() {
            public Map<String, Object> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String hql = "from Role r where 1=1 ";

                if (businessParams.containsKey("roleName"))//角色名称
                {
                    String roleName = "%" + businessParams.get("roleName").toString() + "%";
                    businessParams.put("roleName", roleName);
                    hql += " and r.roleName like :roleName";
                }
                hql += " order by r.roleCode asc";
                return PageUtil.hqlQuery(session, hql, businessParams, pageParams);
            }
        });
    }

    /**
     * 查询角色列表信息
     */
    public List<Role> queryRoleList() {
        String hql = "from Role r where 1=1";
        return this.hibernateTemplate.find(hql);
    }


    /**
     * 查询用户信息列表
     *
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询参数
     */
    public Map<String, Object> queryUserList(final Map<String, Object> businessParams, final Page pageParams) {
        return this.hibernateTemplate.execute(new HibernateCallback<Map<String, Object>>() {
            public Map<String, Object> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String hql = "from User u where 1=1 ";

                if (businessParams.containsKey("userCode"))//用户编号
                {
                    hql += " and u.userCode = :userCode ";
                }

                if (businessParams.containsKey("cellphone"))//手机号
                {
                    hql += " and u.cellphone = :cellphone ";
                }

                if (businessParams.containsKey("userName"))//用户姓名
                {
                    hql += " and u.userName like :userName ";
                }

                if (businessParams.containsKey("topUserCode"))//上级用户编号
                {
                    hql += " and u.topUserCode = :topUserCode ";
                }

                if (businessParams.containsKey("outterUserCode"))//外部商家编号
                {
                    hql += " and u.outterUserCode = :outterUserCode ";
                }

                if (businessParams.containsKey("roleCode"))//角色编号
                {
                    hql += " and u.roleCode = :roleCode ";
                }

                if (businessParams.containsKey("status"))//用户状态
                {
                    hql += " and u.status = :status ";
                }

                if (businessParams.containsKey("tranferType"))//用户状态
                {
                    hql += " and u.tranferType = :tranferType ";
                }

                hql += " order by u.userCode desc";
                return PageUtil.hqlQuery(session, hql, businessParams, pageParams);
            }
        });
    }

    /**
     * 根据用户状态查询用户信息列表
     */
    public List<User> queryUserList(int status) {
        String hql = "from User u where u.status = :status";
        List<User> userList =
                this.hibernateTemplate
                        .findByNamedParam(hql, "status", status);
        return userList;
    }


    /**
     * 查询用户详情信息
     *
     * @param userCode 用户编号
     */
    public User getUserDetail(String userCode) {
        String hql = "from User u where u.userCode = :userCode";
        List<User> userList = this.hibernateTemplate.findByNamedParam(hql, "userCode", userCode);
        if (null != userList && !userList.isEmpty()) {
            return userList.get(0);
        }
        return null;
    }

    /**
     * 查询终端列表
     *
     * @param businessParams 业务查询条件
     * @param pageParams     分页查询参数
     */
    public Map<String, Object> queryTerminalList(final Map<String, Object> businessParams, final Page pageParams) {
        return this.hibernateTemplate.execute(new HibernateCallback<Map<String, Object>>() {
            public Map<String, Object> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String hql =
                        "select new Terminal(t.terminalCode,t.userCode,u.userName,u.cellphone) "
                                + "from User u,Terminal t where u.userCode = t.userCode ";

                if (businessParams.containsKey("terminalCode"))//终端编号
                {
                    hql += " and t.terminalCode = :terminalCode ";
                }

                if (businessParams.containsKey("cellphone"))//手机号
                {
                    hql += " and u.cellphone = :cellphone ";
                }

                if (businessParams.containsKey("userName"))//用户姓名
                {
                    hql += " and u.userName like :userName ";
                }

                if (businessParams.containsKey("userCode"))//用户编号
                {
                    hql += " and u.userCode = :userCode ";
                }

                hql += " order by u.userCode desc";
                return PageUtil.hqlQuery(session, hql, businessParams, pageParams);
            }
        });

    }

    /**
     * 根据用户编号查询终端信息
     *
     * @param userCode 用户编号
     */
    public Terminal getTerminalDetail(String userCode) {
        String hql = "from Terminal t where t.userCode = :userCode";
        List<Terminal> terminalList =
                this.hibernateTemplate.findByNamedParam(hql,
                        new String[]{"userCode"},
                        new Object[]{userCode});
        if (null != terminalList && !terminalList.isEmpty()) {
            return terminalList.get(0);
        }
        return null;
    }

    /**
     * 根据终端编号查询终端信息
     *
     * @param terminalCode 终端编号
     */
    public Terminal getTerminalDetailByTerminalCode(String terminalCode) {
        String hql = "from Terminal t where t.terminalCode = :terminalCode";
        List<Terminal> terminalList =
                this.hibernateTemplate
                        .findByNamedParam(hql,
                                new String[]{"terminalCode"},
                                new Object[]{terminalCode});
        if (null != terminalList && !terminalList.isEmpty()) {
            return terminalList.get(0);
        }
        return null;
    }

    /**
     * 编辑终端信息
     *
     * @param terminal 终端
     */
    public void saveOrUpdateTerminal(Terminal terminal) {
        this.hibernateTemplate.saveOrUpdate(terminal);
    }

    /**
     * 删除终端信息
     *
     * @param userCode 用户编号
     */
    public boolean deleteTerminal(String userCode) {
        Terminal terminal = this.getTerminalDetail(userCode);
        if (null == terminal) {
            return false;
        } else {
            this.hibernateTemplate.delete(terminal);
            return true;
        }
    }
}

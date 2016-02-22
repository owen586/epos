package com.tinytrustframework.epos.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.tinytrustframework.epos.dao.UserDao;
import com.tinytrustframework.epos.entity.Role;
import com.tinytrustframework.epos.entity.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.stereotype.Repository;

import com.tinytrustframework.epos.common.utils.lang.PageUtil;
import com.tinytrustframework.epos.entity.Terminal;

/**
 * <一句话功能简述>
 *
 *
 * @author owen
 * @version [版本号, 2015-7-28]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
@Repository
public class UserDaoImpl extends BaseDao implements UserDao {

    /**
     * {@inheritDoc}
     */
    public void saveOrUpdateUser(User user) {
        this.getHibernateTemplate().saveOrUpdate(user);
    }


    public Map<String, Object> queryRoleList(final Map<String, Object> params, final int pageNo, final int pageSize) {
        return this.getHibernateTemplate().execute(new HibernateCallback<Map<String, Object>>() {
            public Map<String, Object> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String hql = "from Role r where 1=1 ";

                if (params.containsKey("roleName"))//角色名称
                {
                    String roleName = "%" + params.get("roleName").toString() + "%";
                    params.put("roleName", roleName);
                    hql += " and r.roleName like :roleName";
                }
                hql += " order by r.roleCode asc";
                return PageUtil.findByHQLQueryWithMap(session, pageNo, pageSize, hql, params);
            }
        });
    }

    

    public List<Role> queryRoleList() {
        return this.getHibernateTemplate().findByNamedQuery("hql.role.list");
    }


    public Map<String, Object> queryUserList(final Map<String, Object> params, final int pageNo, final int pageSize) {
        return this.getHibernateTemplate().execute(new HibernateCallback<Map<String, Object>>() {
            public Map<String, Object> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String hql = "from User u where 1=1 ";

                if (params.containsKey("userCode"))//用户编号
                {
                    hql += " and u.userCode = :userCode ";
                }

                if (params.containsKey("cellphone"))//手机号
                {
                    hql += " and u.cellphone = :cellphone ";
                }

                if (params.containsKey("userName"))//用户姓名
                {
                    hql += " and u.userName like :userName ";
                }

                if (params.containsKey("topUserCode"))//上级用户编号 
                {
                    hql += " and u.topUserCode = :topUserCode ";
                }

                if (params.containsKey("outterUserCode"))//外部商家编号 
                {
                    hql += " and u.outterUserCode = :outterUserCode ";
                }

                if (params.containsKey("roleCode"))//角色编号
                {
                    hql += " and u.roleCode = :roleCode ";
                }

                if (params.containsKey("status"))//用户状态
                {
                    hql += " and u.status = :status ";
                }

                if (params.containsKey("tranferType"))//用户状态
                {
                    hql += " and u.tranferType = :tranferType ";
                }

                hql += " order by u.userCode desc";
                return PageUtil.findByHQLQueryWithMap(session, pageNo, pageSize, hql, params);
            }
        });
    }


    public List<User> queryUserList(int status) {
        @SuppressWarnings("unchecked")
        List<User> userList =
                this.getHibernateTemplate()
                        .findByNamedQueryAndNamedParam("hql.user.query_user_by_status", "status", status);
        return userList;
    }


    public User getUserDetail(String userCode) {
        String hql = "from User u where u.userCode = :userCode";
        @SuppressWarnings("unchecked")
        List<User> userList = this.getHibernateTemplate().findByNamedParam(hql, "userCode", userCode);
        if (null != userList && !userList.isEmpty()) {
            return userList.get(0);
        }
        return null;
    }


    public Map<String, Object> queryTerminalList(final Map<String, Object> params, final int pageNo, final int pageSize) {
        return this.getHibernateTemplate().execute(new HibernateCallback<Map<String, Object>>() {
            public Map<String, Object> doInHibernate(Session session)
                    throws HibernateException, SQLException {
                String hql =
                        "select new Terminal(t.terminalCode,t.userCode,u.userName,u.cellphone) "
                                + "from User u,Terminal t where u.userCode = t.userCode ";

                if (params.containsKey("terminalCode"))//终端编号
                {
                    hql += " and t.terminalCode = :terminalCode ";
                }

                if (params.containsKey("cellphone"))//手机号
                {
                    hql += " and u.cellphone = :cellphone ";
                }

                if (params.containsKey("userName"))//用户姓名
                {
                    hql += " and u.userName like :userName ";
                }

                if (params.containsKey("userCode"))//用户编号 
                {
                    hql += " and u.userCode = :userCode ";
                }

                hql += " order by u.userCode desc";
                return PageUtil.findByHQLQueryWithMap(session, pageNo, pageSize, hql, params);
            }
        });

    }


    public Terminal getTerminalDetail(String userCode) {
        @SuppressWarnings("unchecked")
        List<Terminal> terminalList =
                this.getHibernateTemplate().findByNamedQueryAndNamedParam("hql.terminal.get_terminal_detail",
                        new String[]{"userCode"},
                        new Object[]{userCode});
        if (null != terminalList && !terminalList.isEmpty()) {
            return terminalList.get(0);
        }
        return null;
    }


    public Terminal getTerminalDetailByTerminalCode(String terminalCode) {
        @SuppressWarnings("unchecked")
        List<Terminal> terminalList =
                this.getHibernateTemplate()
                        .findByNamedQueryAndNamedParam("hql.terminal.get_terminal_detail_by_termianl_code",
                                new String[]{"terminalCode"},
                                new Object[]{terminalCode});
        if (null != terminalList && !terminalList.isEmpty()) {
            return terminalList.get(0);
        }
        return null;
    }


    public void saveOrUpdateTerminal(Terminal terminal) {
        this.getHibernateTemplate().saveOrUpdate(terminal);
    }


    public boolean deleteTerminal(String userCode) {
        Terminal terminal = this.getTerminalDetail(userCode);
        if (null == terminal) {
            return false;
        } else {
            this.getHibernateTemplate().delete(terminal);
            return true;
        }
    }
}

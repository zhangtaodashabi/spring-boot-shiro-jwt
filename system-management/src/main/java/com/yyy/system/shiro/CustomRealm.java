package com.yyy.system.shiro;


import com.yyy.common.jwt.JWTToken;
import com.yyy.common.utils.JWTUtil;
import com.yyy.common.utils.ToolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import org.springframework.stereotype.Component;



/**
 * @author chen
 * @date 2019/7/23
 * @email 15218979950@163.com
 * @description 自定义Realm, 实现Shiro安全认证
 */

@Component
@Slf4j
public class CustomRealm extends AuthorizingRealm {


	//这里照着这样写，一定要用懒加载，不然会二次代理，具体原因请参考百度
//	@Lazy
//	@Autowired
//	private UserService userService;

	/**
	 * 必须重写此方法，不然会报错
	 */
	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof JWTToken;
	}

	/**
	 * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		log.info("————身份认证方法————");
		String token = (String) authenticationToken.getCredentials();
		// 解密获得username，用于和数据库进行对比
		String username = JWTUtil.getUsername(token);
		Integer userId = JWTUtil.getUserId(token);
		if (ToolUtil.isEmpty(userId)||ToolUtil.isEmpty(username) || !JWTUtil.verify(token, username,userId)) {
			throw new AuthenticationException("token认证失败。");
		}

		/* 以下数据库查询可根据实际情况，可以不必再次查询，这里我两次查询会很耗资源
		 * 我这里增加两次查询是因为考虑到数据库管理员可能自行更改数据库中的用户信息
		 */
//		if (ToolUtil.isEmpty(userService.lambdaQuery().eq(UserDO::getUsername, username).one())) {
//			throw new AuthenticationException("该用户不存在。");
//		}
		return new SimpleAuthenticationInfo(token, token, "MyRealm");
	}

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String username = JWTUtil.getUsername(principals.toString());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //大概照着这样写
//        SysUserDTO userDTO = userService.selectByUserName(username);
//        Set<String> permissionSet =  userDTO.getMenuDTOS().stream().map(MenuDTO::getPermission).collect(Collectors.toSet());
//        Set<String> roleSet =  userDTO.getRoleDTOList().stream().map(RoleDTO::getCode).collect(Collectors.toSet());
//        //设置该用户拥有的角色和权限
//        info.setRoles(roleSet);
//        info.setStringPermissions(permissionSet);
        return info;
    }
}
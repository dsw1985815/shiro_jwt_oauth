sample
===
* 注释

	select #use("cols")# from access_token_cache where #use("condition")#

cols
===

	id,rexp,jti,access_token

updateSample
===

	`id`=#id#,`rexp`=#rexp#,`jti`=#jti#,`access_token`=#accessToken#

condition
===

	1 = 1  
	@if(!isEmpty(rexp)){
	 and `rexp`=#rexp#
	@}
	@if(!isEmpty(jti)){
	 and `jti`=#jti#
	@}
	@if(!isEmpty(accessToken)){
	 and `access_token`=#accessToken#
	@}
	

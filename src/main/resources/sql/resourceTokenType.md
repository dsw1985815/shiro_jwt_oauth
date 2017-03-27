sample
===
* 注释

	select #use("cols")# from resource_token_type where #use("condition")#

cols
===

	id,token_type,resource_id

updateSample
===

	`id`=#id#,`token_type`=#tokenType#,`resource_id`=#resourceId#

condition
===

	1 = 1  
	@if(!isEmpty(tokenType)){
	 and `token_type`=#tokenType#
	@}
	@if(!isEmpty(resourceId)){
	 and `resource_id`=#resourceId#
	@}
	

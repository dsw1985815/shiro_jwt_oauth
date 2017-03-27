sample
===
* 注释

	select #use("cols")# from source_filters where #use("condition")#

cols
===

	id,filter_type

updateSample
===

	`id`=#id#,`filter_type`=#filterType#

condition
===

	1 = 1  
	@if(!isEmpty(filterType)){
	 and `filter_type`=#filterType#
	@}
sample
===
* 注释

        select #use("cols")# from token_type where #use("condition")#

cols
===

        id,name,describe,expire,check_times,required

updateSample
===

        `id`=#id#,`name`=#name#,`describe`=#describe#,`expire`=#expire#,`check_times`=#checkTimes#

condition
===

    1 = 1
        @if(!isEmpty(name)){
        and `name`=#name#
    @}
        @if(!isEmpty(describe)){
        and `describe`=#describe#
    @}
        @if(!isEmpty(expire)){
        and `expire`=#expire#
    @}
        @if(!isEmpty(checkTimes)){
        and `check_times`=#checkTimes#
    @}

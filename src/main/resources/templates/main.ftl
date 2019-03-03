<#import "parts/common.ftl" as c>
<#import "parts/login.ftl" as l>
<@c.page>
<div>
    <@l.logout/>
   </div>
<div>
    <form method="post">
        <input type="text" name="text" placeholder="Введите текст"/>
        <input type="text" name="tag" placeholder="Тэг"/>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <input type="submit" value = "Добавить"/>
    </form>
</div>
    <div>Список сообщений:</div>

    <form method="get" action="/main">
            <input type="text" name="filter" placeholder="Фильтр"/>
            <input type="submit" value = "Найти"/>
        </form>

    <#list messages as message>
        <div>
            <b>${message.id}</b>
            <span>${message.text}</span>
            <i>${message.tag}</i>
            <strong>${message.authorName}</strong>
        </div>
    <#else > No messages
    </#list>
</@c.page>
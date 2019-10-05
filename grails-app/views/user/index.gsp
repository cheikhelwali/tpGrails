<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
    <title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
<a href="#list-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
    </ul>
</div>
<div id="list-user" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>
            <th class="sortable"><a href="/user/index?sort=username&amp;max10&amp;order=asc">Username</a></th>
            <th class="sortable"><a href="/user/index?sort=thumbnail&amp;max10&amp;order=asc">Thumbnail</a></th>
            <th class="sortable"><a href="/user/index?sort=annonces&amp;max10&amp;order=asc">Annonces</a></th>
        </tr>
        </thead>
        <g:each in="${userList}" var="instance">
            <tr>
                <td><g:link controller="user" action="show" id="${instance.id}">${instance.username}</g:link> </td>
                <td><asset:image src="${instance.thumbnail.filename}"/></td>
                <td>
                    <ul>
                        <g:each in="${instance.annonces}" var="annonce">
                            <li><g:link controller="annonce" action="show" id="${annonce.id}">${annonce.title}</g:link></li>
                        </g:each>
                    </ul>
                </td>
            </tr>
        </g:each>
    </table>
</div>
</body>
</html>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'annonce.label', default: 'Annonce')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<a href="#create-annonce" class="skip" tabindex="-1"><g:message code="default.link.skip.label"
                                                                default="Skip to content&hellip;"/></a>

<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]"/></g:link></li>
    </ul>
</div>

<div id="create-annonce" class="content scaffold-create" role="main">
    <h1><g:message code="default.create.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${this.annonce}">
        <ul class="errors" role="alert">
            <g:eachError bean="${this.annonce}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>

    <form action="save" method="POST" enctype="multipart/form-data">

        <div class='fieldcontain required'>

            <label for='title'>Title
                <span class='required-indicator'>*</span>
            </label><input type="text" name="title" value="" required="" id="title"/><br><br>

            <label for='description'>Description
                <span class='required-indicator'>*</span>
            </label><input type="text" name="description" value="" required="" id="description"/><br><br>


            <label for='validTill'>Valid Till
                <span class='required-indicator'>*</span>
            </label>
        <input type="date" name="validTill" value="date.struct" id="validTill"/>

        </div>

        <div class='fieldcontain'>
            <label>Illustrations</label>
            <input type="file" name="filename" multiple/><br>
            <label for='state'>State</label>
            <input type="hidden" name="_state"/>
            <input type="checkbox" name="state" id="state"/>
        </div>

        <div class='fieldcontain required'>
            <label for='author'>Author
                <span class='required-indicator'>*</span>
            </label>
            <select name="author" required="" id="author">
                <g:each in="${tpgrails.User.all}" var="author">
                    <option value="${author.id}">${author.id}</option>
                </g:each>
            </select>
        </div>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}"/>
        </fieldset>
    </form>
</div>
</body>
</html>
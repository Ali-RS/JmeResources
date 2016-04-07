<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <link id="favicon" rel="shortcut icon" href="/favicon.ico" type="image/x-icon">

        <link rel="stylesheet" href="/lib/bootstrap-3.3.6-dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="/lib/bootstrap-3.3.6-dist/css/bootstrap-theme.min.css">
        <link rel="stylesheet" href="/css/style.css">

        <script type="text/javascript" src="/lib/jquery-1.12.1/jquery-1.12.1.min.js"></script>
        <script type="text/javascript" src="/lib/bootstrap-3.3.6-dist/js/bootstrap.min.js"></script>
        <!-- <script type="text/javascript" src="/lib/angular-1.5.0/angular.min.js"></script> -->
        <script type="text/javascript" src="/lib/bootbox-4.4.0/bootbox.min.js"></script>
        <script type="text/javascript" src="/js/logout.js"></script>

        <title>${pageTitle}</title>
    </head>

    <body>

        <nav class="navbar navbar-default" id="jme-header">
            <div class="container">

                <div class="navbar-header">

                    <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse-1" aria-expanded="false">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>

                    <a class="navbar-brand" href="/">
                        <img alt="jme-logo" id=site-logo src="http://hub.jmonkeyengine.org/uploads/default/1252/91fcc91347189c84.png">
                    </a>
                </div>

                <div class="collapse navbar-collapse" id="navbar-collapse-1">
                    <ul class="nav navbar-nav">

                    </ul>

                    <ul class="nav navbar-nav navbar-right">

                        <c:if test = "${loggedIn eq false}">
                            <li>
                                <btn class="btn btn-sm btn-primary" onclick="window.location.href='/login/'"><span class="glyphicon glyphicon-log-in" aria-hidden="true"></span>&nbsp;&nbsp;&nbsp;Log In</btn>
                            </li>
                        </c:if>

                        <c:if test = "${loggedIn eq true}">
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><c:out value="${user.username}" /></a>
                            <ul class="dropdown-menu">
                                <li><a href="/messages/">Private Messages</a></li>
                                <li role="separator" class="divider"></li>
                                <li><a href="/asset/user/">My Assets</a></li>
                                <li role="separator" class="divider"></li>
                                <li><a href="#" onclick="logout()"><span class="glyphicon glyphicon-log-out" aria-hidden="true"></span> Log Out</a></li>
                            </ul>
                        </li>
                        </c:if>
                    </ul>

                    <c:if test = "${admin eq true}">
                    <ul class="nav navbar-nav navbar-right">
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Admin</span></a>
                            <ul class="dropdown-menu">
                                <li><a href="/admin/potentialassets/">Potential Assets</a></li>
                                <li role="separator" class="divider"></li>
                                <li><a href="/admin/">General Configuration</a></li>
                                <li><a href="/admin/bintray/">Bintray Configuration</a></li>
                                <li><a href="/admin/discourse/">Discourse Configuration</a></li>


                            </ul>
                        </li>
                    </ul>
                    </c:if>

                </div>

            </div>
        </nav>

        <div class="container">

<%-- 
    Document   : login
    Created on : Sep 21, 2012, 4:36:00 PM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@taglib uri="http://java.sun.com/jsf/core" prefix="f" %>

<html>
    <head>
        <title>Login</title>
    <head>
    <link rel="stylesheet" type="text/css" href="CSS/style.css" />
    <body>
        <f:view>
            
            <h:form id ="mainForm">
                <div id="page">
                    <div id="page_top">
                        <span class="nodisp"> </span>
                    </div>

                    <div id="charts" class="page_container subpage">
                        <h2 class="page_header">Login</h2>
                        <div class="page_sub_body page_gradient">
                            <div class="page_column_wide">

                                <div class="rounded_container_blue" style="padding-top: 30px;">
                                    
                                        <div class="field_row">
                                            <div class="label_container">
                                                <label>Login Name or Email</label>
                                            </div>
                                                <h:inputText id="username" value="#{loginbean.username}" required="true" styleClass="field_container"/>
                                            <div class="clear">
                                                <span class="nodisp"> </span>
                                            </div>
                                        </div>
                                        <div class="field_row">
                                            <div class="label_container">
                                                <label>Password</label>
                                            </div>
                                                <h:inputSecret id="password" value="#{loginbean.password}" required="true" styleClass="field_container"/>
                                            <div class="clear">
                                                <span class="nodisp"> </span>
                                            </div>
                                        </div>
                                        <div class="final_row">
                                            
                                            <h:commandButton action="#{loginbean.checkValidUser}" value="CheckVaildUser" type="submit" image="images/buttons/login_blue.gif"/>
                                            
                                            <div class="final_row_text_container" style="padding-top: 15px;">
                                                <a class="small_text" style="color: #008ee8;" href="/login/forgot_password">Forgot your Password?</a>
                                            </div>
                                            <div style="float: right; margin-top: -12px;">
                                                <a class="small_text" href="/labels/login">Record Label Login »</a>
                                            </div>

                                        </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </h:form>
        </f:view>
    <body>
</html>

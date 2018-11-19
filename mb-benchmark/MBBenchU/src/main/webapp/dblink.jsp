<%--
  Created by IntelliJ IDEA.
  User: Daniel
  Date: 2018/9/18
  Time: 11:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>数据库连接</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
    <!-- Theme style -->
    <link rel="stylesheet" href="dist/css/AdminLTE.min.css">
    <!-- AdminLTE Skins. Choose a skin from the css/skins
         folder instead of downloading all of them to reduce the load. -->
    <link rel="stylesheet" href="dist/css/skins/_all-skins.min.css">
    <link rel="stylesheet" href="diycss/animate.css">
    <link rel="stylesheet" href="diycss/style.css">
    <link rel="stylesheet" href="diycss/flavr.css">
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">

    <header class="main-header">
        <!-- Logo -->
        <a href="welcomepage.html" class="logo">
            <span class="logo-mini"><b>GH</b></span>
            <!-- logo for regular state and mobile devices -->
            <span class="logo-lg"><b>MBBench-U</b></span>
        </a>
        <!-- Header Navbar: style can be found in header.less -->
        <nav class="navbar navbar-static-top">
            <!-- Sidebar toggle button-->
            <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
                <span class="sr-only">隐藏菜单</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </a>
        </nav>
    </header>
    <!-- Left side column. contains the logo and sidebar -->
    <aside class="main-sidebar">
        <!-- sidebar: style can be found in sidebar.less -->
        <section class="sidebar">
            <ul class="sidebar-menu">
                <li class="header">系统功能菜单</li>
                <li>
                    <a href="welcomepage.html">
                        <i class="fa fa-dashboard"></i><span> 系统设置</span>
                    </a>
                </li>
                <li>
                    <a href="datagen.jsp">
                        <i class="fa fa-files-o"></i><span> 海量文件生成</span>
                    </a>
                </li>
                <li class="active">
                    <a href="dblink.jsp">
                        <i class="fa fa-th"></i> <span> 数据库连接</span>
                    </a>
                </li>
                <li class="treeview">
                    <a href="#">
                        <i class="fa fa-pie-chart"></i>
                        <span>传输负载测试</span>
                        <span class="pull-right-container">
                            <i class="fa fa-angle-left pull-right"></i>
                        </span>
                    </a>
                    <ul class="treeview-menu">
                        <li><a href="transload.jsp"><i class="fa fa-circle-o"></i> 数据上传负载测试</a></li>
                        <li><a href="transdown.jsp"><i class="fa fa-circle-o"></i> 数据下载负载测试</a></li>
                    </ul>
                </li>
                <li class="treeview">
                    <a href="#">
                        <i class="fa fa-laptop"></i>
                        <span>查询负载测试</span>
                        <span class="pull-right-container">
                            <i class="fa fa-angle-left pull-right"></i>
                        </span>
                    </a>
                    <ul class="treeview-menu">
                        <li><a href="querysimple.jsp"><i class="fa fa-circle-o"></i> 简单查询负载测试</a></li>
                        <li><a href="querymulti.jsp"><i class="fa fa-circle-o"></i> 复杂查询负载测试</a></li>
                    </ul>
                </li>
                <li class="treeview">
                    <a href="#">
                        <i class="fa fa-edit"></i> <span>结果查看</span>
                        <span class="pull-right-container">
                            <i class="fa fa-angle-left pull-right"></i>
                        </span>
                    </a>
                    <ul class="treeview-menu">
                        <li><a href="transresult.html"><i class="fa fa-circle-o"></i> 传输结果查看</a></li>
                        <li><a href="querysimpleresult.html"><i class="fa fa-circle-o"></i> 简单查询结果查看</a></li>
                        <li><a href="querymultiresult.html"><i class="fa fa-circle-o"></i> 复杂查询结果查看</a></li>
                    </ul>
                </li>
                <li>
                    <a href="comparestart.jsp">
                        <i class="fa fa-table"></i> <span>结果对比报告</span>
                        <span class="pull-right-container">

                        </span>
                    </a>
                </li>
                <li>
                    <a href="help.html">
                        <i class="fa fa-calendar"></i> <span>帮助文档</span>
                        <span class="pull-right-container">
                            <small class="label pull-right bg-blue">new</small>
                        </span>
                    </a>
                </li>
                <li>
                    <a href="link.html">
                        <i class="fa fa-envelope"></i> <span>联系我们</span>
                        <span class="pull-right-container">
                            <small class="label pull-right bg-yellow">call me</small>
                        </span>
                    </a>
                </li>
                <li class="header">信息提示</li>
                <li><a href="#"><i class="fa fa-circle-o text-red"></i> <span>重要内容</span></a></li>
                <li><a href="#"><i class="fa fa-circle-o text-yellow"></i> <span>警告内容</span></a></li>
                <li><a href="#"><i class="fa fa-circle-o text-aqua"></i> <span>提示信息</span></a></li>
            </ul>
        </section>
        <!-- /.sidebar -->
    </aside>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                数据库连接
                <small>当前所选择的数据库为<label id="databasetype"><%=session.getAttribute("databasetype")%></label></small>
            </h1>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-9">
                    <div class="box box-default">
                        <div class="box-header with-border">
                            <i class="fa fa-tv"></i>
                            <h3 class="box-title">数据库连接</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="row">
                                <div class="col-xs-9">
                                    <div class="form-group">
                                        <label>请输入数据库的连接网址，如"localhost"</label>
                                        <input id="database_ip" type="text" class="form-control border-input" placeholder="请输入网址" />
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-6">
                                    <div class="form-group">
                                        <label>请输入数据库的连接端口号</label>
                                        <input id="database_port" type="text" class="form-control border-input" placeholder="请输入端口号" />
                                    </div>
                                </div>
                                <div class="col-xs-4">
                                    <div class="form-group">
                                        <label>&nbsp</label>
                                        <div class="text-yellow">
                                            <span>请根据您在安装<%=session.getAttribute("databasetype")%>的实际配置填写</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-6">
                                    <div class="form-group">
                                        <label>请输入存储文件的数据库名称，如"FileDB"</label>
                                        <input id="database_dbname" type="text" class="form-control border-input" placeholder="请输入数据库名称" />
                                    </div>
                                </div>
                                <div class="col-xs-4">
                                    <div class="form-group">
                                        <label>&nbsp</label>
                                        <div class="text-red">
                                            <span>请不要使用中文填写</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-6">
                                    <div class="form-group">
                                        <label>请输入存储元数据文件的集合名称，如"FileMeta"</label>
                                        <input id="database_collectionname" type="text" class="form-control border-input" placeholder="请输入集合名称" />
                                    </div>
                                </div>
                                <div class="col-xs-4">
                                    <div class="form-group">
                                        <label>&nbsp</label>
                                        <div class="text-red">
                                            <span>请不要使用中文填写</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <hr />
                            <div class="text-center">
                                <input id="dbconnect" type="button" class="btn btn-info btn-fill btn-wd" value="连接数据库" onclick="dbconnect()"/>&nbsp&nbsp&nbsp&nbsp
                                <input id="disdbconnect" type="button" class="btn btn-info btn-fill btn-wd" value="断开连接" onclick="dbdisconnect()"/>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>
                <!-- /.col -->
            </div>
        </section>
        <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <footer class="main-footer">
        <div class="pull-right hidden-xs">
            <b>Version</b> 1.0.0
        </div>
        <strong>Copyright &copy; <script>document.write(new Date().getFullYear())</script>, 制作者<i class="fa fa-heart heart"></i> by <a href="http://deke.ruc.edu.cn/ ">数据工程与知识工程实验室</a>. 版权属于 <a href="http://info.ruc.edu.cn" target="_blank" title="中国人民大学信息学院">中国人民大学信息学院</a></strong>
    </footer>
</div>
<!-- ./wrapper -->

<!-- jQuery 2.2.3 -->
<script src="plugins/jQuery/jquery-2.2.3.min.js"></script>
<!-- Bootstrap 3.3.6 -->
<script src="bootstrap/js/bootstrap.min.js"></script>
<!-- FastClick -->
<script src="plugins/fastclick/fastclick.js"></script>
<!-- AdminLTE App -->
<script src="dist/js/app.min.js"></script>
<!-- AdminLTE for demo purposes -->
<script src="dist/js/demo.js"></script>
<script src="diyjs/common.js"></script>
<script src="diyjs/flavr.min.js"></script>
<script type="text/javascript">
    function dbconnect(){
        var ip = $("#database_ip").val();
        var port = $("#database_port").val();
        var dbname = $("#database_dbname").val();
        var clname = $("#database_collectionname").val();
        var dbtype = $("#databasetype").text();

        $.ajax({
            type:"POST",
            url:"/dbservlet",
            async:true,
            data:{"dbip":ip, "dbport":port, "dbname":dbname, "functiontype":"dbconnect", "metadbname": clname},
            success:function(data){
                if(data.result == "true")
                {
                    new $.flavr("数据库连接成功");
                }
                else
                {
                    new $.flavr("数据库连接失败，请检查数据库是否开启");
                }
            },
            error:function(){
                new $.flavr("数据库连接失败，请检查数据库是否开启");
            },
            dataType:"json"
        });
    }

    function dbdisconnect(){
        $.ajax({
            type:"POST",
            url:"/dbservlet",
            async:true,
            data:{"functiontype":"dbdisconnect"},
            success:function(data){
                if(data.result == "true")
                {
                    new $.flavr("数据库连接已断开");
                }
                else
                {
                    new $.flavr("当前没有连接数据库");
                }
            },
            error:function(){
                new $.flavr("当前没有连接数据库");
            },
            dataType:"json"
        });
    }
</script>
</body>
</html>

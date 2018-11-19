<%--
  Created by IntelliJ IDEA.
  User: Daniel
  Date: 2018/9/18
  Time: 11:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>数据上传负载测试</title>
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
    <script src="diyjs/echarts.min.js"></script>
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
                <li>
                    <a href="dblink.jsp">
                        <i class="fa fa-th"></i> <span> 数据库连接</span>
                    </a>
                </li>
                <li class="treeview active">
                    <a href="#">
                        <i class="fa fa-pie-chart"></i>
                        <span>传输负载测试</span>
                        <span class="pull-right-container">
                            <i class="fa fa-angle-left pull-right"></i>
                        </span>
                    </a>
                    <ul class="treeview-menu">
                        <li class="active"><a href="transload.jsp"><i class="fa fa-circle-o"></i> 数据上传负载测试</a></li>
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
                数据上传负载测试
                <small>当前所选择的数据库为<label id="databasetype"><%=session.getAttribute("databasetype")%></label></small>
            </h1>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <div class="col-md-8">
                    <div class="box box-default">
                        <div class="box-header with-border">
                            <i class="fa fa-tv"></i>
                            <h3 class="box-title">数据上传负载测试</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="row">
                                <div class="col-xs-9">
                                    <div class="form-group">
                                        <div class="alert alert-info">
                                            <span>在执行操作前，请确认右侧的信息内容是否正确，如有不正确的请更正</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-9">
                                    <div class="form-group">
                                        <div class="alert alert-warning">
                                            <span>请确认数据库已连接成功，如果未连接或者连接失败，请返回<b>数据库连接</b>重新操作</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="text-center">
                                <input id="fileupload" type="button" class="btn btn-info btn-fill btn-wd" value="数据上传" onclick="loadtest()"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input id="filemetaupload" type="button" class="btn btn-info btn-fill btn-wd" value="元数据上传" onclick="loadmetatest()"/>
                            </div>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                    <div class="box box-default" id="loadmetastatus" style="display: none;">
                        <div class="box-header with-border">
                            <i class="fa fa-tv"></i>
                            <h3 class="box-title">实时文档上传信息</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label>实时进度条</label>
                                        <div class="progress" id="progressBarloadmeta">
                                            <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="0%" aria-valuemin="0" aria-valuemax="100" style="width: 0%;" id="progressBarItemloadmeta">
                                                <p id="processloadmeta">0%</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="content">
                                    <label>已上传文件数量：</label><span id="loadfile_hadmeta"></span><br />
                                    <label>待上传文件数量：</label><span id="loadfile_needmeta"></span><br />
                                    <label>传输文件的速度：</label><span id="loadfile_speedmeta"></span><br />
                                    <label>当前已用的时间：</label><span id="loadfile_timemeta"></span>
                                </div>
                            </div>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                    <div class="box box-default" id="metachart" style="display: none;">
                        <div class="box-header with-border">
                            <i class="fa fa-tv"></i>
                            <h3 class="box-title">实时文档上传折线图</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="content">
                                <div id="metacanvasDiv" style="width: 100%;height: 400px"></div>
                            </div>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                    <div class="box box-default" id="loadstatus" style="display: none;">
                        <div class="box-header with-border">
                            <i class="fa fa-tv"></i>
                            <h3 class="box-title">实时文件上传信息</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label>实时进度条</label>
                                        <div class="progress" id="progressBarload">
                                            <div class="progress-bar progress-bar-striped active" role="progressbar" aria-valuenow="0%" aria-valuemin="0" aria-valuemax="100" style="width: 0%;" id="progressBarItemload">
                                                <p id="processload">0%</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="content">
                                    <label>已上传文件数量：</label><span id="loadfile_had"></span><br />
                                    <label>待上传文件数量：</label><span id="loadfile_need"></span><br />
                                    <label>传输文件的速度：</label><span id="loadfile_speed"></span><br />
                                    <label>当前已用的时间：</label><span id="loadfile_time"></span>
                                </div>
                            </div>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                    <div class="box box-default" id="loadchart" style="display: none;">
                        <div class="box-header with-border">
                            <i class="fa fa-tv"></i>
                            <h3 class="box-title">实时文件上传折线图</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="content">
                                <div id="filecanvasDiv" style="width: 100%;height: 400px"></div>
                            </div>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>
                <!-- /.col -->
                <div class="col-md-4">
                    <div class="box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title">创建新传输结果文件夹</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <div class="alert alert-info">
                                            <span>当执行新的传输结果记录时，请在执行前点击创建新传输结果文件夹</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="text-center">
                                <input id="create_dir" type="button" class="btn btn-info btn-fill btn-wd" value="创建文件夹" onclick="createdir()"/>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                    </div>

                    <div class="box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title">确认信息</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label>上传文件路径</label>
                                        <input id="filepath_show" type="text" disabled="true"  class="form-control border-input" value="<%=session.getAttribute("filepath") %>"/>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label>上传数据库名称</label>
                                        <input id="dbname_show" type="text" disabled="true" class="form-control border-input" value="<%=session.getAttribute("dbname") %>"/>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label>上传集合名称</label>
                                        <input id="clname_show" type="text" disabled="true" class="form-control border-input" value="<%=session.getAttribute("metadbname") %>"/>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="text-center">
                                    <input type="button" class="btn btn-info btn-fill btn-wd" value="修改信息" onclick="upgrade()"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input type="button" class="btn btn-info btn-fill btn-wd" value="更新信息" onclick="recheck()"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
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
<script src="diyjs/flavr.min.js"></script>
<script src="diyjs/common.js"></script>
<script src="diyjs/transload.js"></script>
<script type="text/javascript">
    $(function(){
        <%
           if(session.getAttribute("dbport") == null){%>
        new $.flavr({
            animateEntrance  : 'tada',
            content     : '未检测到数据库连接，请返回数据库连接重新连接数据库',
            buttons     : {
                primary : { text: '数据库连接', style: 'primary',
                    action: function(){
                        $(location).attr('href', 'dblink.jsp');
                    }
                },
                close   : { text:'确定已连接',style: 'default' }
            }
        });

        <%
        }
        else{ %>
        var dbport = <%=session.getAttribute("dbport") %>
        <%}
    %>
    });
</script>
</body>
</html>

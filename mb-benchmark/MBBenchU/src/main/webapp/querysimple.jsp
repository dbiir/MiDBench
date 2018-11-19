<%@page import="java.io.*"%>
<%@ page import="java.util.ArrayList" %>
<%@ page import="cn.ruc.edu.basecore.FileFunction" %>
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
    <title>简单查询负载测试</title>
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
                <li class="treeview active">
                    <a href="#">
                        <i class="fa fa-laptop"></i>
                        <span>查询负载测试</span>
                        <span class="pull-right-container">
                            <i class="fa fa-angle-left pull-right"></i>
                        </span>
                    </a>
                    <ul class="treeview-menu">
                        <li class="active"><a href="querysimple.jsp"><i class="fa fa-circle-o"></i> 简单查询负载测试</a></li>
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
                简单查询负载测试
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
                            <h3 class="box-title">确认数据生成记录</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">

                            <div class="form-group">
                                <div>
                                    <%
                                        String locationPath = application.getRealPath("/");
                                        File historyFile = new File(locationPath + "/WEB-INF/classes/HistorySettings");
                                        if(!historyFile.exists())
                                            historyFile.mkdirs();
                                        File fileArray[] = historyFile.listFiles();
                                        String[] fileNameList = new String[fileArray.length];
                                        for(int i = 0; i < fileNameList.length; i++){
                                            String name = fileArray[i].getName();
                                            fileNameList[i] = name.substring(0, name.length() - 4);
                                        }
                                        String[] sortedFileName = FileFunction.sortedNumberTypeFileNameArray(fileNameList);
                                        for(int i = 0; i < 3 && i < sortedFileName.length; i++){
                                            String name = sortedFileName[i];
                                    %>
                                    <label class="radio-inline">
                                        <input type="radio" name="historySettings" id="<%=name%>" value="<%=name%>"><%=name%>
                                    </label>
                                    <%
                                        }
                                    %>
                                </div>
                            </div>

                            <div class="text-center">
                                <input type="button" class="btn btn-info btn-fill btn-wd" value="确认信息" onclick="setFileName()"/>
                            </div>
                            <div class="clearfix"></div>

                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->

                    <div class="box box-default">
                        <div class="box-header with-border">
                            <i class="fa fa-tv"></i>
                            <h3 class="box-title">单属性查询测试</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <div class="alert alert-info">
                                            <span><b>单属性查询共分三个查询：单个数值查询，有限元数值查询，范围数值查询。</b></span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <div class="alert alert-info">
                                            <span><b>1.&nbsp; 针对单一属性的单个数值查询:</b>查询属性：文件类型；查询值：BLADE</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <div class="alert alert-info">
                                            <span><b>2.&nbsp; 针对单一属性的有限元数值查询:</b>查询属性：地区；查询值：XiBei、XiNan、QingZangGaoYuan</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <div class="alert alert-info">
                                            <span><b>3.&nbsp; 针对单一属性的范围数值查询:</b>查询属性：仿真时间；查询值：请设定</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <div class="alert alert-danger">
                                            <span>请确认数据库已连接成功，如果未连接或者连接失败，请返回<b>数据库连接</b>重新操作</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="text-center">
                                <input id="singlequery"type="button" class="btn btn-info btn-fill btn-wd" value="开始查询" onclick="singlequery()"/>
                                <br/><span id="workload_a_result" style="color: green;">当前未执行本负载</span>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->

                    <div class="box box-default">
                        <div class="box-header with-border">
                            <i class="fa fa-tv"></i>
                            <h3 class="box-title">多属性查询测试</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <div class="alert alert-info">
                                            <span><b>多属性查询共分四个查询：单个数值查询，有限元数值查询，范围数值查询，多类型数值查询。</b></span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <div class="alert alert-info">
                                            <span><b>1.&nbsp; 针对多属性的单个数值查询:</b>查询属性：地区&文件类型；查询值：“DongBei”&“BLADE”</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <div class="alert alert-info">
                                            <span><b>2.&nbsp; 针对多属性的有限元数值查询:</b>查询属性：地区&文件类型；查询值：地区为“HuaBei”或“DongBei”，文件类型为BLADE</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <div class="alert alert-info">
                                            <span><b>3.&nbsp; 针对多属性的范围数值查询:</b>查询属性：塔架高度&扇叶长度；查询值：自动选择合理的塔架高度和扇叶长度范围</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <div class="alert alert-info">
                                            <span><b>4.&nbsp; 针对多属性的多类型数值查询:</b>查询属性：地区&文件类型&塔架高度；查询值：时间：地区为“DongBei”或“HuaBei”，文件类为BLADE，塔架高度自动选择合理的范围值</span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="text-center">
                                <input id="singlequery2" type="button" class="btn btn-info btn-fill btn-wd" value="开始查询" onclick="singleQuery2()"/>
                                <br/><span id="workload_b_result" style="color: green;">当前未执行本负载</span>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->

                    <div class="box box-default">
                        <div class="box-header with-border">
                            <i class="fa fa-tv"></i>
                            <h3 class="box-title">数据文件版本管理</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <div class="alert alert-info">
                                            <span><b>1.&nbsp; 查询每个文件的版本个数【不执行文件下载】</b></span>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label>请输入生成文件的存储总路径</label>
                                        <input id="datapath" type="text" class="form-control border-input" placeholder="请输入路径" />
                                    </div>
                                </div>
                            </div>

                            <div class="text-center">
                                <input id="singlequery3" type="button" class="btn btn-info btn-fill btn-wd" value="开始查询" onclick="singleQuery3()"/>
                                <br/><span id="workload_c_result" style="color: green;">当前未执行本负载</span>
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                </div>
                <!-- /.col -->
                <div class="col-md-4">
                    <div class="box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title">创建新负载结果文件夹</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <div class="alert alert-info">
                                            <span>当执行新的查询负载结果记录时，请在执行前点击创建新负载结果文件夹</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="text-center">
                                <input id="create_querydir" type="button" class="btn btn-info btn-fill btn-wd" value="创建文件夹" onclick="createquerydir()"/>
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
                                        <label>查询文件下载路径</label>
                                        <input id="chaxunpath_show" type="text" disabled="true"  class="form-control border-input" value="<%=session.getAttribute("chaxunpath") %>"/>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label>数据库名称</label>
                                        <input id="dbname_show" type="text" disabled="true" class="form-control border-input" value="<%=session.getAttribute("dbname") %>"/>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label>集合名称</label>
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

                    <div class="box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title">数据生成记录</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <p class="description text-center">
                                        目前只选取按照时间排序的前3个数据生成记录<br />
                                        该信息用于负载参数设置，请查验该数据生成记录是否为当前负载所测试的数据
                                    </p>
                                </div>
                            </div>
                            <hr>
                            <div class="form-group">
                                <div class="nav-tabs-navigation" style="width:90%;">
                                    <div class="nav-tabs-wrapper" style="margin: 0 auto;">
                                        <%
                                            String jspPath = application.getRealPath("/");
                                            File ss = new File(jspPath + "/WEB-INF/classes/HistorySettings");
                                            if(!ss.exists())
                                                ss.mkdirs();
                                            File filelistFile[] = ss.listFiles();
                                            if(filelistFile.length > 0)
                                            {
                                        %>
                                        <ul id="tabs" class="nav nav-tabs" data-tabs="tabs">
                                            <%
                                                File files = null;
                                                for(int i = 0; i < 3 && filelistFile.length - 1 - i >= 0; i++)
                                                {
                                                    files = filelistFile[filelistFile.length - 1 - i];
                                                    String fileName = files.getName();
                                                    if(i == 0)
                                                    {
                                            %>
                                            <li class="active"><a href="#<%=i %>" data-toggle="tab"><%=fileName.substring(0, fileName.length() - 4) %></a></li>
                                            <%			}
                                            else
                                            {
                                            %>
                                            <li><a href="#<%=i %>" data-toggle="tab"><%=fileName.substring(0, fileName.length() - 4) %></a></li>
                                            <%
                                                    }
                                                }
                                            %>
                                        </ul>
                                    </div>
                                </div>
                                <div id="my-tab-content" class="tab-content text-center">
                                    <%
                                        for(int i = 0; i < 3 && filelistFile.length - 1 - i >= 0; i++)
                                        {
                                            files = filelistFile[filelistFile.length - 1 - i];
                                            BufferedReader bfR = new BufferedReader(new InputStreamReader(new FileInputStream(files), "GB2312"));
                                            String str = null;
                                            String allstr = "\r";
                                            while((str = bfR.readLine()) != null)
                                                allstr += str + "\n";
                                            if(i == 0)
                                            {
                                    %>
                                    <textarea class="tab-pane active" id="<%=i %>" style="scrollbar-3dlight-color: grey; scrollbar-base-color: #00BBFF; width: 100%; height: 300px;">
											<%=allstr %>
										</textarea>
                                    <%
                                    }
                                    else
                                    {
                                    %>
                                    <textarea class="tab-pane" id="<%=i %>" style="scrollbar-3dlight-color: grey; scrollbar-base-color: #00BBFF; width: 100%; height: 300px;">
										<%=allstr %>
									</textarea>
                                    <%
                                            }
                                        }
                                    }
                                    else
                                    {
                                    %>
                                </div>
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <label>&nbsp</label>
                                        <div class="alert alert-info" style="text-align:center;">
                                            <span><%="未检测到/HistorySettings文件夹下有仿真生成设置文件，请查证后再点击" %></span>
                                        </div>
                                    </div>
                                </div>
                                <%
                                    }
                                %>
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
<script src="diyjs/querysimple.js"></script>
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

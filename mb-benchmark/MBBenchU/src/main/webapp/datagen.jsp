<%@page import="java.io.*"%>
<%@ page import="java.net.URL" %>
<%--
  Created by IntelliJ IDEA.
  User: Daniel
  Date: 2018/9/18
  Time: 10:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>海量文件生成</title>
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
                <li class="active">
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
                海量文件生成
                <small>便捷、快速生成想要的数据</small>
            </h1>
        </section>
        <!-- Main content -->
        <section class="content">
            <div class="row">
                <!-- left column -->
                <div class="col-md-8">
                    <!-- general form elements -->
                    <div class="box box-primary">
                        <div class="box-header with-border">
                            <h3 class="box-title">生成参数设置</h3>
                        </div>
                        <!-- /.box-header -->
                        <!-- form start -->
                        <form role="form">
                            <div class="box-body">
                                <div class="row">
                                    <div class="col-xs-7">
                                        <div class="form-group">
                                            <label for="filepath">输入文件存储总路径</label>
                                            <input type="text" class="form-control" id="filepath" placeholder="请输入路径">
                                        </div>
                                    </div>
                                    <div class="col-xs-5">
                                        <div class="form-group">
                                            <label>选择元文件格式</label>
                                            <div class="form-group">
                                                <div class="checkbox">
                                                    <label>
                                                        <input name="metatype" type="checkbox" value="json">
                                                        JSON
                                                    </label>
                                                </div>
                                                <div class="checkbox">
                                                    <label>
                                                        <input name="metatype" type="checkbox" value="xml">
                                                        XML
                                                    </label>
                                                </div>
                                                <div class="checkbox">
                                                    <label>
                                                        <input name="metatype" type="checkbox" value="txt">
                                                        TXT
                                                    </label>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-3">
                                        <div class="form-group">
                                            <label for="devicenum">设置设备数量</label>
                                            <input id="devicenum" type="number" class="form-control border-input" placeholder="数量建议不要过小" />
                                        </div>
                                    </div>
                                    <div class="col-xs-3">
                                        <div class="form-group">
                                            <label for="eachfiles">设置文件数量</label>
                                            <input id="eachfiles" type="number" class="form-control border-input" placeholder="单个设备生成数量" />
                                        </div>
                                    </div>
                                    <div class="col-xs-3">
                                        <div class="form-group">
                                            <label for="changenum">设置更新文件数量</label>
                                            <input id="changenum" type="number" class="form-control border-input" placeholder="数量要小于文件数量" />
                                        </div>
                                    </div>
                                    <div class="col-xs-3">
                                        <div class="form-group">
                                            <label for="unnormal">设置异常文件占比</label>
                                            <input id="unnormal" type="number" class="form-control border-input" placeholder="例如：10" />
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-xs-3">
                                        <div class="form-group">
                                            <label for="towerheigh">设置塔架高度区间</label>
                                            <input id="towerheigh" type="text" class="form-control border-input" placeholder="如60-80" />
                                        </div>
                                    </div>
                                    <div class="col-xs-3">
                                        <div class="form-group">
                                            <label for="bladelength">设置扇叶长度区间</label>
                                            <input id="bladelength" type="text" class="form-control border-input" placeholder="如40-45" />
                                        </div>
                                    </div>
                                    <div class="col-xs-3">
                                        <div class="form-group">
                                            <label for="bladewidth">设置扇叶宽度区间</label>
                                            <input id="bladewidth" type="text" class="form-control border-input" placeholder="如5-15" />
                                        </div>
                                    </div>
                                    <div class="col-xs-3">
                                        <div class="form-group">
                                            <label>使用已有的配置</label>
                                            <input type="button" class="btn btn-block btn-info" onclick="checkhide()" id="usedcheck" value="使用" />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                    <!-- /.box -->

                    <!-- Form Element sizes -->
                    <div class="box box-success">
                        <div class="box-header with-border">
                            <h3 class="box-title">仿真文件大小设置</h3>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-xs-3">
                                    <div class="form-group">
                                        <label for="filesizenum">设置文件分配数量</label>
                                        <input id="filesizenum" type="number" class="form-control border-input" placeholder="请输入分配数量" />
                                    </div>
                                </div>
                                <div class="col-xs-3">
                                    <div class="form-group">
                                        <label for="confirmsize">生成设置文件控件</label>
                                        <input id="confirmsize" type="button" class="btn btn-block btn-info" onclick="filesizeconfirm()" value="生成设置控件" />
                                    </div>
                                </div>
                                <div class="col-xs-6">
                                    <div class="form-group">
                                        <label>提示</label>
                                        <p class="text-green">如重新填写，需刷新页面</p>
                                    </div>
                                </div>
                            </div>

                            <div id="filesizeget">

                            </div>

                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->


                    <div class="box box-danger" id="notused">
                        <div class="box-header with-border">
                            <h3 class="box-title">仿真时间设置</h3>
                        </div>
                        <div class="box-body">
                            <div class="row">
                                <div class="col-xs-3">
                                    <div class="form-group">
                                        <label>#仿真开始时间（日期）</label>
                                        <input id="simustartdate" type="date" class="form-control border-input" value="2015-09-24"/>
                                    </div>
                                </div>
                                <div class="col-xs-3">
                                    <div class="form-group">
                                        <label>#仿真开始时间（时）</label>
                                        <input id="simustarthour" type="number" class="form-control border-input" placeholder="请输入小时" value="0"/>
                                    </div>
                                </div>
                                <div class="col-xs-3">
                                    <div class="form-group">
                                        <label>#仿真开始时间（分）</label>
                                        <input id="simustartmins" type="number" class="form-control border-input" placeholder="请输入分钟" value="0"/>
                                    </div>
                                </div>
                                <div class="col-xs-3">
                                    <div class="form-group">
                                        <label>#仿真开始时间（秒）</label>
                                        <input id="simustartsecs" type="number" class="form-control border-input" placeholder="请输入秒钟" value="0"/>
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-3">
                                    <div class="form-group">
                                        <label>#仿真结束时间（日期）</label>
                                        <input id="simuenddate" type="date" class="form-control border-input" value="2015-09-24"/>
                                    </div>
                                </div>
                                <div class="col-xs-3">
                                    <div class="form-group">
                                        <label>#仿真结束时间（时）</label>
                                        <input id="simuendhour" type="number" class="form-control border-input" placeholder="请输入小时" value="0"/>
                                    </div>
                                </div>
                                <div class="col-xs-3">
                                    <div class="form-group">
                                        <label>#仿真结束时间（分）</label>
                                        <input id="simuendmins" type="number" class="form-control border-input" placeholder="请输入分钟" value="0"/>
                                    </div>
                                </div>
                                <div class="col-xs-3">
                                    <div class="form-group">
                                        <label>#仿真结束时间（秒）</label>
                                        <input id="simuendsecs" type="number" class="form-control border-input" placeholder="请输入秒钟" value="0"/>
                                    </div>
                                </div>
                            </div>
                            <div class="text-center">
                                <input type="button" class="btn btn-info btn-fill btn-wd" value="刷新信息" onclick="refresh()" />&nbsp&nbsp&nbsp&nbsp
                                <input id="btnSubmit" type="button" class="btn btn-info btn-fill btn-wd" value="生成文件" onclick="datagen()" />
                            </div>
                            <div class="clearfix"></div>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->

                    <!-- Input addon -->
                    <div id="isused" style="display: none;">
                        <div class="box box-info">
                            <div class="box-header with-border">
                                <h3 class="box-title">生成增量数据集</h3>
                            </div>
                            <div class="box-body">
                                <div class="form-group">
                                    <label>当前已有的仿真配置文件</label>
                                    <div class="nav-tabs-navigation">
                                        <div class="nav-tabs-wrapper">
                                            <%
                                                //File ss = new File("../webapps/GHBigBench/HistorySettings")
                                                String jspPath = application.getRealPath("/");
                                                File ss = new File(jspPath + "/WEB-INF/classes/HistorySettings");
                                                //File ss = new File("/HistorySettings");
                                                if(!ss.exists())
                                                    ss.mkdirs();
                                                File filelistFile[] = ss.listFiles();
                                                if(filelistFile.length > 0)
                                                {
                                            %>
                                            <ul id="tabs" class="nav nav-tabs" data-tabs="tabs">
                                                <%
                                                    File files = null;
                                                    for(int i = 0; i < 5 && filelistFile.length - 1 - i >= 0; i++)
                                                    {
                                                        files = filelistFile[filelistFile.length - 1 - i];
                                                        if(i == 0)
                                                        {
                                                %>
                                                <li class="active"><a href="#<%=i %>" data-toggle="tab"><%=files.getName().split(".txt")[0] %></a></li>
                                                <%			}
                                                else
                                                {
                                                %>
                                                <li><a href="#<%=i %>" data-toggle="tab"><%=files.getName().split(".txt")[0] %></a></li>
                                                <%
                                                        }
                                                    }
                                                %>
                                            </ul>
                                        </div>
                                    </div>
                                    <div id="my-tab-content" class="tab-content text-center">
                                        <%
                                            for(int i = 0; i < 5 && filelistFile.length - 1 - i >= 0; i++)
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
                                        <textarea class="tab-pane active" id="<%=i %>" style="scrollbar-3dlight-color: grey; scrollbar-base-color: #00BBFF; width: 90%; height: 200px;">
															<%=allstr %>
														</textarea>
                                        <%
                                        }
                                        else
                                        {
                                        %>
                                        <textarea class="tab-pane" id="<%=i %>" style="scrollbar-3dlight-color: grey; scrollbar-base-color: #00BBFF; width: 90%; height: 200px;">
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
                                                <span><%="未检测到/HistorySettings文件夹下有仿真生成设置文件，请查证后再点击" %></span><hr/>
                                            </div>
                                        </div>
                                    </div>
                                    <%
                                        }
                                    %>
                                </div>
                                <div class="row">
                                    <div class="col-md-12">
                                        <div class="form-group">
                                            <label>选择数据生成内容</label>
                                            <div>
                                                <label class="radio-inline">
                                                    <input type="radio" name="usedselect" id="usedusual" value="usual">按照原设置文件数量继续生成
                                                </label>
                                                <label class="radio-inline">
                                                    <input type="radio" name="usedselect" id="usedupdate" value="update">按照新设置的文件数量生成
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div id="update" style="display: none;">
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label>生成文件数量</label>
                                                <input id="usedupdatenum" type="number" class="form-control border-input"/>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label>&nbsp</label>
                                                <div class="alert alert-info">
                                                    <span>文件路径为前面已设置的内容</span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-md-4">
                                            <div class="form-group">
                                                <label>&nbsp</label>
                                                <div class="alert alert-warning">
                                                    <span>不可超过文件内的文件生成设置数</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="clearfix"></div>
                                <div class="text-center">
                                    <input type="button" class="btn btn-info btn-fill btn-wd" value="确认信息" onclick="usedrefresh()" />&nbsp&nbsp&nbsp&nbsp
                                    <input id="usedbtn" type="button" class="btn btn-info btn-fill btn-wd" value="生成文件" onclick="useddatagen()" />
                                </div>
                                <div class="clearfix"></div>
                            </div>
                        </div>
                    </div>
                    <!-- /.box-body -->
                </div>
            </div>
                    <!-- /.box -->
                <!--/.col (left) -->
                <!-- right column -->
            <div class="row">
                <div class="col-md-4">
                    <!-- Horizontal Form -->
                    <div class="box box-info">
                        <div class="box-header with-border">
                            <h3 class="box-title">海量文件生成工具介绍</h3>
                        </div>
                        <!-- /.box-header -->
                        <!-- form start -->
                        <form class="form-horizontal">
                            <div class="box-body">
                                <p class="description text-center">
                                    海量文件生成工具是一款贴合大数据的特点，
                                    专门生成海量小文件的数据生成器，可用作非结构化数据库及文件管理系统的评测导入数据。
                                    通过使用此文件生成系统，用户可以在较短的时间内，生成一定规模的小型不可读文件及该文
                                    件的元数据文件，减少用户收集数据的时间。其中元数据文件的格式包含当今主流的非结构化
                                    数据库的元数据格式，用户无需更正便可将相应格式的元数据文件导入到数据库内。
                                </p>
                            </div>
                        </form>
                    </div>
                    <!-- /.box -->
                    <!-- general form elements disabled -->
                    <div class="box box-warning">
                        <div class="box-header with-border">
                            <h3 class="box-title">生成文件信息</h3>
                        </div>
                        <!-- /.box-header -->
                        <div class="box-body">
                            <p class="description text-center">
                                当数据均设置完毕，请点击刷新信息<br />
                                会根据所设置内容，显示数据生成信息
                            </p>
                            <hr>
                            <div class="text-center">
                                <div class="row">
                                    <div class="col-md-5 col-md-offset-1">
                                        <h3 id="filesshow">&nbsp<br /><small>文件数量</small></h3>

                                    </div>
                                    <div class="col-md-5">
                                        <h3 id="sizesshow">&nbsp<br /><small>存储容量</small></h3>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- /.box-body -->
                    </div>
                    <!-- /.box -->
                    <div id="status" style="display: none;">
                        <div class="box box-info">
                            <div class="box-header with-border">
                                <h3 class="box-title">实时生成信息</h3>
                            </div>
                            <div class="box-body">
                                <div>
                                    <p>实施进度条</p>
                                    <div class="progress" id="progressBar">
                                        <div class="progress-bar progress-bar-primary progress-bar-striped" role="progressbar" aria-valuenow="0%" aria-valuemin="0" aria-valuemax="100" style="width: 0%;" id="progressBarItem">
                                            <p id="process">0%</p>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div id="statusInfo" class="content">
                                            <label>存储总文件的总路径为：</label><span id="filepathshow"></span><br />
                                            <label>总共二进制文件数量为：</label><span id="fileallnum"></span><br />
                                            <label>生成二进制文件数量为：</label><span id="filenum"></span><br />
                                            <label>当前剩余的文件数量为：</label><span id="fileneednum"></span><br />
                                            <label>当前生成文件的速度为：</label><span id="filegenspeednum"></span><br />
                                            <label>当前生成文件的速度为：</label><span id="filegenspeedsize"></span><br />
                                            <label>当前已经消耗的时间为： </label><span id="hadtime"></span><br />
                                            <label>当前预计结束的时间为： </label><span id="needtime"></span><br />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--/.col (right) -->
            </div>
            <!--end row--->
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
<script src="diyjs/newprogressbar.js"></script>
<script type="text/javascript">
    $(function(){
        var dd = new Date(), date= '';
        date += dd.getFullYear() + '-';
        if((dd.getMonth() + 1) < 10)
            date += '0' + (dd.getMonth() + 1) + '-';
        else
            date += (dd.getMonth() + 1) + '-';
        if(dd.getDate() < 10)
            date += '0' + dd.getDate();
        else
            date += dd.getDate();
        $("#simustartdate").val(date);
        $("#simuenddate").val(date);
        $("input[name=usedselect]").each(function(){
            $(this).click(function(){
                var ss = $(this).val();
                if(ss == "update")
                {
                    $("#update").show();
                    $("#usual").hide();
                }
                else
                {
                    $("#update").hide();
                    $("#usual").show();
                }
            });
        });
    });
</script>
</body>
</html>

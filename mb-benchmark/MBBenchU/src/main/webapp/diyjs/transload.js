function createdir(){
    $.ajax({
        type:"post",
        url:"/makedir",
        async:true,
        data:{"dirtype":"transdir"},
        success:function(data)
        {
            if(data.result == "success"){
                new $.flavr("新传输结果文件夹创建成功");
            }
            else
                new $.flavr("新传输结果文件夹创建失败，请确定已输入结果日志存储路径");
        },
        dataType:"json",
        error:function()
        {

        }
    });
}

function upgrade()
{
    $("#filepath_show").removeAttr("disabled");
    $("#dbname_show").removeAttr("disabled");
    $("#clname_show").removeAttr("disabled");
}

function recheck()
{
    $("#filepath_show").attr("disabled", "disabled");
    $("#dbname_show").attr("disabled", "disabled");
    $("#clname_show").attr("disabled", "disabled");
}

function loadtest()
{
    Array.prototype.max = function(){
        return Math.max.apply({},this);
    };
    var loadpath = $("#filepath_show").val();
    $("#loadstatus").show();
    $("#loadchart").show();
    $("#fileupload").attr("disabled","true");
    $("#loadmetastatus").hide();
    $("#metachart").hide();

    $.ajax({
        type:"post",
        url:"/dbservlet",
        async:true,
        data:{"uploadpath":loadpath, "functiontype":"loadfile", "statetype":"init"},
        success:function(data)
        {
        },
        dataType:"json",
        error:function()
        {

        }
    });

    DrawAutoLoadChart(loadpath, "文件上传实时折线图", "loadfile", "filecanvasDiv", "MB/S");

    /*
    var loadtime = 0;
    var loadsettimer = null;
    var loadfile = 0;
    var zongfile = 1;
    var labels = [];
    var loadmax = 0;
    loadsettimer = setInterval(function(){
        $.ajax({
            type:"post",
            url:"/dbservlet",
            async:true,
            data:{"loadpath":loadpath, "functiontype":"loadfile", "statetype":"running"},
            success:function(data)
            {
                loadtime++;

                loadfile = parseInt(data.loadfile);
                zongfile = parseInt(data.zongfile);
                var pro = (loadfile / zongfile)*100;
                $("#progressBarItemload").attr('aria-valuenow', pro.toFixed(2) + "%").css('width', pro.toFixed(2) + "%");
                $("#processload").text(pro.toFixed(2) + "%");
                $("#loadfile_had").text(loadfile+"个");
                $("#loadfile_need").text((zongfile - loadfile) + "个");
                $("#loadfile_speed").text((parseInt(data.loadfilespread) / (1024 * 1024)).toFixed(2) + "MB/秒");
                $("#loadfile_time").text(timeformat(loadtime));

                var flow = [];
                var dataObj = data.arrays;
                $.each(dataObj, function(index, item)
                {
                    flow.push(parseFloat(item.speed));
                });
                loadmax = flow.max();
                var data = [
                    {
                        name : 'speed:',
                        value:flow,
                        color:'#0d8ecf',
                        line_width:2
                    }
                ];
                if(loadtime % 10 == 1){
                    var date = new Date();
                    if(labels.length >= 6)
                        labels.shift(); //用于删除数组内的第一个元素
                    labels.push(date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
                }

                var chart = new iChart.LineBasic2D({
                    render : 'filecanvasDiv',
                    data: data,
                    align:'center',
                    title : {
                        text:'数据文件上传实时折线图',
                        fontsize:24,
                        color:'#f7f7f7'
                    },
                    subtitle : {
                        text:'数据的单位是 MB/S',
                        color:'#f1f1f1'
                    },
                    footnote : {
                        text:'数据点以秒为单位',
                        color:'#f1f1f1'
                    },
                    width : 800,
                    height : 400,
                    shadow:true,
                    shadow_color : '#20262f',
                    shadow_blur : 4,
                    shadow_offsetx : 0,
                    shadow_offsety : 2,
                    background_color:'#383e46',
                    tip:{
                        enable:true,
                        shadow:true
                    },
                    crosshair:{
                        enable:true,
                        line_color:'#62bce9'
                    },
                    sub_option : {
                        label:false,
                        hollow_inside:false,
                        point_size:8
                    },
                    coordinate:{
                        width:640,
                        height:260,
                        grid_color:'#cccccc',
                        axis:{
                            color:'#cccccc',
                            width:[0,0,2,2]
                        },
                        grids:{
                            vertical:{
                                way:'share_alike',
                                value:5
                            }
                        },
                        scale:[{
                            position:'left',
                            start_scale:0,
                            end_scale:loadmax,
                            scale_space:10,
                            scale_size:2,
                            label : {color:'#ffffff',fontsize:11},
                            scale_color:'#9f9f9f'
                        },{
                            position:'bottom',
                            label : {color:'#ffffff',fontsize:11},
                            labels:labels
                        }]
                    }
                });
                //开始画图
                chart.draw();
            },
            dataType:"json",
            error:function()
            {
                //clearInterval(loadsettimer);
            }
        });
        if(loadfile == zongfile || $("#progressBarItemload").attr('aria-valuenow') == "100%")
        {
            $("#progressBarItemload").attr('aria-valuenow', "100%").css('width', "100%");
            $("#processload").text("100%");
            clearInterval(loadsettimer);
        }
    },1000);
    */
}

function loadmetatest()
{
    Array.prototype.max = function(){
        return Math.max.apply({},this);
    };
    var loadpath = $("#filepath_show").val();

    $("#loadstatus").hide();
    $("#loadchart").hide();
    $("#filemetaupload").attr("disabled","true");
    $("#loadmetastatus").show();
    $("#metachart").show();

    $.ajax({
        type:"post",
        url:"/dbservlet",
        async:true,
        data:{"uploadpath":loadpath, "functiontype":"loadmeta", "statetype":"init"},
        success:function(data)
        {
        },
        error:function()
        {
        }
    });
    DrawAutoLoadMetaChart(loadpath, "文档上传实时折线图", "loadmeta", "metacanvasDiv", "Per/S")
    /*
    var loadmeta = 0;
    var zongmeta = 1;
    var loadmetatime = 0;
    var loadmetasettimer = null;
    var labels = [];
    var metamax = 0;
    loadmetasettimer = setInterval(function(){
        $.ajax({
            type:"post",
            url:"/dbservlet",
            async:true,
            data:{"loadpath":loadpath, "functiontype":"loadmeta", "statetype":"running"},
            success:function(data)
            {

                loadmetatime++;
                loadmeta = parseInt(data.loadmeta);
                zongmeta = parseInt(data.zongmeta);
                var pro = (loadmeta / zongmeta)*100;
                $("#progressBarItemloadmeta").attr('aria-valuenow', pro.toFixed(2) + "%").css('width', pro.toFixed(2) + "%");
                $("#processloadmeta").text(pro.toFixed(2) + "%");
                $("#loadfile_hadmeta").text(loadmeta + "个");
                $("#loadfile_needmeta").text((zongmeta - loadmeta) + "个");
                $("#loadfile_speedmeta").text(data.loadmetaspread + "个/秒");
                $("#loadfile_timemeta").text(timeformat(loadmetatime));

                va
                var flow = [];
                var dataObj = data.arrays;
                $.each(dataObj, function(index, item)
                {
                    flow.push(parseFloat(item.speed));
                });

                metamax = flow.max();

                var data = [
                    {
                        name : 'speed:',
                        value:flow,
                        color:'#0d8ecf',
                        line_width:2
                    }
                ];
                if(loadmetatime % 10 == 1){
                    var date = new Date();
                    if(labels.length >= 6)
                        labels.shift(); //用于删除数组内的第一个元素
                    labels.push(date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
                }

                var chart = new iChart.LineBasic2D({
                    render : 'metacanvasDiv',
                    data: data,
                    align:'center',
                    title : {
                        text:'数据文档上传实时折线图',
                        fontsize:24,
                        color:'#f7f7f7'
                    },
                    subtitle : {
                        text:'数据的单位是 Per/S',
                        color:'#f1f1f1'
                    },
                    footnote : {
                        text:'数据点以秒为单位',
                        color:'#f1f1f1'
                    },
                    width : 800,
                    height : 400,
                    shadow:true,
                    shadow_color : '#20262f',
                    shadow_blur : 4,
                    shadow_offsetx : 0,
                    shadow_offsety : 2,
                    background_color:'#383e46',
                    tip:{
                        enable:true,
                        shadow:true
                    },
                    crosshair:{
                        enable:true,
                        line_color:'#62bce9'
                    },
                    sub_option : {
                        label:false,
                        hollow_inside:false,
                        point_size:8
                    },
                    coordinate:{
                        width:640,
                        height:260,
                        grid_color:'#cccccc',
                        axis:{
                            color:'#cccccc',
                            width:[0,0,2,2]
                        },
                        grids:{
                            vertical:{
                                way:'share_alike',
                                value:5
                            }
                        },
                        scale:[{
                            position:'left',
                            start_scale:0,
                            end_scale:metamax,
                            scale_space:10,
                            scale_size:2,
                            label : {color:'#ffffff',fontsize:11},
                            scale_color:'#9f9f9f'
                        },{
                            position:'bottom',
                            label : {color:'#ffffff',fontsize:11},
                            labels:labels
                        }]
                    }
                });
                //开始画图
                chart.draw();
            },
            dataType:"json",
            error:function()
            {
                //clearInterval(loadmetasettimer);
            }
        });
        if(loadmeta == zongmeta || $("#progressBarItemloadmeta").attr('aria-valuenow') == "100%")
        {
            $("#progressBarItemloadmeta").attr('aria-valuenow', "100%").css('width', "100%");
            $("#processloadmeta").text("100%");
            clearInterval(loadmetasettimer);
        }
    },1000);
    */
}

function DrawAutoLoadChart(loadpath, title, functiontype, goalid, danwei) {
    var currentnum = 0;
    var zongnum = 1;
    var time = 0;
    var settimer = null;
    var chart = echarts.init(document.getElementById(goalid));
    var option = {
        title: {
            text: title,
            subtext: '仅供参考'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#283b56'
                }
            }
        },
        legend: {
            data:['最新速度']
        },
        toolbox: {
            show: true,
            feature: {
                dataView: {readOnly: false},
                restore: {},
                saveAsImage: {}
            }
        },
        dataZoom: {
            show: false,
            start: 0,
            end: 100
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: true,
                data: (function (){
                    var now = new Date();
                    var res = [];
                    var len = 50;
                    while (len--) {
                        res.unshift(now.toLocaleTimeString().replace(/^\D*/,''));
                        now = new Date(now - 1000);
                    }
                    return res;
                })()
            }
        ],
        yAxis: [
            {
                type: 'value',
                scale: true,
                name: '速度--' + danwei,
                min:0,
                boundaryGap: [0.2, 0.2]
            }
        ],
        series: [
            {
                name:'当前速度',
                type:'line',
                data:(function (){
                    var res = [];
                    var len = 0;
                    while (len < 50) {
                        res.push(-1);
                        len++;
                    }
                    return res;
                })()
            }
        ]
    };

    chart.setOption(option);

    settimer = setInterval(function(){
        $.ajax({
            type:"post",
            url:"/dbservlet",
            async:true,
            data:{"loadpath":loadpath, "functiontype":functiontype, "statetype":"running"},
            success:function(data)
            {
                time++;
                currentnum = parseInt(data.loadfile);
                zongnum = parseInt(data.zongfile);
                var pro = (currentnum / zongnum)*100;
                $("#progressBarItemload").attr('aria-valuenow', pro.toFixed(2) + "%").css('width', pro.toFixed(2) + "%");
                $("#processload").text(pro.toFixed(2) + "%");
                $("#loadfile_had").text(currentnum + "个");
                $("#loadfile_need").text((zongnum - currentnum) + "个");
                $("#loadfile_speed").text((parseInt(data.loadfilespread) / (1024 * 1024)).toFixed(2)+ danwei);
                $("#loadfile_time").text(timeformat(time));

                var axisData = (new Date()).toLocaleTimeString().replace(/^\D*/,'');

                var data0 = option.series[0].data;

                data0.shift();
                data0.push(parseFloat((parseInt(data.loadfilespread) / (1024 * 1024)).toFixed(2)));

                option.xAxis[0].data.shift();
                option.xAxis[0].data.push(axisData);

                chart.setOption(option);
            },
            dataType:"json",
            error:function()
            {
                //clearInterval(loadmetasettimer);
            }
        });
        if(currentnum == zongnum || $("#progressBarItemload").attr('aria-valuenow') == "100%")
        {
            $("#progressBarItemload").attr('aria-valuenow', "100%").css('width', "100%");
            $("#processload").text("100%");
            clearInterval(settimer);
        }
    },1000);
}

function DrawAutoLoadMetaChart(loadpath, title, functiontype, goalid, danwei) {
    var currentnum = 0;
    var zongnum = 1;
    var time = 0;
    var settimer = null;

    var chart = echarts.init(document.getElementById(goalid));
    var option = {
        title: {
            text: title,
            subtext: '仅供参考'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'cross',
                label: {
                    backgroundColor: '#283b56'
                }
            }
        },
        legend: {
            data:['最新速度']
        },
        toolbox: {
            show: true,
            feature: {
                dataView: {readOnly: false},
                restore: {},
                saveAsImage: {}
            }
        },
        dataZoom: {
            show: false,
            start: 0,
            end: 100
        },
        xAxis: [
            {
                type: 'category',
                boundaryGap: true,
                data: (function (){
                    var now = new Date();
                    var res = [];
                    var len = 50;
                    while (len--) {
                        res.unshift(now.toLocaleTimeString().replace(/^\D*/,''));
                        now = new Date(now - 1000);
                    }
                    return res;
                })()
            }
        ],
        yAxis: [
            {
                type: 'value',
                scale: true,
                name: '速度--' + danwei,
                min:0,
                boundaryGap: [0.2, 0.2]
            }
        ],
        series: [
            {
                name:'当前速度',
                type:'line',
                data:(function (){
                    var res = [];
                    var len = 0;
                    while (len < 50) {
                        res.push(-1);
                        len++;
                    }
                    return res;
                })()
            }
        ]
    };

    chart.setOption(option);

    settimer = setInterval(function(){
        $.ajax({
            type:"post",
            url:"/dbservlet",
            async:true,
            data:{"loadpath":loadpath, "functiontype":functiontype, "statetype":"running"},
            success:function(data)
            {
                time++;
                currentnum = parseInt(data.loadmeta);
                zongnum = parseInt(data.zongmeta);
                var pro = (currentnum / zongnum)*100;
                $("#progressBarItemloadmeta").attr('aria-valuenow', pro.toFixed(2) + "%").css('width', pro.toFixed(2) + "%");
                $("#processloadmeta").text(pro.toFixed(2) + "%");
                $("#loadfile_hadmeta").text(currentnum + "个");
                $("#loadfile_needmeta").text((zongnum - currentnum) + "个");
                $("#loadfile_speedmeta").text(data.loadmetaspread + danwei);
                $("#loadfile_timemeta").text(timeformat(time));

                var axisData = (new Date()).toLocaleTimeString().replace(/^\D*/,'');
                var data0 = option.series[0].data;
                data0.shift();
                data0.push((parseInt(data.loadmetaspread)));
                option.xAxis[0].data.shift();
                option.xAxis[0].data.push(axisData);

                chart.setOption(option);
            },
            dataType:"json",
            error:function()
            {
                //clearInterval(loadmetasettimer);
            }
        });
        if(currentnum == zongnum || $("#progressBarItemload").attr('aria-valuenow') == "100%")
        {
            $("#progressBarItemload").attr('aria-valuenow', "100%").css('width', "100%");
            $("#processload").text("100%");
            clearInterval(settimer);
        }
    },1000);
}
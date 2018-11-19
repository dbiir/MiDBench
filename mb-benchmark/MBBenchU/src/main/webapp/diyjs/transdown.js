function downfileinfo(){
    $.ajax({
        type:"post",
        url:"/dbservlet",
        async:true,
        data:{"functiontype":"getdbfileinfo"},
        success:function(data)
        {
            $("#filesshow").text(data.filenum + "个");
            $("#sizesshow").text(data.datasize + "GB");
        },
        dataType:"json",
        error:function()
        {

        }
    });
}

function downtest()
{
    Array.prototype.max = function(){
        return Math.max.apply({},this);
    };
    var downpath = $("#downpath").val();

    $("#downstatus").show();
    $("#downchart").show();
    $("#filedown").attr("disabled","true");
    $("#downstatusmeta").hide();
    $("#metachart").hide();

    $.ajax({
        type:"post",
        url:"/dbservlet",
        async:true,
        data:{"downpath":downpath, "functiontype":"downfile", "statetype":"init"},
        success:function(data)
        {

        },
        dataType:"json",
        error:function()
        {

        }
    });
    DrawAutoDownChart(downpath, "文件下载实时折线图", "downfile", "downfilecanvasDiv", "MB/S");
    /*
    var downfile = 0;
    var zongdown = 1;
    var downtime = 0;
    var downsettimer = null;
    var labels = [];
    var downmax = 0;
    downsettimer = setInterval(function(){
        $.ajax({
            type:"post",
            url:"/dbservlet",
            async:true,
            data:{"downpath":downpath, "functiontype":"downfile", "statetype":"running"},
            success:function(data)
            {
                downtime = downtime + 1;
                downfile = parseInt(data.downfile);
                zongdown = parseInt(data.downfileall);
                var pro = (downfile / zongdown) * 100;
                $("#progressBarItemdown").attr('aria-valuenow', pro.toFixed(2) + "%").css('width', pro.toFixed(2) + "%");
                $("#processdown").text(pro.toFixed(2) + "%");
                $("#downfile_had").text(downfile + "个");
                $("#downfile_need").text(zongdown - downfile + "个");
                $("#downfile_speed").text((parseInt(data.downfilespread) / (1024 * 1024)).toFixed(2) + "MB/秒");
                $("#downfile_time").text(timeformat(downtime));

                var flow = [];
                var dataObj = data.arrays;
                $.each(dataObj, function(index, item)
                {
                    flow.push(parseFloat(item.speed));
                });

                downmax = flow.max();
                var data = [
                    {
                        name : 'speed:',
                        value:flow,
                        color:'#0d8ecf',
                        line_width:2
                    }
                ];
                if(downtime % 10 == 1){
                    var date = new Date();
                    if(labels.length >= 6)
                        labels.shift(); //用于删除数组内的第一个元素
                    labels.push(date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
                }

                var chart = new iChart.LineBasic2D({
                    render : 'downfilecanvasDiv',
                    data: data,
                    align:'center',
                    title : {
                        text:'数据文件下载实时折线图',
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
                            end_scale:downmax,
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
                //clearInterval(downsettimer);
            }
        });
        if(downfile == zongdown || $("#progressBarItemdown").attr('aria-valuenow') == "100%")
        {
            $("#progressBarItemdown").attr('aria-valuenow', "100%").css('width', "100%");
            $("#processdown").text("100%");
            clearInterval(downsettimer);
        }
    },1000);
    */
}

function downmetatest()
{
    Array.prototype.max = function(){
        return Math.max.apply({},this);
    };
    var downpath = $("#downpath").val();
    $("#downstatus").hide();
    $("#downchart").hide();
    $("#filedownmeta").attr("disabled","true");
    $("#downstatusmeta").show();
    $("#metachart").show();

    $.ajax({
        type:"post",
        url:"/dbservlet",
        async:true,
        data:{"downpath":downpath, "functiontype":"downmeta", "statetype":"init"},
        success:function(data)
        {

        },
        dataType:"json",
        error:function()
        {

        }
    });
    DrawAutoDownMetaChart(downpath, "文档下载实时折线图", "downmeta", "downmetacanvasDiv", "Per/S");
    /*
    var downmeta = 0;
    var downzong = 1;
    var downtimemeta = 0;
    var downmetasettimer = null;
    var metamax = 0;
    var labels = [];
    downmetasettimer = setInterval(function(){
        $.ajax({
            type:"post",
            url:"/dbservlet",
            async:true,
            data:{"downpath":downpath, "functiontype":"downmeta", "statetype":"running"},
            success:function(data)
            {
                downtimemeta = downtimemeta + 1;
                downmeta = parseInt(data.downmeta);
                downzong = parseInt(data.downmetaall);
                var pro = (downmeta / downzong) * 100;
                $("#progressBarItemdownmeta").attr('aria-valuenow', pro.toFixed(2) + "%").css('width', pro.toFixed(2) + "%");
                $("#processdownmeta").text(pro.toFixed(2) + "%");
                $("#downfile_hadmeta").text(downmeta + "个");
                $("#downfile_needmeta").text((downzong - downmeta) + "个");
                $("#downfile_speedmeta").text(data.downmetaspread+"件/秒");
                $("#downfile_timemeta").text(timeformat(downtimemeta));

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
                if(downtimemeta % 10 == 1){
                    var date = new Date();
                    if(labels.length >= 6)
                        labels.shift(); //用于删除数组内的第一个元素
                    labels.push(date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
                }

                var chart = new iChart.LineBasic2D({
                    render : 'downmetacanvasDiv',
                    data: data,
                    align:'center',
                    title : {
                        text:'数据文档下载实时折线图',
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
                //clearInterval(downmetasettimer);
            }
        });
        if(downmeta == downzong || $("#progressBarItemdownmeta").attr('aria-valuenow') == "100%")
        {
            $("#progressBarItemdownmeta").attr('aria-valuenow', "100%").css('width', "100%");
            $("#processdownmeta").text("100%");
            clearInterval(downmetasettimer);
        }
    },1000);
    */
}

function DrawAutoDownChart(downpath, title, functiontype, goalid, danwei) {
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
            data:{"downpath":downpath, "functiontype":functiontype, "statetype":"running"},
            success:function(data)
            {
                time++;
                currentnum = parseInt(data.downfile);
                zongnum = parseInt(data.downfileall);
                var pro = (currentnum / zongnum)*100;
                $("#progressBarItemdown").attr('aria-valuenow', pro.toFixed(2) + "%").css('width', pro.toFixed(2) + "%");
                $("#processdown").text(pro.toFixed(2) + "%");
                $("#downfile_had").text(currentnum + "个");
                $("#downfile_need").text((zongnum - currentnum) + "个");
                $("#downfile_speed").text((parseInt(data.downfilespread) / (1024 * 1024)).toFixed(2)+ danwei);
                $("#downfile_time").text(timeformat(time));

                var axisData = (new Date()).toLocaleTimeString().replace(/^\D*/,'');

                var data0 = option.series[0].data;

                data0.shift();
                data0.push(parseFloat((parseInt(data.downfilespread) / (1024 * 1024)).toFixed(2)));

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
        if(currentnum == zongnum || $("#progressBarItemdown").attr('aria-valuenow') == "100%")
        {
            $("#progressBarItemdown").attr('aria-valuenow', "100%").css('width', "100%");
            $("#processdown").text("100%");
            clearInterval(settimer);
        }
    },1000);
}

function DrawAutoDownMetaChart(downpath, title, functiontype, goalid, danwei) {
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
            data:{"downpath":downpath, "functiontype":functiontype, "statetype":"running"},
            success:function(data)
            {
                time++;
                currentnum = parseInt(data.downmeta);
                zongnum = parseInt(data.downmetaall);
                var pro = (currentnum / zongnum)*100;
                $("#progressBarItemdownmeta").attr('aria-valuenow', pro.toFixed(2) + "%").css('width', pro.toFixed(2) + "%");
                $("#processdownmeta").text(pro.toFixed(2) + "%");
                $("#downfile_hadmeta").text(currentnum + "个");
                $("#downfile_needmeta").text((zongnum - currentnum) + "个");
                $("#downfile_speedmeta").text(data.downmetaspread + danwei);
                $("#downfile_timemeta").text(timeformat(time));

                var axisData = (new Date()).toLocaleTimeString().replace(/^\D*/,'');

                var data0 = option.series[0].data;

                data0.shift();
                data0.push((parseInt(data.downmetaspread)));

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
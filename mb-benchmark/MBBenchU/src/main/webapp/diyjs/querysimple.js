function createquerydir(){
    $.ajax({
        type:"post",
        url:"/makedir",
        async:true,
        data:{"dirtype":"querydir"},
        success:function(data)
        {
            if(data.result == "true"){
                new $.flavr("新传输结果文件夹创建成功");
            }
            else{
                new $.flavr("新传输结果文件夹创建失败，请先建立数据传输结果文件夹");
            }

        },
        dataType:"json",
        error:function()
        {
            new $.flavr("新负载结果文件夹创建失败，请确定已输入结果日志存储路径");
        }
    });
}

function upgrade()
{

    $("#chaxunpath_show").removeAttr("disabled");
    $("#dbname_show").removeAttr("disabled");
    $("#clname_show").removeAttr("disabled");
}

function recheck()
{

    $("#chaxunpath_show").attr("disabled", "disabled");
    $("#dbname_show").attr("disabled", "disabled");
    $("#clname_show").attr("disabled", "disabled");
}

function setFileName(){
    var dd = $("input[name=historySettings]:checked").val();
    $.ajax({
        type:"post",
        url:"/dbservlet",
        async:true,
        data:{
            "functiontype":"querySet",
            "filename":dd
        },
        success:function() {
            new $.flavr("选择成功，为" + dd + "号文件");
        },
        dataType:"json",
        error:function() {}
    });

}

function singlequery()
{
    $("#singlequery").attr("disabled","true");
    var dbname = $("#dbname_show").val();
    var clname = $("#clname_show").val();
    var downpath = $("#chaxunpath_show").val();

    $.ajax({
        type:"post",
        url:"/dbservlet",
        async:true,
        data:{
            "dbname":dbname,
            "clname":clname,
            "functiontype":"querya",
            "statetype":"init",
            "downpath":downpath
        },
        success:function() {},
        dataType:"json",
        error:function () {}
    });

    var loadsettimer = setInterval(function(){
        $.ajax({
            type:"post",
            url:"/dbservlet",
            async:true,
            data:{
                "functiontype":"querya",
                "statetype":"running"
            },
            success:function(data)
            {
                $("#workload_a_result").text(data.result);
                if(data.result == "Single attribute query has been finished")
                    clearInterval(loadsettimer);
            },
            dataType:"json",
            error:function() {}
        });

    },500);
}

function singleQuery2(){
    $("#singlequery2").attr("disabled","true");
    var dbtype = $("#dbtype").text();
    var dbname = $("#dbname_show").val();
    var clname = $("#clname_show").val();
    var downpath = $("#chaxunpath_show").val();

    $.ajax({
        type:"post",
        url:"/dbservlet",
        async:true,
        data:{
            "dbname":dbname,
            "clname":clname,
            "functiontype":"queryb",
            "statetype":"init",
            "downpath":downpath
        },
        success:function() {},
        dataType:"json",
        error:function() {}
    });

    var querybsetter = setInterval(function(){
        $.ajax({
            type:"post",
            url:"/dbservlet",
            async:true,
            data:{
                "functiontype":"queryb",
                "statetype":"running"
            },
            success:function(data)
            {
                $("#workload_b_result").text(data.result);
                if(data.result == "Multi attribute query has been finished")
                    clearInterval(querybsetter);
            },
            dataType:"json",
            error:function() {}
        });

    },500);
}

function singleQuery3(){
    $("#singlequery3").attr("disabled","true");
    var dbtype = $("#dbtype").text();

    var dbname = $("#dbname_show").val();
    var clname = $("#clname_show").val();
    var datapath = $("#datapath").val();

    $.ajax({
        type:"post",
        url:"/dbservlet",
        async:true,
        data:{
            "dbname":dbname,
            "clname":clname,
            "functiontype":"queryc",
            "statetype":"init",
            "datapath":datapath
        },
        success:function() {},
        dataType:"json",
        error:function() {}
    });

    var querybsetter = setInterval(function(){
        $.ajax({
            type:"post",
            url:"/dbservlet",
            async:true,
            data:{
                "functiontype":"queryc",
                "statetype":"running"
            },
            success:function(data)
            {
                $("#workload_c_result").text(data.result);
                if(data.result == "Version query has been finished")
                    clearInterval(querybsetter);
            },
            dataType:"json",
            error:function() {}
        });

    },500);
}
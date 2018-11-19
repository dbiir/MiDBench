function timeformat(time){
		var hour = 0;
		var mins = 0;
		var secs = 0;
		var alltime = parseInt(time);
		
		if(alltime > 3600)
			{
				hour = parseInt(alltime / 3600);
				alltime = alltime - hour*3600;
			}
		else if(parseInt(time) > 60)
			{
				mins = parseInt(alltime / 60);
				alltime = alltime - mins*60;
			}
		secs = alltime;
		
		if(hour != 0)
			{
				return hour + " 时  " + mins + " 分  " + secs +" 秒"; 
			}
		else if(mins != 0)
			{
				return mins + " 分  " + secs +" 秒"; 
			}
		else
			{
				return secs +" 秒"; 
			}
		}

function refresh()
	{
		var devicen = parseInt( $("#devicenum").val() );
		var eachfilenum = parseInt($("#eachfiles").val());
		var changenum = parseInt($("#changenum").val());
		var all = devicen * (eachfilenum + changenum) * 3;
		var avgsize = 0;
		
		var split = parseInt($("#filesizenum").val());
		for(var i = 1; i <= split; i++)
			avgsize += parseFloat($("#filesizeset" + i).val()) * parseFloat($("#filesizepresent" + i).val());
		var sizes = avgsize * all * 0.75 / (100 * 1024);
		
		$("#filesshow").html(all + "<br /><small>文件数量</small>")
		$("#sizesshow").html(sizes.toFixed(2) +"GB<br /><small>存储容量</small>")
	}

function checkhide()
{
	if($("#isused").is(":hidden"))
	{
		$("#isused").show();
		$("#notused").hide();
		$("#usedcheck").val("关闭");
		
	}
	else
	{
		$("#isused").hide();
		$("#notused").show();
		$("#usedcheck").val("打开");
	}
}

function filesizeconfirm()
{
	var split = parseInt($("#filesizenum").val());
    $("#filesizeget").html("");
	for(var j = 1; j <= split; j++)
		$("#filesizeget").append('<div class="row"><div class="col-xs-3"><div class="text-green text-center"><span>第' + j + '个设置</span></div></div><div class="col-xs-4"><input id="filesizeset'+ j +'" name="sizeset" type="text" class="form-control border-input" placeholder="单位：（MB）" /></div><div class="col-xs-4"><input id="filesizepresent'+ j +'" name="sizepresent" type="number" class="form-control border-input" placeholder="单位：（%）" /></div></div><div class="clearfix"></div>');
	$("#filesizepresent" + split).bind("focus",function(){
		var sum = 0;
		for(var i = 1; i < split; i++)
			sum = sum + parseFloat($("#filesizepresent" + i).val());
		$("#filesizepresent" + split).val(100 - sum);
	});
}

function usedrefresh()
{
	var filepath = $("#filepath").val();
	var ss = "您设置的存储路径为： "+ filepath + "<br>";
		
	$("#tabs li").each(function(){
		if($(this).attr('class') == "active")
		{
			ss += "您选中的文件名称为： " + $(this).children().html();
		}	
	});
	
	var dd = $("input[name=usedselect]:checked").val();
	if(dd == "usual")
		ss += "<br>您所选择的执行方式为： " + "继续执行";
	else
	{
		ss += "<br>您所选择的执行方式为： " + "重新设置";
		var updatenum = $("#usedupdatenum").val();
		ss += "<br>重新设置的文件数量为： " + updatenum;
	}
	new $.flavr(ss);
}

function datagen()
	{
	    $("#status").show();
		$("#btnSubmit").attr("disabled","true");
		var filepath = $("#filepath").val();
		var towerheight = $("#towerheigh").val();
		var bladelength = $("#bladelength").val();
		var bladewidth = $("#bladewidth").val();
		var changenum = parseInt($("#changenum").val());
		var devicen = parseInt($("#devicenum").val());
		var eachfilen = parseInt($("#eachfiles").val());
		var sdate = $("#simustartdate").val();
		var shour = $("#simustarthour").val();
		var smins = $("#simustartmins").val();
		var ssecs = $("#simustartsecs").val();
		var edate = $("#simuenddate").val();
		var ehour = $("#simuendhour").val();
		var emins = $("#simuendmins").val();
		var esecs = $("#simuendsecs").val();
		var filesizenum = parseInt($("#filesizenum").val());
		var filesizearray = "";
		var filesizepresentarray = "";
		alert(bladelength + ":" + bladewidth);
        for(var j = 1; j < filesizenum; j++)
		{
			filesizearray += $("#filesizeset" + j).val() + ",";
			filesizepresentarray += $("#filesizepresent" +j).val() + ",";
		}
		filesizearray += $("#filesizeset" + filesizenum).val();
		filesizepresentarray += $("#filesizepresent" + filesizenum).val();
		var message = "metatype";
		var count = 0;
		 $("input[name='metatype']:checked").each(function () {
	         message += "," + this.value;
	         count ++;
	     });
		 alert(filesizearray);
		 alert(filesizepresentarray);
		 alert(message);
		 if(count == 0){
             new $.flavr("你没有设置元数据的生成类型，请确保至少选择其中一个");
			 $("#status").hide();
			 $("#btnSubmit").removeAttr("disabled");
		 }
		 else{
			var time = 0;
			var fileall = devicen * (eachfilen + changenum) * 3;
			var unnormal = $("#unnormal").val();

			$.ajax({
				type:"post",
				url:"/newdatagen",
				async:true,
                traditional:true,
				data:
				{
					"datadir":filepath,
					"filenum":fileall,
					"devicenum":devicen,
					"eachfilen":eachfilen,
					"sdate":sdate,
					"shour":shour,
					"smins":smins,
					"ssecs":ssecs,
					"edate":edate,
					"ehour":ehour,
					"emins":emins,
					"esecs":esecs,
					"towerheight":towerheight,
					"bladelength":bladelength,
					"bladewidth":bladewidth,
					"changenum":changenum,
					"filesizearray":filesizearray,
					"metatype":message,
					"filesizepresentarray":filesizepresentarray,
					"width":0,
					"unnormal":unnormal
				},
				success:function(){

				},
				dataType:"json",	
				error:function()
				{

				}
			});
		
			var timer;
			var hadfile = 0;
			timer = setInterval(function(){ 
				$.ajax({
					type:"POST",
					url:"/newdatagen",
					async:true,
					data:{"width":1},
					success:function(data){
						if(parseInt(data.presentnum) == 0)
							{
								time = time + 1;
							}
						else
							{
								var pro = (parseInt(data.presentnum) / fileall)*100;
								$("#progressBarItem").attr('aria-valuenow', pro.toFixed(2) + "%").css('width', pro.toFixed(2) + "%");
								$("#process").text(pro.toFixed(2) + "%");
								$("#filepathshow").text(filepath);
								$("#fileallnum").text(fileall + "个");
								$("#filenum").text(data.presentnum + "个");
								$("#fileneednum").text(fileall - parseInt(data.presentnum) + "个");
								$("#filegenspeednum").text((parseInt(data.presentnum) - hadfile) + "个/秒");
								$("#filegenspeedsize").text(parseFloat(data.pretentsize).toFixed(2) + "MB/秒")
								$("#hadtime").text(timeformat(time));
								time = time + 1;
								$("#needtime").text( ( timeformat((parseInt(fileall - parseInt(data.presentnum)) / (parseInt(data.presentnum) - hadfile)).toFixed(0))));
								hadfile = parseInt(data.presentnum);
							}					
					},
					dataType:"json",	
					error:function()
					{

					}
				});
				
				if(hadfile == fileall || $("#progressBarItem").attr('aria-valuenow') == "100%")   
				{
					$("#progressBarItem").attr('aria-valuenow', "100%").css('width', "100%");
					$("#process").text("100%");
					clearInterval(timer);
				}
			},1000);	
			function stoptime()
			{
				clearInterval(timer);
			} 
		 }
		
		
	}

function useddatagen()
{
	$("#status").show();
	$("#usedbtn").attr("disabled","true");
	
	var filepath = $("#filepath").val();
	var filename = "";
	$("#tabs li").each(function(){
		if($(this).attr('class') == "active")
		{
			filename = $(this).children().html();
		}	
	});
	var gentype = $("input[name=usedselect]:checked").val();
	
	var timeuse = 0;
	var message = "metatype";
	var count = 0;
	 $("input[name='metatype']:checked").each(function () {
         message += "," + this.value;
         count ++;
     });
	 if(count == 0){
         new $.flavr("你没有设置元数据的生成类型，请确保至少选择其中一个");
		 $("#status").hide();
		 $("#btnSubmit").removeAttr("disabled");
	 }
	 else{
			if(gentype == "usual")
			{
				$.ajax({
					type:"post",
					url:"/usedgen",
					async:true,
					traditional:true,
					data:
					{
						"filepath":filepath,
						"filename":filename,
						"type":"usual",
						"metatype":message,
						"width":0
					},
					success:function(){

					},
					dataType:"json",	
					error:function()
					{
						
					}
				});
			}
			else
			{
				var filenum = parseInt($("#usedupdatenum").val());
				$.ajax({
					type:"post",
					url:"/usedgen",
					async:true,
					traditional:true,
					data:
					{
						"filenum":filenum,
						"filepath":filepath,
						"filename":filename,
						"type":"ee",
						"metatype":message,
						"width":0
					},
					success:function(){

					},
					dataType:"json",	
					error:function()
					{
						
					}
				});
			}
		
			var timerusual;
			var hadfile = 0;
			timerusual = setInterval(function(){ 
				$.ajax({
					type:"POST",
					url:"/usedgen",
					async:true,
					data:{"width":1},
					success:function(data){
						if(parseInt(data.presentnum) == 0)
							{
								timeuse = timeuse + 1;
							}
						else
							{
								var allnum = parseInt(data.allnum);
								var pro = (parseInt(data.presentnum) / allnum) * 100;
								$("#progressBarItem").attr('aria-valuenow', pro.toFixed(2) + "%").css('width', pro.toFixed(2) + "%");
								$("#process").text(pro.toFixed(2) + "%");
								$("#filepathshow").text(filepath);
								$("#fileallnum").text(allnum + "个");
								$("#filenum").text(data.presentnum + "个");
								$("#fileneednum").text(allnum - parseInt(data.presentnum) + "个");
								$("#filegenspeednum").text((parseInt(data.presentnum) - hadfile) + "个/秒");
								$("#filegenspeedsize").text(parseFloat(data.pretentsize).toFixed(2) + "MB/秒")
								$("#hadtime").text(timeformat(timeuse));
								timeuse = timeuse + 1;
								$("#needtime").text( (timeformat((parseInt(allnum - parseInt(data.presentnum)) / (parseInt(data.presentnum) - hadfile)).toFixed(0))));
								hadfile = parseInt(data.presentnum);
							}					
					},
					dataType:"json",	
					error:function()
					{

					}
				});
					
				if(hadfile == allnum || $("#progressBarItem").attr('aria-valuenow') == "100%")   
				{
					$("#progressBarItem").attr('aria-valuenow', "100%").css('width', "100%");
					$("#process").text("100%");
					clearInterval(timerusual);
				}
			},1000);
	 }
	
}

function testcheck(){
	alert("Hello");
	var message = "metatype";
	var count = 0;
	 $("input[name='metatype']:checked").each(function () {
         message += "," + this.value;
         count ++;
     });
	 if(count == 0){
		 $.notify({
	       icon: 'ti-bell',
	       message: "你没有设置元数据的生成类型，请确保至少选择其中一个"
	       },{
	          type: 'danger',
	          timer: 4000
	       });
	 }
	 else{
		 alert(message);
	 }
	 
}
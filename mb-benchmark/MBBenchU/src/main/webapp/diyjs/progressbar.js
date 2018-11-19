function timeformat(time){
		var hour = 0;
		var mins = 0;
		var secs = 0;
		var alltime = parseInt(time);
		
		if(alltime >= 3600)
		{
			hour = parseInt(alltime / 3600);
			alltime = alltime - hour*3600;
		}
		if(parseInt(time) >= 60)
		{
			mins = parseInt(alltime / 60);
			alltime = alltime - mins*60;
		}
		secs = alltime;
	
		if(hour != 0)
			{
				return hour + " 时 " + mins + " 分 " + secs +"秒"; 
			}
		else if(mins != 0)
			{
				return mins + " 分" + secs +"秒"; 
			}
		else
			{
				return secs +"秒"; 
			}
		}
	
function refresh()
	{
		var fileload = parseInt( $("#fileload").val() );
		var devicen = parseInt( $("#devicenum").val() );
		var fileup = parseInt( $("#fileup").val() );
		var filechan = parseInt( $("#filechan").val() ); 
		var filesize = parseInt($("#filesize").val() );
		
		var loadall = fileload * devicen;
		var upall = fileup * devicen;
		var chanall = filechan * devicen;
		var all = loadall + upall + chanall;			
		var size = (all * filesize) / 2048;
		
		$("#filesshow").html(all+"<br /><small>文件数量</small>")
		$("#sizesshow").html(size.toFixed(2)+"GB<br /><small>存储空间</small>");
	}
	
	$(function(){
		$("#filechan").blur(function(){
        	var fileload = parseInt( $("#fileload").val() );
			var devicen = parseInt( $("#devicenum").val() );
			var fileup = parseInt( $("#fileup").val() );
			var filechan = parseInt( $("#filechan").val() ); 
			var filesize = parseInt($("#filesize").val() );
			
			var loadall = fileload * devicen;
			var upall = fileup * devicen;
			var chanall = filechan * devicen;
			var all = loadall + upall + chanall;			
			var size = (all * filesize) / 2048;
		
			$("#filesshow").html(all+"<br /><small>文件数量</small>")
			$("#sizesshow").html(size.toFixed(2)+"GB<br /><small>存储空间</small>");
        });
		$("#btnSubmit").click(function(){
			$("#status").show();
			$("#btnSubmit").attr("disabled","true");
			var filepath = $("#filepath").val();
			var fileload = parseInt( $("#fileload").val() );
			var devicen = parseInt( $("#devicenum").val() );
			var fileup = parseInt( $("#fileup").val() );
			var filechan = parseInt( $("#filechan").val() );
			var filesize = parseInt($("#filesize").val() );
			var unnormal = $("#unnormal").val();
			var hadfile = 0;
			var time = 0;
			var loadall = fileload * devicen;
			
			var zong = loadall + fileup * devicen + filechan * devicen;
			
			var val = parseInt($("#progressBarItem").width());		
			$.ajax({
				type:"POST",
				url:"/GHBigBench/datagen",
				async:true,
				data:{"datadir":filepath, 
					"filenum":loadall, 
					"devicenum":devicen, 
					"fileupcount":fileup,
					"filechancount":filechan, 
					"width":0, 
					"filesize":filesize,
					"unnormal":unnormal
					},
				success:function(){				
				},
				dataType:"json",	
				error:function()
				{
					$.notify({
        				icon: 'ti-bell',
        				message: "文件生成设置有误"
        			},{
            			type: 'danger',
            			timer: 4000
        			});
				}
			});			
			var timer;
			timer = setInterval(function(){ 
				$.ajax({
					type:"POST",
					url:"/MBBench-U/datagen",
					async:true,
					data:{"width":1},
					success:function(data){
						if( parseInt(data.allnum) == 0)
							{
								time = time + 1;
							}
						else
							{
								var pro = (parseInt(data.allnum) / zong)*100;
								$("#progressBarItem").attr('aria-valuenow', pro.toFixed(2) + "%").css('width', pro.toFixed(2) + "%");
								$("#process").text(pro.toFixed(2) + "%");
								$("#filepathshow").text(filepath);
								$("#fileloadnum").text(data.loadfile);
								$("#fileupnum").text(data.upfile);
								$("#filechannum").text(data.changefile);
								$("#fileallnum").text(data.allnum);
								$("#fileneednum").text(zong - parseInt(data.allnum));
								$("#filegenspeed").text( (parseInt(data.allnum) - hadfile) + "MB/秒");
								$("#hadtime").text( timeformat(time));
								time = time + 1;
								$("#needtime").text( ( timeformat( ( parseInt(zong - parseInt(data.allnum)) / (parseInt(data.allnum) - hadfile) ).toFixed(0) ) ));
								hadfile = parseInt(data.allnum);
							}					
					},
					dataType:"json",	
					error:function()
					{
						alert("not success!");
					}
				});

				if( hadfile == zong || $("#progressBarItem").attr('aria-valuenow') == "100%")   
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
		});
	});
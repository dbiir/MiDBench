	
var Init = { 
    //web弹出框样式
    Utility: {
        WebToast: "<div id=\"webToast\">"
                    + "<div class=\"web_transparent\"></div>"
                    + "<div class=\"web-toast\">"
                        + "<div class=\"sk-spinner sk-spinner-three-bounce\">"
                            + "<div class=\"sk-bounce1\"></div>"
                            + "<div class=\"sk-bounce2\"></div>"
                            + "<div class=\"sk-bounce3\"></div>"
                        + "</div>"
                        + "<p class=\"web-toast_content\">数据加载中</p>"
                    + "</div>"
                + "</div>",
    }, 
    //web Toast
    WebToast: function (aContent) {
        var me = Init;
        try {
            $("body").append(me.Utility.WebToast);
            var w = $(window).width();
            var aW = $(".web-toast").width();
            var left = (w - aW) / 2;
            $(".web-toast").css("left", left + "px");
            $(".web-toast_content").text(aContent);
        }
        catch (e) {; }
    },  
    //clear Toast, set time
    ClearToast: function (aElement, aTimeOut) {
        var me = Init;
        try {
            setTimeout(function () {
                $(aElement).remove();
            }, aTimeOut * 1000);
        }
        catch (e) {; }
    }, 

} 
	
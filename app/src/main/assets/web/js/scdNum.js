function init() {
    var a = JSON.parse(rfid.findNum(null));
    var o = dat.getUrlReq();
    if (a.length) {
        var i, d;
        var u = "qry.html?typ=" + o.typ + "&min=" + o.min + "&max=" + o.max;
        d = document.createElement("a");
        d.href = u;
        d.innerHTML = "全部车号";
        boso.appendChild(d);
        for (i = 0; i < a.length; i ++) {
            d = document.createElement("a");
            d.href = u + "&num=" + a[i];
            d.innerHTML = a[i];
            boso.appendChild(d);
        }
    } else {
        boso.innerHTML = "<br><span>暂无车号</span>";
    }
}

dat = {
    getUrlReq: function () {
		var url = location.search;
		var theRequest = {};
		if (url.indexOf("?") != -1) {
			url = url.substr(1).split("&");
			for(var i = 0; i < url.length; i ++) {
				var str = url[i].split("=");
				theRequest[str[0]] = decodeURIComponent(str[1]);
			}
		}
		return theRequest;
    }
};

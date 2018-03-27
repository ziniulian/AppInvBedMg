function init() {
	var o = dat.getUrlReq();
	var s;
	switch (o.typ - 0) {
		case 1:
			s = "小单明细：<br>";
			break;
		case 2:
			s = "被套明细：<br>";
			break;
		case 3:
			s = "枕套明细：<br>";
			break;
		default:
			return;
	}
	var a = JSON.parse(rfid.findDetails(o.id, o.typ, dat.detailNam(o.id * 1000)));
	if (a.length) {
		s += "<table><tbody><tr><td>序列号</td><td>洗涤次数</td><td>车号</td></tr>";
		for (i = 0; i < a.length; i ++) {
			s += "<tr><td>";
			s += dat.formatSn(a[i][0]);
			s += "</td><td>";
			s += a[i][1];
			s += "</td><td>";
			s += a[i][2];
			s += "</td></tr>";
		}
	} else {
		s += "(暂无记录)";
	}
	boso.innerHTML = s;
}

dat = {
	snSize: 6,

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
	},

	// 获取明细表名
	detailNam: function (tim) {
		var t;
		t = new Date(tim);
		var y = t.getFullYear();
		var m = t.getMonth() + 1;
		if (m < 10) {
			m = "0" + m;
		}
		var d = t.getDate();
		if (d < 10) {
			d = "0" + d;
		}
		return "" + y + m + d;
	},

	// 格式化序列号
	formatSn: function (sn) {
		var r = sn + "";
		var i, n;
		if (r.length < dat.snSize) {
			n = dat.snSize - r.length;
			r = "";
			for (i = 0; i < n; i ++) {
				r += "0";
			}
			r += sn;
		}
		return r;
	}

};

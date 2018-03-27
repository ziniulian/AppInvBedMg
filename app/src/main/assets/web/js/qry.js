function init() {
	var o = dat.getUrlReq();
	if (o.min) {
		dat.min = o.min - 0;
		timmin.innerHTML = dat.timD(new Date(o.min * 1000));
	} else {
		timmin.innerHTML = dat.timD(new Date());
		dat.min = Math.floor(Date.parse(timmin.innerHTML + " 0:0:0") / 1000);
	}

	if (o.max) {
		dat.max = o.max - 0;
		timmax.innerHTML = dat.timD(new Date(o.max * 1000));
	} else {
		timmax.innerHTML = dat.timD(new Date());
		dat.max = Math.floor(Date.parse(timmax.innerHTML + " 23:59:59") / 1000);
	}

	if (o.num) {
		dat.setNum(o.num);
	}
	if (o.typ) {
		dat.setTyp(o.typ - 0);
	}
	dat.flush();
}

dat = {
	ts: null,
	num: null,
	min: null,
	max: null,
	typ: 0,	 // 0:全部,1:小单,2:被套,3:枕套

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

	timStr: function (tim) {
		var t;
		t = new Date(tim);
		var y = t.getFullYear() + "";
		var m = t.getMonth() + 1;
		// if (m < 10) {
		//	 m = "0" + m;
		// }
		var d = t.getDate();
		// if (d < 10) {
		//	 d = "0" + d;
		// }
		var h = t.getHours();
		if (h < 10) {
			h = "0" + h;
		}
		var u = t.getMinutes();
		if (u < 10) {
			u = "0" + u;
		}
		// var s = t.getSeconds();
		// if (s < 10) {
		//	 s = "0" + s;
		// }
		// return y + "-" + m + "-" + d + " " + h + ":" + u + ":" + s;
		// return y + m + d + h + u + s;
		return y + "年" + m + "月" + d + "日<br>" + h + ":" + u;
	},

	timD: function (t) {
		var y = t.getFullYear();
		var m = t.getMonth() + 1;
		// if (m < 10) {
		//	 m = "0" + m;
		// }
		var d = t.getDate();
		// if (d < 10) {
		//	 d = "0" + d;
		// }
		return y + "-" + m + "-" + d;
	},

	setNum: function (n) {
		if (n !== dat.num) {
			dat.num = n;
			if (n) {
				num.innerHTML = n;
				num.className = "midSub mfs";
			} else {
				num.innerHTML = "点此选择车号";
				num.className = "midSub mfs numall";
			}
		}
	},

	setTyp: function (t) {
		if (t !== dat.typ) {
			var d = document.getElementById("typDom" + dat.typ);
			d.className = "midSub typ mfs";
			dat.typ = t;
			d = document.getElementById("typDom" + t);
			d.className = "midSub typ mfs typscd";
		}
	},

	chgTyp: function (t) {
		if (t !== dat.typ) {
			dat.typ = t;
			dat.qry();
		}
	},

	datePicker: function (min) {
		var s = min ? timmin.innerHTML : timmax.innerHTML;
		var a = s.split("-");
		rfid.showDatePicker(a[0] - 0, a[1] - 1, a[2] - 0, min);

		// var d = min ? dat.min : dat.max;
		// d = new Date (d * 1000);
		// rfid.showDatePicker(d.getFullYear(), d.getMonth(), d.getDate(), min);
	},

	chgDate: function (min, strd) {
		var d = Math.floor(Date.parse(strd + " 0:0:0") / 1000);
		if (!isNaN(d)) {
			if (min) {
				dat.min = d;
			} else {
				dat.max = d;
			}
			dat.qry();
		}
	},

	today: function () {
		var u = "qry.html?typ=" + dat.typ;
		if (dat.num) {
			u += "&num=" + dat.num;
		}
		location.href = u;
	},

	chgNum: function () {
		var u = "scdNum.html?min=" + dat.min + "&max=" + dat.max + "&typ=" + dat.typ;
		location.href = u;
	},

	qry: function () {
		var u = "qry.html?min=" + dat.min + "&max=" + dat.max + "&typ=" + dat.typ;
		if (dat.num) {
			u += "&num=" + dat.num;
		}
		location.href = u;
	},

	flush: function () {
		var a = JSON.parse(rfid.qry(dat.num, dat.min, dat.max));
		dat.ts = a;
		if (a.length) {
			rom.innerHTML = "";
			var i = a.length - 1;
			var cc = i % 2;
			var count = [0, 0, 0, 0];
			for (; i >= 0; i --) {
				var o = document.createElement("div");
				if (i % 2 !== cc) {
					o.className = "lineBg";
				}

				var d = document.createElement("div");
				d.className = "rowSub ktw";
				d.innerHTML = dat.timStr(a[i][0] * 1000);
				o.appendChild(d);

				d = document.createElement("div");
				d.className = "rowSub knw";
				d.innerHTML = a[i][1];
				o.appendChild(d);

				d = document.createElement("div");
				d.className = "rowSub kpw";
				var s = "<a href='details.html?id=" + a[i][0];
				switch (dat.typ) {
					case 0:
						d.innerHTML = s + "&typ=1'>小单</a><br>" + s + "&typ=2'>被套</a><br>" + s + "&typ=3'>枕套";
						break;
					case 1:
						d.innerHTML = s + "&typ=1'>小单</a>";
						break;
					case 2:
						d.innerHTML = s + "&typ=2'>被套</a>";
						break;
					case 3:
						d.innerHTML = s + "&typ=3'>枕套</a>";
						break;
				}
				o.appendChild(d);

				d = document.createElement("div");
				d.className = "rowSub kcw";
				if (dat.typ) {
					d.innerHTML = a[i][dat.typ + 1];
					count[dat.typ] += a[i][dat.typ + 1];
				} else {
					d.innerHTML = a[i][2] + "<br>" + a[i][3] + "<br>" + a[i][4];
					count[1] += a[i][2];
					count[2] += a[i][3];
					count[3] += a[i][4];
				}
				o.appendChild(d);

				rom.appendChild(o);
			}

			// 显示总计
			if (dat.typ) {
				total.innerHTML += count[dat.typ];
			} else {
				total.innerHTML += (count[1] + count[2] + count[3]);
			}
		} else {
			rom.innerHTML = "<p>没有符合条件的信息 ...</p>";
		}
	}

};

function init() {
}

dat = {
	// 还原
	restore: function () {
		exit.className = "Lc_nosee";
		document.title = "Home";
	},

	// 退出页面
	back: function () {
		document.title = "Exit";
		exit.className = "exit sfs";
		setTimeout(dat.restore, 2000);
	}
};
